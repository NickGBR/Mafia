create table "Table"."Users"
(
    user_id       serial      not null
        constraint "Users_pkey"
            primary key,
    password_hash varchar     not null,
    login         varchar(20) not null
        constraint "Users_login_key"
            unique
);

alter table "Table"."Users"
    owner to postgres;