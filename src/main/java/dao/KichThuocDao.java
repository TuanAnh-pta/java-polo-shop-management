package dao;

import entity.KichThuoc;
import entity.MauSac;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Admin
 */
public class KichThuocDao {
    String getAllSql = """
                       SELECT [MaKichThuoc]
                             ,[KichThuoc]
                         FROM [QLBanAoPoLo_01_12].[dbo].[KichThuoc]
                       """;
    public List<KichThuoc> getAll(){
    return XQuery.getBeanList(KichThuoc.class, getAllSql);
    }
    String findByIdSql = """
                         SELECT [MaKichThuoc]
                               ,[KichThuoc]
                           FROM [QLBanAoPoLo_01_12].[dbo].[KichThuoc]
                           WHERE [MaKichThuoc] = ?
                         """;
    public KichThuoc findById(String MaKichThuoc){
    return XQuery.getSingleBean(KichThuoc.class, findByIdSql, MaKichThuoc);
    }
    String createSql ="""
                      INSERT INTO [dbo].[KichThuoc]
                                 ([MaKichThuoc]
                                 ,[KichThuoc])
                           VALUES
                                 (?,?)
                      """;
    public int create(KichThuoc kichThuoc){
     Object[] value={   
       kichThuoc.getMaKichThuoc(),
         kichThuoc.getKichThuoc()
     };
     return XJdbc.executeUpdate(createSql, value);
    }
    
    String deleteSql ="""
                      DELETE FROM [dbo].[KichThuoc]
                            WHERE MaKichThuoc =?
                      """;
    
    public int delete(String maKichTh){
     return XJdbc.executeUpdate(deleteSql,maKichTh);
             
    }
    
    String update ="""
                   UPDATE [dbo].[KichThuoc]
                      SET 
                         [KichThuoc] = ?
                    WHERE [MaKichThuoc] = ?
                   """;
    
    public int update(KichThuoc kichThuoc){
        Object[]value={
            kichThuoc.getKichThuoc(),
            kichThuoc.getMaKichThuoc()
        };
        return XJdbc.executeUpdate(update, value);
    }
    
    String getByTen = """
                      select * from KichThuoc where KichThuoc like ?
                      """;
    public List<KichThuoc> getByTen(String ten){
     return XQuery.getBeanList(KichThuoc.class,getByTen,"%"+ten.toLowerCase()+"%");
    }
    
    public KichThuoc getByMaKichTh(String maKt){
        String getByMaKichTh = """
                               select *from KichThuoc where MaKichThuoc = ?
                               """;
        List<KichThuoc> lsKichTh = XQuery.getBeanList(KichThuoc.class,getByMaKichTh,maKt);
        if (maKt == null || maKt.trim().isEmpty() || lsKichTh.isEmpty()) {
        return null;
    }

    return lsKichTh.get(0);

    }
}
