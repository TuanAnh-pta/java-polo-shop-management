/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package DA1.poLoMen;

import Login.DangNhapJDiaLog1;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import ui.menumain;

/**
 *
 * @author Lenovo
 */
public class PoLoMen {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Tạo JDialog đăng nhập (modal = true => chặn chương trình tới khi đóng dialog)
            DangNhapJDiaLog1 loginDialog = new DangNhapJDiaLog1(new JFrame(), true);
           // loginDialog.setLocationRelativeTo(null); // Căn giữa màn hình
            loginDialog.setVisible(true); // Hiển thị hộp thoại đăng nhập

            // Sau khi JDialog đóng (dispose), nếu đăng nhập thành công thì vào giao diện chính
            if (loginDialog.isDisplayable() == false) {
                // Nếu bạn muốn kiểm tra trạng thái đăng nhập đúng/sai, có thể thêm biến cờ
                new menumain().setVisible(true);
            }
        });
    }
    
}
