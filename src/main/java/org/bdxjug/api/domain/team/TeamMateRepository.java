package org.bdxjug.api.domain.team;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.SortedSet;

public interface TeamMateRepository {

    static final String CACHE_NAME = "teamMates";

    @Cacheable(value = CACHE_NAME)
    SortedSet<TeamMate> byYear(Integer year);

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    default void clearCache() {
    }
}
