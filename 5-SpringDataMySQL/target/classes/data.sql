CREATE DATABASE IF NOT EXISTS SpringDataMySQL;

use SpringDataMySQL;

-- Insert initial data into the User table
INSERT INTO user (username, email, last_name) VALUES
('john_doe', 'john@example.com', 'Doe'),
('jane_smith', 'jane@example.com', 'Smith');

-- Insert initial data into the Customer table
INSERT INTO customer (name, hand_phone_number, address) VALUES
('Bob Lee', '81234567', '8 Raffles Ave'),
('Alicia Ng', '98765432', '35 Marina Blvd');