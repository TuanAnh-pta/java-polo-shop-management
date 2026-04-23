package dao;

import entity.KhoaHuongDan;
import java.sql.*;
import java.util.ArrayList;
import unti.XJdbc;

public class KhoaHuongDanDao {

    // Lấy tất cả khóa huấn luyện
    public ArrayList<KhoaHuongDan> getAll() {
        ArrayList<KhoaHuongDan> list = new ArrayList<>();
        String sql = "SELECT MaKhoaHD, TieuDe, MoTa, NgayBatDau, NgayKetThuc, TrangThai FROM KhoaHuongDan";

        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            while (rs.next()) {
                KhoaHuongDan kh = new KhoaHuongDan(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getDate(5),
                        rs.getBoolean(6)
                );
                list.add(kh);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm mới
    public int add(KhoaHuongDan kh) {
        String sql = "INSERT INTO KhoaHuongDan(MaKhoaHD, TieuDe, MoTa, NgayBatDau, NgayKetThuc, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        return XJdbc.executeUpdate(sql,
                kh.getMaKhoaHD(),
                kh.getTieuDe(),
                kh.getMoTa(),
                kh.getNgayBatDau(),
                kh.getNgayKetThuc(),
                kh.isTrangThai()
        );
    }

    // Cập nhật
    public int update(KhoaHuongDan kh) {
        String sql = "UPDATE KhoaHuongDan SET TieuDe = ?, MoTa = ?, NgayBatDau = ?, NgayKetThuc = ?, TrangThai = ? WHERE MaKhoaHD = ?";
        return XJdbc.executeUpdate(sql,
                kh.getTieuDe(),
                kh.getMoTa(),
                kh.getNgayBatDau(),
                kh.getNgayKetThuc(),
                kh.isTrangThai(),
                kh.getMaKhoaHD()
        );
    }

    // Xóa
    public boolean delete(String maKhoaHD) {
        String sql = "DELETE FROM KhoaHuongDan WHERE MaKhoaHD = ?";
        return XJdbc.executeUpdate(sql, maKhoaHD) > 0;
    }

    // Tìm kiếm theo mã hoặc tiêu đề
    public ArrayList<KhoaHuongDan> search(String keyword) {
        ArrayList<KhoaHuongDan> list = new ArrayList<>();
        String sql = "SELECT MaKhoaHD, TieuDe, MoTa, NgayBatDau, NgayKetThuc, TrangThai " +
                     "FROM KhoaHuongDan " +
                     "WHERE MaKhoaHD LIKE ? OR TieuDe LIKE ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, "%" + keyword + "%", "%" + keyword + "%")) {
            while (rs.next()) {
                KhoaHuongDan kh = new KhoaHuongDan(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getDate(5),
                        rs.getBoolean(6)
                );
                list.add(kh);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tạo mã tự động
    public String getNextMaKhoaHD() {
        int maxNumber = 0;
        int maxDigits = 0;
        String sql = "SELECT MaKhoaHD FROM KhoaHuongDan";

        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            while (rs.next()) {
                String ma = rs.getString("MaKhoaHD");
                if (ma != null && ma.startsWith("KHD")) {
                    String numPart = ma.substring(3).replaceAll("\\D", "");
                    if (!numPart.isEmpty()) {
                        maxDigits = Math.max(maxDigits, numPart.length());
                        int val = Integer.parseInt(numPart);
                        if (val > maxNumber) maxNumber = val;
                    }
                }
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (maxDigits == 0) maxDigits = 5;
        return "KHD" + String.format("%0" + maxDigits + "d", maxNumber + 1);
    }

    // Cập nhật trạng thái (còn hoạt động / không hoạt động)
    public int capNhatTrangThai(String maKhoaHD, boolean trangThai) {
        String sql = "UPDATE KhoaHuongDan SET TrangThai = ? WHERE MaKhoaHD = ?";
        return XJdbc.executeUpdate(sql, trangThai, maKhoaHD);
    }
}
