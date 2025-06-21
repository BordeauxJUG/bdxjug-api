package org.bdxjug.api.domain.meetings;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.SortedSet;

public interface SpeakerRepository {

    @Cacheable("speakers")
    SortedSet<Speaker> all();

    @CacheEvict({"speakers"})
    default void clearCache() {
    }

}
