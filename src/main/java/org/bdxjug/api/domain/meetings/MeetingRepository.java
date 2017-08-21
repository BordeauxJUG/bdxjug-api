package org.bdxjug.api.domain.meetings;

import java.util.Optional;
import java.util.SortedSet;

public interface MeetingRepository {

    Optional<Meeting> by(MeetingID meetingID);

    SortedSet<Meeting> all();

    SortedSet<Meeting> pastMeetings();

    SortedSet<Meeting> upcomingMeetings();

    SortedSet<Meeting> pastMeetingsByYear(int year);
}
