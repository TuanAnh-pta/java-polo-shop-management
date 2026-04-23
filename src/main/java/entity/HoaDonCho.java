package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
/**
 *
 * @author Admin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonCho {
    private String MaHoaDon;
    private Date NgayTao;
    private String MaNhanVien;
    private String TenNhanVien; 
    private String MaKhachHang;
    private boolean TrangThaiKH;
    private double TongTien;
    private String tenKhachHang;
}


