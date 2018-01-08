package org.bdxjug.api.domain.banner;

import java.util.Optional;

/**
 *
 * @author emoxb
 */
public interface BannerRepository {

    Optional<Banner> get();
    
}
