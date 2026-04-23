/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import entity.ChiTietSanPham2;
import java.util.List;

/**
 *
 * @author xuanTruong
 */
public interface ChiTietSanPham2DAO extends CrudDAO<ChiTietSanPham2, String>{
    List<ChiTietSanPham2> findMaDot(String maDot);
    
    List<ChiTietSanPham2> applyDiscount(String maCTSP, String maDotGG);
    List<ChiTietSanPham2> cancelDiscount(String maCTSP);
}
