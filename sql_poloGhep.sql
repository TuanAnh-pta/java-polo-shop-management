
---------------------------------------------------------------
-- 0) CREATE DATABASE
---------------------------------------------------------------
CREATE DATABASE QLBanAoPoLo_01_12;
GO
USE QLBanAoPoLo_01_12;
GO


/* ============================================================
   1) BASE TABLES
   (No FK dependencies)
   ============================================================ */

-- Xuất xứ
CREATE TABLE XuatSu (
    MaXuatSu NVARCHAR(7) PRIMARY KEY,
    NoiNhap NVARCHAR(100) NOT NULL
);

-- Chức vụ
CREATE TABLE ChucVu (
    maChucVu NVARCHAR(20) PRIMARY KEY,
    tenChucVu NVARCHAR(100) NOT NULL,
    moTa NVARCHAR(MAX),
    trangThai BIT DEFAULT 0
);

-- Màu sắc
CREATE TABLE MauSac (
    MaMauSac NVARCHAR(7) PRIMARY KEY,
    TenMau NVARCHAR(50) NOT NULL
);

-- Kích thước
CREATE TABLE KichThuoc (
    MaKichThuoc NVARCHAR(7) PRIMARY KEY,
    KichThuoc NVARCHAR(50) NOT NULL
);

-- Chất liệu
CREATE TABLE ChatLieu (
    MaChatLieu NVARCHAR(7) PRIMARY KEY,
    ChatLieu NVARCHAR(100) NOT NULL
);


/* ============================================================
   2) TABLES WITH FK (PHASE 2)
   ============================================================ */

-- Sản phẩm
CREATE TABLE SanPham (
    MaSanPham NVARCHAR(7) PRIMARY KEY,
    TenSanPham NVARCHAR(100) NOT NULL,
    MoTa NVARCHAR(255),
    MaChatLieu NVARCHAR(7) NOT NULL,
    MaXuatSu NVARCHAR(7),
    TrangThai BIT NOT NULL DEFAULT 1,

    CONSTRAINT FK_SanPham_ChatLieu FOREIGN KEY (MaChatLieu) REFERENCES ChatLieu(MaChatLieu),
    CONSTRAINT FK_SanPham_XuatSu FOREIGN KEY (MaXuatSu) REFERENCES XuatSu(MaXuatSu)
);

-- Chi tiết sản phẩm
CREATE TABLE ChiTietSanPham (
    MaChiTietSP NVARCHAR(7) PRIMARY KEY,
    SoLuong INT NOT NULL,
    Gia DECIMAL(18,2) NOT NULL,
    GiaSauGiam DECIMAL(18,2),
    MaSanPham NVARCHAR(7) NOT NULL,
    MaMauSac NVARCHAR(7) NOT NULL,
    MaKichThuoc NVARCHAR(7) NOT NULL,

    CONSTRAINT FK_CTSP_SanPham FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham),
    CONSTRAINT FK_CTSP_MauSac FOREIGN KEY (MaMauSac) REFERENCES MauSac(MaMauSac),
    CONSTRAINT FK_CTSP_KichThuoc FOREIGN KEY (MaKichThuoc) REFERENCES KichThuoc(MaKichThuoc)
);

-- Khách hàng
CREATE TABLE KhachHang (
    MaKhachHang NVARCHAR(7) PRIMARY KEY,
    TenKhachHang NVARCHAR(100) NOT NULL,
    SoDienThoai NVARCHAR(20),
    Email NVARCHAR(100),
    DiaChi NVARCHAR(255),
    TrangThai BIT NOT NULL DEFAULT 1,
    GioiTinh NVARCHAR(3) NOT NULL DEFAULT N'Nam'
);

-- Nhân viên
CREATE TABLE NhanVien (
    MaNhanVien NVARCHAR(7) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    gioiTinh NVARCHAR(10),
    soDienThoai NVARCHAR(20),
    email NVARCHAR(100) UNIQUE NOT NULL,
    maChucVu NVARCHAR(20),
    trangThai BIT DEFAULT 0,

    FOREIGN KEY (maChucVu) REFERENCES ChucVu(maChucVu)
);

-- Phiếu giảm giá
CREATE TABLE PhieuGiamGia (
    maPhieu NVARCHAR(20) PRIMARY KEY,
    tenPhieu NVARCHAR(100),
    tenHinhThucGG NVARCHAR(100),
    giaTri DECIMAL(18,2) NOT NULL,
    soLuong INT NOT NULL,
    ngayBatDau DATE,
    ngayKetThuc DATE,
    dieuKienApDung DECIMAL(18,2),
    trangThai BIT DEFAULT 0
);

-- Hóa đơn
CREATE TABLE HoaDon (
    MaHoaDon NVARCHAR(7) PRIMARY KEY,
    MaKhachHang NVARCHAR(7) NOT NULL,
    MaNhanVien NVARCHAR(7) NOT NULL,
    TongTien DECIMAL(18,2) NOT NULL,
    TrangThai BIT NOT NULL CHECK (TrangThai IN (0,1)),
    SoDienThoai NVARCHAR(20),
    Email NVARCHAR(100),
    TenKhachHang NVARCHAR(100),
    NgayTao DATE NOT NULL,
    MaPhieu NVARCHAR(20),
    giaTri DECIMAL(18,2),
    TienSauGiam DECIMAL(18,2),

    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang),
    FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien),
    FOREIGN KEY (MaPhieu) REFERENCES PhieuGiamGia(maPhieu)
);

-- Chi tiết hóa đơn
CREATE TABLE ChiTietHoaDon (
    MaChiTietHoaDon NVARCHAR(50) PRIMARY KEY,
    SoLuong INT NOT NULL,
    DonGia DECIMAL(18,2),
    MaHoaDon NVARCHAR(7) NOT NULL,
    MaChiTietSP NVARCHAR(7) NOT NULL,

    FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon),
    FOREIGN KEY (MaChiTietSP) REFERENCES ChiTietSanPham(MaChiTietSP)
);

-- Tài khoản đăng nhập
CREATE TABLE DangNhap (
    TaiKhoan NVARCHAR(50) PRIMARY KEY,
    MatKhau NVARCHAR(50) NOT NULL,
    MaNhanVien NVARCHAR(7) NOT NULL,
    maChucVu NVARCHAR(20),
    TrangThai BIT NOT NULL DEFAULT 1,

    FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien),
    FOREIGN KEY (maChucVu) REFERENCES ChucVu(maChucVu)
);

select * from DangNhap

/* ============================================================
   3) TABLES FOR PRODUCT DISCOUNT MODULE
   ============================================================ */

-- Đợt giảm giá
CREATE TABLE DotGiamGia (
    MaDotGG NVARCHAR(20) PRIMARY KEY,
    TenDotGG NVARCHAR(100) NOT NULL,
    LoaiGiamGia NVARCHAR(20) NOT NULL,
    GiaTri DECIMAL(18,2) NOT NULL,
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    TrangThai BIT NOT NULL DEFAULT 1,
    MoTa NVARCHAR(MAX)
);
select * from DotGiamGia;

-- Gán sản phẩm vào từng đợt giảm giá
CREATE TABLE DotGiamGia_ChiTietSanPham (
    ID INT IDENTITY PRIMARY KEY,
    MaDotGG NVARCHAR(20) NOT NULL,
    MaChiTietSP NVARCHAR(7) NOT NULL,

    FOREIGN KEY (MaDotGG) REFERENCES DotGiamGia(MaDotGG),
    FOREIGN KEY (MaChiTietSP) REFERENCES ChiTietSanPham(MaChiTietSP)
);

SELECT MaChiTietSP FROM ChiTietSanPham;

select * from DotGiamGia_ChiTietSanPham;
/* ============================================================
   4) INSERT DATA
   (Giữ nguyên dữ liệu bạn cung cấp)
   ============================================================ */

   -- ===========================
-- Đợt giảm giá
-- ===========================
INSERT INTO DotGiamGia
    (MaDotGG, TenDotGG, LoaiGiamGia, GiaTri, NgayBatDau, NgayKetThuc, TrangThai, MoTa)
VALUES
    ('GG001', N'Giảm 20% Tháng 12', N'PhanTram', 20, '2025-12-01', '2025-12-31', 1, N'Áp dụng cho nhóm sản phẩm Polo HOT'),
    ('GG002', N'Giảm 50.000 cho đơn hàng Polo', N'TienMat', 50000, '2025-12-05', '2025-12-25', 1, N'Khuyến mãi Noel'),
    ('GG003', N'Xả hàng cuối năm 30%', N'PhanTram', 30, '2025-12-20', '2026-01-10', 1, N'Áp dụng cho các mẫu tồn kho'),
    ('GG004', N'Clearance 100.000', N'TienMat', 100000, '2025-12-01', '2026-02-01', 1, N'Thanh lý 1 số mẫu cũ');

INSERT INTO DotGiamGia_ChiTietSanPham (MaDotGG, MaChiTietSP)
VALUES
    ('GG001', 'CT00001'),
    ('GG001', 'CT00002'),
    ('GG001', 'CT00003');

INSERT INTO DotGiamGia_ChiTietSanPham (MaDotGG, MaChiTietSP)
VALUES
    ('GG002', 'CT00003'),
    ('GG002', 'CT00004');
INSERT INTO DotGiamGia_ChiTietSanPham (MaDotGG, MaChiTietSP)
VALUES
    ('GG003', 'CT00004'),
    ('GG003', 'CT00005');

INSERT INTO XuatSu VALUES
('XS00001', N'Việt Nam'),
('XS00002', N'Thái Lan'),
('XS00003', N'Trung Quốc'),
('XS00004', N'Nhật Bản'),
('XS00005', N'Hàn Quốc');

INSERT INTO ChucVu (maChucVu, tenChucVu, moTa) VALUES
('CV001', N'Quản lý', N'Người quản lý cửa hàng'),
('CV002', N'Nhân viên', N'Nhân viên bán hàng');

INSERT INTO ChatLieu VALUES
('CL00001', N'Cotton'),
('CL00002', N'Polyester'),
('CL00003', N'Kaki'),
('CL00004', N'Lụa'),
('CL00005', N'Denim');

INSERT INTO SanPham VALUES
('SP00001', N'Áo Polo Nam Trơn', N'Cổ bẻ, chất liệu cotton', 'CL00001', 'XS00001', 1),
('SP00002', N'Áo Polo Nam Sọc Đen', N'Sọc đen trắng cổ điển', 'CL00002', 'XS00002', 1),
('SP00003', N'Áo Polo Nam Thể Thao', N'Thiết kế cho vận động mạnh', 'CL00003', 'XS00003', 1),
('SP00004', N'Áo Polo Nam Form Slim', N'Thiết kế body ôm sát', 'CL00004', 'XS00004', 1),
('SP00005', N'Áo Polo Nam Cổ Tàu', N'Cổ tàu phong cách Nhật Bản', 'CL00005', 'XS00005', 1);

INSERT INTO MauSac VALUES
('MS00001', N'Trắng'),
('MS00002', N'Đen'),
('MS00003', N'Xám'),
('MS00004', N'Xanh Navy'),
('MS00005', N'Đỏ Đô');

INSERT INTO KichThuoc VALUES
('KT00001', N'S'),
('KT00002', N'M'),
('KT00003', N'L'),
('KT00004', N'XL'),
('KT00005', N'XXL');

INSERT INTO ChiTietSanPham VALUES
('CT00001', 20, 300000, 270000, 'SP00001', 'MS00001', 'KT00002'),
('CT00002', 15, 320000, 288000, 'SP00002', 'MS00002', 'KT00003'),
('CT00003', 10, 350000, 315000, 'SP00003', 'MS00003', 'KT00001'),
('CT00004', 12, 280000, 252000, 'SP00004', 'MS00004', 'KT00004'),
('CT00005', 18, 260000, 234000, 'SP00005', 'MS00005', 'KT00002');

INSERT INTO KhachHang VALUES
('KH00001', N'Nguyễn Văn A', '0901234567', 'a@gmail.com', N'Hà Nội', 1, N'Nam'),
('KH00002', N'Lê Thị B', '0912345678', 'b@gmail.com', N'HCM', 1, N'Nữ'),
('KH00003', N'Trần Văn C', '0923456789', 'c@gmail.com', N'Đà Nẵng', 0, N'Nam'),
('KH00004', N'Phạm Thị D', '0934567890', 'd@gmail.com', N'Cần Thơ', 1, N'Nữ'),
('KH00005', N'Hoàng Văn E', '0945678901', 'e@gmail.com', N'Hải Phòng', 0, N'Nam');

INSERT INTO NhanVien VALUES
('NV00001', N'Nguyễn Văn Luận', N'Nam', '0901234567', 'a@gmail.com', 'CV002', 0),
('NV00002', N'Trịnh Trần Phương Tuấn', N'Nam', '0912345678', 'b@gmail.com', 'CV001', 0),
('NV00003', N'Nguyễn Văn Tuấn', N'Nam', '0923456789', 'c@gmail.com', 'CV002', 0),
('NV00004', N'Nguyễn Thị Nhung', N'Nữ', '0934567890', 'd@gmail.com', 'CV002', 0),
('NV00005', N'Hoàng Văn Nam', N'Nam', '0945678901', 'e@gmail.com', 'CV002', 0),
('NV00006', N'Dương Thị Mai Uyên', N'Nữ', '0956789012', 'f@gmail.com', 'CV002', 0);

INSERT INTO DangNhap VALUES
('nv1', '123456', 'NV00001', 'CV002', 1),
('nv2', '123456', 'NV00002', 'CV001', 1),
('nv3', '123456', 'NV00003', 'CV002', 1),
('nv4', '123456', 'NV00004', 'CV002', 1),
('nv5', '123456', 'NV00005', 'CV002', 1);

INSERT INTO PhieuGiamGia VALUES
('PGG001', N'Giảm 100k đơn từ 1 triệu', N'Giảm tiền mặt theo tổng đơn hàng', 100000, 20, '2025-05-01', '2025-06-30', 1000000, 1),
('PGG002', N'Giảm 200k đơn từ 2 triệu', N'Giảm tiền mặt theo tổng đơn hàng', 200000, 15, '2025-05-15', '2025-06-30', 2000000, 1);

INSERT INTO HoaDon VALUES
('HD00001', 'KH00001', 'NV00001', 1500000, 1, '0901234567', 'a@gmail.com', N'Nguyễn Văn A', '2025-07-17', NULL, NULL, NULL),
('HD00002', 'KH00002', 'NV00002', 1800000, 1, '0912345678', 'b@gmail.com', N'Lê Thị B', '2025-07-17', NULL, NULL, NULL),
('HD00003', 'KH00003', 'NV00003', 2000000, 1, '0923456789', 'c@gmail.com', N'Trần Văn C', '2025-07-17', NULL, NULL, NULL),
('HD00004', 'KH00004', 'NV00004', 2200000, 1, '0934567890', 'd@gmail.com', N'Phạm Thị D', '2025-07-17', NULL, NULL, NULL),
('HD00005', 'KH00005', 'NV00005', 2500000, 1, '0945678901', 'e@gmail.com', N'Hoàng Văn E', '2025-07-17', NULL, NULL, NULL);

INSERT INTO ChiTietHoaDon VALUES
('CH00001', 1, 270000, 'HD00001', 'CT00001'),
('CH00002', 1, 288000, 'HD00002', 'CT00002'),
('CH00003', 1, 315000, 'HD00003', 'CT00003'),
('CH00004', 1, 252000, 'HD00004', 'CT00004'),
('CH00005', 1, 234000, 'HD00005', 'CT00005');


/* ============================================================
   5) MORE PROCESSING (DISCOUNT LOGIC)
   ============================================================ */

-- Gán phiếu giảm giá theo tổng tiền
UPDATE HoaDon SET MaPhieu = 'PGG001' WHERE TongTien >= 1000000 AND TongTien < 2000000;
UPDATE HoaDon SET MaPhieu = 'PGG002' WHERE TongTien >= 2000000;

-- Lấy giá trị giảm
UPDATE HoaDon
SET giaTri = ISNULL(pgg.giaTri, 0)
FROM HoaDon hd
LEFT JOIN PhieuGiamGia pgg ON hd.MaPhieu = pgg.maPhieu;

---- Tính tiền sau giảm
--UPDATE HoaDon
--SET TienSauGiam = TongTien - ISNULL(giaTri, 0);

---- Đồng bộ giá SP theo hóa đơn (không khuyến cáo dùng thực tế)
--UPDATE ChiTietSanPham
--SET Gia = hd.TongTien
--FROM ChiTietSanPham ctsp
--JOIN ChiTietHoaDon cthd ON cthd.MaChiTietSP = ctsp.MaChiTietSP
--JOIN HoaDon hd ON hd.MaHoaDon = cthd.MaHoaDon;

--UPDATE ChiTietSanPham
--SET GiaSauGiam = hd.TienSauGiam
--FROM ChiTietSanPham ctsp
--JOIN ChiTietHoaDon cthd ON cthd.MaChiTietSP = ctsp.MaChiTietSP
--JOIN HoaDon hd ON hd.MaHoaDon = cthd.MaHoaDon;

UPDATE ChiTietHoaDon
SET DonGia = CTSP.GiaSauGiam
FROM ChiTietHoaDon CTHD
JOIN ChiTietSanPham CTSP ON CTHD.MaChiTietSP = CTSP.MaChiTietSP;


/* ============================================================
   6) TRIGGER
   ============================================================ */
CREATE TRIGGER trg_UpdateSanPham_TrangThai
ON ChiTietSanPham
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE sp
    SET TrangThai =
        CASE
            WHEN EXISTS (
                SELECT 1 FROM ChiTietSanPham ctsp
                WHERE ctsp.MaSanPham = sp.MaSanPham AND ctsp.SoLuong > 0
            ) THEN 1 ELSE 0 END
    FROM SanPham sp
    WHERE sp.MaSanPham IN (
        SELECT MaSanPham FROM inserted
        UNION
        SELECT MaSanPham FROM deleted
    );
END;
GO


/* ============================================================
   7) VIEWS
   ============================================================ */

CREATE VIEW vw_DoanhThuTheoNgay AS
SELECT CONVERT(DATE, NgayTao) AS NgayTao,
       SUM(TienSauGiam) AS DoanhThu
FROM HoaDon
WHERE TrangThai = 1
GROUP BY CONVERT(DATE, NgayTao);

CREATE VIEW vw_DoanhThuTheoTuan AS
SELECT DATEPART(WEEK, NgayTao) AS Tuan,
       DATEPART(YEAR, NgayTao) AS Nam,
       SUM(TienSauGiam) AS DoanhThu
FROM HoaDon
WHERE TrangThai = 1
GROUP BY DATEPART(WEEK, NgayTao), DATEPART(YEAR, NgayTao);

CREATE VIEW vw_DoanhThuTheoThang AS
SELECT MONTH(NgayTao) AS Thang,
       YEAR(NgayTao) AS Nam,
       SUM(TienSauGiam) AS DoanhThu
FROM HoaDon
WHERE TrangThai = 1
GROUP BY MONTH(NgayTao), YEAR(NgayTao);

CREATE VIEW vw_DoanhThuTheoNam AS
SELECT YEAR(NgayTao) AS Nam,
       SUM(TienSauGiam) AS DoanhThu
FROM HoaDon
WHERE TrangThai = 1
GROUP BY YEAR(NgayTao);
GO

/* ====================== DONE =========================== */

ALTER TABLE KhachHang
ALTER COLUMN GioiTinh NVARCHAR(3) NULL;

ALTER TABLE ChiTietSanPham
ADD MaDotGG NVARCHAR(20) NULL;

-- khang


-- =============================
-- KHÓA HUẤN LUYỆN / HƯỚNG DẪN
-- =============================
CREATE TABLE KhoaHuongDan (
    MaKhoaHD NVARCHAR(7) PRIMARY KEY,
    TieuDe NVARCHAR(100) NOT NULL,
    MoTa NVARCHAR(255),
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    TrangThai BIT NOT NULL DEFAULT 1 -- 1: Hoạt động, 0: Kết thúc
);

CREATE TABLE NhanVien_KhoaHD (
    MaNV NVARCHAR(7) NOT NULL,
    MaKhoaHD NVARCHAR(7) NOT NULL,
    NgayThamGia DATE NOT NULL DEFAULT GETDATE(),
    TrangThai BIT NOT NULL DEFAULT 0, -- 0: Chưa hoàn thành, 1: Hoàn thành
    PRIMARY KEY (MaNV, MaKhoaHD),
    CONSTRAINT FK_NV_KhoaHD_NV FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNhanVien),
    CONSTRAINT FK_NV_KhoaHD_KhoaHD FOREIGN KEY (MaKhoaHD) REFERENCES KhoaHuongDan(MaKhoaHD)
);

-- =============================
-- DỮ LIỆU MẪU CHO KHÓA HUẤN LUYỆN
-- =============================
INSERT INTO KhoaHuongDan VALUES
('KHD001', N'Hướng dẫn bán hàng cơ bản', N'Hướng dẫn nhân viên mới cách bán hàng', '2025-12-05', '2025-12-10', 1),
('KHD002', N'Kỹ năng chăm sóc khách hàng', N'Nâng cao kỹ năng chăm sóc khách hàng', '2025-12-12', '2025-12-15', 1),
('KHD003', N'Hướng dẫn quản lý kho', N'Hướng dẫn quản lý kho và tồn kho', '2025-12-20', '2025-12-25', 1);


CREATE TABLE LichLamViec (
    MaLLV NVARCHAR(7) PRIMARY KEY,
    MaNhanVien NVARCHAR(7) NOT NULL,
    NgayLam DATE NOT NULL,
    CaLam NVARCHAR(20) NOT NULL,
    GhiChu NVARCHAR(255) NULL,
    CONSTRAINT FK_LLV_NV FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien)
);

INSERT INTO LichLamViec (MaLLV, MaNhanVien, NgayLam, CaLam, GhiChu) VALUES
('LLV001', 'NV00001', '2025-11-25', N'Ca Sáng', N'Hoàn thành'),
('LLV002', 'NV00002', '2025-11-25', N'Ca Chiều', N'Hoàn thành'),
('LLV003', 'NV00003', '2025-11-25', N'Ca Sáng', N'Hoàn thành'),
('LLV004', 'NV00004', '2025-11-25', N'Ca Chiều', N'Hoàn thành');

select * from KhachHang;
go
delete from KhachHang where MaKhachHang = 'KH00029'