drop table Votings;

create table Votings
(
    vote_id      serial  not null
        constraint voting_pk primary key,
    game_id      serial  not null,
    character_id serial  not null,
    day_number   integer not null,
    votes_amount integer not null
);

create unique index voting_vote_id_uindex
    on Votings (vote_id);

