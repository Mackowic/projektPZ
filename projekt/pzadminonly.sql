-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Czas generowania: 01 Cze 2016, 17:39
-- Wersja serwera: 10.1.9-MariaDB
-- Wersja PHP: 5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `pz`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `hasla`
--

CREATE TABLE `hasla` (
  `idHasla` int(10) UNSIGNED NOT NULL,
  `Haslo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `hasla`
--

INSERT INTO `hasla` (`idHasla`, `Haslo`) VALUES
(1, '21232f297a57a5a743894a0e4a801fc3'),
(2, '71f32ea381dec1963a07ddef07b89d01');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `komentarze`
--

CREATE TABLE `komentarze` (
  `idkomentarza` int(40) NOT NULL,
  `Opis` varchar(255) CHARACTER SET cp1250 COLLATE cp1250_polish_ci NOT NULL,
  `idUzytkownika` int(10) UNSIGNED NOT NULL,
  `idZadania` int(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `projekty`
--

CREATE TABLE `projekty` (
  `idProjektu` int(10) UNSIGNED NOT NULL,
  `idZadania` int(10) UNSIGNED NOT NULL,
  `idUzytkownika` int(10) UNSIGNED NOT NULL,
  `Nazwa` varchar(255) NOT NULL,
  `Opis` varchar(255) NOT NULL,
  `Poczatek` date NOT NULL,
  `Koniec` date NOT NULL,
  `ludzie` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tokens`
--

CREATE TABLE `tokens` (
  `idTokenu` int(10) UNSIGNED NOT NULL,
  `Token` varchar(255) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uprawnienia`
--

CREATE TABLE `uprawnienia` (
  `idUprawnienia` int(10) UNSIGNED NOT NULL,
  `Ranga` varchar(20) DEFAULT NULL,
  `Opis` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;


INSERT INTO `uprawnienia` (`idUprawnienia`, `Ranga`, `Opis`) VALUES
(1, 'VIP', 'Osoba ktora utworzy projekt'),
(2, 'Admin', 'Administrator'),
(3, 'Pracownik', 'Zwykly pracownik');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uzytkownicy`
--

CREATE TABLE `uzytkownicy` (
  `idUzytkownika` int(10) UNSIGNED NOT NULL,
  `idHasla` int(10) UNSIGNED NOT NULL,
  `Nazwisko` varchar(45) DEFAULT NULL,
  `Imie` varchar(45) DEFAULT NULL,
  `Mail` varchar(45) DEFAULT NULL,
  `Stanowisko` varchar(45) DEFAULT NULL,
  `Telefon` int(10) UNSIGNED DEFAULT NULL,
  `ranga` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

INSERT INTO `uzytkownicy` (`idUzytkownika`, `idHasla`, `Nazwisko`, `Imie`, `Mail`, `Stanowisko`, `Telefon`, `ranga`) VALUES
(1, 1, 'Admin', 'Admin', 'admin@ad.min', NULL, 667925375, '2'),
(2, 2, 'Brak uzytkownika', NULL, 'brak@brak.ch', NULL, NULL, '');



-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `zadania`
--

CREATE TABLE `zadania` (
  `idZadania` int(10) UNSIGNED NOT NULL,
  `idUprawnienia` int(10) UNSIGNED NOT NULL,
  `idUzytkownika` int(10) UNSIGNED NOT NULL,
  `Nazwa` varchar(255) DEFAULT NULL,
  `Opis` varchar(255) NOT NULL,
  `Status_zadania` varchar(20) DEFAULT NULL,
  `projekt` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `hasla`
--
ALTER TABLE `hasla`
  ADD PRIMARY KEY (`idHasla`);

--
-- Indexes for table `komentarze`
--
ALTER TABLE `komentarze`
  ADD PRIMARY KEY (`idkomentarza`),
  ADD KEY `idUzytkownika` (`idUzytkownika`),
  ADD KEY `idZadania` (`idZadania`);

--
-- Indexes for table `projekty`
--
ALTER TABLE `projekty`
  ADD PRIMARY KEY (`idProjektu`),
  ADD KEY `Projekty_FKIndex1` (`idUzytkownika`),
  ADD KEY `Projekty_FKIndex2` (`idZadania`);

--
-- Indexes for table `tokens`
--
ALTER TABLE `tokens`
  ADD PRIMARY KEY (`idTokenu`),
  ADD UNIQUE KEY `Token` (`Token`);

--
-- Indexes for table `uprawnienia`
--
ALTER TABLE `uprawnienia`
  ADD PRIMARY KEY (`idUprawnienia`);
 

--
-- Indexes for table `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  ADD PRIMARY KEY (`idUzytkownika`),
  ADD UNIQUE KEY `Mail` (`Mail`),
  ADD KEY `Uzytkownicy_FKIndex1` (`idHasla`);

--
-- Indexes for table `zadania`
--
ALTER TABLE `zadania`
  ADD PRIMARY KEY (`idZadania`),
  ADD KEY `Zadania_FKIndex2` (`idUprawnienia`),
  ADD KEY `Zadania_FKIndex3` (`idUzytkownika`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `hasla`
--
ALTER TABLE `hasla`
  MODIFY `idHasla` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `komentarze`
--
ALTER TABLE `komentarze`
  MODIFY `idkomentarza` int(40) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `projekty`
--
ALTER TABLE `projekty`
  MODIFY `idProjektu` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `tokens`
--
ALTER TABLE `tokens`
  MODIFY `idTokenu` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `uprawnienia`
--
ALTER TABLE `uprawnienia`
  MODIFY `idUprawnienia` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  MODIFY `idUzytkownika` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT dla tabeli `zadania`
--
ALTER TABLE `zadania`
  MODIFY `idZadania` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `komentarze`
--
ALTER TABLE `komentarze`
  ADD CONSTRAINT `komentarze_ibfk_1` FOREIGN KEY (`idUzytkownika`) REFERENCES `uzytkownicy` (`idUzytkownika`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `komentarze_ibfk_2` FOREIGN KEY (`idZadania`) REFERENCES `zadania` (`idZadania`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `projekty`
--
ALTER TABLE `projekty`
  ADD CONSTRAINT `projekty_ibfk_1` FOREIGN KEY (`idUzytkownika`) REFERENCES `uzytkownicy` (`idUzytkownika`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `projekty_ibfk_2` FOREIGN KEY (`idZadania`) REFERENCES `zadania` (`idZadania`) ON DELETE NO ACTION ON UPDATE NO ACTION;


--
-- Ograniczenia dla tabeli `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  ADD CONSTRAINT `uzytkownicy_ibfk_1` FOREIGN KEY (`idHasla`) REFERENCES `hasla` (`idHasla`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `zadania`
--
ALTER TABLE `zadania`
  ADD CONSTRAINT `zadania_ibfk_1` FOREIGN KEY (`idUprawnienia`) REFERENCES `uprawnienia` (`idUprawnienia`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `zadania_ibfk_2` FOREIGN KEY (`idUzytkownika`) REFERENCES `uzytkownicy` (`idUzytkownika`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
