insert into rooms (password_hash, admin_id, name, description,
                   max_users_amount, game_status, day_number, game_phase,
                   mafia, sheriff, don)
values ('98765', 1, 'room1', 'description1', 10, 'DELETED', 1, 'NIGHT', 0, false, false),
       ('98764', 2, 'room2', 'description2', 10, 'NOT_STARTED', 2, 'NIGHT', 0, false, false),
       ('98763', 3, 'room3', 'description3', 10, 'IN_PROGRESS', 3, 'DAY', 0, false, false),
       ('98762', 4, 'room4', 'description4', 10, 'COMPLETED', 1, 'DAY', 0, false, false),
       ('98761', 5, 'room5', 'description5', 10, 'IN_PROGRESS', 4, 'NIGHT', 0, false, false),
       ('98760', 6, 'room6', 'description6', 10, 'NOT_STARTED', 5, 'DAY', 0, false, false),
       ('98759', 7, 'room7', 'description7', 10, 'IN_PROGRESS', 1, 'NIGHT', 0, false, false),
       ('98758', 8, 'room8', 'description8', 10, 'DELETED', 5, 'DAY', 0, false, false),
       ('98757', 9, 'room9', 'description9', 10, 'IN_PROGRESS', 4, 'DAY', 0, false, false),
       ('98756', 10, 'room10', 'description10', 10, 'COMPLETED', 1, 'NIGHT', 0, false, false);

