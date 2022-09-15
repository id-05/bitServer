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
  `ugroup` int(5) NOT NULL,
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

INSERT INTO `Users` (`uid`, `uname`, `password`, `ruName`, `ruMiddleName`, `ruFamily`, `role`, `ugroup`, `uTheme`, `hasBlockStudy`, `blockStudy`, `successStudyCount`, `returnStudyCount`, `rating`) VALUES
(40, 'admin', 'admin', 'm', 'in', 'ad', 'admin', 1, 'saga', 0, NULL, 0, 0, '0'),
(41, 'local', 'local', 'c', 'al', 'lo', 'localuser', 1, NULL, 1, '5', 0, 0, '0'),
(42, 'remote', 'remote', 'mo', 'te', 're', 'remoteuser', 38, NULL, 0, '0', 0, 0, '0'),
(43, 'onlyview', 'onlyview', 'ко ', 'просмотр', 'Толь', 'onlyview', 1, NULL, 0, NULL, 0, 0, '0');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`uid`),
  ADD KEY `ugroup` (`ugroup`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `Users`
--
ALTER TABLE `Users`
  MODIFY `uid` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `Users`
--
ALTER TABLE `Users`
  ADD CONSTRAINT `Users_ibfk_1` FOREIGN KEY (`ugroup`) REFERENCES `Usergroup` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
