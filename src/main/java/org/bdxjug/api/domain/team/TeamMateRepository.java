package org.bdxjug.api.domain.team;

import java.util.SortedSet;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface TeamMateRepository {

    @Cacheable(CACHE_NAME)
    SortedSet<TeamMate> byYear(Integer year);
    
    @CacheEvict(CACHE_NAME)
    default void clearCache(){
    }
    
    static final String CACHE_NAME = "teamMates";
}
