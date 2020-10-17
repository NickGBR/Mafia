--drop table users;

create table users
(
    user_id       serial      not null primary key,
    password_hash text        not null,
    login         text unique not null,
    statistics_id int,
    room_id       int,

    constraint fk_user2statistics foreign key (statistics_id) references
        statistics (statistics_id),
    constraint fk_user2room foreign key (room_id) references rooms (room_id)

);
