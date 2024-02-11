-- MySQL dump 10.13  Distrib 8.0.31, for macos12 (x86_64)
--
-- Host: 127.0.0.1    Database: ecommerce
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `marketings`
--

DROP TABLE IF EXISTS `marketings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `marketings` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Marketing ID',
  `marketing_type` varchar(100) COLLATE utf8mb3_bin NOT NULL COMMENT '''ON_SITE''',
  `marketing_placement` varchar(100) COLLATE utf8mb3_bin NOT NULL COMMENT 'HOME_BANNER',
  `advertise_type` varchar(100) COLLATE utf8mb3_bin NOT NULL COMMENT 'BANNER',
  `advertise_value` varchar(256) COLLATE utf8mb3_bin NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `created_by` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `updated_by` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marketings`
--

LOCK TABLES `marketings` WRITE;
/*!40000 ALTER TABLE `marketings` DISABLE KEYS */;
INSERT INTO `marketings` VALUES (1,'ON_SITE','HOME_BANNER','BANNER','https://static.coupangcdn.com/ja/cmg_paperboy/image/1672018161667/221227_C1_%EB%AF%B8%EB%93%9C%EC%8B%9C%EC%A6%8C_%EC%83%88%ED%95%B4_%ED%8A%B9%EA%B0%80%EC%84%B8%EC%9D%BC_SMD-24118_PC.jpg','2022-12-27 15:09:00.000000','sonic','2022-12-27 15:09:00.000000','sonic');
/*!40000 ALTER TABLE `marketings` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-08 21:09:38
