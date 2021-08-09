CREATE TABLE IF NOT EXISTS orders(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_date DATE,
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id));

CREATE TABLE IF NOT EXISTS ordered_products(
    order_id BIGINT,
    piece INT,
    product_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id));