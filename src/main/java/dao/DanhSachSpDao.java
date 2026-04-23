package dao;

import entity.DanhSachSp;
import java.util.List;
import unti.XQuery;

/**
 *
 * @author Admin
 */
public class DanhSachSpDao {

    String getAllSql = """
                       SELECT 
                       ct.MaChiTietSP,
                       sp.MaSanPham,
                       ct.Gia,
                       sp.MaChatLieu,
                       ct.MaMauSac,
                       ct.MaKichThuoc,
                       ct.SoLuong
                       FROM ChiTietSanPham ct 
                       INNER JOIN SanPham sp ON sp.MaSanPham = ct.MaSanPham;
                       """;

    public List<DanhSachSp> getAll() {
        return XQuery.getBeanList(DanhSachSp.class, getAllSql);
    }

    String findByIdsql = """
                         SELECT 
                         ct.MaChiTietSP,
                         sp.MaSanPham,
                         ct.Gia,
                        
                         sp.MaChatLieu,
                         ct.MaMauSac,
                          ct.MaKichThuoc,
                         ct.SoLuong
                         FROM ChiTietSanPham ct 
                         INNER JOIN SanPham sp ON sp.MaSanPham = ct.MaSanPham
                         WHERE ct.MaChiTietSP = ?
                         """;

    String findByTenSql = """
    SELECT 
        ct.MaChiTietSP,
        sp.MaSanPham,
        ct.Gia,
      
        sp.MaChatLieu,
        ct.MaMauSac,
        ct.MaKichThuoc,
        ct.SoLuong
    FROM ChiTietSanPham ct
    INNER JOIN SanPham sp ON sp.MaSanPham = ct.MaSanPham
    WHERE sp.TenSanPham LIKE ?
""";

    public DanhSachSp findById(String MaChiTietSP) {
        return XQuery.getSingleBean(DanhSachSp.class, findByIdsql, MaChiTietSP);
    }

    public static void main(String[] args) {
        DanhSachSpDao dao = new DanhSachSpDao();
        List<DanhSachSp> lst = dao.getAll();
        for (DanhSachSp sp : lst) {
            System.out.println(sp.toString());
        }
    }

    public List<DanhSachSp> findByTenSanPham(String ten) {
        String keyword = "%" + ten + "%";
        return XQuery.getBeanList(DanhSachSp.class, findByTenSql, keyword);
    }

}
