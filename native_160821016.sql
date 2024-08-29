-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 04, 2024 at 02:40 PM
-- Server version: 8.0.35-0ubuntu0.22.04.1
-- PHP Version: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `native_160821016`
--

-- --------------------------------------------------------

--
-- Table structure for table `cerbung`
--

CREATE TABLE `cerbung` (
  `idcerbung` int NOT NULL,
  `title` varchar(45) NOT NULL,
  `tanggal` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `jumlah_paragraf` int NOT NULL DEFAULT '1',
  `jumlah_like` int NOT NULL DEFAULT '0',
  `description` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `access` enum('Public','Restricted') NOT NULL,
  `url` varchar(1000) NOT NULL,
  `username` varchar(45) NOT NULL,
  `idgenre` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `cerbung`
--

INSERT INTO `cerbung` (`idcerbung`, `title`, `tanggal`, `jumlah_paragraf`, `jumlah_like`, `description`, `access`, `url`, `username`, `idgenre`) VALUES
(1, 'Rahasia Terkunci di Perpustakaan Kuno', '2023-12-13 10:39:55', 1, 2, '\"Rahasia Terkunci di Perpustakaan Kuno\" mengisahkan tentang seorang mahasiswa bernama Alex yang secara tak sengaja menemukan sebuah buku kuno yang misterius di perpustakaan universitasnya.', 'Public', 'https://asset.kompas.com/crops/fPl5r1G3KXbskdrjrJxk1InCebc=/429x39:5529x3439/780x390/data/photo/2021/05/10/609931bb5334c.jpg', 'Michelle', 12),
(4, 'The Tales of the Aquaman', '2023-12-14 13:27:02', 1, 2, '\"The Tales of Aquaman\" is a collection of stories that explore the adventures of Aquaman, a superhero from the DC Comics universe. ', 'Restricted', 'https://m.media-amazon.com/images/M/MV5BOTk5ODg0OTU5M15BMl5BanBnXkFtZTgwMDQ3MDY3NjM@._V1_.jpg', 'ironman', 13),
(12, 'Mengejar Mimpi', '2024-01-04 13:27:24', 1, 0, 'Kisah tentang ambisi dan ketekunan, berpusat pada seorang protagonis yang dengan gigih mengejar impiannya meskipun dihadapkan pada berbagai tantangan dan hambatan sepanjang perjalanan.', 'Public', 'https://static.promediateknologi.id/crop/0x0:0x0/750x500/webp/photo/2023/06/15/hipwee-yeyye-1-3702379044.jpg', 'levi', 5),
(14, 'test', '2024-01-04 14:07:02', 1, 0, 'test\nt', 'Public', 'https://media.istockphoto.com/id/825778252/id/foto/latar-belakang-langit-biru-dan-awan-putih.jpg?b=1&s=612x612&w=0&k=20&c=rPQfJUh4Lq2aExFLkCpgrrZ8tPl6h1chwKQI2Bdv6A0=', 'ironman', 1),
(15, 'jefbe', '2024-01-04 14:08:39', 1, 0, 'ejfbsjvgb', 'Restricted', 'https://e0.pxfuel.com/wallpapers/391/34/desktop-wallpaper-pemandangan-cute-landscape-thumbnail.jpg', 'ironman', 1),
(16, 'ahvdjf', '2024-01-04 14:12:29', 1, 0, 'ajufbakijfb', 'Restricted', 'https://png.pngtree.com/thumb_back/fh260/background/20230519/pngtree-landscape-wallpaper-image_2569701.jpg', 'superman123', 1);

-- --------------------------------------------------------

--
-- Table structure for table `genre`
--

CREATE TABLE `genre` (
  `idgenre` int NOT NULL,
  `name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `genre`
--

INSERT INTO `genre` (`idgenre`, `name`) VALUES
(1, 'Action'),
(2, 'Anime'),
(3, 'Comedy'),
(4, 'Crime'),
(5, 'Drama'),
(6, 'History'),
(7, 'Horror'),
(8, 'Kids'),
(9, 'Romance'),
(10, 'Sci-Fi & Fantasy'),
(11, 'Thriller'),
(12, 'Mystery'),
(13, 'Adventure'),
(14, 'Documentary');

-- --------------------------------------------------------

--
-- Table structure for table `paragraf`
--

CREATE TABLE `paragraf` (
  `idparagraf` int NOT NULL,
  `paragraf` varchar(100) NOT NULL,
  `idcerbung` int NOT NULL,
  `username` varchar(45) NOT NULL,
  `tanggal` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `paragraf`
--

INSERT INTO `paragraf` (`idparagraf`, `paragraf`, `idcerbung`, `username`, `tanggal`) VALUES
(1, 'Dengan hati berdebar, Alex mengelilingi toko antik tua itu. Matanya tertarik pada cermin kuno itu.', 1, 'Michelle', '2023-12-14 21:14:29'),
(2, 'These tales typically revolve around his efforts to protect the oceans and maintain peace.', 4, 'ironman', '2023-12-14 21:14:29'),
(3, 'Pada sebuah kota kecil yang dikelilingi oleh bukit-bukit hijau,', 1, 'levi', '2023-12-16 01:09:26'),
(5, 'Terdapat sebuah perpustakaan tua yang berdiri selama berabad-abad.', 1, 'ironman', '2023-12-16 01:15:39'),
(68, 'Ini adalah cerita tentang tekad, ketahanan, dan kekuatan diri', 12, 'levi', '2024-01-04 13:27:24'),
(72, 'test', 14, 'ironman', '2024-01-04 14:07:03'),
(73, 'jtgnkej', 15, 'ironman', '2024-01-04 14:08:39'),
(74, 'ksuifheugt', 16, 'superman123', '2024-01-04 14:12:29');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(45) NOT NULL,
  `url` varchar(1000) NOT NULL,
  `password` varchar(45) NOT NULL,
  `confirm_password` varchar(45) NOT NULL,
  `tanggal` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `theme` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `url`, `password`, `confirm_password`, `tanggal`, `theme`) VALUES
('ironman', 'https://images.augustman.com/wp-content/uploads/sites/6/2023/06/29134648/iron-man-f.jpg', 'ironman', 'ironman', '2023-12-12 18:00:48', 0),
('levi', 'https://pm1.aminoapps.com/7524/9ef2175a5e0f211047e74132e716043ff949d1b2r1-736-1041v2_hq.jpg', 'levi', 'levi', '2023-12-15 18:00:48', 0),
('Michelle', 'https://wallpapercosmos.com/w/full/c/2/b/191966-2160x3840-samsung-4k-nebula-marvel-wallpaper-photo.jpg', 'mich', 'mich', '2023-12-11 18:00:48', 0),
('superman123', 'https://cdn1-production-images-kly.akamaized.net/Orb3jOlDta11CdpdCP6i06fksKw=/1200x1200/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/725423/original/superman_in_batman_v_superman_dawn_of_justice-1600x900.jpg', 'superman', 'superman', '2023-12-12 18:00:48', 0);

-- --------------------------------------------------------

--
-- Table structure for table `user_follows_cerbung`
--

CREATE TABLE `user_follows_cerbung` (
  `username` varchar(45) NOT NULL,
  `idcerbung` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `user_follows_cerbung`
--

INSERT INTO `user_follows_cerbung` (`username`, `idcerbung`) VALUES
('ironman', 1),
('levi', 1),
('levi', 4);

-- --------------------------------------------------------

--
-- Table structure for table `user_likes_cerbung`
--

CREATE TABLE `user_likes_cerbung` (
  `username` varchar(45) NOT NULL,
  `idcerbung` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `user_likes_cerbung`
--

INSERT INTO `user_likes_cerbung` (`username`, `idcerbung`) VALUES
('levi', 1),
('Michelle', 1),
('levi', 4),
('Michelle', 4);

-- --------------------------------------------------------

--
-- Table structure for table `user_likes_paragraf`
--

CREATE TABLE `user_likes_paragraf` (
  `username` varchar(45) NOT NULL,
  `idparagraf` int NOT NULL,
  `idcerbung` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `user_likes_paragraf`
--

INSERT INTO `user_likes_paragraf` (`username`, `idparagraf`, `idcerbung`) VALUES
('ironman', 1, 1),
('levi', 1, 1),
('ironman', 2, 4),
('levi', 2, 4),
('ironman', 3, 1),
('levi', 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_requests_cerbung`
--

CREATE TABLE `user_requests_cerbung` (
  `username` varchar(45) NOT NULL,
  `idcerbung` int NOT NULL,
  `allow` enum('Yes','No','Pending') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'Pending',
  `tanggal` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `user_requests_cerbung`
--

INSERT INTO `user_requests_cerbung` (`username`, `idcerbung`, `allow`, `tanggal`) VALUES
('levi', 4, 'Pending', '2024-01-03 18:55:01');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cerbung`
--
ALTER TABLE `cerbung`
  ADD PRIMARY KEY (`idcerbung`,`idgenre`),
  ADD KEY `fk_cerbung_user1_idx` (`username`),
  ADD KEY `fk_cerbung_genre1_idx` (`idgenre`);

--
-- Indexes for table `genre`
--
ALTER TABLE `genre`
  ADD PRIMARY KEY (`idgenre`);

--
-- Indexes for table `paragraf`
--
ALTER TABLE `paragraf`
  ADD PRIMARY KEY (`idparagraf`),
  ADD KEY `fk_paragraf_cerbung_idx` (`idcerbung`),
  ADD KEY `fk_paragraf_user1_idx` (`username`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `user_follows_cerbung`
--
ALTER TABLE `user_follows_cerbung`
  ADD PRIMARY KEY (`username`,`idcerbung`),
  ADD KEY `fk_user_has_cerbung_cerbung2_idx` (`idcerbung`),
  ADD KEY `fk_user_has_cerbung_user2_idx` (`username`);

--
-- Indexes for table `user_likes_cerbung`
--
ALTER TABLE `user_likes_cerbung`
  ADD PRIMARY KEY (`username`,`idcerbung`),
  ADD KEY `fk_user_has_cerbung_cerbung1_idx` (`idcerbung`),
  ADD KEY `fk_user_has_cerbung_user1_idx` (`username`);

--
-- Indexes for table `user_likes_paragraf`
--
ALTER TABLE `user_likes_paragraf`
  ADD PRIMARY KEY (`username`,`idparagraf`,`idcerbung`),
  ADD KEY `fk_user_has_paragraf_paragraf1_idx` (`idparagraf`),
  ADD KEY `fk_user_has_paragraf_user1_idx` (`username`);

--
-- Indexes for table `user_requests_cerbung`
--
ALTER TABLE `user_requests_cerbung`
  ADD PRIMARY KEY (`username`,`idcerbung`),
  ADD KEY `fk_user_has_cerbung_cerbung3_idx` (`idcerbung`),
  ADD KEY `fk_user_has_cerbung_user3_idx` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cerbung`
--
ALTER TABLE `cerbung`
  MODIFY `idcerbung` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `genre`
--
ALTER TABLE `genre`
  MODIFY `idgenre` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `paragraf`
--
ALTER TABLE `paragraf`
  MODIFY `idparagraf` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cerbung`
--
ALTER TABLE `cerbung`
  ADD CONSTRAINT `fk_cerbung_genre1` FOREIGN KEY (`idgenre`) REFERENCES `genre` (`idgenre`),
  ADD CONSTRAINT `fk_cerbung_user1` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

--
-- Constraints for table `paragraf`
--
ALTER TABLE `paragraf`
  ADD CONSTRAINT `fk_paragraf_cerbung` FOREIGN KEY (`idcerbung`) REFERENCES `cerbung` (`idcerbung`),
  ADD CONSTRAINT `fk_paragraf_user1` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

--
-- Constraints for table `user_follows_cerbung`
--
ALTER TABLE `user_follows_cerbung`
  ADD CONSTRAINT `fk_user_has_cerbung_cerbung2` FOREIGN KEY (`idcerbung`) REFERENCES `cerbung` (`idcerbung`),
  ADD CONSTRAINT `fk_user_has_cerbung_user2` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

--
-- Constraints for table `user_likes_cerbung`
--
ALTER TABLE `user_likes_cerbung`
  ADD CONSTRAINT `fk_user_has_cerbung_cerbung1` FOREIGN KEY (`idcerbung`) REFERENCES `cerbung` (`idcerbung`),
  ADD CONSTRAINT `fk_user_has_cerbung_user1` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

--
-- Constraints for table `user_likes_paragraf`
--
ALTER TABLE `user_likes_paragraf`
  ADD CONSTRAINT `fk_user_has_paragraf_paragraf1` FOREIGN KEY (`idparagraf`) REFERENCES `paragraf` (`idparagraf`),
  ADD CONSTRAINT `fk_user_has_paragraf_user1` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

--
-- Constraints for table `user_requests_cerbung`
--
ALTER TABLE `user_requests_cerbung`
  ADD CONSTRAINT `fk_user_has_cerbung_cerbung3` FOREIGN KEY (`idcerbung`) REFERENCES `cerbung` (`idcerbung`),
  ADD CONSTRAINT `fk_user_has_cerbung_user3` FOREIGN KEY (`username`) REFERENCES `user` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
