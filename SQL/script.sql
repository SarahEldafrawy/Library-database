create table if not exists PUBLISHER
(
  publisher_id int         not null,
  name         varchar(50) not null,
  phone        varchar(20) null,
  address      varchar(50) null,
  constraint `PRIMARY`
  primary key (publisher_id)
);

create table if not exists BOOK
(
  book_id       int                                                         not null,
  title         varchar(50)                                                 not null,
  pub_year      date                                                        null,
  selling_price float                                                       not null,
  category      enum ('Science', 'Art', 'Religion', 'History', 'Geography') null,
  quantuty      int                                                         not null,
  publisher_id  int                                                         not null,
  constraint `PRIMARY`
  primary key (book_id),
  constraint BOOK_PUBLISHER_publisher_id_fk
  foreign key (publisher_id) references PUBLISHER (publisher_id)
    on update cascade
);

create table if not exists Author
(
  Name      varchar(50) not null,
  Author_id int         not null,
  book_id   int         not null,
  constraint `PRIMARY`
  primary key (book_id, Author_id),
  constraint Author_BOOK_Book_id_fk
  foreign key (book_id) references BOOK (book_id)
    on update cascade
    on delete cascade
);

create table if not exists `ORDER`
(
  order_id int auto_increment
  constraint `PRIMARY`
  primary key,
  book_id  int  not null,
  quantity int  not null,
  date     date not null,
  constraint ORDER_BOOK__fk
  foreign key (book_id) references BOOK (book_id)
    on update cascade
    on delete cascade
);

create table if not exists USER
(
  user_id          int auto_increment
  constraint `PRIMARY`
  primary key,
  first_name       varchar(20) not null,
  last_name        varchar(20) not null,
  email_address    varchar(50) not null,
  phone_number     varchar(20) null,
  shipping_address varchar(50) not null,
  password         varchar(50) not null,
  promoted         tinyint(1)  not null,
  constraint USER_email_address_uindex
  unique (email_address)
);

create table if not exists CART
(
  book_id  int not null,
  user_id  int not null,
  quantity int not null,
  constraint `PRIMARY`
  primary key (user_id, book_id),
  constraint CART_BOOK_Book_id_fk
  foreign key (book_id) references BOOK (book_id)
    on update cascade
    on delete cascade,
  constraint CART_USER_user_id_fk
  foreign key (user_id) references USER (user_id)
    on update cascade
    on delete cascade
);


