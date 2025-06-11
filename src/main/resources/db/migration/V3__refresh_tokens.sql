

ALTER TABLE shopapp.tokens
ADD COLUMN IF NOT EXISTS refresh_token VARCHAR(255) DEFAULT'';


ALTER TABLE shopapp.tokens
ADD COLUMN IF NOT EXISTS refresh_expiration_date datetime;