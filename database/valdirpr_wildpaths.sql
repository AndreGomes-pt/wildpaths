-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Tempo de geração: 12-Abr-2023 às 21:02
-- Versão do servidor: 10.4.27-MariaDB
-- versão do PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `valdirpr_wildpaths`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `comment`
--

CREATE TABLE `comment` (
  `commentId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `trailId` int(11) NOT NULL,
  `content` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Extraindo dados da tabela `comment`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `favtrail`
--

CREATE TABLE `favtrail` (
  `favTrailId` int(11) NOT NULL,
  `trailId` int(11) NOT NULL,
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `trail`
--

CREATE TABLE `trail` (
  `trailId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `trailTitle` varchar(50) NOT NULL,
  `trailCatg` varchar(4) NOT NULL DEFAULT '1000',
  `totalDistance` double NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `description` text NOT NULL,
  `trailPic1` mediumblob DEFAULT NULL,
  `trailPic2` mediumblob DEFAULT NULL,
  `trailPic3` mediumblob DEFAULT NULL,
  `trailPic4` mediumblob DEFAULT NULL,
  `dateATrail` datetime NOT NULL DEFAULT current_timestamp(),
  `dateDTrail` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Extraindo dados da tabela `trail`
--


-- --------------------------------------------------------

--
-- Estrutura da tabela `user`
--

CREATE TABLE `user` (
  `userId` int(11) NOT NULL,
  `userName` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `userPw` varchar(500) NOT NULL,
  `dateAUser` datetime NOT NULL DEFAULT current_timestamp(),
  `dataDUser` datetime DEFAULT NULL,
  `userBcPic` mediumblob DEFAULT NULL,
  `userPic` mediumblob DEFAULT NULL,
  `bio` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Extraindo dados da tabela `user`
--

--
-- Índices para tabelas despejadas
--

--
-- Índices para tabela `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`commentId`);

--
-- Índices para tabela `favtrail`
--
ALTER TABLE `favtrail`
  ADD PRIMARY KEY (`favTrailId`);

--
-- Índices para tabela `trail`
--
ALTER TABLE `trail`
  ADD PRIMARY KEY (`trailId`);

--
-- Índices para tabela `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT de tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `comment`
--
ALTER TABLE `comment`
  MODIFY `commentId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de tabela `favtrail`
--
ALTER TABLE `favtrail`
  MODIFY `favTrailId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de tabela `trail`
--
ALTER TABLE `trail`
  MODIFY `trailId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT de tabela `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
