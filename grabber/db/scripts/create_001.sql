create table post (
    id serial primary key not null,
    name varchar(500) NOT NULL ,
    text varchar(1000) NOT NULL,
    link varchar(1000) UNIQUE NOT NULL,
    created_at date NOT NULL
);