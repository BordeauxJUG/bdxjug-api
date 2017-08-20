package org.bdxjug.api.domain.members;

import java.util.SortedSet;

public interface MemberRepository {

    SortedSet<Member> all();
}
