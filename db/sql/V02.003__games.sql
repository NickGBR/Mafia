--drop table games;

create table games
(
    game_id    serial  not null primary key,
    phase      text    not null,
    status     text    not null,
    day_number integer not null
);

