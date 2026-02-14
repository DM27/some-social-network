CREATE TABLE IF NOT EXISTS friends (
    user_id             UUID NOT NULL,
    friend_id           UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_users_user_id ON friends (user_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_user_id_friend_id ON friends (user_id, friend_id);

COMMENT ON TABLE friends IS 'Таблица друзей';
