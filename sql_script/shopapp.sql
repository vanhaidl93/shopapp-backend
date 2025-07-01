-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 30, 2025 at 04:14 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shopapp`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(13, 'Dụng cụ học tập'),
(9, 'Mỹ phẩm'),
(4, 'Đồ gia dụng'),
(12, 'Đồ điện tử');

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- --------------------------------------------------------

--
-- Table structure for table `coupons`
--

CREATE TABLE `coupons` (
  `id` int(11) NOT NULL,
  `code` varchar(50) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Dumping data for table `coupons`
--

INSERT INTO `coupons` (`id`, `code`, `active`) VALUES
(1, 'HEAVEN', 1),
(2, 'DISCOUNT20', 1);

-- --------------------------------------------------------

--
-- Table structure for table `coupon_conditions`
--

CREATE TABLE `coupon_conditions` (
  `id` int(11) NOT NULL,
  `coupon_id` int(11) NOT NULL,
  `attribute` varchar(255) NOT NULL,
  `operator` varchar(10) NOT NULL,
  `value` varchar(255) NOT NULL,
  `discount_amount` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

--
-- Dumping data for table `coupon_conditions`
--

INSERT INTO `coupon_conditions` (`id`, `coupon_id`, `attribute`, `operator`, `value`, `discount_amount`) VALUES
(1, 1, 'minimum_amount', '>', '100', 10.00),
(2, 1, 'applicable_date', 'BETWEEN', '2025-06-15', 5.00),
(3, 2, 'minimum_amount', '>', '200', 20.00);

-- --------------------------------------------------------

--
-- Table structure for table `flyway_schema_history`
--

CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT current_timestamp(),
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `fullname` varchar(100) DEFAULT '',
  `email` varchar(100) DEFAULT '',
  `phone_number` varchar(20) NOT NULL,
  `address` varchar(200) NOT NULL,
  `note` varchar(100) DEFAULT '',
  `order_date` datetime DEFAULT current_timestamp(),
  `status` enum('pending','processing','shipped','delivered','cancelled') DEFAULT NULL COMMENT 'Trạng thái đơn hàng',
  `total_money` float DEFAULT NULL CHECK (`total_money` >= 0),
  `shipping_method` varchar(100) DEFAULT NULL,
  `shipping_address` varchar(200) DEFAULT NULL,
  `shipping_date` date DEFAULT NULL,
  `tracking_number` varchar(100) DEFAULT NULL,
  `payment_method` varchar(100) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `coupon_id` int(11) DEFAULT NULL,
  `vnp_txn_ref` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `fullname`, `email`, `phone_number`, `address`, `note`, `order_date`, `status`, `total_money`, `shipping_method`, `shipping_address`, `shipping_date`, `tracking_number`, `payment_method`, `active`, `coupon_id`, `vnp_txn_ref`) VALUES
(14, 2, 'John Smith', 'john@example.com', '1234567890', 'Hẻm A, Đường B, Q10', 'Note 1', '2023-10-08 00:00:00', 'processing', 500, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(15, 5, 'Eric Thompson', 'eric@example.com', '9876543210', '456 Elm St', 'Note 2', '2023-10-08 00:43:21', 'pending', 400, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(16, 2, 'Hans', 'hans@example.com', '5555555555', '789 Oak St', 'Note 3', '2023-10-08 00:43:21', 'pending', 300, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(17, 5, 'Alice Johnson', 'alice@example.com', '5551234567', '789 Cherry Ave', 'Note 4', '2023-10-08 00:43:21', 'pending', 200, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(18, 2, 'Robert Williams', 'robert@example.com', '5559876543', '321 Maple Rd', 'Note 5', '2023-10-08 00:43:21', 'pending', 100, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(19, 2, 'Sarah Davis', 'sarah@example.com', '5554445555', '987 Elm St', 'Note 6', '2023-10-08 00:43:21', 'pending', 250, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(20, 5, 'Michael Anderson', 'michael@example.com', '5556667777', '654 Oak Ave', 'Note 7', '2023-10-08 00:43:21', 'pending', 350, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(21, 2, 'Emma Wilson', 'emma@example.com', '5558889999', '789 Maple Ln', 'Note 8', '2023-10-08 00:43:21', 'pending', 450, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(22, 2, 'Olivia Brown', 'olivia@example.com', '5551112222', '987 Pine St', 'Note 47', '2023-10-08 00:43:21', 'pending', 350, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(23, 5, 'William Davis', 'william@example.com', '5553334444', '654 Elm Ave', 'Note 48', '2023-10-08 00:43:21', 'pending', 250, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(24, 2, 'Sophia Wilson', 'sophia@example.com', '5555556666', '789 Oak Ln', 'Note 49', '2023-10-08 00:43:21', 'pending', 150, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(25, 5, 'Alexander Anderson', 'alexander@example.com', '5557778888', '456 Maple Lane', 'Note 50', '2023-10-08 00:43:21', 'pending', 450, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(26, 2, 'Ava Thompson', 'ava@example.com', '5559990000', '987 Walnut Rd', 'Note 51', '2023-10-08 00:43:21', 'pending', 550, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(27, 5, 'Daniel Johnson', 'daniel@example.com', '5552223333', '654 Pine Ave', 'Note 52', '2023-10-08 00:43:21', 'pending', 650, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(28, 2, 'Mia Williams', 'mia@example.com', '5554445555', '789 Elm St', 'Note 53', '2023-10-08 00:43:21', 'pending', 750, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(29, 5, 'James Davis', 'james@example.com', '5556667777', '456 Oak Ave', 'Note 54', '2023-10-08 00:43:21', 'pending', 850, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(30, 5, 'Benjamin Thompson', 'benjamin@example.com', '5550001111', '654 Walnut Rd', 'Note 56', '2023-10-08 00:43:21', 'pending', 550, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(31, 2, 'Sophia Anderson', 'sophia@example.com', '5551112222', '987 Pine St', 'Note 57', '2023-10-08 00:43:21', 'pending', 350, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(32, 5, 'Elijah Davis', 'elijah@example.com', '5553334444', '654 Elm Ave', 'Note 58', '2023-10-08 00:43:21', 'pending', 250, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(33, 2, 'Ava Wilson', 'ava@example.com', '5555556666', '789 Oak Ln', 'Note 59', '2023-10-08 00:43:21', 'pending', 150, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(34, 5, 'Oliver Thompson', 'oliver@example.com', '5557778888', '456 Maple Lane', 'Note 60', '2023-10-08 00:43:21', 'pending', 450, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(35, 2, 'Mia Johnson', 'mia@example.com', '5559990000', '987 Walnut Rd', 'Note 61', '2023-10-08 00:43:21', 'pending', 550, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(36, 5, 'James Williams', 'james@example.com', '5552223333', '654 Pine Ave', 'Note 62', '2023-10-08 00:43:21', 'pending', 650, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(37, 2, 'Charlotte Davis', 'charlotte@example.com', '5554445555', '789 Elm St', 'Note 63', '2023-10-08 00:43:21', 'pending', 750, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(38, 5, 'Benjamin Wilson', 'benjamin@example.com', '5556667777', '456 Oak Ave', 'Note 64', '2023-10-08 00:43:21', 'pending', 850, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(39, 2, 'Amelia Thompson', 'amelia@example.com', '5558889999', '321 Maple Ln', 'Note 65', '2023-10-08 00:43:21', 'pending', 950, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(40, 5, 'Henry Johnson', 'henry@example.com', '5550001111', '654 Walnut Rd', 'Note 66', '2023-10-08 00:43:21', 'pending', 550, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(41, 5, 'Emily Davis', 'emily@example.com', '5552223333', '456 Walnut Lane', 'Note 46', '2023-10-08 00:43:21', 'pending', 150, NULL, NULL, NULL, NULL, NULL, 1, NULL, ''),
(42, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 0, NULL, ''),
(43, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'processing', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 0, NULL, ''),
(44, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 0, NULL, ''),
(45, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'processing', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 1, NULL, ''),
(46, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 1, NULL, ''),
(47, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 1, NULL, ''),
(48, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 1, NULL, ''),
(49, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 1, NULL, ''),
(50, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 1, NULL, ''),
(53, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-13 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-16', NULL, 'cod', 1, NULL, ''),
(57, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-25 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-28', NULL, 'cod', 1, NULL, NULL),
(58, 7, 'John Fancy', 'test@gmail.com', '0912345677', 'Hẻm A, Đường B, Q10', 'Fragile Good', '2025-06-25 00:00:00', 'pending', 123.45, 'express', 'Hẻm A, Đường B, Q10', '2025-06-28', NULL, 'vnpay', 1, NULL, NULL),
(68, 7, 'Happy Nguyen', 'happy@example.com', '0912345677', 'Hẻm A, Đường B, Q10', 'ds', '2025-06-25 00:00:00', 'pending', 228, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '61800150'),
(69, 7, 'Happy Nguyen', 'happy@example.com', '0912345677', 'Hẻm A, Đường B, Q10', 'ds', '2025-06-25 00:00:00', 'pending', 228, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '61800150'),
(70, 7, 'Happy Nguyen', 'happy@example.com', '0912345677', 'Hẻm A, Đường B, Q10', 'ds', '2025-06-25 00:00:00', 'pending', 228, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '17628223'),
(71, 7, 'Happy Nguyen', 'happy@example.com', '0912345677', 'Hẻm A, Đường B, Q10', '', '2025-06-25 00:00:00', 'pending', 228, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '23146839'),
(72, 7, 'Nguyen Van A', 'happy@example.com', '0912345677', 'Hẻm A, Đường B, Q10', 'ds', '2025-06-25 00:00:00', 'pending', 28752, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '77197955'),
(73, 6, 'Happy Nguyen', 'happy@example.com', '0912345676', 'Ho Chi Minh', 'ds', '2025-06-25 00:00:00', 'pending', 32480, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '01254465'),
(74, 6, 'Happy Nguyen', 'happy@example.com', '0912345676', 'Ho Chi Minh', 'ds', '2025-06-25 00:00:00', 'pending', 92110, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '47987869'),
(75, 6, 'Happy Nguyen', 'happy@example.com', '0912345676', 'Ho Chi Minh', 'ds', '2025-06-25 00:00:00', 'pending', 28953, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '98607668'),
(76, 6, 'Happy Nguyen', 'happy@example.com', '0912345676', 'Ho Chi Minh', 'ds', '2025-06-25 00:00:00', 'pending', 28953, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'cod', 1, NULL, ''),
(77, 6, 'Happy Nguyen', 'happy@example.com', '0912345676', 'Ho Chi Minh', 'ds', '2025-06-25 00:00:00', 'pending', 52253, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '25711577'),
(78, 6, 'Happy Nguyen', 'happy@example.com', '0912345676', 'Ho Chi Minh', 'ds', '2025-06-25 00:00:00', 'shipped', 52253, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '65911288'),
(79, 6, 'Happy Nguyen', 'happy@example.com', '0912345676', 'Ho Chi Minh', 'ds', '2025-06-25 00:00:00', 'shipped', 52253, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, NULL, '46219047'),
(80, 6, 'Happy Nguyen', 'happy@example.com', '0912345676', 'Ho Chi Minh', 'ds', '2025-06-25 00:00:00', 'shipped', 82503, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, 1, '00295658'),
(81, 7, 'Happy Nguyen', 'happy@example.com', '0912345677', 'Hẻm A, Đường B, Q10', 'ds', '2025-06-25 00:00:00', 'shipped', 55638, 'express', 'Ho Chi Minh', '2025-06-28', NULL, 'vnpay', 1, 1, '55401613');

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE `order_details` (
  `id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `number_of_products` int(11) DEFAULT 1,
  `total_money` decimal(10,2) DEFAULT 0.00,
  `color` varchar(20) DEFAULT '',
  `coupon_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_details`
--

INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `price`, `number_of_products`, `total_money`, `color`, `coupon_id`) VALUES
(14, 14, 1, 10.99, 2, 21.98, 'Red', NULL),
(15, 14, 2, 5.99, 3, 17.97, 'Blue', NULL),
(16, 14, 3, 8.49, 1, 8.49, 'Green', NULL),
(17, 15, 1, 10.99, 2, 21.98, 'Red', NULL),
(18, 15, 2, 5.99, 3, 17.97, 'Blue', NULL),
(19, 15, 3, 8.49, 1, 8.49, 'Green', NULL),
(20, 16, 6, 12.99, 3, 38.97, 'Purple', NULL),
(21, 17, 7, 6.99, 1, 6.99, 'Pink', NULL),
(22, 18, 8, 14.99, 2, 29.98, 'Gray', NULL),
(23, 19, 9, 11.49, 1, 11.49, 'Brown', NULL),
(24, 20, 10, 8.99, 3, 26.97, 'Black', NULL),
(25, 21, 11, 13.99, 2, 27.98, 'Silver', NULL),
(26, 22, 12, 10.49, 1, 10.49, 'Gold', NULL),
(27, 23, 13, 7.49, 2, 14.98, 'White', NULL),
(28, 38, 1, 10.99, 2, 21.98, 'Red', NULL),
(29, 38, 2, 5.99, 3, 17.97, 'Blue', NULL),
(30, 38, 3, 8.49, 1, 8.49, 'Green', NULL),
(31, 24, 14, 9.99, 2, 19.98, 'Red', NULL),
(32, 24, 15, 5.99, 3, 17.97, 'Blue', NULL),
(33, 24, 16, 8.49, 1, 8.49, 'Green', NULL),
(34, 25, 17, 10.99, 2, 21.98, 'Yellow', NULL),
(35, 25, 18, 5.99, 3, 17.97, 'Orange', NULL),
(36, 25, 19, 8.49, 1, 8.49, 'Purple', NULL),
(37, 26, 20, 6.99, 2, 13.98, 'Pink', NULL),
(38, 26, 21, 14.99, 1, 14.99, 'Gray', NULL),
(39, 26, 22, 11.49, 3, 34.47, 'Brown', NULL),
(40, 27, 23, 8.99, 2, 17.98, 'Black', NULL),
(41, 27, 24, 13.99, 1, 13.99, 'Silver', NULL),
(42, 27, 25, 10.49, 3, 31.47, 'Gold', NULL),
(43, 28, 26, 7.49, 2, 14.98, 'White', NULL),
(44, 28, 27, 9.99, 1, 9.99, 'Red', NULL),
(45, 28, 28, 5.99, 3, 17.97, 'Blue', NULL),
(46, 29, 29, 8.49, 1, 8.49, 'Green', NULL),
(47, 29, 30, 10.99, 2, 21.98, 'Yellow', NULL),
(48, 29, 31, 5.99, 3, 17.97, 'Orange', NULL),
(49, 30, 32, 8.49, 1, 8.49, 'Purple', NULL),
(50, 30, 33, 6.99, 2, 13.98, 'Pink', NULL),
(51, 30, 34, 14.99, 1, 14.99, 'Gray', NULL),
(52, 31, 35, 11.49, 3, 34.47, 'Brown', NULL),
(53, 31, 36, 8.99, 2, 17.98, 'Black', NULL),
(54, 31, 37, 13.99, 1, 13.99, 'Silver', NULL),
(55, 32, 38, 10.49, 3, 31.47, 'Gold', NULL),
(56, 32, 39, 7.49, 2, 14.98, 'White', NULL),
(57, 32, 40, 9.99, 1, 9.99, 'Red', NULL),
(58, 33, 41, 5.99, 3, 17.97, 'Blue', NULL),
(59, 33, 42, 8.49, 1, 8.49, 'Green', NULL),
(60, 33, 43, 10.99, 2, 21.98, 'Yellow', NULL),
(61, 34, 44, 5.99, 3, 17.97, 'Orange', NULL),
(62, 34, 45, 8.49, 1, 8.49, 'Purple', NULL),
(63, 34, 46, 6.99, 2, 13.98, 'Pink', NULL),
(64, 35, 47, 14.99, 1, 14.99, 'Gray', NULL),
(65, 35, 48, 11.49, 3, 34.47, 'Brown', NULL),
(66, 35, 49, 8.99, 2, 17.98, 'Black', NULL),
(67, 36, 50, 13.99, 1, 13.99, 'Silver', NULL),
(68, 36, 51, 10.49, 3, 31.47, 'Gold', NULL),
(69, 36, 52, 7.49, 2, 14.98, 'White', NULL),
(70, 37, 53, 9.99, 1, 9.99, 'Red', NULL),
(71, 37, 54, 5.99, 3, 17.97, 'Blue', NULL),
(72, 42, 3, 2083.00, 3, 6249.00, NULL, NULL),
(73, 42, 5, 4754.00, 2, 9508.00, NULL, NULL),
(74, 43, 3, 2083.00, 3, 6249.00, NULL, NULL),
(75, 43, 5, 4754.00, 2, 9508.00, NULL, NULL),
(76, 44, 3, 2083.00, 3, 6249.00, NULL, NULL),
(77, 44, 5, 4754.00, 2, 9508.00, NULL, NULL),
(78, 45, 3, 2083.00, 3, 6249.00, NULL, NULL),
(79, 45, 5, 4754.00, 2, 9508.00, NULL, NULL),
(80, 46, 3, 2083.00, 3, 6249.00, NULL, NULL),
(81, 46, 5, 4754.00, 2, 9508.00, NULL, NULL),
(82, 47, 3, 2083.00, 3, 6249.00, NULL, NULL),
(83, 47, 5, 4754.00, 2, 9508.00, NULL, NULL),
(84, 48, 3, 2083.00, 3, 6249.00, NULL, NULL),
(85, 48, 5, 4754.00, 2, 9508.00, NULL, NULL),
(86, 49, 3, 2083.00, 3, 6249.00, NULL, NULL),
(87, 49, 5, 4754.00, 2, 9508.00, NULL, NULL),
(88, 50, 3, 2083.00, 3, 6249.00, NULL, NULL),
(89, 50, 5, 4754.00, 2, 9508.00, NULL, NULL),
(94, 53, 3, 2083.00, 3, 6249.00, NULL, NULL),
(95, 53, 5, 4754.00, 2, 9508.00, NULL, NULL),
(99, 57, 3, 2083.00, 3, 6249.00, NULL, NULL),
(100, 57, 5, 4754.00, 2, 9508.00, NULL, NULL),
(101, 58, 3, 2083.00, 3, 6249.00, NULL, NULL),
(102, 58, 5, 4754.00, 2, 9508.00, NULL, NULL),
(112, 68, 9, 76.00, 3, 228.00, NULL, NULL),
(113, 69, 9, 76.00, 3, 228.00, NULL, NULL),
(114, 70, 9, 76.00, 3, 228.00, NULL, NULL),
(115, 71, 9, 76.00, 3, 228.00, NULL, NULL),
(116, 72, 9, 76.00, 3, 228.00, NULL, NULL),
(117, 72, 5, 4754.00, 6, 28524.00, NULL, NULL),
(118, 73, 2, 58.00, 2, 116.00, NULL, NULL),
(119, 73, 7, 1595.00, 2, 3190.00, NULL, NULL),
(120, 74, 4, 4413.00, 10, 44130.00, NULL, NULL),
(121, 74, 45, 4798.00, 10, 47980.00, NULL, NULL),
(122, 75, 1, 812.30, 4, 3249.20, NULL, NULL),
(123, 75, 3, 2083.00, 5, 10415.00, NULL, NULL),
(124, 76, 1, 812.30, 10, 8123.00, NULL, NULL),
(125, 76, 3, 2083.00, 10, 20830.00, NULL, NULL),
(126, 77, 1, 812.30, 14, 11372.20, NULL, NULL),
(127, 77, 4, 4413.00, 10, 44130.00, NULL, NULL),
(128, 78, 1, 812.30, 10, 8123.00, NULL, NULL),
(129, 78, 4, 4413.00, 10, 44130.00, NULL, NULL),
(130, 79, 1, 812.30, 10, 8123.00, NULL, NULL),
(131, 79, 4, 4413.00, 10, 44130.00, NULL, NULL),
(132, 80, 5, 4754.00, 10, 47540.00, NULL, NULL),
(133, 80, 4, 4413.00, 10, 44130.00, NULL, NULL),
(134, 81, 4, 4413.00, 10, 44130.00, NULL, NULL),
(135, 81, 8, 1769.00, 10, 17690.00, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(350) DEFAULT NULL COMMENT 'Tên sản phẩm',
  `price` decimal(10,2) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `description` longtext DEFAULT '',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `price`, `thumbnail`, `description`, `created_at`, `updated_at`, `category_id`) VALUES
(1, 'macbook air 15 inch 2025', 812.30, '8533224f-0005-4efc-bf25-cfe14d1ccf1a_fernando-andrade-potCPE_Cw8A-unsplash.jpg', 'This is a test product', '2025-01-27 22:01:00', '2025-06-21 09:58:39', 12),
(2, 'Heavy Duty Cotton Watch', 58.00, 'c536c82a-7670-4dd2-ac2c-f2991e83cb11_fernando-andrade-potCPE_Cw8A-unsplash.jpg', 'Occaecati odio occaecati nemo ad iure.', '2025-01-27 22:01:00', '2025-06-21 04:49:19', 9),
(3, 'Aerodynamic Wool Bottle', 2083.00, 'cf0bb2dd-4ee0-4974-ae4f-d30a588be1f9_eniko-kis-KsLPTsYaqIQ-unsplash.jpg', 'In debitis impedit deleniti consectetur minus.', '2025-01-27 22:01:00', '2025-06-21 07:20:03', 13),
(4, 'Heavy Duty Bronze Bottle', 4413.00, '8fbbe817-b592-4277-b190-232372905db3_domino-studio-164_6wVEHfI-unsplash.jpg', 'Aut consequuntur aut rerum asperiores fugit eaque.', '2025-01-27 22:01:00', '2025-06-21 09:58:51', NULL),
(5, 'Small Wooden Clock', 4754.00, NULL, 'Nam reiciendis ut culpa doloremque possimus pariatur id.', '2025-01-27 22:01:00', '2025-06-20 15:23:05', 4),
(6, 'Fantastic Silk Lamp', 106.00, NULL, 'Fuga fugit commodi.', '2025-01-27 22:01:00', NULL, NULL),
(7, 'Small Wool Coat', 1595.00, '3036dff0-72cd-48d4-9efd-7221f41fc61d_022.jpg', 'Et odit hic corporis asperiores amet harum ratione.', '2025-01-27 22:01:00', NULL, NULL),
(8, 'Lightweight Silk Chair', 1769.00, '36bf6016-4eff-4864-bf27-1de57e7d6589_027.jpg', 'Qui et maxime.', '2025-01-27 22:01:00', NULL, NULL),
(9, 'Heavy Duty Concrete Pants', 76.00, '326235eb-015b-4176-8977-fb96df932557_032.jpg', 'Incidunt est et.', '2025-01-27 22:01:00', NULL, NULL),
(10, 'Small Wool Bottle', 2350.00, '3d3097a5-38dc-45b7-81be-7300816ab4b9_037.jpg', 'Ut vel eligendi aliquam quia veniam sed.', '2025-01-27 22:01:00', NULL, NULL),
(11, 'Small Leather Bag', 4802.00, '67dedbd7-d14a-4db0-801f-3ab18fdca6d3_038.jpg', 'Quos ut dolor est.', '2025-01-27 22:01:00', NULL, NULL),
(12, 'Sleek Iron Bottle', 3147.00, 'cf00f484-f8a4-4b9a-8a9f-ef74bfd7ebcf_045.jpg', 'Quisquam omnis deserunt.', '2025-01-27 22:01:00', NULL, NULL),
(13, 'Synergistic Marble Shoes', 4005.00, '862e0948-8961-4c2a-b76a-f2322c846a4b_050.jpg', 'Ut voluptatem voluptas incidunt.', '2025-01-27 22:01:00', NULL, NULL),
(14, 'Sleek Wool Car', 1316.00, 'd152fdfd-6c49-440c-a249-8fdc08646333_055.jpg', 'Aut itaque ut neque voluptatem officiis voluptatem aut.', '2025-01-27 22:01:00', NULL, 4),
(15, 'Enormous Silk Chair', 180.00, '904a9f1c-207c-4df6-bb49-c954656a8c06_059.jpg', 'Vero sed quo quia laudantium et.', '2025-01-27 22:01:00', NULL, 4),
(16, 'Intelligent Iron Computer', 3859.00, 'c86bb69d-e5a2-4c93-8f0a-87504a8966ea_064.jpg', 'Incidunt quaerat pariatur ex ea.', '2025-01-27 22:01:00', NULL, NULL),
(17, 'Intelligent Granite Shirt', 2528.00, 'dafed832-a59c-41ad-bc60-011fadaf8020_069.jpg', 'Sed voluptate iure dolor.', '2025-01-27 22:01:00', NULL, NULL),
(18, 'Mediocre Steel Table', 3265.00, 'a98c6339-a19f-4dfe-a230-9a0ea1236192_083.jpg', 'Sint vel est.', '2025-01-27 22:01:00', NULL, 4),
(19, 'Practical Paper Chair', 2726.00, '699080d6-5b3c-4153-b3cb-140ee06f23f9_088.jpg', 'Id perspiciatis laudantium eum est nihil eos repudiandae.', '2025-01-27 22:01:00', NULL, NULL),
(20, 'Aerodynamic Cotton Computer', 3787.00, 'a8dbe441-3db0-44d5-9b87-baf5b932683e_093.jpg', 'Tempora veritatis voluptas vero voluptatem.', '2025-01-27 22:01:00', NULL, NULL),
(21, 'Enormous Iron Knife', 4616.00, '3399c072-8c9b-4206-94fd-c411ff5339b4_093.jpg', 'Consectetur fuga veniam repellat molestiae ut.', '2025-01-27 22:01:00', NULL, NULL),
(22, 'Mediocre Rubber Shirt', 3140.00, '79e8b5ff-8635-4416-8b4a-461f06ea0c03_096.jpg', 'Aut quod soluta voluptatem.', '2025-01-27 22:01:00', NULL, NULL),
(23, 'Awesome Wooden Bottle', 1199.00, '742f6cfb-8fdf-4fdf-9bee-68b31a70a470_099.jpg', 'Odit aut animi nemo repudiandae molestiae at qui.', '2025-01-27 22:01:00', NULL, NULL),
(24, 'Sleek Aluminum Wallet', 2736.00, '52f4e6a0-d342-4bf4-b7d9-bc7bcf564014_102.jpg', 'Assumenda et minus consequatur dolorem sint.', '2025-01-27 22:01:00', NULL, NULL),
(25, 'Enormous Wool Table', 68.00, '6b4e1d1f-1bc2-4f4a-a1a8-567ed652b9bc_104.jpg', 'Molestias veniam molestias ipsa delectus officiis.', '2025-01-27 22:01:00', NULL, 4),
(26, 'Practical Leather Table', 4589.00, '8ecbe322-1e99-400c-a11f-faafdf136092_109.jpg', 'Qui hic distinctio vitae placeat ad qui voluptatem.', '2025-01-27 22:01:00', NULL, NULL),
(27, 'Fantastic Linen Hat', 3654.00, '68b52bd4-c6b5-41bf-88f2-8471e90b1f14_113.jpg', 'Consequatur accusamus distinctio.', '2025-01-27 22:01:00', NULL, 4),
(28, 'Fantastic Rubber Shirt', 1690.00, '1575726c-6bec-4e9c-a063-fc3ed7589a32_118.jpg', 'Similique voluptas dolores.', '2025-01-27 22:01:00', NULL, NULL),
(29, 'Rustic Leather Watch', 1902.00, '9dd25e0b-b962-4f5c-b798-378998f2c8a8_122.jpg', 'Laborum dolores odio optio vel et vel.', '2025-01-27 22:01:00', NULL, 4),
(30, 'Incredible Leather Knife', 2573.00, NULL, 'Iste aliquid sint ut ut.', '2025-01-27 22:01:00', NULL, NULL),
(31, 'Synergistic Copper Keyboard', 79.00, 'd40a5b86-a736-4bec-8b80-78dd33928ddd_126.jpg', 'Aut non nostrum vero.', '2025-01-27 22:01:00', NULL, NULL),
(32, 'Fantastic Wool Pants', 1438.00, '0172df63-6c6c-4565-bf5f-c9ac847e77e9_128.jpg', 'Nihil nemo excepturi unde sunt.', '2025-01-27 22:01:00', NULL, 4),
(33, 'Heavy Duty Wool Car', 3847.00, '4bde7c98-7e06-4e20-9081-8aa2c345da02_131.jpg', 'Dolore expedita et voluptatibus at.', '2025-01-27 22:01:00', NULL, NULL),
(34, 'Gorgeous Cotton Knife', 4267.00, NULL, 'Dolorem ad quas illo nulla.', '2025-01-27 22:01:00', NULL, 4),
(35, 'Lightweight Aluminum Chair', 2455.00, NULL, 'Sed officia rerum.', '2025-01-27 22:01:00', NULL, NULL),
(36, 'Incredible Copper Wallet', 1301.00, NULL, 'Sed odit non sit unde voluptatem.', '2025-01-27 22:01:00', NULL, NULL),
(37, 'Enormous Cotton Table', 2531.00, NULL, 'Ab accusantium veniam quibusdam accusantium nemo quia.', '2025-01-27 22:01:00', NULL, NULL),
(38, 'Heavy Duty Linen Bag', 2930.00, NULL, 'Sint dolor fugiat consequatur delectus occaecati vero.', '2025-01-27 22:01:00', NULL, NULL),
(39, 'Mediocre Aluminum Wallet', 4322.00, NULL, 'Sit maiores autem eius dicta et.', '2025-01-27 22:01:00', NULL, NULL),
(40, 'Incredible Copper Watch', 4244.00, NULL, 'Consequatur dignissimos fugiat dolorum.', '2025-01-27 22:01:00', NULL, NULL),
(41, 'Enormous Aluminum Wallet', 787.00, NULL, 'Non earum inventore omnis eos ex aliquid.', '2025-01-27 22:01:00', NULL, NULL),
(42, 'Awesome Steel Watch', 572.00, NULL, 'Placeat error et cum voluptate.', '2025-01-27 22:01:00', NULL, NULL),
(43, 'Enormous Wooden Shoes', 2068.00, NULL, 'Nam molestiae quo amet deserunt et dolores.', '2025-01-27 22:01:00', NULL, NULL),
(44, 'Synergistic Cotton Keyboard', 3710.00, NULL, 'Alias et laborum fugiat minima vel non.', '2025-01-27 22:01:00', NULL, NULL),
(45, 'Heavy Duty Silk Computer', 4798.00, NULL, 'Officia minus nihil beatae ea voluptas asperiores.', '2025-01-27 22:01:00', NULL, NULL),
(46, 'Synergistic Rubber Bag', 1536.00, NULL, 'Pariatur modi dolor.', '2025-01-27 22:01:00', NULL, NULL),
(47, 'Enormous Paper Lamp', 1049.00, NULL, 'Est similique voluptatem aut nam vel consequatur.', '2025-01-27 22:01:00', NULL, NULL),
(48, 'Rustic Copper Plate', 550.00, NULL, 'Hic quis praesentium explicabo veritatis dolor.', '2025-01-27 22:01:00', NULL, NULL),
(49, 'Enormous Granite Pants', 3577.00, NULL, 'Accusamus est culpa quos voluptate corrupti quos.', '2025-01-27 22:01:00', NULL, NULL),
(50, 'Fantastic Bronze Coat', 122.00, NULL, 'Consequatur non provident.', '2025-01-27 22:01:00', NULL, NULL),
(51, 'Durable Bronze Clock', 3236.00, NULL, 'Nihil libero corrupti quis nostrum commodi sunt.', '2025-01-27 22:01:00', NULL, NULL),
(52, 'Gorgeous Wooden Bottle', 4287.00, NULL, 'Consectetur animi recusandae doloribus rerum optio.', '2025-01-27 22:01:00', NULL, 4),
(53, 'Durable Rubber Chair', 1190.00, NULL, 'Voluptatibus magni ab ipsa porro velit accusamus vero.', '2025-01-27 22:01:00', NULL, NULL),
(54, 'Rustic Aluminum Lamp', 1440.00, NULL, 'Dolorem aspernatur tempora.', '2025-01-27 22:01:00', NULL, NULL),
(55, 'Mediocre Steel Bag', 2653.00, NULL, 'Labore itaque quia voluptates.', '2025-01-27 22:01:00', NULL, NULL),
(56, 'Gorgeous Concrete Lamp', 2523.00, NULL, 'Illum aut expedita.', '2025-01-27 22:01:00', NULL, NULL),
(57, 'Practical Linen Gloves', 4126.00, NULL, 'Ut neque eos qui odit eius perspiciatis qui.', '2025-01-27 22:01:00', NULL, NULL),
(58, 'Incredible Marble Plate', 2188.00, NULL, 'Facilis quae dolorum cupiditate dicta.', '2025-01-27 22:01:00', NULL, 4),
(59, 'Synergistic Marble Gloves', 4183.00, NULL, 'Est a qui et eos.', '2025-01-27 22:01:00', NULL, 4),
(60, 'Intelligent Concrete Pants', 1242.00, NULL, 'Dolorem magnam perspiciatis consequatur cum deleniti tempore reiciendis.', '2025-01-27 22:01:00', NULL, NULL),
(61, 'Enormous Linen Clock', 4580.00, NULL, 'Quibusdam magnam ea deleniti saepe quod.', '2025-01-27 22:01:00', NULL, 4),
(62, 'Rustic Wool Lamp', 2580.00, NULL, 'Error consectetur qui.', '2025-01-27 22:01:00', NULL, NULL),
(63, 'Rustic Rubber Bottle', 4801.00, NULL, 'Sed quam consequatur.', '2025-01-27 22:01:00', NULL, 4),
(64, 'Lightweight Cotton Shoes', 1055.00, NULL, 'Natus doloribus earum sit unde repellendus cum.', '2025-01-27 22:01:00', NULL, NULL),
(65, 'Intelligent Leather Hat', 1126.00, NULL, 'Dolorum vitae labore perferendis nostrum maxime omnis.', '2025-01-27 22:01:00', NULL, NULL),
(66, 'Heavy Duty Plastic Plate', 33.00, NULL, 'Voluptatem pariatur aut quod aliquid reiciendis velit et.', '2025-01-27 22:01:00', NULL, 4),
(67, 'Fantastic Marble Knife', 1732.00, NULL, 'Fuga ullam odio totam deserunt labore.', '2025-01-27 22:01:00', NULL, NULL),
(68, 'Lightweight Granite Chair', 4218.00, NULL, 'Est in qui ipsum et autem.', '2025-01-27 22:01:00', NULL, NULL),
(69, 'Synergistic Copper Bag', 3811.00, NULL, 'Quisquam quasi voluptas.', '2025-01-27 22:01:00', NULL, NULL),
(70, 'Gorgeous Linen Hat', 171.00, NULL, 'Alias cumque sed totam delectus id natus.', '2025-01-27 22:01:00', NULL, NULL),
(71, 'Ergonomic Iron Car', 1231.00, NULL, 'Fugit provident et sequi perferendis ex accusamus.', '2025-01-27 22:01:00', NULL, NULL),
(72, 'Synergistic Concrete Knife', 1562.00, NULL, 'Sunt eum nihil.', '2025-01-27 22:01:00', NULL, 4),
(73, 'Incredible Aluminum Watch', 1227.00, NULL, 'Voluptates nostrum distinctio deleniti.', '2025-01-27 22:01:00', NULL, NULL),
(74, 'Awesome Aluminum Bag', 1045.00, NULL, 'Explicabo maiores neque.', '2025-01-27 22:01:00', NULL, NULL),
(75, 'Rustic Concrete Table', 3566.00, NULL, 'Quod non explicabo beatae qui.', '2025-01-27 22:01:00', NULL, NULL),
(76, 'Small Granite Gloves', 500.00, NULL, 'Eos velit quidem non.', '2025-01-27 22:01:00', NULL, 4),
(77, 'Incredible Wooden Bottle', 3955.00, NULL, 'Distinctio nihil eum numquam aspernatur perspiciatis sequi sit.', '2025-01-27 22:01:00', NULL, NULL),
(78, 'Intelligent Paper Bench', 3354.00, NULL, 'Aperiam rerum sit.', '2025-01-27 22:01:00', NULL, NULL),
(79, 'Awesome Paper Bottle', 3216.00, NULL, 'Vitae molestiae dolores id ut et quidem.', '2025-01-27 22:01:00', NULL, NULL),
(80, 'Ergonomic Bronze Hat', 3593.00, NULL, 'Tempore consequatur aspernatur architecto sed consectetur voluptas temporibus.', '2025-01-27 22:01:00', NULL, NULL),
(81, 'Fantastic Wool Bench', 3741.00, NULL, 'Accusamus dolorem cum nihil laudantium voluptatem sed molestiae.', '2025-01-27 22:01:00', NULL, NULL),
(82, 'Synergistic Steel Coat', 4710.00, NULL, 'Nostrum expedita cupiditate maiores molestias.', '2025-01-27 22:01:00', NULL, NULL),
(83, 'Small Copper Computer', 1048.00, NULL, 'Asperiores ullam est.', '2025-01-27 22:01:00', NULL, NULL),
(84, 'Heavy Duty Steel Shoes', 1702.00, NULL, 'Fugit est et ipsam quia neque maiores ducimus.', '2025-01-27 22:01:00', NULL, NULL),
(85, 'Durable Steel Watch', 3946.00, NULL, 'Aut dolores impedit dolores fuga.', '2025-01-27 22:01:00', NULL, NULL),
(86, 'Incredible Paper Shoes', 1175.00, NULL, 'Reprehenderit illum quia aut qui id.', '2025-01-27 22:01:00', NULL, NULL),
(87, 'Aerodynamic Linen Wallet', 4274.00, NULL, 'Rem ad illum consequatur ut dolorem alias.', '2025-01-27 22:01:00', NULL, NULL),
(88, 'Gorgeous Plastic Wallet', 2915.00, NULL, 'Fugit adipisci corporis aut.', '2025-01-27 22:01:00', NULL, NULL),
(89, 'Synergistic Copper Coat', 2582.00, NULL, 'Eum et assumenda eum atque et amet fuga.', '2025-01-27 22:01:00', NULL, NULL),
(90, 'Awesome Bronze Pants', 1024.00, NULL, 'Exercitationem eos nihil sit eveniet ex.', '2025-01-27 22:01:00', NULL, NULL),
(91, 'Lightweight Wool Watch', 1300.00, NULL, 'Ut voluptatem numquam.', '2025-01-27 22:01:00', NULL, 4),
(92, 'Awesome Rubber Chair', 3277.00, NULL, 'Numquam sint sit laboriosam molestiae minus itaque aliquam.', '2025-01-27 22:01:00', NULL, NULL),
(93, 'Gorgeous Silk Bag', 2447.00, NULL, 'Impedit totam voluptatem dolorem illum et eum.', '2025-01-27 22:01:00', NULL, NULL),
(94, 'Aerodynamic Leather Table', 1282.00, NULL, 'Iste aspernatur aut sit porro aperiam iste.', '2025-01-27 22:01:00', NULL, NULL),
(95, 'Mediocre Aluminum Shoes', 1152.00, NULL, 'Provident voluptatem velit itaque atque ducimus vel.', '2025-01-27 22:01:00', NULL, NULL),
(96, 'Rustic Plastic Gloves', 587.00, NULL, 'Quos libero et veritatis nobis eum.', '2025-01-27 22:01:00', NULL, NULL),
(97, 'Aerodynamic Marble Bag', 1401.00, NULL, 'Delectus numquam ut non in.', '2025-01-27 22:01:00', NULL, NULL),
(98, 'Lightweight Paper Watch', 4669.00, NULL, 'Et voluptatum et ut neque.', '2025-01-27 22:01:00', NULL, 4),
(99, 'Mediocre Leather Hat', 4839.00, NULL, 'Ut molestias reprehenderit debitis veniam aut.', '2025-01-27 22:01:00', NULL, NULL),
(100, 'Awesome Aluminum Car', 4932.00, NULL, 'Omnis laboriosam deleniti hic qui animi sequi sit.', '2025-01-27 22:01:00', NULL, 4),
(101, 'macbook air 15 inch 2025', 12000.00, NULL, 'macbook air 15 inch 2025', '2025-06-20 16:51:53', NULL, 12),
(102, 'macbook air 15 inch 2025', 12000.00, NULL, 'macbook air 15 inch 2025', '2025-06-20 16:52:11', NULL, 12),
(103, 'macbook air 15 inch 2025', 12333.00, NULL, 'macbook air 15 inch 2025', '2025-06-20 16:57:33', NULL, 12),
(104, 'macbook air 15 inch 2025', 12333.00, NULL, 'macbook air 15 inch 2025', '2025-06-20 16:57:54', NULL, 12),
(105, 'macbook air 15 inch 2025', 10000000.00, NULL, 'ma', '2025-06-20 17:00:12', NULL, 12),
(106, 'macbook air 15 inch 2025', 10000000.00, NULL, 'ma', '2025-06-20 17:02:06', '2025-06-21 03:58:16', 12),
(107, 'macbook air 15 inch 2025', 10000000.00, NULL, 'ma', '2025-06-20 17:02:28', NULL, 12),
(108, 'macbook air 15 inch 2025', 10000000.00, NULL, 'ma', '2025-06-20 17:04:32', NULL, 12),
(109, 'macbook air 15 inch 2025', 10000000.00, NULL, 'ma', '2025-06-20 17:09:20', NULL, 12),
(110, 'macbook air 15 inch 2025', 10000000.00, NULL, 'ma', '2025-06-20 17:09:44', NULL, 12),
(111, 'macbook air 15 inch 2025', 10000000.00, 'c905452e-27e2-4672-9d3a-1c5d70becca6_c-d-x-PDX_a_82obo-unsplash.jpg', 'ma', '2025-06-20 17:13:58', '2025-06-21 06:34:38', 12),
(112, 'macbook air 15 inch 2025', 2000.00, 'ba245557-1678-4ec7-81d7-d0df3c77cef1_rachit-tank-2cFZ_FB08UM-unsplash.jpg', 'macbook air 15 inch 2025', '2025-06-21 04:50:03', '2025-06-21 04:50:17', 12),
(113, 'Heavy Duty Bronze Bottle', 500.00, '574b60f1-fdae-44b0-beab-b6d3e284ccb8_domino-studio-164_6wVEHfI-unsplash.jpg', 'Heavy Duty Bronze Bottle', '2025-06-21 15:46:51', '2025-06-21 15:46:51', 4),
(114, 'Heavy Duty Bronze Bottle', 500.00, 'cc5db4b3-fa5f-4d1a-b4a1-51b437eccc91_domino-studio-164_6wVEHfI-unsplash.jpg', 'Heavy Duty Bronze Bottle', '2025-06-21 15:46:58', '2025-06-21 15:46:58', 4),
(115, 'Heavy Duty Bronze Bottle', 500.00, '895be138-44b2-4ae9-8dff-7fa6e8a14b73_domino-studio-164_6wVEHfI-unsplash.jpg', 'Heavy Duty Bronze Bottle', '2025-06-21 15:47:39', '2025-06-21 15:48:19', 4);

-- --------------------------------------------------------

--
-- Table structure for table `product_images`
--

CREATE TABLE `product_images` (
  `id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `image_name` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product_images`
--

INSERT INTO `product_images` (`id`, `product_id`, `image_name`) VALUES
(8, 1, 'f2fcda26-8304-48bc-9472-ead1c297770f_001.jpg'),
(9, 1, '0d83d40d-78e9-4c7d-80ee-3b5b68003104_002.jpg'),
(10, 1, 'cbef6f33-b6b7-45e0-ac44-f1a0b5fe1d4c_003.jpg'),
(14, 2, '34823684-2d69-4e22-8ad6-aa2bc1ccbaf6_006.jpg'),
(15, 2, '5815f773-c836-464e-a857-ef03d465390e_007.jpg'),
(16, 2, 'e0dc71cb-b492-4aad-a9da-5b864eb9419d_008.jpg'),
(17, 2, 'a1846d28-2fa2-41a6-bcc3-59585be39d78_009.jpg'),
(19, 3, 'c4bd25e4-620c-44fc-9f69-22edde299a8f_011.jpg'),
(20, 3, '5f0bdbc4-64d5-4114-b590-7c85503a9174_012.jpg'),
(21, 3, 'f139045a-ef18-4aa3-9484-e06721d9150c_013.jpg'),
(24, 5, '6ba6318c-ed8b-4f6d-9a9b-d8ca1c6e1afc_016.jpg'),
(25, 5, 'b5d21126-2dd3-42bb-80dd-8f79d999d29c_017.jpg'),
(26, 5, 'f8a83202-74a6-4386-99db-3ef713a26471_016.jpg'),
(27, 5, '0661c814-b39c-47ab-958f-9465679eeb9b_017.jpg'),
(28, 7, '3036dff0-72cd-48d4-9efd-7221f41fc61d_022.jpg'),
(29, 7, 'cb6259db-e46d-48a5-8b7a-6c46a3b55446_023.jpg'),
(30, 7, 'bf379bbd-9e69-4cd6-bc03-d6db54b780b7_024.jpg'),
(31, 7, 'e2856261-99af-4494-82fe-bccb1befb22b_025.jpg'),
(32, 7, 'ccd11465-f64c-444d-90e1-8862ab71a62c_026.jpg'),
(33, 8, '36bf6016-4eff-4864-bf27-1de57e7d6589_027.jpg'),
(34, 8, '07175991-c161-48ce-8e66-a0ec093eea50_028.jpg'),
(35, 8, 'f92c0523-1b77-4233-bdc9-b8dab47536ed_029.jpg'),
(36, 8, 'cd3814e3-0918-440a-bab5-1db67ca4574f_030.jpg'),
(37, 8, '35dda624-858d-499d-867e-0260c358e591_031.jpg'),
(38, 9, '326235eb-015b-4176-8977-fb96df932557_032.jpg'),
(39, 9, 'e950378b-a6ff-40c6-a6b5-b469d841b282_033.jpg'),
(40, 9, '4e8a2b84-96b2-49c4-bbcf-2805934a8076_034.jpg'),
(41, 9, '95251a5e-7eaa-45e9-9571-04b3247ccfab_035.jpg'),
(42, 9, '1178bd64-484b-4e34-84cf-86881d1f8948_036.jpg'),
(43, 10, '3d3097a5-38dc-45b7-81be-7300816ab4b9_037.jpg'),
(44, 10, '390bbdba-c579-4f5a-a455-59d7dbda9c72_038.jpg'),
(45, 10, '29925120-5403-49cc-b85c-922673a0d5ed_039.jpg'),
(46, 10, 'd1fef884-a4c1-471b-8fc9-400195436564_040.jpg'),
(47, 11, '67dedbd7-d14a-4db0-801f-3ab18fdca6d3_038.jpg'),
(48, 11, '3175fef9-b080-4eca-8bc7-e9cc5947c642_039.jpg'),
(49, 11, '2c899f27-f467-4ad2-a52e-6e307d8bdfda_040.jpg'),
(50, 11, '0e32e381-4cb5-4771-a008-1354a612e763_041.jpg'),
(51, 11, 'cad35cbc-fd56-4f4c-b2db-77d04f63a149_042.jpg'),
(52, 12, 'cf00f484-f8a4-4b9a-8a9f-ef74bfd7ebcf_045.jpg'),
(53, 12, '09443def-0c28-40e1-b18d-f1cc8be05464_046.jpg'),
(54, 12, '9fc08b21-eb95-474e-b9c8-9de7bc6360e0_047.jpg'),
(55, 12, '813ad44a-a7bd-432a-bddc-b8f9a32b0039_048.jpg'),
(56, 12, '356a89f7-ef4d-454d-b2ad-da9f4fdaf6d6_049.jpg'),
(57, 13, '862e0948-8961-4c2a-b76a-f2322c846a4b_050.jpg'),
(58, 13, '8db6fb15-d24b-4c7f-b61d-c9d3452dd2fe_051.jpg'),
(59, 13, 'fbf2c23a-a2c3-48fb-ac69-c8ac4913bfb2_052.jpg'),
(60, 13, '25cefd9a-e306-422e-9fbf-8c98141dc81a_053.jpg'),
(61, 13, 'ab864e03-25f0-4537-9ca3-1d2bd431a535_054.jpg'),
(62, 14, 'd152fdfd-6c49-440c-a249-8fdc08646333_055.jpg'),
(63, 14, 'e29ac0a0-8c1f-493f-a66b-6f170de48f94_056.jpg'),
(64, 14, '1a384d85-a6a5-47a7-861d-5fb56054829c_057.jpg'),
(65, 14, '2cf23264-4685-44d8-a371-41c3e5ad9563_058.jpg'),
(66, 14, 'a8a30554-51c9-4a99-9ff4-e25169edc713_059.jpg'),
(67, 15, '904a9f1c-207c-4df6-bb49-c954656a8c06_059.jpg'),
(68, 15, '5d8fd37f-06a5-4906-9252-61ba564723c1_060.jpg'),
(69, 15, '95a13b88-919f-49af-b868-e2bfed0bb98e_061.jpg'),
(70, 15, '0ca19870-50b7-4719-82ae-b6b8272d45cd_062.jpg'),
(71, 15, '26365791-3978-4df8-b6fd-f2f516b1e7e3_063.jpg'),
(72, 16, 'c86bb69d-e5a2-4c93-8f0a-87504a8966ea_064.jpg'),
(73, 16, '93b29ad3-b5ab-40c1-ace8-ec069cba2db1_065.jpg'),
(74, 16, 'd832472c-856a-4243-bc98-531af0b1a1a5_066.jpg'),
(75, 16, '05b3bc62-c8f1-41c3-abd0-3515a1f19ed3_067.jpg'),
(76, 16, 'ee6f30dc-68de-4c8a-bfd7-62df2ed20d22_068.jpg'),
(77, 17, 'dafed832-a59c-41ad-bc60-011fadaf8020_069.jpg'),
(78, 17, '65cf789d-ed86-4fd7-8030-2a79af81a88b_070.jpg'),
(79, 17, 'a2ac276a-5384-4646-ad52-ad00b731cc9b_080.jpg'),
(80, 17, '665a59de-01f1-4a4f-870e-4fc64a43f384_081.jpg'),
(81, 17, 'f3c45546-5b36-403e-b26e-781cb8f2f825_082.jpg'),
(82, 18, 'a98c6339-a19f-4dfe-a230-9a0ea1236192_083.jpg'),
(83, 18, 'a2dc214e-e02d-4b63-bcfd-03926d983833_084.jpg'),
(84, 18, 'b2f38c0a-4b15-480a-8160-314a216b8586_085.jpg'),
(85, 18, 'c69ed25c-c703-49e4-bc5e-f3c94d2ed703_086.jpg'),
(86, 18, '07f74ddc-b4c7-489d-95cb-d417d5277938_087.jpg'),
(87, 19, '699080d6-5b3c-4153-b3cb-140ee06f23f9_088.jpg'),
(88, 19, 'e21a7434-b10d-412a-996b-be2a0703f8b0_089.jpg'),
(89, 19, '121db8eb-34dd-4f28-879e-4206ca84410a_090.jpg'),
(90, 19, '2ad86d1c-2b0a-4817-8fbc-0785f7d74870_091.jpg'),
(91, 19, 'a544ae1e-8cac-4940-9b7e-c7ddd8b40efc_092.jpg'),
(92, 20, 'a8dbe441-3db0-44d5-9b87-baf5b932683e_093.jpg'),
(93, 20, '195b50a0-ec76-4b51-8c34-cfe127be1ae3_094.jpg'),
(94, 20, '4db2c5fd-3593-45bc-b942-ab1f52974b9f_095.jpg'),
(95, 20, 'ecafbacb-d393-417c-988c-ad65b539749e_096.jpg'),
(96, 20, '4253fc5d-899f-4e6d-b45c-c239acbdc5e8_097.jpg'),
(97, 21, '3399c072-8c9b-4206-94fd-c411ff5339b4_093.jpg'),
(98, 21, '9e591a61-f1de-4ccc-a37e-d60b08ad9f7b_094.jpg'),
(99, 21, '8dd9aaa4-8706-4be3-b50b-4621bfd1be93_095.jpg'),
(100, 22, '79e8b5ff-8635-4416-8b4a-461f06ea0c03_096.jpg'),
(101, 22, '113703a7-fcd1-4759-88d2-650987899ad6_097.jpg'),
(102, 22, '01bac487-2758-4a55-be45-ae020dbbf65f_098.jpg'),
(103, 22, '0f6cc84b-a25d-471a-8b33-7281987968a8_099.jpg'),
(104, 23, '742f6cfb-8fdf-4fdf-9bee-68b31a70a470_099.jpg'),
(105, 23, '912a1687-201e-4e80-98ab-f7ebddf157f9_100.jpg'),
(106, 23, 'c1bb4b13-d3e5-4543-9517-debacd1ec1c9_101.jpg'),
(107, 24, '52f4e6a0-d342-4bf4-b7d9-bc7bcf564014_102.jpg'),
(108, 24, '25de850a-1d1d-44b0-bf47-d755b4cc70e9_103.jpg'),
(109, 24, 'af47fc82-8b9f-487c-99cf-bbc453f8189e_104.jpg'),
(110, 25, '6b4e1d1f-1bc2-4f4a-a1a8-567ed652b9bc_104.jpg'),
(111, 25, '71bb68f6-6c70-416c-b506-208a96e0a0ef_105.jpg'),
(112, 25, '7ab15e39-6589-4fe2-9dbe-5ef5ef1775b6_106.jpg'),
(113, 25, '0954d8c6-0bde-406d-8448-8e7570ddb092_108.jpg'),
(114, 26, '8ecbe322-1e99-400c-a11f-faafdf136092_109.jpg'),
(115, 26, '0db7e33b-0133-48b1-aa3d-210235f78016_110.jpg'),
(116, 26, 'fa29af18-1765-4370-955f-4ea0c14ede78_111.jpg'),
(117, 26, '7569bde4-4484-44cb-9338-a14ea927011d_112.jpg'),
(118, 27, '68b52bd4-c6b5-41bf-88f2-8471e90b1f14_113.jpg'),
(119, 27, 'aaa89b16-33c0-4e60-bd07-e89970be7a0a_114.jpg'),
(120, 27, '3e810ada-210a-4449-a25c-df0ffcee8bfd_115.jpg'),
(121, 27, '5d84cbea-90da-4753-b273-10db236b2fad_116.jpg'),
(122, 27, '7435fb50-5b74-4d77-b397-b211ab62c26c_117.jpg'),
(123, 28, '1575726c-6bec-4e9c-a063-fc3ed7589a32_118.jpg'),
(124, 28, 'b0c66298-0fa2-4431-99b9-77583cefe70d_119.jpg'),
(125, 28, 'c52e5599-e351-46ea-b6a4-0729a3118972_120.jpg'),
(126, 28, 'bb36fe37-4ef3-4f9c-8067-825617afbb54_121.jpg'),
(127, 29, '9dd25e0b-b962-4f5c-b798-378998f2c8a8_122.jpg'),
(128, 29, '066c0a93-9553-4aa6-9467-867a14dc368d_123.jpg'),
(129, 29, '1d5b57ce-3f73-48f2-ac92-f96d35dba744_124.jpg'),
(130, 29, '5e2be4c1-c3cc-4225-98c7-656e7bda9ae5_125.jpg'),
(131, 31, 'd40a5b86-a736-4bec-8b80-78dd33928ddd_126.jpg'),
(132, 31, '395efb19-034f-47d6-aa9f-8a773b20971e_127.jpg'),
(133, 32, '0172df63-6c6c-4565-bf5f-c9ac847e77e9_128.jpg'),
(134, 32, '28bb7329-fb63-4e5f-b21c-a66d4a61366d_129.jpg'),
(135, 32, '12babe42-6fd2-45e6-81e9-14ca68a5774e_130.jpg'),
(136, 33, '4bde7c98-7e06-4e20-9081-8aa2c345da02_131.jpg'),
(137, 33, 'e81584ec-0f6c-4f6c-babd-d5275ca64191_132.jpg'),
(138, 33, 'e7c5de4c-b197-408c-a8ee-6209e1392299_133.jpg'),
(145, 53, '980df8b3-2cd3-4b80-af3f-a1ce754ba87e_c-d-x-PDX_a_82obo-unsplash.jpg'),
(146, 53, '8239287e-b9c4-40fd-8ce1-7101426dbff5_domino-studio-164_6wVEHfI-unsplash.jpg'),
(147, 53, 'aa6ef70b-b8ac-41ae-afc1-89b9bc9d21db_eniko-kis-KsLPTsYaqIQ-unsplash.jpg'),
(148, 53, 'd9a88a2b-6d23-4ae2-b74f-d344a3c13c8c_fernando-andrade-potCPE_Cw8A-unsplash.jpg'),
(149, 53, 'd8eba8da-af2f-444b-b47c-95e44b9f1ae7_rachit-tank-2cFZ_FB08UM-unsplash.jpg'),
(151, 2, 'a8323a1c-2d5b-44cf-a083-9978b64ed8d2_rachit-tank-2cFZ_FB08UM-unsplash.jpg'),
(152, 111, '4192f6ed-5b15-467e-be5f-ae203ec009b7_c-d-x-PDX_a_82obo-unsplash.jpg'),
(153, 111, '6468075c-2c5f-492e-92bc-2a079ce24c02_domino-studio-164_6wVEHfI-unsplash.jpg'),
(154, 111, '11d76ee8-c8df-4aa4-a1bf-b68ec0c1769c_eniko-kis-KsLPTsYaqIQ-unsplash.jpg'),
(155, 111, 'c349b8cd-1c7b-4ff1-8cae-0af84cc449bf_fernando-andrade-potCPE_Cw8A-unsplash.jpg'),
(156, 111, 'a384ef6e-07cc-49ac-affc-32db0018a0ae_rachit-tank-2cFZ_FB08UM-unsplash.jpg'),
(157, 112, '492fb74f-db04-4aed-8f6c-9d01757fbdcd_c-d-x-PDX_a_82obo-unsplash.jpg'),
(158, 112, '3ed4a7d3-b532-41e7-bc2f-b18517966c20_domino-studio-164_6wVEHfI-unsplash.jpg'),
(159, 112, '350c4a8d-0382-48d2-903f-36f4132c5ce6_eniko-kis-KsLPTsYaqIQ-unsplash.jpg'),
(160, 112, 'd010cfc8-d83c-4075-aed2-c6c229691e9b_fernando-andrade-potCPE_Cw8A-unsplash.jpg'),
(161, 112, '6f2044c8-fa1f-43bf-965a-3203b34238d3_rachit-tank-2cFZ_FB08UM-unsplash.jpg'),
(162, 3, '887e2de3-c61e-4653-9f72-5e0b2bcc2077_domino-studio-164_6wVEHfI-unsplash.jpg'),
(163, 3, '0d0e0b91-a8bb-4747-a756-dd61728e103f_fernando-andrade-potCPE_Cw8A-unsplash.jpg'),
(164, 1, '2cf7dc4a-f28f-4b55-8dbb-2f873eb66ab0_c-d-x-PDX_a_82obo-unsplash.jpg'),
(166, 1, '0b5f8a0d-0177-4607-affa-4a0424b0480b_domino-studio-164_6wVEHfI-unsplash.jpg'),
(167, 113, '3c66ffef-9bd8-4d74-9331-b078d6be5e65_c-d-x-PDX_a_82obo-unsplash.jpg'),
(168, 113, '844a1978-3359-4d2d-9b24-f4e2e5275bcf_domino-studio-164_6wVEHfI-unsplash.jpg'),
(169, 113, '81aeedae-2d6c-4dcd-b6e8-53489382faeb_eniko-kis-KsLPTsYaqIQ-unsplash.jpg'),
(170, 113, 'a6f6d0bc-8599-405c-aa11-34e1cd7cba70_fernando-andrade-potCPE_Cw8A-unsplash.jpg'),
(171, 114, 'a4860eb1-f305-4b5b-8804-10466c740cac_c-d-x-PDX_a_82obo-unsplash.jpg'),
(172, 114, 'f04e573b-aae2-46db-aca4-709310566ba1_domino-studio-164_6wVEHfI-unsplash.jpg'),
(173, 114, '36b71a48-5124-4d0d-a537-899ac96c4166_eniko-kis-KsLPTsYaqIQ-unsplash.jpg'),
(174, 114, 'bcca66c5-01b6-4fb0-b775-a1259503ca3d_fernando-andrade-potCPE_Cw8A-unsplash.jpg'),
(175, 115, 'edd5e458-8fad-41c2-8f4c-45c8f629e0f5_c-d-x-PDX_a_82obo-unsplash.jpg'),
(176, 115, 'b53c0eab-10a5-4d26-9fc5-e1ba806f500f_domino-studio-164_6wVEHfI-unsplash.jpg'),
(177, 115, '92b932dd-87e2-44b1-bd86-a00b78797bee_eniko-kis-KsLPTsYaqIQ-unsplash.jpg'),
(178, 115, 'cef4072a-7ba6-4449-91ca-142be3cb1e26_fernando-andrade-potCPE_Cw8A-unsplash.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

-- --------------------------------------------------------

--
-- Table structure for table `tokens`
--

CREATE TABLE `tokens` (
  `id` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `token_type` varchar(50) NOT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `revoked` tinyint(1) NOT NULL,
  `expired` tinyint(1) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `is_mobile` tinyint(1) DEFAULT 0,
  `refresh_token` varchar(255) DEFAULT '',
  `refresh_expiration_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tokens`
--

INSERT INTO `tokens` (`id`, `token`, `token_type`, `expiration_date`, `revoked`, `expired`, `user_id`, `is_mobile`, `refresh_token`, `refresh_expiration_date`) VALUES
(11, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6ImFub255bW91c1VzZXIiLCJ1c2VySWQiOiI2IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FOT05ZTU9VUyIsInN1YiI6IkpXVC1UT0tFTiIsImV4cCI6MTc1MjI1MjM4NX0.nP3ouSHHCAh85hCv8F2a4-DHs9tfE2gB2-uolcJZoxY', 'Bearer', '2025-07-11 16:46:25', 0, 0, 6, 1, '088dbfdc-a14d-4c9d-b963-16ff87f2c829', '2025-08-10 16:46:25'),
(12, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5MTIzNDU2NzYiLCJ1c2VySWQiOiI2IiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJzdWIiOiJKV1QtVE9LRU4iLCJleHAiOjE3NTIyNTI5MzN9.D3PxpCNuXilzOtksyhiDPg2_d5mkx0JUUjUkwjPz-lA', 'Bearer', '2025-07-11 16:55:33', 0, 0, 6, 1, '3235da12-3896-4605-8364-55c9545ad6c3', '2025-08-10 16:55:33'),
(139, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5MTIzNDU2NzkiLCJ1c2VySWQiOiIyIiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwic3ViIjoiSldULVRPS0VOIiwiZXhwIjoxNzUwNjQ3MTIxfQ.S7rU9rM2HeWgoxq3ndnfSzvoMwF3-nc-clpoYtQuvmg', 'Bearer', '2025-06-23 02:52:01', 0, 0, 2, 0, 'ae52a19e-cdb0-4c57-8aa0-449d019323d7', '2025-06-23 02:52:51'),
(141, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5MTIzNDU2NzkiLCJ1c2VySWQiOiIyIiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwic3ViIjoiSldULVRPS0VOIiwiZXhwIjoxNzUwNjQ3ODc0fQ.mxIa46-aHG-eabUmom-KeXw9a7XrgsbPpxaD4KCx0vM', 'Bearer', '2025-06-23 03:04:34', 0, 0, 2, 0, 'aada9ef0-1dbc-4d37-9927-1ee05d49a0d5', '2025-06-23 03:05:24'),
(215, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5MTIzNDU2NzYiLCJ1c2VySWQiOiI2IiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJzdWIiOiJKV1QtVE9LRU4iLCJleHAiOjE3NTA5MzQxMzF9.86jytFOEOoIiiK5c-zjL6iDo2rMblklpBa5GKS0EELU', 'Bearer', '2025-06-26 10:35:31', 0, 0, 6, 0, '4c541499-5e9f-4bc8-9cdf-863e83a740ec', '2025-07-02 10:35:31'),
(216, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5MTIzNDU2NzciLCJ1c2VySWQiOiI3IiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJzdWIiOiJKV1QtVE9LRU4iLCJleHAiOjE3NTA5MzQ1NTZ9.Q2sJOc8DbeMXqjcjq8AxTCNYqvRHVZ5Fp_VsI8PQaW0', 'Bearer', '2025-06-26 10:42:37', 0, 0, 7, 0, '870e879e-1720-4c60-83bf-c116b3d50439', '2025-07-02 10:42:37'),
(217, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5MTIzNDU2NzciLCJ1c2VySWQiOiI3IiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJzdWIiOiJKV1QtVE9LRU4iLCJleHAiOjE3NTA5MzUwMzV9.L2HN8JhGqqYWPw63qkZ6FWSQqS-POcFc4yhycEMCZ-s', 'Bearer', '2025-06-26 10:50:35', 0, 0, 7, 0, 'c5ef71f7-c711-43b3-b5fd-c21a06662352', '2025-07-02 10:50:35'),
(218, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWJqZWN0IjoiMDkxMjM0NTY3NyIsInVzZXJJZCI6IjciLCJzdWIiOiJKV1QtVE9LRU4iLCJleHAiOjE3NTEyNTUwNDB9.1JXN77uanegRt3uvS23MCxUT_rNJ2xJUCpeGo_Ho6hw', 'Bearer', '2025-06-30 03:44:00', 0, 0, 7, 0, '91477258-b907-4241-a427-692b67134060', '2025-07-06 03:44:00'),
(228, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWJqZWN0IjoiMDkxMjM0NTY3OSIsInVzZXJJZCI6IjIiLCJzdWIiOiJKV1QtVE9LRU4iLCJleHAiOjE3NTEzNDI0NTV9.EAb0Z21P9DRjAI21p8xK2wV7eRxLIb1kGFzOW0Bs_ks', 'Bearer', '2025-07-01 04:00:55', 0, 0, 2, 0, 'c5c05141-87bf-4549-8bfd-f0813b5a38c0', '2025-07-07 04:00:55');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `fullname` varchar(100) DEFAULT '',
  `phone_number` varchar(15) DEFAULT NULL,
  `address` varchar(200) DEFAULT '',
  `password` char(68) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  `date_of_birth` date DEFAULT NULL,
  `facebook_account_id` varchar(50) DEFAULT '',
  `google_account_id` varchar(50) DEFAULT '',
  `role_id` int(11) DEFAULT 1,
  `email` varchar(255) DEFAULT '',
  `profile_image` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `fullname`, `phone_number`, `address`, `password`, `created_at`, `updated_at`, `is_active`, `date_of_birth`, `facebook_account_id`, `google_account_id`, `role_id`, `email`, `profile_image`) VALUES
(2, 'Admin Testing', '0912345679', 'Hẻm A, Đường B, Q10', '{bcrypt}$2a$10$A7sqnyf/Upohgs1C6ueJg.m4gNw0GOr00oYvBxEW8DPK/EUMDOsBS', '2025-01-27 21:30:06', NULL, 1, NULL, '0', '0', 2, '', ''),
(5, 'User 03', '0912345675', 'Hẻm A, Đường B, Q10', '{bcrypt}$2a$10$fvyOa4H1Tc2AbzfZShl5e.vBBYddVzlhXXv3tH/b/9IzM05AfIVRy', '2025-01-27 21:33:33', NULL, 1, NULL, '0', '0', 1, '', ''),
(6, 'Happy Nguyen', '0912345676', 'Ho Chi Minh', '{bcrypt}$2a$10$gndi0ahLSQOUyEOEIiDCaeedEX050VYD80TDnncNo4AyZVHvEoUmm', '2025-05-25 16:00:55', '2025-06-11 16:31:03', 1, NULL, '0', '0', 1, '', ''),
(7, 'User 02', '0912345677', 'Hẻm A, Đường B, Q10', '{bcrypt}$2a$10$mOCc5os1Zs2BSfWoCOtwuuE3kQxlXeqMx/lSb8PB4rHhrzSwl273.', '2025-06-11 15:29:46', '2025-06-13 10:49:48', 1, NULL, '0', '0', 1, '', ''),
(8, 'User 01', '0912345674', 'Hẻm A, Đường B, Q10', '{bcrypt}$2a$10$pCwS1n9LYiqBsy1G6wTC8OhfJSNmkPkcd.nmNkNHsnfm0r6s68tam', '2025-06-19 02:11:14', NULL, 1, NULL, '0', '0', 1, '', ''),
(9, 'Hai Nguyen', NULL, NULL, '', '2025-06-29 09:34:25', NULL, 1, NULL, NULL, '114237537200622527404', 1, 'vanhaidemowebapp@gmail.com', 'https://lh3.googleusercontent.com/a/ACg8ocK2iIwBkIEWMl2BIoJAzRvh8OYOkbYxm0DbCIYNcTfByvzFFTQ=s96-c');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `name_2` (`name`),
  ADD UNIQUE KEY `name_3` (`name`),
  ADD UNIQUE KEY `name_4` (`name`),
  ADD UNIQUE KEY `name_5` (`name`),
  ADD UNIQUE KEY `name_6` (`name`),
  ADD UNIQUE KEY `name_7` (`name`),
  ADD UNIQUE KEY `name_8` (`name`),
  ADD UNIQUE KEY `name_9` (`name`),
  ADD UNIQUE KEY `name_10` (`name`),
  ADD UNIQUE KEY `name_11` (`name`),
  ADD UNIQUE KEY `name_12` (`name`),
  ADD UNIQUE KEY `name_13` (`name`);

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `comments_ibfk_2` (`user_id`);

--
-- Indexes for table `coupons`
--
ALTER TABLE `coupons`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `coupon_conditions`
--
ALTER TABLE `coupon_conditions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `coupon_id` (`coupon_id`);

--
-- Indexes for table `flyway_schema_history`
--
ALTER TABLE `flyway_schema_history`
  ADD PRIMARY KEY (`installed_rank`),
  ADD KEY `flyway_schema_history_s_idx` (`success`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `fk_orders_coupon` (`coupon_id`);

--
-- Indexes for table `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `fk_order_details_coupon` (`coupon_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `product_images`
--
ALTER TABLE `product_images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_product_images_product_id` (`product_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tokens`
--
ALTER TABLE `tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token` (`token`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `coupons`
--
ALTER TABLE `coupons`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `coupon_conditions`
--
ALTER TABLE `coupon_conditions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=82;

--
-- AUTO_INCREMENT for table `order_details`
--
ALTER TABLE `order_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=136;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;

--
-- AUTO_INCREMENT for table `product_images`
--
ALTER TABLE `product_images`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=184;

--
-- AUTO_INCREMENT for table `tokens`
--
ALTER TABLE `tokens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=229;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `coupon_conditions`
--
ALTER TABLE `coupon_conditions`
  ADD CONSTRAINT `coupon_conditions_ibfk_1` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_orders_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`),
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `fk_order_details_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`),
  ADD CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `product_images`
--
ALTER TABLE `product_images`
  ADD CONSTRAINT `fk_product_images_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `product_images_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `tokens`
--
ALTER TABLE `tokens`
  ADD CONSTRAINT `tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
