
-- Drop the existing foreign key constraint
ALTER TABLE comments
DROP FOREIGN KEY comments_ibfk_2;

-- Recreate it with ON DELETE SET NULL
ALTER TABLE comments
ADD CONSTRAINT comments_ibfk_2 FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE `users`
MODIFY `google_account_id` VARCHAR(50) DEFAULT '',
MODIFY `facebook_account_id` VARCHAR(50) DEFAULT '',
ADD COLUMN `profile_image` VARCHAR(255) DEFAULT '';


