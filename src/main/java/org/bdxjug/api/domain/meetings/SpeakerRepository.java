package org.bdxjug.api.domain.meetings;

import java.util.Optional;
import java.util.SortedSet;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface SpeakerRepository {

    @Cacheable("speakerById")
    Optional<Speaker> by(SpeakerID speakerID);

    @Cacheable("speakers")
    SortedSet<Speaker> all();

    @CacheEvict({"speakers", "speakerById"})
    default void clearCache() {
    }

}
