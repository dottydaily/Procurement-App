-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 17, 2018 at 02:50 AM
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
-- Table structure for table `customer_list`
--

DROP TABLE IF EXISTS `customer_list`;
CREATE TABLE `customer_list` (
  `customer_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `customer_firstname` varchar(50) COLLATE utf16_thai_520_w2 NOT NULL,
  `customer_lastname` varchar(50) COLLATE utf16_thai_520_w2 NOT NULL,
  `customer_email` varchar(100) COLLATE utf16_thai_520_w2 NOT NULL,
  `customer_address` varchar(200) COLLATE utf16_thai_520_w2 NOT NULL,
  `customer_status` varchar(20) COLLATE utf16_thai_520_w2 NOT NULL,
  `customer_tel` int(10) UNSIGNED ZEROFILL NOT NULL,
  `customer_lastdate` date DEFAULT NULL,
  `customer_limit` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_thai_520_w2;
﻿
-- --------------------------------------------------------

--
-- Table structure for table `order_list`
--

DROP TABLE IF EXISTS `order_list`;
CREATE TABLE `order_list` (
  `orderlist_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `product_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `amount` int(11) NOT NULL,
  `price_each` int(11) NOT NULL,
  `po_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_thai_520_w2;
﻿
-- --------------------------------------------------------

--
-- Table structure for table `po`
--

DROP TABLE IF EXISTS `po`;
CREATE TABLE `po` (
  `po_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `pr_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `customer_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `orderlist_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `send_date` date NOT NULL,
  `po_status` varchar(20) COLLATE utf16_thai_520_w2 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_thai_520_w2;
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
-- Table structure for table `product_list`
--

DROP TABLE IF EXISTS `product_list`;
CREATE TABLE `product_list` (
  `productlist_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `product_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `product_name` varchar(50) COLLATE utf16_thai_520_w2 NOT NULL,
  `price_each` int(11) NOT NULL,
  `product_amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_thai_520_w2;
﻿
-- --------------------------------------------------------

--
-- Table structure for table `quotation_list`
--

DROP TABLE IF EXISTS `quotation_list`;
CREATE TABLE `quotation_list` (
  `quotation_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `customer_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `employee_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `productlist_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `total_cost` int(10) UNSIGNED NOT NULL
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
-- Indexes for table `customer_list`
--
ALTER TABLE `customer_list`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `order_list`
--
ALTER TABLE `order_list`
  ADD PRIMARY KEY (`orderlist_id`,`product_id`),
  ADD KEY `po_id` (`po_id`);

--
-- Indexes for table `po`
--
ALTER TABLE `po`
  ADD PRIMARY KEY (`po_id`);

--
-- Indexes for table `pr`
--
ALTER TABLE `pr`
  ADD PRIMARY KEY (`pr_id`);

--
-- Indexes for table `product_list`
--
ALTER TABLE `product_list`
  ADD PRIMARY KEY (`productlist_id`,`product_id`);

--
-- Indexes for table `quotation_list`
--
ALTER TABLE `quotation_list`
  ADD PRIMARY KEY (`quotation_id`);

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
-- AUTO_INCREMENT for table `customer_list`
--
ALTER TABLE `customer_list`
  MODIFY `customer_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `order_list`
--
ALTER TABLE `order_list`
  MODIFY `orderlist_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `po`
--
ALTER TABLE `po`
  MODIFY `po_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pr`
--
ALTER TABLE `pr`
  MODIFY `pr_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `product_list`
--
ALTER TABLE `product_list`
  MODIFY `productlist_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `quotation_list`
--
ALTER TABLE `quotation_list`
  MODIFY `quotation_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user_list`
--
ALTER TABLE `user_list`
  MODIFY `user_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
﻿COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
