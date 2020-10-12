create table "Table"."Rooms"
(
    room_id       serial  not null
        constraint rooms_pk
            primary key,
    name          varchar not null,
    password_hash integer,
    game_id       integer
);

alter table "Table"."Rooms"
    owner to postgres;

create unique index rooms_game_id_uindex
    on "Table"."Rooms" (game_id);

create unique index rooms_name_uindex
    on "Table"."Rooms" (name);

create unique index rooms_room_id_uindex
    on "Table"."Rooms" (room_id);

