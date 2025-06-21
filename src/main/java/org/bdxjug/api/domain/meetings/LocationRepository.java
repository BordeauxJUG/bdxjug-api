package org.bdxjug.api.domain.meetings;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface LocationRepository {
    
    @Cacheable(cacheNames = "locations")
    List<Location> all();

    @CacheEvict("loacations")
    default void clearCache() {
    }

}
