CREATE
DATABASE ShopApp;
USE
ShopApp;
-- user whenever they wanna purchase ==> they need to register the account ==> create users table
CREATE TABLE users
(
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    fullname            VARCHAR(100)          DEFAULT '',
    phone_number        VARCHAR(10)  NOT NULL,
    address             VARCHAR(200)          DEFAULT '',
    password            VARCHAR(100) NOT NULL DEFAULT '',
    created_at          DATETIME,
    updated_at          DATETIME,
    is_active           TINYINT(1) DEFAULT 1,
    date_of_birth       DATE,
    facebook_account_id INT                   DEFAULT 0,
    google_account_id   INT                   DEFAULT 0
);
ALTER TABLE users
    ADD COLUMN role_id INT;

-- roles of user, roles table
CREATE TABLE roles
(
    id   INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);
ALTER TABLE users
    ADD FOREIGN KEY (role_id) REFERENCES roles (id);

-- token table that's fetched from Auth server of Social Provider, using to authentication.
CREATE TABLE tokens
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    token           VARCHAR(255) UNIQUE NOT NULL,
    token_type      VARCHAR(50)         NOT NULL,
    expiration_date DATETIME,
    revoked         TINYINT(1) NOT NULL,
    expired         TINYINT(1) NOT NULL,
    user_id         INT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- social_accounts table,
CREATE TABLE social_accounts
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    provider    VARCHAR(20)  NOT NULL COMMENT 'Tên nhà cung cấp social network',
    proivder_id VARCHAR(50)  NOT NULL,
    email       VARCHAR(150) NOT NULL COMMENT 'Email tài khoản',
    name        VARCHAR(100) NOT NULL COMMENT 'Tên người dùng',
    user_id     INT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Categories Table, 'electoric, housewares'...
CREATE TABLE categories
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT'Tên danh mục, vd: đồ điện tử'
);

-- products table, 'Iphone 15pro, macbook air 15 inch 2023"
CREATE TABLE products
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(350) COMMENT 'Tên sản phẩm',
    price       FLOAT NOT NULL CHECK (price >= 0),
    thumnail    VARCHAR(300) DEFAULT '',
    description LONGTEXT     DEFAULT '',
    created_at  DATETIME,
    updated_at  DATETIME,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

-- product_images, include the info images per one product, loading multiple image for one product.
CREATE TABLE product_images
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    image_url  VARCHAR(300),
);

-- orders table relate to the info of one order ( fullname, phone, total prices,
-- order_details ( 3xLaptop + 2 brushes..))
CREATE TABLE orders
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    user_id      INT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    fullname     VARCHAR(100) DEFAULT '',
    email        VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(20)  NOT NULL,
    address      VARCHAR(200) NOT NULL,
    note         VARCHAR(100) DEFAULT '',
    order_date   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    status       VARCHAR(20),
    total_money  FLOAT CHECK (total_money >= 0)
);

ALTER TABLE orders
    ADD COLUMN shipping_method VARCHAR(100);
ALTER TABLE orders
    ADD COLUMN shipping_address VARCHAR(200);
ALTER TABLE orders
    ADD COLUMN shipping_date DATE;
ALTER TABLE orders
    ADD COLUMN racking_number VARCHAR(100);
ALTER TABLE orders
    ADD COLUMN payment_method VARCHAR(100);
-- delete order, using active field, not remove specify record.
ALTER TABLE orders
    ADD COLUMN active TINYINT(1);
-- defining specifical status for status field.
ALTER TABLE orders MODIFY COLUMN status ENUM('pending', 'processing','shipped','deliveried','cancelled')
    COMMENT 'Trạng thái đơn hàng';

-- order_details table,
CREATE TABLE order_details
(
    id                 INT PRIMARY KEY AUTO_INCREMENT,
    order_id           INT,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    product_id         INT,
    FOREIGN KEY (product_id) REFERENCES products (id),
    price              FLOAT CHECK (price >= 0),
    number_of_products INT CHECK (number_of_products > 0),
    total_money        FLOAT CHECK (total_money >= 0),
    color              VARCHAR(20) DEFAULT ''
);