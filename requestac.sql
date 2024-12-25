-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost:3306
-- Thời gian đã tạo: Th12 25, 2024 lúc 06:09 PM
-- Phiên bản máy phục vụ: 5.7.44
-- Phiên bản PHP: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `requestac`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `douutien`
--

CREATE TABLE `douutien` (
  `madouutien` int(11) NOT NULL,
  `tendouutien` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `douutien`
--

INSERT INTO `douutien` (`madouutien`, `tendouutien`) VALUES
(1, 'Critical'),
(2, 'High'),
(3, 'Medium'),
(4, 'Low');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `username` varchar(255) NOT NULL,
  `hinhanh` varchar(255) DEFAULT NULL,
  `hoten` varchar(255) DEFAULT NULL,
  `kichhoat` bit(1) NOT NULL,
  `loaitaikhoan` int(11) DEFAULT NULL,
  `ngaysinh` date DEFAULT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`username`, `hinhanh`, `hoten`, `kichhoat`, `loaitaikhoan`, `ngaysinh`, `password`) VALUES
('admin', NULL, 'Admin User', b'1', 0, '2024-12-25', '$2a$10$RRRmH3r8NGfjZXaQ6dGyfOyEMkumPxLMsyJ3j7S4lndIsoZbY.RSK'),
('admin1', 'images/avatar-anh-meo-cute-1.jpg', 'Liam', b'1', 2, '2001-08-09', '$2a$10$135YrhZCRHISf..6/6bUiuAl8rxjyxy02F3QEfHF280zsud.oVndG'),
('Employee', NULL, 'Liam', b'1', 2, '2001-08-09', '$2a$10$5nzPSbL.aJnDPjoGkyJUcOG5wY9Bqdk1yb5q4FbcE2YtHLfGRFhYW'),
('supost', 'images/avatar-anh-meo-cute-1.jpg', 'Liam', b'1', 1, '2001-08-09', '$2a$10$NoxjmsewJqCQNppnNp/xheRNNDzfuY7BQLdZ.3SlvTqp75QcqMYg2');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `yeucau`
--

CREATE TABLE `yeucau` (
  `mayeucau` int(11) NOT NULL,
  `ngaygui` date DEFAULT NULL,
  `noidung` varchar(255) DEFAULT NULL,
  `tieude` varchar(255) NOT NULL,
  `madouutien` int(11) DEFAULT NULL,
  `manv_gui` varchar(255) DEFAULT NULL,
  `manv_xuly` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `yeucau`
--

INSERT INTO `yeucau` (`mayeucau`, `ngaygui`, `noidung`, `tieude`, `madouutien`, `manv_gui`, `manv_xuly`) VALUES
(5, '2024-12-25', 'nghiêm trọng', 'nghiêm trọng', 1, 'admin1', 'supost'),
(6, '2024-12-25', 'yêu cầu 1', 'yêu cầu 1', 2, 'admin1', NULL),
(7, '2024-12-25', 'yêu cầu 2', 'yêu cầu 2', 4, 'admin1', NULL),
(8, '2024-12-25', 'yêu cầu 3', 'yêu cầu 3', 1, 'admin1', NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `douutien`
--
ALTER TABLE `douutien`
  ADD PRIMARY KEY (`madouutien`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`username`);

--
-- Chỉ mục cho bảng `yeucau`
--
ALTER TABLE `yeucau`
  ADD PRIMARY KEY (`mayeucau`),
  ADD KEY `FKqb3tn4wrdaiytmn4b8r6ql4a7` (`madouutien`),
  ADD KEY `FKh0wy7wjwf52l1skp0rv47xwf9` (`manv_gui`),
  ADD KEY `FKouixxxkducxucdtsptbwwpjxl` (`manv_xuly`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `douutien`
--
ALTER TABLE `douutien`
  MODIFY `madouutien` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `yeucau`
--
ALTER TABLE `yeucau`
  MODIFY `mayeucau` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `yeucau`
--
ALTER TABLE `yeucau`
  ADD CONSTRAINT `FKh0wy7wjwf52l1skp0rv47xwf9` FOREIGN KEY (`manv_gui`) REFERENCES `nhanvien` (`username`),
  ADD CONSTRAINT `FKouixxxkducxucdtsptbwwpjxl` FOREIGN KEY (`manv_xuly`) REFERENCES `nhanvien` (`username`),
  ADD CONSTRAINT `FKqb3tn4wrdaiytmn4b8r6ql4a7` FOREIGN KEY (`madouutien`) REFERENCES `douutien` (`madouutien`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
