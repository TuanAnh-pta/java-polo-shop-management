package ui;

import dao.PhieuGiamGiaDAO;
import entity.PhieuGiamGia;
import java.math.BigDecimal;
import static java.nio.file.Files.list;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

public class KhuyenMaiView extends javax.swing.JInternalFrame {

    private PhieuGiamGiaDAO phieuGiamGiaDAO;
    private PhieuGiamGiaDAO daoPGG = new PhieuGiamGiaDAO();
    private List<PhieuGiamGia> listPGG;
    private DefaultTableModel defaultTableModel;
    private int selectedId = -1;

    public KhuyenMaiView() {
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
        fillComboLocHtgg(); 
//        fillTable(phieuGiamGiaDAO.getAll());
        fillTable(phieuGiamGiaDAO.getAll());

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

            String giaTriStr = nf.format(p.getGiaTri()) + " đồng";
            String dieuKienStr = nf.format(p.getDieuKienApDung()) + " đồng";

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
    if (text == null) return BigDecimal.ZERO;

    // Bỏ hết ký tự không phải số hoặc dấu phẩy
    String cleaned = text.replaceAll("[^0-9,]", "")
                         .replace(",", ".")
                         .trim();

    if (cleaned.isEmpty()) return BigDecimal.ZERO;

    return new BigDecimal(cleaned);
}

private BigDecimal parseCurrency(String text) {
    if (text == null) return BigDecimal.ZERO;

    // Bỏ chữ, khoảng trắng và ký tự không phải số hoặc dấu phẩy, chấm
    String cleaned = text.replaceAll("[^0-9,\\.]", "")
                         .replace(",", ".")
                         .trim();

    if (cleaned.isEmpty()) return BigDecimal.ZERO;

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
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jLabel15.setText("Tìm kiếm: ");

        cboLocHtgg.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 983, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoHDActionPerformed

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

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (!validateForm()) {
            return;
        }
        phieuGiamGiaDAO.update(readFromForm());
        clearForm();
        JOptionPane.showMessageDialog(this, "Sửa thành công");
        fillTable(phieuGiamGiaDAO.getAll());
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
    int selectedRow = tblKM.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
        return;
    }

    String maPhieu = tblKM.getValueAt(selectedRow, 0).toString();

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xoá phiếu \"" + maPhieu + "\"?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        phieuGiamGiaDAO.deleteByMaPhieu(maPhieu); // bạn cần viết hàm này trong DAO
        clearForm();
        JOptionPane.showMessageDialog(this, "Xóa thành công");
        fillTable(phieuGiamGiaDAO.getAll());
    }
    }//GEN-LAST:event_btnXoaActionPerformed

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

    private void cboHinhThucGGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHinhThucGGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboHinhThucGGActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        String keyword = txtTimKiem.getText().trim().toLowerCase();
    String dkStr = cboLocHtgg.getSelectedItem() != null ? cboLocHtgg.getSelectedItem().toString() : "Tất cả";
    filterTable(dkStr, keyword);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void cboLocHtggActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocHtggActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLocHtggActionPerformed

    private void txtDKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDKActionPerformed

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
                new KhuyenMaiView().pack();
                new KhuyenMaiView().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboHinhThucGG;
    private javax.swing.JComboBox<String> cboLocHtgg;
    private com.toedter.calendar.JDateChooser dtBatDau;
    private com.toedter.calendar.JDateChooser dtKetThuc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdoHD;
    private javax.swing.JRadioButton rdoKHD;
    private javax.swing.JTable tblKM;
    private javax.swing.JTextField txtDK;
    private javax.swing.JTextField txtGiaTri;
    private javax.swing.JTextField txtMaPhieu;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenPhieu;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
