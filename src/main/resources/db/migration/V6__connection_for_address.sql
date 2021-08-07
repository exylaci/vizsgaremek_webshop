alter table addresses
    drop column customer_id;

ALTER TABLE addresses_customers
    RENAME COLUMN addresses_id TO address_id;