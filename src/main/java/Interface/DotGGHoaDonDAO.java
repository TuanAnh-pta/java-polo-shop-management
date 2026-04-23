/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import entity.DotGiamGia_HoaDon;
import java.util.List;

/**
 *
 * @author xuanTruong
 */
public interface DotGGHoaDonDAO {
    boolean insert(String maHD, String maDotGG);

    List<DotGiamGia_HoaDon> findByHoaDon(String maHD);
}
