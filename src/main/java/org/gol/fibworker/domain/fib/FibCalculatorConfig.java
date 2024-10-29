package org.gol.fibworker.domain.fib;

import org.gol.fibworker.domain.fib.strategy.FibonacciStrategyFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FibCalculatorConfig {

    @Bean
    @ConditionalOnProperty(prefix = "fib", name = "calculator-type", havingValue = "eager")
    FibCalculator eagerFibCalculator(FibonacciStrategyFactory fibStrategyFactory) {
        return new EagerFibCalculator(fibStrategyFactory);
    }

    @Bean
    @ConditionalOnProperty(prefix = "fib", name = "calculator-type", havingValue = "caching")
    FibCalculator cachingFibCalculator(FibonacciStrategyFactory fibStrategyFactory) {
        return new CachingFibCalculator(new EagerFibCalculator(fibStrategyFactory));
    }
}
