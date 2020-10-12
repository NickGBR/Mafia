create table "Table"."Characters"
(
    game_id      serial  not null,
    user_id      serial  not null,
    character_id serial  not null
        constraint characters_pk
            primary key,
    role         integer not null,
    status       text    not null
);

alter table "Table"."Characters"
    owner to postgres;

create unique index characters_character_id_uindex
    on "Table"."Characters" (character_id);

create unique index characters_game_id_uindex
    on "Table"."Characters" (game_id);

create unique index characters_user_id_uindex
    on "Table"."Characters" (user_id);

