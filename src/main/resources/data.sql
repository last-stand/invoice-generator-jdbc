INSERT INTO PRODUCT(name, unit_price) VALUES('Honey', 100);
INSERT INTO PRODUCT(name, unit_price) VALUES('Corn', 33);
INSERT INTO PRODUCT(name, unit_price) VALUES('Chocos', 60);

INSERT INTO INVOICE(customer, date) VALUES('Jack', CURRENT_TIMESTAMP());
INSERT INTO INVOICE(customer, date) VALUES('Sparrow', CURRENT_TIMESTAMP());

INSERT INTO INVOICE_ITEM(quantity, product_id, invoice_id) VALUES(2 , 1, 1);
INSERT INTO INVOICE_ITEM(quantity, product_id, invoice_id) VALUES(4 , 2, 1);
INSERT INTO INVOICE_ITEM(quantity, product_id, invoice_id) VALUES(10 , 3, 2);

INSERT INTO TAX(product_id, tax_type, rate) VALUES(1 , 'SalesTax', 0.01);
INSERT INTO TAX(product_id, tax_type, rate) VALUES(1 , 'GST', 0.1);
INSERT INTO TAX(product_id, tax_type, rate) VALUES(1 , 'CGST', 2);
INSERT INTO TAX(product_id, tax_type, rate) VALUES(2 , 'VAT', 0.3);
INSERT INTO TAX(product_id, tax_type, rate) VALUES(2 , 'GST', 0.02);
INSERT INTO TAX(product_id, tax_type, rate) VALUES(3 , 'SalesTax', 1);
INSERT INTO TAX(product_id, tax_type, rate) VALUES(3 , 'VAT', 2.3);
