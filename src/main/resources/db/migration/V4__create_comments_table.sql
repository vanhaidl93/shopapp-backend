CREATE TABLE IF NOT EXISTS comments (
  id INT PRIMARY KEY AUTO_INCREMENT,
  product_id INT,
  user_id INT,
  content VARCHAR(255),
  created_at DATETIME,
  updated_at DATETIME,
  FOREIGN KEY (product_id) REFERENCES products(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Drop the existing foreign key constraint
ALTER TABLE comments
DROP FOREIGN KEY comments_ibfk_2;

-- Recreate it with ON DELETE SET NULL
ALTER TABLE comments
ADD CONSTRAINT comments_ibfk_2 FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE;