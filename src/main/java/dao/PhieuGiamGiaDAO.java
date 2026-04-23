package dao;

import entity.PhieuGiamGia;
import unti.XJdbc;
import unti.XQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PhieuGiamGiaDAO {

    String sql = "SELECT maPhieu, tenPhieu, tenHinhThucGG, giaTri, soLuong, ngayBatDau, ngayKetThuc, dieuKienApDung, trangThai FROM PhieuGiamGia;";
    String create_SQL = "INSERT INTO PhieuGiamGia(maPhieu, tenPhieu, tenHinhThucGG, giaTri, soLuong, ngayBatDau, ngayKetThuc, dieuKienApDung, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    String update_SQL = "UPDATE PhieuGiamGia SET tenPhieu=?, tenHinhThucGG=?, giaTri=?, soLuong=?, ngayBatDau=?, ngayKetThuc=?, dieuKienApDung=?, trangThai=? WHERE maPhieu=?";
    String update_status_SQL = "UPDATE PhieuGiamGia SET trangThai = 0 WHERE maPhieu = ?";

    public List<PhieuGiamGia> getAll() {
        return XQuery.getBeanList(PhieuGiamGia.class, sql);
    }

    public PhieuGiamGia create(PhieuGiamGia p) {
        Object[] values = {
            p.getMaPhieu(),
            p.getTenPhieu(),
            p.getTenHinhThucGG(),
            p.getGiaTri(),
            p.getSoLuong(),
            p.getNgayBatDau(),
            p.getNgayKetThuc(),
            p.getDieuKienApDung(),
            p.isTrangThai()
        };
        XJdbc.executeUpdate(create_SQL, values);
        return p;
    }

    public PhieuGiamGia update(PhieuGiamGia p) {
        Object[] values = {
            p.getTenPhieu(),
            p.getTenHinhThucGG(),
            p.getGiaTri(),
            p.getSoLuong(),
            p.getNgayBatDau(),
            p.getNgayKetThuc(),
            p.getDieuKienApDung(),
            p.isTrangThai(),
            p.getMaPhieu() // dùng maPhieu để WHERE
        };
        XJdbc.executeUpdate(update_SQL, values);
        return p;
    }

    public PhieuGiamGia delete(PhieuGiamGia p) {
        Object[] values = {
            p.getMaPhieu()
        };
        XJdbc.executeUpdate(update_status_SQL, values);
        return p;
    }

    public boolean isMaPhieuExists(String maPhieu) {
        String sql = "SELECT COUNT(*) FROM PhieuGiamGia WHERE maPhieu = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, maPhieu)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteByMaPhieu(String maPhieu) {
//        String sql = "DELETE FROM PhieuGiamGia WHERE maPhieu = ?";
//        XJdbc.executeUpdate(sql, maPhieu);
        try {
            // 1) Gỡ liên kết khỏi hóa đơn trước
            String sqlClearHD = "UPDATE HoaDon SET MaPhieu = NULL, giaTri = 0, TienSauGiam = TongTien WHERE MaPhieu = ?";
            XJdbc.executeUpdate(sqlClearHD, maPhieu);

            // 2) Xóa phiếu
            String sqlDelete = "DELETE FROM PhieuGiamGia WHERE maPhieu = ?";
            int rows = XJdbc.executeUpdate(sqlDelete, maPhieu);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PhieuGiamGia> getAllActive() {
        String sql = "SELECT * FROM PhieuGiamGia WHERE trangThai = 1";
        return XQuery.getBeanList(PhieuGiamGia.class, sql);
    }

    public String getLastMaPhieu() {
        String sql = "SELECT MAX(maPhieu) FROM PhieuGiamGia";
        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
