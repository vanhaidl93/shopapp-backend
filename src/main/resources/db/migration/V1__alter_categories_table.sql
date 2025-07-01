
-- categories table, add externally UNIQUE property.
ALTER TABLE categories
MODIFY name VARCHAR(50) UNIQUE;
