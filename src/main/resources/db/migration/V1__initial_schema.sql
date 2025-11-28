-- ============================================================
-- INITIAL SCHEMA FOR ENTERPRISE PRODUCT API
-- Tables: categories, products, orders
-- Flyway Version: V1
-- ============================================================

-- Drop tables if already exist (optional for dev)
-- DROP TABLE IF EXISTS orders;
-- DROP TABLE IF EXISTS products;
-- DROP TABLE IF EXISTS categories;

-- ============================================================
-- TABLE: categories
-- ============================================================
CREATE TABLE categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL UNIQUE,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: products
-- ============================================================
CREATE TABLE products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    version BIGINT DEFAULT 0,
    category_id BIGINT,
    PRIMARY KEY (id),

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Indexes for products
CREATE INDEX idx_product_name ON products(name);
CREATE INDEX idx_product_price ON products(price);
CREATE INDEX idx_product_category ON products(category_id);

-- ============================================================
-- TABLE: orders
-- ============================================================
CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    quantity INT NOT NULL,
    order_date DATETIME,
    product_id BIGINT,
    PRIMARY KEY (id),

    CONSTRAINT fk_orders_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;
