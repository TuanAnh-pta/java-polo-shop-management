/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author xuanTruong
 */
public class DotGiamGia {

    private String maDotGG;      
    private String tenDotGG;     
    private String loaiGiamGia;  
    private BigDecimal giaTri;       
    private java.sql.Date ngayBatDau;
    private java.sql.Date ngayKetThuc;
    private boolean trangThai;   
    private String moTa;

    public DotGiamGia() {
    }

    public DotGiamGia(String maDotGG, String tenDotGG, String loaiGiamGia, BigDecimal giaTri, Date ngayBatDau, Date ngayKetThuc, boolean trangThai, String moTa) {
        this.maDotGG = maDotGG;
        this.tenDotGG = tenDotGG;
        this.loaiGiamGia = loaiGiamGia;
        this.giaTri = giaTri;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.moTa = moTa;
    }

    public String getMaDotGG() {
        return maDotGG;
    }

    public void setMaDotGG(String maDotGG) {
        this.maDotGG = maDotGG;
    }

    public String getTenDotGG() {
        return tenDotGG;
    }

    public void setTenDotGG(String tenDotGG) {
        this.tenDotGG = tenDotGG;
    }

    public String getLoaiGiamGia() {
        return loaiGiamGia;
    }

    public void setLoaiGiamGia(String loaiGiamGia) {
        this.loaiGiamGia = loaiGiamGia;
    }

    public BigDecimal getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(BigDecimal giaTri) {
        this.giaTri = giaTri;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
}
