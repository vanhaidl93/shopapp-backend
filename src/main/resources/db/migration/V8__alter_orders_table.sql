
SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'shopapp'
  AND TABLE_NAME = 'orders'
  AND COLUMN_NAME = 'vnp_txn_ref';

SELECT
  IF(@columnCount = 0,
     'ALTER TABLE `orders` ADD COLUMN `vnp_txn_ref` VARCHAR(255) DEFAULT '';',
     'SELECT ''Column vnp_txn_ref already exists'';')
     INTO @alterStatement;

PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;