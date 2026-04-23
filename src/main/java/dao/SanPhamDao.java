package dao;

import entity.SanPham;
import java.util.List;
import unti.XQuery;

/**
 *
 * @author Admin
 */
public class SanPhamDao {
    String getAllSql = """
                       SELECT [MaSanPham]
                             ,[TenSanPham]
                             ,[MoTa]
                             ,[MaChatLieu]
                         FROM [QLBanAoPoLo_01_12].[dbo].[SanPham]
                       """;
    public List<SanPham> getAll(){
    return XQuery.getBeanList(SanPham.class, getAllSql);
    }
    String findByIdsql = """
                         SELECT [MaSanPham]
                               ,[TenSanPham]
                               ,[MoTa]
                               ,[MaChatLieu]
                           FROM [QLBanAoPoLo_01_12].[dbo].[SanPham]
                           WHERE [MaSanPham] = ?
                         """;
    public SanPham findById(String MaSanPham){
    return XQuery.getSingleBean(SanPham.class, findByIdsql, MaSanPham);
    }
}
