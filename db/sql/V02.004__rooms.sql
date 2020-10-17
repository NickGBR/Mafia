--drop table rooms;

create table rooms
(
    room_id          serial not null primary key,
    name             text   not null,
    description      text,
    password_hash    text   not null,
    game_id          int    not null,
    max_users_amount int,

    constraint fk_room2game foreign key (game_id) references games (game_id)
);


