package org.bdxjug.api.domain.meetings;

import java.util.SortedSet;

public interface MeetingRepository {

    SortedSet<Meeting> all();

    SortedSet<Meeting> pastMeetings();

    SortedSet<Meeting> upcomingMeetings();

    SortedSet<Meeting> pastMeetingsByYear(int year);
}
