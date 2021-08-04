CREATE TABLE IF NOT EXISTS products(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_name varchar(100) not null,
    unit_price int;
    piece int;
    id bigint category_id NOT NULL,
    product_description varchar(255));