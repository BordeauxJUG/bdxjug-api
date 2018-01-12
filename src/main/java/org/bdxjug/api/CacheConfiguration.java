package org.bdxjug.api;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("WithCache")
@EnableCaching
public class CacheConfiguration {
    
}
