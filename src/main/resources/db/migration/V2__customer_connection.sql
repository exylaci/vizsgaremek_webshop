CREATE TABLE IF NOT EXISTS customers(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    customer_name varchar(100) not null,
    email_address varchar(100) not null,
    comment varchar(255),
    delivery_address_id bigint,
    invoice_address_id bigint);

CREATE TABLE IF NOT EXISTS addresses_customers(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    addresses_id bigint,
    customers_id bigint,
    FOREIGN KEY(addresses_id) REFERENCES addresses(id),
    FOREIGN KEY(customers_id) REFERENCES customers(id));