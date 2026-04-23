package ui;

import dao.ChiTietSanPham2_Ipml;
import dao.DotGiamGia_Ipml;
import dao.PhieuGiamGiaDAO;
import entity.ChiTietSanPham2;
import entity.DotGiamGia;
import entity.PhieuGiamGia;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class KhuyenMaiView_1 extends javax.swing.JInternalFrame implements Controller.Crud_DotGiamGia {

    private ChiTietSanPham2_Ipml repoCT = new ChiTietSanPham2_Ipml();
    private DotGiamGia_Ipml repodgg = new DotGiamGia_Ipml();
    private DefaultTableModel mol = new DefaultTableModel();
    private int index = -1;
    private PhieuGiamGiaDAO phieuGiamGiaDAO;
    private PhieuGiamGiaDAO daoPGG = new PhieuGiamGiaDAO();
    private List<PhieuGiamGia> listPGG;
    private DefaultTableModel defaultTableModel;
    private int selectedId = -1;

    public KhuyenMaiView_1() {
        initComponents();
        phieuGiamGiaDAO = new PhieuGiamGiaDAO();
        defaultTableModel = new DefaultTableModel();
        listPGG = daoPGG.getAll();
        tblKM.setModel(defaultTableModel);
        defaultTableModel.addColumn("maPhieu");
        defaultTableModel.addColumn("tenPhieu");
        defaultTableModel.addColumn("tenHinhThucGG");
        defaultTableModel.addColumn("giaTri");
        defaultTableModel.addColumn("soLuong");
        defaultTableModel.addColumn("ngayBatDau");
        defaultTableModel.addColumn("ngayKetThuc");
        defaultTableModel.addColumn("dieuKienApDung");
        defaultTableModel.addColumn("trangThai");

        cboHinhThucGG.addItem("-- Chọn hình thức --");
        cboHinhThucGG.addItem("Giảm tiền mặt theo tổng đơn hàng");
        cboHinhThucGG.addItem("Giảm theo phầm trăm");

        fillComboLocHtgg();
//        fillTable(phieuGiamGiaDAO.getAll());
        fillTable(phieuGiamGiaDAO.getAll());

        cbo_loaiGiamGia.removeAllItems();
        cbo_loaiGiamGia.addItem("-- Chọn hình thức --");
        cbo_loaiGiamGia.addItem("PhanTram");
        cbo_loaiGiamGia.addItem("TienMat");

        cboLocdgg.removeAllItems();
        cboLocdgg.addItem("Mời chọn đợt giảm giá");
        for (DotGiamGia x : repodgg.findTrangThai()) {
            cboLocdgg.addItem(x.getMaDotGG());
        }
        this.fillToTable();
        edit_locDgg();
        this.fillToTable_CTSP2();

        loadCboDotGiamGia();
        loadCboDotGiamGiaCT();
    }

    private boolean validateForm() {
        if (txtMaPhieu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã phiếu!");
            txtMaPhieu.requestFocus();
            return false;
        }

        if (txtTenPhieu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên chương trình!");
            txtTenPhieu.requestFocus();
            return false;
        }

        if (cboHinhThucGG.getSelectedItem() == null || cboHinhThucGG.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hình thức giảm giá!");
            cboHinhThucGG.requestFocus();
            return false;
        }

        try {
            BigDecimal giaTri = new BigDecimal(txtGiaTri.getText().trim());
            if (giaTri.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Giá trị giảm giá phải lớn hơn 0!");
                txtGiaTri.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị giảm giá không hợp lệ!");
            txtGiaTri.requestFocus();
            return false;
        }

        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                txtSoLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
            txtSoLuong.requestFocus();
            return false;
        }

        Date startDate = dtBatDau.getDate();
        Date endDate = dtKetThuc.getDate();
        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và ngày kết thúc!");
            return false;
        }
        if (startDate.after(endDate)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được sau ngày kết thúc!");
            return false;
        }

        // ✅ Chỉ check trùng mã khi thêm mới (selectedId == -1)
        if (selectedId == -1 && phieuGiamGiaDAO.isMaPhieuExists(txtMaPhieu.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Mã phiếu đã tồn tại!");
            txtMaPhieu.requestFocus();
            return false;
        }

        return true;
    }

    private void fillTable(List<PhieuGiamGia> list) {
        defaultTableModel.setRowCount(0);
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN")); // Định dạng VN

        for (PhieuGiamGia p : list) {
            String trangThaiStr = (p.getTrangThai() != null && p.getTrangThai())
                    ? "Đang hoạt động"
                    : "Ngừng hoạt động";

            String giaTriStr = nf.format(p.getGiaTri());
            String dieuKienStr = nf.format(p.getDieuKienApDung());

            defaultTableModel.addRow(new Object[]{
                p.getMaPhieu(),
                p.getTenPhieu(),
                p.getTenHinhThucGG(),
                giaTriStr, // giá trị đã format
                p.getSoLuong(),
                p.getNgayBatDau(),
                p.getNgayKetThuc(),
                dieuKienStr, // điều kiện áp dụng đã format
                trangThaiStr
            });
        }
    }

    private void filterTable(String dieuKienLoc, String keyword) {

        defaultTableModel.setRowCount(0);
        keyword = keyword.toLowerCase();

        BigDecimal dkSelected = null;
        boolean isTatCa = dieuKienLoc.equalsIgnoreCase("Tất cả");

        // Nếu không phải "Tất cả" thì parse sang BigDecimal để so sánh chính xác
        if (!isTatCa) {
            try {
                // Loại bỏ " đ", rồi mới bỏ dấu . và ,
                String rawNumber = dieuKienLoc.replace(" đ", "").replace(".", "").replace(",", "");
                dkSelected = new BigDecimal(rawNumber);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi định dạng điều kiện lọc: " + dieuKienLoc);
                return;
            }
        }

        for (PhieuGiamGia pgg : listPGG) {
            String ma = pgg.getMaPhieu().toLowerCase();
            String ten = pgg.getTenPhieu().toLowerCase();

            boolean matchKeyword = ma.contains(keyword) || ten.contains(keyword);

            boolean matchDieuKien = isTatCa || pgg.getDieuKienApDung().compareTo(dkSelected) == 0;

            if (matchKeyword && matchDieuKien) {
                defaultTableModel.addRow(new Object[]{
                    pgg.getMaPhieu(),
                    pgg.getTenPhieu(),
                    pgg.getTenHinhThucGG(),
                    pgg.getGiaTri(),
                    pgg.getSoLuong(),
                    pgg.getNgayBatDau(),
                    pgg.getNgayKetThuc(),
                    pgg.getDieuKienApDung(),
                    pgg.isTrangThai() ? "Đang hoạt động" : "Không hoạt động"
                });
            }
        }
    }

    private void fillComboLocHtgg() {
        cboLocHtgg.removeAllItems();

        Set<BigDecimal> dieuKienSet = new TreeSet<>();
        for (PhieuGiamGia pgg : listPGG) {
            dieuKienSet.add(pgg.getDieuKienApDung());
        }

        cboLocHtgg.addItem("Tất cả");

        for (BigDecimal dk : dieuKienSet) {
            // Format như "2.000.000 đ"
            String formatted = NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(dk) + " đ";
            cboLocHtgg.addItem(formatted); // vẫn add String nếu bạn muốn hiển thị đẹp
        }
    }

    private PhieuGiamGia readFromForm() {

        String maPhieu = txtMaPhieu.getText().trim();
        String tenPhieu = txtTenPhieu.getText().trim();
        String tenHinhThucGG = (cboHinhThucGG.getSelectedItem() != null)
                ? cboHinhThucGG.getSelectedItem().toString().trim()
                : "";

        // Giá trị giảm giá
        BigDecimal giaTri = parseBigDecimal(txtGiaTri.getText());
        BigDecimal dieuKienApDung = parseBigDecimal(txtDK.getText());
        try {
            String gtText = txtGiaTri.getText().trim();
            giaTri = (gtText.isEmpty()) ? BigDecimal.ZERO : new BigDecimal(gtText);
        } catch (NumberFormatException e) {
            giaTri = BigDecimal.ZERO;
        }

        // Số lượng
        int soLuong;
        try {
            String slText = txtSoLuong.getText().trim();
            soLuong = (slText.isEmpty()) ? 0 : Integer.parseInt(slText);
        } catch (NumberFormatException e) {
            soLuong = 0;
        }

        // Ngày bắt đầu & kết thúc
        Date ngayBatDau = dtBatDau.getDate();
        Date ngayKetThuc = dtKetThuc.getDate();

        // Điều kiện áp dụng
        //cố định ---------------------------------------------------------========================================
        try {
            String dkText = txtDK.getText().trim();
            dieuKienApDung = (dkText.isEmpty()) ? BigDecimal.ZERO : new BigDecimal(dkText);
        } catch (NumberFormatException e) {
            dieuKienApDung = BigDecimal.ZERO;
        }

        boolean trangThai = rdoHD.isSelected();

        return new PhieuGiamGia(
                maPhieu,
                tenPhieu,
                tenHinhThucGG,
                giaTri,
                soLuong,
                ngayBatDau,
                ngayKetThuc,
                dieuKienApDung,
                trangThai
        );
    }

    private BigDecimal parseBigDecimal(String text) {
        if (text == null) {
            return BigDecimal.ZERO;
        }

        // Bỏ hết ký tự không phải số hoặc dấu phẩy
        String cleaned = text.replaceAll("[^0-9,]", "")
                .replace(",", ".")
                .trim();

        if (cleaned.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(cleaned);
    }

    private BigDecimal parseCurrency(String text) {
        if (text == null) {
            return BigDecimal.ZERO;
        }

        // Bỏ chữ, khoảng trắng và ký tự không phải số hoặc dấu phẩy, chấm
        String cleaned = text.replaceAll("[^0-9,\\.]", "")
                .replace(",", ".")
                .trim();

        if (cleaned.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(cleaned);
    }

    private String generateNewMaPhieu() {
        String lastMa = phieuGiamGiaDAO.getLastMaPhieu();
        if (lastMa == null) {
            return "PGG001";
        }
        int number = Integer.parseInt(lastMa.substring(3)) + 1;
        return String.format("PGG%03d", number);
    }

    private void clearForm() {
        selectedId = -1;
        txtMaPhieu.setText("");
        txtTenPhieu.setText("");
        cboHinhThucGG.setSelectedIndex(0);
        txtGiaTri.setText("");
        txtSoLuong.setText("");
        dtBatDau.setDate(new Date());
        dtKetThuc.setDate(new Date());
        txtDK.setText("");
        rdoHD.setSelected(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKM = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtMaPhieu = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTenPhieu = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtGiaTri = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        dtBatDau = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        dtKetThuc = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        txtDK = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        rdoHD = new javax.swing.JRadioButton();
        rdoKHD = new javax.swing.JRadioButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        cboHinhThucGG = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        cboLocHtgg = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        btn_ngungApDung = new javax.swing.JButton();
        btn_apDungg = new javax.swing.JButton();
        cbo_maDotGiamGia = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbldgg = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMauSacCT = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtTendot = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtGiaTrigg = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblChiTietKM = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        dt_BatDau = new com.toedter.calendar.JDateChooser();
        jLabel22 = new javax.swing.JLabel();
        dt_ketThuc = new com.toedter.calendar.JDateChooser();
        jLabel24 = new javax.swing.JLabel();
        rdo_hoatDong = new javax.swing.JRadioButton();
        rdo_ngunghd = new javax.swing.JRadioButton();
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        cbo_loaiGiamGia = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txt_timkiem = new javax.swing.JTextField();
        btn_TimKiem = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        cboLocdgg = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        jLabel30 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtMadot = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtGiaGiamCT = new javax.swing.JTextField();
        txtMaDotCT = new javax.swing.JTextField();
        txtTenSPCT = new javax.swing.JTextField();
        txtMaCT = new javax.swing.JTextField();
        txtKTCT = new javax.swing.JTextField();
        txtSLCT = new javax.swing.JTextField();
        txtGiaGocCT = new javax.swing.JTextField();
        btn_fillAll = new javax.swing.JButton();

        setMaximizable(true);
        setResizable(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel3.setText("Danh sách khuyến mại");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));

        tblKM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã phiếu", "Tên chương trình", "Hình thức giảm giá", "Giá trị giảm giá", "Số lượng", "Ngày bắt đầu", "Ngày kết thúc", "Điều kiện áp dụng", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKMMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKM);
        if (tblKM.getColumnModel().getColumnCount() > 0) {
            tblKM.getColumnModel().getColumn(0).setResizable(false);
            tblKM.getColumnModel().getColumn(1).setResizable(false);
            tblKM.getColumnModel().getColumn(2).setResizable(false);
            tblKM.getColumnModel().getColumn(3).setResizable(false);
            tblKM.getColumnModel().getColumn(4).setResizable(false);
            tblKM.getColumnModel().getColumn(4).setHeaderValue("Số lượng");
            tblKM.getColumnModel().getColumn(5).setResizable(false);
            tblKM.getColumnModel().getColumn(6).setResizable(false);
            tblKM.getColumnModel().getColumn(7).setResizable(false);
            tblKM.getColumnModel().getColumn(8).setResizable(false);
        }

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 940, 240));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel5.setText("Phiếu giảm giá");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 2, -1, -1));

        jLabel1.setText("Mã phiếu ");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 41, -1, -1));
        jPanel2.add(txtMaPhieu, new org.netbeans.lib.awtextra.AbsoluteConstraints(142, 35, 208, 28));

        jLabel6.setText("Tên chương trình");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 95, -1, -1));
        jPanel2.add(txtTenPhieu, new org.netbeans.lib.awtextra.AbsoluteConstraints(142, 87, 210, 32));

        jLabel11.setText("Hình thức giảm giá");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel7.setText("Giá trị giảm giá");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));
        jPanel2.add(txtGiaTri, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 161, -1));

        jLabel12.setText("Số lượng");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));
        jPanel2.add(txtSoLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, 163, -1));

        jLabel8.setText("Thời gian bắt đầu");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, -1, -1));
        jPanel2.add(dtBatDau, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 30, 163, 28));

        jLabel9.setText("Thời gian kết thúc");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 80, -1, -1));
        jPanel2.add(dtKetThuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 70, 163, 28));

        jLabel13.setText("Điều kiện áp dụng");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 110, -1, -1));

        txtDK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDKActionPerformed(evt);
            }
        });
        jPanel2.add(txtDK, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 110, 160, -1));

        jLabel10.setText("Trạng thái");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 150, -1, -1));

        buttonGroup1.add(rdoHD);
        rdoHD.setText("Đang hoạt động");
        rdoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHDActionPerformed(evt);
            }
        });
        jPanel2.add(rdoHD, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 150, -1, -1));

        buttonGroup1.add(rdoKHD);
        rdoKHD.setText("Ngừng hoạt động");
        jPanel2.add(rdoKHD, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 180, -1, -1));

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        jPanel2.add(btnThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 210, 92, 30));

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Edit.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        jPanel2.add(btnSua, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 210, 93, 30));

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        jPanel2.add(btnXoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 210, 88, 30));

        cboHinhThucGG.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cboHinhThucGG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHinhThucGGActionPerformed(evt);
            }
        });
        jPanel2.add(cboHinhThucGG, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 200, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null), "Tìm kiếm ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel14.setText("Điều Kiện Áp Dụng");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        btnTimKiem.setBackground(new java.awt.Color(204, 255, 255));
        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Search.png"))); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTimKiemMouseClicked(evt);
            }
        });
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jLabel15.setText("Tìm kiếm: ");

        cboLocHtgg.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cboLocHtgg.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLocHtggItemStateChanged(evt);
            }
        });
        cboLocHtgg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboLocHtggMouseClicked(evt);
            }
        });
        cboLocHtgg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocHtggActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(cboLocHtgg, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTimKiem)
                .addGap(46, 46, 46))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboLocHtgg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 940, 70));

        jTabbedPane1.addTab("Phiếu giảm giá", jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel27.setText("Chi tiết");
        jPanel3.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, -1, -1));

        btn_ngungApDung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Edit.png"))); // NOI18N
        btn_ngungApDung.setText("Ngừng ");
        btn_ngungApDung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ngungApDungActionPerformed(evt);
            }
        });
        jPanel3.add(btn_ngungApDung, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 40, 100, 30));

        btn_apDungg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
        btn_apDungg.setText("Áp Dụng");
        btn_apDungg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_apDunggActionPerformed(evt);
            }
        });
        jPanel3.add(btn_apDungg, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 40, 110, 30));

        cbo_maDotGiamGia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbo_maDotGiamGia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_maDotGiamGiaItemStateChanged(evt);
            }
        });
        cbo_maDotGiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_maDotGiamGiaActionPerformed(evt);
            }
        });
        jPanel3.add(cbo_maDotGiamGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 40, 140, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel4.setText("Danh sách khuyến mại");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, -1, -1));

        tbldgg.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã đợt", "Tên đợt", "Loại GG", "Giá trị GG", "Ngày bắt đầu", "Ngày kết thúc", "Mô tả", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbldgg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbldggMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbldgg);
        if (tbldgg.getColumnModel().getColumnCount() > 0) {
            tbldgg.getColumnModel().getColumn(0).setResizable(false);
            tbldgg.getColumnModel().getColumn(1).setResizable(false);
            tbldgg.getColumnModel().getColumn(2).setResizable(false);
            tbldgg.getColumnModel().getColumn(3).setResizable(false);
            tbldgg.getColumnModel().getColumn(4).setResizable(false);
            tbldgg.getColumnModel().getColumn(5).setResizable(false);
            tbldgg.getColumnModel().getColumn(6).setResizable(false);
            tbldgg.getColumnModel().getColumn(7).setResizable(false);
        }

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 580, 240));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel16.setText("Đợt giảm giá");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 2, -1, -1));

        jLabel2.setText("Giá sau giảm");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 240, -1, -1));
        jPanel3.add(txtMauSacCT, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 230, 160, 30));

        jLabel17.setText("Tên đợt:");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));
        jPanel3.add(txtTendot, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 160, 30));

        jLabel18.setText("Loại giảm giá");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        jLabel19.setText("Giá trị giảm giá");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));
        jPanel3.add(txtGiaTrigg, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 150, -1));

        tblChiTietKM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã đợt ", "Mã CT", "Tên SP", "Màu sắc", "Kích Thước", "Số Lượng", "Giá gốc", "Giá giảm"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblChiTietKM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChiTietKMMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblChiTietKM);
        if (tblChiTietKM.getColumnModel().getColumnCount() > 0) {
            tblChiTietKM.getColumnModel().getColumn(0).setResizable(false);
            tblChiTietKM.getColumnModel().getColumn(1).setResizable(false);
            tblChiTietKM.getColumnModel().getColumn(2).setResizable(false);
            tblChiTietKM.getColumnModel().getColumn(3).setResizable(false);
            tblChiTietKM.getColumnModel().getColumn(4).setResizable(false);
            tblChiTietKM.getColumnModel().getColumn(6).setResizable(false);
        }

        jPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 390, 530, 240));

        jLabel20.setText("Mô tả");
        jPanel3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, -1, -1));

        jLabel21.setText("Thời gian bắt đầu");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, -1, -1));
        jPanel3.add(dt_BatDau, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 160, 28));

        jLabel22.setText("Thời gian kết thúc");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, -1, -1));
        jPanel3.add(dt_ketThuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 163, 28));

        jLabel24.setText("Trạng thái");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 160, -1, -1));

        buttonGroup1.add(rdo_hoatDong);
        rdo_hoatDong.setText("Đang hoạt động");
        rdo_hoatDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_hoatDongActionPerformed(evt);
            }
        });
        jPanel3.add(rdo_hoatDong, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, -1, -1));

        buttonGroup1.add(rdo_ngunghd);
        rdo_ngunghd.setText("Ngừng hoạt động");
        jPanel3.add(rdo_ngunghd, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, -1, -1));

        btn_them.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });
        jPanel3.add(btn_them, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 92, 30));

        btn_sua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Edit.png"))); // NOI18N
        btn_sua.setText("Sửa");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });
        jPanel3.add(btn_sua, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 93, 30));

        btn_xoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Delete.png"))); // NOI18N
        btn_xoa.setText("Xóa");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });
        jPanel3.add(btn_xoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 230, 88, 30));

        cbo_loaiGiamGia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbo_loaiGiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_loaiGiamGiaActionPerformed(evt);
            }
        });
        jPanel3.add(cbo_loaiGiamGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 160, -1));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null), "Tìm kiếm ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel25.setText("Mã đợt:");

        txt_timkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_timkiemActionPerformed(evt);
            }
        });

        btn_TimKiem.setBackground(new java.awt.Color(204, 255, 255));
        btn_TimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_TimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Search.png"))); // NOI18N
        btn_TimKiem.setText("Tìm kiếm");
        btn_TimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TimKiemActionPerformed(evt);
            }
        });

        jLabel26.setText("Tìm kiếm: ");

        cboLocdgg.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cboLocdgg.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLocdggItemStateChanged(evt);
            }
        });
        cboLocdgg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocdggActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboLocdgg, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_TimKiem)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboLocdgg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(txt_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_TimKiem))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 570, 70));

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane3.setViewportView(txtMoTa);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, 260, 50));

        jLabel30.setText("Mã áp dụng:");
        jPanel3.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 40, -1, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel23.setText("Danh sách Chi tiết");
        jPanel3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 360, -1, -1));

        jLabel29.setText("Mã đợt:");
        jPanel3.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        jPanel3.add(txtMadot, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 160, 28));

        jLabel28.setText("Mã CT");
        jPanel3.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 160, -1, -1));

        jLabel31.setText("Mã đợt");
        jPanel3.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 120, 50, -1));

        jLabel32.setText("Tên SP");
        jPanel3.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 200, -1, -1));

        jLabel33.setText("Màu sắc");
        jPanel3.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 240, -1, -1));

        jLabel34.setText("Kích thước");
        jPanel3.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 110, -1, -1));

        jLabel35.setText("Số Lượng");
        jPanel3.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 150, -1, -1));

        jLabel36.setText("Giá gốc");
        jPanel3.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 190, -1, -1));
        jPanel3.add(txtGiaGiamCT, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 230, 160, 30));
        jPanel3.add(txtMaDotCT, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 160, 30));
        jPanel3.add(txtTenSPCT, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 190, 160, 30));
        jPanel3.add(txtMaCT, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 150, 160, 30));
        jPanel3.add(txtKTCT, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 110, 160, 30));
        jPanel3.add(txtSLCT, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 150, 160, 30));
        jPanel3.add(txtGiaGocCT, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 190, 160, 30));

        btn_fillAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
        btn_fillAll.setText("fill Sản phẩm");
        btn_fillAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_fillAllActionPerformed(evt);
            }
        });
        jPanel3.add(btn_fillAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 350, 140, 30));

        jTabbedPane1.addTab("Đợt giảm giá", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1192, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboLocdggActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocdggActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLocdggActionPerformed

    private void btn_TimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TimKiemActionPerformed
        // TODO add your handling code here

        String name = txt_timkiem.getText();
        this.fillToTable_tendot(name);
    }//GEN-LAST:event_btn_TimKiemActionPerformed

    private void txt_timkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_timkiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_timkiemActionPerformed

    private void cbo_loaiGiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_loaiGiamGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_loaiGiamGiaActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        int selectedRow = tbldgg.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đợt giảm giá cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return; // chưa chọn dòng → dừng
        }

        int chon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa đợt giảm giá không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (chon == JOptionPane.YES_OPTION) {
            try {
                this.delete();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                edit_locDgg(); // load lại bảng
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        loadCboDotGiamGia();

    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        // TODO add your handling code here:
        String currentMaDot = txtMauSacCT.getText().trim(); // hoặc từ biến lưu trước đó
        // Kiểm tra form, truyền mã cũ để bỏ qua khi check trùng
        if (validateFormForUpdate(currentMaDot)) {
            this.update();
        }
        loadCboDotGiamGia();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        if (checkForm()) {
            this.create();
        }
        loadCboDotGiamGia();

    }//GEN-LAST:event_btn_themActionPerformed

    private void rdo_hoatDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_hoatDongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdo_hoatDongActionPerformed

    private void tbldggMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbldggMouseClicked
        // TODO add your handling code here:
        int row = tbldgg.getSelectedRow();
        if (row < 0) {
            return;
        }

// Load form đợt giảm giá
        index = row;
        edit();

// Lấy mã đợt tại dòng được chọn
        String maDot = tbldgg.getValueAt(row, 0).toString();

// Load danh sách chi tiết theo mã đợt
        fillToTable_madot(maDot);

// ====== Thêm đoạn này ======
        if (tblChiTietKM.getRowCount() > 0) {
            tblChiTietKM.setRowSelectionInterval(0, 0); // chọn dòng đầu tiên
            editCT(0); // load textbox theo dòng đầu tiên
        } else {
            clearCT(); // nếu không có chi tiết → clear form
        }

    }//GEN-LAST:event_tbldggMouseClicked

    private void cboLocHtggActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocHtggActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLocHtggActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        String dkStr = cboLocHtgg.getSelectedItem() != null ? cboLocHtgg.getSelectedItem().toString() : "Tất cả";
        filterTable(dkStr, keyword);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void cboHinhThucGGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHinhThucGGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboHinhThucGGActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblKM.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
            return;
        }
        String maPhieu = tblKM.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xoá phiếu \"" + maPhieu + "\"?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            phieuGiamGiaDAO.deleteByMaPhieu(maPhieu);
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            fillTable(phieuGiamGiaDAO.getAll());
            clearForm();

        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (!validateForm()) {
            return;
        }
        phieuGiamGiaDAO.update(readFromForm());
        clearForm();
        JOptionPane.showMessageDialog(this, "Sửa thành công");
        fillTable(phieuGiamGiaDAO.getAll());
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        if (!validateForm()) {
            return;
        }

        PhieuGiamGia phieu = readFromForm();
        phieu.setMaPhieu(generateNewMaPhieu()); // luôn tạo mã mới

        phieuGiamGiaDAO.create(phieu);
        clearForm();
        JOptionPane.showMessageDialog(this, "Thêm thành công");
        fillTable(phieuGiamGiaDAO.getAll());
    }//GEN-LAST:event_btnThemActionPerformed

    private void rdoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoHDActionPerformed

    private void txtDKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDKActionPerformed

    private void tblKMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKMMouseClicked
        int selectedRow = tblKM.getSelectedRow();
        if (selectedRow >= 0) {

            // Gán selectedId theo vị trí trong listPGG
            selectedId = selectedRow;

            txtMaPhieu.setText(tblKM.getValueAt(selectedRow, 0).toString());
            txtTenPhieu.setText(tblKM.getValueAt(selectedRow, 1).toString());
            cboHinhThucGG.setSelectedItem(tblKM.getValueAt(selectedRow, 2).toString());

            // Bỏ " đồng" và dấu . khi set vào textfield
            txtGiaTri.setText(tblKM.getValueAt(selectedRow, 3).toString()
                    .replace(" đồng", "").replace(".", "").trim());

            txtSoLuong.setText(tblKM.getValueAt(selectedRow, 4).toString());

            Object ngayBD = tblKM.getValueAt(selectedRow, 5);
            if (ngayBD instanceof Date) {
                dtBatDau.setDate((Date) ngayBD);
            } else {
                dtBatDau.setDate(null);
            }

            Object ngayKT = tblKM.getValueAt(selectedRow, 6);
            if (ngayKT instanceof Date) {
                dtKetThuc.setDate((Date) ngayKT);
            } else {
                dtKetThuc.setDate(null);
            }

            txtDK.setText(tblKM.getValueAt(selectedRow, 7).toString()
                    .replace(" đồng", "").replace(".", "").trim());

            String trangThai = tblKM.getValueAt(selectedRow, 8).toString();
            if (trangThai.equals("Đang hoạt động")) {
                rdoHD.setSelected(true);
            } else {
                rdoKHD.setSelected(true);
            }
        }
    }//GEN-LAST:event_tblKMMouseClicked

    private void cboLocHtggItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLocHtggItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLocHtggItemStateChanged

    private void cboLocHtggMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboLocHtggMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLocHtggMouseClicked

    private void cboLocdggItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLocdggItemStateChanged
        // TODO add your handling code here:
        edit_locDgg();
    }//GEN-LAST:event_cboLocdggItemStateChanged

    private void btnTimKiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTimKiemMouseClicked

    private void tblChiTietKMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiTietKMMouseClicked
        // TODO add your handling code here:
        index = tblChiTietKM.getSelectedRow();
        this.editCT(index);
    }//GEN-LAST:event_tblChiTietKMMouseClicked

    private void btn_apDunggActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_apDunggActionPerformed
        // TODO add your handling code here:
        int row = tblChiTietKM.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết sản phẩm để áp dụng giảm giá!");
            return;
        }

        String maCTSP = (String) tblChiTietKM.getValueAt(row, 1); // MaChiTietSP
        String maDotGG = (String) cbo_maDotGiamGia.getSelectedItem(); // Chọn đợt giảm giá

        if (maDotGG == null || maDotGG.equals("Mời chọn đợt giảm giá")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đợt giảm giá!");
            return;
        }

        try {
            repoCT.applyDiscount(maCTSP, maDotGG);
            fillToTable_CTSP2();
            JOptionPane.showMessageDialog(this, "Áp dụng giảm giá thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi khi áp dụng giảm giá: " + ex.getMessage());
        }
    }//GEN-LAST:event_btn_apDunggActionPerformed

    private void btn_ngungApDungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ngungApDungActionPerformed
        // TODO add your handling code here:
        int row = tblChiTietKM.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết sản phẩm để ngừng áp dụng!");
            return;
        }

        String maCTSP = (String) tblChiTietKM.getValueAt(row, 1); // MaChiTietSP

        try {
            repoCT.cancelDiscount(maCTSP);
            fillToTable_CTSP2();
            JOptionPane.showMessageDialog(this, "Ngừng áp dụng giảm giá thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi khi ngừng áp dụng giảm giá: " + ex.getMessage());
        }
    }//GEN-LAST:event_btn_ngungApDungActionPerformed

    private void cbo_maDotGiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_maDotGiamGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_maDotGiamGiaActionPerformed

    private void cbo_maDotGiamGiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_maDotGiamGiaItemStateChanged
        // TODO add your handling code here:
        edit_ct();
    }//GEN-LAST:event_cbo_maDotGiamGiaItemStateChanged

    private void btn_fillAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_fillAllActionPerformed
        // TODO add your handling code here:
        this.fillToTable_CTSP2();
    }//GEN-LAST:event_btn_fillAllActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
////                new KhuyenMaiView().pack();
//                new KhuyenMaiView_1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_apDungg;
    private javax.swing.JButton btn_fillAll;
    private javax.swing.JButton btn_ngungApDung;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboHinhThucGG;
    private javax.swing.JComboBox<String> cboLocHtgg;
    private javax.swing.JComboBox<String> cboLocdgg;
    private javax.swing.JComboBox<String> cbo_loaiGiamGia;
    private javax.swing.JComboBox<String> cbo_maDotGiamGia;
    private com.toedter.calendar.JDateChooser dtBatDau;
    private com.toedter.calendar.JDateChooser dtKetThuc;
    private com.toedter.calendar.JDateChooser dt_BatDau;
    private com.toedter.calendar.JDateChooser dt_ketThuc;
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
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rdoHD;
    private javax.swing.JRadioButton rdoKHD;
    private javax.swing.JRadioButton rdo_hoatDong;
    private javax.swing.JRadioButton rdo_ngunghd;
    private javax.swing.JTable tblChiTietKM;
    private javax.swing.JTable tblKM;
    private javax.swing.JTable tbldgg;
    private javax.swing.JTextField txtDK;
    private javax.swing.JTextField txtGiaGiamCT;
    private javax.swing.JTextField txtGiaGocCT;
    private javax.swing.JTextField txtGiaTri;
    private javax.swing.JTextField txtGiaTrigg;
    private javax.swing.JTextField txtKTCT;
    private javax.swing.JTextField txtMaCT;
    private javax.swing.JTextField txtMaDotCT;
    private javax.swing.JTextField txtMaPhieu;
    private javax.swing.JTextField txtMadot;
    private javax.swing.JTextField txtMauSacCT;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSLCT;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenPhieu;
    private javax.swing.JTextField txtTenSPCT;
    private javax.swing.JTextField txtTendot;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txt_timkiem;
    // End of variables declaration//GEN-END:variables

    @Override
    public void open() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setForm(DotGiamGia entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public DotGiamGia getForm() {
        DotGiamGia dgg = new DotGiamGia();

        dgg.setMaDotGG(txtMadot.getText());
        dgg.setTenDotGG(txtTendot.getText());
        dgg.setLoaiGiamGia(cbo_loaiGiamGia.getSelectedItem().toString());

        // Validate giá trị giảm
        try {
            BigDecimal giaTri = new BigDecimal(txtGiaTrigg.getText());
            if (giaTri.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập giá trị giảm lớn hơn 0!");
                return null;
            }
            dgg.setGiaTri(giaTri);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị giảm không hợp lệ!");
            return null;
        }

        // Validate ngày
        if (dt_BatDau.getDate() == null || dt_ketThuc.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và kết thúc!");
            return null;
        }
        dgg.setNgayBatDau(new java.sql.Date(dt_BatDau.getDate().getTime()));
        dgg.setNgayKetThuc(new java.sql.Date(dt_ketThuc.getDate().getTime()));

        // Trạng thái
        if (rdo_hoatDong.isSelected()) {
            dgg.setTrangThai(true);
        } else if (rdo_ngunghd.isSelected()) {
            dgg.setTrangThai(false);
        }

        dgg.setMoTa(txtMoTa.getText());

        return dgg;
    }

    @Override
    public void fillToTable() {
        mol = (DefaultTableModel) tbldgg.getModel();
        mol.setRowCount(0);

        for (DotGiamGia x : repodgg.findAll()) {
            String TrangThai;
            if (x.isTrangThai()) {
                TrangThai = "Đang hoạt động";
            } else {
                TrangThai = "Ngừng hoạt động";
            }
            Object[] data = new Object[]{
                x.getMaDotGG(), x.getTenDotGG(), x.getLoaiGiamGia(), x.getGiaTri(), x.getNgayBatDau(),
                x.getNgayKetThuc(), x.getMoTa(), TrangThai
            };
            mol.addRow(data);
        }
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void edit() {
        // Kiểm tra index hợp lệ
        if (index < 0 || index >= tbldgg.getRowCount()) {
            return;
        }

        // Lấy dữ liệu an toàn
        //safeString tránh lỗi
        txtMadot.setText((tbldgg.getValueAt(index, 0)).toString());
        txtTendot.setText((tbldgg.getValueAt(index, 1)).toString());

        cbo_loaiGiamGia.setSelectedItem(tbldgg.getValueAt(index, 2).toString());

        // Giá trị: loại bỏ format tiền
        txtGiaTrigg.setText(tbldgg.getValueAt(index, 3).toString());
//                .replace(" đồng", "").replace(".", "").trim());

        // Ngày bắt đầu
        Object ngayBD = tbldgg.getValueAt(index, 4);
        if (ngayBD instanceof java.util.Date) {
            dt_BatDau.setDate((java.util.Date) ngayBD);
        } else {
            dt_BatDau.setDate(null);
        }

        // Ngày kết thúc
        Object ngayKT = tbldgg.getValueAt(index, 5);
        if (ngayKT instanceof java.util.Date) {
            dt_ketThuc.setDate((java.util.Date) ngayKT);
        } else {
            dt_ketThuc.setDate(null);
        }

        // Mô tả
        txtMoTa.setText(tbldgg.getValueAt(index, 6).toString());

        // Trạng thái
        String trangThai = (String) tbldgg.getValueAt(index, 7);
        if (trangThai.equalsIgnoreCase("Đang hoạt động")) {
            rdo_hoatDong.setSelected(true);
        } else {
            rdo_ngunghd.setSelected(true);
        }
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void create() {
        DotGiamGia dgg = this.getForm(); // lấy dữ liệu từ form
        repodgg.create(dgg);              // gọi DAO để insert vào DB

        // Thông báo thành công
        JOptionPane.showMessageDialog(this, "Thêm đợt giảm giá thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

        this.fillToTable();
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update() {
        DotGiamGia dgg = this.getForm(); // lấy dữ liệu từ form
        repodgg.update(dgg);              // gọi DAO để update vào DB

        // Thông báo thành công
        JOptionPane.showMessageDialog(this, "Cập nhật đợt giảm giá thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

        this.fillToTable();
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete() {
        int id_delete = tbldgg.getSelectedRow();
        String id = (String) tbldgg.getValueAt(id_delete, 0);
        repodgg.deleteById(id);
        this.fillToTable();
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setEditable(boolean editable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void checkAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void uncheckAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteCheckedItems() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void moveFirst() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void movePrevious() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void moveNext() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void moveLast() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void moveTo(int rowIndex) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void edit_locDgg() {
        String maDotGG = (String) cboLocdgg.getSelectedItem();

        // Nếu chưa chọn gì → full
        if (maDotGG == null || maDotGG.equals("Mời chọn đợt giảm giá")) {
            fillToTable();
            return;
        }
        mol = (DefaultTableModel) tbldgg.getModel();
        mol.setRowCount(0);
        for (DotGiamGia x : repodgg.findMaDot(maDotGG)) {

            // Map trạng thái
            String TrangThai = x.isTrangThai() ? "Đang hoạt động" : "Ngừng hoạt động";

            Object[] data = new Object[]{
                x.getMaDotGG(),
                x.getTenDotGG(),
                x.getLoaiGiamGia(),
                x.getGiaTri(),
                x.getNgayBatDau(),
                x.getNgayKetThuc(),
                x.getMoTa(),
                TrangThai
            };
            mol.addRow(data);
        }
    }

    public void loadCboDotGiamGia() {
        cboLocdgg.removeAllItems();
        cboLocdgg.addItem("Mời chọn đợt giảm giá");

        for (DotGiamGia x : repodgg.findTrangThai()) {
            if (x.isTrangThai()) {               // chỉ show đang hoạt động
                cboLocdgg.addItem(x.getMaDotGG());
            }
        }
    }

    public void fillToTable_tendot(String name) {
        mol = (DefaultTableModel) tbldgg.getModel();
        mol.setRowCount(0);

        for (DotGiamGia x : repodgg.findByName(name)) {
            String TrangThai;
            if (x.isTrangThai()) {
                TrangThai = "Đang hoạt động";
            } else {
                TrangThai = "Ngừng hoạt động";
            }
            Object[] data = new Object[]{
                x.getMaDotGG(), x.getTenDotGG(), x.getLoaiGiamGia(), x.getGiaTri(), x.getNgayBatDau(),
                x.getNgayKetThuc(), x.getMoTa(), TrangThai
            };
            mol.addRow(data);
        }
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean checkForm() {
        // 1. Kiểm tra mã đợt giảm giá
        String maDot = txtMadot.getText().trim();
        if (maDot.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã đợt giảm giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMadot.requestFocus();
            return false;
        }
        if (repodgg.isMaDotTonTai(maDot)) {
            JOptionPane.showMessageDialog(this, "Mã đợt giảm giá đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMadot.requestFocus();
            return false;
        }
        // 2. Kiểm tra tên đợt giảm giá
        String tenDot = txtTendot.getText().trim();
        if (tenDot.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đợt giảm giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTendot.requestFocus();
            return false;
        }
        // 3. Kiểm tra giá trị giảm hợp lệ
        String giaTriStr = txtGiaTrigg.getText().trim();
        if (giaTriStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá trị giảm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaTrigg.requestFocus();
            return false;
        }
        BigDecimal giaTri;
        try {
            giaTri = new BigDecimal(giaTriStr);
            if (giaTri.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Giá trị giảm phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtGiaTrigg.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị giảm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaTrigg.requestFocus();
            return false;
        }
        // 4. Kiểm tra ngày bắt đầu/kết thúc
        if (dt_BatDau.getDate() == null || dt_ketThuc.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đủ ngày bắt đầu và kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dt_BatDau.getDate().after(dt_ketThuc.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // 5. Kiểm tra trạng thái radio
        if (!rdo_hoatDong.isSelected() && !rdo_ngunghd.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true; // Tất cả kiểm tra hợp lệ
    }

    private boolean validateFormForUpdate(String currentMaDot) {
        // Kiểm tra mã đợt giảm giá
        String maDot = txtMadot.getText().trim();
        if (maDot.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã đợt giảm giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMadot.requestFocus();
            return false;
        }

        // Kiểm tra tên đợt giảm giá
        String tenDot = txtTendot.getText().trim();
        if (tenDot.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đợt giảm giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTendot.requestFocus();
            return false;
        }
        // Kiểm tra giá trị giảm
        String giaTriStr = txtGiaTrigg.getText().trim();
        if (giaTriStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá trị giảm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaTrigg.requestFocus();
            return false;
        }
        BigDecimal giaTri;
        try {
            giaTri = new BigDecimal(giaTriStr);
            if (giaTri.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Giá trị giảm phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtGiaTrigg.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị giảm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaTrigg.requestFocus();
            return false;
        }
        // Kiểm tra ngày
        if (dt_BatDau.getDate() == null || dt_ketThuc.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đủ ngày bắt đầu và kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dt_BatDau.getDate().after(dt_ketThuc.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Kiểm tra trạng thái
        if (!rdo_hoatDong.isSelected() && !rdo_ngunghd.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void fillToTable_CTSP2() {
        mol = (DefaultTableModel) tblChiTietKM.getModel();
        mol.setRowCount(0);

        for (ChiTietSanPham2 x : repoCT.findAll()) {
            Object[] data = new Object[]{
                (x.getGiaSauGiam() != null && x.getGiaSauGiam().compareTo(x.getGiaGoc()) != 0)
                ? x.getMaDotGG() : "", // chỉ hiện mã đợt nếu giá giảm khác giá gốc
                x.getMaChiTietSP(),
                x.getTenSanPham(),
                x.getTenMau(),
                x.getKichThuoc(),
                x.getSoLuong(),
                x.getGiaGoc(),
                (x.getGiaSauGiam() != null) ? x.getGiaSauGiam() : x.getGiaGoc()
            };
            mol.addRow(data);
        }
    }

    public void loadCboDotGiamGiaCT() {
        cbo_maDotGiamGia.removeAllItems();
        cbo_maDotGiamGia.addItem("Mời chọn đợt giảm giá");

        for (DotGiamGia x : repodgg.findTrangThai()) {
            if (x.isTrangThai()) {               // chỉ show đang hoạt động
                cbo_maDotGiamGia.addItem(x.getMaDotGG());
            }
        }
    }

    public void edit_ct() {
        String maDotGG = (String) cbo_maDotGiamGia.getSelectedItem();

        // Nếu chưa chọn gì → hiển thị tất cả sản phẩm
        if (maDotGG == null || maDotGG.equals("Mời chọn đợt giảm giá")) {
            fillToTable_CTSP2();
            return;
        }

    }

    public void editCT(int index) {
        txtMaDotCT.setText(tblChiTietKM.getValueAt(index, 0).toString());
        txtMaCT.setText(tblChiTietKM.getValueAt(index, 1).toString());
        txtTenSPCT.setText(tblChiTietKM.getValueAt(index, 2).toString());
        txtMauSacCT.setText(tblChiTietKM.getValueAt(index, 3).toString());
        txtKTCT.setText(tblChiTietKM.getValueAt(index, 4).toString());
        txtSLCT.setText(tblChiTietKM.getValueAt(index, 5).toString());
        txtGiaGocCT.setText(tblChiTietKM.getValueAt(index, 6).toString());
        txtGiaGiamCT.setText(tblChiTietKM.getValueAt(index, 7).toString());
    }

    public void fillToTable_madot(String maDot) {
        mol = (DefaultTableModel) tblChiTietKM.getModel();
        mol.setRowCount(0);

        for (ChiTietSanPham2 ct : repoCT.findMaDot(maDot)) {
            mol.addRow(new Object[]{
                ct.getMaDotGG(),
                ct.getMaChiTietSP(),
                ct.getTenSanPham(),
                ct.getTenMau(),
                ct.getKichThuoc(),
                ct.getSoLuong(),
                ct.getGiaGoc(),
                ct.getGiaSauGiam()
            });
        }
    }

    public void clearCT() {
        txtMaDotCT.setText("");
        txtMaCT.setText("");
        txtTenSPCT.setText("");
        txtMauSacCT.setText("");
        txtKTCT.setText("");
        txtSLCT.setText("");
        txtGiaGocCT.setText("");
        txtGiaGiamCT.setText("");
    }
}
