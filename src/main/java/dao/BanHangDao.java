/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.BanHang;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Lenovo
 */
public class BanHangDao {
    String getAll = """
        SELECT 
            sp.MaSanPham, 
            sp.TenSanPham,
            spct.SoLuong,
            spct.Gia
        FROM SanPham as sp
        JOIN ChiTietSanPham spct ON sp.MaSanPham = spct.MaSanPham
    """;

    String getById = """
        SELECT 
            sp.MaSanPham, 
            sp.TenSanPham,
            ctsp.SoLuong,
            ctsp.Gia
        FROM SanPham AS sp
        INNER JOIN ChiTietSanPham AS ctsp ON sp.MaSanPham = ctsp.MaSanPham
        WHERE sp.MaSanPham = ?
    """;
    String deleteById = """
                        DELETE FROM [dbo].[SanPham]
                              WHERE id =?
                        """;

    public List<BanHang> getById(String ma) {
        return XQuery.getBeanList(BanHang.class, getById, ma);
    }

    public List<BanHang> getAll() {
        return XQuery.getBeanList(BanHang.class, getAll);
    }
   public void deleteById(String id) {
        XJdbc.executeUpdate(deleteById, id);
    }
}

