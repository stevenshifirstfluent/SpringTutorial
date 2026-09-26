use advancedcoursedemo;
-- Truncate the course table
TRUNCATE TABLE course;

-- Insert initial data with image paths
INSERT INTO course (id, name, image_path) VALUES (1, 'Spring Boot Basics', '/images/springboot.jpg');
INSERT INTO course (id, name, image_path) VALUES (2, 'Java 101', '/images/java101.jpg');
INSERT INTO course (id, name, image_path) VALUES (3, 'Introduction to Hibernate', '/images/hibernate.jpg');

