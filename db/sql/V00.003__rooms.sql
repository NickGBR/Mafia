drop table Rooms;

create table Rooms
(
    room_id       serial      not null
        constraint rooms_pk primary key,
    name          varchar(50) not null,
    password_hash integer,
    game_id       integer
);

create unique index rooms_game_id_uindex
    on Rooms (game_id);

create unique index rooms_name_uindex
    on Rooms (name);

create unique index rooms_room_id_uindex
    on Rooms (room_id);

