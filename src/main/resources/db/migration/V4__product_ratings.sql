CREATE TABLE IF NOT EXISTS products(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_name varchar(100) NOT NULL,
    unit_price INTEGER,
    piece INTEGER,
    category_id bigint,
    product_description varchar(255),
    FOREIGN KEY(category_id) REFERENCES product_category_types(id));

CREATE TABLE IF NOT EXISTS ratings(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    ratings INTEGER,
    FOREIGN KEY(product_id) REFERENCES products(id));