/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ChiTietHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import unti.XJdbc;

/**
 *
 * @author Lenovo
 */
public class ChiTietHoaDonDao {
    
    public boolean insert(ChiTietHoaDon cthd) {
    String sql = "INSERT INTO ChiTietHoaDon (MaChiTietHoaDon, SoLuong, DonGia, MaHoaDon, MaChiTietSP) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = XJdbc.openConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, cthd.getMaChiTietHoaDon());
        ps.setInt(2, cthd.getSoLuong());
        ps.setBigDecimal(3, cthd.getDonGia());
        ps.setString(4, cthd.getMaHoaDon());
        ps.setString(5, cthd.getMaChiTietSP());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    
}
