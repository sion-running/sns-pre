package com.fastcampus.sns.repository;

import com.fastcampus.sns.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

/**
 * Redis에 유저를 캐싱하고, 캐싱한 유저를 가져오는 역할을 하는 클래스
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {
    private final RedisTemplate<String, User> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user) {
        String key = getKey(user.getUsername());
        log.info("set user to redis {}, {}", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<User> getUser(String userName) {
        String key = getKey(userName);
        User user = userRedisTemplate.opsForValue().get(getKey(userName));
        log.info("get user from redis {}, {}", key, user);
        return Optional.ofNullable(user);
    }

    private String getKey(String userName) {
        return "USER:" + userName;
    }
}
