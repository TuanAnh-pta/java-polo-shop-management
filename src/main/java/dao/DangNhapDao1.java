package dao;

import entity.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import unti.JDBCUtil;
import unti.XJdbc;
import unti.XJdbc;

public class DangNhapDao1 {

    // Kiểm tra đăng nhập
    public static boolean checkLogin(String user, String pass) {
        String sql = """
            SELECT 1
            FROM NhanVien nv
            JOIN DangNhap dn ON nv.MaNhanVien = dn.MaNhanVien
            WHERE dn.TaiKhoan = ? AND dn.MatKhau = ?
        """;
        try {
            ResultSet rs = XJdbc.executeQuery(sql, user, pass);
            boolean found = rs.next();
            rs.getStatement().getConnection().close();
            return found;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

public boolean capNhatMatKhau(String taiKhoan, String matKhauMoi) {
    String sql = "UPDATE DangNhap SET MatKhau = ? WHERE TaiKhoan = ?";
    try {
        int updated = XJdbc.executeUpdate(sql, matKhauMoi, taiKhoan);
        return updated > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


    // Lấy thông tin NhanVien theo username, bao gồm MaChucVu để phân quyền
    public NhanVien getNhanVienByUsername(String username) {
        NhanVien nv = null;
        String sql = """
            SELECT nv.MaNhanVien, nv.hoTen, dn.MaChucVu
            FROM NhanVien nv
            JOIN DangNhap dn ON nv.MaNhanVien = dn.MaNhanVien
            WHERE dn.TaiKhoan = ?
        """;
        try {
            ResultSet rs = XJdbc.executeQuery(sql, username);
            if (rs.next()) {
                nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("MaNhanVien"));
                nv.setHoTen(rs.getString("hoTen"));
                nv.setMaChucVu(rs.getString("MaChucVu")); // Thêm phân quyền
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nv;
    }
}
