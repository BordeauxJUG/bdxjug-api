package org.bdxjug.api.domain.meetings;

import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface LocationRepository {

    @Cacheable(cacheNames = "locationById")
    Optional<Location> by(LocationID locationID);

    List<Location> all();
    
    @CacheEvict("locationById")
    default void clearCache(){
    }
    
}
