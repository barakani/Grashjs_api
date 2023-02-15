INSERT INTO currency (name, code)
VALUES ('Euro', '€'),
       ('Dollar', '$'),
       ('Dirham', 'DH')
ON DUPLICATE KEY
    UPDATE name = name;
