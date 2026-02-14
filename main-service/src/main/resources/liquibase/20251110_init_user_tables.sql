CREATE TABLE IF NOT EXISTS users (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name          TEXT NULL,
    second_name         TEXT NULL,
    birth_date          DATE NULL,
    gender              INTEGER,
    biography           TEXT NULL,
    city                TEXT NULL,
    password            TEXT NOT NULL
);

COMMENT ON TABLE users IS 'Пользователи';