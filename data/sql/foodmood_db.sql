CREATE DATABASE  IF NOT EXISTS `foodmood` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `foodmood`;
-- MySQL dump 10.13  Distrib 8.0.46, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: foodmood
-- ------------------------------------------------------
-- Server version	9.7.2

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '7b408fc8-1512-11f0-bc6b-7610f082bc27:1-11184';

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` char(36) NOT NULL,
  `actor_id` char(36) NOT NULL,
  `table_session_id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_cart_actor_table_session` (`actor_id`,`table_session_id`),
  KEY `idx_cart_table_session` (`table_session_id`),
  CONSTRAINT `fk_cart_table_session` FOREIGN KEY (`table_session_id`) REFERENCES `table_session` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `cart_id` char(36) NOT NULL,
  `dish_id` char(36) NOT NULL,
  `product_name` varchar(50) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`cart_id`,`dish_id`),
  KEY `fk_cart_item_dish` (`dish_id`),
  CONSTRAINT `fk_cart_item_cart` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cart_item_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id_dish`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `chk_cart_item_price` CHECK ((`unit_price` > 0)),
  CONSTRAINT `chk_cart_item_quantity` CHECK ((`quantity` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credentials`
--

DROP TABLE IF EXISTS `credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credentials` (
  `user_id` char(36) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_credentials_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credentials`
--

LOCK TABLES `credentials` WRITE;
/*!40000 ALTER TABLE `credentials` DISABLE KEYS */;
INSERT INTO `credentials` VALUES ('4e2d54c5-6c29-4c4f-881b-be0c000f2a32','/XgX/zwxj/TFzMY6rVsZkg==:C9V7ODhfxV3iE+KHS+o2tnr9bR5RHR80cmzQlsKUGz8=','2026-01-04 17:04:04'),('ae15bbef-0cfb-42d9-b8e4-e4bb5eac6a64','ljEsG8BLPvhWcgQ/+X1jsQ==:3f2kkGXRZdP7AATujQL04CK+EpNss8Uxm5FIp0oAi0o=','2026-01-04 17:41:44'),('dd764457-869d-499c-89e8-5a089971bfde','/x9PbJPIfz6NZ4DzvOkMbw==:JUquCP9p64iunDZWIpv8p7sMBc8UzII7y4F63GCCD2c=','2026-01-04 17:40:40');
/*!40000 ALTER TABLE `credentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_order`
--

DROP TABLE IF EXISTS `customer_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_order` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `table_session_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` enum('OPEN','CONFIRMED','SERVED','CANCELLED') NOT NULL DEFAULT 'OPEN',
  PRIMARY KEY (`id`),
  KEY `idx_find_open` (`table_session_id`,`user_id`,`status`),
  KEY `idx_by_table_session` (`table_session_id`),
  CONSTRAINT `fk_order_table_session` FOREIGN KEY (`table_session_id`) REFERENCES `table_session` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_order`
--

LOCK TABLES `customer_order` WRITE;
/*!40000 ALTER TABLE `customer_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_order_line`
--

DROP TABLE IF EXISTS `customer_order_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_order_line` (
  `order_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dish_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `product_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`order_id`,`dish_id`),
  KEY `fk_line_dish` (`dish_id`),
  CONSTRAINT `fk_line_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id_dish`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_line_order` FOREIGN KEY (`order_id`) REFERENCES `customer_order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_price` CHECK ((`unit_price` > 0)),
  CONSTRAINT `chk_qty` CHECK ((`quantity` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_order_line`
--

LOCK TABLES `customer_order_line` WRITE;
/*!40000 ALTER TABLE `customer_order_line` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_order_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dish`
--

DROP TABLE IF EXISTS `dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish` (
  `id_dish` char(36) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `course_type` enum('APPETIZER','FIRST_COURSE','MAIN_COURSE','PIZZA','SIDE_DISH','DESSERT','FRUIT','BEVERAGE') NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_uri` varchar(512) DEFAULT NULL,
  `state` enum('AVAILABLE','UNAVAILABLE') DEFAULT 'AVAILABLE',
  PRIMARY KEY (`id_dish`),
  KEY `idx_dish_name` (`name`),
  KEY `idx_dish_course_type` (`course_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish`
--

LOCK TABLES `dish` WRITE;
/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
INSERT INTO `dish` VALUES ('05008d9b-96c7-485a-9668-494febcab9ba','Pizza Margherita Senza Glutine','Pizza margherita senza glutine','PIZZA',8.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/margherita_senza_glutine.png','AVAILABLE'),('0897046f-c9d3-4b40-850d-2aac6dca922d','Tiramisù Classico',NULL,'DESSERT',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/tiramisu.png','AVAILABLE'),('0925245a-e707-4182-9225-9e83de8c00ae','Panna cotta frutti di bosco',NULL,'DESSERT',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/panna_cotta_fruttidibosco.png','AVAILABLE'),('09d4cfcb-8315-4fd2-8a43-faf2b70751e0','Verdure grigliate',NULL,'SIDE_DISH',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/verdure_grigliate.png','AVAILABLE'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','Fettuccine ai funghi',NULL,'FIRST_COURSE',10.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/fettuccine_funghi.png','AVAILABLE'),('12ae040b-9041-42dd-beeb-7fa14cde5455','Bruschette al pomodoro',NULL,'APPETIZER',4.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/bruschette_al_pomodoro.png','AVAILABLE'),('1d8babc4-9351-4d00-a9bf-1312d16a9029','Cotoletta di Pollo',NULL,'MAIN_COURSE',12.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/cotoletta_pollo.png','AVAILABLE'),('1f49e974-886f-4780-b7e7-9954e95eccb8','Pizza Margherita',NULL,'PIZZA',8.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/pizza_margherita.png','AVAILABLE'),('23656355-c655-448b-adac-0beef47fc439','Torta al cioccolato vegana',NULL,'DESSERT',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/torta_cioccolato_vegana.png','AVAILABLE'),('29317bdb-c98f-4c96-97ae-57bcb17372b2','Pasta alla norma',NULL,'FIRST_COURSE',8.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/pasta_alla_norma.png','AVAILABLE'),('29d05959-f615-4e13-b4b7-ef6e1b695397','Macedonia arancia',NULL,'FRUIT',2.00,NULL,'UNAVAILABLE'),('2d405676-15eb-4a22-8b21-454e8f29b568','Caffè Decaffinato',NULL,'BEVERAGE',1.30,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/caffè.png','AVAILABLE'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','Lasagna',NULL,'FIRST_COURSE',9.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/lasagna.png','AVAILABLE'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','Tigelle vegane','Soffici tigelle vegane.','APPETIZER',7.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/tigella_vegana.png','AVAILABLE'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','Pizza Ortolana',NULL,'PIZZA',7.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/pizza_ortolana.png','AVAILABLE'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','Pizzette di patate',NULL,'APPETIZER',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/pizzette_di_patate.png','AVAILABLE'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','Macedonia di frutta','Selezione di frutta fresca di stagione tagliata a cubetti','FRUIT',4.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/macedonia_frutta.png','AVAILABLE'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','Pizza Marinara','Pizza marinara','PIZZA',6.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/pizza_marinara.png','AVAILABLE'),('7379516e-3025-46ff-a447-d88fd82d136d','Patatine Fritte','Patatine fritte, croccanti fuori e morbide dentro','SIDE_DISH',4.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/patate_fritte.png','AVAILABLE'),('7582e403-0b3b-4ab0-b922-a324f6ee0719','Melanzane Grigliate',NULL,'SIDE_DISH',4.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/melanzane_grigliate.png','AVAILABLE'),('7953db75-1847-41fe-ba78-3fbab2d87a75','Prosciutto crudo e bufala',NULL,'APPETIZER',4.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/prosciutto_bufala.png','AVAILABLE'),('7fcc8dc6-684c-4988-a066-561b0a54fa0c','Birra Chiara alla Spina','Birra chiara alla spina, servita fredda','BEVERAGE',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/birra_spina.png','AVAILABLE'),('8224c8fe-ebf4-4319-b850-b8fef7e9908d','Risotto ai funghi porcini',NULL,'FIRST_COURSE',10.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/risotto_funghi.png','AVAILABLE'),('8272e17b-0c48-4da4-9c7b-885c5fe307d8','Caffè',NULL,'BEVERAGE',1.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/caffè.png','AVAILABLE'),('8494df1e-4429-49cc-8e31-558e1a0259cf','Cheesecake frutti di bosco',NULL,'DESSERT',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/cheesecake_fruttidibosco.png','AVAILABLE'),('8599d9a4-ef94-41fc-bae5-3c177850f316','Caffè Macchiato',NULL,'BEVERAGE',1.20,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/caffè_macchiato.png','AVAILABLE'),('88adc407-f91f-4c12-8328-75a12d4fe01a','Coca Cola',NULL,'BEVERAGE',3.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/cocacola.png','AVAILABLE'),('89f77840-583c-44ce-aea2-71f5f979afd4','Crostini ai funghi',NULL,'APPETIZER',4.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/crostini_funghi.png','AVAILABLE'),('8f0e5345-c0ce-4983-8a9e-38f0cbaf20f4','Parmigiana di melanzane',NULL,'MAIN_COURSE',12.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/parmigiana.png','AVAILABLE'),('943e978b-5550-4a6f-be94-09a3dbb6109b','Supplì',NULL,'APPETIZER',1.20,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/supplì.png','AVAILABLE'),('96726503-79ad-43ab-b85d-7677d4b85966','Pizza Diavola',NULL,'PIZZA',9.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/pizza_diavola.png','AVAILABLE'),('9a62f0b7-1b3b-41a5-b1c3-d5112cc07a9a','Hamburger di Manzo',NULL,'MAIN_COURSE',12.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/hamburger_manzo.png','AVAILABLE'),('9c815641-f078-4abe-a565-a72af58f5755','Panna Cotta',NULL,'DESSERT',4.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/panna_cotta_caramello.png','AVAILABLE'),('a8538f4f-16b2-4068-a997-028092c80f32','Pasta e ceci',NULL,'FIRST_COURSE',8.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/pasta_ceci.png','AVAILABLE'),('a89f272f-3021-4028-8ee6-2c63677fd874','Fagiolini con patate',NULL,'SIDE_DISH',4.50,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/fagiolini_patate.png','AVAILABLE'),('a990fc91-cb8e-40a9-8797-e0564531708d','Torta di mele',NULL,'DESSERT',4.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/torta_mele.png','AVAILABLE'),('b50d4e28-1e63-4a4f-a1a6-d1d6e317f1c0','Coca Cola Zero 330 ml',NULL,'BEVERAGE',2.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/cocacolazero.png','AVAILABLE'),('b50f4d58-18a0-4b7e-afea-f181c33fa0eb','Mousse al cioccolato',NULL,'DESSERT',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/mousse_cioccolato.png','AVAILABLE'),('bb8af6d6-d81a-4b6c-a6e7-a725ad669d18','Paccheri al pomodoro',NULL,'FIRST_COURSE',8.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/paccheri_al_pomodoro.png','AVAILABLE'),('c220ae75-b857-40eb-b1f0-7595e41e3fbd','Sorbetto al limone',NULL,'DESSERT',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/sorbetto_al_limone.png','AVAILABLE'),('c6f932fd-2683-4b4b-924a-fb5db9f11b80','Patate al forno',NULL,'SIDE_DISH',5.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/patate_forno.png','AVAILABLE'),('d094bb7e-4e22-41d3-8246-76b97d3da7e2','Acqua Panna',NULL,'BEVERAGE',1.50,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/acqua_panna.png','AVAILABLE'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','Tagliere di salumi e formaggi','Tagliere misto di salumi e formaggi.','APPETIZER',9.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/tagliere_di_montagna.png','AVAILABLE'),('e73f9102-71f9-4c0f-b010-a7eae9dcb563','Spaghetti aglio, olio e peperoncino',NULL,'FIRST_COURSE',9.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/spaghetti_aglio_olio.png','AVAILABLE'),('eb1c065e-e61b-4054-ac4c-95b43e4ca25d','Spaghetti Carbonara',NULL,'FIRST_COURSE',11.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/spaghetti_carbonara.png','AVAILABLE'),('ebf0acdc-56f9-49d9-95c4-76a9d3e17eeb','Tagliata di manzo',NULL,'MAIN_COURSE',16.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/tagliata_manzo.png','AVAILABLE'),('eca7ef0b-089d-4532-b8e6-cbfc34bdf876','Petto di pollo al limone',NULL,'MAIN_COURSE',11.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/petto_di_pollo.png','AVAILABLE'),('ee625b10-a368-40df-86e9-4375a6772bfd','Tagliere vegano','Tagliere vegano da dividere in compagnia.','APPETIZER',8.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/tagliere_vegano.png','AVAILABLE'),('febed3e5-f811-41f0-a53e-1479b18a5080','Amatriciana',NULL,'FIRST_COURSE',9.00,'file:/Users/gabrielemonti/dev/Progetto-ISPW/src/main/resources/product_img/amatriciana.png','AVAILABLE');
/*!40000 ALTER TABLE `dish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dish_diet_category`
--

DROP TABLE IF EXISTS `dish_diet_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish_diet_category` (
  `id_dish` char(36) NOT NULL,
  `diet_category` enum('TRADITIONAL','VEGETARIAN','VEGAN','GLUTEN_FREE','LACTOSE_FREE') NOT NULL,
  PRIMARY KEY (`id_dish`,`diet_category`),
  KEY `idx_ddc_category` (`diet_category`),
  CONSTRAINT `fk_ddc_dish` FOREIGN KEY (`id_dish`) REFERENCES `dish` (`id_dish`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_diet_category`
--

LOCK TABLES `dish_diet_category` WRITE;
/*!40000 ALTER TABLE `dish_diet_category` DISABLE KEYS */;
INSERT INTO `dish_diet_category` VALUES ('05008d9b-96c7-485a-9668-494febcab9ba','TRADITIONAL'),('0897046f-c9d3-4b40-850d-2aac6dca922d','TRADITIONAL'),('0925245a-e707-4182-9225-9e83de8c00ae','TRADITIONAL'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','TRADITIONAL'),('1d8babc4-9351-4d00-a9bf-1312d16a9029','TRADITIONAL'),('1f49e974-886f-4780-b7e7-9954e95eccb8','TRADITIONAL'),('29317bdb-c98f-4c96-97ae-57bcb17372b2','TRADITIONAL'),('29d05959-f615-4e13-b4b7-ef6e1b695397','TRADITIONAL'),('2d405676-15eb-4a22-8b21-454e8f29b568','TRADITIONAL'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','TRADITIONAL'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','TRADITIONAL'),('7379516e-3025-46ff-a447-d88fd82d136d','TRADITIONAL'),('7953db75-1847-41fe-ba78-3fbab2d87a75','TRADITIONAL'),('7fcc8dc6-684c-4988-a066-561b0a54fa0c','TRADITIONAL'),('8224c8fe-ebf4-4319-b850-b8fef7e9908d','TRADITIONAL'),('8272e17b-0c48-4da4-9c7b-885c5fe307d8','TRADITIONAL'),('8494df1e-4429-49cc-8e31-558e1a0259cf','TRADITIONAL'),('8599d9a4-ef94-41fc-bae5-3c177850f316','TRADITIONAL'),('88adc407-f91f-4c12-8328-75a12d4fe01a','TRADITIONAL'),('89f77840-583c-44ce-aea2-71f5f979afd4','TRADITIONAL'),('8f0e5345-c0ce-4983-8a9e-38f0cbaf20f4','TRADITIONAL'),('943e978b-5550-4a6f-be94-09a3dbb6109b','TRADITIONAL'),('96726503-79ad-43ab-b85d-7677d4b85966','TRADITIONAL'),('9a62f0b7-1b3b-41a5-b1c3-d5112cc07a9a','TRADITIONAL'),('9c815641-f078-4abe-a565-a72af58f5755','TRADITIONAL'),('a990fc91-cb8e-40a9-8797-e0564531708d','TRADITIONAL'),('b50d4e28-1e63-4a4f-a1a6-d1d6e317f1c0','TRADITIONAL'),('b50f4d58-18a0-4b7e-afea-f181c33fa0eb','TRADITIONAL'),('bb8af6d6-d81a-4b6c-a6e7-a725ad669d18','TRADITIONAL'),('d094bb7e-4e22-41d3-8246-76b97d3da7e2','TRADITIONAL'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','TRADITIONAL'),('eb1c065e-e61b-4054-ac4c-95b43e4ca25d','TRADITIONAL'),('ebf0acdc-56f9-49d9-95c4-76a9d3e17eeb','TRADITIONAL'),('eca7ef0b-089d-4532-b8e6-cbfc34bdf876','TRADITIONAL'),('febed3e5-f811-41f0-a53e-1479b18a5080','TRADITIONAL'),('05008d9b-96c7-485a-9668-494febcab9ba','VEGETARIAN'),('0897046f-c9d3-4b40-850d-2aac6dca922d','VEGETARIAN'),('0925245a-e707-4182-9225-9e83de8c00ae','VEGETARIAN'),('09d4cfcb-8315-4fd2-8a43-faf2b70751e0','VEGETARIAN'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','VEGETARIAN'),('12ae040b-9041-42dd-beeb-7fa14cde5455','VEGETARIAN'),('1f49e974-886f-4780-b7e7-9954e95eccb8','VEGETARIAN'),('23656355-c655-448b-adac-0beef47fc439','VEGETARIAN'),('29317bdb-c98f-4c96-97ae-57bcb17372b2','VEGETARIAN'),('29d05959-f615-4e13-b4b7-ef6e1b695397','VEGETARIAN'),('2d405676-15eb-4a22-8b21-454e8f29b568','VEGETARIAN'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','VEGETARIAN'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','VEGETARIAN'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','VEGETARIAN'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','VEGETARIAN'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','VEGETARIAN'),('7379516e-3025-46ff-a447-d88fd82d136d','VEGETARIAN'),('7582e403-0b3b-4ab0-b922-a324f6ee0719','VEGETARIAN'),('7fcc8dc6-684c-4988-a066-561b0a54fa0c','VEGETARIAN'),('8224c8fe-ebf4-4319-b850-b8fef7e9908d','VEGETARIAN'),('8494df1e-4429-49cc-8e31-558e1a0259cf','VEGETARIAN'),('8599d9a4-ef94-41fc-bae5-3c177850f316','VEGETARIAN'),('88adc407-f91f-4c12-8328-75a12d4fe01a','VEGETARIAN'),('89f77840-583c-44ce-aea2-71f5f979afd4','VEGETARIAN'),('8f0e5345-c0ce-4983-8a9e-38f0cbaf20f4','VEGETARIAN'),('9c815641-f078-4abe-a565-a72af58f5755','VEGETARIAN'),('a8538f4f-16b2-4068-a997-028092c80f32','VEGETARIAN'),('a89f272f-3021-4028-8ee6-2c63677fd874','VEGETARIAN'),('a990fc91-cb8e-40a9-8797-e0564531708d','VEGETARIAN'),('b50d4e28-1e63-4a4f-a1a6-d1d6e317f1c0','VEGETARIAN'),('b50f4d58-18a0-4b7e-afea-f181c33fa0eb','VEGETARIAN'),('bb8af6d6-d81a-4b6c-a6e7-a725ad669d18','VEGETARIAN'),('c220ae75-b857-40eb-b1f0-7595e41e3fbd','VEGETARIAN'),('c6f932fd-2683-4b4b-924a-fb5db9f11b80','VEGETARIAN'),('d094bb7e-4e22-41d3-8246-76b97d3da7e2','VEGETARIAN'),('e73f9102-71f9-4c0f-b010-a7eae9dcb563','VEGETARIAN'),('ee625b10-a368-40df-86e9-4375a6772bfd','VEGETARIAN'),('09d4cfcb-8315-4fd2-8a43-faf2b70751e0','VEGAN'),('12ae040b-9041-42dd-beeb-7fa14cde5455','VEGAN'),('23656355-c655-448b-adac-0beef47fc439','VEGAN'),('29d05959-f615-4e13-b4b7-ef6e1b695397','VEGAN'),('2d405676-15eb-4a22-8b21-454e8f29b568','VEGAN'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','VEGAN'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','VEGAN'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','VEGAN'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','VEGAN'),('7379516e-3025-46ff-a447-d88fd82d136d','VEGAN'),('7582e403-0b3b-4ab0-b922-a324f6ee0719','VEGAN'),('88adc407-f91f-4c12-8328-75a12d4fe01a','VEGAN'),('a8538f4f-16b2-4068-a997-028092c80f32','VEGAN'),('a89f272f-3021-4028-8ee6-2c63677fd874','VEGAN'),('b50d4e28-1e63-4a4f-a1a6-d1d6e317f1c0','VEGAN'),('bb8af6d6-d81a-4b6c-a6e7-a725ad669d18','VEGAN'),('c220ae75-b857-40eb-b1f0-7595e41e3fbd','VEGAN'),('c6f932fd-2683-4b4b-924a-fb5db9f11b80','VEGAN'),('d094bb7e-4e22-41d3-8246-76b97d3da7e2','VEGAN'),('e73f9102-71f9-4c0f-b010-a7eae9dcb563','VEGAN'),('ee625b10-a368-40df-86e9-4375a6772bfd','VEGAN'),('05008d9b-96c7-485a-9668-494febcab9ba','GLUTEN_FREE'),('2d405676-15eb-4a22-8b21-454e8f29b568','GLUTEN_FREE'),('7379516e-3025-46ff-a447-d88fd82d136d','GLUTEN_FREE'),('7953db75-1847-41fe-ba78-3fbab2d87a75','GLUTEN_FREE'),('88adc407-f91f-4c12-8328-75a12d4fe01a','GLUTEN_FREE'),('b50d4e28-1e63-4a4f-a1a6-d1d6e317f1c0','GLUTEN_FREE'),('b50f4d58-18a0-4b7e-afea-f181c33fa0eb','GLUTEN_FREE'),('c220ae75-b857-40eb-b1f0-7595e41e3fbd','GLUTEN_FREE'),('d094bb7e-4e22-41d3-8246-76b97d3da7e2','GLUTEN_FREE'),('ebf0acdc-56f9-49d9-95c4-76a9d3e17eeb','GLUTEN_FREE'),('eca7ef0b-089d-4532-b8e6-cbfc34bdf876','GLUTEN_FREE'),('09d4cfcb-8315-4fd2-8a43-faf2b70751e0','LACTOSE_FREE'),('12ae040b-9041-42dd-beeb-7fa14cde5455','LACTOSE_FREE'),('23656355-c655-448b-adac-0beef47fc439','LACTOSE_FREE'),('29d05959-f615-4e13-b4b7-ef6e1b695397','LACTOSE_FREE'),('2d405676-15eb-4a22-8b21-454e8f29b568','LACTOSE_FREE'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','LACTOSE_FREE'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','LACTOSE_FREE'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','LACTOSE_FREE'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','LACTOSE_FREE'),('7379516e-3025-46ff-a447-d88fd82d136d','LACTOSE_FREE'),('7582e403-0b3b-4ab0-b922-a324f6ee0719','LACTOSE_FREE'),('7fcc8dc6-684c-4988-a066-561b0a54fa0c','LACTOSE_FREE'),('88adc407-f91f-4c12-8328-75a12d4fe01a','LACTOSE_FREE'),('a8538f4f-16b2-4068-a997-028092c80f32','LACTOSE_FREE'),('a89f272f-3021-4028-8ee6-2c63677fd874','LACTOSE_FREE'),('b50d4e28-1e63-4a4f-a1a6-d1d6e317f1c0','LACTOSE_FREE'),('bb8af6d6-d81a-4b6c-a6e7-a725ad669d18','LACTOSE_FREE'),('c220ae75-b857-40eb-b1f0-7595e41e3fbd','LACTOSE_FREE'),('c6f932fd-2683-4b4b-924a-fb5db9f11b80','LACTOSE_FREE'),('d094bb7e-4e22-41d3-8246-76b97d3da7e2','LACTOSE_FREE'),('e73f9102-71f9-4c0f-b010-a7eae9dcb563','LACTOSE_FREE'),('ebf0acdc-56f9-49d9-95c4-76a9d3e17eeb','LACTOSE_FREE'),('eca7ef0b-089d-4532-b8e6-cbfc34bdf876','LACTOSE_FREE'),('ee625b10-a368-40df-86e9-4375a6772bfd','LACTOSE_FREE');
/*!40000 ALTER TABLE `dish_diet_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dish_ingredient`
--

DROP TABLE IF EXISTS `dish_ingredient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish_ingredient` (
  `id_dish` char(36) NOT NULL,
  `ingredient_name` varchar(50) NOT NULL,
  `quantity` decimal(10,2) NOT NULL,
  `unit` enum('GRAM','MILLILITER') NOT NULL,
  PRIMARY KEY (`id_dish`,`ingredient_name`),
  KEY `idx_di_ingredient` (`ingredient_name`),
  CONSTRAINT `fk_di_dish` FOREIGN KEY (`id_dish`) REFERENCES `dish` (`id_dish`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_di_ingredient` FOREIGN KEY (`ingredient_name`) REFERENCES `ingredient` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_ingredient`
--

LOCK TABLES `dish_ingredient` WRITE;
/*!40000 ALTER TABLE `dish_ingredient` DISABLE KEYS */;
INSERT INTO `dish_ingredient` VALUES ('05008d9b-96c7-485a-9668-494febcab9ba','FARINA SENZA GLUTINE',140.00,'GRAM'),('05008d9b-96c7-485a-9668-494febcab9ba','Mozzarella',60.00,'GRAM'),('05008d9b-96c7-485a-9668-494febcab9ba','Olio EVO',10.00,'MILLILITER'),('05008d9b-96c7-485a-9668-494febcab9ba','Passata di pomodoro',17.00,'GRAM'),('0897046f-c9d3-4b40-850d-2aac6dca922d','Cacao',5.00,'GRAM'),('0897046f-c9d3-4b40-850d-2aac6dca922d','Caffè',60.00,'MILLILITER'),('0897046f-c9d3-4b40-850d-2aac6dca922d','Mascarpone',40.00,'GRAM'),('0897046f-c9d3-4b40-850d-2aac6dca922d','Savoiardi',30.00,'GRAM'),('0897046f-c9d3-4b40-850d-2aac6dca922d','Uova',25.00,'GRAM'),('0897046f-c9d3-4b40-850d-2aac6dca922d','Zucchero',15.00,'GRAM'),('0925245a-e707-4182-9225-9e83de8c00ae','FRUTTI DI BOSCO',20.00,'GRAM'),('0925245a-e707-4182-9225-9e83de8c00ae','GELATINA',2.00,'GRAM'),('0925245a-e707-4182-9225-9e83de8c00ae','Panna',60.00,'GRAM'),('0925245a-e707-4182-9225-9e83de8c00ae','Zucchero',20.00,'GRAM'),('09d4cfcb-8315-4fd2-8a43-faf2b70751e0','Melanzane',50.00,'GRAM'),('09d4cfcb-8315-4fd2-8a43-faf2b70751e0','Peperoni',50.00,'GRAM'),('09d4cfcb-8315-4fd2-8a43-faf2b70751e0','Zucchine',50.00,'GRAM'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','Funghi',60.00,'GRAM'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','Olio EVO',3.00,'MILLILITER'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','Panna',30.00,'GRAM'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','PARMIGIANO',10.00,'GRAM'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','PASTA DI GRANO DURO',45.00,'GRAM'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','PEPE NERO MACINATO',0.30,'GRAM'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','Prezzemolo',3.00,'GRAM'),('0efb45a1-5a0d-417e-b35f-46fcbe15a40e','SALE DA CUCINA',1.00,'GRAM'),('12ae040b-9041-42dd-beeb-7fa14cde5455','Aglio',1.00,'GRAM'),('12ae040b-9041-42dd-beeb-7fa14cde5455','Olio EVO',4.00,'MILLILITER'),('12ae040b-9041-42dd-beeb-7fa14cde5455','Pane',80.00,'GRAM'),('12ae040b-9041-42dd-beeb-7fa14cde5455','Pomodorini',200.00,'GRAM'),('1d8babc4-9351-4d00-a9bf-1312d16a9029','Limone',5.00,'MILLILITER'),('1d8babc4-9351-4d00-a9bf-1312d16a9029','Olio di semi',10.00,'MILLILITER'),('1d8babc4-9351-4d00-a9bf-1312d16a9029','Pangrattato',25.00,'GRAM'),('1d8babc4-9351-4d00-a9bf-1312d16a9029','PARMIGIANO',10.00,'GRAM'),('1d8babc4-9351-4d00-a9bf-1312d16a9029','Petto di pollo',150.00,'GRAM'),('1d8babc4-9351-4d00-a9bf-1312d16a9029','Sale',2.00,'GRAM'),('1d8babc4-9351-4d00-a9bf-1312d16a9029','Uova',30.00,'GRAM'),('1f49e974-886f-4780-b7e7-9954e95eccb8','Acqua',90.00,'MILLILITER'),('1f49e974-886f-4780-b7e7-9954e95eccb8','Basilico',2.00,'GRAM'),('1f49e974-886f-4780-b7e7-9954e95eccb8','FARINA',100.00,'GRAM'),('1f49e974-886f-4780-b7e7-9954e95eccb8','Lievito',3.00,'GRAM'),('1f49e974-886f-4780-b7e7-9954e95eccb8','Mozzarella',60.00,'GRAM'),('1f49e974-886f-4780-b7e7-9954e95eccb8','Olio EVO',10.00,'MILLILITER'),('1f49e974-886f-4780-b7e7-9954e95eccb8','Passata di pomodoro',80.00,'GRAM'),('1f49e974-886f-4780-b7e7-9954e95eccb8','Sale',3.00,'GRAM'),('23656355-c655-448b-adac-0beef47fc439','Cacao',10.00,'GRAM'),('23656355-c655-448b-adac-0beef47fc439','FARINA',30.00,'GRAM'),('23656355-c655-448b-adac-0beef47fc439','LIEVITO DI BIRRA SECCO',3.00,'GRAM'),('23656355-c655-448b-adac-0beef47fc439','Zucchero',20.00,'GRAM'),('29317bdb-c98f-4c96-97ae-57bcb17372b2','Melanzane',80.00,'GRAM'),('29317bdb-c98f-4c96-97ae-57bcb17372b2','Olio EVO',10.00,'MILLILITER'),('29317bdb-c98f-4c96-97ae-57bcb17372b2','PASTA DI GRANO DURO',45.00,'GRAM'),('29317bdb-c98f-4c96-97ae-57bcb17372b2','POMODORO',150.00,'GRAM'),('29317bdb-c98f-4c96-97ae-57bcb17372b2','Ricotta',25.00,'GRAM'),('29d05959-f615-4e13-b4b7-ef6e1b695397','Arancia',100.00,'GRAM'),('2d405676-15eb-4a22-8b21-454e8f29b568','Caffè',30.00,'MILLILITER'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','Burro',7.00,'GRAM'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','FARINA',7.00,'GRAM'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','LATTE',50.00,'GRAM'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','Olio EVO',5.00,'MILLILITER'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','PARMIGIANO',25.00,'GRAM'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','Passata di pomodoro',120.00,'GRAM'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','PASTA DI GRANO DURO',50.00,'GRAM'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','PEPE NERO MACINATO',0.20,'GRAM'),('3a845f5b-5608-4c28-b8cd-8a7cf51e3468','Sale',2.00,'GRAM'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','FARINA',30.00,'GRAM'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','LATTE DI SOIA',30.00,'GRAM'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','LIEVITO DI BIRRA SECCO',1.40,'GRAM'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','Olio EVO',6.00,'MILLILITER'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','Pomodorini',30.00,'GRAM'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','Sale',2.00,'GRAM'),('3ca87f2b-d726-4dba-af16-bc861db1e85f','Zucchine',30.00,'GRAM'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','Acqua',100.00,'MILLILITER'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','FARINA',120.00,'GRAM'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','Lievito',1.00,'GRAM'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','Melanzane',50.00,'GRAM'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','Olio EVO',10.00,'MILLILITER'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','Peperoni',50.00,'GRAM'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','Pomodorini',50.00,'GRAM'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','Sale',1.00,'GRAM'),('48aed47d-86f7-46a3-87c5-a407e9a766d4','Zucchine',50.00,'GRAM'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','Basilico',1.00,'GRAM'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','FORMAGGIO GRATTUGIATO',55.00,'GRAM'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','Olio EVO',7.00,'MILLILITER'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','Origano',1.00,'GRAM'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','Passata di pomodoro',60.00,'GRAM'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','Patate',250.00,'GRAM'),('5176cfe1-d5a4-4df7-91c5-5dfa1e4e0cc4','Sale',1.00,'GRAM'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','Arancia',60.00,'GRAM'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','Banana',50.00,'GRAM'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','Limone',5.00,'MILLILITER'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','Mela',60.00,'GRAM'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','Pera',50.00,'GRAM'),('58ed60a3-4f2e-4e78-ba16-7c740905e3d9','Zucchero',5.00,'GRAM'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','Acqua',100.00,'MILLILITER'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','Aglio',1.00,'GRAM'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','FARINA',130.00,'GRAM'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','LIEVITO DI BIRRA SECCO',1.00,'GRAM'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','Olio EVO',12.00,'MILLILITER'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','Origano',1.00,'GRAM'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','Passata di pomodoro',90.00,'GRAM'),('72062b9d-2941-4b19-b6a6-5e8c592dc7a6','Sale',3.00,'GRAM'),('7379516e-3025-46ff-a447-d88fd82d136d','Olio di semi',25.00,'MILLILITER'),('7379516e-3025-46ff-a447-d88fd82d136d','Patate',200.00,'GRAM'),('7379516e-3025-46ff-a447-d88fd82d136d','Sale',2.00,'GRAM'),('7582e403-0b3b-4ab0-b922-a324f6ee0719','Aglio',2.00,'GRAM'),('7582e403-0b3b-4ab0-b922-a324f6ee0719','Melanzane',200.00,'GRAM'),('7582e403-0b3b-4ab0-b922-a324f6ee0719','Olio EVO',10.00,'MILLILITER'),('7582e403-0b3b-4ab0-b922-a324f6ee0719','PEPE NERO MACINATO',0.30,'GRAM'),('7582e403-0b3b-4ab0-b922-a324f6ee0719','SALE DA CUCINA',1.50,'GRAM'),('7953db75-1847-41fe-ba78-3fbab2d87a75','MOZZARELLA DI BUFALA',50.00,'GRAM'),('7953db75-1847-41fe-ba78-3fbab2d87a75','PROSCIUTTO CRUDO',50.00,'GRAM'),('7fcc8dc6-684c-4988-a066-561b0a54fa0c','BIRRA',300.00,'GRAM'),('8224c8fe-ebf4-4319-b850-b8fef7e9908d','Burro',20.00,'GRAM'),('8224c8fe-ebf4-4319-b850-b8fef7e9908d','Funghi',120.00,'GRAM'),('8224c8fe-ebf4-4319-b850-b8fef7e9908d','PARMIGIANO',20.00,'GRAM'),('8224c8fe-ebf4-4319-b850-b8fef7e9908d','Riso Carnaroli',80.00,'GRAM'),('8272e17b-0c48-4da4-9c7b-885c5fe307d8','Caffè',30.00,'MILLILITER'),('8494df1e-4429-49cc-8e31-558e1a0259cf','Burro',10.00,'GRAM'),('8494df1e-4429-49cc-8e31-558e1a0259cf','FRUTTI DI BOSCO',50.00,'GRAM'),('8494df1e-4429-49cc-8e31-558e1a0259cf','Mascarpone',70.00,'GRAM'),('8494df1e-4429-49cc-8e31-558e1a0259cf','Savoiardi',25.00,'GRAM'),('8494df1e-4429-49cc-8e31-558e1a0259cf','Yogurt',50.00,'GRAM'),('8599d9a4-ef94-41fc-bae5-3c177850f316','Caffè',30.00,'MILLILITER'),('88adc407-f91f-4c12-8328-75a12d4fe01a','COCA COLA',330.00,'MILLILITER'),('89f77840-583c-44ce-aea2-71f5f979afd4','Aglio',0.30,'GRAM'),('89f77840-583c-44ce-aea2-71f5f979afd4','Funghi',125.00,'GRAM'),('89f77840-583c-44ce-aea2-71f5f979afd4','Olio EVO',5.00,'MILLILITER'),('89f77840-583c-44ce-aea2-71f5f979afd4','Pane',50.00,'GRAM'),('89f77840-583c-44ce-aea2-71f5f979afd4','PEPE NERO MACINATO',0.10,'GRAM'),('89f77840-583c-44ce-aea2-71f5f979afd4','Prezzemolo',1.00,'GRAM'),('89f77840-583c-44ce-aea2-71f5f979afd4','SCAMORZA',50.00,'GRAM'),('8f0e5345-c0ce-4983-8a9e-38f0cbaf20f4','Melanzane',100.00,'GRAM'),('8f0e5345-c0ce-4983-8a9e-38f0cbaf20f4','Mozzarella',50.00,'GRAM'),('8f0e5345-c0ce-4983-8a9e-38f0cbaf20f4','Olio EVO',10.00,'MILLILITER'),('8f0e5345-c0ce-4983-8a9e-38f0cbaf20f4','PARMIGIANO',15.00,'GRAM'),('8f0e5345-c0ce-4983-8a9e-38f0cbaf20f4','Pomodorini',25.00,'GRAM'),('943e978b-5550-4a6f-be94-09a3dbb6109b','Mozzarella',15.00,'GRAM'),('943e978b-5550-4a6f-be94-09a3dbb6109b','Olio di semi',3.00,'MILLILITER'),('943e978b-5550-4a6f-be94-09a3dbb6109b','PARMIGIANO',5.00,'GRAM'),('943e978b-5550-4a6f-be94-09a3dbb6109b','Passata di pomodoro',30.00,'GRAM'),('943e978b-5550-4a6f-be94-09a3dbb6109b','Riso Carnaroli',50.00,'GRAM'),('943e978b-5550-4a6f-be94-09a3dbb6109b','Uova',5.00,'GRAM'),('96726503-79ad-43ab-b85d-7677d4b85966','Acqua',100.00,'MILLILITER'),('96726503-79ad-43ab-b85d-7677d4b85966','FARINA',130.00,'GRAM'),('96726503-79ad-43ab-b85d-7677d4b85966','Mozzarella',70.00,'GRAM'),('96726503-79ad-43ab-b85d-7677d4b85966','Olio EVO',5.00,'MILLILITER'),('96726503-79ad-43ab-b85d-7677d4b85966','Passata di pomodoro',30.00,'GRAM'),('96726503-79ad-43ab-b85d-7677d4b85966','SALAME',60.00,'GRAM'),('96726503-79ad-43ab-b85d-7677d4b85966','Sale',1.00,'GRAM'),('9a62f0b7-1b3b-41a5-b1c3-d5112cc07a9a','Lattuga',25.00,'GRAM'),('9a62f0b7-1b3b-41a5-b1c3-d5112cc07a9a','MANZO MACINATO',160.00,'GRAM'),('9a62f0b7-1b3b-41a5-b1c3-d5112cc07a9a','Olio EVO',5.00,'MILLILITER'),('9a62f0b7-1b3b-41a5-b1c3-d5112cc07a9a','Pane',60.00,'GRAM'),('9a62f0b7-1b3b-41a5-b1c3-d5112cc07a9a','POMODORO',40.00,'GRAM'),('9a62f0b7-1b3b-41a5-b1c3-d5112cc07a9a','Sale',1.00,'GRAM'),('9a62f0b7-1b3b-41a5-b1c3-d5112cc07a9a','Senape',10.00,'GRAM'),('9c815641-f078-4abe-a565-a72af58f5755','CARAMELLO',10.00,'GRAM'),('9c815641-f078-4abe-a565-a72af58f5755','LATTE',30.00,'GRAM'),('9c815641-f078-4abe-a565-a72af58f5755','Panna',80.00,'GRAM'),('9c815641-f078-4abe-a565-a72af58f5755','Zucchero',15.00,'GRAM'),('a8538f4f-16b2-4068-a997-028092c80f32','Ceci',120.00,'GRAM'),('a8538f4f-16b2-4068-a997-028092c80f32','Olio EVO',10.00,'MILLILITER'),('a8538f4f-16b2-4068-a997-028092c80f32','PASTA DI GRANO DURO',45.00,'GRAM'),('a89f272f-3021-4028-8ee6-2c63677fd874','FAGIOLINI',100.00,'GRAM'),('a89f272f-3021-4028-8ee6-2c63677fd874','Olio EVO',10.00,'MILLILITER'),('a89f272f-3021-4028-8ee6-2c63677fd874','Patate',100.00,'GRAM'),('a89f272f-3021-4028-8ee6-2c63677fd874','Sale',2.00,'GRAM'),('a990fc91-cb8e-40a9-8797-e0564531708d','Burro',12.00,'GRAM'),('a990fc91-cb8e-40a9-8797-e0564531708d','FARINA',30.00,'GRAM'),('a990fc91-cb8e-40a9-8797-e0564531708d','Limone',3.00,'MILLILITER'),('a990fc91-cb8e-40a9-8797-e0564531708d','Mela',50.00,'GRAM'),('a990fc91-cb8e-40a9-8797-e0564531708d','Uova',25.00,'GRAM'),('a990fc91-cb8e-40a9-8797-e0564531708d','Zucchero',20.00,'GRAM'),('b50d4e28-1e63-4a4f-a1a6-d1d6e317f1c0','COCA COLA ZERO',330.00,'GRAM'),('b50f4d58-18a0-4b7e-afea-f181c33fa0eb','Cioccolato fondente',40.00,'GRAM'),('b50f4d58-18a0-4b7e-afea-f181c33fa0eb','Panna',60.00,'GRAM'),('b50f4d58-18a0-4b7e-afea-f181c33fa0eb','Uova',50.00,'GRAM'),('b50f4d58-18a0-4b7e-afea-f181c33fa0eb','Zucchero',10.00,'GRAM'),('bb8af6d6-d81a-4b6c-a6e7-a725ad669d18','Olio EVO',10.00,'MILLILITER'),('bb8af6d6-d81a-4b6c-a6e7-a725ad669d18','PACCHERI',90.00,'GRAM'),('bb8af6d6-d81a-4b6c-a6e7-a725ad669d18','Passata di pomodoro',110.00,'GRAM'),('c220ae75-b857-40eb-b1f0-7595e41e3fbd','Acqua',70.00,'MILLILITER'),('c220ae75-b857-40eb-b1f0-7595e41e3fbd','Limone',30.00,'MILLILITER'),('c220ae75-b857-40eb-b1f0-7595e41e3fbd','Zucchero',25.00,'GRAM'),('c6f932fd-2683-4b4b-924a-fb5db9f11b80','Olio EVO',10.00,'MILLILITER'),('c6f932fd-2683-4b4b-924a-fb5db9f11b80','Patate',200.00,'GRAM'),('c6f932fd-2683-4b4b-924a-fb5db9f11b80','Rosmarino',5.00,'GRAM'),('d094bb7e-4e22-41d3-8246-76b97d3da7e2','Acqua',1000.00,'MILLILITER'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','CACIOTTA',15.00,'GRAM'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','COPPA',15.00,'GRAM'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','MARMELLATA',20.00,'GRAM'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','MORTADELLA',15.00,'GRAM'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','PECORINO PICCANTE',15.00,'GRAM'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','PEPERONI SOTT\'OLIO',15.00,'GRAM'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','PROSCIUTTO CRUDO',15.00,'GRAM'),('d5ec27fa-b0e5-4a5b-8a86-3bca99b4fd99','SALAME',15.00,'GRAM'),('e73f9102-71f9-4c0f-b010-a7eae9dcb563','Aglio',3.00,'GRAM'),('e73f9102-71f9-4c0f-b010-a7eae9dcb563','Olio EVO',15.00,'MILLILITER'),('e73f9102-71f9-4c0f-b010-a7eae9dcb563','PASTA DI GRANO DURO',45.00,'GRAM'),('eb1c065e-e61b-4054-ac4c-95b43e4ca25d','GUANCIALE',20.00,'GRAM'),('eb1c065e-e61b-4054-ac4c-95b43e4ca25d','PASTA DI GRANO DURO',45.00,'GRAM'),('eb1c065e-e61b-4054-ac4c-95b43e4ca25d','PECORINO ROMANO DOP',13.00,'GRAM'),('eb1c065e-e61b-4054-ac4c-95b43e4ca25d','PEPE NERO MACINATO',0.50,'GRAM'),('eb1c065e-e61b-4054-ac4c-95b43e4ca25d','SALE DA CUCINA',1.50,'GRAM'),('eb1c065e-e61b-4054-ac4c-95b43e4ca25d','Uova',25.00,'GRAM'),('ebf0acdc-56f9-49d9-95c4-76a9d3e17eeb','Sale',1.00,'GRAM'),('ebf0acdc-56f9-49d9-95c4-76a9d3e17eeb','TAGLIATA DI MANZO',250.00,'GRAM'),('eca7ef0b-089d-4532-b8e6-cbfc34bdf876','Limone',60.00,'MILLILITER'),('eca7ef0b-089d-4532-b8e6-cbfc34bdf876','Olio EVO',7.00,'MILLILITER'),('eca7ef0b-089d-4532-b8e6-cbfc34bdf876','Petto di pollo',150.00,'GRAM'),('ee625b10-a368-40df-86e9-4375a6772bfd','GRISSINI ALL\'OLIO D\'OLIVA',10.00,'GRAM'),('ee625b10-a368-40df-86e9-4375a6772bfd','OLIVE',40.00,'GRAM'),('ee625b10-a368-40df-86e9-4375a6772bfd','Pomodorini',100.00,'GRAM'),('ee625b10-a368-40df-86e9-4375a6772bfd','TARALLI ALL\'OLIO D\'OLIVA',10.00,'GRAM'),('ee625b10-a368-40df-86e9-4375a6772bfd','VERDURE SOTT\'OLIO',50.00,'GRAM'),('febed3e5-f811-41f0-a53e-1479b18a5080','GUANCIALE',25.00,'GRAM'),('febed3e5-f811-41f0-a53e-1479b18a5080','Passata di pomodoro',80.00,'GRAM'),('febed3e5-f811-41f0-a53e-1479b18a5080','PASTA DI GRANO DURO',45.00,'GRAM'),('febed3e5-f811-41f0-a53e-1479b18a5080','PECORINO ROMANO DOP',25.00,'GRAM');
/*!40000 ALTER TABLE `dish_ingredient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredient`
--

DROP TABLE IF EXISTS `ingredient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredient` (
  `name` varchar(50) NOT NULL,
  `protein` decimal(7,2) NOT NULL DEFAULT '0.00',
  `carbohydrate` decimal(7,2) NOT NULL DEFAULT '0.00',
  `fat` decimal(7,2) NOT NULL DEFAULT '0.00',
  `unit` enum('GRAM','MILLILITER') NOT NULL DEFAULT 'GRAM',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredient`
--

LOCK TABLES `ingredient` WRITE;
/*!40000 ALTER TABLE `ingredient` DISABLE KEYS */;
INSERT INTO `ingredient` VALUES ('Aceto balsamico',0.50,17.00,0.00,'MILLILITER'),('Acqua',0.00,0.00,0.00,'MILLILITER'),('Aglio',6.36,33.10,0.50,'GRAM'),('Aglio in polvere',16.60,72.70,0.70,'GRAM'),('ARACHIDI',25.80,16.10,49.20,'GRAM'),('Arancia',0.90,12.00,0.10,'GRAM'),('Banana',1.10,23.00,0.30,'GRAM'),('Basilico',3.15,2.65,0.65,'GRAM'),('BIRRA',0.40,3.50,0.00,'GRAM'),('Bresaola',33.00,0.00,2.50,'GRAM'),('Broccoli',2.80,6.60,0.40,'GRAM'),('Burro',0.50,0.50,82.00,'GRAM'),('Cacao',19.60,57.90,13.70,'GRAM'),('CACIOTTA',22.00,1.00,26.00,'GRAM'),('Caffè',0.10,0.00,0.00,'MILLILITER'),('CARAMELLO',0.00,93.00,0.00,'GRAM'),('Carota',0.90,9.60,0.20,'GRAM'),('Cavolfiore',1.90,5.00,0.30,'GRAM'),('Ceci',8.90,27.40,2.60,'GRAM'),('Cioccolato fondente',5.00,50.00,35.00,'GRAM'),('Cipolla',1.10,9.30,0.10,'GRAM'),('COCA COLA',0.00,10.50,0.00,'MILLILITER'),('COCA COLA ZERO',0.00,0.10,0.00,'GRAM'),('COPPA',24.00,0.00,30.00,'GRAM'),('Couscous',12.00,72.00,1.00,'GRAM'),('Cozze',12.00,4.00,2.00,'GRAM'),('EMMENTAL',28.00,0.50,29.00,'GRAM'),('Erba cipollina',3.30,4.40,0.70,'GRAM'),('Fagioli',8.70,22.80,0.50,'GRAM'),('FAGIOLINI',1.80,7.00,0.10,'GRAM'),('FARINA',10.00,76.00,1.00,'GRAM'),('FARINA SENZA GLUTINE',4.00,78.00,2.00,'GRAM'),('Farro',14.00,67.00,2.50,'GRAM'),('FORMAGGIO GRATTUGIATO',33.00,0.00,30.00,'GRAM'),('Fragole',0.70,7.70,0.30,'GRAM'),('FRUTTI DI BOSCO',1.10,7.80,0.40,'GRAM'),('Funghi',3.10,3.30,0.30,'GRAM'),('Gamberi',20.00,0.00,1.50,'GRAM'),('GELATINA',85.00,0.00,0.10,'GRAM'),('GRISSINI ALL\'OLIO D\'OLIVA',11.00,70.00,9.00,'GRAM'),('GUANCIALE',14.00,0.00,61.00,'GRAM'),('INGREDIENTE DI PROVA',1.00,1.00,1.00,'GRAM'),('Kiwi',1.10,15.00,0.50,'GRAM'),('LATTE',1.00,1.00,1.00,'GRAM'),('LATTE DI SOIA',3.30,0.60,1.80,'GRAM'),('Lattuga',1.40,2.90,0.20,'GRAM'),('Lenticchie',9.00,20.00,0.40,'GRAM'),('Lievito',16.00,8.00,1.00,'GRAM'),('LIEVITO DI BIRRA SECCO',40.00,41.00,7.00,'GRAM'),('Lime',0.10,8.50,0.30,'MILLILITER'),('Limone',0.10,9.00,0.30,'MILLILITER'),('MANDORLE',21.20,21.70,49.90,'GRAM'),('MANZO MACINATO',26.00,0.00,15.00,'GRAM'),('MARMELLATA',0.50,60.00,0.50,'GRAM'),('Mascarpone',4.50,4.00,40.00,'GRAM'),('Mela',0.30,14.00,0.20,'GRAM'),('Melanzane',1.00,5.90,0.20,'GRAM'),('Miele',0.30,82.00,0.00,'GRAM'),('MORTADELLA',16.00,2.00,25.00,'GRAM'),('Mozzarella',18.00,2.00,17.00,'GRAM'),('MOZZARELLA DI BUFALA',18.00,1.00,23.00,'GRAM'),('Noci',15.20,13.70,65.20,'GRAM'),('Olio di semi',0.00,0.00,100.00,'MILLILITER'),('Olio EVO',0.00,0.00,100.00,'MILLILITER'),('OLIVE',1.00,1.00,15.00,'GRAM'),('OLIVE VERDI DENOCCIOLATE',1.00,1.00,15.00,'GRAM'),('Origano',9.00,69.00,4.00,'GRAM'),('PACCHERI',13.00,75.00,2.00,'GRAM'),('Pane',8.00,49.00,3.20,'GRAM'),('Pangrattato',13.00,73.00,5.00,'GRAM'),('Panna',2.00,3.00,35.00,'GRAM'),('PARMIGIANO',33.00,0.00,28.00,'GRAM'),('Passata di pomodoro',1.60,6.00,0.30,'GRAM'),('PASTA DI GRANO DURO',13.00,70.00,1.50,'GRAM'),('PASTA DI SEMOLA',13.00,75.00,2.00,'GRAM'),('Patate',2.00,17.00,0.10,'GRAM'),('PECORINO PICCANTE',26.00,0.00,33.00,'GRAM'),('PECORINO ROMANO DOP',28.00,0.00,30.00,'GRAM'),('PEPE NERO MACINATO',10.00,64.00,3.30,'GRAM'),('Peperoni',1.00,6.00,0.30,'GRAM'),('PEPERONI SOTT\'OLIO',1.00,5.00,15.00,'GRAM'),('Pera',0.40,15.00,0.10,'GRAM'),('Petto di pollo',31.00,0.00,3.60,'GRAM'),('Pistacchi',20.00,28.00,45.00,'GRAM'),('Pomodorini',0.90,3.90,0.20,'GRAM'),('POMODORO',0.90,3.90,0.20,'GRAM'),('Prezzemolo',3.00,6.00,0.80,'GRAM'),('Prosciutto cotto',18.00,1.00,7.00,'GRAM'),('PROSCIUTTO CRUDO',26.00,0.00,18.00,'GRAM'),('Quinoa',14.00,64.00,6.00,'GRAM'),('Ricotta',11.00,3.00,8.00,'GRAM'),('Riso Carnaroli',7.00,78.00,0.60,'GRAM'),('Rosmarino',3.30,20.70,5.90,'GRAM'),('Rucola',2.60,3.70,0.70,'GRAM'),('SALAME',22.00,1.00,36.00,'GRAM'),('Sale',0.00,0.00,0.00,'GRAM'),('SALE DA CUCINA',0.00,0.01,0.00,'GRAM'),('SALMONE',20.00,0.00,13.00,'GRAM'),('Salsa di soia',8.00,5.00,0.10,'MILLILITER'),('Salsiccia',16.00,1.00,25.00,'GRAM'),('Savoiardi',8.00,70.00,8.00,'GRAM'),('SCAMORZA',22.00,1.00,23.00,'GRAM'),('Sedano',0.70,3.00,0.20,'GRAM'),('Semi di sesamo',18.00,23.00,50.00,'GRAM'),('Semi di zucca',30.00,11.00,49.00,'GRAM'),('Senape',6.00,6.00,11.00,'GRAM'),('Spaghetti',13.00,75.00,1.50,'GRAM'),('Spinaci',2.90,3.60,0.40,'GRAM'),('TAGLIATA DI MANZO',26.00,0.00,13.00,'GRAM'),('Tahina',17.00,23.00,53.00,'GRAM'),('TARALLI ALL\'OLIO D\'OLIVA',9.00,65.00,18.00,'GRAM'),('Tonno',23.00,0.00,1.00,'GRAM'),('Tortillas',8.00,50.00,6.00,'GRAM'),('Uova',13.00,1.10,11.00,'GRAM'),('VERDURE SOTT\'OLIO',1.00,4.00,17.00,'GRAM'),('Yogurt',4.00,5.00,3.00,'GRAM'),('Zucca',1.00,6.50,0.10,'GRAM'),('Zucchero',0.00,100.00,0.00,'GRAM'),('Zucchine',1.20,3.10,0.30,'GRAM');
/*!40000 ALTER TABLE `ingredient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredient_allergen`
--

DROP TABLE IF EXISTS `ingredient_allergen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredient_allergen` (
  `ingredient_name` varchar(50) NOT NULL,
  `allergen_type` enum('GLUTEN','CRUSTACEANS','EGGS','FISH','PEANUTS','SOY','MILK','NUTS','CELERY','MUSTARD','SESAME','SULPHITES','LUPIN','MOLLUSCS') NOT NULL,
  PRIMARY KEY (`ingredient_name`,`allergen_type`),
  CONSTRAINT `fk_ingredient_all` FOREIGN KEY (`ingredient_name`) REFERENCES `ingredient` (`name`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredient_allergen`
--

LOCK TABLES `ingredient_allergen` WRITE;
/*!40000 ALTER TABLE `ingredient_allergen` DISABLE KEYS */;
INSERT INTO `ingredient_allergen` VALUES ('Aceto balsamico','SULPHITES'),('ARACHIDI','PEANUTS'),('BIRRA','GLUTEN'),('Burro','MILK'),('CACIOTTA','MILK'),('Couscous','GLUTEN'),('Cozze','MOLLUSCS'),('EMMENTAL','MILK'),('FARINA','GLUTEN'),('Farro','GLUTEN'),('FORMAGGIO GRATTUGIATO','MILK'),('Gamberi','CRUSTACEANS'),('GRISSINI ALL\'OLIO D\'OLIVA','GLUTEN'),('INGREDIENTE DI PROVA','PEANUTS'),('INGREDIENTE DI PROVA','SULPHITES'),('LATTE','MILK'),('LATTE DI SOIA','SOY'),('MANDORLE','NUTS'),('Mascarpone','MILK'),('Mozzarella','MILK'),('MOZZARELLA DI BUFALA','MILK'),('Noci','NUTS'),('PACCHERI','GLUTEN'),('Pane','GLUTEN'),('Pangrattato','GLUTEN'),('Panna','MILK'),('Parmigiano','MILK'),('PASTA DI GRANO DURO','GLUTEN'),('PASTA DI SEMOLA','GLUTEN'),('PECORINO PICCANTE','MILK'),('PECORINO ROMANO DOP','MILK'),('Pistacchi','NUTS'),('Ricotta','MILK'),('Salmone','FISH'),('Salsa di soia','GLUTEN'),('Salsa di soia','SOY'),('Savoiardi','GLUTEN'),('Savoiardi','EGGS'),('SCAMORZA','MILK'),('Sedano','CELERY'),('Semi di sesamo','SESAME'),('Senape','MUSTARD'),('Spaghetti','GLUTEN'),('Tahina','SESAME'),('TARALLI ALL\'OLIO D\'OLIVA','GLUTEN'),('Tonno','FISH'),('Tortillas','GLUTEN'),('Uova','EGGS'),('Yogurt','MILK');
/*!40000 ALTER TABLE `ingredient_allergen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant_room`
--

DROP TABLE IF EXISTS `restaurant_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant_room` (
  `id` int NOT NULL,
  `room_rows` int NOT NULL,
  `room_cols` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant_room`
--

LOCK TABLES `restaurant_room` WRITE;
/*!40000 ALTER TABLE `restaurant_room` DISABLE KEYS */;
INSERT INTO `restaurant_room` VALUES (1,7,5);
/*!40000 ALTER TABLE `restaurant_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant_table`
--

DROP TABLE IF EXISTS `restaurant_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant_table` (
  `id` int NOT NULL,
  `seats` int NOT NULL,
  `row` int DEFAULT NULL,
  `col` int DEFAULT NULL,
  `status` enum('FREE','BOOKED','OCCUPIED') NOT NULL DEFAULT 'FREE',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant_table`
--

LOCK TABLES `restaurant_table` WRITE;
/*!40000 ALTER TABLE `restaurant_table` DISABLE KEYS */;
INSERT INTO `restaurant_table` VALUES (0,2,0,0,'FREE'),(1,4,0,1,'OCCUPIED'),(2,6,0,2,'OCCUPIED'),(3,8,0,3,'OCCUPIED'),(4,2,0,4,'OCCUPIED'),(5,4,1,0,'OCCUPIED'),(6,6,1,1,'BOOKED'),(7,8,1,2,'FREE'),(8,2,1,3,'FREE'),(9,4,1,4,'FREE'),(10,6,2,0,'OCCUPIED'),(11,8,2,1,'BOOKED'),(12,2,2,2,'FREE'),(13,4,2,3,'FREE'),(14,6,2,4,'FREE'),(15,8,3,0,'FREE'),(16,6,3,1,'BOOKED'),(17,4,3,2,'FREE'),(18,2,3,3,'OCCUPIED'),(19,8,3,4,'FREE'),(20,2,4,0,'FREE'),(27,6,6,0,'FREE'),(29,8,6,4,'OCCUPIED'),(30,4,4,4,'FREE');
/*!40000 ALTER TABLE `restaurant_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_session`
--

DROP TABLE IF EXISTS `table_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_session` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `table_id` int NOT NULL,
  `is_open` tinyint(1) NOT NULL DEFAULT '1',
  `open_table_id` int GENERATED ALWAYS AS ((case when (`is_open` = 1) then `table_id` else NULL end)) STORED,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_open_table` (`open_table_id`),
  KEY `idx_open_session` (`table_id`,`is_open`),
  CONSTRAINT `table_session_ibfk_1` FOREIGN KEY (`table_id`) REFERENCES `restaurant_table` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_session`
--

LOCK TABLES `table_session` WRITE;
/*!40000 ALTER TABLE `table_session` DISABLE KEYS */;
INSERT INTO `table_session` (`id`, `table_id`, `is_open`) VALUES ('15c8e544-db4e-429d-93ac-70f1e09eeceb',1,1);
/*!40000 ALTER TABLE `table_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id_user` char(36) NOT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role` enum('CUSTOMER','WAITER','MANAGER') NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('4e2d54c5-6c29-4c4f-881b-be0c000f2a32','Mario','Rossi','mariorossi@email.com','CUSTOMER'),('ae15bbef-0cfb-42d9-b8e4-e4bb5eac6a64','Cameriere','Ristorante','waiter@email.com','WAITER'),('dd764457-869d-499c-89e8-5a089971bfde','Manager','Ristorante','admin@email.com','MANAGER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'foodmood'
--

--
-- Dumping routines for database 'foodmood'
--
/*!50003 DROP PROCEDURE IF EXISTS `close_table_session_by_table` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `close_table_session_by_table`(
  IN p_table_id INT
)
BEGIN
  DECLARE v_rows INT DEFAULT 0;

  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    RESIGNAL;
  END;

  START TRANSACTION;

  UPDATE table_session
  SET is_open = 0
  WHERE table_id = p_table_id AND is_open = 1;

  SET v_rows = ROW_COUNT();

  IF v_rows > 0 THEN
    UPDATE restaurant_table
    SET status = 'FREE'
    WHERE id = p_table_id;
  END IF;

  COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `delete_all_restaurant_tables` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_all_restaurant_tables`()
BEGIN
    DELETE FROM restaurant_table;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `delete_cart_by_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_cart_by_id`(
    IN p_id CHAR(36)
)
BEGIN
    DELETE FROM cart
    WHERE id = p_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `delete_dish_by_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_dish_by_id`(IN p_id_dish CHAR(36))
BEGIN
	DELETE FROM dish
    WHERE id_dish = p_id_dish;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `delete_ingredient_by_name` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_ingredient_by_name`(
	IN p_name VARCHAR(50)
)
BEGIN
	DELETE FROM ingredient
    WHERE name = p_name;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `enter_table_session` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `enter_table_session`(
  IN  p_table_id INT,
  IN  p_session_id CHAR(36),
  OUT p_effective_session_id CHAR(36)
)
BEGIN
  DECLARE v_rows INT DEFAULT 0;
  DECLARE v_session_id CHAR(36);

  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    RESIGNAL;
  END;

  START TRANSACTION;

  SELECT id
    FROM restaurant_table
   WHERE id = p_table_id
   FOR UPDATE;

  INSERT IGNORE INTO table_session (id, table_id, is_open)
  VALUES (p_session_id, p_table_id, 1);

  SET v_rows = ROW_COUNT();

  IF v_rows = 1 THEN
    SET v_session_id = p_session_id;

    UPDATE restaurant_table
       SET status = 'OCCUPIED'
     WHERE id = p_table_id;

  ELSE
    SELECT id
      INTO v_session_id
      FROM table_session
     WHERE table_id = p_table_id
       AND is_open = 1
     LIMIT 1
     FOR UPDATE;

    UPDATE restaurant_table
       SET status = 'OCCUPIED'
     WHERE id = p_table_id;
  END IF;

  COMMIT;

  SET p_effective_session_id = v_session_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_carts` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_all_carts`()
BEGIN
    SELECT
        c.id,
        c.actor_id,
        c.table_session_id,
        ci.dish_id,
        ci.product_name,
        ci.unit_price,
        ci.quantity
    FROM cart c
    LEFT JOIN cart_item ci ON ci.cart_id = c.id
    ORDER BY c.id, ci.dish_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_dishes` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_all_dishes`()
BEGIN
    SELECT id_dish, name, description, course_type, price, image_uri, state
    FROM dish
    ORDER BY name;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_dish_ingredients` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_all_dish_ingredients`()
BEGIN
    SELECT id_dish, ingredient_name, quantity, unit
    FROM dish_ingredient;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_ingredients` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_all_ingredients`()
BEGIN
	SELECT i.name,
           i.protein,
           i.carbohydrate,
           i.fat,
           i.unit,
           ia.allergen_type
    FROM ingredient i
    LEFT JOIN ingredient_allergen ia
           ON ia.ingredient_name = i.name
	ORDER BY i.name;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_all_restaurant_tables` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_all_restaurant_tables`()
BEGIN
    SELECT id, `seats`, `row`, `col`, status
    FROM restaurant_table
    ORDER BY id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_cart_by_actor_and_table_session` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_cart_by_actor_and_table_session`(
    IN p_actor_id CHAR(36),
    IN p_table_session_id CHAR(36)
)
BEGIN
    SELECT
        c.id,
        c.actor_id,
        c.table_session_id,
        ci.dish_id,
        ci.product_name,
        ci.unit_price,
        ci.quantity
    FROM cart c
    LEFT JOIN cart_item ci ON ci.cart_id = c.id
    WHERE c.actor_id = p_actor_id
      AND c.table_session_id = p_table_session_id
    ORDER BY ci.dish_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_cart_by_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_cart_by_id`(
    IN p_id CHAR(36)
)
BEGIN
    SELECT
        c.id,
        c.actor_id,
        c.table_session_id,
        ci.dish_id,
        ci.product_name,
        ci.unit_price,
        ci.quantity
    FROM cart c
    LEFT JOIN cart_item ci ON ci.cart_id = c.id
    WHERE c.id = p_id
    ORDER BY ci.dish_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_credential_by_user_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_credential_by_user_id`(
    IN p_user_id CHAR(36)
)
BEGIN
    SELECT user_id, password_hash, created_at
    FROM credentials
    WHERE user_id = p_user_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_dishes_by_course_type` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_dishes_by_course_type`(IN p_course_type VARCHAR(32))
BEGIN
    SELECT id_dish, name, description, course_type, price, image_uri, state
    FROM dish
    WHERE course_type = p_course_type
    ORDER BY name;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_dishes_by_diet_category` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_dishes_by_diet_category`(IN p_diet_category VARCHAR(32))
BEGIN
	SELECT DISTINCT
        d.id_dish,
        d.name,
        d.description,
        d.course_type,
        d.price,
        d.image_uri,
        d.state
    FROM dish d
    JOIN dish_diet_category dc
      ON dc.id_dish = d.id_dish
    WHERE dc.diet_category = p_category
    ORDER BY d.name;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_dish_by_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_dish_by_id`(IN p_id_dish CHAR(36))
BEGIN
    SELECT id_dish, name, description, course_type, price, image_uri, state
    FROM dish
    WHERE id_dish = p_id_dish;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_dish_by_name` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_dish_by_name`(IN p_name VARCHAR(50))
BEGIN
    SELECT id_dish, name, description, course_type, price, image_uri, state
    FROM dish
    WHERE name = p_name;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_dish_diet_categories` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_dish_diet_categories`(IN p_id_dish CHAR(36))
BEGIN
    SELECT diet_category
    FROM dish_diet_category
    WHERE id_dish = p_id_dish
    ORDER BY diet_category;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_dish_ingredients` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_dish_ingredients`(
    IN p_id_dish CHAR(36)
)
BEGIN
    SELECT
        ingredient_name,
        quantity,
        unit
    FROM dish_ingredient
    WHERE id_dish = p_id_dish;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_ingredient_by_name` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_ingredient_by_name`(
	IN p_name VARCHAR(50)
)
BEGIN
	SELECT i.name,
           i.protein,
           i.carbohydrate,
           i.fat,
           i.unit,
           ia.allergen_type
    FROM ingredient i
    LEFT JOIN ingredient_allergen ia
           ON ia.ingredient_name = i.name
    WHERE i.name = p_name;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_order_by_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_order_by_id`(
    IN p_order_id CHAR(36)
)
BEGIN
    SELECT
        o.id,
        o.user_id,
        o.table_session_id,
        o.status,
        l.dish_id,
        l.product_name,
        l.unit_price,
        l.quantity
    FROM customer_order o
    JOIN customer_order_line l
        ON l.order_id = o.id
    WHERE o.id = p_order_id
    ORDER BY l.dish_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_restaurant_table_by_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_restaurant_table_by_id`(IN p_id_table INT)
BEGIN
    SELECT id, `seats`, `row`, `col`, status
    FROM restaurant_table
    WHERE id = p_id_table;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_table_by_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_table_by_id`(IN p_table_id INT)
BEGIN
	SELECT id, seats, `row`, `col`, status
    FROM restaurant_table
    WHERE id = p_table_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_table_session_by_id` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_table_session_by_id`(IN p_session_id CHAR(36))
BEGIN
  SELECT id, table_id, is_open
  FROM table_session
  WHERE id = p_session_id
  LIMIT 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_user_by_email` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_user_by_email`(
    IN p_email VARCHAR(255)
)
BEGIN
    SET p_email = LOWER(p_email);

    SELECT id_user, name, surname, email, role
    FROM users
    WHERE email = p_email;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_cart` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_cart`(
    IN p_id CHAR(36),
    IN p_actor_id CHAR(36),
    IN p_table_session_id CHAR(36),
    IN p_items JSON
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    INSERT INTO cart (id, actor_id, table_session_id)
    VALUES (p_id, p_actor_id, p_table_session_id);

    INSERT INTO cart_item (
        cart_id,
        dish_id,
        product_name,
        unit_price,
        quantity
    )
    SELECT
        p_id,
        items.dish_id,
        items.product_name,
        items.unit_price,
        items.quantity
    FROM JSON_TABLE(
        COALESCE(p_items, JSON_ARRAY()),
        '$[*]' COLUMNS (
            dish_id CHAR(36) PATH '$.dishId',
            product_name VARCHAR(50) PATH '$.productName',
            unit_price DECIMAL(10,2) PATH '$.unitPrice',
            quantity INT PATH '$.quantity'
        )
    ) AS items;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_customer_order` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_customer_order`(
    IN p_id               CHAR(36),
    IN p_user_id          CHAR(36),
    IN p_table_session_id CHAR(36),
    IN p_status           ENUM('OPEN','CONFIRMED','SERVED','CANCELLED')
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Errore nell'inserimento dell'ordine";
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

        INSERT INTO customer_order (id, user_id, table_session_id, status)
        VALUES (p_id, p_user_id, p_table_session_id, p_status);

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_customer_order_line` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_customer_order_line`(
    IN p_order_id       CHAR(36),
    IN p_dish_id        CHAR(36),
	IN p_product_name   VARCHAR(50),
    IN p_unit_price     DECIMAL(10,2),
    IN p_quantity       INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
      ROLLBACK;
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Errore durante l\'inserimento dell\'ordine';
    END;

    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;

    IF NOT EXISTS (
        SELECT 1 FROM customer_order
        WHERE id = p_order_id
        FOR UPDATE
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ordine non trovato';
    END IF;

    INSERT INTO customer_order_line (order_id, dish_id, product_name, unit_price, quantity)
    VALUES (p_order_id, p_dish_id, p_product_name, p_unit_price, p_quantity);

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_dish` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_dish`(
	IN p_id_dish	   CHAR(36),	 
    IN p_name          VARCHAR(50),
    IN p_description   VARCHAR(255),
    IN p_course_type   enum('APPETIZER','FIRST_COURSE','MAIN_COURSE','PIZZA','SIDE_DISH','DESSERT','FRUIT','BEVERAGE'),
    IN p_price         DECIMAL(10,2),
    IN p_image_uri     VARCHAR(512),
    IN p_state         enum('AVAILABLE','UNAVAILABLE'),
    IN p_ingredients   JSON,
    IN p_diet_categories JSON
)
BEGIN
    DECLARE v_len INT DEFAULT 0;
    DECLARE v_i   INT DEFAULT 0;

    DECLARE v_ing_name VARCHAR(50);
    DECLARE v_qty      DECIMAL(10,2);
    DECLARE v_unit     VARCHAR(20);
    
    DECLARE v_cat_len INT DEFAULT 0;
    DECLARE v_ci 	  INT DEFAULT 0;
    DECLARE v_cat	  VARCHAR(32);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

        INSERT INTO dish (id_dish, name, description, course_type, price, image_uri, state)
        VALUES (p_id_dish, p_name, p_description, p_course_type, p_price, p_image_uri, p_state)
        ON DUPLICATE KEY UPDATE
			name          = VALUES(name),
            description   = VALUES(description),
            course_type   = VALUES(course_type),
            price         = VALUES(price),
            image_uri     = VALUES(image_uri),
            state         = VALUES(state);

        DELETE FROM dish_ingredient
        WHERE id_dish = p_id_dish;

        SET v_len = COALESCE(JSON_LENGTH(p_ingredients), 0);
        SET v_i = 0;

        WHILE v_i < v_len DO
            SET v_ing_name = JSON_UNQUOTE(
                JSON_EXTRACT(p_ingredients, CONCAT('$[', v_i, '].ingredientName'))
            );
            SET v_qty = JSON_EXTRACT(
                p_ingredients, CONCAT('$[', v_i, '].quantity')
            );
            SET v_unit = JSON_UNQUOTE(
                JSON_EXTRACT(p_ingredients, CONCAT('$[', v_i, '].unit'))
            );

            IF v_ing_name IS NOT NULL AND v_ing_name <> '' THEN
                INSERT INTO dish_ingredient (id_dish, ingredient_name, quantity, unit)
                VALUES (p_id_dish, v_ing_name, v_qty, v_unit);
            END IF;

            SET v_i = v_i + 1;
        END WHILE;
        
        DELETE FROM dish_diet_category WHERE id_dish = p_id_dish;
        
        SET v_cat_len = COALESCE(JSON_LENGTH(p_diet_categories), 0);
        SET v_ci = 0;
        
        WHILE v_ci < v_cat_len DO
			SET v_cat = JSON_UNQUOTE(JSON_EXTRACT(p_diet_categories, CONCAT('$[', v_ci, ']')));
            
            IF v_cat IS NOT NULL AND v_cat <> '' THEN
				INSERT INTO dish_diet_category (id_dish, diet_category)
                VALUES (p_id_dish, v_cat);
			END IF;
            
            SET v_ci = v_ci + 1;
		END WHILE;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_dish_ingredient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_dish_ingredient`(
    IN p_id_dish         CHAR(36),
    IN p_ingredient_name VARCHAR(50),
    IN p_quantity        DECIMAL(10,2),
    IN p_unit            ENUM('GRAM','MILLILITER')
)
BEGIN
    INSERT INTO dish_ingredient (id_dish, ingredient_name, quantity, unit)
    VALUES (p_id_dish, p_ingredient_name, p_quantity, p_unit)
    AS new
    ON DUPLICATE KEY UPDATE
        quantity = new.quantity,
        unit     = new.unit;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_ingredient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_ingredient`(
    IN p_name         VARCHAR(50),
    IN p_protein      DECIMAL(7,2),
    IN p_carbohydrate DECIMAL(7,2),
    IN p_fat          DECIMAL(7,2),
    IN p_unit		  ENUM('GRAM','MILLILITER'),
    IN p_allergens    JSON   
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

        CALL insert_ingredient_data(p_name, p_protein, p_carbohydrate, p_fat, p_unit);
        CALL replace_allergens_json(p_name, p_allergens);

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_ingredient_data` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_ingredient_data`(
    IN p_name         VARCHAR(50),
    IN p_protein      DECIMAL(7,2),
    IN p_carbohydrate DECIMAL(7,2),
    IN p_fat          DECIMAL(7,2),
    IN p_unit 		  ENUM('GRAM', 'MILLILITER')
)
BEGIN
    INSERT INTO ingredient (name, protein, carbohydrate, fat, unit)
    VALUES (p_name, p_protein, p_carbohydrate, p_fat, p_unit)
    ON DUPLICATE KEY UPDATE
        protein      = VALUES(protein),
        carbohydrate = VALUES(carbohydrate),
        fat          = VALUES(fat),
        unit		 = VALUES(unit); 
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_restaurant_table` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_restaurant_table`(
	IN p_id INT,
    IN p_seats INT,
    IN p_row INT,
    IN p_col INT,
    IN p_status VARCHAR(16)
)
BEGIN
    INSERT INTO restaurant_table(id, seats, `row`, `col`, status)
    VALUES (p_id, p_seats, p_row, p_col, p_status)
    ON DUPLICATE KEY UPDATE
        seats  = VALUES(seats),
        `row`  = VALUES(`row`),
        `col`  = VALUES(`col`),
        status = VALUES(status);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_user` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_user`(
	IN p_id CHAR(36),
    IN p_name VARCHAR(50),
    IN p_surname VARCHAR(50),
    IN p_email VARCHAR(50),
    IN p_role ENUM('CUSTOMER','WAITER','MANAGER')
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Errore nell'inserimento dell'utente";
	END;
    
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;
    
    SET p_email = LOWER(p_email);
    INSERT INTO users (id_user, name, surname, email, role) VALUES (p_id, p_name, p_surname, p_email, p_role);
    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `load_restaurant_room` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `load_restaurant_room`()
BEGIN
    SELECT room_rows, room_cols
    FROM restaurant_room
    LIMIT 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `load_restaurant_tables` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `load_restaurant_tables`()
BEGIN
    SELECT id, seats, `row`, `col`, `status`
    FROM restaurant_table;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `replace_allergens_json` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `replace_allergens_json`(
    IN p_name      VARCHAR(50),
    IN p_allergens JSON 
)
BEGIN
    DECLARE v_len  INT DEFAULT 0;
    DECLARE v_i    INT DEFAULT 0;
    DECLARE v_type VARCHAR(50);
    
	DELETE FROM ingredient_allergen
    WHERE ingredient_name = p_name;
    
	SET v_len = COALESCE(JSON_LENGTH(p_allergens), 0);
    
	WHILE v_i < v_len DO
        SET v_type = JSON_UNQUOTE(JSON_EXTRACT(p_allergens, CONCAT('$[', v_i, ']')));

        IF v_type IS NOT NULL AND v_type <> '' THEN
            INSERT INTO ingredient_allergen (ingredient_name, allergen_type)
            VALUES (p_name, v_type);
        END IF;
        
		SET v_i = v_i + 1;
    END WHILE;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `save_credential` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `save_credential`(
	IN p_user_id CHAR(36),
    IN p_password_hash VARCHAR(255)
)
BEGIN
	
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Errore nell\'insermento delle credenziali';
	END;
    
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    START TRANSACTION;
    
		INSERT INTO credentials (user_id, password_hash) VALUES (p_user_id, p_password_hash);
        
	COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `save_restaurant_room` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `save_restaurant_room`(
    IN p_rows INT,
    IN p_cols INT
)
BEGIN
    INSERT INTO restaurant_room (id, room_rows, room_cols)
    VALUES (1, p_rows, p_cols)
    ON DUPLICATE KEY UPDATE
        room_rows = VALUES(room_rows),
        room_cols = VALUES(room_cols);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_login` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_login`(
  IN  p_email      VARCHAR(254),
  OUT p_user_id    INT UNSIGNED,
  OUT p_first_name VARCHAR(100),
  OUT p_last_name  VARCHAR(100),
  OUT p_is_active  BOOLEAN,
  OUT p_pwd_hash   VARCHAR(255)
)
BEGIN
  DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN END;
  SET p_user_id = NULL;
  SET p_first_name = NULL;
  SET p_last_name = NULL;
  SET p_is_active = NULL;
  SET p_pwd_hash = NULL;
  SET p_email = LOWER(p_email);
  SELECT u.user_id, u.first_name, u.last_name, u.is_active, a.pwd_hash
    INTO p_user_id, p_first_name, p_last_name, p_is_active, p_pwd_hash
  FROM `auth` AS a
  JOIN `users` AS u ON u.user_id = a.user_id
  WHERE a.`email` = p_email
  LIMIT 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_register` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_register`(
  IN  p_first_name VARCHAR(100),
  IN  p_last_name  VARCHAR(100),
  IN  p_email      VARCHAR(254),
  IN  p_pwd_hash   VARCHAR(255),
  OUT p_user_id    INT UNSIGNED
)
BEGIN
  DECLARE EXIT HANDLER FOR 1062
  BEGIN
    ROLLBACK;
    SET p_user_id = NULL;
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'EMAIL_EXISTS';
  END;
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET p_user_id = NULL;
  END;
  SET p_email = LOWER(p_email);
  START TRANSACTION;
    INSERT INTO `users`(`first_name`,`last_name`,`is_active`)
    VALUES (p_first_name, p_last_name, TRUE);
    SET p_user_id = LAST_INSERT_ID();
    INSERT INTO `auth`(`user_id`,`email`,`pwd_hash`)
    VALUES (p_user_id, p_email, p_pwd_hash);
  COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_cart` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_cart`(
    IN p_id CHAR(36),
    IN p_items JSON
)
BEGIN
    DECLARE v_cart_exists INT DEFAULT 0;

    DECLARE CONTINUE HANDLER FOR NOT FOUND
        SET v_cart_exists = 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT 1
    INTO v_cart_exists
    FROM cart
    WHERE id = p_id
    FOR UPDATE;

    IF v_cart_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Carrello non trovato';
    END IF;

    DELETE FROM cart_item
    WHERE cart_id = p_id;

    INSERT INTO cart_item (
        cart_id,
        dish_id,
        product_name,
        unit_price,
        quantity
    )
    SELECT
        p_id,
        items.dish_id,
        items.product_name,
        items.unit_price,
        items.quantity
    FROM JSON_TABLE(
        COALESCE(p_items, JSON_ARRAY()),
        '$[*]' COLUMNS (
            dish_id CHAR(36) PATH '$.dishId',
            product_name VARCHAR(50) PATH '$.productName',
            unit_price DECIMAL(10,2) PATH '$.unitPrice',
            quantity INT PATH '$.quantity'
        )
    ) AS items;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-24 12:14:34
