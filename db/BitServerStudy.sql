-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Июн 04 2021 г., 11:43
-- Версия сервера: 10.3.27-MariaDB-0+deb10u1
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

INSERT INTO `BitServerStudy` (`id`, `sid`, `shortid`, `sdescription`, `sdate`, `modality`, `dateaddinbase`, `patientname`, `patientbirthdate`, `patientsex`, `anamnes`, `result`, `typeresult`, `status`, `statusstyle`, `rustatus`, `anonimstudyid`, `userwhosent`, `datesent`, `userwhodiagnost`, `datablock`, `dateresult`, `usergroupwhosees`, `userwhoblock`) VALUES
(1, 'a4b9196d-78740aa4-bb170ac2-db5cb342-bb62a9cd', '9549', 'HEAD', '2021-03-03 15:00:00', 'MR', '2021-06-02 05:01:24', 'ZEMLYANOVA VERA YUR;EVNA', '1965-11-06 15:00:00', 'F', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

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
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
