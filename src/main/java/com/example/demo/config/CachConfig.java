package com.example.demo.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Java configuration for spring boot in memory cache 
 */
@Configuration
@EnableCaching
public class CachConfig {
	/**
	 * 
	 * @return  instance of CacheM<anager as spring bean 
	 * @cache name : numberMap 
	 */
	 @Bean
	    public CacheManager cacheManager() {
	        return new ConcurrentMapCacheManager("numberMap");
	    }
}
