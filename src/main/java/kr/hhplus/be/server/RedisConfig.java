package kr.hhplus.be.server;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
public class RedisConfig {
    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    @Bean
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379");
        RedissonClient redisson = Redisson.create(config);

        if (Arrays.asList(environment.getActiveProfiles()).contains("local")
                || Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            redisson.getKeys().flushall();
        }

        return redisson;
    }

    private final Environment environment;

    public RedisConfig(Environment environment) {
        this.environment = environment;
    }
}
