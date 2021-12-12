drop table if exists book;
drop table if exists author;

create table book
(
    id        bigint not null auto_increment primary key,
    isbn      varchar(255),
    publisher varchar(255),
    title     varchar(255),
    author_id BIGINT
) engine = InnoDB;

create table author
(
    id        bigint not null auto_increment primary key,
    first_name   varchar(255),
    last_name    varchar(255),
    primary key (id)
) engine = InnoDB;

alter table book add constraint book_author_fk foreign key (author_id) references author (id);

insert into author (first_name, last_name) values ('Craig', 'Walls');

insert into book (isbn, publisher, title, author_id) values ('123123', 'Simon & Schuster', 'Spring in Action, 5th Edition',
        (select id from author where first_name = 'Craig' and last_name = 'Walls'));

insert into book (isbn, publisher, title, author_id) values ('123124', 'Simon & Schuster', 'Spring Boot in Action, 1st Edition',
        (select id from author where first_name = 'Craig' and last_name = 'Walls'));

insert into book (isbn, publisher, title, author_id) values ('123125', 'Simon & Schuster', 'Spring in Action, 5th Edition',
        (select id from author where first_name = 'Craig' and last_name = 'Walls'));

insert into author (first_name, last_name) values ('Eric', 'Evans');

insert into book (isbn, publisher, title, author_id) values ('123126', 'Addison Wesley', 'Domain-Driven Design',
        (select id from author where first_name = 'Eric' and last_name = 'Evans'));

insert into author (first_name, last_name) values ('Robert', 'Martin');

insert into book (isbn, publisher, title, author_id) values ('123127', 'Addison Wesley', 'Clean Code',
        (select id from author where first_name = 'Eric' and last_name = 'Evans'));
