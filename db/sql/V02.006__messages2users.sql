create table messages2users
(
    user_id    int not null,
    message_id int not null primary key,

    constraint fk_messages2users_messages foreign key (message_id)
        references messages (message_id),
    constraint fk_messages2users_users foreign key (user_id) references users (user_id)
);