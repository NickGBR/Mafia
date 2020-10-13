drop table roles cascade;

create table roles
(
    role_id serial      not null primary key,
    role    varchar(20) not null
);


insert into roles (role_id, role)
values (1, 'don'),
       (2, 'mafia'),
       (3, 'mafia'),
       (4, 'sheriff'),
       (5, 'citizen'),
       (6, 'citizen'),
       (7, 'citizen'),
       (8, 'citizen'),
       (9, 'citizen'),
       (10, 'citizen');