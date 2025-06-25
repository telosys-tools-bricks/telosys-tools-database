DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS foo;

CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE orders (
    id INT PRIMARY KEY,
    user_id INT,
    total DECIMAL(10,2),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE foo (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    v1 INT,
    v2 VARCHAR(3),
    CONSTRAINT uq_foo UNIQUE (v1, v2)
);

