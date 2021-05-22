DROP DATABASE IF EXISTS beer;
DROP USER IF EXISTS `BeerAppId`@`%`;
CREATE DATABASE IF NOT EXISTS beer CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `BeerAppId`@`%` IDENTIFIED WITH mysql_native_password BY 'eagles123';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `beer`.* TO `BeerAppId`@`%`;
FLUSH PRIVILEGES;