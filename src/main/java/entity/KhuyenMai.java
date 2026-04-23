/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 * @author Nga Cọt
 */
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class KhuyenMai {
     private String maPhieu;
    private String tenPhieu;
    private String tenHinhThucGG;
    private double giaTri;
    private int soLuong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String dieuKienApDung;
    private boolean trangThai;

    // constructor, getter, setter

   @Override
public String toString() {
    DecimalFormat df = new DecimalFormat("#,###");
    return tenPhieu + " - Giảm " + df.format(giaTri) + (tenHinhThucGG.contains("%") ? "%" : "đ");
}


}
