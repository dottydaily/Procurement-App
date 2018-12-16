-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 16, 2018 at 06:11 AM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.3.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

﻿--
-- Database: `procurement_app`
--
﻿
-- --------------------------------------------------------

--
-- Table structure for table `pr`
--

DROP TABLE IF EXISTS `pr`;
CREATE TABLE `pr` (
  `pr_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `date` date NOT NULL,
  `productlist_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `employee_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `customer_id` int(5) UNSIGNED ZEROFILL NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_thai_520_w2;
﻿
-- --------------------------------------------------------

--
-- Table structure for table `productlist`
--

DROP TABLE IF EXISTS `productlist`;
CREATE TABLE `productlist` (
  `productlist_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `product_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `product_name` varchar(50) COLLATE utf16_thai_520_w2 NOT NULL,
  `price_each` int(11) NOT NULL,
  `product_amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_thai_520_w2;
﻿
-- --------------------------------------------------------

--
-- Table structure for table `user_list`
--

DROP TABLE IF EXISTS `user_list`;
CREATE TABLE `user_list` (
  `user_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `username` varchar(50) COLLATE utf16_thai_520_w2 NOT NULL,
  `password` varchar(50) COLLATE utf16_thai_520_w2 NOT NULL,
  `firstname` varchar(50) COLLATE utf16_thai_520_w2 NOT NULL,
  `lastname` varchar(50) COLLATE utf16_thai_520_w2 NOT NULL,
  `email` varchar(50) COLLATE utf16_thai_520_w2 NOT NULL,
  `phonenumber` varchar(10) COLLATE utf16_thai_520_w2 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_thai_520_w2;
﻿
--
-- Dumping data for table `user_list`
--

﻿INSERT INTO `user_list` (`user_id`, `username`, `password`, `firstname`, `lastname`, `email`, `phonenumber`) VALUES
(00001, 'dottydaily', '10206', 'PORNPAT', 'SANTIBUPPAKUL', 'pornpat.s@ku.th', '0891085695')﻿,
(00002, 'PATPORN', 'BUPPASANTIKUL', 'dailydotty', '60201', 's.pornpat@mailg.com', '5965801980')﻿;
﻿
--
-- Indexes for dumped tables
--

--
-- Indexes for table `pr`
--
ALTER TABLE `pr`
  ADD PRIMARY KEY (`pr_id`);

--
-- Indexes for table `productlist`
--
ALTER TABLE `productlist`
  ADD PRIMARY KEY (`productlist_id`,`product_id`);

--
-- Indexes for table `user_list`
--
ALTER TABLE `user_list`
  ADD PRIMARY KEY (`user_id`);
﻿
--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pr`
--
ALTER TABLE `pr`
  MODIFY `pr_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `productlist`
--
ALTER TABLE `productlist`
  MODIFY `productlist_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user_list`
--
ALTER TABLE `user_list`
  MODIFY `user_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
﻿COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
