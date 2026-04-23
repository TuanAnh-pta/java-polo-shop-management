package entity;

import java.sql.Date;

public class LichLamViec {

    private String maLLV;
    private String maNhanVien;
    private String hoTen;        // Lấy từ JOIN bảng NhanVien
    private Date ngayLam;
    private String caLam;
    private String ghiChu;

    public LichLamViec() {
    }

    public LichLamViec(String maLLV, String maNhanVien, String hoTen,
                       Date ngayLam, String caLam, String ghiChu) {
        this.maLLV = maLLV;
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.ngayLam = ngayLam;
        this.caLam = caLam;
        this.ghiChu = ghiChu;
    }

    public String getMaLLV() {
        return maLLV;
    }

    public void setMaLLV(String maLLV) {
        this.maLLV = maLLV;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Date getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(Date ngayLam) {
        this.ngayLam = ngayLam;
    }

    public String getCaLam() {
        return caLam;
    }

    public void setCaLam(String caLam) {
        this.caLam = caLam;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return "LichLamViec{" +
                "maLLV='" + maLLV + '\'' +
                ", maNhanVien='" + maNhanVien + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", ngayLam=" + ngayLam +
                ", caLam='" + caLam + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
