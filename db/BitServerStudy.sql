-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Июл 12 2021 г., 08:39
-- Версия сервера: 10.3.29-MariaDB-0+deb10u1
-- Версия PHP: 7.3.27-1~deb10u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `orthanc`
--

-- --------------------------------------------------------

--
-- Структура таблицы `BitServerStudy`
--

CREATE TABLE `BitServerStudy` (
  `id` int(20) NOT NULL,
  `sid` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shortid` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sdescription` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sdate` datetime NOT NULL,
  `modality` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dateaddinbase` datetime NOT NULL,
  `patientname` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `patientbirthdate` datetime DEFAULT NULL,
  `patientsex` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL,
  `anamnes` blob DEFAULT NULL,
  `result` blob DEFAULT NULL,
  `typeresult` tinyint(1) NOT NULL,
  `status` int(11) NOT NULL,
  `statusstyle` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rustatus` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `anonimstudyid` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `userwhosent` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `datesent` datetime DEFAULT NULL,
  `userwhodiagnost` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `datablock` datetime DEFAULT NULL,
  `dateresult` datetime DEFAULT NULL,
  `usergroupwhosees` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `userwhoblock` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `BitServerStudy`
--

INSERT INTO `BitServerStudy` (`id`, `sid`, `shortid`, `sdescription`, `source`, `sdate`, `modality`, `dateaddinbase`, `patientname`, `patientbirthdate`, `patientsex`, `anamnes`, `result`, `typeresult`, `status`, `statusstyle`, `rustatus`, `anonimstudyid`, `userwhosent`, `datesent`, `userwhodiagnost`, `datablock`, `dateresult`, `usergroupwhosees`, `userwhoblock`) VALUES
(1, '65825c42-156c5242-8ea830b9-40463d2c-54d16ebb', '7345', 'L-SPANE', 'Chita', '2020-08-01 15:00:00', 'MR', '2021-07-05 03:10:05', '-  -    SHEGLOVA ALEKSANDRA MIHAILOVNA', '1945-11-25 15:00:00', 'F', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0),
(2, '693fe349-cc08233c-1b832b0c-4701c830-62c012d9', '7900', 'L-SPINE', 'Chita', '2020-09-08 15:00:00', 'MR', '2021-07-05 03:10:05', 'ALIMAMEDOVA LYUDMILA SEMENOVNA', '1952-01-14 15:00:00', 'F', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0),
(3, '7d9b1f22-c438615d-3c212c81-e6f73008-a12c793e', '9549', 'C-SPINE', 'Chita', '2021-03-03 15:00:00', 'MR', '2021-07-05 03:10:05', 'BEREZHNOVA SVETLANA VASILEVNA', '1967-08-04 15:00:00', 'M', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0),
(4, '7ecade72-1affb716-9f5594cf-579fbd0a-83223b6e', '9649', 'HEAD', 'Chita', '2021-03-14 15:00:00', 'MR', '2021-07-05 03:10:05', 'POPKOVA OLIGA LEONIDOVNA', '1969-10-23 15:00:00', 'F', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0),
(5, 'a4b9196d-78740aa4-bb170ac2-db5cb342-bb62a9cd', '9549', 'HEAD', 'Chita', '2021-03-03 15:00:00', 'MR', '2021-07-05 03:10:05', 'ZEMLYANOVA VERA YUR;EVNA', '1965-11-06 15:00:00', 'F', '', '', 0, 3, 'noresult', 'Не описан', NULL, NULL, NULL, NULL, '2021-07-08 02:29:38', NULL, NULL, 41),
(6, 'c1d79117-3ced44d7-9326fee2-d7ae2882-893091bc', '7561', 'HEAD', 'Chita', '2020-08-16 15:00:00', 'MR', '2021-07-05 03:10:05', 'TIRIN VLADIMIR VITALEVICH', '1998-12-11 15:00:00', 'M', '', 0x443a5c726573756c74732f63316437393131372d33636564343464372d39333236666565322d64376165323838322d38393330393162632e646f6378, 1, 2, 'noresult', 'Не описан', NULL, NULL, NULL, '41', '2021-07-08 02:09:43', '2021-07-08 02:28:57', NULL, 41),
(7, 'dc531b49-8462320d-0b384dde-4b71072f-004d1572', '7561', 'HEAD', 'Chita', '2020-08-02 15:00:00', 'MR', '2021-07-05 03:10:05', 'ALDONIN ALEKSANDR SERGEEVICH', '1938-05-17 15:00:00', 'M', '', 0x443a5c726573756c74732f64633533316234392d38343632333230642d30623338346464652d34623731303732662d30303464313537322e646f6378, 1, 2, 'noresult', 'Не описан', NULL, NULL, NULL, '41', '2021-07-08 00:13:31', '2021-07-08 02:06:58', NULL, 41);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `BitServerStudy`
--
ALTER TABLE `BitServerStudy`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `BitServerStudy`
--
ALTER TABLE `BitServerStudy`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
