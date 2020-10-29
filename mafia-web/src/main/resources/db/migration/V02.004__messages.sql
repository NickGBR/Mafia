create table messages
(
    message_id  serial not null primary key,
    destination text   not null,
    text        text   not null
);


