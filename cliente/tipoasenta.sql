-- MySQL dump 10.13  Distrib 5.6.41, for Win64 (x86_64)
--
-- Host: localhost    Database: smx
-- ------------------------------------------------------
-- Server version	5.6.41

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
-- Table structure for table `tipoasentamiento`
--

DROP TABLE IF EXISTS `tipoasentamiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipoasentamiento` (
  `id` int(10) unsigned NOT NULL,
  `tipo` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoasentamiento`
--

LOCK TABLES `tipoasentamiento` WRITE;
/*!40000 ALTER TABLE `tipoasentamiento` DISABLE KEYS */;
INSERT INTO `tipoasentamiento` VALUES (1,'Aeropuerto'),(2,'Barrio'),(4,'Campamento'),(8,'Ciudad'),(9,'Colonia'),(10,'Condominio'),(11,'Congregación'),(12,'Conjunto habitacional'),(15,'Ejido'),(16,'Estación'),(17,'Equipamiento'),(18,'Exhacienda'),(20,'Finca'),(21,'Fraccionamiento'),(22,'Gran usuario'),(23,'Granja'),(24,'Hacienda'),(25,'Ingenio'),(26,'Parque industrial'),(27,'Poblado comunal'),(28,'Pueblo'),(29,'Ranchería'),(30,'Residencial'),(31,'Unidad habitacional'),(32,'Villa'),(33,'Zona comercial'),(34,'Zona federal'),(37,'Zona industrial'),(38,'Ampliación'),(39,'Club de golf'),(40,'Puerto'),(45,'Paraje'),(47,'Zona militar'),(48,'Rancho');
/*!40000 ALTER TABLE `tipoasentamiento` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-17 23:45:55
