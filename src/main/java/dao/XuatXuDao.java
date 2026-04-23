/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.XuatXu;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Nga Cọt
 */
public class XuatXuDao {
    
    String getAll = "select * from XuatSu ";
    
    
    public List<XuatXu> getAll(){
     return XQuery.getBeanList(XuatXu.class, getAll);
    }
    
    String findByMaXX = "select * from XuatSu where MaXuatSu = ?";
    public XuatXu findByMaXX(String maXX){
     return XQuery.getSingleBean(XuatXu.class,findByMaXX,maXX);
    }
    
    String createSql = """
                       INSERT INTO [dbo].[XuatSu]
                                  ([MaXuatSu]
                                  ,[NoiNhap])
                            VALUES
                                  (?,?)
                       """;
    public int create(XuatXu xuatXu){
     Object[] value={   
        xuatXu.getMaXuatSu(),
         xuatXu.getNoiNhap()
     };
     return XJdbc.executeUpdate(createSql, value);
    }
    String updateSql = """
                       UPDATE [dbo].[XuatSu]
                          SET 
                             [NoiNhap] = ?
                        WHERE [MaXuatSu] =?
                       """;
    public int update(XuatXu xuatXu){
        Object[]value={
          xuatXu.getNoiNhap(),
          xuatXu.getMaXuatSu()
        };
        return XJdbc.executeUpdate(updateSql, value);
    }
    String deleteSql= """
                      DELETE FROM [dbo].[XuatSu]
                            WHERE [MaXuatSu] =?
                      """;
    public int delete(String ma){
     return XJdbc.executeUpdate(deleteSql,ma);
    }
    String findByTen ="""
                      select * from XuatSu where NoiNhap like ?
                      """;
    public List<XuatXu> findByTen(String ten){
     return XQuery.getBeanList(XuatXu.class,findByTen,"%"+ten.toLowerCase()+"%");
    }
        String findXuatXuSql = """
                           SELECT [MaXuatSu]
                                 ,[NoiNhap]
                             FROM [QLBanAoPoLo_01_12].[dbo].[XuatSu]
                           WHERE [MaXuatSu] = ? 
                           """;
    public XuatXu findxuatXu(String MaXuatSu){
    return XQuery.getSingleBean(XuatXu.class, findXuatXuSql, MaXuatSu);
    } 
}
