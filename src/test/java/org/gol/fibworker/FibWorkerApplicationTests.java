package org.gol.fibworker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.activemq.ArtemisContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class FibWorkerApplicationTests {

    static ArtemisContainer artemis = new ArtemisContainer("apache/activemq-artemis:2.37.0-alpine");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        artemis.start();
        registry.add("spring.artemis.broker-url", artemis::getBrokerUrl);
    }

    @Autowired
    private Environment env;

    @Test
    void contextLoads() {
        assertThat(env).isNotNull();
    }
}
