
CREATE DATABASE IF NOT EXISTS SpringDataMySQL;
use SpringDataMySQL;

-- Schema for the User table
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    lastName VARCHAR(100)
);

TRUNCATE TABLE user;

-- Schema for the Customer table

CREATE TABLE IF NOT EXISTS customer (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(35) NOT NULL,
  hand_phone_number VARCHAR(8),
  address VARCHAR(35)
);

TRUNCATE TABLE customer;