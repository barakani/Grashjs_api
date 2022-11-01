INSERT INTO currency (id, name, code)
VALUES (1, 'Euro', 'EUR'),
       (2, 'Dollar', 'USD')
ON CONFLICT
    (id)
DO UPDATE SET name = excluded.name;
