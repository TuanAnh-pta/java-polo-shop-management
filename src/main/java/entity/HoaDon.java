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
import java.sql.Date;

/**
 *
 * @author Nga Cọt
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HoaDon {

    private String maHoaDon;        // Mã hóa đơn
    private String maKhachHang;     // Mã khách hàng
    private String maNhanVien;      // Mã nhân viên tạo hóa đơn

    private BigDecimal tongTien;    // Tổng tiền trước giảm giá
    private BigDecimal tienSauGiam; // Tổng tiền sau khi áp dụng giảm

    private BigDecimal giamGiaDot;  // Số tiền giảm từ ĐỢT GIẢM GIÁ
    private String maDotGG;         // Mã đợt giảm giá được áp dụng

    private boolean trangThai;      // Trạng thái thanh toán: true = đã trả, false = chưa trả

    private String soDienThoai;     // Dùng khi load dữ liệu hóa đơn
    private String email;           
    private String tenKhachHang;    

    private Date ngayTao;           // Thời gian tạo hóa đơn
}

