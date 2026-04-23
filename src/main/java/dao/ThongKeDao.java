/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ThongKe;
import java.util.Date;
import java.util.List;
import unti.XQuery;

/**
 *
 * @author hp
 */
public class ThongKeDao {
// Doanh thu theo Ngày trong khoảng từ - đến

    public List<ThongKe> getDoanhThuTheoNgay(Date tuNgay, Date denNgay) {
        String sql = "SELECT * FROM vw_DoanhThuTheoNgay WHERE NgayTao BETWEEN ? AND ?";
        return XQuery.getBeanList(ThongKe.class, sql, tuNgay, denNgay);
    }

    // Doanh thu theo Tuần (không cần lọc)
    public List<ThongKe> getDoanhThuTheoTuan() {
        String sql = "SELECT * FROM vw_DoanhThuTheoTuan";
        return XQuery.getBeanList(ThongKe.class, sql);
    }

    // Doanh thu theo Tháng (không cần lọc)
    public List<ThongKe> getDoanhThuTheoThang() {
        String sql = "SELECT * FROM vw_DoanhThuTheoThang";
        return XQuery.getBeanList(ThongKe.class, sql);
    }

    // Doanh thu theo Năm (không cần lọc)
    public List<ThongKe> getDoanhThuTheoNam() {
        String sql = "SELECT * FROM vw_DoanhThuTheoTuan";
        return XQuery.getBeanList(ThongKe.class, sql);
    }
}
