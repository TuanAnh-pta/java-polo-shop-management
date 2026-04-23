package dao;

import entity.HoaDon;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import unti.JDBCUtil;
import unti.XJdbc;
import unti.XQuery;

public class HoaDonDao {

    String getAllSql = """
    SELECT hd.MaHoaDon,
           nv.hoTen,
           kh.TenKhachHang,
           hd.NgayTao,
           hd.TrangThai
    FROM HoaDon hd
    JOIN NhanVien nv ON hd.MaNhanVien = nv.MaNhanVien
    JOIN KhachHang kh ON hd.MaKhachHang = kh.MaKhachHang
""";

    public List<HoaDon> getAll() {
        return XQuery.getBeanList(HoaDon.class, getAllSql);
    }

    public boolean add(HoaDon hd) {
        String sql = """
           INSERT INTO [dbo].[HoaDon]
                       ([MaHoaDon]
                       ,[MaKhachHang]
                       ,[MaNhanVien]
                       ,[TongTien]
                       ,[TrangThai]
                       ,[SoDienThoai]
                       ,[Email]
                       ,[TenKhachHang]
                       ,[NgayTao])
                 VALUES
                      (?,?,?,?,?,?,?,?,?)
            """;

        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Đảm bảo TongTien không null
            if (hd.getTongTien() == null) {
                hd.setTongTien(BigDecimal.ZERO);
            }

            ps.setString(1, hd.getMaHoaDon());      // MaHoaDon
            ps.setString(2, hd.getMaKhachHang());   // MaKhachHang
            ps.setString(3, hd.getMaNhanVien());    // MaNhanVien
            ps.setBigDecimal(4, hd.getTongTien());  // TongTien
            ps.setBoolean(5, hd.isTrangThai());     // TrangThai
            ps.setString(6, hd.getSoDienThoai());   // SoDienThoai
            ps.setString(7, hd.getEmail());         // Email
            ps.setString(8, hd.getTenKhachHang());  // TenKhachHang
            ps.setDate(9, hd.getNgayTao());         // NgayTao

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNewMaHoaDon() {
        String maMoi = "HD0001";
        try {
            String sql = "SELECT TOP 1 MaHoaDon FROM HoaDon ORDER BY MaHoaDon DESC";
            ResultSet rs = XJdbc.executeQuery(sql);
            if (rs.next()) {
                String maCu = rs.getString("MaHoaDon"); // VD: HD0005
                int so = Integer.parseInt(maCu.substring(2)) + 1;
                maMoi = String.format("HD%04d", so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maMoi;
    }

    // Trong KhachHangDao
    public String getNewMaKhachHang() {
        String maMoi = "KH0001";
        try {
            String sql = "SELECT TOP 1 MaKhachHang FROM KhachHang ORDER BY MaKhachHang DESC";
            ResultSet rs = XJdbc.executeQuery(sql);
            if (rs.next()) {
                String maCu = rs.getString("MaKhachHang");
                int so = Integer.parseInt(maCu.substring(2)) + 1;
                maMoi = String.format("KH%04d", so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maMoi;
    }

    public boolean deleteHoaDon(String maHoaDon) {
        try {
            // Xóa chi tiết hóa đơn
            String sqlCT = "DELETE FROM ChiTietHoaDon WHERE MaHoaDon = ?";
            XJdbc.executeUpdate(sqlCT, maHoaDon);

            // Xóa đợt giảm giá áp dụng cho hóa đơn (nếu có)
            String sqlGG = "DELETE FROM DotGiamGia_HoaDon WHERE MaHoaDon = ?";
            XJdbc.executeUpdate(sqlGG, maHoaDon);

            // Xóa hóa đơn
            String sqlHD = "DELETE FROM HoaDon WHERE MaHoaDon = ?";
            int rows = XJdbc.executeUpdate(sqlHD, maHoaDon);

            return rows > 0;  // true nếu đã xóa
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDaThanhToan(String maHoaDon) {
        String sql = "SELECT TrangThai FROM HoaDon WHERE MaHoaDon = ?";
        try {
            Boolean trangThai = (Boolean) XJdbc.getValue(sql, maHoaDon);
            return trangThai != null && trangThai; // true = Đã thanh toán
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Mặc định là chưa thanh toán nếu có lỗi
    }

    public boolean hoaDonChuaCoSanPham(String maHoaDon) {
        String sql = "SELECT COUNT(*) FROM ChiTietHoaDon WHERE MaHoaDon = ?";
        Integer count = XJdbc.getValue(sql, maHoaDon);
        return count != null && count == 0;
    }

    public boolean thanhToanDonHang(String maHoaDon, String maKhachHang, String hinhThucTT,
            String tenKhachHang, String maNV, String maKhuyenMai) {
        String sql = "UPDATE HoaDon SET TrangThai = ?, MaKhachHang = ?, HinhThucThanhToan = ?, "
                + "TenKhachHang = ?, MaNhanVien = ?, MaKhuyenMai = ?, NgayThanhToan = GETDATE() "
                + "WHERE MaHoaDon = ?";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Nếu TrangThai là boolean thì dùng ps.setBoolean(1, true);
            ps.setString(1, "Đã thanh toán");
            ps.setString(2, maKhachHang);
            ps.setString(3, hinhThucTT);
            ps.setString(4, tenKhachHang);
            ps.setString(5, maNV);
            ps.setString(6, maKhuyenMai);
            ps.setString(7, maHoaDon);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTrangThai(String maHoaDon, boolean trangThai) {
        String sql = "UPDATE HoaDon SET TrangThai = ? WHERE MaHoaDon = ?";
        try {
            int rows = XJdbc.executeUpdate(sql, trangThai, maHoaDon);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTrangThaiByMaHoaDon(String maHoaDon) {
        String trangThai = "";
        String sql = "SELECT TrangThai FROM HoaDon WHERE MaHoaDon = ?";

        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                trangThai = rs.getString("TrangThai");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trangThai;
    }

}
