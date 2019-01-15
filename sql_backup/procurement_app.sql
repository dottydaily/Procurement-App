-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 15, 2019 at 03:15 PM
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

--
-- Database: `procurement_app`
--
CREATE DATABASE IF NOT EXISTS `procurement_app` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `procurement_app`;

-- --------------------------------------------------------

--
-- Table structure for table `customer_list`
--

DROP TABLE IF EXISTS `customer_list`;
CREATE TABLE `customer_list` (
  `customer_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `customer_firstname` varchar(50) NOT NULL,
  `customer_lastname` varchar(50) NOT NULL,
  `customer_email` varchar(100) NOT NULL,
  `customer_address` varchar(200) NOT NULL,
  `customer_status` varchar(20) NOT NULL,
  `customer_tel` int(10) UNSIGNED ZEROFILL NOT NULL,
  `customer_limit` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer_list`
--

INSERT INTO `customer_list` (`customer_id`, `customer_firstname`, `customer_lastname`, `customer_email`, `customer_address`, `customer_status`, `customer_tel`, `customer_limit`) VALUES
(00001, 'PETER', 'JACKSON', 'peter.j@gmail.com', '50/878-879, Rangsit 12 Soi 15, Rangsit-Pathumthani road, Prachathipat distinct, Thanyaburi, Pathumthani, 12130', 'Good', 0897778180, 5000),
(00002, 'JOHN', 'WATSON', 'john.w@hotmail.com', '70/723, Soi Mongkol, Viphawadee road, Bangkhen distinct, Chatuchak, Bangkok, 10120', 'Good', 0776651234, 5000),
(00003, 'JEFF', 'POTTER', 'jeff.p@yahoo.com', '70/723, Soi Mongkol, Viphawadee road, Bangkhen distinct, Chatuchak, Bangkok, 10120', 'Good', 0638899713, 5000),
(00004, 'SOMCHAI', 'JAIDEE', 'somchai.j@ku.th', '50/878-879, Rangsit 15 Soi 22, Rangsit-Pathumthani road, Prachathipat distinct, Thanyaburi, Pathumthani, 12130', 'Good', 0126647335, 5000),
(00005, 'PAPATPHOL', 'BOONCHUAL', 'aisawaka@yahoo.jp', '47, Pluksa village, Soi 17/2, Bueng Yitho distinct, Thanyaburi, Pathumthani, 12130', 'Good', 0982346657, 5000),
(00006, 'TAWAREE', 'PASATHITI', 'nan_tawaree@gmail.com', '12/775, Supalai village, Soi 10, Bueng Yitho distinct, Klong-4, Pathumthani, 12130', 'Good', 0972253470, 5000),
(00007, 'CHATCHART', 'PHUETHAI', 'chatchart.p@gmail.com', '15, Rangsit-Pathumthani 14 Soi 17, Prachathipat, Thanyaburi, Pathumthani, 12130', 'Good', 0778971001, 5000),
(00008, 'PORNPAT', 'SANTIBUPPAKUL', 'pornpat.s@ku.th', '37, Rangsit 12 Soi 15, Prachathipat, Thanyaburi, Pathumthani, 12130', 'Good', 0891085695, 5000);

-- --------------------------------------------------------

--
-- Table structure for table `po`
--

DROP TABLE IF EXISTS `po`;
CREATE TABLE `po` (
  `po_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `pr_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `quotation_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `send_date` date NOT NULL,
  `po_status` varchar(20) NOT NULL DEFAULT 'Incomplete'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `po`
--

INSERT INTO `po` (`po_id`, `pr_id`, `quotation_id`, `send_date`, `po_status`) VALUES
(00001, 00002, 00001, '2018-12-28', 'Complete'),
(00002, 00001, 00000, '2018-12-20', 'Complete'),
(00003, 00005, 00003, '2019-01-07', 'Complete'),
(00004, 00003, 00004, '2019-01-14', 'Complete'),
(00006, 00000, 00002, '2019-07-07', 'Complete'),
(00008, 00006, 00006, '2019-10-10', 'Complete'),
(00009, 00000, 00002, '2019-01-14', 'Complete'),
(00011, 00011, 00009, '2019-01-19', 'Incomplete'),
(00012, 00010, 00008, '2019-01-31', 'Incomplete'),
(00013, 00009, 00007, '2019-01-25', 'Incomplete'),
(00014, 00012, 00010, '2019-01-24', 'Complete');

-- --------------------------------------------------------

--
-- Table structure for table `pr`
--

DROP TABLE IF EXISTS `pr`;
CREATE TABLE `pr` (
  `prDetailID` int(5) UNSIGNED ZEROFILL NOT NULL,
  `pr_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `product_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `date` date NOT NULL,
  `customer_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `pr_status` varchar(20) NOT NULL DEFAULT 'Incomplete'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pr`
--

INSERT INTO `pr` (`prDetailID`, `pr_id`, `product_id`, `date`, `customer_id`, `pr_status`) VALUES
(00001, 00000, 00008, '2018-12-13', 00004, 'Incomplete'),
(00002, 00000, 00005, '2018-12-13', 00004, 'Incomplete'),
(00003, 00001, 00009, '2018-12-13', 00004, 'Complete'),
(00004, 00001, 00004, '2018-12-13', 00004, 'Complete'),
(00005, 00002, 00010, '2018-12-23', 00004, 'Complete'),
(00006, 00002, 00011, '2018-12-23', 00004, 'Complete'),
(00007, 00003, 00001, '2018-07-23', 00003, 'Complete'),
(00008, 00003, 00006, '2018-07-23', 00003, 'Complete'),
(00009, 00004, 00001, '2018-07-02', 00001, 'Incomplete'),
(00010, 00005, 00002, '2019-01-03', 00007, 'Complete'),
(00011, 00005, 00005, '2019-01-03', 00007, 'Complete'),
(00012, 00005, 00012, '2019-01-03', 00007, 'Complete'),
(00013, 00006, 00012, '2019-01-18', 00004, 'Complete'),
(00014, 00007, 00004, '2019-01-27', 00006, 'Complete'),
(00015, 00007, 00013, '2019-01-27', 00006, 'Complete'),
(00016, 00008, 00010, '2019-01-06', 00002, 'Incomplete'),
(00017, 00008, 00014, '2019-01-06', 00002, 'Incomplete'),
(00018, 00009, 00007, '2019-01-13', 00006, 'Incomplete'),
(00019, 00010, 00015, '2019-01-13', 00004, 'Incomplete'),
(00020, 00011, 00003, '2019-01-09', 00004, 'Incomplete'),
(00021, 00012, 00003, '2019-01-10', 00001, 'Complete'),
(00022, 00013, 00016, '2019-01-13', 00006, 'Incomplete');

-- --------------------------------------------------------

--
-- Table structure for table `product_list`
--

DROP TABLE IF EXISTS `product_list`;
CREATE TABLE `product_list` (
  `product_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `product_name` varchar(50) NOT NULL,
  `price_per_each` int(11) NOT NULL,
  `product_amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `product_list`
--

INSERT INTO `product_list` (`product_id`, `product_name`, `price_per_each`, `product_amount`) VALUES
(00001, 'Pentel 310', 29, 100),
(00002, 'Oker Mouse MC21', 320, 25),
(00003, 'Jojo Chocolate', 20, 50),
(00004, 'Creative Speaker SP002', 399, 2),
(00005, 'Kitkat Vanilla', 30, 77),
(00006, 'LA Bycicle LED-002', 25, 1),
(00007, 'LG Monitor S20C300L', 1879, 3),
(00008, 'Asaki EarPhone', 189, 2),
(00009, 'Kingston USB 3.0 16 GB', 299, 2),
(00010, 'AJ DVD SP-338', 1999, 1),
(00011, 'Hatari 18-inch GREEN', 499, 3),
(00012, 'Microsoft Wired Mouse', 799, 1),
(00013, 'Cloreen', 40, 2),
(00014, 'Ostry KC06A', 2490, 1),
(00015, 'REMAX 3.0 USB 2 M', 199, 3),
(00016, 'BBQ Candy', 20, 79);

-- --------------------------------------------------------

--
-- Table structure for table `quotation_list`
--

DROP TABLE IF EXISTS `quotation_list`;
CREATE TABLE `quotation_list` (
  `quotationDetailID` int(5) UNSIGNED ZEROFILL NOT NULL,
  `quotation_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `pr_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `product_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `date` date NOT NULL,
  `customer_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `total_cost` int(20) UNSIGNED NOT NULL,
  `quotation_status` varchar(20) NOT NULL DEFAULT 'Incomplete'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `quotation_list`
--

INSERT INTO `quotation_list` (`quotationDetailID`, `quotation_id`, `pr_id`, `product_id`, `date`, `customer_id`, `total_cost`, `quotation_status`) VALUES
(00001, 00000, 00001, 00009, '2018-12-13', 00004, 6596, 'Complete'),
(00002, 00000, 00001, 00004, '2018-12-13', 00004, 6596, 'Complete'),
(00003, 00001, 00002, 00010, '2018-12-23', 00004, 3496, 'Complete'),
(00004, 00001, 00002, 00011, '2018-12-23', 00004, 3496, 'Complete'),
(00005, 00002, 00000, 00008, '2018-12-13', 00004, 1918, 'Incomplete'),
(00006, 00002, 00000, 00005, '2018-12-13', 00004, 1918, 'Incomplete'),
(00007, 00003, 00005, 00002, '2019-01-03', 00007, 10809, 'Complete'),
(00008, 00003, 00005, 00005, '2019-01-03', 00007, 10809, 'Complete'),
(00009, 00003, 00005, 00012, '2019-01-03', 00007, 10809, 'Complete'),
(00010, 00004, 00003, 00001, '2018-07-23', 00003, 2925, 'Complete'),
(00011, 00004, 00003, 00006, '2018-07-23', 00003, 2925, 'Complete'),
(00012, 00006, 00006, 00012, '2019-01-18', 00004, 599, 'Complete'),
(00013, 00007, 00009, 00007, '2019-01-13', 00006, 5637, 'Incomplete'),
(00014, 00008, 00010, 00015, '2019-01-13', 00004, 597, 'Incomplete'),
(00015, 00009, 00011, 00003, '2019-01-09', 00004, 1250, 'Incomplete'),
(00016, 00010, 00012, 00003, '2019-01-10', 00001, 1000, 'Complete');

-- --------------------------------------------------------

--
-- Table structure for table `user_list`
--

DROP TABLE IF EXISTS `user_list`;
CREATE TABLE `user_list` (
  `user_id` int(5) UNSIGNED ZEROFILL NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phonenumber` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_list`
--

INSERT INTO `user_list` (`user_id`, `username`, `password`, `firstname`, `lastname`, `email`, `phonenumber`) VALUES
(00001, 'dottydaily', '10206', 'PORNPAT', 'SANTIBUPPAKUL', 'pornpat.s@ku.th', '0891085695'),
(00002, 'PATPORN', 'BUPPASANTIKUL', 'dailydotty', '60201', 's.pornpat@mailg.com', '5965801980'),
(00003, 'guest', 'hello', 'HERE', 'GUEST', 'guest@gmail.com', '0888888888'),
(00004, 'jiratheep', '1234', 'JIRATHEEP', 'SADAKORN', 'jiratheep.s@ku.th', '0853261226'),
(00005, 'parms', '1234', 'PARM', 'POON', 'poo@h.com', '0863888888');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer_list`
--
ALTER TABLE `customer_list`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `po`
--
ALTER TABLE `po`
  ADD PRIMARY KEY (`po_id`);

--
-- Indexes for table `pr`
--
ALTER TABLE `pr`
  ADD PRIMARY KEY (`prDetailID`);

--
-- Indexes for table `product_list`
--
ALTER TABLE `product_list`
  ADD PRIMARY KEY (`product_id`);

--
-- Indexes for table `quotation_list`
--
ALTER TABLE `quotation_list`
  ADD PRIMARY KEY (`quotationDetailID`);

--
-- Indexes for table `user_list`
--
ALTER TABLE `user_list`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer_list`
--
ALTER TABLE `customer_list`
  MODIFY `customer_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `po`
--
ALTER TABLE `po`
  MODIFY `po_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `pr`
--
ALTER TABLE `pr`
  MODIFY `prDetailID` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `product_list`
--
ALTER TABLE `product_list`
  MODIFY `product_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `quotation_list`
--
ALTER TABLE `quotation_list`
  MODIFY `quotationDetailID` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `user_list`
--
ALTER TABLE `user_list`
  MODIFY `user_id` int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
