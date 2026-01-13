INSERT INTO users (first_name, second_name, birth_date, gender, biography, city, "password", id)
VALUES('Елизавета', 'Харитонова', '1995-01-01', 1, 'Born in 1995-01-01', 'Лыткарино', '$2a$10$zNJ33YZlD2GTpRNMxBsLEeQ1GEVwmbFLEZ3ZQTauIWGKA72PdRmRu', 'bf788d81-93cf-47f7-ad4a-4a2caddec99c'::uuid) ON CONFLICT DO NOTHING;
INSERT INTO users (first_name, second_name, birth_date, gender, biography, city, "password", id)
VALUES('Никита', 'Хохлов', '2002-01-01', 0, 'Born in 2002-01-01', 'Курск', '$2a$10$iUltWjjZQ5QIhnP8BzlCM.3F3t4YBJVHEo1zRxSYkOh6BbuBU.zh2', '5dfbdef9-6cfa-4328-b788-86448d20a13c'::uuid) ON CONFLICT DO NOTHING;

INSERT INTO friends (user_id, friend_id)
VALUES('bf788d81-93cf-47f7-ad4a-4a2caddec99c'::uuid, '5dfbdef9-6cfa-4328-b788-86448d20a13c'::uuid) ON CONFLICT DO NOTHING;

INSERT INTO posts (id, "text", author_user_id, created_at, updated_at)
VALUES('8f7a4415-0392-4983-b51e-803486cd2d0e'::uuid, '{"text": "Lorem 1 ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lectus mauris ultrices eros in cursus turpis massa."}', 'bf788d81-93cf-47f7-ad4a-4a2caddec99c'::uuid, '2025-12-10 02:16:51.937', '2025-12-10 02:16:51.937') ON CONFLICT DO NOTHING;
INSERT INTO posts (id, "text", author_user_id, created_at, updated_at)
VALUES('1c7b1f5a-545d-40fa-a291-1a10339ce54e'::uuid, '{"text": "Lorem 2 ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lectus mauris ultrices eros in cursus turpis massa."}', 'bf788d81-93cf-47f7-ad4a-4a2caddec99c'::uuid, '2025-12-10 02:18:20.923', '2025-12-10 02:18:20.923') ON CONFLICT DO NOTHING;

INSERT INTO dialogs (id, user_from_id, user_to_id, message, created_at, shard_key)
VALUES('497b2d37-1fc1-47f8-96eb-339968c9ce85'::uuid, 'bf788d81-93cf-47f7-ad4a-4a2caddec99c'::uuid, '5dfbdef9-6cfa-4328-b788-86448d20a13c'::uuid, 'Привет, как дела?', '2025-12-29 03:47:49.225', -972899793) ON CONFLICT DO NOTHING;