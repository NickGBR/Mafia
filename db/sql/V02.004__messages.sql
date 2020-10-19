create table messages
(
    message_id  serial not null primary key,
    room_id     int    not null,
    user_id     int    not null,
    text        text   not null
);


