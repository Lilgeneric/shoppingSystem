-- MariaDB dump 10.19  Distrib 10.4.28-MariaDB, for Win64 (AMD64)
--
-- Host: 127.0.0.1    Database: data
-- ------------------------------------------------------
-- Server version	10.4.28-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_users`
--
CREATE DATABASE test;
USE test;
DROP TABLE IF EXISTS `admin_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `locked` int(11) DEFAULT 0,
  `password_attempts` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_users`
--

LOCK TABLES `admin_users` WRITE;
/*!40000 ALTER TABLE `admin_users` DISABLE KEYS */;
INSERT INTO `admin_users` VALUES ('admin','E10ADC3949BA59ABBE56E057F20F883E',0,0);
/*!40000 ALTER TABLE `admin_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cart_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `purchase_time` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (11,3,10,'x754745','2023-08-22 21:43:00'),(14,7,20,'x754745','2023-08-22 21:43:09'),(15,5,300,'x754745','2023-08-22 21:44:06'),(16,1,1,'2','2023-08-22 22:06:34'),(17,1,2,'3','2023-08-22 22:14:26');
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_users`
--

DROP TABLE IF EXISTS `customer_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `user_level` varchar(50) NOT NULL,
  `register_time` varchar(50) NOT NULL,
  `phone_number` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `total_spending` int(11) DEFAULT NULL,
  `password_attempts` int(11) DEFAULT 0,
  `locked` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_users`
--

LOCK TABLES `customer_users` WRITE;
/*!40000 ALTER TABLE `customer_users` DISABLE KEYS */;
INSERT INTO `customer_users` VALUES ('x754754904','e10adc3949ba59abbe56e057f20f883e','铜牌客户','','','',3,NULL,0,0),('x754745904','e10adc3949ba59abbe56e057f20f883e','铜牌客户','','','',4,NULL,0,0),('xyz','e10adc3949ba59abbe56e057f20f883e','铜牌客户','','15176338808','x754745904@163.com',5,NULL,0,0),('3','eccbc87e4b5ce2fe28308fd9f2a7baf3','铜牌客户','1692623959','3','3',8,NULL,0,0),('4','a87ff679a2f3e71d9181a67b7542122c','金牌客户','1692623969','4','4',9,62017,0,0),('5','e4da3b7fbbce2345d7772b0674a318d5','铜牌客户','1692623978','5','5',10,0,0,0),('6','1679091c5a880faf6fb5e6087eb1b2dc','金牌客户','1692623988','6','6',11,186015,0,0),('7','8f14e45fceea167a5a36dedd4bea2543','铜牌客户','1692623998','7','7',12,NULL,0,0),('8','c9f0f895fb98ab9159f51fd0297e236d','金牌客户','1692624008','8','8',13,72000,0,0),('9','45c48cce2e2d7fbdea1afc51c7c6ad26','金牌客户','1692624018','9','9',14,36000,0,0),('11','6512bd43d9caa6e02c990b0a82652dca','铜牌客户','1692692043','','',15,NULL,0,0),('12','c20ad4d76fe97759aa27a0c99bff6710','金牌客户','1692692205','12','12',16,19508,0,0),('xyz111','1dab067c43b4901545997d0265f4836e','铜牌客户','1692702393','651651','654654654',17,NULL,0,0),('x754745','06ebfd2a1c28335ea0063d1a084e370f','铜牌客户','1692702661','15176338808','x754745904@163.com',18,NULL,0,0),('xx777','0caaa57f4736a5297f2321378364cd27','铜牌客户','1692787237','123456','123456',19,NULL,0,0),('test1','2276865ce43549d5684fefb5bda4537c','金牌客户','1692791051','15111111111','x754745904@163.com',20,80000,5,1);
/*!40000 ALTER TABLE `customer_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) NOT NULL,
  `manufacturer` varchar(255) NOT NULL,
  `production_date` bigint(20) NOT NULL,
  `model` varchar(255) NOT NULL,
  `purchase_price` double NOT NULL,
  `retail_price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'手机','苹果',1682870400000,'14',6000,6500,23),(2,'电脑','联想',1667664000000,'y9000p',9000,10000,50),(3,'笔','晨光',1626883200000,'a',0.5,1,9991),(4,'盆','好用的盆',1692720000000,'不锈钢',10,15,0),(5,'华为手机','华为',1692720000000,'p60',6000,8000,7),(6,'苹果','烟台',1692633600000,'富士',5,8,49999),(7,'笔记本电脑','戴尔',1626796800000,'G15 5511',8000,8500,99979),(8,'矿泉水','农夫山泉',1692720000000,'水',1,2,100);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_history`
--

DROP TABLE IF EXISTS `purchase_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `product_id` int(11) NOT NULL,
  `purchase_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `total_amount` double NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_history`
--

LOCK TABLES `purchase_history` WRITE;
/*!40000 ALTER TABLE `purchase_history` DISABLE KEYS */;
INSERT INTO `purchase_history` VALUES (1,'5',1,'2023-08-23 01:47:48',195000,30),(2,'12',3,'2023-08-23 01:51:56',19,19),(3,'12',6,'2023-08-23 01:53:46',8,1),(4,'8',5,'2023-08-23 04:24:36',72000,9),(5,'9',4,'2023-08-23 04:31:21',12000,800),(6,'9',5,'2023-08-23 04:31:21',24000,3),(7,'test1',6,'2023-08-23 11:45:19',80000,10000);
/*!40000 ALTER TABLE `purchase_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-23 19:59:25
