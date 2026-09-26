CREATE DATABASE IF NOT EXISTS advancedcoursedemo;

use advancedcoursedemo;

CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Primary key with auto-increment
    name VARCHAR(255) NOT NULL,            -- Course name
    imagePath VARCHAR(255)                 -- Path or URL to the course image
);

