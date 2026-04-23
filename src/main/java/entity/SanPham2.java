/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Nga Cọt
 */
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class SanPham2 {
   private String MaSanPham;
    private String TenSanPham;
    private String MoTa;
    private String MaChatLieu;
    private String MaXuatSu;
    private boolean TrangThai;
    @Override
    public String toString(){
        return TenSanPham;
    }

    
}
