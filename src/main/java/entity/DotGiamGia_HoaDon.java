/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author xuanTruong
 */
public class DotGiamGia_HoaDon {

    private int id;
    private String maDotGG;
    private String maHoaDon;

    public DotGiamGia_HoaDon(int id, String maDotGG, String maHoaDon) {
        this.id = id;
        this.maDotGG = maDotGG;
        this.maHoaDon = maHoaDon;
    }

    public DotGiamGia_HoaDon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaDotGG() {
        return maDotGG;
    }

    public void setMaDotGG(String maDotGG) {
        this.maDotGG = maDotGG;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }
    
}
