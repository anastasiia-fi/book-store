INSERT INTO books(id, title, author, isbn, price, description) VALUES (1, 'BookA', 'AuthorA', '0123456789', 100.01, 'DescriptionA');
INSERT INTO books(id, title, author, isbn, price, description) VALUES (2, 'BookB', 'AuthorB', '9123456780', 200.01, 'DescriptionB');
INSERT INTO books(id, title, author, isbn, price, description) VALUES (3, 'BookC', 'AuthorC', '0923456781', 300.01, 'DescriptionC');

INSERT INTO books_categories(book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories(book_id, category_id) VALUES (2, 2);
INSERT INTO books_categories(book_id, category_id) VALUES (3, 1);
