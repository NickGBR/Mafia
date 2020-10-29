create table messages2rooms
(
    room_id    int not null,
    message_id int not null primary key,

    constraint fk_messages2rooms_messages foreign key (message_id)
        references messages (message_id),
    constraint fk_messages2rooms_rooms foreign key (room_id) references rooms (room_id)
);