DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

CREATE TABLE books
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    author_id INT          NOT NULL,
    title     VARCHAR(250) NOT NULL,
    priceOld  VARCHAR(250) DEFAULT NULL,
    price     VARCHAR(250) DEFAULT NULL
);

CREATE TABLE authors
(
    id        INT AUTO_INCREMENT,
    firstname VARCHAR(250) NOT NULL,
    lastname  VARCHAR(250) NOT NULL,
    constraint BOOKS_AUTHORS_ID_FK
        foreign key (id) references books (author_id)
);







