package org.bdxjug.api.domain.meetings;

import java.util.Optional;
import java.util.SortedSet;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface MeetingRepository {

    Optional<Meeting> by(MeetingID meetingID);

    SortedSet<Meeting> all();

    @Cacheable("pastMeetings")
    SortedSet<Meeting> pastMeetings();

    @Cacheable("upcomingMeetings")
    SortedSet<Meeting> upcomingMeetings();

    SortedSet<Meeting> pastMeetingsByYear(int year);
    
    @CacheEvict({"pastMeetings", "upcomingMeetings"})
    default void clearCache(){
    }
    
}
