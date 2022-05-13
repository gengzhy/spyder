package xin.cosmos.common.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

/**
 * Redis工具类
 */
@Component
public class RedisService {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long timeoutSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutSeconds));
    }

    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public Boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public Boolean setIfAbsent(String key, Object value, long timeoutSeconds) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(timeoutSeconds));
    }

    public Long deleteLike(String key) {
        Set<Object> keys = Optional.ofNullable(redisTemplate.keys(key)).orElseThrow(NullPointerException::new);
        return redisTemplate.delete(keys);
    }

    public void addList(String key, List value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public void setList(String key, List value) {
        delete(key);
        addList(key, value);
    }

    public <T> List<T> getList(String key) {
        return (List<T>) Optional.ofNullable(redisTemplate.opsForList().range(key, 0, -1)).orElse(Collections.emptyList());
    }

    public void setMap(String key, Map value) {
        delete(key);
        redisTemplate.opsForHash().putAll(key, value);
    }

    public <K, V> Map<K, V> getMap(String key) {
        return (Map<K, V>) redisTemplate.opsForHash().entries(key);
    }

    public <V> List<V> getMapValues(String key) {
        return (List<V>) redisTemplate.opsForHash().values(key);
    }

    public Boolean expired(String key, long timeoutSeconds) {
        return redisTemplate.expire(key, Duration.ofSeconds(timeoutSeconds));
    }

    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Long incr(String key, long timeoutSeconds) {
        Long value = incr(key);
        expired(key, timeoutSeconds);
        return value;
    }
}
