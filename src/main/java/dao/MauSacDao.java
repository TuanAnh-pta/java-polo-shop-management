package dao;

import entity.MauSac;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Admin
 */
public class MauSacDao {
    String getAllSql = """
                       SELECT [MaMauSac]
                             ,[TenMau]
                         FROM [QLBanAoPoLo_01_12].[dbo].[MauSac]
                       """;
    public List<MauSac> getAll(){
    return XQuery.getBeanList(MauSac.class, getAllSql);
    }
    String findByIdSql = """
                         SELECT [MaMauSac]
                               ,[TenMau]
                           FROM [QLBanAoPoLo_01_12].[dbo].[MauSac]
                           WHERE [MaMauSac] = ?
                         """;
    public MauSac findById(String MaMauSac){
     return XQuery.getSingleBean(MauSac.class, findByIdSql, MaMauSac);
    }
    String createSql ="""
                      INSERT INTO [dbo].[MauSac]
                                 ([MaMauSac]
                                 ,[TenMau])
                           VALUES
                                 (?,?)
                      """;
    public int create(MauSac mauSac){
        Object[] value={
            mauSac.getMaMauSac(),
            mauSac.getTenMau()
        };
        return XJdbc.executeUpdate(createSql, value);
    }
    
    String deleteSql = """
                       DELETE FROM [dbo].[MauSac]
                             WHERE MaMauSac = ?
                       """;
    public int delete(String maMau){
     return XJdbc.executeUpdate(deleteSql,maMau);
    }
    
    String updateSql = """
                       UPDATE [dbo].[MauSac]
                          SET 
                             [TenMau] = ?
                        WHERE [MaMauSac] = ?
                       """;
    
    public int update(MauSac mauSac){
        Object[] value={
            mauSac.getTenMau(),
            mauSac.getMaMauSac()
        };
        return XJdbc.executeUpdate(updateSql, value);
    }
    
   String byTenSql = """
    SELECT * FROM MauSac WHERE LOWER(TenMau) LIKE ?
""";

public List<MauSac> getByTen(String ten){
    return XQuery.getBeanList(MauSac.class, byTenSql, "%" + ten.toLowerCase() + "%");
}
public MauSac findByMaMau(String maMau){
    String finByMaMau = """
        SELECT * FROM MauSac WHERE MaMauSac = ?
    """;
    List<MauSac> lsmau = XQuery.getBeanList(MauSac.class, finByMaMau, maMau);
    
    if (maMau == null || maMau.trim().isEmpty() || lsmau.isEmpty()) {
        return null;
    }

    return lsmau.get(0);
}

}
