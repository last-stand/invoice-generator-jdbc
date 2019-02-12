DROP TABLE IF EXISTS INVOICE;
CREATE TABLE INVOICE(
  invoice_id integer IDENTITY(1,1) PRIMARY KEY,
  customer varchar(255),
  date varchar (255)
);

DROP TABLE IF EXISTS PRODUCT;
CREATE TABLE PRODUCT(
  product_id integer IDENTITY(1,1) PRIMARY KEY,
  name varchar(255),
  unit_price double
);

DROP TABLE IF EXISTS INVOICE_ITEM;
CREATE TABLE INVOICE_ITEM(
  invoice_item_id integer IDENTITY(1,1) PRIMARY KEY,
  quantity integer,
  product_id integer,
  invoice_id integer,
  CONSTRAINT fk_product_id foreign key (product_id) references PRODUCT(product_id),
  CONSTRAINT fk_invoice_id foreign key (invoice_id) references INVOICE(invoice_id)
);

DROP TABLE IF EXISTS TAX;
CREATE TABLE TAX(
  tax_id integer IDENTITY(1,1) PRIMARY KEY,
  product_id integer,
  tax_type varchar(255),
  rate DECIMAL(4,2),
  CONSTRAINT fk_tax_product_id foreign key (product_id) references PRODUCT(product_id)
);
