-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Июн 04 2021 г., 11:42
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
-- Структура таблицы `BitServerResources`
--

CREATE TABLE `BitServerResources` (
  `id` int(20) NOT NULL,
  `rname` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rvalue` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `BitServerResources`
--

INSERT INTO `BitServerResources` (`id`, `rname`, `rvalue`) VALUES
(30, 'orthancaddress', '192.168.1.58'),
(31, 'port', '8042'),
(32, 'login', 'doctor'),
(33, 'password', 'doctor'),
(34, 'pathtojson', '/etc/orthanc/'),
(35, 'pathtoresultfile', 'D:\\results\\'),
(36, 'addressforosimis', '192.168.1.58:8042');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `BitServerResources`
--
ALTER TABLE `BitServerResources`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `BitServerResources`
--
ALTER TABLE `BitServerResources`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
