/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;

/**
 *
 * @author xuanTruong
 */
public class ChiTietSanPham2 {

    private String maChiTietSP;   // ctsp.MaChiTietSP
    private String maDotGG;       // dct.MaDotGG
    private String tenSanPham;    // sp.TenSanPham
    private String tenMau;        // ms.TenMau
    private String kichThuoc;     // kt.KichThuoc
    private BigDecimal giaGoc;    // ctsp.Gia
    private BigDecimal giaSauGiam; // ctsp.GiaSauGiam
    private int soLuong;

    public ChiTietSanPham2() {
    }

    public ChiTietSanPham2(String maChiTietSP, String maDotGG, String tenSanPham, String tenMau, String kichThuoc, BigDecimal giaGoc, BigDecimal giaSauGiam, int soLuong) {
        this.maChiTietSP = maChiTietSP;
        this.maDotGG = maDotGG;
        this.tenSanPham = tenSanPham;
        this.tenMau = tenMau;
        this.kichThuoc = kichThuoc;
        this.giaGoc = giaGoc;
        this.giaSauGiam = giaSauGiam;
        this.soLuong = soLuong;
    }

    public String getMaChiTietSP() {
        return maChiTietSP;
    }

    public void setMaChiTietSP(String maChiTietSP) {
        this.maChiTietSP = maChiTietSP;
    }

    public String getMaDotGG() {
        return maDotGG;
    }

    public void setMaDotGG(String maDotGG) {
        this.maDotGG = maDotGG;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getTenMau() {
        return tenMau;
    }

    public void setTenMau(String tenMau) {
        this.tenMau = tenMau;
    }

    public String getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public BigDecimal getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(BigDecimal giaGoc) {
        this.giaGoc = giaGoc;
    }

    public BigDecimal getGiaSauGiam() {
        return giaSauGiam;
    }

    public void setGiaSauGiam(BigDecimal giaSauGiam) {
        this.giaSauGiam = giaSauGiam;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }


}
