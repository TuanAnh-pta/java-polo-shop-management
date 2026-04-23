/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import Interface.DotGGDAO;
import entity.DotGiamGia;
import java.util.List;
import unti.XJdbc;
import unti.XQuery;

/**
 *
 * @author xuanTruong
 */
public class DotGiamGia_Ipml implements DotGGDAO {

    String sql_findAll = "select * from dotgiamgia";
    String sql_findMa = "SELECT * FROM DotGiamGia WHERE TrangThai = 1 ";
    String sql_findMaDot = "SELECT * FROM DotGiamGia WHERE  maDotGG LIKE ?";
    String sql_findTenDot = "SELECT * FROM DotGiamGia WHERE  tenDotGG LIKE ?";

    String sql_delete = "delete  FROM DotGiamGia WHERE  maDotGG = ?";
    String sql_ADD = "INSERT INTO DotGiamGia "
            + "(maDotGG, tenDotGG, loaiGiamGia, giaTri, ngayBatDau, ngayKetThuc, trangThai, moTa) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    String sql_update = "UPDATE DotGiamGia SET "
            + "tenDotGG = ?, loaiGiamGia = ?, giaTri = ?, ngayBatDau = ?, "
            + "ngayKetThuc = ?, trangThai = ?, moTa = ? "
            + "WHERE maDotGG = ?";

    String sql_countMa = "SELECT COUNT(*) FROM DotGiamGia WHERE maDotGG = ?";

    String sql_DotGiamLonNhat = """
    SELECT TOP 1 *
    FROM DotGiamGia
    WHERE TrangThai = 1
      AND GETDATE() BETWEEN NgayBatDau AND NgayKetThuc
    ORDER BY GiaTri DESC
""";

    @Override
    public DotGiamGia create(DotGiamGia entity) {
        Object[] values = new Object[]{
            entity.getMaDotGG(), entity.getTenDotGG(), entity.getLoaiGiamGia(), entity.getGiaTri(),
            entity.getNgayBatDau(), entity.getNgayKetThuc(), entity.isTrangThai(), entity.getMoTa()
        };
        XJdbc.executeUpdate(sql_ADD, values);
        return entity;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(DotGiamGia entity) {
        Object[] values = new Object[]{
            entity.getTenDotGG(), entity.getLoaiGiamGia(), entity.getGiaTri(),
            entity.getNgayBatDau(), entity.getNgayKetThuc(), entity.isTrangThai(), entity.getMoTa(),
            entity.getMaDotGG()
        };
        XJdbc.executeUpdate(sql_update, values);

        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(sql_delete, id);
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DotGiamGia> findAll() {
        return XQuery.getBeanList(DotGiamGia.class, sql_findAll);
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public DotGiamGia findById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DotGiamGia> findMaDot(String maDotgg) {
        return XQuery.getBeanList(DotGiamGia.class, sql_findMaDot, maDotgg);
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DotGiamGia> findTrangThai() {
        return XQuery.getBeanList(DotGiamGia.class, sql_findMa);
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DotGiamGia> findByName(String name) {
        return XQuery.getBeanList(DotGiamGia.class, sql_findTenDot, '%' + name + '%');
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isMaDotTonTai(String maDot) {
        return countMaDot(maDot) > 0; // kiểm tra nếu có bản ghi trùng
    }

    @Override
    public int countMaDot(String maDot) {
        try {
            Object value = XJdbc.getValue(sql_countMa, maDot); // getValue trả về Object
            if (value != null) {
                return ((Number) value).intValue(); // chuyển sang int
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // nếu lỗi hoặc không tìm thấy
    }

    @Override
    public DotGiamGia getDotGiamGiaHienTai() {
        return XQuery.getSingleBean(DotGiamGia.class, sql_DotGiamLonNhat);
    }

}
