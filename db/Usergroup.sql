-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Июл 12 2021 г., 08:40
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
-- Структура таблицы `Usergroup`
--

CREATE TABLE `Usergroup` (
  `id` int(16) NOT NULL,
  `ruName` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `ruContragent` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `downloadTrue` tinyint(1) NOT NULL DEFAULT 0,
  `forlocal` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `Usergroup`
--

INSERT INTO `Usergroup` (`id`, `ruName`, `status`, `ruContragent`, `downloadTrue`, `forlocal`) VALUES
(1, 'Админы', 'active', 'БИТ-СЕРВИС', 1, 1),
(38, 'Демо группа', 'active', 'Демо', 1, 0),
(39, 'Воронеж', 'inactive', '\"ООО Воронеж\"', 1, 0),
(40, 'Демо 2', 'active', '\"Демо 2\"', 1, 0);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `Usergroup`
--
ALTER TABLE `Usergroup`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `Usergroup`
--
ALTER TABLE `Usergroup`
  MODIFY `id` int(16) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
