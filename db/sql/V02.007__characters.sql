-- drop table characters;

create table characters
(
    character_id serial not null primary key,
    game_id      int    not null,
    user_id      serial not null,
    role         text,
    status       text   not null,
    vote_id      int,

    constraint fk_characters2users foreign key (user_id) references users (user_id)
   -- constraint fk_characters2games foreign key (game_id) references games (game_id)
);




