delete from address;
delete from customer;
delete from roles;

INSERT INTO roles ("id", "name") VALUES
(0,	'Admin'),
(1,	'User');

INSERT INTO customer ("id", "name", "last_name", "email", "date_of_birth", "password", "role") VALUES
(1,	'John',	'Rambo',	'john@firstblood.com',	'2018-11-27', '1234',	1),
(2,	'Pavel Updated',	'Sharlan',	'pasha.sharlan@gmail.com',	'2018-11-28',	'1234',	0);


INSERT INTO address ("id", "country", "city", "street", "house_number", "customer_id") VALUES
(1,	'Belarus',	'Minsk',	'Chkalova', 9, 2);
