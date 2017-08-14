package org.bdxjug.api.domain.meetings;

import java.util.List;

public interface MeetingRepository {

    List<Meeting> pastMeetings();

    List<Meeting> upcomingMeetings();

    List<Meeting> pastMeetingsByYear(int year);

    List<MeetingAttendee> attendees(Meeting meeting);
}
