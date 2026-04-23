/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import Interface.ChiTietSanPham2DAO;
import entity.ChiTietSanPham2;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author xuanTruong
 */
public class ChiTietSanPham2_Ipml implements ChiTietSanPham2DAO {

    // SQL lấy tất cả chi tiết sản phẩm
    String sql_findAll = """
    SELECT 
        ctsp.MaChiTietSP,
        ctsp.MaDotGG,
        sp.TenSanPham,
        ms.TenMau AS TenMau,
        kt.KichThuoc AS KichThuoc,
        ctsp.Gia AS GiaGoc,
        ctsp.GiaSauGiam,
        ctsp.SoLuong AS SoLuong
    FROM ChiTietSanPham ctsp
    LEFT JOIN SanPham sp ON ctsp.MaSanPham = sp.MaSanPham
    LEFT JOIN MauSac ms ON ctsp.MaMauSac = ms.MaMauSac
    LEFT JOIN KichThuoc kt ON ctsp.MaKichThuoc = kt.MaKichThuoc
""";

    // Lấy chi tiết SP theo đợt giảm giá
    String sql_findMaDot = """
      SELECT 
            ctsp.MaChiTietSP,
            ctsp.MaDotGG,
            sp.TenSanPham,
            ms.TenMau AS TenMau,
            kt.KichThuoc AS KichThuoc,
            ctsp.Gia AS GiaGoc,
            ctsp.GiaSauGiam,
            ctsp.SoLuong AS SoLuong
        FROM ChiTietSanPham ctsp
        LEFT JOIN SanPham sp ON ctsp.MaSanPham = sp.MaSanPham
        LEFT JOIN MauSac ms ON ctsp.MaMauSac = ms.MaMauSac
        LEFT JOIN KichThuoc kt ON ctsp.MaKichThuoc = kt.MaKichThuoc
        LEFT JOIN DotGiamGia dg ON ctsp.MaDotGG = dg.MaDotGG
        WHERE ctsp.MaDotGG = ? AND dg.TrangThai = 1
""";

    // SQL áp dụng giảm giá cho 1 chi tiết sản phẩm
    private String SQL_AP_DUNG = """
        UPDATE ctsp
        SET ctsp.MaDotGG = ?, 
            ctsp.GiaSauGiam = CASE 
                                WHEN dg.LoaiGiamGia = 'PhanTram' THEN ctsp.Gia - (ctsp.Gia * dg.GiaTri / 100)
                                WHEN dg.LoaiGiamGia = 'TienMat' THEN ctsp.Gia - dg.GiaTri
                                ELSE ctsp.Gia
                              END
        FROM ChiTietSanPham ctsp
        JOIN DotGiamGia dg ON dg.MaDotGG = ?
        WHERE ctsp.MaChiTietSP = ?
    """;

    // SQL ngừng áp dụng giảm giá cho 1 chi tiết sản phẩm
    private String SQL_BO_GIAM = """
        UPDATE ChiTietSanPham
        SET MaDotGG = NULL, GiaSauGiam = Gia
        WHERE MaChiTietSP = ?
    """;

    // SQL lấy chi tiết sản phẩm sau khi update
    private String SQL_SELECT_AFTER_UPDATE = """
        SELECT CTSP.MaChiTietSP, SP.TenSanPham, CTSP.Gia AS GiaGoc, CTSP.GiaSauGiam, CTSP.MaDotGG
        FROM ChiTietSanPham CTSP
        JOIN SanPham SP ON CTSP.MaSanPham = SP.MaSanPham
        WHERE CTSP.MaChiTietSP = ?
    """;
    private String SQL_SELECT_AFTER_REMOVE = SQL_SELECT_AFTER_UPDATE;

    @Override
    public ChiTietSanPham2 create(ChiTietSanPham2 entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(ChiTietSanPham2 entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteById(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ChiTietSanPham2> findAll() {
        return XQuery.getBeanList(ChiTietSanPham2.class, sql_findAll);
    }

    @Override
    public ChiTietSanPham2 findById(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ChiTietSanPham2> findMaDot(String maDot) {
        return XQuery.getBeanList(ChiTietSanPham2.class, sql_findMaDot, maDot);
    }

    // Áp dụng giảm giá cho chi tiết SP
    @Override
    public List<ChiTietSanPham2> applyDiscount(String maCTSP, String maDotGG) {
        // Cập nhật giá và lưu mã đợt giảm giá
        XJdbc.executeUpdate(SQL_AP_DUNG, maDotGG, maDotGG, maCTSP);
        return XQuery.getBeanList(ChiTietSanPham2.class, SQL_SELECT_AFTER_UPDATE, maCTSP);
    }

    // Ngừng áp dụng giảm giá cho chi tiết SP
    @Override
    public List<ChiTietSanPham2> cancelDiscount(String maCTSP) {
        XJdbc.executeUpdate(SQL_BO_GIAM, maCTSP);
        return XQuery.getBeanList(ChiTietSanPham2.class, SQL_SELECT_AFTER_REMOVE, maCTSP);
    }

}
