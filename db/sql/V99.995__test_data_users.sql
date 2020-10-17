--drop table users;
insert into users (password_hash, login, statistics_id, room_id)
values (12345, 'user1', 1, 1),
       (12346, 'user2', 2, 2),
       (12347, 'user3', 3, 1),
       (12348, 'user4', 4, 2),
       (12349, 'user5', 5, 1),
       (12360, 'user6', 6, 1),
       (12361, 'user7', 7, 2),
       (12362, 'user8', 8, 1),
       (12363, 'user9', 9, 2);