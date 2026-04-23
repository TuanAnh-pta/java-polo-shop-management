/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.KhuyenMai;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Lenovo
 */
public class KhuyenMaiDao {
    String getAllSql = """
                       SELECT 
                             [tenPhieu]                          
                         FROM [QLBanAoPoLo_01_12].[dbo].[PhieuGiamGia]
                       """;
    String findById = """
                      SELECT *
                        FROM [QLBanAoPoLo_01_12].[dbo].[PhieuGiamGia]
                        where maPhieu = ?
                      """;
    
    public List<KhuyenMai> getAll(){
        return XQuery.getBeanList(KhuyenMai.class, getAllSql);
    }
    
    public List<KhuyenMai> getAllActive() {
    String sql = "SELECT * FROM PhieuGiamGia WHERE trangThai = 1";
    return XQuery.getBeanList(KhuyenMai.class, sql);
}
    
    
}
