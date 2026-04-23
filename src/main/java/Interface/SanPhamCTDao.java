/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import entity.ChiTietSanPham;
import java.util.List;

/**
 *
 * @author Nga Cọt
 */
public interface SanPhamCTDao extends CrudDAO<ChiTietSanPham,String>{
    List<ChiTietSanPham> findByMaSP(String maSP);
}
