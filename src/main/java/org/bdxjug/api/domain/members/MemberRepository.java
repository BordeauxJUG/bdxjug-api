package org.bdxjug.api.domain.members;

import java.util.SortedSet;
import org.springframework.cache.annotation.CacheEvict;

public interface MemberRepository {

    SortedSet<Member> all();
    
}
