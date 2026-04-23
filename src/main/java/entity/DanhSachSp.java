package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DanhSachSp {
    private String MaChiTietSP;
    private String MaSanPham;
    private BigDecimal gia; 
    private String MaChatLieu;
    private String MaMauSac;
    private String MaKichThuoc;
    private int SoLuong;
    
            
}
