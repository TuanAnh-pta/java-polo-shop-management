package entity;

import lombok.*;
import java.math.BigDecimal;


/**
 *
 * @author Admin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonChiTiet {
    private String MaHoaDon;
    private String MaChiTietHoaDon;
    private String MaSanPham;
    private int SoLuong;
    private String MaMauSac;
    private String MaKichThuoc;
    private String MaChatLieu;
    private String MaXuatSu;
    private BigDecimal DonGia;
}
