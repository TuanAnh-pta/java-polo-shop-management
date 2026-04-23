package dao;

import entity.KhachHang;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.JOptionPane;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author Admin
 */
public class KhachHangDao {

    String getAllsql = """
                       SELECT [MaKhachHang]
                             ,[TenKhachHang]
                             ,[SoDienThoai]
                             ,[Email]
                             ,[DiaChi]
                             ,[TrangThai]
                             ,[GioiTinh]
                         FROM [QLBanAoPoLo_01_12].[dbo].[KhachHang]
                       """;

    public List<KhachHang> getAll() {
        return XQuery.getBeanList(KhachHang.class, getAllsql);
    }
    String deletesql = """
                       UPDATE KhachHang
                               SET TrangThai = 0
                               WHERE MaKhachHang = ?
                       """;

    public int delete(String MaKhachHang) {
        return XJdbc.executeUpdate(deletesql, MaKhachHang);
    }

    String insertsql = """
                       INSERT INTO [dbo].[KhachHang]
                                  ([MaKhachHang]
                                  ,[TenKhachHang]
                                  ,[SoDienThoai]
                                  ,[Email]
                                  ,[DiaChi]
                                  ,[TrangThai]
                                  ,[GioiTinh])
                            VALUES
                                  (?,?,?,?,?,?,?)
                       """;

    public int insert(KhachHang ha) {
        Object[] values = {
            ha.getMaKhachHang(),
            ha.getTenKhachHang(),
            ha.getSoDienThoai(),
            ha.getEmail(),
            ha.getDiaChi(),
            ha.isTrangThai(),
            ha.getGioiTinh()
        };
        return XJdbc.executeUpdate(insertsql, values);
    }
    String updatesql = """
                       UPDATE [dbo].[KhachHang]
                          SET [TenKhachHang] = ?
                             ,[SoDienThoai] = ?
                             ,[Email] = ?
                             ,[DiaChi] = ?
                             ,[TrangThai] = ?
                             ,[GioiTinh] = ?
                        WHERE [MaKhachHang] = ?
                       """;

    public int update(KhachHang ha) {
        Object[] values = {
            ha.getTenKhachHang(),
            ha.getSoDienThoai(),
            ha.getEmail(),
            ha.getDiaChi(),
            ha.isTrangThai(),
            ha.getGioiTinh(),
            ha.getMaKhachHang()
        };
        return XJdbc.executeUpdate(updatesql, values);
    }

    public List<KhachHang> findAllFields(String keyword) {
        String findAllsql = """
        SELECT * FROM KhachHang
        WHERE MaKhachHang LIKE ?
           OR TenKhachHang LIKE ?
           OR SoDienThoai LIKE ?
           OR Email LIKE ?
           OR DiaChi LIKE ?
    """;
        String likeKeyword = "%" + keyword + "%";
        return XQuery.getBeanList(KhachHang.class, findAllsql, likeKeyword, likeKeyword, likeKeyword, likeKeyword, likeKeyword);
    }
    String findGioiTinhSql = """
                         SELECT * FROM KhachHang
                         WHERE GioiTinh = ?
                         """;

    public List<KhachHang> findGioiTinh(String GioiTinh) {
        return XQuery.getBeanList(KhachHang.class, findGioiTinhSql, GioiTinh);
    }
    String findTrangThaisql = """
                            SELECT * FROM KhachHang
                            WHERE TrangThai = ?
                            """;

    public List<KhachHang> findTrangThai(boolean TrangThai) {
        return XQuery.getBeanList(KhachHang.class, findTrangThaisql, TrangThai);
    }

    public String getNewMaKhachHang() {
        List<KhachHang> list = getAll();
        int max = 0;

        for (KhachHang kh : list) {
            String ma = kh.getMaKhachHang(); // VD: KH0007

            try {
                // Cắt bỏ "KH" và parse phần số
                String so = ma.replaceAll("\\D+", ""); // Lấy tất cả chữ số
                int num = Integer.parseInt(so);
                if (num > max) {
                    max = num;
                }
            } catch (NumberFormatException e) {
                // Bỏ qua mã không hợp lệ (ví dụ KHBL)
                System.err.println("Bỏ qua mã không hợp lệ: " + ma);
            }
        }

        return String.format("KH%05d", max + 1); // VD: KH00008
    }

    String deletehetsql = """
                    DELETE FROM KhachHang
                          WHERE MaKhachHang = ?
                    """;

    public int deletehet(String MaKhachHang) {
        return XJdbc.executeUpdate(deletehetsql, MaKhachHang);
    }
    String findByIdsql = """
                         SELECT [MaKhachHang]
                                ,[TenKhachHang]
                                ,[SoDienThoai]
                                ,[Email]
                                ,[DiaChi]
                             FROM [QLBanAoPoLo_01_12].[dbo].[KhachHang]
                             WHERE [MaKhachHang] = ?
                         """;

    public KhachHang findById(String MaKhachHang) {
        return XQuery.getSingleBean(KhachHang.class, findByIdsql, MaKhachHang);
    }
    String findById = "select *from KhachHang where MaKhachHang=?";

    public KhachHang findByMa(String ma) {
        return XQuery.getSingleBean(KhachHang.class, findById, ma);

    }

}
