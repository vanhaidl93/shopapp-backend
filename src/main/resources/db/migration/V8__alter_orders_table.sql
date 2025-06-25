-- Drop the existing foreign key constraint
ALTER TABLE orders
ADD COLUMN vnp_txn_ref VARCHAR(255) DEFAULT '';
