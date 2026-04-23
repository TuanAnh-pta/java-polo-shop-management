/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ChiTietSanPham;
import entity.SanPham2;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Nga Cọt
 */
public class SanPhamCTDao {
    String getAll ="""
                   SELECT [MaChiTietSP]
                                             ,[SoLuong]
                                             ,[Gia]
                                             ,[GiaSauGiam]
                                             
                                             ,[MaMauSac]
                                             ,[MaKichThuoc]
                                         FROM [dbo].[ChiTietSanPham]
                   """;
    
    
    String byMaSPCT ="""
                     SELECT *FROM ChiTietSanPham WHERE MaSanPham =? 
                     """;
    String createSql = """
                    INSERT INTO [dbo].[ChiTietSanPham]
                               ([MaChiTietSP]
                               ,[SoLuong]
                               ,[Gia]
                              
                               ,[MaSanPham]
                               ,[MaMauSac]
                               ,[MaKichThuoc])
                         VALUES
                               (?,?,?,?,?,?)
                    """;
    String updateSql = """
                       UPDATE [dbo].[ChiTietSanPham]
                          SET 
                             [SoLuong] = ?
                             ,[Gia] = ?
                             
                             ,[MaSanPham] = ?
                             ,[MaMauSac] = ?
                             ,[MaKichThuoc] = ?
                        WHERE [MaChiTietSP] = ?
                       """;
    String deleteSql = """
                       DELETE FROM [dbo].[ChiTietSanPham]
                             WHERE [MaChiTietSP] = ?
                       """;
    
    String findByMaCT = """
                        SELECT *FROM ChiTietSanPham WHERE MaChiTietSP like ?
                        """;
    
    public List<ChiTietSanPham> getAll(){
        return XQuery.getBeanList(ChiTietSanPham.class, getAll);
    }
    public List<ChiTietSanPham> findByMaSP(String maSP){
        return XQuery.getBeanList(ChiTietSanPham.class,byMaSPCT,maSP);
    }
    public List<ChiTietSanPham> findByMaCT(String maCT){
        return XQuery.getBeanList(ChiTietSanPham.class, findByMaCT, "%"+maCT+"%");
    }
    public int create (ChiTietSanPham chiTietSanPham){
        Object[] values={
            chiTietSanPham.getMaChiTietSP(),
            chiTietSanPham.getSoLuong(),
            chiTietSanPham.getGia(),
            
            chiTietSanPham.getMaSanPham(),
            chiTietSanPham.getMaMauSac(),
            chiTietSanPham.getMaKichThuoc()
        };
       return XJdbc.executeUpdate(createSql, values);
    }
    public int delete(String maChiTiet){
        return XJdbc.executeUpdate(deleteSql,maChiTiet);
    }
    public int update(ChiTietSanPham chiTietSanPham){
       Object[] values ={
          chiTietSanPham.getSoLuong(),
           chiTietSanPham.getGia(),
           
           chiTietSanPham.getMaSanPham(),
           chiTietSanPham.getMaMauSac(),
           chiTietSanPham.getMaKichThuoc(),
           chiTietSanPham.getMaChiTietSP()          
       };
       return XJdbc.executeUpdate(updateSql, values);
    }
    // Tìm một ChiTietSanPham theo mã chi tiết (PK)
public ChiTietSanPham findByMaChiTietSP(String maCT) {
    String sql = "SELECT * FROM ChiTietSanPham WHERE MaChiTietSP = ?";
    List<ChiTietSanPham> list = XQuery.getBeanList(ChiTietSanPham.class, sql, maCT);
    return list.isEmpty() ? null : list.get(0);
}
//String findAllFields = """
//    SELECT * FROM ChiTietSanPham 
//    WHERE LOWER(MaChiTietSP) LIKE ? 
//       OR LOWER(CAST(SoLuong AS CHAR)) LIKE ? 
//       OR LOWER(CAST(Gia AS CHAR)) LIKE ? 
//       OR LOWER(CAST(GiaSauGiam AS CHAR)) LIKE ? 
//       OR LOWER(MaSanPham) LIKE ? 
//       OR LOWER(MaMauSac) LIKE ? 
//       OR LOWER(MaKichThuoc) LIKE ?
//""";
String findAllFields = """
    SELECT cts.*
    FROM ChiTietSanPham cts
    JOIN SanPham sp ON cts.MaSanPham = sp.MaSanPham
    JOIN MauSac m ON cts.MaMauSac = m.MaMauSac
    JOIN KichThuoc k ON cts.MaKichThuoc = k.MaKichThuoc
    WHERE LOWER(cts.MaChiTietSP) LIKE ?
       OR LOWER(CAST(cts.SoLuong AS CHAR)) LIKE ?
       OR LOWER(CAST(cts.Gia AS CHAR)) LIKE ?
       OR LOWER(sp.TenSanPham) LIKE ?
       OR LOWER(m.TenMau) LIKE ?
       OR LOWER(k.KichThuoc) LIKE ?
""";


public List<ChiTietSanPham> findAllFields(String keyword) {
    String kw = "%" + keyword.toLowerCase() + "%";
    return XQuery.getBeanList(ChiTietSanPham.class, findAllFields, kw, kw, kw, kw, kw, kw);
}

public String getMaChiTietSPByMaSP(String maSP) {
    String sql = "SELECT TOP 1 MaChiTietSP FROM ChiTietSanPham WHERE MaSanPham = ?";
    ResultSet rs = null;
    
    try {
        rs = XJdbc.executeQuery(sql, maSP); // Dùng hàm đã có trong XJdbc
        if (rs.next()) {
            return rs.getString("MaChiTietSP");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) {
                rs.getStatement().getConnection().close(); // Đảm bảo giải phóng kết nối
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    return null;
}




}
