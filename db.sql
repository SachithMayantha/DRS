CREATE DATABASE  IF NOT EXISTS `drs` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `drs`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: drs
-- ------------------------------------------------------
-- Server version	8.0.38

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
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `departmentId` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `mobile` varchar(45) NOT NULL,
  PRIMARY KEY (`departmentId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Melbourne Hospital','hospital@com.au','035699900');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disaster`
--

DROP TABLE IF EXISTS `disaster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disaster` (
  `disasterId` int NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `location` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `severity` varchar(50) NOT NULL,
  `date` date NOT NULL,
  `reportedBy` varchar(100) NOT NULL,
  `locationType` varchar(50) DEFAULT NULL,
  `priorityNo` int DEFAULT NULL,
  PRIMARY KEY (`disasterId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disaster`
--

LOCK TABLES `disaster` WRITE;
/*!40000 ALTER TABLE `disaster` DISABLE KEYS */;
INSERT INTO `disaster` VALUES (1,'Hurricane','saaafa','wdeqfqfqfq fwefwfwfw wefwfw','Low','2024-09-05','Jaye','Industrial Zone',16),(2,'Flood','Wennappuwa','Bla bla bla...','Moderate','2024-09-18','Jayath','Industrial Zone',8),(3,'Fire','Elsternwick','Near the Chemist Warehouse','Critical','2024-09-04','Kasun Perera','Urban Area',12),(4,'Earthquake','Warranabool','Near the Warrnabool Beach','High','2024-09-04','Madu Roxy','Rural Area',10),(5,'Fire','Windsor','Next to windsor station','Severe','2024-09-09','Ashin','Urban Area',16),(6,'Flood','Werribee','Near Werribee Zoo','Low','2024-09-10','Nishan','Rural Area',6),(7,'Fire','Balaclava','Next to Balaclava Coles','Severe','2024-09-10','Ashan','Urban Area',11),(8,'Hurricane','Elwood','Beach area','Severe','2024-09-11','Kamish','Urban Area',12);
/*!40000 ALTER TABLE `disaster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fire`
--

DROP TABLE IF EXISTS `fire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fire` (
  `fireId` int NOT NULL AUTO_INCREMENT,
  `disasterId` int NOT NULL,
  `fighters` int DEFAULT NULL,
  `supporters` int DEFAULT NULL,
  `suppression` int DEFAULT NULL,
  `status` varchar(45) NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`fireId`),
  KEY `disasterId` (`disasterId`),
  CONSTRAINT `fire_ibfk_1` FOREIGN KEY (`disasterId`) REFERENCES `disaster` (`disasterId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fire`
--

LOCK TABLES `fire` WRITE;
/*!40000 ALTER TABLE `fire` DISABLE KEYS */;
INSERT INTO `fire` VALUES (1,6,0,2,0,'Pending'),(2,3,5,2,4,'Pending');
/*!40000 ALTER TABLE `fire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health`
--

DROP TABLE IF EXISTS `health`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health` (
  `healthId` int NOT NULL AUTO_INCREMENT,
  `disasterId` int NOT NULL,
  `doctors` int DEFAULT NULL,
  `nurses` int DEFAULT NULL,
  `ambulances` int DEFAULT NULL,
  `status` varchar(45) NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`healthId`),
  KEY `disasterId_idx` (`disasterId`),
  CONSTRAINT `disasterId` FOREIGN KEY (`disasterId`) REFERENCES `disaster` (`disasterId`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health`
--

LOCK TABLES `health` WRITE;
/*!40000 ALTER TABLE `health` DISABLE KEYS */;
INSERT INTO `health` VALUES (11,7,3,6,1,'Approved'),(15,5,3,7,1,'Approved'),(16,4,5,7,1,'Pending'),(17,3,3,5,2,'Pending');
/*!40000 ALTER TABLE `health` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `police`
--

DROP TABLE IF EXISTS `police`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `police` (
  `policeId` int NOT NULL AUTO_INCREMENT,
  `disasterId` int NOT NULL,
  `policeman` int DEFAULT NULL,
  `trafficControllers` int DEFAULT NULL,
  `investigators` int DEFAULT NULL,
  `status` varchar(45) NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`policeId`),
  KEY `disasterId` (`disasterId`),
  CONSTRAINT `police_ibfk_1` FOREIGN KEY (`disasterId`) REFERENCES `disaster` (`disasterId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `police`
--

LOCK TABLES `police` WRITE;
/*!40000 ALTER TABLE `police` DISABLE KEYS */;
INSERT INTO `police` VALUES (1,6,6,3,1,'Pending'),(2,3,4,2,2,'Pending');
/*!40000 ALTER TABLE `police` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userId` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `mobile` int NOT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Jayath','jaye1@gmail.com','1234',450235996,'USER'),(2,'Mayantha','maye','4321',450235996,'ADMIN'),(3,'Madushi','madu@gmail.com','1234',450235996,'USER'),(4,'Shemil','shem@gmail.com','1234',450235996,'USER'),(5,'Jayath','jaye@gmail.com','56789',564747765,'USER'),(6,'Ashan','ash@gmail.com','12345',596944,'USER'),(7,'Windsor Hospital','win','123',56945656,'DEPARTMENT'),(8,'Brighton Fire Fighters','bri-fire@gmail.com','brighton',54356467,'DEPARTMENT'),(9,'Richmond Police','rich-pol@gmail.com','richmond',948586667,'DEPARTMENT'),(10,'Melbourne Hospital','hospital@com.au','1234',34978000,'DEPARTMENT'),(11,'Melbourne Hospital','hospital@com.au','1234',35699900,'DEPARTMENT');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-25 13:08:34
