/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author tomio
 */

import entity.ChucVu;
import java.sql.*;
import java.util.ArrayList;
import unti.XJdbc;
public class ChucVuDao {
    private String sql;

    public ArrayList<ChucVu> getAll() {
        ArrayList<ChucVu> list = new ArrayList<>();
        sql = "SELECT * FROM ChucVu";
        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            while (rs.next()) {
                ChucVu cv = new ChucVu(
                    rs.getString("maChucVu"),
                    rs.getString("tenChucVu")
                );
                list.add(cv);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getTenChucVuByMa(String ma) {
        sql = "SELECT tenChucVu FROM ChucVu WHERE maChucVu = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, ma)) {
            if (rs.next()) {
                return rs.getString("tenChucVu");
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getMaChucVuByTen(String tenChucVu) {
            String sql = "SELECT maChucVu FROM ChucVu WHERE tenChucVu = ?";
    try {
        ResultSet rs = XJdbc.executeQuery(sql, tenChucVu);
        if (rs.next()) {
            return rs.getString("maChucVu");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null; // nếu không tìm thấy
    }
}
