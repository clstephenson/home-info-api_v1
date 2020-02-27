CREATE DATABASE  IF NOT EXISTS `homeinfodb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `homeinfodb`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: aaf7q4limrw6hk.cxe6etw4advl.us-west-2.rds.amazonaws.com    Database: homeinfodb
-- ------------------------------------------------------
-- Server version	5.6.40-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `features`
--

DROP TABLE IF EXISTS `features`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `features` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `last_modified_at` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `location_id` bigint(20) DEFAULT NULL,
  `property_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpgg2j9dcs0k7sd0h5a7f58tsr` (`location_id`),
  KEY `FKk407rhwbd1ee8w5etlfljxa7t` (`property_id`),
  CONSTRAINT `FKk407rhwbd1ee8w5etlfljxa7t` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKpgg2j9dcs0k7sd0h5a7f58tsr` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `features`
--

LOCK TABLES `features` WRITE;
/*!40000 ALTER TABLE `features` DISABLE KEYS */;
INSERT INTO `features` VALUES (2,'2019-04-29 21:49:51','Chris','2019-04-29 21:49:51','Chris','BBQ','','APPLIANCE',5,3),(3,'2019-04-29 21:50:35','Chris','2019-04-29 21:50:35','Chris','Travertine Pavers','','MATERIAL',5,3);
/*!40000 ALTER TABLE `features` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `last_modified_at` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `category_item_id` bigint(20) DEFAULT NULL,
  `content_type` varchar(255) DEFAULT NULL,
  `original_file_name` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `property_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo47g136dpdbv1d5ajs2vrcy2u` (`property_id`),
  CONSTRAINT `FKo47g136dpdbv1d5ajs2vrcy2u` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ideas`
--

DROP TABLE IF EXISTS `ideas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ideas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `last_modified_at` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `property_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo5mf38ivmmcb0dtdwlcchoxdr` (`property_id`),
  CONSTRAINT `FKo5mf38ivmmcb0dtdwlcchoxdr` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ideas`
--

LOCK TABLES `ideas` WRITE;
/*!40000 ALTER TABLE `ideas` DISABLE KEYS */;
INSERT INTO `ideas` VALUES (1,'2019-04-29 21:46:51','Chris','2019-04-29 21:46:51','Chris','Some Idea','This is an idea note.',3),(2,'2019-04-29 21:47:10','Chris','2019-04-29 21:47:10','Chris','another idea','This is another idea note.',3);
/*!40000 ALTER TABLE `ideas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locations`
--

DROP TABLE IF EXISTS `locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `last_modified_at` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `dimensions` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `property_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKan8lnt3isaki1aswx9oq4swl0` (`property_id`),
  CONSTRAINT `FKan8lnt3isaki1aswx9oq4swl0` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locations`
--

LOCK TABLES `locations` WRITE;
/*!40000 ALTER TABLE `locations` DISABLE KEYS */;
INSERT INTO `locations` VALUES (3,'2019-04-29 21:44:49','Chris','2019-04-29 21:44:49','Chris','24 x 30','Living Room','Some Notes',3),(4,'2019-04-29 21:49:22','Chris','2019-04-29 21:49:22','Chris','','Master Bedroom','',3),(5,'2019-04-29 21:49:34','Chris','2019-04-29 21:49:34','Chris','','Backyard','',3);
/*!40000 ALTER TABLE `locations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `properties`
--

DROP TABLE IF EXISTS `properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `properties` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `last_modified_at` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `state_province` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `square_footage` int(11) DEFAULT NULL,
  `year_built` int(11) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtnyxik10x9mx26017sjnuo2nw` (`user_id`),
  CONSTRAINT `FKtnyxik10x9mx26017sjnuo2nw` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `properties`
--

LOCK TABLES `properties` WRITE;
/*!40000 ALTER TABLE `properties` DISABLE KEYS */;
INSERT INTO `properties` VALUES (3,'2019-04-29 21:44:13','Chris','2019-04-29 21:45:58','Chris','7555 E Main Street','Phoenix','United States','86352','Arizona','Primary Residence',1824,1996,1),(4,'2019-04-29 21:44:30','Chris','2019-04-29 21:54:19','Chris','some address','Mesa','United States','84698','Arizona','Second Residence',2000,2003,1);
/*!40000 ALTER TABLE `properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `last_modified_at` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `frequency_in_days` int(11) DEFAULT NULL,
  `is_recurring` bit(1) DEFAULT NULL,
  `last_completion_date` date DEFAULT NULL,
  `property_id` bigint(20) NOT NULL,
  `vendor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK62bhpo6q81rk7ys0o3897munb` (`property_id`),
  KEY `FK4fta3xad2xu4x30xrrs6klqnv` (`vendor_id`),
  CONSTRAINT `FK4fta3xad2xu4x30xrrs6klqnv` FOREIGN KEY (`vendor_id`) REFERENCES `vendors` (`id`),
  CONSTRAINT `FK62bhpo6q81rk7ys0o3897munb` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (2,'2019-04-29 21:51:18','Chris','2019-04-29 21:53:35','Chris','AC Service',365,'','2019-04-18',3,3),(3,'2019-04-29 21:51:58','Chris','2019-04-29 21:51:58','Chris','Change return air filter',60,'','2019-04-01',3,NULL);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `last_modified_at` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `disabled` bit(1) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `uuid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_6km2m9i3vjuy36rnvkgj1l61s` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2019-04-29 17:58:26','Chris','2019-04-29 17:58:26','Chris','user','Chris','\0','Stephenson','$2a$10$h5V2w7KBHo3LITSR8J6.TevOZ7EkZpqymU40NrlsQsCV3Odixhpkq','someuuid');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendors`
--

DROP TABLE IF EXISTS `vendors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendors` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `last_modified_at` datetime NOT NULL,
  `last_modified_by` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiuqso7j7nivq7sb3v3v4j7ein` (`user_id`),
  CONSTRAINT `FKiuqso7j7nivq7sb3v3v4j7ein` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendors`
--

LOCK TABLES `vendors` WRITE;
/*!40000 ALTER TABLE `vendors` DISABLE KEYS */;
INSERT INTO `vendors` VALUES (2,'2019-04-29 21:52:43','Chris','2019-04-29 21:52:43','Chris','trees@example.com','Tree trimming company','','555-555-5555','',1),(3,'2019-04-29 21:53:20','Chris','2019-04-29 21:53:20','Chris','','HVAC contractor','','555-555-5555','',1);
/*!40000 ALTER TABLE `vendors` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-29 14:57:39
