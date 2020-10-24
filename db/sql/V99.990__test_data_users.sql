insert into users (login, password_hash, is_ready,
                   character, character_status, votes_against, is_admin)
values ('user1', '12345', true, 'CITIZEN', 'DEAD', 3, true),
       ('user2', '12346', true, 'DON', 'ALIVE', 2, false),
       ('user3', '12347', false, 'CITIZEN', 'ALIVE', 5, false),
       ('user4', '12348', false, 'CITIZEN', 'DEAD', 3, true),
       ('user5', '12349', true, 'MAFIA', 'DEAD', 4, false),
       ('user6', '12350', false, 'MAFIA', 'ALIVE', 0, false),
       ('user7', '12351', true, 'CITIZEN', 'ALIVE', 1, true),
       ('user8', '12352', false, 'MAFIA', 'DEAD', 9, false),
       ('user9', '12353', true, 'CITIZEN', 'ALIVE', 2, false),
       ('user10', '12354', false, 'CITIZEN', 'DEAD', 3, true);


