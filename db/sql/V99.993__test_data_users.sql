--drop table users;
insert into users (password_hash, login, statistics_id)
values ( 12345, 'user1', 1),
       ( 12346, 'user2', 2);

/*insert into users (user_id, password_hash, login, statistics_id)
values (1, 12345, 'user1', 1),
       (2, 12346, 'user2', 2),
       (3, 12347, 'user3', 3),
       (4, 12348, 'user4', 4),
       (5, 12349, 'user5', 5),
       (6, 12360, 'user6', 6),
       (7, 12361, 'user7', 7),
       (8, 12362, 'user8', 8),
       (9, 12363, 'user9', 9);*/