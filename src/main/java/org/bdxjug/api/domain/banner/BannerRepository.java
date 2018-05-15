package org.bdxjug.api.domain.banner;

import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author emoxb
 */
public interface BannerRepository {

	public static final String TAILLE_GRANDE="GRANDE";
	public static final String TAILLE_PETITE="PETITE";
	
    @Cacheable("pt" + CACHE_NAME)
    Optional<Banner> getPetite();
    
    @Cacheable("gd_" + CACHE_NAME)
    Optional<Banner> getGrande();
    
    @CacheEvict("gd" + CACHE_NAME)
    default void clearCache(){
    }
    
    static final String CACHE_NAME = "banner";
}
