insert into users (login, password_hash, room_id, is_ready,
                   character, character_status, votes_against)
values ('user1', '12345', 1, true, 'CITIZEN', 'DEAD', 3),
       ('user2', '12346', 1, true, 'DON', 'ALIVE', 2),
       ('user3', '12347', 1, false, 'CITIZEN', 'ALIVE', 5),
       ('user4', '12348', 2, false, 'CITIZEN', 'DEAD', 3),
       ('user5', '12349', 2, true, 'MAFIA', 'DEAD', 4),
       ('user6', '12350', 2, false, 'MAFIA', 'ALIVE', 0),
       ('user7', '12351', 3, true, 'CITIZEN', 'ALIVE', 1),
       ('user8', '12352', 3, false, 'MAFIA', 'DEAD', 9),
       ('user9', '12353', 3, true, 'CITIZEN', 'ALIVE', 2),
       ('user10', '12354', 3, false, 'CITIZEN', 'DEAD', 3);


