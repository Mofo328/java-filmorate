MERGE INTO mpa (mpa_id,mpa_name) VALUES (1,'G');
MERGE INTO mpa (mpa_id,mpa_name) VALUES (2,'PG');
MERGE INTO mpa (mpa_id,mpa_name) VALUES (3,'PG-13');
MERGE INTO mpa (mpa_id,mpa_name) VALUES (4,'R');
MERGE INTO mpa (mpa_id,mpa_name) VALUES (5,'NC-17');

MERGE INTO genres(genre_id,genre_name) VALUES (1,'Комедия');
MERGE INTO genres(genre_id,genre_name) VALUES (2,'Драма');
MERGE INTO genres(genre_id,genre_name) VALUES (3,'Мультфильм');
MERGE INTO genres(genre_id,genre_name) VALUES (4,'Триллер');
MERGE INTO genres(genre_id,genre_name) VALUES (5,'Документальный');
MERGE INTO genres(genre_id,genre_name) VALUES (6,'Боевик');

INSERT INTO users (user_name, login, email, birthday) VALUES ('User1', 'Surname1', 'mail1@yandex.ru', '2020-01-01');
INSERT INTO users (user_name, login, email, birthday) VALUES ('User2', 'Surname2', 'mail2@yandex.ru', '2021-01-01');
INSERT INTO users (user_name, login, email, birthday) VALUES ('User3', 'Surname3', 'mail3@yandex.ru', '2022-02-01');
INSERT INTO users (user_name, login, email, birthday) VALUES ('User4', 'Surname4', 'mail4@yandex.ru', '2023-01-02');

INSERT INTO films(film_name, description, released, duration, mpa_id)
    VALUES ('film1', 'description1', '2019-01-11', '124', 1),
           ('film2', 'description2', '2020-02-12', '141', 2),
           ('film3', 'description3', '2021-03-13', '160', 3),
           ('film4', 'description4', '2022-04-14', '111', 4),
           ('film5', 'description5', '2023-05-15', '110', 5),
           ('film6', 'description6', '2024-06-16', '115', 5),
           ('film7', 'description7', '2018-07-17', '140', 5);

INSERT INTO film_genres(film_id, genre_id) VALUES (1, 1),(2, 2),(3, 3),(4, 4),(5, 5),(6, 5),(7, 2);

INSERT INTO friends(user_id, friend_id) VALUES (1, 2),(1, 3),(2, 1),(2, 4),(3, 1),(3, 4),(4, 1);

INSERT INTO likes(film_id,user_id) VALUES (1,1),(2,2),(3,3),(4,4),(5,1),(6,2),(7,3);