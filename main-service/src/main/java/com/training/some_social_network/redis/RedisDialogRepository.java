package com.training.some_social_network.redis;

import com.training.some_social_network.dao.DialogDo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RedisDialogRepository {

    private static final String DIALOG_KEY_PREFIX = "dialogs:";
    private static final Duration DIALOG_CACHE_TTL = Duration.of(24, ChronoUnit.HOURS);
    public static final String ADD_DIALOG_LUA = """
            local key = KEYS[1]
            local value = ARGV[1]
            local ttl = ARGV[2]
            redis.call('LPUSH', key, value)
            redis.call('EXPIRE', key, ttl)
            return 1
    """;
    public static final String FIND_DIALOGS_LUA = """
            local key = KEYS[1]
            return redis.call('LRANGE', key, 0, -1)
    """;
    private final RedisScript<String> ADD_DIALOG_SCRIPT = new DefaultRedisScript<>(ADD_DIALOG_LUA, String.class);
    private final RedisScript<List> FIND_DIALOGS_SCRIPT = new DefaultRedisScript<>(FIND_DIALOGS_LUA, List.class);

    @Qualifier("redisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;


    public void add(DialogDo dialogDo) {
        String key = DIALOG_KEY_PREFIX + dialogDo.getShardKey();
        redisTemplate.execute(ADD_DIALOG_SCRIPT, List.of(key), dialogDo, DIALOG_CACHE_TTL.getSeconds());
    }

    @SuppressWarnings("unchecked")
    public List<DialogDo> findDialogs(String shardKey) {
        String key = DIALOG_KEY_PREFIX + shardKey;
        List result = redisTemplate.execute(FIND_DIALOGS_SCRIPT, List.of(key));
        return result == null ? List.of() : (List<DialogDo>) result;
    }
}
