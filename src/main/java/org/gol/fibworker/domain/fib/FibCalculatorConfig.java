package org.gol.fibworker.domain.fib;

import org.gol.fibworker.domain.fib.strategy.FibonacciStrategyFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
class FibCalculatorConfig {

    @Bean
    FibCalculator eagerFibCalculator(FibonacciStrategyFactory fibStrategyFactory) {
        return new EagerFibCalculator(fibStrategyFactory);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "fib.calculation", name = "is-cache-active", havingValue = "true")
    FibCalculator cachingFibCalculator(FibonacciStrategyFactory fibStrategyFactory) {
        log.info("Initializing CACHING fib calculator");
        return new CachingFibCalculator(eagerFibCalculator(fibStrategyFactory));
    }
}
