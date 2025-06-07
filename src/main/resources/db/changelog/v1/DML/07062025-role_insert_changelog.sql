-- liquibase formatted sql

-- changeset konon:1749286017010-20
INSERT INTO roles (name)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');