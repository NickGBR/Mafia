drop table Games;

create table Games
(
    game_id    serial  not null,
    phase      serial  not null,
    status     boolean not null,
    day_number integer not null
);

/*create unique index games_day_number_uindex
    on Games (day_number);

create unique index games_game_id_uindex
    on Games (game_id);*/

