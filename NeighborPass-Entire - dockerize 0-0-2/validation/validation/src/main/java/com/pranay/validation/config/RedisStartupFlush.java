package com.pranay.validation.config;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisStartupFlush {
    private final StringRedisTemplate template;
    public RedisStartupFlush(StringRedisTemplate template) { this.template = template; }

    @PostConstruct
    public void flushAllOnStartup() {
        // DANGER: clears all DBs in the Redis instance
        template.getConnectionFactory().getConnection().flushAll();
    }
}
