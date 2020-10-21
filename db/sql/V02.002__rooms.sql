create table rooms
(
    room_id          serial not null primary key,
    password_hash    text   not null,
    admin_id         int    not null unique,
    room_name        text   not null,
    description      text,
    max_users_amount int    not null,
    game_status      text   not null,
    day_number       int,
    game_phase       text,
    mafia            int,
    sheriff          boolean,
    don              boolean,

    constraint fk_admin2user foreign key (admin_id) references users (user_id)
);


