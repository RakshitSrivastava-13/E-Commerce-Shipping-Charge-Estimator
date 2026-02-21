package com.jumbotail.shipping.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for caching using Caffeine (in-memory cache).
 * Provides caching for warehouse and shipping charge calculations to improve performance.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configures the cache manager with Caffeine as the caching provider.
     * Sets maximum cache size and TTL (time-to-live) for cache entries.
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "nearestWarehouse",
                "shippingCharge"
        );

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats());

        return cacheManager;
    }
}
