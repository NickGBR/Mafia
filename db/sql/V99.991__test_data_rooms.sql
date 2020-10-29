insert into rooms (password_hash,  room_name, description,
                   max_users_amount, game_status, day_number, game_phase,
                   mafia, sheriff, don)
values ('98765', 'room1', 'description1', 10, 'COMPLETED', 1, 'MAFIA_DISCUSS_PHASE', 0, false, false),
       ('98764', 'room2', 'description2', 10, 'COMPLETED', 2, 'MAFIA_VOTE_PHASE', 0, false, false),
       ('98763', 'room3', 'description3', 10, 'COMPLETED', 3, 'CIVILIANS_DISCUSS_PHASE', 0, false, false),
       ('98762', 'room4', 'description4', 10, 'COMPLETED', 1, 'CIVILIANS_VOTE_PHASE', 0, false, false),
     --  ('98761', 'room5', 'description5', 10, 'IN_PROGRESS', 4, 'NIGHT', 0, false, false),
       ('98760', 'room6', 'description6', 10, 'COMPLETED', 5, 'DON_PHASE', 0, false, false),
       ('98759', 'room7', 'description7', 10, 'COMPLETED', 1, 'SHERIFF_PHASE', 0, false, false),
       ('98758', 'room8', 'description8', 10, 'COMPLETED', 5, 'DON_PHASE', 0, false, false),
       ('98757', 'room9', 'description9', 10, 'COMPLETED', 4, 'CIVILIANS_DISCUSS_PHASE', 0, false, false),
       ('98756', 'room10', 'description10', 10, 'COMPLETED', 1, 'DON_PHASE', 0, false, false);

