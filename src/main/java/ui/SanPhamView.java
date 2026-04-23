package ui;

import dao.ChatLieuDao;
import dao.KichThuocDao;
import dao.MauSacDao;
import dao.SanPham2Dao;
import dao.SanPhamCTDao;
import dao.SanPhamDao;
import dao.XuatXuDao;
import entity.ChatLieu;
import entity.ChiTietSanPham;
import entity.KichThuoc;
import entity.MauSac;

import entity.SanPham2;
import entity.XuatXu;
import java.awt.CardLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.smartcardio.Card;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class SanPhamView extends javax.swing.JInternalFrame {
//CardLayout cardLayout;

    SanPhamCTDao sanPhamCTDao = new SanPhamCTDao();
    List<ChiTietSanPham> lsSPCT = new ArrayList<>();
    SanPham2Dao sanPhamDao = new SanPham2Dao();
    List<SanPham2> LsSp = new ArrayList<>();
    KichThuocDao kichThDao = new KichThuocDao();
    List<KichThuoc> lsKichTh = new ArrayList<>();
    MauSacDao mauDao = new MauSacDao();
    List<MauSac> lsMau = new ArrayList<>();
    ChatLieuDao chatLieuDao = new ChatLieuDao();
    List<ChatLieu> lsChatLieu = new ArrayList<>();
    XuatXuDao xuatXuDao = new XuatXuDao();
    List<XuatXu> lsXuatXu = new ArrayList<>();
    int lastSelectedRow = -1; // Khai báo ở đầu class

    public SanPhamView() {
        initComponents();
        FillSanPhamCT();
        FillSanPham();
        findMau();
        findKichThuoc();
        findChatLieu();
        fillMauSac();
        findKichT();
        FindChatLieu();
        FindXuatXu();
        initCardLayout();
        findXuatXu();
    }

    private void initCardLayout() {
        jPanelCardContainer.setLayout(new CardLayout());
        jPanelCardContainer.add(panelMauSac1, "panelMauSac1");
        jPanelCardContainer.add(panelChatLieu1, "panelChatLieu1");
        jPanelCardContainer.add(panelKichThuoc1, "panelKichThuoc1");
        jPanelCardContainer.add(panelXuatXu, "panelXuatXu");
    }

    private void FillSanPhamCT() {
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow < 0 || selectedRow >= LsSp.size()) {
            return;
        }
        // Lấy mã sản phẩm từ đối tượng SanPham tương ứng
        SanPham2 selectedSanPham = LsSp.get(selectedRow);
        String maSP = selectedSanPham.getMaSanPham();

        // Tìm danh sách chi tiết theo mã sản phẩm
        lsSPCT = sanPhamCTDao.findByMaSP(maSP);

        // Đổ dữ liệu lên bảng chi tiết sản phẩm
        DefaultTableModel modelCT = (DefaultTableModel) tblSanPCT.getModel();
        modelCT.setRowCount(0);

        for (ChiTietSanPham spct : lsSPCT) {
            Object[] row = {
                spct.getMaChiTietSP(),
                spct.getSoLuong(),
                spct.getGia(),
                sanPhamDao.findByMa(spct.getMaSanPham()).getTenSanPham(),
                mauDao.findById(spct.getMaMauSac()).getTenMau(),
                kichThDao.findById(spct.getMaKichThuoc()).getKichThuoc()
            };
            modelCT.addRow(row);
        }

    }

    private void clean() {
        txtMaChiT.setText("");
        txtSoLuong.setText("");
        txtGia.setText("");
        //   txtGiaSauGiam.setText("");
        lblTenSp.setText("");
        cboMau.setSelectedIndex(-1);
        cboKichThuoc.setSelectedIndex(-1);
        lblMaSp.setText("");

    }

    private void cleanSp() {
        txtMaSanPham.setText("");
        txtTenSanPham.setText("");
        txtMoTa.setText("");
        cboChatLieu.setSelectedIndex(-1);
        cboXX.setSelectedIndex(-1);
        buttonGroup1.clearSelection();
    }

    private void FillSanPham() {
        DefaultTableModel modelSp = (DefaultTableModel) tblSanPham.getModel();
        modelSp.setRowCount(0);
        LsSp = sanPhamDao.getAll();
        for (SanPham2 sanPham : LsSp) {
            Object[] rowData = {
                sanPham.getMaSanPham(),
                sanPham.getTenSanPham(),
                sanPham.getMoTa(),
                chatLieuDao.finById(sanPham.getMaChatLieu()).getChatLieu(),
                xuatXuDao.findByMaXX(sanPham.getMaXuatSu()).getNoiNhap(),
                sanPham.isTrangThai() ? "Còn hàng" : "Hết hàng"
            };
            modelSp.addRow(rowData);
        }
    }

    private void findMau() {
        DefaultComboBoxModel comboBoxModelMS = (DefaultComboBoxModel) cboMau.getModel();
        comboBoxModelMS.removeAllElements();
        comboBoxModelMS.addElement(" ");
        lsMau = mauDao.getAll();
        for (MauSac mau : lsMau) {
            comboBoxModelMS.addElement(mau);
        }
        cboMau.setSelectedIndex(0);
    }

    private void findKichThuoc() {
        DefaultComboBoxModel cboModelKT = (DefaultComboBoxModel) cboKichThuoc.getModel();
        cboModelKT.removeAllElements();
        cboModelKT.addElement("");
        lsKichTh = kichThDao.getAll();
        for (KichThuoc kichThuoc : lsKichTh) {
            cboModelKT.addElement(kichThuoc);
        }
        cboKichThuoc.setSelectedIndex(0);
    }

    private void findChatLieu() {
        DefaultComboBoxModel cbModelCL = (DefaultComboBoxModel) cboChatLieu.getModel();
        cbModelCL.removeAllElements();
        cbModelCL.addElement(" ");
        lsChatLieu = chatLieuDao.getAll();
        for (ChatLieu chatLieu : lsChatLieu) {
            cbModelCL.addElement(chatLieu);
        }
        cboChatLieu.setSelectedIndex(0); // hoặc -1 nếu muốn hoàn toàn không chọn
    }

    private void setFromChiTiet(int indexRow) {
        ChiTietSanPham sanPhamCT = lsSPCT.get(indexRow);

        txtMaChiT.setText(sanPhamCT.getMaChiTietSP());
        txtSoLuong.setText(String.valueOf(sanPhamCT.getSoLuong()));
        txtGia.setText(String.valueOf(sanPhamCT.getGia()));
        SanPham2 sanp = sanPhamDao.findByMa(sanPhamCT.getMaSanPham());
        if (sanp != null) {
            lblTenSp.setText(sanp.getTenSanPham());
        }
        //txtMaSP.setText(sanPhamCT.getMaSanPham());
        MauSac mau = mauDao.findById(sanPhamCT.getMaMauSac());
        cboMau.setSelectedItem(mau);
        KichThuoc kichT = kichThDao.findById(sanPhamCT.getMaKichThuoc());
        cboKichThuoc.setSelectedItem(kichT);
    }

    private boolean validateFormChiTiet() {
        if (txtMaChiT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Chi Tiết");
            return false;
        }

        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải >= 0");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ");
            return false;
        }

//        try {
//            double gia = Double.parseDouble(txtGia.getText().trim());
//            double giaGiam = Double.parseDouble(txtGiaSauGiam.getText().trim());
//            if (gia < 0 || giaGiam < 0) {
//                JOptionPane.showMessageDialog(this, "Giá hoặc giá giảm phải >= 0");
//                return false;
//            }
//            
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Giá hoặc giá sau giảm không hợp lệ");
//            return false;
//        }
        if (lblTenSp.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập Tên Sản Phẩm");
            return false;
        }

        if (cboMau.getSelectedItem() == null || cboKichThuoc.getSelectedItem() == null
                || cboMau.getSelectedItem().toString().trim().isEmpty()
        || cboKichThuoc.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Màu và Kích thước");
            return false;
        }
        
        return true;
    }

    private ChiTietSanPham getDataFromCT() {
        ChiTietSanPham chiTiet = new ChiTietSanPham();
    chiTiet.setMaChiTietSP(txtMaChiT.getText());
    chiTiet.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
    
    // Chuyển String thành BigDecimal
    String giaStr = txtGia.getText();
    if (giaStr != null && !giaStr.trim().isEmpty()) {
        chiTiet.setGia(new BigDecimal(giaStr.trim()));
    } else {
        chiTiet.setGia(BigDecimal.ZERO); // hoặc xử lý phù hợp khi giá không nhập
    }
        //String tenSP = txtTen.getText();
//        for (SanPham2 sp : LsSp) {
//            if (sp.getTenSanPham().equals(tenSP)) {
//                chiTiet.setMaSanPham(sp.getMaSanPham());
//                break;
//            }
//        }
        // chiTiet.setMaSanPham(txtMaSP.getText());
        MauSac mauSac = (MauSac) cboMau.getSelectedItem();
        chiTiet.setMaMauSac(mauSac.getMaMauSac());
        KichThuoc kichThuoc = (KichThuoc) cboKichThuoc.getSelectedItem();
        chiTiet.setMaKichThuoc(kichThuoc.getMaKichThuoc());
        chiTiet.setMaSanPham(lblMaSp.getText());
        return chiTiet;

    }

    private void setFromSanPHam(int indexRow) {
        SanPham2 sanpham = LsSp.get(indexRow);
        txtMaSanPham.setText(sanpham.getMaSanPham());
        txtTenSanPham.setText(sanpham.getTenSanPham());
        txtMoTa.setText(sanpham.getMoTa());
        ChatLieu chatLieu = chatLieuDao.finById(sanpham.getMaChatLieu());
        cboChatLieu.setSelectedItem(chatLieu);
        XuatXu xuatXu = xuatXuDao.findByMaXX(sanpham.getMaXuatSu());
        cboXX.setSelectedItem(xuatXu);
        rdoCon.setSelected(sanpham.isTrangThai());
        rdoHet.setSelected(!sanpham.isTrangThai());
    }

    private boolean validateFormSanPham() {
        String MaSP = txtMaSanPham.getText().trim();
        String tenSp = txtTenSanPham.getText().trim();
        String mota = txtMoTa.getText().trim();
        if (MaSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Sản Phẩm");
            return false;
        }

        if (tenSp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên Sản Phẩm");
            return false;
        }
        if (mota.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mô tả ");
            txtMoTa.requestFocus();
            return false;
        }
        if (cboChatLieu.getSelectedItem() == null||
                cboChatLieu.getSelectedItem().toString().trim().isBlank()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Chất Liệu");
            return false;
        }
        if (!tenSp.matches("^[^0-9]+$")) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không thể chứa số");
            txtTenSanPham.requestFocus();
            return false;
        }
        if (!mota.matches("^[^0-9]+$")) {
            JOptionPane.showMessageDialog(this, "Mô tả không thể chứa số");
            txtMoTa.requestFocus();
            return false;
        }
        if (cboXX.getSelectedItem() == null||
                cboXX.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn xuất xứ");
            return false;
        }
        if (buttonGroup1.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái sản phẩm!");
            return false;
        }
        if (!rdoCon.isSelected() && !rdoHet.isSelected()) {
          JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái Còn hoặc Hết");
        return false;
        }

        return true;
    }

    private SanPham2 getFromSanPham() {
        SanPham2 sanPham2 = new SanPham2();

        sanPham2.setMaSanPham(txtMaSanPham.getText());
        sanPham2.setTenSanPham(txtTenSanPham.getText());
        sanPham2.setMoTa(txtMoTa.getText());
        ChatLieu chatLieu = (ChatLieu) cboChatLieu.getSelectedItem();
        sanPham2.setMaChatLieu(chatLieu.getMaChatLieu());
        XuatXu xuatXu = (XuatXu) cboXX.getSelectedItem();
        sanPham2.setMaXuatSu(xuatXu.getMaXuatSu());
        sanPham2.setTrangThai(rdoCon.isSelected());
        return sanPham2;
    }

    private void fillMauSac() {
        DefaultTableModel model = (DefaultTableModel) tblMau.getModel();
        model.setRowCount(0);
        lsMau = mauDao.getAll();
        for (MauSac mauSac : lsMau) {
            Object[] row = {
                mauSac.getMaMauSac(),
                mauSac.getTenMau()
            };
            model.addRow(row);
        }
    }

    private void setFromMau(int indexRow) {
        MauSac mauSac = lsMau.get(indexRow);
        txtMaMau.setText(mauSac.getMaMauSac());
        txtTenMau.setText(mauSac.getTenMau());

    }

    private boolean validateFormMauSac() {
    String maMau = txtMaMau.getText();
    String tenMau = txtTenMau.getText();

    // Kiểm tra rỗng
    if (maMau.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Màu");
        txtMaMau.requestFocus();
        return false;
    }
    if (tenMau.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên Màu");
        txtTenMau.requestFocus();
        return false;
    }

    // Kiểm tra không bắt đầu bằng dấu cách
    if (maMau.startsWith(" ")) {
        JOptionPane.showMessageDialog(this, "Mã Màu không được bắt đầu bằng dấu cách");
        txtMaMau.requestFocus();
        return false;
    }
    if (tenMau.startsWith(" ")) {
        JOptionPane.showMessageDialog(this, "Tên Màu không được bắt đầu bằng dấu cách");
        txtTenMau.requestFocus();
        return false;
    }

    // Kiểm tra tên màu không chứa số
    if (!tenMau.matches("^[^0-9]+$")) {
        JOptionPane.showMessageDialog(this, "Tên Màu không được chứa số");
        txtTenMau.requestFocus();
        return false;
    }

    return true;
}


    private MauSac getFromMau() {
        MauSac mauSac = new MauSac();
        mauSac.setMaMauSac(txtMaMau.getText());
        mauSac.setTenMau(txtTenMau.getText());
        return mauSac;

    }

    private void clearMau() {
        txtMaMau.setText("");
        txtTenMau.setText("");
    }

    private void findKichT() {
        DefaultTableModel model = (DefaultTableModel) tblKichThuoc.getModel();
        model.setRowCount(0);
        lsKichTh = kichThDao.getAll();
        for (KichThuoc kichThuoc : lsKichTh) {
            Object[] row = {
                kichThuoc.getMaKichThuoc(),
                kichThuoc.getKichThuoc()
            };
            model.addRow(row);
        }
    }

    private void setFromKichTh(int indexRow) {
        KichThuoc kichThuoc = lsKichTh.get(indexRow);
        txtMaKichThuoc.setText(kichThuoc.getMaKichThuoc());
        txtKichThuoc.setText(kichThuoc.getKichThuoc());

    }

    private boolean validateFormKichT() {
    String maKichThuoc = txtMaKichThuoc.getText();
    String kichThuoc = txtKichThuoc.getText();

    // Kiểm tra rỗng
    if (maKichThuoc.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập mã kích thước");
        txtMaKichThuoc.requestFocus();
        return false;
    }
    if (kichThuoc.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập kích thước");
        txtKichThuoc.requestFocus();
        return false;
    }

    // Kiểm tra dấu cách ở đầu
    if (maKichThuoc.startsWith(" ")) {
        JOptionPane.showMessageDialog(this, "Mã kích thước không được bắt đầu bằng dấu cách");
        txtMaKichThuoc.requestFocus();
        return false;
    }
    if (kichThuoc.startsWith(" ")) {
        JOptionPane.showMessageDialog(this, "Kích thước không được bắt đầu bằng dấu cách");
        txtKichThuoc.requestFocus();
        return false;
    }

    // Không cho phép chứa số
    if (!kichThuoc.matches("^[^0-9]+$")) {
        JOptionPane.showMessageDialog(this, "Kích thước không được chứa số");
        txtKichThuoc.requestFocus();
        return false;
    }

    return true;
}

    private KichThuoc getFromKT() {
        KichThuoc kichThuoc = new KichThuoc();
        kichThuoc.setMaKichThuoc(txtMaKichThuoc.getText());
        kichThuoc.setKichThuoc(txtKichThuoc.getText());
        return kichThuoc;
    }

    private void clearKT() {
        txtMaKichThuoc.setText("");
        txtKichThuoc.setText("");
    }

    private void FindChatLieu() {
        DefaultTableModel modelCl = (DefaultTableModel) tblChatLieu3.getModel();
        modelCl.setRowCount(0);

        lsChatLieu = chatLieuDao.getAll();
        for (ChatLieu chatLieu : lsChatLieu) {
            Object[] row = {
                chatLieu.getMaChatLieu(),
                chatLieu.getChatLieu(),};
            modelCl.addRow(row);
        }
    }

    private void FindXuatXu() {
        DefaultComboBoxModel boxModel = (DefaultComboBoxModel) cboXX.getModel();
        cboXX.removeAllItems();
        boxModel.addElement("");
        lsXuatXu = xuatXuDao.getAll();
        for (XuatXu xuatXu : lsXuatXu) {
            boxModel.addElement(xuatXu);
        }
        cboXX.setSelectedIndex(0);
    }

    private void setFromCL(int indexRow) {
        ChatLieu chatLieu = lsChatLieu.get(indexRow);
        txtMaCL3.setText(chatLieu.getMaChatLieu());
        txtCL3.setText(chatLieu.getChatLieu());

    }

   private boolean validateFormChatLieu() {
    String maCl = txtMaCL3.getText();
    String Cl = txtCL3.getText();

    // Kiểm tra rỗng
    if (maCl.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập mã chất liệu");
        txtMaCL3.requestFocus();
        return false;
    }
    if (Cl.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập chất liệu");
        txtCL3.requestFocus();
        return false;
    }

    // Kiểm tra không bắt đầu bằng dấu cách
    if (maCl.startsWith(" ")) {
        JOptionPane.showMessageDialog(this, "Mã chất liệu không được bắt đầu bằng dấu cách");
        txtMaCL3.requestFocus();
        return false;
    }
    if (Cl.startsWith(" ")) {
        JOptionPane.showMessageDialog(this, "Chất liệu không được bắt đầu bằng dấu cách");
        txtCL3.requestFocus();
        return false;
    }

    // Kiểm tra chất liệu không chứa số
    if (!Cl.matches("^[^0-9]+$")) {
        JOptionPane.showMessageDialog(this, "Chất liệu không thể chứa số");
        txtCL3.requestFocus();
        return false;
    }

    return true;
}


    private ChatLieu getFromCl() {
        ChatLieu chatLieu = new ChatLieu();
        chatLieu.setMaChatLieu(txtMaCL3.getText());
        chatLieu.setChatLieu(txtCL3.getText());

        return chatLieu;
    }

    private void clearCl() {
        txtMaCL3.setText("");
        txtCL3.setText("");
    }

    private void findXuatXu() {
        DefaultTableModel model = (DefaultTableModel) tblXuatXu.getModel();
        model.setRowCount(0);
        lsXuatXu = xuatXuDao.getAll();
        for (XuatXu xuatXu : lsXuatXu) {
            Object[] value = {
                xuatXu.getMaXuatSu(),
                xuatXu.getNoiNhap()
            };
            model.addRow(value);
        }
    }

    private void setFormXX(int indexRow) {
        XuatXu xuatXu = lsXuatXu.get(indexRow);
        txtMaXuatXu.setText(xuatXu.getMaXuatSu());
        txtNoiNhap.setText(xuatXu.getNoiNhap());
    }

    private XuatXu getFormXX() {
        XuatXu xuatXu = new XuatXu();
        xuatXu.setMaXuatSu(txtMaXuatXu.getText());
        xuatXu.setNoiNhap(txtNoiNhap.getText());
        return xuatXu;
    }

   private boolean validateFormXuatXu() {
    String maXX = txtMaXuatXu.getText();
    String noiNhap = txtNoiNhap.getText();

    if (maXX.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập mã xuất xứ");
        txtMaXuatXu.requestFocus();
        return false;
    }
    if (noiNhap.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập nơi nhập");
        txtNoiNhap.requestFocus();
        return false;
    }

    if (maXX.startsWith(" ")) {
        JOptionPane.showMessageDialog(this, "Mã xuất xứ không được bắt đầu bằng dấu cách");
        txtMaXuatXu.requestFocus();
        return false;
    }
    if (noiNhap.startsWith(" ")) {
        JOptionPane.showMessageDialog(this, "Nơi nhập không được bắt đầu bằng dấu cách");
        txtNoiNhap.requestFocus();
        return false;
    }

    if (!noiNhap.matches("^[^0-9]+$")) {
        JOptionPane.showMessageDialog(this, "Nơi nhập không được chứa số");
        txtNoiNhap.requestFocus();
        return false;
    }
    return true;
}


    public void clearXX() {
        txtMaXuatXu.setText("");
        txtNoiNhap.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        tabs = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        txtTim = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtMaSanPham = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTenSanPham = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtMoTa = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboChatLieu = new javax.swing.JComboBox<>();
        cboXuatXu = new javax.swing.JLabel();
        cboXX = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        rdoCon = new javax.swing.JRadioButton();
        rdoHet = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        btnThemSanPham = new javax.swing.JButton();
        btnXoaSp = new javax.swing.JButton();
        btnLamMoiSP = new javax.swing.JButton();
        btnSuaSp = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPCT = new javax.swing.JTable();
        txtTimSpCT = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btnTimSPCT = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtMaChiT = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        txtGia = new javax.swing.JTextField();
        cboMau = new javax.swing.JComboBox<>();
        cboKichThuoc = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        lblMaSp = new javax.swing.JLabel();
        lblTenSp = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnMauSac = new javax.swing.JButton();
        btnKichThuoc = new javax.swing.JButton();
        btnChatLieu = new javax.swing.JButton();
        btnXuatXu = new javax.swing.JButton();
        jPanelCardContainer = new javax.swing.JPanel();
        panelKichThuoc1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtMaKichThuoc = new javax.swing.JTextField();
        txtKichThuoc = new javax.swing.JTextField();
        btnTimKichT = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        btnThemKichTH = new javax.swing.JButton();
        btnLamM = new javax.swing.JButton();
        btnSuaKichT = new javax.swing.JButton();
        btnXoaKichTH = new javax.swing.JButton();
        txtTimKichTh = new javax.swing.JTextField();
        btnHT = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblKichThuoc = new javax.swing.JTable();
        panelChatLieu1 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        txtCL3 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        btnTimCL3 = new javax.swing.JButton();
        txtTimCL3 = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblChatLieu3 = new javax.swing.JTable();
        txtMaCL3 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnThemCL3 = new javax.swing.JButton();
        btnSuaChatLieu3 = new javax.swing.JButton();
        btnXoaCL3 = new javax.swing.JButton();
        btnLamMoiCL3 = new javax.swing.JButton();
        panelXuatXu = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtMaXuatXu = new javax.swing.JTextField();
        txtNoiNhap = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        btnThemXX = new javax.swing.JButton();
        btnSuaXX = new javax.swing.JButton();
        btnXoaXX = new javax.swing.JButton();
        btnLMXX = new javax.swing.JButton();
        txtTimXuatXu = new javax.swing.JTextField();
        btnTimXX = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblXuatXu = new javax.swing.JTable();
        btnHienThiXX = new javax.swing.JButton();
        panelMauSac1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblMau = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtMaMau = new javax.swing.JTextField();
        txtTenMau = new javax.swing.JTextField();
        btnTimMau = new javax.swing.JButton();
        txtTimMau = new javax.swing.JTextField();
        btnHienThi = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        btnThemMau = new javax.swing.JButton();
        btnSuaMau = new javax.swing.JButton();
        btnXoaMau = new javax.swing.JButton();
        btnLm = new javax.swing.JButton();

        tabs.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Sản Phẩm "));

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm ", "Mô tả ", "Mã chất liệu", "Xuất xứ ", "Trạng Thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSanPham);

        btnTim.setBackground(new java.awt.Color(255, 153, 0));
        btnTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnTim.setText("Tìm kiếm");
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 153, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Open file.png"))); // NOI18N
        jButton1.setText("Hiển Thị ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(txtTim, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnTim)
                .addGap(48, 48, 48)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTim)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông Tin"));

        jLabel8.setText("Mã sản phẩm : ");

        jLabel9.setText("Tên sản phẩm :");

        jLabel10.setText("Mô tả : ");

        jLabel11.setText("Chất liệu : ");

        cboChatLieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChatLieuActionPerformed(evt);
            }
        });

        cboXuatXu.setText("Xuất xứ: ");

        cboXX.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel21.setText("Trạng thái:");

        buttonGroup1.add(rdoCon);
        rdoCon.setText("Còn hàng");
        rdoCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoConActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoHet);
        rdoHet.setText("Hết hàng ");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Mời chọn"));

        btnThemSanPham.setBackground(new java.awt.Color(255, 153, 51));
        btnThemSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Add.png"))); // NOI18N
        btnThemSanPham.setText("Thêm ");
        btnThemSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSanPhamActionPerformed(evt);
            }
        });

        btnXoaSp.setBackground(new java.awt.Color(255, 153, 0));
        btnXoaSp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Delete.png"))); // NOI18N
        btnXoaSp.setText("Xóa");
        btnXoaSp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSpActionPerformed(evt);
            }
        });

        btnLamMoiSP.setBackground(new java.awt.Color(255, 153, 0));
        btnLamMoiSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Refresh.png"))); // NOI18N
        btnLamMoiSP.setText("Làm Mới ");
        btnLamMoiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiSPActionPerformed(evt);
            }
        });

        btnSuaSp.setBackground(new java.awt.Color(255, 153, 0));
        btnSuaSp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Edit.png"))); // NOI18N
        btnSuaSp.setText("Sửa");
        btnSuaSp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaSpActionPerformed1(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoiSP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnXoaSp, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addComponent(btnSuaSp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemSanPham)
                    .addComponent(btnSuaSp, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLamMoiSP)
                    .addComponent(btnXoaSp))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                            .addComponent(txtMoTa)))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                            .addComponent(cboXuatXu)
                            .addGap(46, 46, 46)
                            .addComponent(cboXX, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(139, 139, 139)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addComponent(rdoCon)
                        .addGap(18, 18, 18)
                        .addComponent(rdoHet))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(106, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoHet)
                            .addComponent(rdoCon)
                            .addComponent(jLabel21))
                        .addGap(34, 34, 34)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cboChatLieu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboXX, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboXuatXu, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 86, Short.MAX_VALUE))
        );

        tabs.addTab("Sản Phẩm", jPanel4);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Sản Phẩm Chi Tiết "));

        tblSanPCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã CT", "Số lượng ", "Giá ", "Tên SP", "Màu sắc", "Size"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPCTMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPCT);

        btnTimSPCT.setBackground(new java.awt.Color(255, 153, 0));
        btnTimSPCT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnTimSPCT.setText("Tìm Kiếm ");
        btnTimSPCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimSPCTActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 153, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Open file.png"))); // NOI18N
        jButton2.setText("Hiển Thị ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtTimSpCT, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(411, 411, 411)
                                .addComponent(jLabel12))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnTimSPCT)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimSpCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimSPCT)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin chi tiết"));

        jLabel1.setText("Mã chi tiết: ");

        jLabel2.setText("Số lượng: ");

        jLabel3.setText("Giá : ");

        jLabel5.setText("Tên sản phẩm ");

        jLabel6.setText("Màu sắc ");

        jLabel7.setText("Kích thước ");

        txtMaChiT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaChiTActionPerformed(evt);
            }
        });

        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });

        cboMau.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboKichThuoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel19.setText("Mã sản phẩm");

        btnThem.setBackground(new java.awt.Color(255, 153, 51));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Add.png"))); // NOI18N
        btnThem.setText("Thêm ");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 153, 0));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Edit.png"))); // NOI18N
        btnSua.setText("Sửa ");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 153, 0));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setBackground(new java.awt.Color(255, 153, 0));
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Refresh.png"))); // NOI18N
        btnLamMoi.setText("Làm Mới ");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(389, 389, 389))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)))
                .addGap(471, 471, 471))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel1))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTenSp, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtMaChiT, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel7))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(18, 18, 18)
                                .addComponent(lblMaSp, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(btnSua)
                        .addGap(36, 36, 36)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(cboMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(lblMaSp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTenSp, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMaChiT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(cboKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnLamMoi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnMauSac.setText("Màu Sắc");
        btnMauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMauSacActionPerformed(evt);
            }
        });

        btnKichThuoc.setText("Kích Thước");
        btnKichThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKichThuocActionPerformed(evt);
            }
        });

        btnChatLieu.setText("Chất liệu");
        btnChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChatLieuActionPerformed(evt);
            }
        });

        btnXuatXu.setText("Xuất xứ");
        btnXuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatXuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelCardContainer.setPreferredSize(new java.awt.Dimension(471, 500));
        jPanelCardContainer.setRequestFocusEnabled(false);
        jPanelCardContainer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelKichThuoc1.setBorder(javax.swing.BorderFactory.createTitledBorder("Thuộc tính"));
        panelKichThuoc1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel14.setText("Kích Thước");
        panelKichThuoc1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, -1, -1));

        jLabel17.setText("Mã kích thước");
        panelKichThuoc1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jLabel18.setText("Kích thước");
        panelKichThuoc1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));
        panelKichThuoc1.add(txtMaKichThuoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 320, -1));

        txtKichThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKichThuocActionPerformed(evt);
            }
        });
        panelKichThuoc1.add(txtKichThuoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 320, -1));

        btnTimKichT.setBackground(new java.awt.Color(255, 153, 0));
        btnTimKichT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnTimKichT.setText("Tìm");
        btnTimKichT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKichTActionPerformed(evt);
            }
        });
        panelKichThuoc1.add(btnTimKichT, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 240, 90, -1));

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Mời chọn"));

        btnThemKichTH.setBackground(new java.awt.Color(255, 153, 0));
        btnThemKichTH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Add.png"))); // NOI18N
        btnThemKichTH.setText("Thêm");
        btnThemKichTH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKichTHActionPerformed(evt);
            }
        });

        btnLamM.setBackground(new java.awt.Color(255, 153, 0));
        btnLamM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Refresh.png"))); // NOI18N
        btnLamM.setText("Làm Mới");
        btnLamM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMActionPerformed(evt);
            }
        });

        btnSuaKichT.setBackground(new java.awt.Color(255, 153, 0));
        btnSuaKichT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Edit.png"))); // NOI18N
        btnSuaKichT.setText("Sửa");
        btnSuaKichT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKichTActionPerformed(evt);
            }
        });

        btnXoaKichTH.setBackground(new java.awt.Color(255, 153, 0));
        btnXoaKichTH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Delete.png"))); // NOI18N
        btnXoaKichTH.setText("Xóa");
        btnXoaKichTH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaKichTHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(btnThemKichTH)
                .addGap(28, 28, 28)
                .addComponent(btnSuaKichT)
                .addGap(18, 18, 18)
                .addComponent(btnXoaKichTH)
                .addGap(18, 18, 18)
                .addComponent(btnLamM)
                .addGap(0, 7, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemKichTH)
                    .addComponent(btnSuaKichT)
                    .addComponent(btnXoaKichTH)
                    .addComponent(btnLamM))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        panelKichThuoc1.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 430, -1));

        txtTimKichTh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKichThActionPerformed(evt);
            }
        });
        panelKichThuoc1.add(txtTimKichTh, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 220, 30));

        btnHT.setBackground(new java.awt.Color(255, 153, 0));
        btnHT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Open file.png"))); // NOI18N
        btnHT.setText("Hiển Thị");
        btnHT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHTActionPerformed(evt);
            }
        });
        panelKichThuoc1.add(btnHT, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, 100, -1));

        tblKichThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Kích Thước", "Kích thước "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKichThuoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKichThuocMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblKichThuoc);

        panelKichThuoc1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 440, 150));

        jPanelCardContainer.add(panelKichThuoc1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 478, -1, -1));

        panelChatLieu1.setBorder(javax.swing.BorderFactory.createTitledBorder("Thuộc tính"));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel30.setText("Chất Liệu ");

        jLabel31.setText("Chất liệu");

        btnTimCL3.setBackground(new java.awt.Color(255, 153, 0));
        btnTimCL3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnTimCL3.setText("Tìm");
        btnTimCL3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimCLActionPerformed(evt);
            }
        });

        tblChatLieu3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã chất liệu", "Chất liệu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblChatLieu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChatLieuMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblChatLieu3);

        txtMaCL3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaCLActionPerformed(evt);
            }
        });

        jLabel32.setText("Mã chất liệu");

        jButton9.setBackground(new java.awt.Color(255, 153, 0));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Open file.png"))); // NOI18N
        jButton9.setText("Hiển thị ");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Mời chọn"));

        btnThemCL3.setBackground(new java.awt.Color(255, 153, 0));
        btnThemCL3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Add.png"))); // NOI18N
        btnThemCL3.setText("Thêm ");
        btnThemCL3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemCLActionPerformed(evt);
            }
        });

        btnSuaChatLieu3.setBackground(new java.awt.Color(255, 153, 0));
        btnSuaChatLieu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Edit.png"))); // NOI18N
        btnSuaChatLieu3.setText("Sửa");
        btnSuaChatLieu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaChatLieuActionPerformed(evt);
            }
        });

        btnXoaCL3.setBackground(new java.awt.Color(255, 153, 0));
        btnXoaCL3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Delete.png"))); // NOI18N
        btnXoaCL3.setText("Xóa");
        btnXoaCL3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaCLActionPerformed(evt);
            }
        });

        btnLamMoiCL3.setBackground(new java.awt.Color(255, 153, 0));
        btnLamMoiCL3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Refresh.png"))); // NOI18N
        btnLamMoiCL3.setText("Làm Mới");
        btnLamMoiCL3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiCLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnThemCL3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSuaChatLieu3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoaCL3)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoiCL3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemCL3)
                    .addComponent(btnSuaChatLieu3)
                    .addComponent(btnXoaCL3)
                    .addComponent(btnLamMoiCL3))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelChatLieu1Layout = new javax.swing.GroupLayout(panelChatLieu1);
        panelChatLieu1.setLayout(panelChatLieu1Layout);
        panelChatLieu1Layout.setHorizontalGroup(
            panelChatLieu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChatLieu1Layout.createSequentialGroup()
                .addGroup(panelChatLieu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelChatLieu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panelChatLieu1Layout.createSequentialGroup()
                            .addGap(147, 147, 147)
                            .addComponent(jLabel30))
                        .addGroup(panelChatLieu1Layout.createSequentialGroup()
                            .addGap(27, 27, 27)
                            .addGroup(panelChatLieu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel32)
                                .addComponent(jLabel31))
                            .addGap(36, 36, 36)
                            .addGroup(panelChatLieu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtMaCL3, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                .addComponent(txtCL3)))
                        .addGroup(panelChatLieu1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(txtTimCL3, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnTimCL3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton9))
                        .addGroup(panelChatLieu1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        panelChatLieu1Layout.setVerticalGroup(
            panelChatLieu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChatLieu1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel30)
                .addGap(20, 20, 20)
                .addGroup(panelChatLieu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(txtMaCL3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(panelChatLieu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtCL3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelChatLieu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTimCL3)
                    .addComponent(txtTimCL3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelCardContainer.add(panelChatLieu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 974, -1, -1));

        panelXuatXu.setBorder(javax.swing.BorderFactory.createTitledBorder("Thuộc tính"));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel4.setText("Xuất Xứ");

        jLabel20.setText("Mã xuất xứ:");

        jLabel22.setText("Nơi nhập:");

        txtMaXuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaXuatXuActionPerformed(evt);
            }
        });

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Mời chọn"));

        btnThemXX.setBackground(new java.awt.Color(255, 153, 0));
        btnThemXX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Add.png"))); // NOI18N
        btnThemXX.setText("Thêm");
        btnThemXX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemXXActionPerformed(evt);
            }
        });

        btnSuaXX.setBackground(new java.awt.Color(255, 153, 0));
        btnSuaXX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Edit.png"))); // NOI18N
        btnSuaXX.setText("Sửa");
        btnSuaXX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaXXActionPerformed(evt);
            }
        });

        btnXoaXX.setBackground(new java.awt.Color(255, 153, 0));
        btnXoaXX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Delete.png"))); // NOI18N
        btnXoaXX.setText("Xóa");
        btnXoaXX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaXXActionPerformed(evt);
            }
        });

        btnLMXX.setBackground(new java.awt.Color(255, 153, 0));
        btnLMXX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Refresh.png"))); // NOI18N
        btnLMXX.setText("Làm mới");
        btnLMXX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLMXXActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnThemXX)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSuaXX)
                .addGap(12, 12, 12)
                .addComponent(btnXoaXX)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLMXX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemXX)
                    .addComponent(btnSuaXX)
                    .addComponent(btnXoaXX)
                    .addComponent(btnLMXX, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        btnTimXX.setBackground(new java.awt.Color(255, 153, 0));
        btnTimXX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnTimXX.setText("Tìm");
        btnTimXX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimXXActionPerformed(evt);
            }
        });

        tblXuatXu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã xuất xứ ", "Nơi nhập"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblXuatXu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblXuatXuMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblXuatXu);

        btnHienThiXX.setBackground(new java.awt.Color(255, 153, 0));
        btnHienThiXX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Open file.png"))); // NOI18N
        btnHienThiXX.setText("Hiển thị");
        btnHienThiXX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiXXActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelXuatXuLayout = new javax.swing.GroupLayout(panelXuatXu);
        panelXuatXu.setLayout(panelXuatXuLayout);
        panelXuatXuLayout.setHorizontalGroup(
            panelXuatXuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelXuatXuLayout.createSequentialGroup()
                .addGroup(panelXuatXuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelXuatXuLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(panelXuatXuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel22))
                        .addGroup(panelXuatXuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelXuatXuLayout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelXuatXuLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtNoiNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelXuatXuLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMaXuatXu))))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelXuatXuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtTimXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimXX)
                        .addGap(18, 18, 18)
                        .addComponent(btnHienThiXX))
                    .addGroup(panelXuatXuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        panelXuatXuLayout.setVerticalGroup(
            panelXuatXuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelXuatXuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(9, 9, 9)
                .addGroup(panelXuatXuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtMaXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelXuatXuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtNoiNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelXuatXuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimXX)
                    .addComponent(btnHienThiXX))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelCardContainer.add(panelXuatXu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1404, -1, -1));

        panelMauSac1.setBorder(javax.swing.BorderFactory.createTitledBorder("Thuộc tính"));

        tblMau.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã màu ", "Tên màu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMau.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMauMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblMau);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel13.setText("Màu Sắc ");

        jLabel15.setText("Mã màu");

        jLabel16.setText("Tên Màu");

        btnTimMau.setBackground(new java.awt.Color(255, 153, 0));
        btnTimMau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnTimMau.setText("Tìm");
        btnTimMau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimMauActionPerformed(evt);
            }
        });

        txtTimMau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMauActionPerformed(evt);
            }
        });

        btnHienThi.setBackground(new java.awt.Color(255, 153, 0));
        btnHienThi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Open file.png"))); // NOI18N
        btnHienThi.setText("Hiển thị");
        btnHienThi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiActionPerformed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Mời chọn"));

        btnThemMau.setBackground(new java.awt.Color(255, 153, 0));
        btnThemMau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Add.png"))); // NOI18N
        btnThemMau.setText("Thêm ");
        btnThemMau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMauActionPerformed(evt);
            }
        });

        btnSuaMau.setBackground(new java.awt.Color(255, 153, 0));
        btnSuaMau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Edit.png"))); // NOI18N
        btnSuaMau.setText("Sửa");
        btnSuaMau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaMauActionPerformed(evt);
            }
        });

        btnXoaMau.setBackground(new java.awt.Color(255, 153, 0));
        btnXoaMau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Delete.png"))); // NOI18N
        btnXoaMau.setText("Xóa");
        btnXoaMau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaMauActionPerformed(evt);
            }
        });

        btnLm.setBackground(new java.awt.Color(255, 153, 0));
        btnLm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Refresh.png"))); // NOI18N
        btnLm.setText("Làm mới");
        btnLm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnThemMau)
                .addGap(18, 18, 18)
                .addComponent(btnSuaMau)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(btnXoaMau)
                .addGap(18, 18, 18)
                .addComponent(btnLm)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemMau)
                    .addComponent(btnSuaMau)
                    .addComponent(btnXoaMau)
                    .addComponent(btnLm)))
        );

        javax.swing.GroupLayout panelMauSac1Layout = new javax.swing.GroupLayout(panelMauSac1);
        panelMauSac1.setLayout(panelMauSac1Layout);
        panelMauSac1Layout.setHorizontalGroup(
            panelMauSac1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMauSac1Layout.createSequentialGroup()
                .addGroup(panelMauSac1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMauSac1Layout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(jLabel13))
                    .addGroup(panelMauSac1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(panelMauSac1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMauSac1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(38, 38, 38)
                                .addComponent(txtMaMau))
                            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMauSac1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(35, 35, 35)
                                .addComponent(txtTenMau)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMauSac1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelMauSac1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMauSac1Layout.createSequentialGroup()
                        .addComponent(txtTimMau, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimMau)
                        .addGap(18, 18, 18)
                        .addComponent(btnHienThi)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMauSac1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelMauSac1Layout.setVerticalGroup(
            panelMauSac1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMauSac1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelMauSac1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(panelMauSac1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(txtTenMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelMauSac1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimMau)
                    .addComponent(btnHienThi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        jPanelCardContainer.add(panelMauSac1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 500));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelCardContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelCardContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        tabs.addTab("Sản Phẩm Chi Tiết ", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnKichThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKichThuocActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) jPanelCardContainer.getLayout();
        card.show(jPanelCardContainer, "panelKichThuoc1"); // "panelMauSac" là tên bạn gán cho JPanel màu sắc

    }//GEN-LAST:event_btnKichThuocActionPerformed

    private void btnHTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHTActionPerformed
        // TODO add your handling code here:
        this.findKichT();
    }//GEN-LAST:event_btnHTActionPerformed

    private void txtTimKichThActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKichThActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKichThActionPerformed

    private void btnTimKichTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKichTActionPerformed
        // TODO add your handling code here:
       String ten = txtTimKichTh.getText().trim();
    if (ten.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin");
        return;
    }

    List<KichThuoc> lsTimDc = kichThDao.getByTen(ten);
    DefaultTableModel model = (DefaultTableModel) tblKichThuoc.getModel();
    model.setRowCount(0);

    if (lsTimDc.isEmpty()) {
        int result = JOptionPane.showConfirmDialog(this, "Không tìm thấy kích thước phù hợp. Bạn có muốn thử lại?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            txtTimKichTh.requestFocus();
        } else {
            txtTimKichTh.setText("");
            return;
        }
    } else {
        for (KichThuoc kichThuoc : lsTimDc) {
            Object[] row = {
                kichThuoc.getMaKichThuoc(),
                kichThuoc.getKichThuoc()
            };
            model.addRow(row);
        }
        txtTimKichTh.setText("");
    }
    }//GEN-LAST:event_btnTimKichTActionPerformed

    private void btnLamMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMActionPerformed
        // TODO add your handling code here:
        this.clearKT();
    }//GEN-LAST:event_btnLamMActionPerformed

    private void btnXoaKichTHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaKichTHActionPerformed
        // TODO add your handling code here:
        String maKT = txtMaKichThuoc.getText().trim();
        if (maKT.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập hoặc chọn mã kích thước cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa kích thước này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Thực hiện xóa
        int result = kichThDao.delete(maKT);
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "Xóa thành công");
            findKichT();
            findKichThuoc();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        this.clearKT();
    }//GEN-LAST:event_btnXoaKichTHActionPerformed

    private void btnSuaKichTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaKichTActionPerformed
        // TODO add your handling code here:
        try {
            if (validateFormKichT()) {
                KichThuoc kichThuoc = getFromKT();
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn sửa kích thước này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }
                kichThDao.update(kichThuoc);
                JOptionPane.showMessageDialog(this, "Sửa thành Công");
                findKichT();
                findKichThuoc();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thêm thất bại !" + e.getMessage());
        }
    }//GEN-LAST:event_btnSuaKichTActionPerformed

    private void btnThemKichTHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKichTHActionPerformed
        // TODO add your handling code here:
        try {
            if (validateFormKichT()) {
                KichThuoc kichThuoc = getFromKT();
                String maKt = kichThuoc.getMaKichThuoc();
                if (kichThDao.getByMaKichTh(maKt) != null) {
                    JOptionPane.showMessageDialog(this, "Trung ma vui lòng nhập lại");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thêm kích thước này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }
                kichThDao.create(kichThuoc);
                JOptionPane.showMessageDialog(this, "Thêm thành Công");
                findKichT();
                findKichThuoc();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thêm thất bại !" + e.getMessage());
        }
        txtMaKichThuoc.setText("");
        txtKichThuoc.setText("");
    }//GEN-LAST:event_btnThemKichTHActionPerformed

    private void txtKichThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKichThuocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKichThuocActionPerformed

    private void tblKichThuocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKichThuocMouseClicked
        // TODO add your handling code here:
        int index = tblKichThuoc.getSelectedRow();
        this.setFromKichTh(index);
    }//GEN-LAST:event_tblKichThuocMouseClicked

    private void btnHienThiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHienThiActionPerformed
        // TODO add your handling code here:
        this.fillMauSac();
    }//GEN-LAST:event_btnHienThiActionPerformed

    private void txtTimMauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimMauActionPerformed

    private void btnTimMauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimMauActionPerformed
        // TODO add your handling code here:
        String ten = txtTimMau.getText();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        List<MauSac> lsTimDc = mauDao.getByTen(ten);
        DefaultTableModel model = (DefaultTableModel) tblMau.getModel();
        model.setRowCount(0);
        lsMau = mauDao.getAll();
        if (lsTimDc.isEmpty()) {
        int result = JOptionPane.showConfirmDialog(this, "Không tìm thấy màu không phù hợp. Bạn có muốn thử lại?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            txtTimMau.requestFocus();
        } else {
            txtTimMau.setText("");
            return;
        }
    } else {
        for (MauSac mauSac : lsTimDc) {
            Object[] row = {
                mauSac.getMaMauSac(),
                mauSac.getTenMau()
            };
            model.addRow(row);
        }
        txtTimMau.setText("");
        }
    }//GEN-LAST:event_btnTimMauActionPerformed

    private void btnLmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLmActionPerformed
        // TODO add your handling code here:
        this.clearMau();
    }//GEN-LAST:event_btnLmActionPerformed

    private void btnSuaMauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaMauActionPerformed
        // TODO add your handling code here:
        try {
            if (validateFormMauSac()) {
                MauSac mauSac = getFromMau();
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn sửa màu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }
                mauDao.update(mauSac);
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                fillMauSac();
                findMau();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Sửa thất bại" + e.getMessage());
        }
    }//GEN-LAST:event_btnSuaMauActionPerformed

    private void btnXoaMauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaMauActionPerformed
        // TODO add your handling code here:
        try {
            String maMau = txtMaMau.getText().trim();
            if (maMau.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập hoặc chọn mã màu cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa Màu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            mauDao.delete(maMau);
            JOptionPane.showMessageDialog(this, "Xóa thành  công ");
            fillMauSac();
            findMau();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xóa Thất bại! " + e.getMessage());
            e.printStackTrace();
        }
        this.clearMau();
    }//GEN-LAST:event_btnXoaMauActionPerformed

    private void btnThemMauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMauActionPerformed
        // TODO add your handling code here:
        try {
            if (validateFormMauSac()) {
                MauSac mauSac = getFromMau();
                if (mauSac == null) {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ");
                    return;
                }

                String mam = mauSac.getMaMauSac();
                if (mauDao.findByMaMau(mam) != null) {
                    JOptionPane.showMessageDialog(this, "Trùng mã, vui lòng nhập lại");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thêm màu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }

                mauDao.create(mauSac);
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                fillMauSac();
                findMau();

                txtMaMau.setText("");
                txtTenMau.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm thất bại: " + e.getMessage());
        }
    }//GEN-LAST:event_btnThemMauActionPerformed

    private void tblMauMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMauMouseClicked
        // TODO add your handling code here:
        int indexRow = tblMau.getSelectedRow();
        this.setFromMau(indexRow);
    }//GEN-LAST:event_tblMauMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.FindChatLieu();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtMaCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaCLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaCLActionPerformed

    private void tblChatLieuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChatLieuMouseClicked
        // TODO add your handling code here:
        int index = tblChatLieu3.getSelectedRow();
        this.setFromCL(index);
    }//GEN-LAST:event_tblChatLieuMouseClicked

    private void btnTimCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimCLActionPerformed
        // TODO add your handling code here:
        String ten = txtTimCL3.getText();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập chất liệu cần tìm");
            return;
        }
        List<ChatLieu> lsTimDc = chatLieuDao.getByTen(ten);
        DefaultTableModel modelTimCL = (DefaultTableModel) tblChatLieu3.getModel();
        modelTimCL.setRowCount(0);
        if (lsTimDc.isEmpty()) {
        int result = JOptionPane.showConfirmDialog(this, "Không tìm thấy chất liệu phù hợp. Bạn có muốn thử lại?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            txtTimCL3.requestFocus();
        } else {
            txtTimCL3.setText("");
            return;
        }
    } else {
        for (ChatLieu chatLieu : lsTimDc) {
            Object[] row = {
                chatLieu.getMaChatLieu(),
                chatLieu.getChatLieu(),};
            modelTimCL.addRow(row);
        }
        }
    }//GEN-LAST:event_btnTimCLActionPerformed

    private void btnLamMoiCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiCLActionPerformed
        // TODO add your handling code here:
        this.clearCl();
    }//GEN-LAST:event_btnLamMoiCLActionPerformed

    private void btnXoaCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaCLActionPerformed
        // TODO add your handling code here:
        try {
            String maCl = txtMaCL3.getText().trim();
            if (maCl.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập hoặc chọn mã chất liệu cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa chất liệu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            chatLieuDao.delete(maCl);
            FindChatLieu();
            findChatLieu();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xóa thất bại" + e.getMessage());
        }
        this.clearKT();
    }//GEN-LAST:event_btnXoaCLActionPerformed

    private void btnSuaChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaChatLieuActionPerformed
        // TODO add your handling code here:
        try {
            if (validateFormChatLieu()) {
                ChatLieu chatLieu = getFromCl();
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn sửa chất liệu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }
                chatLieuDao.update(chatLieu);
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                FindChatLieu();
                findChatLieu();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Sửa thất bại: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSuaChatLieuActionPerformed

    private void btnThemCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemCLActionPerformed
        // TODO add your handling code here:
        try {
            if (validateFormChatLieu()) {
                ChatLieu chatLieu = getFromCl();
                String maCl = chatLieu.getMaChatLieu();
                if (chatLieuDao.finById(maCl) != null) {
                    JOptionPane.showMessageDialog(this, "Trùng mã vui lòng nhập lại");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thêm chất liệu  này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }
                chatLieuDao.create(chatLieu);
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                FindChatLieu();
                findChatLieu();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thêm thất bại: " + e.getMessage());
        }
    }//GEN-LAST:event_btnThemCLActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        this.clean();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        try {
            //if(validateFormChiTiet()){
            String maCT = txtMaChiT.getText().trim();
            if (!sanPhamCTDao.findByMaSP(maCT).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm vì vẫn còn chi tiết sản phẩm liên quan!");
                return;
            }
            // Kiểm tra mã có rỗng không
            if (maCT.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập hoặc chọn mã chi tiết sản phẩm cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Hỏi lại người dùng trước khi xóa
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa chi tiết sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            // Thực hiện xóa
            sanPhamCTDao.delete(maCT);
            FillSanPhamCT(); // Load lại bảng
            JOptionPane.showMessageDialog(this, "Xóa chi tiết sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            //   }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        this.clean();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        try {
            if (validateFormChiTiet()) {
                // Lấy dữ liệu từ form
                ChiTietSanPham chiTietSanPham = getDataFromCT();
                if (chiTietSanPham == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn và nhập đầy đủ thông tin cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn sửa sản phẩm chi tiết này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }
                // Gọi DAO để cập nhật
                sanPhamCTDao.update(chiTietSanPham);

                // Nếu không có lỗi thì coi như thành công
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                FillSanPhamCT();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể cập nhật sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        try {
            if (validateFormChiTiet()) {
                ChiTietSanPham chiTietSanPham = getDataFromCT();
                String maCt = chiTietSanPham.getMaChiTietSP();
                if (sanPhamCTDao.findByMaChiTietSP(maCt) != null) {
                    JOptionPane.showMessageDialog(this, "Trùng mã vui lòng nhập lại");
                    return;
                }
                 int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thêm sản phẩm chi tiết này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng chọn không thì không thêm
            }
                sanPhamCTDao.create(chiTietSanPham);
                FillSanPhamCT();
                FillSanPham();
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm chi tiết thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm chi tiết thất bại: " + e.getMessage());
            e.printStackTrace();
        }
        this.clean();
    }//GEN-LAST:event_btnThemActionPerformed

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoLuongActionPerformed

    private void txtMaChiTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaChiTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaChiTActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.FillSanPhamCT();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnTimSPCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimSPCTActionPerformed
        // TODO add your handling code here:
        String tuKhoa = txtTimSpCT.getText().trim();
        if (tuKhoa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa cần tìm");
            return;
        }

        List<ChiTietSanPham> lsTimDc = sanPhamCTDao.findAllFields(tuKhoa);
        DefaultTableModel modelSp = (DefaultTableModel) tblSanPCT.getModel();
        modelSp.setRowCount(0);

        if (lsTimDc.isEmpty()) {
    int result = JOptionPane.showConfirmDialog(this,
        "Không tìm thấy sản phẩm phù hợp.\nBạn có muốn thử lại?",
        "Thông báo",
        JOptionPane.YES_NO_OPTION);
    if (result == JOptionPane.YES_OPTION) {
        txtTimSpCT.requestFocus();
    } else {
        txtTimSpCT.setText("");
    }
    return;
}
        for (ChiTietSanPham spct : lsTimDc) {
            Object[] row = {
                spct.getMaChiTietSP(),
                spct.getSoLuong(),
                spct.getGia(),
                sanPhamDao.findByMa(spct.getMaSanPham()).getTenSanPham(),
                mauDao.findById(spct.getMaMauSac()).getTenMau(),
                kichThDao.findById(spct.getMaKichThuoc()).getKichThuoc()
            };
            modelSp.addRow(row);
        }
        txtTimSpCT.setText("");
    }//GEN-LAST:event_btnTimSPCTActionPerformed

    private void rdoConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoConActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoConActionPerformed

    private void btnXoaSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSpActionPerformed
        // TODO add your handling code here:
        try {
            //if(validateFormChiTiet()){
            String maSp = txtMaSanPham.getText().trim();

            if (maSp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập hoặc chọn mã sản phẩm cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa chi tiết sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            // Thực hiện xóa
            sanPhamDao.delete(maSp);
            FillSanPham(); // Load lại bảng
            JOptionPane.showMessageDialog(this, "Xóa chi tiết sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            //   }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        this.cleanSp();
    }//GEN-LAST:event_btnXoaSpActionPerformed

    private void btnThemSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSanPhamActionPerformed
        // TODO add your handling code here:
        try {
            if (!validateFormSanPham()) {
                return;
            }

            SanPham2 sanPham2 = getFromSanPham();
            String ma = sanPham2.getMaSanPham();
            String ten = sanPham2.getTenSanPham().trim().toLowerCase();
            if (sanPhamDao.findByMa(ma) != null) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm đã tồn tại!");
                return;
            }
            if (!sanPhamDao.findByName(ten).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Trùng tên sản phẩm!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thêm sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return; // Người dùng chọn không thì dừng
    }

            sanPhamDao.create(sanPham2);
            this.LsSp = sanPhamDao.getAll(); // Cập nhật lại danh sách sản phẩm
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công");
            cleanSp();
            FillSanPham();
            FillSanPhamCT();

            // 👉 Chọn dòng vừa thêm
            int lastRow = tblSanPham.getRowCount() - 1;
            if (lastRow >= 0) {
                tblSanPham.setRowSelectionInterval(lastRow, lastRow);
                tblSanPham.scrollRectToVisible(tblSanPham.getCellRect(lastRow, 0, true));
                lastSelectedRow = lastRow;     // ✅ Cập nhật dòng đã chọn
                setFromSanPHam(lastRow);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThemSanPhamActionPerformed

    private void btnLamMoiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiSPActionPerformed
        // TODO add your handling code here:
        this.cleanSp();

    }//GEN-LAST:event_btnLamMoiSPActionPerformed

    private void cboChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChatLieuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboChatLieuActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.FillSanPham();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        // TODO add your handling code here:
       String tuKhoa = txtTim.getText().trim();
if (tuKhoa.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa cần tìm");
    return;
}

List<SanPham2> lsTimDc = sanPhamDao.findAllFields(tuKhoa);
DefaultTableModel modelSp = (DefaultTableModel) tblSanPham.getModel();
modelSp.setRowCount(0);

if (lsTimDc.isEmpty()) {
    int result = JOptionPane.showConfirmDialog(this,
        "Không tìm thấy sản phẩm phù hợp.\nBạn có muốn thử lại?",
        "Thông báo",
        JOptionPane.YES_NO_OPTION);
    if (result == JOptionPane.YES_OPTION) {
        txtTim.requestFocus();
    } else {
        txtTim.setText("");
    }
    return;
}

for (SanPham2 sanPham : lsTimDc) {
    Object[] rowData = {
        sanPham.getMaSanPham(),
        sanPham.getTenSanPham(),
        sanPham.getMoTa(),
        chatLieuDao.finById(sanPham.getMaChatLieu()).getChatLieu(),
        xuatXuDao.findByMaXX(sanPham.getMaXuatSu()).getNoiNhap(),
        sanPham.isTrangThai() ? "Còn hàng" : "Hết hàng"
    };
    modelSp.addRow(rowData);
}

    }//GEN-LAST:event_btnTimActionPerformed

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        // TODO add your handling code here:
        int indexRow = tblSanPham.getSelectedRow();
        String maSP = tblSanPham.getValueAt(indexRow, 0).toString(); // Lấy mã SP từ cột 0
        String tenSp = tblSanPham.getValueAt(indexRow, 1).toString();
        if (indexRow == lastSelectedRow) {
            // Nếu bấm lại đúng dòng => chuyển tab
            lblMaSp.setText(maSP); // Gán mã SP sang ô bên SPCT
            lblTenSp.setText(tenSp);
            tabs.setSelectedIndex(1);
            this.FillSanPhamCT();
        } else {
            // Lần đầu bấm => chỉ load dữ liệu lên form
            this.setFromSanPHam(indexRow);
            lastSelectedRow = indexRow;
        }
    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void btnMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMauSacActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) jPanelCardContainer.getLayout();
        card.show(jPanelCardContainer, "panelMauSac1"); // "panelMauSac" là tên bạn gán cho JPanel màu sắc
    }//GEN-LAST:event_btnMauSacActionPerformed

    private void btnChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChatLieuActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) jPanelCardContainer.getLayout();
        card.show(jPanelCardContainer, "panelChatLieu1");
    }//GEN-LAST:event_btnChatLieuActionPerformed

    private void btnSuaSpActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaSpActionPerformed1
        // TODO add your handling code here:
        try {
            if (validateFormSanPham()) {
                SanPham2 sanPham2 = getFromSanPham();
                if (sanPham2 == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn và nhập đầy đủ thông tin cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn sửa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }
                sanPhamDao.update(sanPham2);
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                FillSanPham();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi chưa sửa được" + e.getMessage());
        }
    }//GEN-LAST:event_btnSuaSpActionPerformed1

    private void tblSanPCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPCTMouseClicked
        // TODO add your handling code here:
        int index = tblSanPCT.getSelectedRow();
        this.setFromChiTiet(index);
    }//GEN-LAST:event_tblSanPCTMouseClicked

    private void txtMaXuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaXuatXuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaXuatXuActionPerformed

    private void btnXoaXXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaXXActionPerformed
        // TODO add your handling code here:

        try {
            String maXX = txtMaXuatXu.getText().trim();
            if (maXX.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập hoặc chọn mã Xuất xứ cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa  xuất xứ này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            xuatXuDao.delete(maXX);
            findXuatXu();
            FindXuatXu();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xóa thất bại" + e.getMessage());
        }
        this.clearXX();
    }//GEN-LAST:event_btnXoaXXActionPerformed

    private void btnXuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatXuActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) jPanelCardContainer.getLayout();
        card.show(jPanelCardContainer, "panelXuatXu");
    }//GEN-LAST:event_btnXuatXuActionPerformed

    private void btnSuaXXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaXXActionPerformed
        // TODO add your handling code here:
        try {
            if (validateFormXuatXu()) {
                XuatXu xuatXu = getFormXX();
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn sửa xuất xứ này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }
                xuatXuDao.update(xuatXu);
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                findXuatXu();
                FindXuatXu();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi không sửa được" + e.getMessage());
        }
        this.clearXX();
    }//GEN-LAST:event_btnSuaXXActionPerformed

    private void tblXuatXuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblXuatXuMouseClicked
        // TODO add your handling code here:
        int index = tblXuatXu.getSelectedRow();
        this.setFormXX(index);
    }//GEN-LAST:event_tblXuatXuMouseClicked

    private void btnThemXXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemXXActionPerformed
        // TODO add your handling code here:
        try {

            if (validateFormXuatXu()) {
                XuatXu xuatXu = getFormXX();
                String ma = xuatXu.getMaXuatSu();
                if (xuatXuDao.findByMaXX(ma) != null) {
                    JOptionPane.showMessageDialog(this, "Trùng mã sản phẩm");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thêm xuất xứ này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng không đồng ý thì dừng sửa
            }
                xuatXuDao.create(xuatXu);
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                findXuatXu();
                FindXuatXu();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thêm thất bại" + e.getMessage());
        }
        this.clearXX();
    }//GEN-LAST:event_btnThemXXActionPerformed

    private void btnLMXXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLMXXActionPerformed
        // TODO add your handling code here:
        this.clearXX();
    }//GEN-LAST:event_btnLMXXActionPerformed

    private void btnTimXXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimXXActionPerformed
        // TODO add your handling code here:
        String ten = txtTimXuatXu.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        List<XuatXu> lsTimDc = xuatXuDao.findByTen(ten);
        DefaultTableModel model = (DefaultTableModel) tblXuatXu.getModel();
        model.setRowCount(0);
        if (lsTimDc.isEmpty()) {
        int result = JOptionPane.showConfirmDialog(this, "Không tìm thấy xuất xứ phù hợp. Bạn có muốn thử lại?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            txtTimXuatXu.requestFocus();
        } else {
            txtTimXuatXu.setText("");
            return;
        }
    } else {
        for (XuatXu xuatXu : lsTimDc) {
            Object[] value = {
                xuatXu.getMaXuatSu(),
                xuatXu.getNoiNhap()
            };
            model.addRow(value);
        }
        this.clearXX();
        }
    }//GEN-LAST:event_btnTimXXActionPerformed

    private void btnHienThiXXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHienThiXXActionPerformed
        // TODO add your handling code here:
        this.findXuatXu();
    }//GEN-LAST:event_btnHienThiXXActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChatLieu;
    private javax.swing.JButton btnHT;
    private javax.swing.JButton btnHienThi;
    private javax.swing.JButton btnHienThiXX;
    private javax.swing.JButton btnKichThuoc;
    private javax.swing.JButton btnLMXX;
    private javax.swing.JButton btnLamM;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLamMoiCL3;
    private javax.swing.JButton btnLamMoiSP;
    private javax.swing.JButton btnLm;
    private javax.swing.JButton btnMauSac;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnSuaChatLieu3;
    private javax.swing.JButton btnSuaKichT;
    private javax.swing.JButton btnSuaMau;
    private javax.swing.JButton btnSuaSp;
    private javax.swing.JButton btnSuaXX;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemCL3;
    private javax.swing.JButton btnThemKichTH;
    private javax.swing.JButton btnThemMau;
    private javax.swing.JButton btnThemSanPham;
    private javax.swing.JButton btnThemXX;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnTimCL3;
    private javax.swing.JButton btnTimKichT;
    private javax.swing.JButton btnTimMau;
    private javax.swing.JButton btnTimSPCT;
    private javax.swing.JButton btnTimXX;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaCL3;
    private javax.swing.JButton btnXoaKichTH;
    private javax.swing.JButton btnXoaMau;
    private javax.swing.JButton btnXoaSp;
    private javax.swing.JButton btnXoaXX;
    private javax.swing.JButton btnXuatXu;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboChatLieu;
    private javax.swing.JComboBox<String> cboKichThuoc;
    private javax.swing.JComboBox<String> cboMau;
    private javax.swing.JComboBox<String> cboXX;
    private javax.swing.JLabel cboXuatXu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelCardContainer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel lblMaSp;
    private javax.swing.JLabel lblTenSp;
    private javax.swing.JPanel panelChatLieu1;
    private javax.swing.JPanel panelKichThuoc1;
    private javax.swing.JPanel panelMauSac1;
    private javax.swing.JPanel panelXuatXu;
    private javax.swing.JRadioButton rdoCon;
    private javax.swing.JRadioButton rdoHet;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblChatLieu3;
    private javax.swing.JTable tblKichThuoc;
    private javax.swing.JTable tblMau;
    private javax.swing.JTable tblSanPCT;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTable tblXuatXu;
    private javax.swing.JTextField txtCL3;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtKichThuoc;
    private javax.swing.JTextField txtMaCL3;
    private javax.swing.JTextField txtMaChiT;
    private javax.swing.JTextField txtMaKichThuoc;
    private javax.swing.JTextField txtMaMau;
    private javax.swing.JTextField txtMaSanPham;
    private javax.swing.JTextField txtMaXuatXu;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextField txtNoiNhap;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenMau;
    private javax.swing.JTextField txtTenSanPham;
    private javax.swing.JTextField txtTim;
    private javax.swing.JTextField txtTimCL3;
    private javax.swing.JTextField txtTimKichTh;
    private javax.swing.JTextField txtTimMau;
    private javax.swing.JTextField txtTimSpCT;
    private javax.swing.JTextField txtTimXuatXu;
    // End of variables declaration//GEN-END:variables
}
