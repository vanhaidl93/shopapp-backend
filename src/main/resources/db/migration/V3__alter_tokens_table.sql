
-- add `is_mobile` include condition.
SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'shopapp'
  AND TABLE_NAME = 'tokens'
  AND COLUMN_NAME = 'is_mobile';

SELECT
  IF(@columnCount = 0,
     'ALTER TABLE tokens ADD COLUMN is_mobile TINYINT(1) DEFAULT 0;',
     'SELECT ''Column is_mobile already exists'';')
     INTO @alterStatement;

PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- add `refresh_token` include condition.
SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'shopapp'
  AND TABLE_NAME = 'tokens'
  AND COLUMN_NAME = 'refresh_token';

SELECT
  IF(@columnCount = 0,
     'ALTER TABLE tokens ADD COLUMN refresh_token VARCHAR(255) DEFAULT '''';',
     'SELECT ''Column refresh_token already exists'';')
     INTO @alterStatement;

PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- add `refresh_expiration_date` include condition.
SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'shopapp'
  AND TABLE_NAME = 'tokens'
  AND COLUMN_NAME = 'refresh_expiration_date';

SELECT
  IF(@columnCount = 0,
     'ALTER TABLE tokens ADD COLUMN refresh_expiration_date datetime;',
     'SELECT ''Column refresh_expiration_date already exists'';')
     INTO @alterStatement;

PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
