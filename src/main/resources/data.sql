CREATE TABLE persons (
    id INT AUTO_INCREMENT PRIMARY KEY, 
    first_name VARCHAR(250) NOT NULL, 
    last_name VARCHAR(250) NOT NULL, 
    adress VARCHAR(250) NOT NULL, 
    city VARCHAR(250) NOT NULL, 
    zip INT NOT NULL, 
    phone VARCHAR(15) NOT NULL, 
    email VARCHAR(250) NOT NULL
);

CREATE TABLE firestations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    adress VARCHAR(250) NOT NULL,
    station INT NOT NULL
);


CREATE TABLE medicalrecords (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  birthdate DATE NOT NULL,
  medication VARCHAR(250) NOT NULL
);

INSERT INTO persons (first_name, last_name, adress, city, zip, phone, email)
VALUES ('John', 'Doe', '123 Main St', 'Springfield', '12345', '555-1234', 'john.doe@example.com');