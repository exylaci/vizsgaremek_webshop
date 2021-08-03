CREATE TABLE IF NOT EXISTS product_category_types(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_category_type varchar(100) not null,
    category_description varchar(255));