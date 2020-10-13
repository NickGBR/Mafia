drop table users cascade;

create table users
(
    user_id       serial             not null primary key,
    password_hash bigint             not null,
    login         varchar(20) unique not null,
    room_id       serial,
    statistics_id serial not null,

    constraint fk_users2rooms foreign key (room_id) references rooms (room_id),
    constraint fk_user2statistics foreign key (statistics_id) references
        statistics (statistics_id)
);
