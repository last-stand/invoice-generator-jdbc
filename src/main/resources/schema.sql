DROP TABLE IF EXISTS INVOICE;
CREATE TABLE INVOICE(
  id integer IDENTITY(1,1) PRIMARY KEY,
  customer varchar(255),
  date varchar (255)
);

DROP TABLE IF EXISTS PRODUCT;
CREATE TABLE PRODUCT(
  id integer IDENTITY(1,1) PRIMARY KEY,
  name varchar(255),
  unit_price double
);

DROP TABLE IF EXISTS INVOICE_ITEM;
CREATE TABLE INVOICE_ITEM(
  id integer IDENTITY(1,1) PRIMARY KEY,
  quantity integer,
  product_id integer,
  invoice_item_id integer,
  CONSTRAINT fk_product_id foreign key (product_id) references PRODUCT(id),
  CONSTRAINT fk_invoice_id foreign key (invoice_item_id) references INVOICE(id)
);

-- ALTER TABLE INVOICE_ITEM
--     ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id)
--     REFERENCES PRODUCT(id);
--
-- ALTER TABLE INVOICE_ITEM
--     ADD CONSTRAINT fk_invoice_id FOREIGN KEY (invoice_item_id)
--     REFERENCES INVOICE(id);