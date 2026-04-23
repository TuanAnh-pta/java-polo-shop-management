package entity;

import lombok.*;
import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder

public class giaodich {
    private String MaKhachHang;
    private String MaHoaDon;
    private String TenSanPham;
    private int SoLuong;
    private BigDecimal TongTien;
    private boolean TrangThai;
}
