
ALTER TABLE `users`
MODIFY COLUMN `phone_number` VARCHAR(15),
MODIFY COLUMN `password` CHAR(68) NOT NULL,
ALTER COLUMN `role_id` SET DEFAULT 1,
MODIFY `google_account_id` VARCHAR(50) DEFAULT '',
MODIFY `facebook_account_id` VARCHAR(50) DEFAULT '';


SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'shopapp'
  AND TABLE_NAME = 'users'
  AND COLUMN_NAME = 'profile_image';

SELECT
  IF(@columnCount = 0,
     'ALTER TABLE `users`ADD COLUMN `profile_image` VARCHAR(255) DEFAULT '';',
     'SELECT ''Column profile_image already exists'';')
     INTO @alterStatement;

PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;








