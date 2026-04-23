package dao;

//import entity.HoaDon;
import entity.HoaDon2;
import java.util.List;
import unti.XQuery;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class HoaDon2Dao {

    String getAllSql = """
       SELECT 
           kh.MaKhachHang,
           kh.TenKhachHang,
           kh.SoDienThoai,
           hd.MaHoaDon,
           hd.TongTien,
           hd.giaTri,
           hd.TienSauGiam,
           hd.TrangThai,
           nv.MaNhanVien,
           nv.hoTen,
           hd.NgayTao
       FROM HoaDon hd
       INNER JOIN NhanVien nv ON nv.MaNhanVien = hd.MaNhanVien
       INNER JOIN KhachHang kh ON kh.MaKhachHang = hd.MaKhachHang
       WHERE hd.TrangThai = 0;
       """;

    public List<HoaDon2> getAll() {
        return XQuery.getBeanList(HoaDon2.class, getAllSql);
    }
     String getAllSqll = """
       SELECT 
           kh.MaKhachHang,
           kh.TenKhachHang,
           kh.SoDienThoai,
           hd.MaHoaDon,
           hd.TongTien,
           hd.giaTri,
           hd.TienSauGiam,
           hd.TrangThai,
           nv.MaNhanVien,
           nv.hoTen,
           hd.NgayTao
       FROM HoaDon hd
       INNER JOIN NhanVien nv ON nv.MaNhanVien = hd.MaNhanVien
       INNER JOIN KhachHang kh ON kh.MaKhachHang = hd.MaKhachHang
       ;
       """;

    public List<HoaDon2> getAll_all() {
        return XQuery.getBeanList(HoaDon2.class, getAllSqll);
    }

    public List<HoaDon2> findAllFields(String keyword) {
        String findAllsql = """
                        SELECT 
                        kh.MaKhachHang,
                        kh.TenKhachHang,
                        kh.SoDienThoai,
                        hd.MaHoaDon,
                        hd.TongTien,
                        hd.giaTri,
                        hd.TienSauGiam,
                        hd.TrangThai,
                        nv.MaNhanVien,
                        nv.hoTen,
                        hd.NgayTao
                        FROM HoaDon hd
                        INNER JOIN NhanVien nv ON nv.MaNhanVien = hd.MaNhanVien
                        INNER JOIN KhachHang kh ON kh.MaKhachHang = hd.MaKhachHang
                        WHERE hd.MaHoaDon LIKE ?
                           OR nv.MaNhanVien LIKE ?
                           OR nv.hoTen LIKE ?
                           OR kh.MaKhachHang LIKE ?
                           OR kh.TenKhachHang LIKE ?
                           OR kh.SoDienThoai LIKE ?
                           
                        """;
        String likeKeyword = "%" + keyword + "%";
        return XQuery.getBeanList(HoaDon2.class, findAllsql, likeKeyword, likeKeyword, likeKeyword, likeKeyword, likeKeyword, likeKeyword);
    }

    String findTrangThaisql = """
                            SELECT 
                            kh.MaKhachHang,
                            kh.TenKhachHang,
                            kh.SoDienThoai,
                            hd.MaHoaDon,
                            hd.TongTien,
                            hd.giaTri,
                            hd.TienSauGiam,
                            hd.TrangThai,
                            nv.MaNhanVien,
                            nv.hoTen,
                            hd.NgayTao
                            FROM HoaDon hd
                            INNER JOIN NhanVien nv ON nv.MaNhanVien = hd.MaNhanVien
                            INNER JOIN KhachHang kh ON kh.MaKhachHang = hd.MaKhachHang
                            WHERE hd.TrangThai = ?
                          """;

    public List<HoaDon2> findTrangThai(boolean TrangThai) {
        return XQuery.getBeanList(HoaDon2.class, findTrangThaisql, TrangThai);
    }
    String findNgayTaoKhoangSql = """
    SELECT 
        kh.MaKhachHang,
        kh.TenKhachHang,
        kh.SoDienThoai,
        hd.MaHoaDon,
        hd.TongTien,
        hd.giaTri,
        hd.TienSauGiam,
        hd.TrangThai,
        nv.MaNhanVien,
        nv.hoTen,
        hd.NgayTao
    FROM HoaDon hd
    INNER JOIN NhanVien nv ON nv.MaNhanVien = hd.MaNhanVien
    INNER JOIN KhachHang kh ON kh.MaKhachHang = hd.MaKhachHang
    WHERE hd.NgayTao BETWEEN ? AND ?
""";

    public List<HoaDon2> findNgayTaoKhoang(Date startDate, Date endDate) {
        return XQuery.getBeanList(HoaDon2.class, findNgayTaoKhoangSql, startDate, endDate);
    }

}
