/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import entity.NhanVien;
import java.util.List;
import unti.XJdbc;

/**
 *
 * @author tomio
 */
public class NhanVienDao {
         private String sql;

    public ArrayList<NhanVien> getAllNhanVien() {
        ArrayList<NhanVien> list = new ArrayList<>();
        sql = "SELECT maNhanVien, hoTen, gioiTinh, soDienThoai, email, maChuCVu, trangThai FROM NhanVien WHERE trangThai = 0";
        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("hoTen"),
                    rs.getString("gioiTinh"),
                    rs.getString("soDienThoai"),
                    rs.getString("email"),
                    rs.getString("maChucVu"),
                    rs.getBoolean("trangThai") ? 1 : 0
                );
                list.add(nv);
            }
            rs.getStatement().getConnection().close(); // đóng sau khi dùng xong
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int add(NhanVien n) {
        sql = "INSERT INTO NhanVien(maNhanVien, hoTen, gioiTinh, soDienThoai, email, maChucVu, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return XJdbc.executeUpdate(sql,
            n.getMaNhanVien(),
            n.getHoTen(),
            n.getGioiTinh(),
            n.getSoDienThoai(),
            n.getEmail(),
            n.getMaChucVu(),
            n.getTrangThai() == 1
        );
    }

public int update(NhanVien n) {
    sql = "UPDATE NhanVien SET hoTen = ?, gioiTinh = ?, soDienThoai = ?, email = ?, maChucVu = ?, trangThai = ? WHERE maNhanVien = ?";
    return XJdbc.executeUpdate(sql,
        n.getHoTen(),
        n.getGioiTinh(),
        n.getSoDienThoai(),
        n.getEmail(),
        n.getMaChucVu(),
        n.getTrangThai() == 1, // bit
        n.getMaNhanVien()    // dùng làm điều kiện WHERE
    );
}

    public boolean delete(String maNV) {
        sql = "UPDATE NhanVien SET trangThai = 1 WHERE maNhanVien = ?";
        return XJdbc.executeUpdate(sql, maNV) > 0;
    }
    
    
public ArrayList<NhanVien> search(String keyword) {
    ArrayList<NhanVien> list = new ArrayList<>();
    sql = "SELECT * FROM NhanVien WHERE (maNhanVien LIKE ? OR hoTen LIKE ? OR soDienThoai LIKE ? OR email LIKE ?) AND trangThai = 0";
    try (ResultSet rs = XJdbc.executeQuery(sql, 
            "%" + keyword + "%", 
            "%" + keyword + "%", 
            "%" + keyword + "%", 
            "%" + keyword + "%")) {
        
        while (rs.next()) {
            NhanVien nv = new NhanVien(
                rs.getString("maNhanVien"),
                rs.getString("hoTen"),
                rs.getString("gioiTinh"),
                rs.getString("soDienThoai"),
                rs.getString("email"),
                rs.getString("maChucVu"),
                rs.getBoolean("trangThai") ? 1 : 0
            );
            list.add(nv);
        }
        rs.getStatement().getConnection().close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}



public NhanVien checkLogin(String email) {
    sql = "SELECT * FROM NhanVien WHERE email = ? AND trangThai = 0";
    try (ResultSet rs = XJdbc.executeQuery(sql, email)) {
        if (rs.next()) {
            NhanVien nv = new NhanVien(
                rs.getString("maNhanVien"),
                rs.getString("hoTen"),
                rs.getString("gioiTinh"),
                rs.getString("soDienThoai"),
                rs.getString("email"),
                rs.getString("maChucVu"),
                rs.getBoolean("trangThai") ? 1 : 0
            );
            rs.getStatement().getConnection().close();
            return nv;
        }
        rs.getStatement().getConnection().close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


public ArrayList<NhanVien> getByStatus(int status) {
    ArrayList<NhanVien> list = new ArrayList<>();
    sql = "SELECT * FROM NhanVien WHERE trangThai = ?";
    try (ResultSet rs = XJdbc.executeQuery(sql, status)) {
        while (rs.next()) {
            NhanVien nv = new NhanVien(
                rs.getString("maNhanVien"),
                rs.getString("hoTen"),
                rs.getString("gioiTinh"),
                rs.getString("soDienThoai"),
                rs.getString("email"),
                rs.getString("maChucVu"),
                rs.getBoolean("trangThai") ? 1 : 0
            );
            list.add(nv);
        }
        rs.getStatement().getConnection().close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

public String getMaChucVuByTen(String tenChucVu) {
    sql = "SELECT maChucVu FROM ChucVu WHERE tenChucVu = ?";
    try (ResultSet rs = XJdbc.executeQuery(sql, tenChucVu)) {
        if (rs.next()) {
            return rs.getString("maChucVu");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "";
}

public String getTenChucVuByMa(String ma) {
    sql = "SELECT tenChucVu FROM ChucVu WHERE maChucVu = ?";
    try (ResultSet rs = XJdbc.executeQuery(sql, ma)) {
        if (rs.next()) {
            return rs.getString("tenChucVu");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "";
}

public String getNextMaNhanVien() {
    int maxNumber = 0;
    int maxDigits = 0;
    sql = "SELECT maNhanVien FROM NhanVien";
    try (ResultSet rs = XJdbc.executeQuery(sql)) {
        while (rs.next()) {
            String ma = rs.getString("maNhanVien");
            if (ma != null && ma.toUpperCase().startsWith("NV")) {
                String numPart = ma.substring(2);            // phần sau "NV"
                String digitsOnly = numPart.replaceAll("\\D", ""); // chỉ giữ số
                if (!digitsOnly.isEmpty()) {
                    maxDigits = Math.max(maxDigits, digitsOnly.length());
                    try {
                        int val = Integer.parseInt(digitsOnly);
                        if (val > maxNumber) maxNumber = val;
                    } catch (NumberFormatException ex) {
                        // bỏ qua các mã lạ
                    }
                }
            }
        }
        rs.getStatement().getConnection().close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    if (maxDigits == 0) maxDigits = 5; // mặc định 5 chữ số -> NV00001 kiểu
    int next = maxNumber + 1;
    return "NV" + String.format("%0" + maxDigits + "d", next);
}
}
