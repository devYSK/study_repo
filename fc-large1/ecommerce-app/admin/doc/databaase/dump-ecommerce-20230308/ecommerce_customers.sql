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
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `phone_number` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `grade` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT 'BASIC' COMMENT '''BASIC'', ''VIP''',
  `age` smallint DEFAULT '0',
  `email` varchar(100) COLLATE utf8mb3_bin NOT NULL COMMENT '고객의 이메일',
  `password` varchar(100) COLLATE utf8mb3_bin NOT NULL COMMENT '고객 비밀번호',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `created_by` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_by` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (20,'이철수','010-111-2222','전북 창원시 의창구 의안로 1지 번전북 창원시 의창구 중동 399-8','BASIC',17,'','',0,'2022-12-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(21,'김순자','010-222-3333','전북 창원시 의창구 의안로 4지 번전북 창원시 의창구 소답동 113-20','BASIC',22,'','',0,'2022-11-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(22,'김형순','010-333-4444','전북 창원시 의창구 의안로 6지 번전북 창원시 의창구 소답동 113-2','BASIC',40,'','',0,'2022-05-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(23,'이창동','010-444-5555','전북 창원시 의창구 의안로 7지 번전북 창원시 의창구 중동 401-1','BASIC',28,'','',0,'2022-12-01 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(24,'홍상순','010-555-6666','전북 창원시 의창구 의안로 11지 번전북 창원시 의창구 중동 405','BASIC',15,'','',0,'2022-07-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(25,'금둥이','010-666-7777','전북 창원시 의창구 의안로 15 (성동아파트)지 번전북 창원시 의창구 중동 407-2','BASIC',56,'','',0,'2022-08-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(26,'이지선','010-777-8888','전북 금천시 의창구 의안로 15 (성동아파트)지 번전북 금천시 의창구 중동 407-2','BASIC',60,'','',0,'2022-12-05 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(27,'김지호','010-888-9999','전북 금천시 의창구 의안로 16 (중동상가아파트)지 번전북 금천시 의창구 소답동 111-1','BASIC',33,'','',0,'2022-11-01 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(28,'임민호','010-999-0000','전북 금천시 의창구 의안로 17지 번전북 금천시 의창구 중동 407-4','BASIC',21,'','',0,'2022-11-05 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(29,'권융','010-000-1111','전북 금천시 의창구 의안로 21지 번전북 금천시 의창구 소답동 313-4','BASIC',25,'','',0,'2022-09-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(30,'황상준','010-110-2222','전북 금천시 의창구 중동 447-1','BASIC',32,'','',0,'2022-08-12 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(31,'황희지','010-111-2222','전북 금천시 의창구 의안로 24지 번전북 금천시 의창구 소답동 109-5','BASIC',43,'','',0,'2022-04-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(32,'권은지','010-112-3333','전북 금천시 의창구 의안로 29-1지 번전북 금천시 의창구 중동 448-3','BASIC',54,'','',0,'2022-05-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(33,'김은혜','010-113-4444','전북 금천시 의창구 의안로 30지 번전북 금천시 의창구 소답동 108-2','BASIC',22,'','',0,'2022-06-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(34,'안성수','010-114-5555','전북 금천시 의창구 의안로 31지 번전북 금천시 의창구 중동 448-17','BASIC',21,'','',0,'2022-12-20 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(35,'유지훈','010-115-6666','전북 금천시 의창구 의안로 4지 번전북 금천시 의창구 소답동 113-20','BASIC',44,'','',0,'2022-11-20 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(36,'홍수안','010-116-7777','전북 금천시 의창구 의안로 7지 번전북 금천시 의창구 중동 401-1','BASIC',55,'','',0,'2022-10-05 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(37,'김초이','010-117-8888','전북 금천시 의창구 의안로 15 (성동아파트)지 번전북 금천시 의창구 중동 407-2','BASIC',45,'','',0,'2022-09-20 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(38,'강훈','010-118-9999','전북 금천시 의창구 의안로 21지 번전북 금천시 의창구 소답동 313-4','BASIC',35,'','',0,'2022-08-25 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(39,'오희수','010-201-1111','전북 금천시 의창구 의안로 24지 번전북 금천시 의창구 소답동 109-5','BASIC',65,'','',0,'2022-07-10 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(40,'임지후','010-202-2222','전북 금천시 의창구 의안로 29-1지 번전북 금천시 의창구 중동 448-3','BASIC',27,'','',0,'2022-11-02 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(41,'선우주희','010-203-3333','전북 금천시 의창구 의안로 30지 번전북 금천시 의창구 소답동 108-2','BASIC',29,'','',0,'2022-10-02 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(42,'김선아','010-204-4444','전북 금천시 의창구 의안로 24지 번전북 금천시 의창구 소답동 109-5','BASIC',30,'','',0,'2022-11-27 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic'),(43,'이소희','010-205-5555','전북 금천시 의창구 의안로 1지 번전북 금천시 의창구 중동 399-8','BASIC',40,'','',0,'2022-09-20 00:00:00.000000','sonic','2022-12-10 00:00:00.000000','sonic');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
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
