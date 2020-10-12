drop table Users;

create table Users
(
    user_id       serial      not null
        constraint "Users_pkey" primary key,
    password_hash bigint     not null,
    login         varchar(20) not null
        constraint "Users_login_key" unique
);
