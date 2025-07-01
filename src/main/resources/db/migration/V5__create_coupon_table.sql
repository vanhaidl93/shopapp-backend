CREATE TABLE IF NOT EXISTS coupons (
  id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE IF NOT EXISTS coupon_conditions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  coupon_id INT NOT NULL,
  attribute VARCHAR(255) NOT NULL,
  operator VARCHAR(10) NOT NULL,
  value VARCHAR(255) NOT NULL,
  discount_amount DECIMAL(5, 2) NOT NULL,
  FOREIGN KEY (coupon_id) REFERENCES coupons(id)
);


SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'shopapp'
  AND TABLE_NAME = 'orders'
  AND COLUMN_NAME = 'coupon_id';

SELECT
  IF(@columnCount = 0,
     'ALTER TABLE orders ADD COLUMN coupon_id INT, ADD CONSTRAINT fk_orders_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id);',
     'SELECT ''Column coupon_id already exists'';')
     INTO @alterStatement;

PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'shopapp'
  AND TABLE_NAME = 'order_details'
  AND COLUMN_NAME = 'coupon_id';

SELECT
  IF(@columnCount = 0,
     'ALTER TABLE order_details ADD COLUMN coupon_id INT, ADD CONSTRAINT fk_order_details_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id);',
     'SELECT ''Column coupon_id already exists'';')
     INTO @alterStatement;

PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;