-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3308
-- Generation Time: Dec 03, 2023 at 04:06 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mycafe`
--

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `categoryName` varchar(255) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `categoryName`, `createdAt`) VALUES
(1, 'pizza', '2023-11-30 19:37:17');

-- --------------------------------------------------------

--
-- Table structure for table `deal`
--

CREATE TABLE `deal` (
  `id` int(11) NOT NULL,
  `dealName` varchar(255) NOT NULL,
  `dealPrice` double NOT NULL,
  `dealDescription` text DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `deal`
--

INSERT INTO `deal` (`id`, `dealName`, `dealPrice`, `dealDescription`, `createdAt`) VALUES
(1, 'myDeal', 1233, 'wer', '2023-12-03 12:46:23');

-- --------------------------------------------------------

--
-- Table structure for table `deal_item`
--

CREATE TABLE `deal_item` (
  `id` int(11) NOT NULL,
  `dealId` int(11) DEFAULT NULL,
  `itemId` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `deal_item`
--

INSERT INTO `deal_item` (`id`, `dealId`, `itemId`, `quantity`, `createdAt`) VALUES
(1, 1, 1, NULL, '2023-12-03 12:46:23');

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `id` int(11) NOT NULL,
  `itemName` varchar(255) NOT NULL,
  `itemPrice` double NOT NULL,
  `categoryId` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`id`, `itemName`, `itemPrice`, `categoryId`, `createdAt`) VALUES
(1, 'pizza', 1200, 1, '2023-12-01 08:33:23'),
(3, 'pizzaazaza', 11, 1, '2023-12-02 11:09:33');

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `id` int(11) NOT NULL,
  `orderDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `orderPlacedBy` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order_item`
--

CREATE TABLE `order_item` (
  `orderId` int(11) NOT NULL,
  `itemId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `roleName` varchar(255) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `roleName`, `createdAt`) VALUES
(1, 'admin', '2023-12-02 10:58:50'),
(2, 'salesman', '2023-12-02 10:58:50');

-- --------------------------------------------------------

--
-- Table structure for table `sales_report`
--

CREATE TABLE `sales_report` (
  `id` int(11) NOT NULL,
  `reportDate` date NOT NULL,
  `totalSales` double NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sales_report_item`
--

CREATE TABLE `sales_report_item` (
  `reportId` int(11) NOT NULL,
  `itemId` int(11) NOT NULL,
  `quantitySold` int(11) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `roleId` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `username`, `password`, `roleId`, `createdAt`) VALUES
(1, 'Ali', 'admin', 'admin', 1, '2023-12-03 12:23:01');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `deal`
--
ALTER TABLE `deal`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `deal_item`
--
ALTER TABLE `deal_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `dealId` (`dealId`),
  ADD KEY `itemId` (`itemId`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `categoryId` (`categoryId`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`id`),
  ADD KEY `orderPlacedBy` (`orderPlacedBy`);

--
-- Indexes for table `order_item`
--
ALTER TABLE `order_item`
  ADD PRIMARY KEY (`orderId`,`itemId`),
  ADD KEY `itemId` (`itemId`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sales_report`
--
ALTER TABLE `sales_report`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sales_report_item`
--
ALTER TABLE `sales_report_item`
  ADD PRIMARY KEY (`reportId`,`itemId`),
  ADD KEY `itemId` (`itemId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `roleId` (`roleId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `deal`
--
ALTER TABLE `deal`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `deal_item`
--
ALTER TABLE `deal_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `item`
--
ALTER TABLE `item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `sales_report`
--
ALTER TABLE `sales_report`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `deal_item`
--
ALTER TABLE `deal_item`
  ADD CONSTRAINT `deal_item_ibfk_1` FOREIGN KEY (`dealId`) REFERENCES `deal` (`id`),
  ADD CONSTRAINT `deal_item_ibfk_2` FOREIGN KEY (`itemId`) REFERENCES `item` (`id`);

--
-- Constraints for table `item`
--
ALTER TABLE `item`
  ADD CONSTRAINT `item_ibfk_1` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`);

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`orderPlacedBy`) REFERENCES `users` (`id`);

--
-- Constraints for table `order_item`
--
ALTER TABLE `order_item`
  ADD CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`orderId`) REFERENCES `order` (`id`),
  ADD CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`itemId`) REFERENCES `item` (`id`);

--
-- Constraints for table `sales_report_item`
--
ALTER TABLE `sales_report_item`
  ADD CONSTRAINT `sales_report_item_ibfk_1` FOREIGN KEY (`reportId`) REFERENCES `sales_report` (`id`),
  ADD CONSTRAINT `sales_report_item_ibfk_2` FOREIGN KEY (`itemId`) REFERENCES `item` (`id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
