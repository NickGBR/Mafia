create table characters2games
(
    character_id int not null,
    game_id      int not null,

    constraint fk_characters2games1 foreign key (character_id) references
    characters (character_id),
    constraint fk_characters2games2 foreign key (game_id) references
    games (game_id),
    primary key (character_id, game_id)

);
