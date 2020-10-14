drop table users;
insert into users (user_id, password_hash, login, room_id, statistics_id)
values (1, 12345, 'user1', 1, 1),
       (2, 12346, 'user2', 1, 2),
       (3, 12347, 'user3', 1, 3),
       (4, 12348, 'user4', 2, 4),
       (5, 12349, 'user5', 2, 5),
       (6, 12360, 'user6', 2, 6),
       (7, 12361, 'user7', 3, 7),
       (8, 12362, 'user8', 3, 8),
       (9, 12363, 'user9', 4, 9);
