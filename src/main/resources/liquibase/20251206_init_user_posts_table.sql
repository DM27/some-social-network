CREATE TABLE IF NOT EXISTS posts (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    text                    TEXT NOT NULL,
    author_user_id          UUID NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT now(),
    updated_at              TIMESTAMP NOT NULL DEFAULT now(),
    FOREIGN KEY (author_user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_posts_author_user_id ON posts (author_user_id);

COMMENT ON TABLE friends IS 'Таблица постов пользователя';
