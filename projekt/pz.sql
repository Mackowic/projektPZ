-- phpMyAdmin SQL Dump
-- version 4.3.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Czas generowania: 06 Kwi 2016, 21:10
-- Wersja serwera: 5.6.24
-- Wersja PHP: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Baza danych: `pz`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `hasla`
--

CREATE TABLE IF NOT EXISTS `hasla` (
  `idHasla` int(10) unsigned NOT NULL,
  `Haslo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `hasla`
--

INSERT INTO `hasla` (`idHasla`, `Haslo`) VALUES
(1, '1'),
(2, '098f6bcd4621d373cade4e832627b4f6');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `projekty`
--

CREATE TABLE IF NOT EXISTS `projekty` (
  `idProjektu` int(10) unsigned NOT NULL,
  `idZadania` int(10) unsigned NOT NULL,
  `idUzytkownika` int(10) unsigned NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `projekty`
--

INSERT INTO `projekty` (`idProjektu`, `idZadania`, `idUzytkownika`) VALUES
(1, 1, 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uprawnienia`
--

CREATE TABLE IF NOT EXISTS `uprawnienia` (
  `idUprawnienia` int(10) unsigned NOT NULL,
  `idUzytkownika` int(10) unsigned NOT NULL,
  `Ranga` varchar(20) DEFAULT NULL,
  `Opis` varchar(255) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `uprawnienia`
--

INSERT INTO `uprawnienia` (`idUprawnienia`, `idUzytkownika`, `Ranga`, `Opis`) VALUES
(1, 1, 'VIP', NULL);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uzytkownicy`
--

CREATE TABLE IF NOT EXISTS `uzytkownicy` (
  `idUzytkownika` int(10) unsigned NOT NULL,
  `idHasla` int(10) unsigned NOT NULL,
  `Nazwisko` varchar(45) DEFAULT NULL,
  `Imie` varchar(45) DEFAULT NULL,
  `Mail` varchar(45) DEFAULT NULL,
  `Stanowisko` varchar(45) DEFAULT NULL,
  `Telefon` int(10) unsigned DEFAULT NULL,
  `Nick` varchar(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `uzytkownicy`
--

INSERT INTO `uzytkownicy` (`idUzytkownika`, `idHasla`, `Nazwisko`, `Imie`, `Mail`, `Stanowisko`, `Telefon`, `Nick`) VALUES
(1, 1, 'Bartkowski', 'Artur', 'arturpro666@gmail.com', 'Programista', 667925375, 'Grunge'),
(7, 2, 'Pardel', 'Przemek', 'Test', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `zadania`
--

CREATE TABLE IF NOT EXISTS `zadania` (
  `idZadania` int(10) unsigned NOT NULL,
  `idUprawnienia` int(10) unsigned NOT NULL,
  `idUzytkownika` int(10) unsigned NOT NULL,
  `Nazwa` varchar(45) DEFAULT NULL,
  `Status_zadania` varchar(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `zadania`
--

INSERT INTO `zadania` (`idZadania`, `idUprawnienia`, `idUzytkownika`, `Nazwa`, `Status_zadania`) VALUES
(1, 1, 1, 'Programowanie Zespolowe', 'Aktywne');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `hasla`
--
ALTER TABLE `hasla`
  ADD PRIMARY KEY (`idHasla`);

--
-- Indexes for table `projekty`
--
ALTER TABLE `projekty`
  ADD PRIMARY KEY (`idProjektu`), ADD KEY `Projekty_FKIndex1` (`idUzytkownika`), ADD KEY `Projekty_FKIndex2` (`idZadania`);

--
-- Indexes for table `uprawnienia`
--
ALTER TABLE `uprawnienia`
  ADD PRIMARY KEY (`idUprawnienia`), ADD KEY `Uprawnienia_FKIndex1` (`idUzytkownika`);

--
-- Indexes for table `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  ADD PRIMARY KEY (`idUzytkownika`), ADD KEY `Uzytkownicy_FKIndex1` (`idHasla`);

--
-- Indexes for table `zadania`
--
ALTER TABLE `zadania`
  ADD PRIMARY KEY (`idZadania`), ADD KEY `Zadania_FKIndex2` (`idUprawnienia`), ADD KEY `Zadania_FKIndex3` (`idUzytkownika`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `hasla`
--
ALTER TABLE `hasla`
  MODIFY `idHasla` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT dla tabeli `projekty`
--
ALTER TABLE `projekty`
  MODIFY `idProjektu` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT dla tabeli `uprawnienia`
--
ALTER TABLE `uprawnienia`
  MODIFY `idUprawnienia` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT dla tabeli `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  MODIFY `idUzytkownika` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT dla tabeli `zadania`
--
ALTER TABLE `zadania`
  MODIFY `idZadania` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `projekty`
--
ALTER TABLE `projekty`
ADD CONSTRAINT `projekty_ibfk_1` FOREIGN KEY (`idUzytkownika`) REFERENCES `uzytkownicy` (`idUzytkownika`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `projekty_ibfk_2` FOREIGN KEY (`idZadania`) REFERENCES `zadania` (`idZadania`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `uprawnienia`
--
ALTER TABLE `uprawnienia`
ADD CONSTRAINT `uprawnienia_ibfk_1` FOREIGN KEY (`idUzytkownika`) REFERENCES `uzytkownicy` (`idUzytkownika`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
ADD CONSTRAINT `uzytkownicy_ibfk_1` FOREIGN KEY (`idHasla`) REFERENCES `hasla` (`idHasla`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `zadania`
--
ALTER TABLE `zadania`
ADD CONSTRAINT `zadania_ibfk_1` FOREIGN KEY (`idUprawnienia`) REFERENCES `uprawnienia` (`idUprawnienia`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `zadania_ibfk_2` FOREIGN KEY (`idUzytkownika`) REFERENCES `uzytkownicy` (`idUzytkownika`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
