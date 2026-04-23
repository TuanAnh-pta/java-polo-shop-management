/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import Interface.DotGGHoaDonDAO;
import entity.DotGiamGia_HoaDon;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author xuanTruong
 */
public class DotGGHoaDonDAOIpml implements DotGGHoaDonDAO {

    @Override
    public boolean insert(String maHD, String maDotGG) {
        String sql = """
            INSERT INTO DotGiamGia_HoaDon (MaDotGG, MaHoaDon)
            VALUES (?, ?)
        """;

        return XJdbc.executeUpdate(sql, maDotGG, maHD) > 0;
    }

    @Override
    public List<DotGiamGia_HoaDon> findByHoaDon(String maHD) {
        String sql = """
            SELECT * FROM DotGiamGia_HoaDon WHERE MaHoaDon = ?
        """;

        return XQuery.getBeanList(DotGiamGia_HoaDon.class, sql, maHD);
    }
}
