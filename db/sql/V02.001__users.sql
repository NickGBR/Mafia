create table users
(
    user_id          serial      not null primary key,
    login            text unique not null,
    password_hash    text        not null,
    room_id          int,
    is_ready         boolean,
    character        text,
    character_status text,
    votes_against    int
);
