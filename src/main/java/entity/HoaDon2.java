package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.math.BigDecimal;
/**
 *
 * @author Admin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon2 {
    private String MaHoaDon;
    private BigDecimal TongTien;
    private Date NgayTao;
    private String SoDienThoai;
    private BigDecimal GiaTri;
    private BigDecimal TienSauGiam;
    private boolean TrangThai;
    private String MaNhanVien;
    private String TenKhachHang;
    private String MaKhachHang;
    private String HoTen;
}
