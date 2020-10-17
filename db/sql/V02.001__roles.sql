--drop table roles;

create table roles
(
    role_id serial      not null primary key,
    role    varchar(20) not null
);


insert into roles (role_id, role)
values (1, 'DON'),
       (2, 'MAFIA'),
       (3, 'SHERIFF'),
       (4, 'CITIZEN');