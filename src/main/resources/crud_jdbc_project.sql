create database crud_jdbc_project;

create table user
(
    username varchar(100) not null,
    password varchar(100) not null,
    id       int auto_increment
        primary key
);

