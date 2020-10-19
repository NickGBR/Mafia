-- drop table users2characters;
create table users2characters
(
    user_id       int not null primary key,
    character_id       int not null,

    constraint fk_users2characters1 foreign key (user_id) references users (user_id),
    constraint fk_users2characters2 foreign key (character_id) references characters (character_id)
);

