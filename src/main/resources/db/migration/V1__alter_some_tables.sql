
-- categories table, add externally UNIQUE property.
ALTER TABLE categories MODIFY name VARCHAR(50) UNIQUE;

-- products table, change FLOAT to DECIMAL(10,2), price colum.
ALTER TABLE products MODIFY price DECIMAL(10,2);

-- products table, modify VARCHAR(255), thumbnail column.
ALTER TABLE products MODIFY thumbnail VARCHAR(255);

-- users table.
ALTER TABLE `users` MODIFY COLUMN `phone_number` VARCHAR(15);
ALTER TABLE `users` MODIFY COLUMN `password` CHAR(68) NOT NULL;
ALTER TABLE `users` ALTER COLUMN `role_id` SET DEFAULT 1;

-- order_details table.
ALTER TABLE `order_details` MODIFY COLUMN `price` DECIMAL(10, 2),
    MODIFY COLUMN `number_of_products` INT DEFAULT 1,
    MODIFY COLUMN `total_money` DECIMAL(10, 2) DEFAULT 0;
