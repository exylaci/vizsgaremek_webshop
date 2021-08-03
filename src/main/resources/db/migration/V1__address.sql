USE webshop;

CREATE TABLE IF NOT EXISTS addresses(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    city varchar(50) not null,
    zip_code varchar(4) not null,
    street_house varchar(100) not null,
    comment varchar(255),
    customer_id bigint);