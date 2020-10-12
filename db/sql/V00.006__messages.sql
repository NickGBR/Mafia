drop table Messages;

create table Messages
(
    message_id serial  not null,
    game_id    integer  not null,
    --constraint messages_pk primary key,
    user_id    serial  not null,
    text       text    not null,
    addressee  integer not null
);

/*create index messages_game_id_uindex
    on Messages (game_id);*/

create unique index messages_message_id_uindex
    on Messages (message_id);

