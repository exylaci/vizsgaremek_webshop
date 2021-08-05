alter table customers add foreign key (delivery_address_id) references addresses (id);

alter table customers add foreign key (invoice_address_id) references addresses (id);