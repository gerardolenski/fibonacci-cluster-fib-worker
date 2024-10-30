package org.gol.fibworker.infrastructure.redis;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import lombok.extern.slf4j.Slf4j;

@Configuration
class RedisConfig implements CachingConfigurer {

    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }

    /**
     * Spring has issue and the CacheErrorHandler is not invoked for @Cacheable(sync=true). This serializer can be a
     * workaround - instead of registering LoggingCacheErrorHandler, e.g:
     *
     * <pre>
     *     @Bean
     *     public RedisCacheConfiguration cacheConfiguration() {
     *         return RedisCacheConfiguration.defaultCacheConfig()
     *                 .prefixCacheNameWith("fib-worker-")
     *                 .entryTtl(Duration.ofMinutes(10))
     *                 .disableCachingNullValues()
     *                 .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RelaxedJdkRedisSerializer()));
     *     }
     * </pre>
     */
    @Slf4j
    static class RelaxedJdkRedisSerializer extends JdkSerializationRedisSerializer {

        @Override
        public Object deserialize(byte[] bytes) {
            try {
                return super.deserialize(bytes);
            } catch (Exception e) {
                log.warn("Cannot deserialize bytes", e);
                return null;
            }
        }
    }
}
