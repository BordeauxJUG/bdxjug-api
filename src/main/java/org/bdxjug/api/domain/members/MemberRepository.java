package org.bdxjug.api.domain.members;

import java.util.SortedSet;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface MemberRepository {

    @Cacheable("members")
    SortedSet<Member> all();
    
    @CacheEvict("members")
    default void clearCache(){
    }
}
