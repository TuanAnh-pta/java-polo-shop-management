/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package ui;

import Repository.Repository_NhanVien;
import dao.ChucVuDao;
import dao.KhoaHuongDanDao;
import dao.LichLamViecDao;
import dao.NhanVienDao;
import dao.NhanVien_KhoaHDDao;
import entity.ChucVu;
import entity.KhoaHuongDan;
import entity.LichLamViec;
import entity.NhanVien;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class NhanVienView1 extends javax.swing.JInternalFrame {

    private LichLamViecDao daoLLV = new LichLamViecDao();
    private Repository.Repository_NhanVien rp = new Repository_NhanVien();
    private ChucVuDao daoChucVu = new ChucVuDao();
    private NhanVienDao daoNhanVien = new NhanVienDao();
    private DefaultTableModel mol;
    private int i;
    private int t = 1;

    public NhanVienView1() {
        initComponents();
        txt_maNhanVien.setEditable(false);
        cbo_chucVu.setEditable(false);
        cbo_trangThai.setEditable(false);
        txt_MaLich.setEditable(false);

        cboTrangThai.setEnabled(false);
        cbo_CaLam.setEnabled(false);
        cbo_chucVu.setEnabled(false);
        cbo_trangThai.setEnabled(false);

        this.fillTable(rp.getByStatus(0));
        this.fillComboLocVT();
        fillComboChucVu();
        this.fillComboTT();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        this.loadTableLichLamViec();
        fillComboCaLam();
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

    public void loadTableLichLamViec() {
        DefaultTableModel model = (DefaultTableModel) tbl_LLV.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        ArrayList<LichLamViec> list = daoLLV.getAll();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // định dạng ngày

        for (LichLamViec x : list) {
            model.addRow(new Object[]{
                x.getMaLLV(),
                x.getMaNhanVien(),
                x.getHoTen(), // thêm cột họ tên
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

        // Tạo instance DAO
        KhoaHuongDanDao daoKHD = new KhoaHuongDanDao();
        ArrayList<KhoaHuongDan> list = daoKHD.getAll(); // Lấy tất cả khóa huấn luyện

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (KhoaHuongDan kh : list) {
            model.addRow(new Object[]{
                kh.getMaKhoaHD(), // Mã khóa
                kh.getTieuDe(), // Tiêu đề
                kh.getNgayBatDau() != null ? sdf.format(kh.getNgayBatDau()) : "", // Ngày bắt đầu
                kh.getNgayKetThuc() != null ? sdf.format(kh.getNgayKetThuc()) : "", // Ngày kết thúc
                kh.getMoTa(), // Mô tả
                kh.isTrangThai() ? "Còn hoạt động" : "Không hoạt động" // Trạng thái
            });
        }

        // Load combobox trạng thái nếu có
        cboTrangThai.removeAllItems();
        cboTrangThai.addItem("Còn hoạt động");
        cboTrangThai.addItem("Không hoạt động");
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

            boolean matchesKeyword
                    = nv.getMaNhanVien().toLowerCase().contains(keyword)
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

// ...
    public void showDataLichLamViec() {
        int row = tbl_LLV.getSelectedRow();
        if (row >= 0) {
            txt_MaLich.setText(tbl_LLV.getValueAt(row, 0).toString());        // MaLLV
            txt_MaNhanVienLLV.setText(tbl_LLV.getValueAt(row, 1).toString());    // MaNhanVien
            txt_HoVaTenLLV.setText(tbl_LLV.getValueAt(row, 2).toString());    // HoTen

            txt_NgayLam.setText(tbl_LLV.getValueAt(row, 3).toString());       // Ngày làm

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
        cbo_trangThai.setSelectedIndex(0);
    }

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt_SoDienThoai = new javax.swing.JTextField();
        rdo_Nu = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
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
        jLabel8 = new javax.swing.JLabel();
        txt_MaNhanVienLLV = new javax.swing.JTextField();
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
        btn_thamGiaKHL = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Email:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, -1, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Số điện thoại:");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        txt_SoDienThoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SoDienThoaiActionPerformed(evt);
            }
        });
        getContentPane().add(txt_SoDienThoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 150, -1));

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

        tab_LLV.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 870, 140));

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

        jLabel8.setText(" Họ và tên:");
        tab_LLV.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 60, -1));
        tab_LLV.add(txt_MaNhanVienLLV, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 120, -1));

        jTabbedPane1.addTab("LỊCH LÀM VIỆC", tab_LLV);

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
        tab_KHD.add(txtMoTa, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 190, -1));
        tab_KHD.add(txtMaKhoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 170, -1));

        cboTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        tab_KHD.add(cboTrangThai, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, 190, -1));

        btn_thamGiaKHL.setText("Tham gia");
        btn_thamGiaKHL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_thamGiaKHLActionPerformed(evt);
            }
        });
        tab_KHD.add(btn_thamGiaKHL, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, 170, 50));

        jTabbedPane1.addTab("KHÓA HUẤN LUYỆN ", tab_KHD);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 950, 270));

        btn_sua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Edit.png"))); // NOI18N
        btn_sua.setText("Sửa");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });
        getContentPane().add(btn_sua, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 50, 200, 60));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_SoDienThoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SoDienThoaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SoDienThoaiActionPerformed

    private void rdo_NuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_NuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdo_NuActionPerformed

    private void txt_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailActionPerformed

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

    private void tbl_QLNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_QLNVMouseClicked
        i = tbl_QLNV.getSelectedRow();
        showData();
    }//GEN-LAST:event_tbl_QLNVMouseClicked

    private void btnDaNghiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaNghiActionPerformed
        fillTable(rp.getByStatus(1));
    }//GEN-LAST:event_btnDaNghiActionPerformed

    private void btnDangLamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangLamActionPerformed
        fillTable(rp.getByStatus(0));
    }//GEN-LAST:event_btnDangLamActionPerformed

    private void tbl_LLVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_LLVMouseClicked
        // TODO add your handling code here:
        i = tbl_LLV.getSelectedRow();
        showDataLichLamViec();
    }//GEN-LAST:event_tbl_LLVMouseClicked

    private void tbl_KHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_KHDMouseClicked
        // TODO add your handling code here:
        i = tbl_KHD.getSelectedRow();
        showDataKhoaHD();
    }//GEN-LAST:event_tbl_KHDMouseClicked

    private void txtNgayKetThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayKetThucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayKetThucActionPerformed

    private void btn_thamGiaKHLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_thamGiaKHLActionPerformed
        try {
            int row = tbl_KHD.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa huấn luyện để tham gia");
                return;
            }

            // Lấy dữ liệu từ bảng khóa
            String maKhoaHD = tbl_KHD.getValueAt(row, 0).toString();
            String tieuDe = tbl_KHD.getValueAt(row, 1).toString();
            String trangThaiStr = tbl_KHD.getValueAt(row, 5).toString(); // cột trạng thái

            // Validate trạng thái
            if (!trangThaiStr.equals("Còn hoạt động")) {
                JOptionPane.showMessageDialog(this, "Không thể tham gia khóa đã ngưng hoạt động");
                return;
            }

            // Lấy mã nhân viên từ ô txt_maNhanVien
            String maNV = txt_maNhanVien.getText().trim();
            if (maNV.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên trước khi tham gia khóa");
                return;
            }

            // Kiểm tra đã tham gia chưa
            NhanVien_KhoaHDDao daoNVKHD = new NhanVien_KhoaHDDao();
            if (daoNVKHD.isDaThamGia(maNV, maKhoaHD)) {
                JOptionPane.showMessageDialog(this, "Nhân viên này đã tham gia khóa rồi");
                return;
            }

            // Xác nhận tham gia
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn tham gia khóa: " + tieuDe + "?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (daoNVKHD.themNhanVienKhoaHD(maNV, maKhoaHD) > 0) {
                    JOptionPane.showMessageDialog(this, "Tham gia khóa huấn luyện thành công");
                    loadTableKHD(); // load lại danh sách khóa
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: Không thể tham gia khóa huấn luyện");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống, không thể tham gia khóa huấn luyện");
        }
    }//GEN-LAST:event_btn_thamGiaKHLActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDaNghi;
    private javax.swing.JButton btnDangLam;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_thamGiaKHL;
    private javax.swing.JComboBox<String> cboLocCV;
    private javax.swing.JComboBox<String> cboTrangThai;
    private javax.swing.JComboBox<String> cbo_CaLam;
    private javax.swing.JComboBox<String> cbo_chucVu;
    private javax.swing.JComboBox<String> cbo_trangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rdo_Nam;
    private javax.swing.JRadioButton rdo_Nu;
    private javax.swing.JPanel tab_KHD;
    private javax.swing.JPanel tab_LLV;
    private javax.swing.JPanel tab_TTNV;
    private javax.swing.JTable tbl_KHD;
    private javax.swing.JTable tbl_LLV;
    private javax.swing.JTable tbl_QLNV;
    private javax.swing.JTextField txtMaKhoa;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextField txtNgayBatDau;
    private javax.swing.JTextField txtNgayKetThuc;
    private javax.swing.JTextField txtTenKhoa;
    private javax.swing.JTextField txtTimKiem;
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
