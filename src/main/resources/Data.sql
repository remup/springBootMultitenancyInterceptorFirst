DROP TABLE IF EXISTS Customer;
 
CREATE TABLE Customer (
  customer_id INT PRIMARY KEY,
 customer_name VARCHAR(50) NOT NULL
);
 
INSERT INTO Customer (customer_id, customer_name) VALUES
  (1,'Aliiiii'),
  (2,'Billiiiii');