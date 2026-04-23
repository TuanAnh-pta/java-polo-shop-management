package dao;

import entity.LichLamViec;
import java.sql.*;
import java.util.ArrayList;
import unti.XJdbc;

public class LichLamViecDao {

    // Lấy tất cả lịch làm việc + họ tên nhân viên
    public ArrayList<LichLamViec> getAll() {
        ArrayList<LichLamViec> list = new ArrayList<>();

        String sql = """
            SELECT llv.MaLLV, llv.MaNhanVien, nv.hoTen, 
                   llv.NgayLam, llv.CaLam, llv.GhiChu
            FROM LichLamViec llv
            JOIN NhanVien nv ON llv.MaNhanVien = nv.MaNhanVien
        """;

        try (ResultSet rs = XJdbc.executeQuery(sql)) {

            while (rs.next()) {
                LichLamViec llv = new LichLamViec(
                        rs.getString(1), // MaLLV
                        rs.getString(2), // MaNhanVien
                        rs.getString(3), // hoTen từ bảng NhanVien
                        rs.getDate(4),   // NgayLam
                        rs.getString(5), // CaLam
                        rs.getString(6)  // GhiChu
                );
                list.add(llv);
            }

            rs.getStatement().getConnection().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm mới (không có cột hoTen)
    public int add(LichLamViec llv) {
        String sql = """
            INSERT INTO LichLamViec(MaLLV, MaNhanVien, NgayLam, CaLam, GhiChu)
            VALUES (?, ?, ?, ?, ?)
        """;
        return XJdbc.executeUpdate(sql,
                llv.getMaLLV(),
                llv.getMaNhanVien(),
                llv.getNgayLam(),
                llv.getCaLam(),
                llv.getGhiChu()
        );
    }

    // Cập nhật (không update hoTen)
    public int update(LichLamViec llv) {
        String sql = """
            UPDATE LichLamViec 
            SET MaNhanVien = ?, NgayLam = ?, CaLam = ?, GhiChu = ?
            WHERE MaLLV = ?
        """;
        return XJdbc.executeUpdate(sql,
                llv.getMaNhanVien(),
                llv.getNgayLam(),
                llv.getCaLam(),
                llv.getGhiChu(),
                llv.getMaLLV()
        );
    }

    // Xóa
    public boolean delete(String maLLV) {
        String sql = "DELETE FROM LichLamViec WHERE MaLLV = ?";
        return XJdbc.executeUpdate(sql, maLLV) > 0;
    }

    // Tìm kiếm theo mã nhân viên hoặc ngày làm + JOIN để lấy họ tên
    public ArrayList<LichLamViec> search(String keyword) {
        ArrayList<LichLamViec> list = new ArrayList<>();

        String sql = """
            SELECT llv.MaLLV, llv.MaNhanVien, nv.hoTen,
                   llv.NgayLam, llv.CaLam, llv.GhiChu
            FROM LichLamViec llv
            JOIN NhanVien nv ON llv.MaNhanVien = nv.MaNhanVien
            WHERE llv.MaNhanVien LIKE ? 
               OR CONVERT(VARCHAR, llv.NgayLam, 23) LIKE ?
        """;

        try (ResultSet rs = XJdbc.executeQuery(sql, 
                "%" + keyword + "%", "%" + keyword + "%")) {

            while (rs.next()) {
                LichLamViec llv = new LichLamViec(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getString(6)
                );
                list.add(llv);
            }

            rs.getStatement().getConnection().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tạo mã LLV tự động
    public String getNextMaLLV() {
        int maxNumber = 0;
        int maxDigits = 0;

        String sql = "SELECT MaLLV FROM LichLamViec";

        try (ResultSet rs = XJdbc.executeQuery(sql)) {

            while (rs.next()) {
                String ma = rs.getString("MaLLV");
                if (ma != null && ma.startsWith("LLV")) {
                    String numPart = ma.substring(3);
                    String digits = numPart.replaceAll("\\D", "");
                    if (!digits.isEmpty()) {
                        maxDigits = Math.max(maxDigits, digits.length());
                        int val = Integer.parseInt(digits);
                        if (val > maxNumber) maxNumber = val;
                    }
                }
            }

            rs.getStatement().getConnection().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (maxDigits == 0) maxDigits = 5;

        return "LLV" + String.format("%0" + maxDigits + "d", maxNumber + 1);
    }

    // ===========================
    // Cập nhật GhiChu (thông báo từ quản lý)
    // ===========================
    public int capNhatGhiChu(String maLLV, String ghiChuMoi) {
        String sql = "UPDATE LichLamViec SET GhiChu = ? WHERE MaLLV = ?";
        return XJdbc.executeUpdate(sql, ghiChuMoi, maLLV);
    }
}
