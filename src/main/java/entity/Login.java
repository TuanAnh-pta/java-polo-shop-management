package entity;

import lombok.*;

/**
 *
 * @author Admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login {
    private String TaiKhoan;
    private String MatKhau;
    private String MaNhanVien;
    private String VaiTro;
    private Boolean TrangThai;
}
