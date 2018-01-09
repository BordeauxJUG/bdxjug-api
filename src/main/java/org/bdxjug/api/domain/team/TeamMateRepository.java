package org.bdxjug.api.domain.team;

import java.util.SortedSet;

public interface TeamMateRepository {

    SortedSet<TeamMate> all();
    SortedSet<TeamMate> byYear(Integer year);
}
