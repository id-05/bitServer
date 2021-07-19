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
-- Структура таблицы `BitServerScheduler`
--

CREATE TABLE `BitServerScheduler` (
  `schedulerid` int(11) NOT NULL,
  `usercreateid` int(11) NOT NULL,
  `destinationgroup` int(11) NOT NULL,
  `clock` int(2) DEFAULT 0,
  `minute` int(2) NOT NULL DEFAULT 0,
  `timecondition` varchar(6) COLLATE utf8mb4_unicode_ci NOT NULL,
  `modality` varchar(6) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `BitServerScheduler`
--

INSERT INTO `BitServerScheduler` (`schedulerid`, `usercreateid`, `destinationgroup`, `clock`, `minute`, `timecondition`, `modality`, `source`) VALUES
(46, 0, 40, 11, 8, 'После', 'XA', 'Chita'),
(47, 0, 38, 18, 14, 'После', 'MR', 'Все'),
(48, 0, 40, 16, 0, 'После', 'Все', 'Все');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `BitServerScheduler`
--
ALTER TABLE `BitServerScheduler`
  ADD PRIMARY KEY (`schedulerid`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `BitServerScheduler`
--
ALTER TABLE `BitServerScheduler`
  MODIFY `schedulerid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
