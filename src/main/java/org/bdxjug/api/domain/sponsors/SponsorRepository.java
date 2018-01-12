package org.bdxjug.api.domain.sponsors;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface SponsorRepository {

    @Cacheable(CACHE_NAME)
    List<Sponsor> all();

    @CacheEvict(CACHE_NAME)
    default void clearCache(){
    }
    
    static final String CACHE_NAME = "sponsors";
}
