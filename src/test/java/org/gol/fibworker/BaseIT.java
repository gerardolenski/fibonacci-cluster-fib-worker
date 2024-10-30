package org.gol.fibworker;

import com.redis.testcontainers.RedisContainer;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.activemq.ArtemisContainer;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIT {

    static final ArtemisContainer artemis = new ArtemisContainer("apache/activemq-artemis:2.37.0-alpine");
    static final RedisContainer redis = new RedisContainer("redis:6.2.6");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        artemis.start();
        redis.start();
        registry.add("spring.artemis.broker-url", artemis::getBrokerUrl);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getRedisPort);
    }
}
