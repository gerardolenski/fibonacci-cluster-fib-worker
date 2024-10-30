package org.gol.fibworker.domain.fib;

import org.gol.fibworker.BaseIT;
import org.gol.fibworker.domain.model.AlgorithmClaim;
import org.gol.fibworker.domain.model.SequenceBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestPropertySource(properties = "fib.calculation.is-cache-active=true")
class CachingFibCalculatorTest extends BaseIT {

    private static final AlgorithmClaim ITERATIVE_ALG = new AlgorithmClaim("ITERATIVE");
    private static final AlgorithmClaim BINETS_ALG = new AlgorithmClaim("BINETS");
    private static final SequenceBase BASE_OF_10 = new SequenceBase(10);

    @Autowired
    private FibCalculator sut;
    @SpyBean
    @Qualifier("eagerFibCalculator")
    private FibCalculator eagerFibCalculator;
    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        ofNullable(cacheManager.getCache(CachingFibCalculator.FIB_CALC_CACHE))
                .ifPresent(Cache::clear);
    }

    @Test
    @DisplayName("should hit cache for same number and same algorithm")
    void sameNumberSameAlgorithm() {
        //when calculating same number twice with same algorithm
        var res1 = sut.calculateFibonacciNumber(ITERATIVE_ALG, BASE_OF_10);
        var res2 = sut.calculateFibonacciNumber(ITERATIVE_ALG, BASE_OF_10);

        //then cache should be hit
        assertThat(res1).isEqualTo(res2);
        verify(eagerFibCalculator).calculateFibonacciNumber(any(), any());
    }

    @Test
    @DisplayName("should not hit cache for same number but different algorithm")
    void sameNumberDifferentAlgorithm() {
        //when calculating same number twice with different algorithm
        var res1 = sut.calculateFibonacciNumber(ITERATIVE_ALG, BASE_OF_10);
        var res2 = sut.calculateFibonacciNumber(BINETS_ALG, BASE_OF_10);

        //then cache should not be hit
        assertThat(res1.number()).isEqualTo(res2.number());
        verify(eagerFibCalculator, times(2)).calculateFibonacciNumber(any(), any());
    }

    @Test
    @DisplayName("should not hit cache for different number")
    void differentNumberDifferentAlgorithm() {
        //when
        sut.calculateFibonacciNumber(ITERATIVE_ALG, BASE_OF_10);
        sut.calculateFibonacciNumber(ITERATIVE_ALG, new SequenceBase(11));

        //then cache should not be hit
        verify(eagerFibCalculator, times(2)).calculateFibonacciNumber(any(), any());
    }
}