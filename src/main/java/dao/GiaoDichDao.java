package dao;

import entity.giaodich;
import java.util.List;
import unti.XQuery;

/**
 *
 * @author Admin
 */
public class GiaoDichDao {
    String findByIDsql = """
                    SELECT 
                    kh.MaKhachHang,
                    hd.MaHoaDon,
                    sp.TenSanPham,
                    cts.SoLuong,
                    hd.TongTien,
                    hd.TrangThai
                    FROM ChiTietHoaDon ct
                    INNER JOIN HoaDon hd ON hd.MaHoaDon = ct.MaHoaDon
                    INNER JOIN KhachHang kh ON kh.MaKhachHang = hd.MaKhachHang
                    INNER JOIN ChiTietSanPham cts ON cts.MaChiTietSP = ct.MaChiTietSP
                    INNER JOIN SanPham sp ON sp.MaSanPham = cts.MaSanPham
                    WHERE kh.MaKhachHang = ?
                    """;
    public List<giaodich> findById(String MaKhachHang){
    return XQuery.getBeanList(giaodich.class, findByIDsql, MaKhachHang);
    }
}
