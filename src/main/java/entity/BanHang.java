/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 *
 * @author Lenovo
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class BanHang {
    private String MaSanPham;
    private String TenSanPham;
    private int soLuong;
  private BigDecimal gia;
}
