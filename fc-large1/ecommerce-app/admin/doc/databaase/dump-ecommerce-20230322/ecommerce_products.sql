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
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `vendor_id` bigint NOT NULL,
  `status` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT 'READY_TO_SELL' COMMENT 'READY_TO_SELL, STOP_SELLING, SELLING, SOLD_OUT',
  `image_url` varchar(500) COLLATE utf8mb3_bin NOT NULL DEFAULT '',
  `image_detail_url` varchar(500) COLLATE utf8mb3_bin NOT NULL,
  `product_desc` varchar(1024) COLLATE utf8mb3_bin NOT NULL,
  `stock_quantity` int NOT NULL DEFAULT '0',
  `delivery_type` varchar(45) COLLATE utf8mb3_bin NOT NULL DEFAULT 'FREE' COMMENT 'FREE, PAID',
  `is_exposed` tinyint(1) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `created_by` varchar(45) COLLATE utf8mb3_bin NOT NULL DEFAULT 'system',
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_by` varchar(45) COLLATE utf8mb3_bin NOT NULL DEFAULT 'system',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'키움정 성장발육 건강기능식품',137000.00,1,'READY_TO_SELL','https://thumbnail6.coupangcdn.com/thumbnails/remote/492x492ex/image/retail/images/9100450745248400-7ea4af35-b5e0-477d-985b-eca669c528c1.jpg','https://thumbnail6.coupangcdn.com/thumbnails/remot…74352155-6015c634-e00c-4fc2-9f4f-bc849927eae0.jpg','원산지: 상품 상세설명 참조\n유통기한: 2023-07-26 이거나 그 이후인 상품\n타입: 알약/캡슐\n영양제 기능: 관절/뼈\n사용대상: 어린이\n쿠팡상품번호: 3325924 - 224845626',0,'FREE',1,0,'2022-12-27 15:15:16.918495','system','2022-12-27 15:15:16.918495','system'),(2,'농심 올리브 짜파게티',4450.00,2,'READY_TO_SELL','https://thumbnail9.coupangcdn.com/thumbnails/remote/492x492ex/image/product/image/vendoritem/2017/01/03/3000979635/ba0d89d3-682d-4342-b317-7243e16aa0d1.jpg','https://thumbnail8.coupangcdn.com/thumbnails/remot…8/175158/b2732f05-4810-407d-967d-08c8db29f3cb.jpg','원산지: 상품 상세설명 참조\n유통기한: 2023-03-16 이거나 그 이후인 상품\n포장형태: 봉지\n라면 맛: 보통맛\n라면 종류: 짜장면\n쿠팡상품번호: 6215299058 - 13447261191',0,'FREE',1,0,'2022-12-27 15:27:22.988329','system','2022-12-27 15:27:22.988329','system'),(3,'하루견과 시그니처 베리스 믹스넛',31401.00,3,'READY_TO_SELL','https://thumbnail6.coupangcdn.com/thumbnails/remote/492x492ex/image/retail/images/3031051098167802-f9f57ea5-3443-439a-a274-78272da06c30.jpg','https://thumbnail8.coupangcdn.com/thumbnails/remot…api/2nsumfpi/4880ed2313404c7ea99ef55f3c529c85.jpg','원산지: 상품 상세설명 참조\n유통기한: 2023-11-01 이거나 그 이후인 상품\n탈각여부: 탈각\n개당 중량: 22g\n총 수량: 50개\n쿠팡상품번호: 6213419047 - 12396603146',0,'FREE',1,0,'2022-12-27 15:27:22.999367','system','2022-12-27 15:27:22.999367','system'),(4,'넛츠랩 오리지널 블루베리 견과',19830.00,4,'READY_TO_SELL','https://thumbnail8.coupangcdn.com/thumbnails/remote/492x492ex/image/retail/images/1125421243187633-e27e423e-64cf-491c-9f14-3f83da18f4fb.jpg','https://thumbnail7.coupangcdn.com/thumbnails/remot…api/1cdq1jfh/796b8c51767f4c15b1500c932804a2ab.jpg','원산지: 상품 상세설명 참조\n유통기한: 2023-02-27 이거나 그 이후인 상품\n개당 중량: 20g\n총 수량: 30개\n포장 타입: 봉지\n쿠팡상품번호: 5373502152 - 7960509239',0,'FREE',1,0,'2022-12-27 15:27:23.006887','system','2022-12-27 15:27:23.006887','system'),(5,'웰킵스 황사방역마스크 대형 KF94',218800.00,9,'READY_TO_SELL','https://thumbnail10.coupangcdn.com/thumbnails/remote/492x492ex/image/retail/images/2320341799804127-1fc9e95b-f09d-4ac4-9314-3aa8712653e3.jpg','	https://thumbnail10.coupangcdn.com/thumbnails/remo…3e5c767e88067c8caf86a5cf3aa9e4faef0a157fec655.jpg','용도: 황사마스크\nKF 등급: KF94\n마스크 사용연령: 대형\n마스크 색상계열: 화이트계열\n사용연령: 성인용\n쿠팡상품번호: 5164468540 - 17271358012',0,'FREE',1,0,'2022-12-27 15:27:23.010401','system','2022-12-27 15:27:23.010401','system'),(6,'웰킵스 리얼블랙 황사마스크 대형 KF94',21240.00,8,'READY_TO_SELL','https://thumbnail10.coupangcdn.com/thumbnails/remote/492x492ex/image/retail/images/13863925431113347-2c741625-dd57-468f-be3b-c9577cd231f1.jpg','	https://thumbnail7.coupangcdn.com/thumbnails/remot…31265575-dc9fc5de-67f1-4a1f-a7d0-475f008d970b.jpg','유통기한: 2024-05-17 이거나 그 이후인 상품\n용도: 황사마스크\nKF 등급: KF94\n마스크 사용연령: 대형\n총 수량: 25개\n쿠팡상품번호: 192235061 - 550018846\n',0,'FREE',1,0,'2022-12-27 15:27:23.012731','system','2022-12-27 15:27:23.012731','system');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-22 23:44:47
