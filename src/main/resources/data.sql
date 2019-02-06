INSERT INTO PRODUCT(name, unit_price) VALUES('Honey', 100);
INSERT INTO PRODUCT(name, unit_price) VALUES('Corn', 33);
INSERT INTO PRODUCT(name, unit_price) VALUES('Chocos', 60);

INSERT INTO INVOICE(customer, date) VALUES('Jack', CURRENT_TIMESTAMP());
INSERT INTO INVOICE(customer, date) VALUES('Sparrow', CURRENT_TIMESTAMP());

INSERT INTO INVOICE_ITEM(quantity, product_id, invoice_item_id) VALUES(2 , 1, 1);
INSERT INTO INVOICE_ITEM(quantity, product_id, invoice_item_id) VALUES(4 , 2, 1);

INSERT INTO INVOICE_ITEM(quantity, product_id, invoice_item_id) VALUES(10 , 3, 2);