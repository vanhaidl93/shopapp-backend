-- Drop the existing foreign key constraint
ALTER TABLE products
DROP FOREIGN KEY products_ibfk_1;

-- Recreate it with ON DELETE SET NULL
ALTER TABLE products
ADD CONSTRAINT products_ibfk_1 FOREIGN KEY (category_id) REFERENCES categories(id)
ON DELETE SET NULL;
