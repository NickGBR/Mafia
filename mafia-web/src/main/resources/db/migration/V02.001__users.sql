create table users
(
    user_id          serial      not null primary key,
    login            text unique not null,
    password_hash    text        not null,
    is_ready         boolean     not null,
    is_admin         boolean     not null,
    has_voted        boolean     not null,
    character        text,
    character_status text,
    votes_against    int
);
