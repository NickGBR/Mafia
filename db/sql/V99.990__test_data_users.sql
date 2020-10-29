insert into users (login, password_hash, is_ready,
                   character, character_status, votes_against, is_admin, has_voted)
values ('user1', '12345', true, 'CITIZEN', 'DEAD', 3, true, false),
       ('user2', '12346', true, 'DON', 'ALIVE', 2, false, false),
       ('user3', '12347', false, 'CITIZEN', 'ALIVE', 5, false, false),
       ('user4', '12348', false, 'CITIZEN', 'DEAD', 3, true, false),
       ('user5', '12349', true, 'MAFIA', 'DEAD', 4, false, false),
       ('user6', '12350', false, 'MAFIA', 'ALIVE', 0, false, false),
       ('user7', '12351', true, 'CITIZEN', 'ALIVE', 1, true, false),
       ('user8', '12352', false, 'MAFIA', 'DEAD', 9, false, false),
       ('user9', '12353', true, 'CITIZEN', 'ALIVE', 2, false, false),
       ('user10', '12354', false, 'CITIZEN', 'DEAD', 3, true, false);


