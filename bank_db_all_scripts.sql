CREATE DATABASE `bank` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `clients` (
                           `client_id` int NOT NULL AUTO_INCREMENT,
                           `name` varchar(45) NOT NULL,
                           `address` varchar(45) DEFAULT NULL,
                           `client_code` text NOT NULL,
                           `client_password` text NOT NULL,
                           PRIMARY KEY (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accounts` (
                            `account_number` varchar(19) NOT NULL,
                            `client_id` int NOT NULL,
                            `amount` double NOT NULL DEFAULT '0',
                            `currency` enum('RON','EUR') NOT NULL,
                            `IBAN` varchar(34) NOT NULL,
                            PRIMARY KEY (`account_number`),
                            KEY `fsd_idx` (`client_id`),
                            CONSTRAINT `fk_client_account` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `conversions` (
                               `from` varchar(3) NOT NULL,
                               `to` varchar(3) NOT NULL,
                               `value` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO bank.accounts (account_number, client_id, amount, currency, IBAN) VALUES ('2341-4356-6547-6453', 2, 160, 'RON', 'AT551100068727871635');
INSERT INTO bank.accounts (account_number, client_id, amount, currency, IBAN) VALUES ('0000-0000-0000-0000', 1, 398, 'EUR', 'IE12BOFI90000112345678');
INSERT INTO bank.accounts (account_number, client_id, amount, currency, IBAN) VALUES ('0000-0000-0000-0001', 1, 680, 'EUR', 'IE12BOFI90000145345678');

INSERT INTO bank.clients (client_id, name, address, client_code, client_password) VALUES (1, 'Naomi Watts', '21st Downing street', 'badalent', 'ziggystardust');
INSERT INTO bank.clients (client_id, name, address, client_code, client_password) VALUES (2, 'Thomasin MacKenzie', '61st Darwin Street', 'leiber', 'senate');

INSERT INTO bank.conversions (`from`, `to`, value) VALUES ('RON', 'EUR', 0.2);
INSERT INTO bank.conversions (`from`, `to`, value) VALUES ('EUR', 'RON', 4.93);
