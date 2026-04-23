package dao;

import entity.HoaDonChiTiet;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Admin
 */
public class HoaDonChiTietDao {

    String findById = """
                     SELECT 
                     ct.MaHoaDon,
                     ct.MaChiTietHoaDon,
                     hd.MaSanPham,
                     hd.MaSanPham,
                     ct.SoLuong,
                     hd.MaMauSac,
                     hd.MaKichThuoc,
                     sp.MaChatLieu,
                     ct.DonGia,
                     sp.MaXuatSu
                     FROM ChiTietHoaDon ct
                     INNER JOIN ChiTietSanPham hd ON ct.MaChiTietSP = hd.MaChiTietSP
                     INNER JOIN SanPham sp ON sp.MaSanPham = hd.MaSanPham
                     WHERE ct.MaHoaDon = ?
                     """;

    public List<HoaDonChiTiet> findById(String MaHoaDon) {
    return XQuery.getBeanList(HoaDonChiTiet.class, findById, MaHoaDon );
}
}