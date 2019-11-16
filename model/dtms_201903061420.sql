CREATE DATABASE  IF NOT EXISTS `dtms` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `dtms`;
-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: dtms
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authority_resource`
--

DROP TABLE IF EXISTS `authority_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `authority_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `permission` int(11) NOT NULL,
  `authority_name` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority_resource`
--

LOCK TABLES `authority_resource` WRITE;
/*!40000 ALTER TABLE `authority_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `authority_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_line`
--

DROP TABLE IF EXISTS `business_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `business_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_line`
--

LOCK TABLES `business_line` WRITE;
/*!40000 ALTER TABLE `business_line` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `databasechangelog`
--

DROP TABLE IF EXISTS `databasechangelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `databasechangelog` (
  `ID` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `AUTHOR` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `FILENAME` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `MD5SUM` varchar(35) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `COMMENTS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TAG` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LIQUIBASE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CONTEXTS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LABELS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `databasechangelog`
--

LOCK TABLES `databasechangelog` WRITE;
/*!40000 ALTER TABLE `databasechangelog` DISABLE KEYS */;
INSERT INTO `databasechangelog` VALUES ('00000000000001','jhipster','config/liquibase/changelog/00000000000000_initial_schema.xml','2019-03-06 14:19:08',1,'EXECUTED','7:cdff7516eabbb8aae8f7ad9d11492f13','createTable tableName=jhi_user; createIndex indexName=idx_user_login, tableName=jhi_user; createIndex indexName=idx_user_email, tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableN...','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163241-1','jhipster','config/liquibase/changelog/20190122163241_added_entity_AuthorityResource.xml','2019-03-06 14:19:08',2,'EXECUTED','7:85e23afad2932518d3a2c501453e34a9','createTable tableName=authority_resource','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163241-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163241_added_entity_AuthorityResource.xml','2019-03-06 14:19:08',3,'EXECUTED','7:51a4c7a207fe1493db5207cb56c292f8','addColumn tableName=authority_resource','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163242-1','jhipster','config/liquibase/changelog/20190122163242_added_entity_BusinessLine.xml','2019-03-06 14:19:08',4,'EXECUTED','7:fb8ba05a7042adb415c52a17d0a5a4d4','createTable tableName=business_line','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163242-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163242_added_entity_BusinessLine.xml','2019-03-06 14:19:08',5,'EXECUTED','7:0c7ceca14544cbc31569db593ef8ff87','addColumn tableName=business_line','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163243-1','jhipster','config/liquibase/changelog/20190122163243_added_entity_ProjectTemplates.xml','2019-03-06 14:19:08',6,'EXECUTED','7:849eb1cade20630821dc9d2e0d5fccee','createTable tableName=project_templates','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163243-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163243_added_entity_ProjectTemplates.xml','2019-03-06 14:19:09',7,'EXECUTED','7:9a463c672abf98d7dad1b73f7a4f8eff','addColumn tableName=project_templates','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163244-1','jhipster','config/liquibase/changelog/20190122163244_added_entity_ProjectWorkflows.xml','2019-03-06 14:19:09',8,'EXECUTED','7:37b3c9844fb632f8a0212088202b1024','createTable tableName=project_workflows','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163244-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163244_added_entity_ProjectWorkflows.xml','2019-03-06 14:19:09',9,'EXECUTED','7:593bea18b0ae595140af4cc2ac0df0ba','addColumn tableName=project_workflows','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163245-1','jhipster','config/liquibase/changelog/20190122163245_added_entity_Projects.xml','2019-03-06 14:19:09',10,'EXECUTED','7:19cd4871a3f0f21700f9a367b6ab9c04','createTable tableName=projects; dropDefaultValue columnName=start_time, tableName=projects; dropDefaultValue columnName=end_time, tableName=projects','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163245-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163245_added_entity_Projects.xml','2019-03-06 14:19:09',11,'EXECUTED','7:69cdc1ab417b1a4996b0ff82c8a6ff47','addColumn tableName=projects','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163246-1','jhipster','config/liquibase/changelog/20190122163246_added_entity_ProjectUsers.xml','2019-03-06 14:19:09',12,'EXECUTED','7:f88d1a542af17135e1a13635019be0e8','createTable tableName=project_users','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163246-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163246_added_entity_ProjectUsers.xml','2019-03-06 14:19:10',13,'EXECUTED','7:60869cd7800057657963cffee16d7137','addColumn tableName=project_users','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163247-1','jhipster','config/liquibase/changelog/20190122163247_added_entity_PurchaseOrders.xml','2019-03-06 14:19:10',14,'EXECUTED','7:98f13f338cde0dc6faaaa8a592399a07','createTable tableName=purchase_orders; dropDefaultValue columnName=start_time, tableName=purchase_orders; dropDefaultValue columnName=end_time, tableName=purchase_orders','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163247-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163247_added_entity_PurchaseOrders.xml','2019-03-06 14:19:11',15,'EXECUTED','7:82eae8e14ab2c77157e7589d49a4e4a9','addColumn tableName=purchase_orders','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163248-1','jhipster','config/liquibase/changelog/20190122163248_added_entity_Packages.xml','2019-03-06 14:19:11',16,'EXECUTED','7:f0fd566eaa4becb98ee371deaad23a62','createTable tableName=packages; dropDefaultValue columnName=estimate_delivery, tableName=packages; dropDefaultValue columnName=start_time, tableName=packages; dropDefaultValue columnName=end_time, tableName=packages','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163248-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163248_added_entity_Packages.xml','2019-03-06 14:19:11',17,'EXECUTED','7:8225c657d599a33d7eb1c28c0692e10e','addColumn tableName=packages','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163249-1','jhipster','config/liquibase/changelog/20190122163249_added_entity_Tasks.xml','2019-03-06 14:19:12',18,'EXECUTED','7:6391b485795a2b074ffb0763dd265de6','createTable tableName=tasks; dropDefaultValue columnName=op_estimate_start_time, tableName=tasks; dropDefaultValue columnName=op_estimate_end_time, tableName=tasks; dropDefaultValue columnName=op_start_time, tableName=tasks; dropDefaultValue colum...','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163249-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163249_added_entity_Tasks.xml','2019-03-06 14:19:12',19,'EXECUTED','7:8ba14307d97cc226757170629490eb4b','addColumn tableName=tasks','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163250-1','jhipster','config/liquibase/changelog/20190122163250_added_entity_TMSCustomField.xml','2019-03-06 14:19:12',20,'EXECUTED','7:65b4f43cf2a3e97a2fab5bdffbe9cea5','createTable tableName=tms_custom_field','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163250-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163250_added_entity_TMSCustomField.xml','2019-03-06 14:19:12',21,'EXECUTED','7:e5d5cb458ebc73c84b137c5b1945716f','addColumn tableName=tms_custom_field','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163251-1','jhipster','config/liquibase/changelog/20190122163251_added_entity_TMSCustomFieldScreen.xml','2019-03-06 14:19:12',22,'EXECUTED','7:5992b1a14c4c80f1f9be4ed6d3c91736','createTable tableName=tms_custom_field_screen','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163251-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163251_added_entity_TMSCustomFieldScreen.xml','2019-03-06 14:19:12',23,'EXECUTED','7:b0025f7cac9ede3411b81c4de471c771','addColumn tableName=tms_custom_field_screen','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163252-1','jhipster','config/liquibase/changelog/20190122163252_added_entity_TMSCustomFieldScreenValue.xml','2019-03-06 14:19:12',24,'EXECUTED','7:6bc355acec5fd64a17fc01e4fafdb122','createTable tableName=tms_custom_field_screen_value','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163252-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163252_added_entity_TMSCustomFieldScreenValue.xml','2019-03-06 14:19:13',25,'EXECUTED','7:4a546b76c69d92ee242dd592c869eaae','addColumn tableName=tms_custom_field_screen_value','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163253-1','jhipster','config/liquibase/changelog/20190122163253_added_entity_Issues.xml','2019-03-06 14:19:13',26,'EXECUTED','7:d4d161ca559740ac7b2b2d186903634b','createTable tableName=issues','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163253-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163253_added_entity_Issues.xml','2019-03-06 14:19:13',27,'EXECUTED','7:ff1d54aa1717e854be5c6d52421025b1','addColumn tableName=issues','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163254-1','jhipster','config/liquibase/changelog/20190122163254_added_entity_TMSLogHistory.xml','2019-03-06 14:19:13',28,'EXECUTED','7:dbcda7a89e201b25e6a8c0fedfe22d45','createTable tableName=tms_log_history','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163254-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163254_added_entity_TMSLogHistory.xml','2019-03-06 14:19:13',29,'EXECUTED','7:56e057e0c35e547588fd4a29495b835e','addColumn tableName=tms_log_history','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163255-1','jhipster','config/liquibase/changelog/20190122163255_added_entity_Notes.xml','2019-03-06 14:19:13',30,'EXECUTED','7:3e9a3a606584f4df7ce518f45a342ca1','createTable tableName=notes','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163255-audit-1','jhipster-entity-audit','config/liquibase/changelog/20190122163255_added_entity_Notes.xml','2019-03-06 14:19:13',31,'EXECUTED','7:ad248d9b741e4d8c6f59c219c9d8ae4e','addColumn tableName=notes','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122171158','jhipster','config/liquibase/changelog/20190122171158_added_entity_EntityAuditEvent.xml','2019-03-06 14:19:14',32,'EXECUTED','7:5a696671965a1033d4021cc98168b228','createTable tableName=jhi_entity_audit_event; createIndex indexName=idx_entity_audit_event_entity_id, tableName=jhi_entity_audit_event; createIndex indexName=idx_entity_audit_event_entity_type, tableName=jhi_entity_audit_event; dropDefaultValue co...','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163243-2','jhipster','config/liquibase/changelog/20190122163243_added_entity_constraints_ProjectTemplates.xml','2019-03-06 14:19:14',33,'EXECUTED','7:dda9c72c67a77935abd9165cc427dbb3','addForeignKeyConstraint baseTableName=project_templates, constraintName=fk_project_templates_business_line_id, referencedTableName=business_line','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163244-2','jhipster','config/liquibase/changelog/20190122163244_added_entity_constraints_ProjectWorkflows.xml','2019-03-06 14:19:14',34,'EXECUTED','7:b26307f315f41d500d27588cba14b985','addForeignKeyConstraint baseTableName=project_workflows, constraintName=fk_project_workflows_project_templates_id, referencedTableName=project_templates','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163245-2','jhipster','config/liquibase/changelog/20190122163245_added_entity_constraints_Projects.xml','2019-03-06 14:19:15',35,'EXECUTED','7:8903ae5efd3ba9ffabd36cf4401e2b65','addForeignKeyConstraint baseTableName=projects, constraintName=fk_projects_project_templates_id, referencedTableName=project_templates; addForeignKeyConstraint baseTableName=projects, constraintName=fk_projects_project_lead_id, referencedTableName...','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163246-2','jhipster','config/liquibase/changelog/20190122163246_added_entity_constraints_ProjectUsers.xml','2019-03-06 14:19:16',36,'EXECUTED','7:6935b5d0ba8f9415b457faf3967578f1','addForeignKeyConstraint baseTableName=project_users, constraintName=fk_project_users_project_id, referencedTableName=projects','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163247-2','jhipster','config/liquibase/changelog/20190122163247_added_entity_constraints_PurchaseOrders.xml','2019-03-06 14:19:17',37,'EXECUTED','7:125dd98d63980e3450edfe23ffac4669','addForeignKeyConstraint baseTableName=purchase_orders, constraintName=fk_purchase_orders_project_id, referencedTableName=projects; addForeignKeyConstraint baseTableName=purchase_orders, constraintName=fk_purchase_orders_purchase_order_lead_id, ref...','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163248-2','jhipster','config/liquibase/changelog/20190122163248_added_entity_constraints_Packages.xml','2019-03-06 14:19:17',38,'EXECUTED','7:2159ff1012132c6d3c8587fffd691378','addForeignKeyConstraint baseTableName=packages, constraintName=fk_packages_purchase_orders_id, referencedTableName=purchase_orders','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163249-2','jhipster','config/liquibase/changelog/20190122163249_added_entity_constraints_Tasks.xml','2019-03-06 14:19:18',39,'EXECUTED','7:086b9fa93cbe1a48430fcdd9884bd49c','addForeignKeyConstraint baseTableName=tasks, constraintName=fk_tasks_packages_id, referencedTableName=packages','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163251-2','jhipster','config/liquibase/changelog/20190122163251_added_entity_constraints_TMSCustomFieldScreen.xml','2019-03-06 14:19:19',40,'EXECUTED','7:5eced12c3e285da0c0baa176c9fc90f4','addForeignKeyConstraint baseTableName=tms_custom_field_screen, constraintName=fk_tmscustom_field_screen_tms_custom_field_id, referencedTableName=tms_custom_field; addForeignKeyConstraint baseTableName=tms_custom_field_screen, constraintName=fk_tms...','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163252-2','jhipster','config/liquibase/changelog/20190122163252_added_entity_constraints_TMSCustomFieldScreenValue.xml','2019-03-06 14:19:20',41,'EXECUTED','7:8805c880e1f26856afba734a7123fdaa','addForeignKeyConstraint baseTableName=tms_custom_field_screen_value, constraintName=fk_tmscustom_field_screen_value_purchase_orders_id, referencedTableName=purchase_orders; addForeignKeyConstraint baseTableName=tms_custom_field_screen_value, const...','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163253-2','jhipster','config/liquibase/changelog/20190122163253_added_entity_constraints_Issues.xml','2019-03-06 14:19:21',42,'EXECUTED','7:7ee21eda7e7ce044a4c994bcd022fa1f','addForeignKeyConstraint baseTableName=issues, constraintName=fk_issues_purchase_order_id, referencedTableName=purchase_orders','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163254-2','jhipster','config/liquibase/changelog/20190122163254_added_entity_constraints_TMSLogHistory.xml','2019-03-06 14:19:22',43,'EXECUTED','7:14b8634e12d394744f7084617555c09e','addForeignKeyConstraint baseTableName=tms_log_history, constraintName=fk_tmslog_history_projects_id, referencedTableName=projects; addForeignKeyConstraint baseTableName=tms_log_history, constraintName=fk_tmslog_history_purchase_orders_id, referenc...','',NULL,'3.5.4',NULL,NULL,'1856745994'),('20190122163255-2','jhipster','config/liquibase/changelog/20190122163255_added_entity_constraints_Notes.xml','2019-03-06 14:19:23',44,'EXECUTED','7:e8e8211b0bd41c436926a90001be75a1','addForeignKeyConstraint baseTableName=notes, constraintName=fk_notes_tasks_id, referencedTableName=tasks','',NULL,'3.5.4',NULL,NULL,'1856745994'),('1548661587010-1','TuHP (generated)','config/liquibase/changelog/20190128074615_changelog.xml','2019-03-06 14:19:23',45,'EXECUTED','7:53c664a8d574a010fda1298313aae717','addColumn tableName=jhi_user','',NULL,'3.5.4',NULL,NULL,'1856745994'),('1548661587010-2','TuHP (generated)','config/liquibase/changelog/20190128074615_changelog.xml','2019-03-06 14:19:23',46,'EXECUTED','7:32ce0e4eee9b3435ea99a3fe9233eb0e','addColumn tableName=jhi_user','',NULL,'3.5.4',NULL,NULL,'1856745994'),('1548661587010-3','TuHP (generated)','config/liquibase/changelog/20190128074615_changelog.xml','2019-03-06 14:19:23',47,'EXECUTED','7:bcd2cc83614c6c9d64ad967147c9e39f','addColumn tableName=jhi_user','',NULL,'3.5.4',NULL,NULL,'1856745994'),('1550809248676-1','TuHP (generated)','config/liquibase/changelog/20190222042034_changelog.xml','2019-03-06 14:19:23',48,'EXECUTED','7:051605310bc1bc2ecbeaff6b90a39e89','addColumn tableName=project_workflows','',NULL,'3.5.4',NULL,NULL,'1856745994'),('1550809248676-2','TuHP (generated)','config/liquibase/changelog/20190222042034_changelog.xml','2019-03-06 14:19:23',49,'EXECUTED','7:fb78f42c1327852ef865ff98c24a74b3','dropUniqueConstraint constraintName=phone, tableName=jhi_user','',NULL,'3.5.4',NULL,NULL,'1856745994'),('1550809248676-3','TuHP (generated)','config/liquibase/changelog/20190222042034_changelog.xml','2019-03-06 14:19:24',50,'EXECUTED','7:9afc7c804e6a132c3875680c6ae089d8','addColumn tableName=notes','',NULL,'3.5.4',NULL,NULL,'1856745994'),('1550809248676-4','TuHP (generated)','config/liquibase/changelog/20190222042034_changelog.xml','2019-03-06 14:19:24',51,'EXECUTED','7:85bac75fed93ea586af028c2deca0097','addColumn tableName=tms_custom_field','',NULL,'3.5.4',NULL,NULL,'1856745994');
/*!40000 ALTER TABLE `databasechangelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `databasechangeloglock`
--

DROP TABLE IF EXISTS `databasechangeloglock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `databasechangeloglock`
--

LOCK TABLES `databasechangeloglock` WRITE;
/*!40000 ALTER TABLE `databasechangeloglock` DISABLE KEYS */;
INSERT INTO `databasechangeloglock` VALUES (1,_binary '\0',NULL,NULL);
/*!40000 ALTER TABLE `databasechangeloglock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issues`
--

DROP TABLE IF EXISTS `issues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `issues` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `purchase_order_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_issues_purchase_order_id` (`purchase_order_id`),
  CONSTRAINT `fk_issues_purchase_order_id` FOREIGN KEY (`purchase_order_id`) REFERENCES `purchase_orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issues`
--

LOCK TABLES `issues` WRITE;
/*!40000 ALTER TABLE `issues` DISABLE KEYS */;
/*!40000 ALTER TABLE `issues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_authority`
--

DROP TABLE IF EXISTS `jhi_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `jhi_authority` (
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_authority`
--

LOCK TABLES `jhi_authority` WRITE;
/*!40000 ALTER TABLE `jhi_authority` DISABLE KEYS */;
INSERT INTO `jhi_authority` VALUES ('ROLE_ADMIN'),('ROLE_USER');
/*!40000 ALTER TABLE `jhi_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_entity_audit_event`
--

DROP TABLE IF EXISTS `jhi_entity_audit_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `jhi_entity_audit_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entity_id` bigint(20) NOT NULL,
  `entity_type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `action` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `entity_value` longtext COLLATE utf8_unicode_ci,
  `commit_version` int(11) DEFAULT NULL,
  `modified_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_date` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_entity_audit_event_entity_id` (`entity_id`),
  KEY `idx_entity_audit_event_entity_type` (`entity_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_entity_audit_event`
--

LOCK TABLES `jhi_entity_audit_event` WRITE;
/*!40000 ALTER TABLE `jhi_entity_audit_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `jhi_entity_audit_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_persistent_audit_event`
--

DROP TABLE IF EXISTS `jhi_persistent_audit_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `jhi_persistent_audit_event` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `principal` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `event_date` timestamp NULL DEFAULT NULL,
  `event_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `idx_persistent_audit_event` (`principal`,`event_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_persistent_audit_event`
--

LOCK TABLES `jhi_persistent_audit_event` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `jhi_persistent_audit_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_persistent_audit_evt_data`
--

DROP TABLE IF EXISTS `jhi_persistent_audit_evt_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `jhi_persistent_audit_evt_data` (
  `event_id` bigint(20) NOT NULL,
  `name` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`event_id`,`name`),
  KEY `idx_persistent_audit_evt_data` (`event_id`),
  CONSTRAINT `fk_evt_pers_audit_evt_data` FOREIGN KEY (`event_id`) REFERENCES `jhi_persistent_audit_event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_persistent_audit_evt_data`
--

LOCK TABLES `jhi_persistent_audit_evt_data` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_user`
--

DROP TABLE IF EXISTS `jhi_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `jhi_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password_hash` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `image_url` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `activation_key` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reset_key` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `about` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_user_login` (`login`),
  UNIQUE KEY `idx_user_login` (`login`),
  UNIQUE KEY `ux_user_email` (`email`),
  UNIQUE KEY `idx_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user`
--

LOCK TABLES `jhi_user` WRITE;
/*!40000 ALTER TABLE `jhi_user` DISABLE KEYS */;
INSERT INTO `jhi_user` VALUES (1,'system','$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG','System','System','system@localhost','',_binary '','vi',NULL,NULL,'system','2019-03-06 07:19:06',NULL,'system',NULL,NULL,NULL,NULL),(2,'anonymoususer','$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO','Anonymous','User','anonymous@localhost','',_binary '','vi',NULL,NULL,'system','2019-03-06 07:19:06',NULL,'system',NULL,NULL,NULL,NULL),(3,'admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost','',_binary '','vi',NULL,NULL,'system','2019-03-06 07:19:06',NULL,'system',NULL,NULL,NULL,NULL),(4,'user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','User','User','user@localhost','',_binary '','vi',NULL,NULL,'system','2019-03-06 07:19:06',NULL,'system',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `jhi_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_user_authority`
--

DROP TABLE IF EXISTS `jhi_user_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `jhi_user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`),
  CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user_authority`
--

LOCK TABLES `jhi_user_authority` WRITE;
/*!40000 ALTER TABLE `jhi_user_authority` DISABLE KEYS */;
INSERT INTO `jhi_user_authority` VALUES (1,'ROLE_ADMIN'),(3,'ROLE_ADMIN'),(1,'ROLE_USER'),(3,'ROLE_USER'),(4,'ROLE_USER');
/*!40000 ALTER TABLE `jhi_user_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notes`
--

DROP TABLE IF EXISTS `notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `notes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` longtext COLLATE utf8_unicode_ci,
  `attachment` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tasks_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notes_tasks_id` (`tasks_id`),
  CONSTRAINT `fk_notes_tasks_id` FOREIGN KEY (`tasks_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notes`
--

LOCK TABLES `notes` WRITE;
/*!40000 ALTER TABLE `notes` DISABLE KEYS */;
/*!40000 ALTER TABLE `notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packages`
--

DROP TABLE IF EXISTS `packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `packages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `op` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reviewer` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fi` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `delivery` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `estimate_delivery` timestamp NULL,
  `target` int(11) DEFAULT NULL,
  `start_time` timestamp NULL,
  `end_time` timestamp NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `purchase_orders_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_packages_purchase_orders_id` (`purchase_orders_id`),
  CONSTRAINT `fk_packages_purchase_orders_id` FOREIGN KEY (`purchase_orders_id`) REFERENCES `purchase_orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packages`
--

LOCK TABLES `packages` WRITE;
/*!40000 ALTER TABLE `packages` DISABLE KEYS */;
/*!40000 ALTER TABLE `packages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_templates`
--

DROP TABLE IF EXISTS `project_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `project_templates` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `image` longblob NOT NULL,
  `image_content_type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `business_line_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_project_templates_business_line_id` (`business_line_id`),
  CONSTRAINT `fk_project_templates_business_line_id` FOREIGN KEY (`business_line_id`) REFERENCES `business_line` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_templates`
--

LOCK TABLES `project_templates` WRITE;
/*!40000 ALTER TABLE `project_templates` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_templates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_users`
--

DROP TABLE IF EXISTS `project_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `project_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_login` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `role_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_project_users_project_id` (`project_id`),
  CONSTRAINT `fk_project_users_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_users`
--

LOCK TABLES `project_users` WRITE;
/*!40000 ALTER TABLE `project_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_workflows`
--

DROP TABLE IF EXISTS `project_workflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `project_workflows` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `step` int(11) NOT NULL,
  `input_dto` longtext COLLATE utf8_unicode_ci NOT NULL,
  `op_grid_dto` longtext COLLATE utf8_unicode_ci NOT NULL,
  `pm_grid_dto` longtext COLLATE utf8_unicode_ci NOT NULL,
  `next_uri` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `project_templates_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  `activity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_project_workflows_project_templates_id` (`project_templates_id`),
  CONSTRAINT `fk_project_workflows_project_templates_id` FOREIGN KEY (`project_templates_id`) REFERENCES `project_templates` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_workflows`
--

LOCK TABLES `project_workflows` WRITE;
/*!40000 ALTER TABLE `project_workflows` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_workflows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projects`
--

DROP TABLE IF EXISTS `projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `projects` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `jhi_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `start_time` timestamp NULL,
  `end_time` timestamp NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `project_templates_id` bigint(20) DEFAULT NULL,
  `project_lead_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_projects_project_templates_id` (`project_templates_id`),
  KEY `fk_projects_project_lead_id` (`project_lead_id`),
  CONSTRAINT `fk_projects_project_lead_id` FOREIGN KEY (`project_lead_id`) REFERENCES `project_users` (`id`),
  CONSTRAINT `fk_projects_project_templates_id` FOREIGN KEY (`project_templates_id`) REFERENCES `project_templates` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projects`
--

LOCK TABLES `projects` WRITE;
/*!40000 ALTER TABLE `projects` DISABLE KEYS */;
/*!40000 ALTER TABLE `projects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `purchase_orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_time` timestamp NULL,
  `end_time` timestamp NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `project_id` bigint(20) DEFAULT NULL,
  `purchase_order_lead_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_purchase_orders_project_id` (`project_id`),
  KEY `fk_purchase_orders_purchase_order_lead_id` (`purchase_order_lead_id`),
  CONSTRAINT `fk_purchase_orders_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`),
  CONSTRAINT `fk_purchase_orders_purchase_order_lead_id` FOREIGN KEY (`purchase_order_lead_id`) REFERENCES `project_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_orders`
--

LOCK TABLES `purchase_orders` WRITE;
/*!40000 ALTER TABLE `purchase_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tasks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `severity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `priority` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `data` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `file_name` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `jhi_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `availability` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `frame` int(11) DEFAULT NULL,
  `actual_object` int(11) DEFAULT NULL,
  `op_status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `op_estimate_start_time` timestamp NULL,
  `op_estimate_end_time` timestamp NULL,
  `op_start_time` timestamp NULL,
  `op_end_time` timestamp NULL,
  `review_1_status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `review_1_start_time` timestamp NULL,
  `review_1_end_time` timestamp NULL,
  `fix_status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fix_start_time` timestamp NULL,
  `fix_end_time` timestamp NULL,
  `review_2_status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `review_2_start_time` timestamp NULL,
  `review_2_end_time` timestamp NULL,
  `fi_status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fi_start_time` timestamp NULL,
  `fi_end_time` timestamp NULL,
  `duration` int(11) DEFAULT NULL,
  `target` int(11) DEFAULT NULL,
  `error_quantity` int(11) DEFAULT NULL,
  `error_severity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `parent` bigint(20) DEFAULT NULL,
  `op` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `review_1` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `review_2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fixer` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fi` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `packages_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tasks_packages_id` (`packages_id`),
  CONSTRAINT `fk_tasks_packages_id` FOREIGN KEY (`packages_id`) REFERENCES `packages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tms_custom_field`
--

DROP TABLE IF EXISTS `tms_custom_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tms_custom_field` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `jhi_type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `default_value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `possible_values` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `valid_regex` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  `label` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tms_custom_field`
--

LOCK TABLES `tms_custom_field` WRITE;
/*!40000 ALTER TABLE `tms_custom_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `tms_custom_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tms_custom_field_screen`
--

DROP TABLE IF EXISTS `tms_custom_field_screen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tms_custom_field_screen` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `jhi_sequence` int(11) NOT NULL,
  `tms_custom_field_id` bigint(20) DEFAULT NULL,
  `project_workflows_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tms_custom_field_id` (`tms_custom_field_id`),
  KEY `fk_tmscustom_field_screen_project_workflows_id` (`project_workflows_id`),
  CONSTRAINT `fk_tmscustom_field_screen_project_workflows_id` FOREIGN KEY (`project_workflows_id`) REFERENCES `project_workflows` (`id`),
  CONSTRAINT `fk_tmscustom_field_screen_tms_custom_field_id` FOREIGN KEY (`tms_custom_field_id`) REFERENCES `tms_custom_field` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tms_custom_field_screen`
--

LOCK TABLES `tms_custom_field_screen` WRITE;
/*!40000 ALTER TABLE `tms_custom_field_screen` DISABLE KEYS */;
/*!40000 ALTER TABLE `tms_custom_field_screen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tms_custom_field_screen_value`
--

DROP TABLE IF EXISTS `tms_custom_field_screen_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tms_custom_field_screen_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `jhi_value` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `text` longtext COLLATE utf8_unicode_ci,
  `purchase_orders_id` bigint(20) DEFAULT NULL,
  `packages_id` bigint(20) DEFAULT NULL,
  `tasks_id` bigint(20) DEFAULT NULL,
  `tms_custom_field_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tmscustom_field_screen_value_purchase_orders_id` (`purchase_orders_id`),
  KEY `fk_tmscustom_field_screen_value_packages_id` (`packages_id`),
  KEY `fk_tmscustom_field_screen_value_tasks_id` (`tasks_id`),
  KEY `fk_tmscustom_field_screen_value_tms_custom_field_id` (`tms_custom_field_id`),
  CONSTRAINT `fk_tmscustom_field_screen_value_packages_id` FOREIGN KEY (`packages_id`) REFERENCES `packages` (`id`),
  CONSTRAINT `fk_tmscustom_field_screen_value_purchase_orders_id` FOREIGN KEY (`purchase_orders_id`) REFERENCES `purchase_orders` (`id`),
  CONSTRAINT `fk_tmscustom_field_screen_value_tasks_id` FOREIGN KEY (`tasks_id`) REFERENCES `tasks` (`id`),
  CONSTRAINT `fk_tmscustom_field_screen_value_tms_custom_field_id` FOREIGN KEY (`tms_custom_field_id`) REFERENCES `tms_custom_field` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tms_custom_field_screen_value`
--

LOCK TABLES `tms_custom_field_screen_value` WRITE;
/*!40000 ALTER TABLE `tms_custom_field_screen_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `tms_custom_field_screen_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tms_log_history`
--

DROP TABLE IF EXISTS `tms_log_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tms_log_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `old_value` longtext COLLATE utf8_unicode_ci,
  `new_value` longtext COLLATE utf8_unicode_ci,
  `projects_id` bigint(20) DEFAULT NULL,
  `purchase_orders_id` bigint(20) DEFAULT NULL,
  `packages_id` bigint(20) DEFAULT NULL,
  `tasks_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tmslog_history_projects_id` (`projects_id`),
  KEY `fk_tmslog_history_purchase_orders_id` (`purchase_orders_id`),
  KEY `fk_tmslog_history_packages_id` (`packages_id`),
  KEY `fk_tmslog_history_tasks_id` (`tasks_id`),
  CONSTRAINT `fk_tmslog_history_packages_id` FOREIGN KEY (`packages_id`) REFERENCES `packages` (`id`),
  CONSTRAINT `fk_tmslog_history_projects_id` FOREIGN KEY (`projects_id`) REFERENCES `projects` (`id`),
  CONSTRAINT `fk_tmslog_history_purchase_orders_id` FOREIGN KEY (`purchase_orders_id`) REFERENCES `purchase_orders` (`id`),
  CONSTRAINT `fk_tmslog_history_tasks_id` FOREIGN KEY (`tasks_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tms_log_history`
--

LOCK TABLES `tms_log_history` WRITE;
/*!40000 ALTER TABLE `tms_log_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `tms_log_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-06 14:22:43
