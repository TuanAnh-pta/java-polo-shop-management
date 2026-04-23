package dao;

import java.sql.*;
import unti.XJdbc;

public class NhanVien_KhoaHDDao {

    // Kiểm tra nhân viên đã tham gia khóa chưa
    public boolean isDaThamGia(String maNV, String maKhoaHD) {
        String sql = "SELECT COUNT(*) FROM NhanVien_KhoaHD WHERE MaNV = ? AND MaKhoaHD = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, maNV, maKhoaHD)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm nhân viên vào khóa huấn luyện (mặc định TrangThai = 0, NgayThamGia = GETDATE())
    public int themNhanVienKhoaHD(String maNV, String maKhoaHD) {
        String sql = "INSERT INTO NhanVien_KhoaHD(MaNV, MaKhoaHD) VALUES (?, ?)";
        return XJdbc.executeUpdate(sql, maNV, maKhoaHD);
    }

    // Cập nhật trạng thái hoàn thành khóa
    public int capNhatHoanThanh(String maNV, String maKhoaHD) {
        String sql = "UPDATE NhanVien_KhoaHD SET TrangThai = 1 WHERE MaNV = ? AND MaKhoaHD = ?";
        return XJdbc.executeUpdate(sql, maNV, maKhoaHD);
    }

    // Lấy trạng thái tham gia của nhân viên với khóa
    public Boolean getTrangThai(String maNV, String maKhoaHD) {
        String sql = "SELECT TrangThai FROM NhanVien_KhoaHD WHERE MaNV = ? AND MaKhoaHD = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, maNV, maKhoaHD)) {
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // chưa tham gia
    }

}
