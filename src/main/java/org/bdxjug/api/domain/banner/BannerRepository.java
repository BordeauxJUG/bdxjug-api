package org.bdxjug.api.domain.banner;

import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author emoxb
 */
public interface BannerRepository {

    @Cacheable(CACHE_NAME)
    Optional<Banner> get();
    
    @CacheEvict(CACHE_NAME)
    default void clearCache(){
    }
    
    static final String CACHE_NAME = "banner";
}
