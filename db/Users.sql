-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Июн 04 2021 г., 11:44
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
-- Структура таблицы `Users`
--

CREATE TABLE `Users` (
  `uid` int(20) NOT NULL,
  `uname` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ruName` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ruMiddleName` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `ruFamily` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `groupUser` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `uTheme` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hasBlockStudy` tinyint(1) NOT NULL DEFAULT 0,
  `blockStudy` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `successStudyCount` int(11) NOT NULL DEFAULT 0,
  `returnStudyCount` int(11) NOT NULL DEFAULT 0,
  `rating` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `Users`
--

INSERT INTO `Users` (`uid`, `uname`, `password`, `ruName`, `ruMiddleName`, `ruFamily`, `role`, `groupUser`, `uTheme`, `hasBlockStudy`, `blockStudy`, `successStudyCount`, `returnStudyCount`, `rating`) VALUES
(17, '6', '6', '6', '6', '6', 'admin', 'Собственные работники', 'saga', 0, '', 0, 0, '0'),
(27, '7', '7', '7', '1', '7', 'localuser', 'Админы', '', 0, '', 0, 0, '0'),
(28, '8', '8', '8', '8', '8', 'remoteuser', 'Собственные работники', '', 1, '3', 0, 0, '0'),
(30, '9', '9', '9', '9', '9', 'client', 'Клиенты', NULL, 0, NULL, 0, 0, '0'),
(35, '1', '1', '1', '1', '1', 'localuser', 'Клиенты', NULL, 0, NULL, 0, 0, '0');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`uid`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `Users`
--
ALTER TABLE `Users`
  MODIFY `uid` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
