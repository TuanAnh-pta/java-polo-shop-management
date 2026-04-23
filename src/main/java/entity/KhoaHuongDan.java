package entity;

import java.util.Date;

public class KhoaHuongDan {
    private String maKhoaHD;
    private String tieuDe;
    private String moTa;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private boolean trangThai;

    public KhoaHuongDan(String maKhoaHD, String tieuDe, String moTa, Date ngayBatDau, Date ngayKetThuc, boolean trangThai) {
        this.maKhoaHD = maKhoaHD;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }

    // getter và setter
    public String getMaKhoaHD() { return maKhoaHD; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public Date getNgayBatDau() { return ngayBatDau; }
    public Date getNgayKetThuc() { return ngayKetThuc; }
    public boolean isTrangThai() { return trangThai; }
}
