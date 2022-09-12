INSERT INTO currency (name, code)
VALUES ('Euro', 'EUR'),
       ('Dollar', 'USD') ON DUPLICATE KEY
UPDATE name = name;
