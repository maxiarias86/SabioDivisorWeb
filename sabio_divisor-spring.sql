-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 02-08-2025 a las 01:09:37
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `sabio_divisor-spring`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_user`
--

CREATE TABLE `app_user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `app_user`
--

INSERT INTO `app_user` (`id`, `email`, `name`, `password`) VALUES
(4, 'maxi@gmail.com', 'maxi', '$2a$10$86TFzCuyAopknMVgfY.pZ.B/XMOcEdxdOT0wAocgSaZvyB4FQvQNS'),
(5, 'dai@gmail.com', 'dai', '$2a$10$0hlwrzEtWXBNiP//xO2SEePyhSEVbwlebi8PlxthqVRtxCEFJL0Fu'),
(6, 'cuba@gmail.com', 'cuba', '$2a$10$JhDWut/2EkuTmdrFDtKWCuOZNANVBGCnG/iCKAqUy.RHCu76tj10u'),
(7, 'rodo@gmail.com', 'rodo', '$2a$10$b2ihRh.OC0qntJrbVIT3IOdT87thDedPB7ZNxvPNWVtk.NUDwpG56'),
(8, 'gaston@gmail.com', 'gaston', '$2a$10$Rg2L1wXfiqgAsedKafLeHueJZ3YfSR0Wk/Zfc6o2bz8T5jU4.uq7u'),
(9, 'benja@gmail.com', 'benja', '$2a$10$YvOrZLTQnXH7VYlFxbCCe.eouynqaZfhSHmLsilYBm2SF3A8JlyWC'),
(10, 'vazquez@gmail.com', 'Estefi ', '$2a$10$VioDOS/uZ6fDiDHkiKHZ.edOl6DfPL4VC9Tm8L5UgDIvmjUGDGV/.'),
(12, 'cata@gmail.com', 'cata', '$2a$10$xfkZPjritq52bZfCmNvFeegiq6k02kgngFkPG/rBTQOI56FFYm1W.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `debt`
--

CREATE TABLE `debt` (
  `id` bigint(20) NOT NULL,
  `amount` double NOT NULL,
  `due_date` date DEFAULT NULL,
  `installment_number` int(11) NOT NULL,
  `creditor_id` bigint(20) NOT NULL,
  `debtor_id` bigint(20) NOT NULL,
  `expense_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `debt`
--

INSERT INTO `debt` (`id`, `amount`, `due_date`, `installment_number`, `creditor_id`, `debtor_id`, `expense_id`) VALUES
(57, 1666.6666666666667, '2023-01-01', 1, 6, 4, 202),
(58, 1666.6666666666667, '2023-02-01', 2, 6, 4, 202),
(59, 1666.6666666666667, '2023-03-01', 3, 6, 4, 202),
(60, 1666.6666666666667, '2023-04-01', 4, 6, 4, 202),
(61, 1666.6666666666667, '2023-05-01', 5, 6, 4, 202),
(62, 1666.6666666666667, '2023-06-01', 6, 6, 4, 202),
(63, 1666.6666666666667, '2023-07-01', 7, 6, 4, 202),
(64, 1666.6666666666667, '2023-08-01', 8, 6, 4, 202),
(65, 1666.6666666666667, '2023-09-01', 9, 6, 4, 202),
(66, 1666.6666666666667, '2023-10-01', 10, 6, 4, 202),
(67, 1666.6666666666667, '2023-11-01', 11, 6, 4, 202),
(68, 1666.6666666666667, '2023-12-01', 12, 6, 4, 202),
(69, 1666.6666666666667, '2024-01-01', 13, 6, 4, 202),
(70, 1666.6666666666667, '2024-02-01', 14, 6, 4, 202),
(71, 1666.6666666666667, '2024-03-01', 15, 6, 4, 202),
(72, 1666.6666666666667, '2024-04-01', 16, 6, 4, 202),
(73, 1666.6666666666667, '2024-05-01', 17, 6, 4, 202),
(74, 1666.6666666666667, '2024-06-01', 18, 6, 4, 202),
(75, 1666.6666666666667, '2024-07-01', 19, 6, 4, 202),
(76, 1666.6666666666667, '2024-08-01', 20, 6, 4, 202),
(77, 1666.6666666666667, '2024-09-01', 21, 6, 4, 202),
(78, 1666.6666666666667, '2024-10-01', 22, 6, 4, 202),
(79, 1666.6666666666667, '2024-11-01', 23, 6, 4, 202),
(80, 1666.6666666666667, '2024-12-01', 24, 6, 4, 202),
(81, 1666.6666666666667, '2023-01-01', 1, 6, 5, 202),
(82, 1666.6666666666667, '2023-02-01', 2, 6, 5, 202),
(83, 1666.6666666666667, '2023-03-01', 3, 6, 5, 202),
(84, 1666.6666666666667, '2023-04-01', 4, 6, 5, 202),
(85, 1666.6666666666667, '2023-05-01', 5, 6, 5, 202),
(86, 1666.6666666666667, '2023-06-01', 6, 6, 5, 202),
(87, 1666.6666666666667, '2023-07-01', 7, 6, 5, 202),
(88, 1666.6666666666667, '2023-08-01', 8, 6, 5, 202),
(89, 1666.6666666666667, '2023-09-01', 9, 6, 5, 202),
(90, 1666.6666666666667, '2023-10-01', 10, 6, 5, 202),
(91, 1666.6666666666667, '2023-11-01', 11, 6, 5, 202),
(92, 1666.6666666666667, '2023-12-01', 12, 6, 5, 202),
(93, 1666.6666666666667, '2024-01-01', 13, 6, 5, 202),
(94, 1666.6666666666667, '2024-02-01', 14, 6, 5, 202),
(95, 1666.6666666666667, '2024-03-01', 15, 6, 5, 202),
(96, 1666.6666666666667, '2024-04-01', 16, 6, 5, 202),
(97, 1666.6666666666667, '2024-05-01', 17, 6, 5, 202),
(98, 1666.6666666666667, '2024-06-01', 18, 6, 5, 202),
(99, 1666.6666666666667, '2024-07-01', 19, 6, 5, 202),
(100, 1666.6666666666667, '2024-08-01', 20, 6, 5, 202),
(101, 1666.6666666666667, '2024-09-01', 21, 6, 5, 202),
(102, 1666.6666666666667, '2024-10-01', 22, 6, 5, 202),
(103, 1666.6666666666667, '2024-11-01', 23, 6, 5, 202),
(104, 1666.6666666666667, '2024-12-01', 24, 6, 5, 202),
(137, 1666.6666666666667, '2023-10-10', 1, 7, 5, 303),
(138, 1666.6666666666667, '2023-11-10', 2, 7, 5, 303),
(139, 1666.6666666666667, '2023-12-10', 3, 7, 5, 303),
(140, 1666.6666666666667, '2024-01-10', 4, 7, 5, 303),
(141, 1666.6666666666667, '2024-02-10', 5, 7, 5, 303),
(142, 1666.6666666666667, '2024-03-10', 6, 7, 5, 303),
(143, 3909.3333333333335, '2023-10-10', 1, 8, 5, 303),
(144, 3909.3333333333335, '2023-11-10', 2, 8, 5, 303),
(145, 3909.3333333333335, '2023-12-10', 3, 8, 5, 303),
(146, 3909.3333333333335, '2024-01-10', 4, 8, 5, 303),
(147, 3909.3333333333335, '2024-02-10', 5, 8, 5, 303),
(148, 3909.3333333333335, '2024-03-10', 6, 8, 5, 303),
(149, 4115, '2025-05-25', 1, 7, 5, 353),
(150, 4115, '2025-06-25', 2, 7, 5, 353),
(151, 4115, '2025-07-25', 3, 7, 5, 353),
(152, 2500, '2024-07-09', 1, 4, 9, 302),
(153, 2500, '2024-08-09', 2, 4, 9, 302),
(154, 2500, '2024-09-09', 3, 4, 9, 302),
(155, 2500, '2024-10-09', 4, 4, 9, 302),
(156, 2500, '2024-11-09', 5, 4, 9, 302),
(157, 2500, '2024-12-09', 6, 4, 9, 302),
(158, 2500, '2025-01-09', 7, 4, 9, 302),
(159, 2500, '2025-02-09', 8, 4, 9, 302),
(160, 2500, '2025-03-09', 9, 4, 9, 302),
(161, 2500, '2025-04-09', 10, 4, 9, 302),
(162, 2500, '2025-05-09', 11, 4, 9, 302),
(163, 2500, '2025-06-09', 12, 4, 9, 302),
(164, 2057.25, '2024-07-09', 1, 5, 9, 302),
(165, 2057.25, '2024-08-09', 2, 5, 9, 302),
(166, 2057.25, '2024-09-09', 3, 5, 9, 302),
(167, 2057.25, '2024-10-09', 4, 5, 9, 302),
(168, 2057.25, '2024-11-09', 5, 5, 9, 302),
(169, 2057.25, '2024-12-09', 6, 5, 9, 302),
(170, 2057.25, '2025-01-09', 7, 5, 9, 302),
(171, 2057.25, '2025-02-09', 8, 5, 9, 302),
(172, 2057.25, '2025-03-09', 9, 5, 9, 302),
(173, 2057.25, '2025-04-09', 10, 5, 9, 302),
(174, 2057.25, '2025-05-09', 11, 5, 9, 302),
(175, 2057.25, '2025-06-09', 12, 5, 9, 302),
(176, 11728, '2025-07-26', 1, 5, 6, 402),
(177, 11728, '2025-08-26', 2, 5, 6, 402),
(178, 5000, '2025-07-25', 1, 4, 5, 552),
(179, 5000, '2025-08-25', 2, 4, 5, 552),
(192, 720.1666666666666, '2023-12-31', 1, 8, 4, 602),
(193, 720.1666666666666, '2024-01-31', 2, 8, 4, 602),
(194, 720.1666666666666, '2024-02-29', 3, 8, 4, 602),
(195, 720.1666666666666, '2024-03-31', 4, 8, 4, 602),
(196, 720.1666666666666, '2024-04-30', 5, 8, 4, 602),
(197, 720.1666666666666, '2024-05-31', 6, 8, 4, 602),
(198, 8333.333333333334, '2023-12-31', 1, 8, 5, 602),
(199, 8333.333333333334, '2024-01-31', 2, 8, 5, 602),
(200, 8333.333333333334, '2024-02-29', 3, 8, 5, 602),
(201, 8333.333333333334, '2024-03-31', 4, 8, 5, 602),
(202, 8333.333333333334, '2024-04-30', 5, 8, 5, 602),
(203, 8333.333333333334, '2024-05-31', 6, 8, 5, 602),
(207, 33333.333333333336, '2025-07-17', 1, 4, 10, 652),
(208, 33333.333333333336, '2025-08-17', 2, 4, 10, 652),
(209, 33333.333333333336, '2025-09-17', 3, 4, 10, 652);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `expense`
--

CREATE TABLE `expense` (
  `description` varchar(255) DEFAULT NULL,
  `installments` int(11) NOT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `expense`
--

INSERT INTO `expense` (`description`, `installments`, `id`) VALUES
('Alfombra', 6, 152),
('Heladera', 24, 202),
('Disfraz SpiderMan', 12, 302),
('Pijama', 6, 303),
('Empanadas', 3, 353),
('Chorizos', 2, 402),
('Parlante', 2, 552),
('Gomero', 6, 602),
('vacas', 3, 652);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `payment`
--

CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL,
  `payer_id` bigint(20) NOT NULL,
  `recipient_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `payment`
--

INSERT INTO `payment` (`id`, `payer_id`, `recipient_id`) VALUES
(102, 4, 5),
(103, 5, 4),
(104, 4, 5),
(352, 4, 9),
(452, 4, 6),
(502, 4, 6),
(653, 10, 4),
(654, 10, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transaction`
--

CREATE TABLE `transaction` (
  `transaction_type` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL,
  `amount` double NOT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `transaction`
--

INSERT INTO `transaction` (`transaction_type`, `id`, `amount`, `date`) VALUES
('PAYMENT', 102, 1000, '2025-07-03'),
('PAYMENT', 103, 3000, '2025-07-25'),
('PAYMENT', 104, 2000, '2020-10-10'),
('EXPENSE', 152, 30000, '2025-06-30'),
('EXPENSE', 202, 80000, '2023-01-01'),
('EXPENSE', 302, 54687, '2024-07-09'),
('EXPENSE', 303, 33456, '2023-10-10'),
('PAYMENT', 352, 3500, '2023-01-14'),
('EXPENSE', 353, 12345, '2025-05-25'),
('EXPENSE', 402, 23456, '2025-07-26'),
('PAYMENT', 452, 20000, '2025-07-24'),
('PAYMENT', 502, 20000, '2025-07-26'),
('EXPENSE', 552, 10000, '2025-07-25'),
('EXPENSE', 602, 54321, '2023-12-31'),
('EXPENSE', 652, 200000, '2025-07-17'),
('PAYMENT', 653, 33333.333333333336, '2025-07-27'),
('PAYMENT', 654, 20000, '2025-07-27');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transaction_seq`
--

CREATE TABLE `transaction_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `transaction_seq`
--

INSERT INTO `transaction_seq` (`next_val`) VALUES
(751);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `app_user`
--
ALTER TABLE `app_user`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `debt`
--
ALTER TABLE `debt`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK85qkgrfgqf91fm2yhqgq4f1qj` (`creditor_id`),
  ADD KEY `FK6u4t26dkdjkmcn3n1jcdp57x1` (`debtor_id`),
  ADD KEY `FKdcncyn4p6immwf094962pe5ph` (`expense_id`);

--
-- Indices de la tabla `expense`
--
ALTER TABLE `expense`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKbunnqc4q53r6app8fx8fthday` (`payer_id`),
  ADD KEY `FKn8sxfxrqmk6t2kwjvva0fj91d` (`recipient_id`);

--
-- Indices de la tabla `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `app_user`
--
ALTER TABLE `app_user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `debt`
--
ALTER TABLE `debt`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=210;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `debt`
--
ALTER TABLE `debt`
  ADD CONSTRAINT `FK6u4t26dkdjkmcn3n1jcdp57x1` FOREIGN KEY (`debtor_id`) REFERENCES `app_user` (`id`),
  ADD CONSTRAINT `FK85qkgrfgqf91fm2yhqgq4f1qj` FOREIGN KEY (`creditor_id`) REFERENCES `app_user` (`id`),
  ADD CONSTRAINT `FKdcncyn4p6immwf094962pe5ph` FOREIGN KEY (`expense_id`) REFERENCES `expense` (`id`);

--
-- Filtros para la tabla `expense`
--
ALTER TABLE `expense`
  ADD CONSTRAINT `FKpjkh4g79gl5arx0oyjdy63ymy` FOREIGN KEY (`id`) REFERENCES `transaction` (`id`);

--
-- Filtros para la tabla `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `FK3l3of0r80ek6uraurauqighnv` FOREIGN KEY (`id`) REFERENCES `transaction` (`id`),
  ADD CONSTRAINT `FKbunnqc4q53r6app8fx8fthday` FOREIGN KEY (`payer_id`) REFERENCES `app_user` (`id`),
  ADD CONSTRAINT `FKn8sxfxrqmk6t2kwjvva0fj91d` FOREIGN KEY (`recipient_id`) REFERENCES `app_user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
