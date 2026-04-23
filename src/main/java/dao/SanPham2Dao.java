/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.SanPham;
import entity.SanPham2;
import java.util.ArrayList;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Nga Cọt
 */
public class SanPham2Dao {

    String getAll = """
                    SELECT * FROM SanPham
                    """;

    public List<SanPham2> getAll() {
        return XQuery.getBeanList(SanPham2.class, getAll);
    }

    String findByMaSp = """
                       select * from SanPham where MaSanPham like ?
                       """;

    public List<SanPham2> findByMaSp(String maSp) {
        return XQuery.getBeanList(SanPham2.class, findByMaSp, maSp);
    }
    
    String findByTen ="""
                      select * from SanPham where TenSanPham like ?
                     """;
    public List<SanPham2> findByTen(String ten){
     return XQuery.getBeanList(SanPham2.class, findByTen, "%"+ten+"%");
    }
    String findByName ="SELECT * FROM SanPham WHERE LOWER(LTRIM(RTRIM(TenSanPham))) = ?";
    public List<SanPham2> findByName(String name){
        return XQuery.getBeanList(SanPham2.class,findByName,name.trim().toLowerCase());
                }
    String findByMa ="""
                     select * from SanPham where MaSanPham = ?
                     """;
    
    public SanPham2 findByMa(String ma){
     return XQuery.getSingleBean(SanPham2.class,findByMa,ma);
    }
    
    String createSql = """
                       INSERT INTO [dbo].[SanPham]
                                                        ([MaSanPham]
                                                        ,[TenSanPham]
                                                        ,[MoTa]
                                                        ,[MaChatLieu]
                                                        ,[MaXuatSu]
                                                        ,[TrangThai])
                                                  VALUES
                                                        (?,?,?,?,?,?)
                       """;
    public int create (SanPham2 sanPham2){
        Object[] values={
            sanPham2.getMaSanPham(),
            sanPham2.getTenSanPham(),
            sanPham2.getMoTa(),
            sanPham2.getMaChatLieu(),
            sanPham2.getMaXuatSu(),
            sanPham2.isTrangThai()
        };
        return XJdbc.executeUpdate(createSql, values);
    }
    String deleteSql = """
                       DELETE FROM [dbo].[SanPham]
                             WHERE MaSanPham = ? 
                       """;
    public int delete (String maSP){
        return XJdbc.executeUpdate(deleteSql, maSP);
    }
    String updateSql = """
                       UPDATE [dbo].[SanPham]
                          SET 
                             [TenSanPham] = ?
                             ,[MoTa] = ?
                             ,[MaChatLieu] = ?
                             ,[MaXuatSu] = ?
                             ,[TrangThai] = ?
                        WHERE [MaSanPham] = ?
                       """;
    public int update(SanPham2 sanPham2){
        Object[] value={   
         sanPham2.getTenSanPham(),
         sanPham2.getMoTa(),
         sanPham2.getMaChatLieu(),
         sanPham2.getMaXuatSu(),
         sanPham2.isTrangThai(),
         sanPham2.getMaSanPham()
        };
        return XJdbc.executeUpdate(updateSql, value);
    }
    
  String findAllFields = """
   SELECT sp.*
           FROM SanPham sp
           JOIN ChatLieu cl ON sp.MaChatLieu = cl.MaChatLieu
           JOIN XuatSu xs ON sp.MaXuatSu = xs.MaXuatSu
           WHERE LOWER(sp.MaSanPham) LIKE ?
              OR LOWER(sp.TenSanPham) LIKE ?
              OR LOWER(sp.MoTa) LIKE ?
              OR LOWER(cl.ChatLieu) LIKE ?
              OR LOWER(xs.NoiNhap) LIKE ?
              OR LOWER(CAST(sp.TrangThai AS CHAR)) LIKE ?
""";



public List<SanPham2> findAllFields(String keyword) {
    String kw = "%" + keyword.toLowerCase() + "%";
    return XQuery.getBeanList(SanPham2.class, findAllFields, kw, kw, kw, kw, kw, kw);
}

}



