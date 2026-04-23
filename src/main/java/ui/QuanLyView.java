package ui;

//import .Repository_NhanVien;
import Repository.Repository_NhanVien;
import dao.ChucVuDao;
import dao.KhoaHuongDanDao;
import dao.LichLamViecDao;
import dao.NhanVienDao;
import entity.ChucVu;
import entity.KhoaHuongDan;
import entity.LichLamViec;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import entity.NhanVien;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import unti.XJdbc;

public class QuanLyView extends javax.swing.JInternalFrame {

    KhoaHuongDanDao daoKHD = new KhoaHuongDanDao();
    private LichLamViecDao daoLLV = new LichLamViecDao();
    private Repository.Repository_NhanVien rp = new Repository_NhanVien();
    private ChucVuDao daoChucVu = new ChucVuDao();
    private NhanVienDao daoNhanVien = new NhanVienDao();
    private DefaultTableModel mol;
    private int i;
    private int t = 1;
    
    

    public QuanLyView() {
        initComponents();
        txt_maNhanVien.setEditable(false);
        cbo_chucVu.setEditable(false);
        cbo_trangThai.setEditable(false);
        txt_MaLich.setEditable(false);
        this.fillTable(rp.getByStatus(0));
        this.fillComboLocVT();
        fillComboChucVu();
        this.fillComboTT();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        this.loadTableLichLamViec();
        fillComboCaLam();
        fillComboTrangThai();
        this.loadTableHSLV();
        this.loadTableKHD();
    }

    public void fillTable(List<NhanVien> list) {
    mol = (DefaultTableModel) tbl_QLNV.getModel();
    mol.setRowCount(0);
    for (NhanVien nv : list) {
        mol.addRow(new Object[]{
            nv.getMaNhanVien(),
            nv.getHoTen(),
            nv.getGioiTinh(),
            nv.getSoDienThoai(),
            nv.getEmail(),
            daoChucVu.getTenChucVuByMa(nv.getMaChucVu()), // ✅ Lấy tên chức vụ từ mã
            nv.getTrangThai() == 0 ? "Đang làm" : "Đã nghỉ"
        });
    }
    }
    
public void openPanelKhenThuongDieuChinh(String maNV, String hoTen, double tongDoanhThu, double chiTieu) {
    JFrame frame = new JFrame("Khen Thưởng / Điều Chỉnh");
    frame.setSize(400, 250);
    frame.setLocationRelativeTo(this);
    frame.setLayout(new GridBagLayout());
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5,5,5,5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0; gbc.gridy = 0;
    frame.add(new JLabel("Mã nhân viên:"), gbc);
    gbc.gridx = 1;
    JTextField txtMaNV = new JTextField(maNV);
    txtMaNV.setEditable(false);
    frame.add(txtMaNV, gbc);

    gbc.gridx = 0; gbc.gridy = 1;
    frame.add(new JLabel("Họ và tên:"), gbc);
    gbc.gridx = 1;
    JTextField txtHoTen = new JTextField(hoTen);
    txtHoTen.setEditable(false);
    frame.add(txtHoTen, gbc);

    gbc.gridx = 0; gbc.gridy = 2;
    frame.add(new JLabel("Tổng doanh thu:"), gbc);
    gbc.gridx = 1;
    JTextField txtTongDoanhThu = new JTextField(String.valueOf(tongDoanhThu));
    txtTongDoanhThu.setEditable(false);
    frame.add(txtTongDoanhThu, gbc);

    gbc.gridx = 0; gbc.gridy = 3;
    frame.add(new JLabel("Chỉ tiêu:"), gbc);
    gbc.gridx = 1;
    JTextField txtChiTieu = new JTextField(String.valueOf(chiTieu));
    txtChiTieu.setEditable(false);
    frame.add(txtChiTieu, gbc);

    gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
    JButton btnAction;
    if(tongDoanhThu >= chiTieu){
        btnAction = new JButton("Khen Thưởng");
    } else {
        btnAction = new JButton("Điều Chỉnh Công Việc");
    }
    frame.add(btnAction, gbc);

    btnAction.addActionListener(e -> {
        if(tongDoanhThu >= chiTieu){
            JOptionPane.showMessageDialog(frame, "Nhân viên đạt chỉ tiêu, thực hiện khen thưởng!");
        } else {
            JOptionPane.showMessageDialog(frame, "Nhân viên chưa đạt chỉ tiêu, cần điều chỉnh công việc.");
        }
    });

    frame.setVisible(true);
}

public void showDataKhoaHD() {
    int row = tbl_KHD.getSelectedRow();
    if (row >= 0) {
        // Mã khóa huấn luyện và tên khóa
        txtMaKhoa.setText(tbl_KHD.getValueAt(row, 0).toString());
        txtTenKhoa.setText(tbl_KHD.getValueAt(row, 1).toString());

        // Ngày bắt đầu
        Object ngayBD = tbl_KHD.getValueAt(row, 2);
        txtNgayBatDau.setText(ngayBD != null ? ngayBD.toString() : "");

        // Ngày kết thúc
        Object ngayKT = tbl_KHD.getValueAt(row, 3);
        txtNgayKetThuc.setText(ngayKT != null ? ngayKT.toString() : "");

        // Mô tả
        Object moTa = tbl_KHD.getValueAt(row, 4);
        txtMoTa.setText(moTa != null ? moTa.toString() : "");

        // Trạng thái tham gia
        Object trangThai = tbl_KHD.getValueAt(row, 5);
        cboTrangThai.setSelectedItem(trangThai != null ? trangThai.toString() : "Chưa tham gia");
    }
}

public void loadTableKHD() {
    DefaultTableModel model = (DefaultTableModel) tbl_KHD.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    ArrayList<KhoaHuongDan> list = daoKHD.getAll(); // Lấy tất cả khóa huấn luyện
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    for (KhoaHuongDan kh : list) {
        model.addRow(new Object[]{
            kh.getMaKhoaHD(),                             // Mã khóa
            kh.getTieuDe(),                               // Tiêu đề
            kh.getNgayBatDau() != null ? sdf.format(kh.getNgayBatDau()) : "", // Ngày bắt đầu
            kh.getNgayKetThuc() != null ? sdf.format(kh.getNgayKetThuc()) : "", // Ngày kết thúc
            kh.getMoTa(),                                 // Mô tả
            kh.isTrangThai() ? "Còn hoạt động" : "Không hoạt động" // Trạng thái
        });
    }

    // Load combobox trạng thái
    cboTrangThai.removeAllItems();
    cboTrangThai.addItem("Còn hoạt động");
    cboTrangThai.addItem("Không hoạt động");
}







public void loadTableLichLamViec() {
    DefaultTableModel model = (DefaultTableModel) tbl_LLV.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    ArrayList<LichLamViec> list = daoLLV.getAll();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // định dạng ngày

    for (LichLamViec x : list) {
        model.addRow(new Object[]{
            x.getMaLLV(),
            x.getMaNhanVien(),
            x.getHoTen(),                     // thêm cột họ tên
            x.getNgayLam() != null ? sdf.format(x.getNgayLam()) : "",
            x.getCaLam(),
            x.getGhiChu()
        });
    }

    // Load combobox CaLam
    cbo_CaLam.removeAllItems();
    cbo_CaLam.addItem("Ca Sáng");
    cbo_CaLam.addItem("Ca Chiều");
    cbo_CaLam.addItem("Ca Tối"); // nếu có
}



public void loadTableHSLV() {
    String[] columns = {"Mã nhân viên", "Họ và tên", "Số đơn hàng", "Tổng doanh thu", "Trung bình đơn", "Chỉ tiêu"};
    DefaultTableModel model = new DefaultTableModel(columns, 0);

    String sql = "SELECT nv.MaNhanVien, nv.hoTen, " +
                 "COUNT(hd.MaHoaDon) AS SoDonHang, " +
                 "ISNULL(SUM(hd.TienSauGiam),0) AS TongDoanhThu, " +
                 "CASE WHEN COUNT(hd.MaHoaDon)=0 THEN 0 ELSE CAST(SUM(hd.TienSauGiam) AS FLOAT)/COUNT(hd.MaHoaDon) END AS TrungBinhDon, " +
                 "5000000 AS ChiTieu " +
                 "FROM NhanVien nv " +
                 "LEFT JOIN HoaDon hd ON nv.MaNhanVien = hd.MaNhanVien AND hd.TrangThai=1 " +
                 "GROUP BY nv.MaNhanVien, nv.hoTen " +
                 "ORDER BY nv.MaNhanVien";

    try (ResultSet rs = XJdbc.executeQuery(sql)) {
        while (rs.next()) {
            Object[] row = {
                rs.getString("MaNhanVien"),
                rs.getString("hoTen"),
                rs.getInt("SoDonHang"),
                rs.getDouble("TongDoanhThu"),
                rs.getDouble("TrungBinhDon"),
                rs.getDouble("ChiTieu")
            };
            model.addRow(row);
        }
        tbl_HSLV.setModel(model);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi load dữ liệu hiệu suất: " + e.getMessage());
    }
}


public void fillComboTrangThai() {
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    model.addElement("Còn hoạt động");   // giá trị 1
    model.addElement("Đã kết "); // giá trị 0
    cboTrangThai.setModel(model);
}


public void fillComboChucVu() {
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    for (ChucVu cv : daoChucVu.getAll()) {
        model.addElement(cv.getTenChucVu()); // ✅ chỉ tên
    }
    cbo_chucVu.setModel(model);
}

    public void fillComboLocVT() {
        DefaultComboBoxModel cmol = (DefaultComboBoxModel) cboLocCV.getModel();
        cmol.removeAllElements();
        cmol.addElement("Tất cả");
        cmol.addElement("Quản lý");
        cmol.addElement("Nhân viên");
    }

public void fillComboTT() {
    DefaultComboBoxModel cmol = (DefaultComboBoxModel) cbo_trangThai.getModel();
    cmol.removeAllElements();
    cmol.addElement("Đang làm");
    cmol.addElement("Đã nghỉ");
}

public void fillComboCaLam() {
    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cbo_CaLam.getModel();
    model.removeAllElements();  // Xóa dữ liệu cũ

    // Thêm dữ liệu mẫu
    model.addElement("Ca sáng");
    model.addElement("Ca chiều");
    model.addElement("Ca tối");
}

private void filterTable(String tenChucVu, String keyword) {
    List<NhanVien> filteredList = new ArrayList<>();
    keyword = keyword.toLowerCase();

    for (NhanVien nv : rp.getByStatus(0)) {
        String tenCV_NV = daoChucVu.getTenChucVuByMa(nv.getMaChucVu());
        boolean matchesChucVu = tenChucVu.equals("Tất cả")
                || tenCV_NV.equalsIgnoreCase(tenChucVu);

        boolean matchesKeyword =
                nv.getMaNhanVien().toLowerCase().contains(keyword)
                || nv.getHoTen().toLowerCase().contains(keyword)
                || nv.getSoDienThoai().toLowerCase().contains(keyword)
                || nv.getEmail().toLowerCase().contains(keyword);

        if (matchesChucVu && matchesKeyword) {
            filteredList.add(nv);
        }
    }

    fillTable(filteredList);
}


public void showData() {
int row = tbl_QLNV.getSelectedRow();
    if (row >= 0) {
        txt_maNhanVien.setText(tbl_QLNV.getValueAt(row, 0).toString());
        txt_hoVaTen.setText(tbl_QLNV.getValueAt(row, 1).toString());

        String gioiTinh = tbl_QLNV.getValueAt(row, 2).toString();
        if (gioiTinh.equalsIgnoreCase("Nam")) {
            rdo_Nam.setSelected(true);
        } else {
            rdo_Nu.setSelected(true);
        }

        txt_SoDienThoai.setText(tbl_QLNV.getValueAt(row, 3).toString());
        txt_email.setText(tbl_QLNV.getValueAt(row, 4).toString());

        cbo_chucVu.setSelectedItem(tbl_QLNV.getValueAt(row, 5).toString());
        cbo_trangThai.setSelectedItem(tbl_QLNV.getValueAt(row, 6).toString());
    }
}

public void showDataHSLV() {
    int row = tbl_HSLV.getSelectedRow();
    if (row >= 0) {
        // Mã nhân viên và họ tên
        txtMaNhanVienHSLV.setText(tbl_HSLV.getValueAt(row, 0).toString());
        txtHoVaTenHSLV.setText(tbl_HSLV.getValueAt(row, 1).toString());

        // Số đơn hàng
        Object soDon = tbl_HSLV.getValueAt(row, 2);
        txtSoDonHang.setText(soDon != null ? soDon.toString() : "0");

        // Tổng doanh thu
        Object tongDT = tbl_HSLV.getValueAt(row, 3);
        txtTongDoanhThu.setText(tongDT != null ? tongDT.toString() : "0");

        // Trung bình đơn
        Object trungBinh = tbl_HSLV.getValueAt(row, 4);
        txtTrungBinhDon.setText(trungBinh != null ? trungBinh.toString() : "0");

        // Chỉ tiêu
        Object chiTieu = tbl_HSLV.getValueAt(row, 5);
        txtChiTieu.setText(chiTieu != null ? chiTieu.toString() : "0");
    }
}



// ...

public void showDataLichLamViec() {
    int row = tbl_LLV.getSelectedRow();
    if (row >= 0) {
        txt_MaLich.setText(tbl_LLV.getValueAt(row, 0).toString());        // MaLLV
        txt_MaNhanVienLLV.setText(tbl_LLV.getValueAt(row, 1).toString());    // MaNhanVien
        txt_HoVaTenLLV.setText(tbl_LLV.getValueAt(row, 2).toString());    // HoTen

        txt_NgayLam.setText(tbl_LLV.getValueAt(row, 3).toString());       // Ngày làm

        // Set CaLam combobox
        String caLam = tbl_LLV.getValueAt(row, 4).toString().trim();
        boolean found = false;
        for (int i = 0; i < cbo_CaLam.getItemCount(); i++) {
            if (cbo_CaLam.getItemAt(i).trim().equalsIgnoreCase(caLam)) {
                cbo_CaLam.setSelectedIndex(i);
                found = true;
                break;
            }
        }
        if (!found) {
            cbo_CaLam.setSelectedIndex(-1); // nếu không tìm thấy
        }

        // Ghi chú
        Object ghiChu = tbl_LLV.getValueAt(row, 5);
        txt_GhiChu.setText(ghiChu != null ? ghiChu.toString() : "");
    }
}

    private void clearForm() {
    txt_maNhanVien.setText(daoNhanVien.getNextMaNhanVien()); // Tự sinh mã mới
    txt_hoVaTen.setText("");
    txt_SoDienThoai.setText("");
    txt_email.setText("");
    rdo_Nam.setSelected(true);
    cbo_chucVu.setSelectedIndex(0);
    cbo_trangThai.setSelectedIndex(0);}
    
    private void clearFormLLV() {
    // Tạo mã LLV mới tự động
    txt_MaLich.setText(daoLLV.getNextMaLLV());

    // Xóa các trường nhập liệu
    txt_MaNhanVienLLV.setText("");
    txt_HoVaTenLLV.setText("");
    txt_NgayLam.setText("");   // có thể để dd/MM/yyyy trống
    cbo_CaLam.setSelectedIndex(0);  // chọn ca đầu tiên
    txt_GhiChu.setText("");

    // Chọn mặc định cho một số trường nếu cần
    txt_MaNhanVienLLV.requestFocus(); // con trỏ về Mã NV
}
    private void clearFormKHL() {
    // Tạo mã khóa huấn luyện mới tự động
    txtMaKhoa.setText(daoKHD.getNextMaKhoaHD());

    // Xóa các trường nhập liệu
    txtTenKhoa.setText("");
    txtNgayBatDau.setText("");    // có thể để dd/MM/yyyy trống
    txtNgayKetThuc.setText("");   // có thể để dd/MM/yyyy trống
    txtMoTa.setText("");

    // Reset combobox trạng thái về mặc định (Còn hoạt động)
    cboTrangThai.setSelectedIndex(0);

    // Con trỏ về trường Tên khóa để tiện nhập liệu
    txtTenKhoa.requestFocus();
}



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        btn_sua = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        btn_lamMoi = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        txt_SoDienThoai = new javax.swing.JTextField();
        rdo_Nu = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        btn_xoa = new javax.swing.JButton();
        txt_maNhanVien = new javax.swing.JTextField();
        cbo_chucVu = new javax.swing.JComboBox<>();
        txt_hoVaTen = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        rdo_Nam = new javax.swing.JRadioButton();
        cbo_trangThai = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        cboLocCV = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        tab_TTNV = new javax.swing.JPanel();
        btnDangLam = new javax.swing.JButton();
        btnDaNghi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_QLNV = new javax.swing.JTable();
        tab_LLV = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_LLV = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbo_CaLam = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txt_HoVaTenLLV = new javax.swing.JTextField();
        txt_MaLich = new javax.swing.JTextField();
        txt_GhiChu = new javax.swing.JTextField();
        txt_NgayLam = new javax.swing.JTextField();
        btn_themLLV = new javax.swing.JButton();
        btn_suaLLV = new javax.swing.JButton();
        btn_xoaLLV = new javax.swing.JButton();
        btn_lamMoiLLV = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txt_MaNhanVienLLV = new javax.swing.JTextField();
        tab_HSLV = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_HSLV = new javax.swing.JTable();
        btnKhenThuong = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtMaNhanVienHSLV = new javax.swing.JTextField();
        txtHoVaTenHSLV = new javax.swing.JTextField();
        txtSoDonHang = new javax.swing.JTextField();
        txtTongDoanhThu = new javax.swing.JTextField();
        txtTrungBinhDon = new javax.swing.JTextField();
        txtChiTieu = new javax.swing.JTextField();
        tab_KHD = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_KHD = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtTenKhoa = new javax.swing.JTextField();
        txtNgayBatDau = new javax.swing.JTextField();
        txtNgayKetThuc = new javax.swing.JTextField();
        txtMoTa = new javax.swing.JTextField();
        txtMaKhoa = new javax.swing.JTextField();
        cboTrangThai = new javax.swing.JComboBox<>();
        btn_themKHL = new javax.swing.JButton();
        btn_suaKHL = new javax.swing.JButton();
        btn_lamMoiKHL = new javax.swing.JButton();
        btn_xoaKHL = new javax.swing.JButton();

        setResizable(true);
        setPreferredSize(new java.awt.Dimension(1100, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_sua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Edit.png"))); // NOI18N
        btn_sua.setText("Sửa");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });
        getContentPane().add(btn_sua, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 100, 90, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Email:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, -1, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Số điện thoại:");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        btn_lamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Refresh.png"))); // NOI18N
        btn_lamMoi.setText("Làm mới");
        btn_lamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lamMoiActionPerformed(evt);
            }
        });
        getContentPane().add(btn_lamMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 140, 200, -1));

        btn_them.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Add.png"))); // NOI18N
        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });
        getContentPane().add(btn_them, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 30, 200, 60));

        txt_SoDienThoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SoDienThoaiActionPerformed(evt);
            }
        });
        getContentPane().add(txt_SoDienThoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 150, -1));

        buttonGroup1.add(rdo_Nu);
        rdo_Nu.setText("Nữ");
        rdo_Nu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_NuActionPerformed(evt);
            }
        });
        getContentPane().add(rdo_Nu, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, -1, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Chức Vụ:");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, -1, -1));

        txt_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailActionPerformed(evt);
            }
        });
        getContentPane().add(txt_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, 160, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Mã nhân viên:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        btn_xoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Delete.png"))); // NOI18N
        btn_xoa.setText("Xóa");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });
        getContentPane().add(btn_xoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 100, 100, -1));

        txt_maNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_maNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(txt_maNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 150, -1));

        cbo_chucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbo_chucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_chucVuActionPerformed(evt);
            }
        });
        getContentPane().add(cbo_chucVu, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, 160, -1));

        txt_hoVaTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_hoVaTenActionPerformed(evt);
            }
        });
        getContentPane().add(txt_hoVaTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 150, -1));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Trạng thái:");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, 19));

        buttonGroup1.add(rdo_Nam);
        rdo_Nam.setText("Nam");
        rdo_Nam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_NamActionPerformed(evt);
            }
        });
        getContentPane().add(rdo_Nam, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, -1, -1));

        cbo_trangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbo_trangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_trangThaiActionPerformed(evt);
            }
        });
        getContentPane().add(cbo_trangThai, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 120, 160, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null), "Tìm kiếm ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        cboLocCV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLocCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocCVActionPerformed(evt);
            }
        });

        jLabel6.setText("Chức Vụ:");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        btnTimKiem.setBackground(new java.awt.Color(204, 255, 255));
        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Search.png"))); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jLabel7.setText("Tìm kiếm: ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboLocCV, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnTimKiem)
                .addGap(0, 112, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboLocCV, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 890, 70));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Họ và tên:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Giới tính:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setText("Thông tin nhân viên");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 0, -1, 32));

        tab_TTNV.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDangLam.setBackground(new java.awt.Color(51, 153, 255));
        btnDangLam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDangLam.setText("Đang Làm");
        btnDangLam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangLamActionPerformed(evt);
            }
        });
        tab_TTNV.add(btnDangLam, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        btnDaNghi.setBackground(new java.awt.Color(51, 153, 255));
        btnDaNghi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDaNghi.setText("Đã Nghỉ");
        btnDaNghi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaNghiActionPerformed(evt);
            }
        });
        tab_TTNV.add(btnDaNghi, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));

        tbl_QLNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Họ và tên", "Giới tính", "Số điện thoại", "Email", "Chức Vụ", "Trạng thái"
            }
        ));
        tbl_QLNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_QLNVMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_QLNV);

        tab_TTNV.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 830, 188));

        jTabbedPane1.addTab("THÔNG TIN NHÂN VIÊN", tab_TTNV);

        tab_LLV.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_LLV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã lịch", "Mã nhân viên", "Họ và tên", "Ngày làm", "Ca làm", "Ghi chú"
            }
        ));
        tbl_LLV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_LLVMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbl_LLV);

        tab_LLV.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 880, 140));

        jLabel1.setText("Mã lịch:");
        tab_LLV.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel2.setText("Mã nhân viên:");
        tab_LLV.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, -1, -1));

        jLabel3.setText("Ngày làm:");
        tab_LLV.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, -1, -1));

        jLabel4.setText("Ca làm:");
        tab_LLV.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, -1, -1));

        cbo_CaLam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        tab_LLV.add(cbo_CaLam, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 110, -1));

        jLabel5.setText("Ghi chú:");
        tab_LLV.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, -1, -1));
        tab_LLV.add(txt_HoVaTenLLV, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 120, -1));
        tab_LLV.add(txt_MaLich, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 120, -1));
        tab_LLV.add(txt_GhiChu, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 50, 110, -1));
        tab_LLV.add(txt_NgayLam, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 120, -1));

        btn_themLLV.setText("Thêm");
        btn_themLLV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themLLVActionPerformed(evt);
            }
        });
        tab_LLV.add(btn_themLLV, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, 100, -1));

        btn_suaLLV.setText("Sửa");
        btn_suaLLV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaLLVActionPerformed(evt);
            }
        });
        tab_LLV.add(btn_suaLLV, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 10, 110, -1));

        btn_xoaLLV.setText("Xóa");
        btn_xoaLLV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaLLVActionPerformed(evt);
            }
        });
        tab_LLV.add(btn_xoaLLV, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 50, 100, 30));

        btn_lamMoiLLV.setText("Làm mới");
        btn_lamMoiLLV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lamMoiLLVActionPerformed(evt);
            }
        });
        tab_LLV.add(btn_lamMoiLLV, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 50, 110, -1));

        jLabel8.setText(" Họ và tên:");
        tab_LLV.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 60, -1));
        tab_LLV.add(txt_MaNhanVienLLV, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 120, -1));

        jTabbedPane1.addTab("LỊCH LÀM VIỆC", tab_LLV);

        tab_HSLV.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_HSLV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Họ và tên", "Số đơn hàng", "Tổng doanh thu", "Trung bình đơn", "Chỉ tiêu"
            }
        ));
        tbl_HSLV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_HSLVMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_HSLV);

        tab_HSLV.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 870, 130));

        btnKhenThuong.setText("Khen thưởng/Điều chỉnh");
        btnKhenThuong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnKhenThuongMouseClicked(evt);
            }
        });
        btnKhenThuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhenThuongActionPerformed(evt);
            }
        });
        tab_HSLV.add(btnKhenThuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, 200, 50));

        jLabel9.setText("Mã nhân viên:");
        tab_HSLV.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel10.setText("Họ và tên:");
        tab_HSLV.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel11.setText("Số đơn hàng");
        tab_HSLV.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, -1, -1));

        jLabel19.setText("Tổng doanh thu:");
        tab_HSLV.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, -1, -1));

        jLabel21.setText("Trung bình đơn");
        tab_HSLV.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, -1, -1));

        jLabel22.setText("Chỉ tiêu");
        tab_HSLV.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, -1, -1));
        tab_HSLV.add(txtMaNhanVienHSLV, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 100, -1));

        txtHoVaTenHSLV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoVaTenHSLVActionPerformed(evt);
            }
        });
        tab_HSLV.add(txtHoVaTenHSLV, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 100, -1));
        tab_HSLV.add(txtSoDonHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 100, -1));
        tab_HSLV.add(txtTongDoanhThu, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 100, -1));

        txtTrungBinhDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrungBinhDonActionPerformed(evt);
            }
        });
        tab_HSLV.add(txtTrungBinhDon, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 100, -1));
        tab_HSLV.add(txtChiTieu, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 100, -1));

        jTabbedPane1.addTab("HIỆU SUẤT LÀM VIỆC", tab_HSLV);

        tab_KHD.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_KHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã khóa", "Tên khóa", "Ngày bắt đầu", "Ngày kết thúc", "Mô tả", "Trạng thái"
            }
        ));
        tbl_KHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_KHDMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbl_KHD);

        tab_KHD.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 930, 143));

        jLabel23.setText("Mô tả:");
        tab_KHD.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, -1, -1));

        jLabel24.setText("Trạng thái:");
        tab_KHD.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, -1, -1));

        jLabel25.setText("Ngày kết thúc");
        tab_KHD.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, -1, -1));

        jLabel26.setText("Ngày bắt đầu:");
        tab_KHD.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, -1, -1));

        jLabel27.setText("Mã khóa:");
        tab_KHD.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel28.setText("Tên khóa:");
        tab_KHD.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));
        tab_KHD.add(txtTenKhoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 170, -1));
        tab_KHD.add(txtNgayBatDau, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 120, -1));

        txtNgayKetThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayKetThucActionPerformed(evt);
            }
        });
        tab_KHD.add(txtNgayKetThuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, 120, -1));
        tab_KHD.add(txtMoTa, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 170, -1));
        tab_KHD.add(txtMaKhoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 170, -1));

        cboTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        tab_KHD.add(cboTrangThai, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, 170, -1));

        btn_themKHL.setText("Thêm");
        btn_themKHL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themKHLActionPerformed(evt);
            }
        });
        tab_KHD.add(btn_themKHL, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, 90, -1));

        btn_suaKHL.setText("Sửa");
        btn_suaKHL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaKHLActionPerformed(evt);
            }
        });
        tab_KHD.add(btn_suaKHL, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 10, 110, -1));

        btn_lamMoiKHL.setText("Làm mới");
        btn_lamMoiKHL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lamMoiKHLActionPerformed(evt);
            }
        });
        tab_KHD.add(btn_lamMoiKHL, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 50, 110, -1));

        btn_xoaKHL.setText("Xóa");
        btn_xoaKHL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaKHLActionPerformed(evt);
            }
        });
        tab_KHD.add(btn_xoaKHL, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, 90, 30));

        jTabbedPane1.addTab("KHÓA HUẤN LUYỆN ", tab_KHD);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 950, 270));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
try {
        int row = tbl_QLNV.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để sửa");
            return;
        }

        String maNV = txt_maNhanVien.getText().trim();
        String hoTen = txt_hoVaTen.getText().trim();
        String sdt = txt_SoDienThoai.getText().trim();
        String email = txt_email.getText().trim();

        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không được để trống mã nhân viên");
            return;
        }

        if (hoTen.isEmpty() || !hoTen.matches("^[\\p{L}\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Tên không hợp lệ. Chỉ được chứa chữ và khoảng trắng.");
            return;
        }

        if (sdt.isEmpty() || !sdt.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ. Phải đúng 10 chữ số.");
            return;
        }

        if (email.isEmpty() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng");
            return;
        }

        for (NhanVien nvOld : rp.getAll()) {
            if (!nvOld.getMaNhanVien().equalsIgnoreCase(maNV)) {
                if (nvOld.getSoDienThoai().equals(sdt)) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại");
                    return;
                }
                if (nvOld.getEmail().equalsIgnoreCase(email)) {
                    JOptionPane.showMessageDialog(this, "Email đã tồn tại");
                    return;
                }
            }
        }

        String gioiTinh = rdo_Nam.isSelected() ? "Nam" : "Nữ";
        String tenChucVu = cbo_chucVu.getSelectedItem().toString();
String maChucVu = daoChucVu.getMaChucVuByTen(tenChucVu);
        int trangThai = cbo_trangThai.getSelectedItem().toString().equals("Đang làm") ? 0 : 1;

        NhanVien nv = new NhanVien(
                maNV, hoTen, gioiTinh, sdt, email, maChucVu, trangThai
        );

        if (rp.update(nv) > 0) {
            fillTable(rp.getAll());
            JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thất bại");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể cập nhật nhân viên");
    }
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_lamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lamMoiActionPerformed
        clearForm();
    }//GEN-LAST:event_btn_lamMoiActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        try {
        String maNV = daoNhanVien.getNextMaNhanVien().trim();
        String hoTen = txt_hoVaTen.getText().trim();
        String sdt = txt_SoDienThoai.getText().trim();
        String email = txt_email.getText().trim();

        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không được để trống mã nhân viên");
            return;
        }

        if (hoTen.isEmpty() || !hoTen.matches("^[\\p{L}\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Tên không hợp lệ. Chỉ được chứa chữ và khoảng trắng.");
            return;
        }

        if (sdt.isEmpty() || !sdt.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ. Phải đúng 10 chữ số.");
            return;
        }

        if (email.isEmpty() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng");
            return;
        }

        for (NhanVien nvOld : rp.getAll()) {
            if (nvOld.getMaNhanVien().equalsIgnoreCase(maNV)) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại");
                return;
            }
            if (nvOld.getSoDienThoai().equals(sdt)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại");
                return;
            }
            if (nvOld.getEmail().equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(this, "Email đã tồn tại");
                return;
            }
        }

        String gioiTinh = rdo_Nam.isSelected() ? "Nam" : "Nữ";
        String tenChucVu = cbo_chucVu.getSelectedItem().toString();
        String maChucVu = daoChucVu.getMaChucVuByTen(tenChucVu); // đổi tên -> mã
        int trangThai = cbo_trangThai.getSelectedItem().toString().equals("Đang làm") ? 0 : 1;

        NhanVien nv = new NhanVien(
                maNV, hoTen, gioiTinh, sdt, email, maChucVu, trangThai
        );

        if (rp.add(nv) > 0) {
            fillTable(rp.getAll());
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi thêm nhân viên");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể thêm nhân viên");
    }
    }//GEN-LAST:event_btn_themActionPerformed

    private void rdo_NuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_NuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdo_NuActionPerformed

    private void txt_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        try {
            int row = tbl_QLNV.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa");
                return;
            }

            String maNV = tbl_QLNV.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn chuyển nhân viên này thành nghỉ việc?", "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                if (rp.delete(maNV)) {
                    JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công");
                    fillTable(rp.getAll());
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên để xóa");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể xóa nhân viên");
        }
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void txt_maNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_maNhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_maNhanVienActionPerformed

    private void cbo_chucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_chucVuActionPerformed

    }//GEN-LAST:event_cbo_chucVuActionPerformed

    private void txt_hoVaTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_hoVaTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_hoVaTenActionPerformed

    private void rdo_NamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_NamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdo_NamActionPerformed

    private void cbo_trangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_trangThaiActionPerformed

    }//GEN-LAST:event_cbo_trangThaiActionPerformed

    private void cboLocCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocCVActionPerformed

    }//GEN-LAST:event_cboLocCVActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        String keyword = txtTimKiem.getText().trim();
        String vaiTro = cboLocCV.getSelectedItem() != null ? cboLocCV.getSelectedItem().toString() : "";
        filterTable(vaiTro, keyword);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnDangLamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangLamActionPerformed
       fillTable(rp.getByStatus(0));
    }//GEN-LAST:event_btnDangLamActionPerformed

    private void btnDaNghiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaNghiActionPerformed
        fillTable(rp.getByStatus(1));
    }//GEN-LAST:event_btnDaNghiActionPerformed

    private void tbl_QLNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_QLNVMouseClicked
        i = tbl_QLNV.getSelectedRow();
        showData();
    }//GEN-LAST:event_tbl_QLNVMouseClicked

    private void txt_SoDienThoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SoDienThoaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SoDienThoaiActionPerformed

    private void tbl_LLVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_LLVMouseClicked
        // TODO add your handling code here:
        i = tbl_LLV.getSelectedRow();
        showDataLichLamViec();
    }//GEN-LAST:event_tbl_LLVMouseClicked

    private void btn_themLLVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themLLVActionPerformed
        // TODO add your handling code here:
         try {
        // Lấy dữ liệu từ form (thay tên các component đúng với form của bạn)
        String maNhanVien = txt_MaNhanVienLLV.getText().trim();
        String hoTen = txt_HoVaTenLLV.getText().trim();
        String ngayLamStr = txt_NgayLam.getText().trim();
        String caLam = cbo_CaLam.getSelectedItem() != null ? cbo_CaLam.getSelectedItem().toString() : "";
        String ghiChu = txt_GhiChu.getText().trim();

        // Validate dữ liệu
        if (maNhanVien.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống");
            return;
        }
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống");
            return;
        }
        if (ngayLamStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ngày làm không được để trống");
            return;
        }
        if (caLam.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn ca làm");
            return;
        }

        // Chuyển ngày từ String -> java.sql.Date
        java.sql.Date ngayLam;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date parsed = sdf.parse(ngayLamStr);
            ngayLam = new java.sql.Date(parsed.getTime());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày làm không hợp lệ. Vui lòng nhập dd/MM/yyyy");
            return;
        }

        // Tạo mã lịch làm việc mới
        String maLLV = daoLLV.getNextMaLLV();

        // Kiểm tra trùng lịch của cùng nhân viên trong ngày
        for (LichLamViec llvOld : daoLLV.getAll()) {
            if (llvOld.getMaNhanVien().equalsIgnoreCase(maNhanVien)
                && llvOld.getNgayLam().equals(ngayLam)) {
                JOptionPane.showMessageDialog(this, "Nhân viên này đã có lịch vào ngày " + ngayLamStr);
                return;
            }
        }

        // Tạo đối tượng LichLamViec
        LichLamViec llv = new LichLamViec(maLLV, maNhanVien, hoTen, ngayLam, caLam, ghiChu);

        // Thêm vào database
        if (daoLLV.add(llv) > 0) {
            loadTableLichLamViec(); // load lại bảng
            JOptionPane.showMessageDialog(this, "Thêm lịch làm việc thành công");
            clearFormLLV();          // reset form
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi thêm lịch làm việc");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể thêm lịch làm việc");
    }
    }//GEN-LAST:event_btn_themLLVActionPerformed

    private void btn_xoaLLVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaLLVActionPerformed
    try {
        int row = tbl_LLV.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch làm việc cần xóa");
            return;
        }

        String maLLV = tbl_LLV.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa lịch làm việc này?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            LichLamViecDao daoLLV = new LichLamViecDao();
            boolean success = daoLLV.delete(maLLV);

            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa lịch làm việc thành công");
                loadTableLichLamViec(); // Load lại bảng
                clearFormLLV();          // Xóa form nhập liệu
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy lịch làm việc để xóa");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể xóa lịch làm việc");
    }
    }//GEN-LAST:event_btn_xoaLLVActionPerformed

    private void btn_suaLLVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaLLVActionPerformed
    try {
        int row = tbl_LLV.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch làm việc để sửa");
            return;
        }

        String maLLV = txt_MaLich.getText().trim();
        String maNhanVien = txt_MaNhanVienLLV.getText().trim();
        String hoTen = txt_HoVaTenLLV.getText().trim();
        String caLam = cbo_CaLam.getSelectedItem().toString();
        String ghiChu = txt_GhiChu.getText().trim();
        String ngayStr = txt_NgayLam.getText().trim();

        if (maLLV.isEmpty() || maNhanVien.isEmpty() || hoTen.isEmpty() || ngayStr.isEmpty() || caLam.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        // Chuyển chuỗi ngày sang java.sql.Date
        java.sql.Date ngayLam;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date utilDate = sdf.parse(ngayStr);
            ngayLam = new java.sql.Date(utilDate.getTime());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày không đúng định dạng dd/MM/yyyy");
            return;
        }

        LichLamViec llv = new LichLamViec(maLLV, maNhanVien, hoTen, ngayLam, caLam, ghiChu);

        LichLamViecDao daoLLV = new LichLamViecDao();
        if (daoLLV.update(llv) > 0) {
            JOptionPane.showMessageDialog(this, "Cập nhật lịch làm việc thành công");
            loadTableLichLamViec();
            clearFormLLV();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể cập nhật lịch làm việc");
    }
    }//GEN-LAST:event_btn_suaLLVActionPerformed

    private void btn_lamMoiLLVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lamMoiLLVActionPerformed
        // TODO add your handling code here:
        clearFormLLV();
    }//GEN-LAST:event_btn_lamMoiLLVActionPerformed

    private void txtHoVaTenHSLVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoVaTenHSLVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoVaTenHSLVActionPerformed

    private void txtTrungBinhDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrungBinhDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrungBinhDonActionPerformed

    private void tbl_HSLVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_HSLVMouseClicked
        // TODO add your handling code here:
        i = tbl_HSLV.getSelectedRow();
        showDataHSLV();
    }//GEN-LAST:event_tbl_HSLVMouseClicked

    private void btnKhenThuongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnKhenThuongMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnKhenThuongMouseClicked

    private void btnKhenThuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhenThuongActionPerformed
        // TODO add your handling code here:
        int row = tbl_HSLV.getSelectedRow();
    if(row >= 0){
    String maNV = tbl_HSLV.getValueAt(row, 0).toString();
    String hoTen = tbl_HSLV.getValueAt(row, 1).toString();
    double tongDoanhThu = Double.parseDouble(tbl_HSLV.getValueAt(row, 3).toString());
    double chiTieu = Double.parseDouble(tbl_HSLV.getValueAt(row, 5).toString());

    openPanelKhenThuongDieuChinh(maNV, hoTen, tongDoanhThu, chiTieu);
}

    }//GEN-LAST:event_btnKhenThuongActionPerformed

    private void txtNgayKetThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayKetThucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayKetThucActionPerformed

    private void tbl_KHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_KHDMouseClicked
        // TODO add your handling code here:
        i = tbl_KHD.getSelectedRow();
        showDataKhoaHD();
    }//GEN-LAST:event_tbl_KHDMouseClicked

    private void btn_themKHLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themKHLActionPerformed
        // TODO add your handling code here:
            try {
        // Lấy dữ liệu từ form
        String maKhoa = txtMaKhoa.getText().trim();
        String tieuDe = txtTenKhoa.getText().trim();
        String ngayBDStr = txtNgayBatDau.getText().trim();
        String ngayKTStr = txtNgayKetThuc.getText().trim();
        String moTa = txtMoTa.getText().trim();
        boolean trangThai = cboTrangThai.getSelectedItem() != null &&
                            cboTrangThai.getSelectedItem().toString().equals("Còn hoạt động");

        // Validate dữ liệu
        if (tieuDe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tiêu đề khóa huấn luyện không được để trống");
            return;
        }
        if (ngayBDStr.isEmpty() || ngayKTStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu và kết thúc không được để trống");
            return;
        }

        // Chuyển ngày từ String -> java.sql.Date
        java.sql.Date ngayBD, ngayKT;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            ngayBD = new java.sql.Date(sdf.parse(ngayBDStr).getTime());
            ngayKT = new java.sql.Date(sdf.parse(ngayKTStr).getTime());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ. Vui lòng nhập dd/MM/yyyy");
            return;
        }

        // Tạo mã khóa mới nếu trống
        if (maKhoa.isEmpty()) {
            maKhoa = daoKHD.getNextMaKhoaHD();
            txtMaKhoa.setText(maKhoa);
        }

        // Tạo đối tượng KhoaHuongDan
        KhoaHuongDan kh = new KhoaHuongDan(maKhoa, tieuDe, moTa, ngayBD, ngayKT, trangThai);

        // Thêm vào database
        if (daoKHD.add(kh) > 0) {
            loadTableKHD(); // load lại bảng
            JOptionPane.showMessageDialog(this, "Thêm khóa huấn luyện thành công");
            clearFormKHL();  // reset form
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi thêm khóa huấn luyện");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể thêm khóa huấn luyện");
    }
    }//GEN-LAST:event_btn_themKHLActionPerformed

    private void btn_suaKHLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaKHLActionPerformed
        // TODO add your handling code here:
        try {
    int row = tbl_KHD.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa huấn luyện để sửa");
        return;
    }

    String maKhoa = txtMaKhoa.getText().trim();
    String tieuDe = txtTenKhoa.getText().trim();
    String moTa = txtMoTa.getText().trim();
    String ngayBDStr = txtNgayBatDau.getText().trim();
    String ngayKTStr = txtNgayKetThuc.getText().trim();
    String trangThaiStr = cboTrangThai.getSelectedItem().toString();

    if (maKhoa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Mã khóa không được để trống");
        return;
    }

    if (tieuDe.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tiêu đề không được để trống");
        return;
    }

    // Chuyển ngày từ String sang java.util.Date
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    java.util.Date ngayBD = null;
    java.util.Date ngayKT = null;
    try {
        ngayBD = sdf.parse(ngayBDStr);
        ngayKT = sdf.parse(ngayKTStr);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Ngày bắt đầu hoặc kết thúc không đúng định dạng dd/MM/yyyy");
        return;
    }

    boolean trangThai = trangThaiStr.equals("Còn hoạt động");

    KhoaHuongDan kh = new KhoaHuongDan(maKhoa, tieuDe, moTa, ngayBD, ngayKT, trangThai);

    if (daoKHD.update(kh) > 0) {
        loadTableKHD(); // Load lại dữ liệu lên bảng
        JOptionPane.showMessageDialog(this, "Cập nhật khóa huấn luyện thành công");
        // clearFormKHD(); // Nếu có phương thức làm mới form
    } else {
        JOptionPane.showMessageDialog(this, "Cập nhật khóa huấn luyện thất bại");
    }

} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể cập nhật khóa huấn luyện");
}

    }//GEN-LAST:event_btn_suaKHLActionPerformed

    private void btn_lamMoiKHLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lamMoiKHLActionPerformed
        // TODO add your handling code here:
        clearFormKHL();
    }//GEN-LAST:event_btn_lamMoiKHLActionPerformed

    private void btn_xoaKHLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaKHLActionPerformed
        // TODO add your handling code here:
         try {
        int row = tbl_KHD.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa huấn luyện cần xóa");
            return;
        }

        String maKhoa = tbl_KHD.getValueAt(row, 0).toString(); 
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa khóa huấn luyện này?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = daoKHD.delete(maKhoa);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa khóa huấn luyện thành công");
                loadTableKHD();     // Load lại bảng
                clearFormKHL();     // Xóa form nhập liệu
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khóa huấn luyện để xóa");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể xóa khóa huấn luyện");
    }
    }//GEN-LAST:event_btn_xoaKHLActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(menumain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menumain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menumain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menumain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyView().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDaNghi;
    private javax.swing.JButton btnDangLam;
    private javax.swing.JButton btnKhenThuong;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btn_lamMoi;
    private javax.swing.JButton btn_lamMoiKHL;
    private javax.swing.JButton btn_lamMoiLLV;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_suaKHL;
    private javax.swing.JButton btn_suaLLV;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_themKHL;
    private javax.swing.JButton btn_themLLV;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JButton btn_xoaKHL;
    private javax.swing.JButton btn_xoaLLV;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboLocCV;
    private javax.swing.JComboBox<String> cboTrangThai;
    private javax.swing.JComboBox<String> cbo_CaLam;
    private javax.swing.JComboBox<String> cbo_chucVu;
    private javax.swing.JComboBox<String> cbo_trangThai;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rdo_Nam;
    private javax.swing.JRadioButton rdo_Nu;
    private javax.swing.JPanel tab_HSLV;
    private javax.swing.JPanel tab_KHD;
    private javax.swing.JPanel tab_LLV;
    private javax.swing.JPanel tab_TTNV;
    private javax.swing.JTable tbl_HSLV;
    private javax.swing.JTable tbl_KHD;
    private javax.swing.JTable tbl_LLV;
    private javax.swing.JTable tbl_QLNV;
    private javax.swing.JTextField txtChiTieu;
    private javax.swing.JTextField txtHoVaTenHSLV;
    private javax.swing.JTextField txtMaKhoa;
    private javax.swing.JTextField txtMaNhanVienHSLV;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextField txtNgayBatDau;
    private javax.swing.JTextField txtNgayKetThuc;
    private javax.swing.JTextField txtSoDonHang;
    private javax.swing.JTextField txtTenKhoa;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTongDoanhThu;
    private javax.swing.JTextField txtTrungBinhDon;
    private javax.swing.JTextField txt_GhiChu;
    private javax.swing.JTextField txt_HoVaTenLLV;
    private javax.swing.JTextField txt_MaLich;
    private javax.swing.JTextField txt_MaNhanVienLLV;
    private javax.swing.JTextField txt_NgayLam;
    private javax.swing.JTextField txt_SoDienThoai;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_hoVaTen;
    private javax.swing.JTextField txt_maNhanVien;
    // End of variables declaration//GEN-END:variables
}
