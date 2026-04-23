/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import entity.DotGiamGia;
import java.util.List;

/**
 *
 * @author xuanTruong
 */
public interface DotGGDAO extends CrudDAO<DotGiamGia, String> {

    List<DotGiamGia> findMaDot(String maDotgg);

    List<DotGiamGia> findTrangThai();

    List<DotGiamGia> findByName(String name);

    boolean isMaDotTonTai(String maDot);

    int countMaDot(String maDot);
    
    DotGiamGia getDotGiamGiaHienTai();
}
