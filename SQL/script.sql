create schema if not exists book_store collate utf8mb4_0900_ai_ci;

create table if not exists AUTHOR
(
	author_id int auto_increment
		primary key,
	name varchar(50) not null,
	constraint AUTHOR_name_uindex
		unique (name)
);

create table if not exists PUBLISHER
(
	publisher_id int auto_increment
		primary key,
	name varchar(50) not null,
	phone varchar(20) null,
	address varchar(50) null
);

create table if not exists BOOK
(
	book_id int not null
		primary key,
	title varchar(50) not null,
	pub_year date null,
	selling_price float not null,
	category enum('Science', 'Art', 'Religion', 'History', 'Geography') null,
	quantity int(11) unsigned not null,
	publisher_id int not null,
	threshold int(11) unsigned not null,
	constraint BOOK_title_uindex
		unique (title),
	constraint BOOK_PUBLISHER_Name_fk
		foreign key (publisher_id) references PUBLISHER (publisher_id)
			on update cascade on delete cascade
);

create table if not exists AUTHORED_BY
(
	author_id int not null,
	book_id int not null,
	primary key (book_id, author_id),
	constraint AUTHORED_BY_AUTHORS_author_id_fk
		foreign key (author_id) references AUTHOR (author_id)
			on update cascade on delete cascade,
	constraint Author_BOOK_Book_id_fk
		foreign key (book_id) references BOOK (book_id)
			on update cascade on delete cascade
);

create index BOOK_category_index
	on BOOK (category);

create definer = root@localhost trigger book_insertion
after INSERT on BOOK
for each row
BEGIN
    if (NEW.quantity < new.threshold)
    then
      insert into `ORDER` values (order_id, new.book_id, NEW.threshold - NEW.quantity, current_date);
    end if;
  END;

create definer = root@localhost trigger book_update
after UPDATE on BOOK
for each row
BEGIN
    declare sum_of_orders int default get_orders_quantity(NEW.book_id);
    if (NEW.quantity + sum_of_orders < new.threshold)
    then
      insert into `ORDER`
      values (order_id, new.book_id, new.threshold - NEW.quantity - sum_of_orders, current_date);
    end if;
  END;

create table if not exists `ORDER`
(
	order_id int auto_increment
		primary key,
	book_id int not null,
	quantity int not null,
	date date not null,
	constraint ORDER_BOOK__fk
		foreign key (book_id) references BOOK (book_id)
			on update cascade on delete cascade
);

create definer = root@localhost trigger confirm_orders
after DELETE on `ORDER`
for each row
BEGIN
    update BOOK set BOOK.quantity = BOOK.quantity + OLD.quantity where BOOK.book_id = OLD.book_id;
  END;

create fulltext index index_on_publisher_name
	on PUBLISHER (name);

create table if not exists USER
(
	user_id int auto_increment
		primary key,
	first_name varchar(20) not null,
	last_name varchar(20) not null,
	email_address varchar(50) not null,
	phone_number varchar(20) null,
	shipping_address varchar(50) not null,
	password varchar(50) not null,
	promoted tinyint(1) not null,
	constraint USER_email_address_uindex
		unique (email_address)
);

create table if not exists CART
(
	book_id int not null,
	user_id int not null,
	quantity int not null,
	in_cart tinyint(1) default 1 not null,
	primary key (user_id, book_id),
	constraint CART_BOOK_Book_id_fk
		foreign key (book_id) references BOOK (book_id)
			on update cascade on delete cascade,
	constraint CART_USER_user_id_fk
		foreign key (user_id) references USER (user_id)
			on update cascade on delete cascade
);

create definer = root@localhost trigger check_out
after DELETE on CART
for each row
BEGIN
    if (old.in_cart = true)
    then
      call sell(OLD.book_id, OLD.user_id, old.quantity);
    end if;

  END;

create definer = root@localhost trigger check_out_validation
before DELETE on CART
for each row
BEGIN
    if (book_quantity(OLD.book_id) < OLD.quantity)
    then
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'there is not enough quantity in stock';
    end if;

  END;

create table if not exists CREDIT_CARD
(
	user_id int not null,
	credit_number varchar(20) not null,
	primary key (user_id, credit_number),
	constraint CREDIT_CARD_USER_user_id_fk
		foreign key (user_id) references USER (user_id)
			on update cascade on delete cascade
);

create definer = root@localhost trigger credit_number_verification_insertion
before INSERT on CREDIT_CARD
for each row
BEGIN
    if (NOT (NEW.credit_number REGEXP
             "[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]$"))
    then
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'credit card format is not right';
    end if;
  END;

create definer = root@localhost trigger credit_number_verification_update
before INSERT on CREDIT_CARD
for each row
BEGIN
    if (NOT (NEW.credit_number REGEXP
             "[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]$"))
    then
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'credit card format is not right';
    end if;
  END;

create table if not exists SALES
(
	sale_id int auto_increment
		primary key,
	book_id int not null,
	user_id int not null,
	date_of_sale date not null,
	quantity int not null,
	constraint SALES_BOOK_Book_id_fk
		foreign key (book_id) references BOOK (book_id)
			on update cascade on delete cascade,
	constraint SALES_USER_user_id_fk
		foreign key (user_id) references USER (user_id)
			on update cascade on delete cascade
);

create definer = root@localhost procedure add_to_cart(IN book_id int, IN user_id int, IN quantity int)
BEGIN
    IF (EXISTS(SELECT *
               FROM CART
               WHERE CART.book_id = book_id
                 AND CART.user_id = user_id))
    then
      UPDATE CART
      set CART.quantity = CART.quantity + quantity
      where CART.book_id = book_id
        AND CART.user_id = user_id;
    ELSE
      INSERT into CART values (book_id, user_id, quantity, 1);
    end if;
  END;

create definer = root@localhost function book_quantity(id int) returns int
BEGIN
    declare quantity int;
    select BOOK.quantity into quantity from BOOK where BOOK.book_id = id;

    return quantity;
  END;

create definer = root@localhost function get_orders_quantity(book_id int) returns int
BEGIN
    declare sum int;
    select COALESCE(SUM(quantity), 0) into sum from `ORDER` where `ORDER`.book_id = book_id;
    return sum;
  END;

create definer = root@localhost procedure purchase(IN user_id int)
BEGIN
    DECLARE rollback bool default 0;
    declare continue handler for SQLEXCEPTION set rollback = 1;
    start transaction;
    delete from CART where CART.user_id = user_id;
    if rollback
    then
      ROLLBACK;
    else
      commit;
    end if;
  END;

create definer = root@localhost procedure remove_from_cart(IN user_id int, IN book_id int)
BEGIN
    DECLARE rollback bool default 0;
    declare continue handler for SQLEXCEPTION set rollback = 1;
    start transaction;
    update CART
    set CART.in_cart = false
    where CART.user_id = user_id
      AND CART.book_id = book_id;

    delete
    from CART
    where CART.user_id = user_id
      AND CART.book_id = book_id;

    if rollback
    then
      ROLLBACK;
    else
      commit;
    end if;
  END;

create definer = root@localhost procedure sell(IN book_id int, IN user_id int, IN quantity int)
BEGIN
    update BOOK set BOOK.quantity = BOOK.quantity - quantity where BOOK.book_id = book_id;
    insert into SALES values (sale_id, book_id, user_id, current_date, quantity);
  END;

