CREATE TABLE IF NOT EXISTS dialogs (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_from_id            UUID NOT NULL,
    user_to_id              UUID NOT NULL,
    message                 TEXT NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT now(),
    shard_key               TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_dialogs_shard_key ON dialogs (shard_key);

COMMENT ON TABLE dialogs IS 'Таблица диалогов пользователей';
