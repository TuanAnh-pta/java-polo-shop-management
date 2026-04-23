package entity;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private String gioiTinh;
    private String soDienThoai;
    private String email;
    private String maChucVu ;
    private int trangThai;

    public NhanVien() {
    }

    public NhanVien( String maNhanVien, String hoTen, String gioiTinh,
            String soDienThoai, String email,
            String chucVu, int trangThai) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.maChucVu = chucVu;
        this.trangThai = trangThai;
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

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

    


    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Object[] toDataRow() {
        return new Object[]{
            this.getMaNhanVien(),
            this.getHoTen(),
            this.getSoDienThoai(),
            this.getEmail(),
            this.getGioiTinh(),
            this.getMaChucVu(),
            (this.getTrangThai() == 0 ? "Đang làm" : "Ngừng làm")
        };
    }
    
    @Override
    public String toString() {
    return this.hoTen;
}
}
