drop table Characters;

create table Characters
(
    game_id      serial      not null,
    user_id      serial      not null,
    character_id serial      not null
        constraint characters_pk
            primary key,
    role         varchar(30) not null,
    status       boolean     not null
);

create unique index characters_character_id_uindex
    on Characters (character_id);

/*create unique index characters_game_id_uindex
    on Characters (game_id);*/

create unique index characters_user_id_uindex
    on Characters (user_id);

