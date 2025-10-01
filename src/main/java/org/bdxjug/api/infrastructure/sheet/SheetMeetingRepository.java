/*
 * Copyright 2016 Benoît Prioux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bdxjug.api.infrastructure.sheet;

import org.bdxjug.api.domain.meetings.LocationID;
import org.bdxjug.api.domain.meetings.Meeting;
import org.bdxjug.api.domain.meetings.MeetingID;
import org.bdxjug.api.domain.meetings.MeetingRepository;
import org.bdxjug.api.domain.meetings.RegistrationID;
import org.bdxjug.api.domain.meetings.SpeakerID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.bdxjug.api.infrastructure.sheet.Sheet.parseDate;
import static org.bdxjug.api.infrastructure.sheet.Sheet.setValue;

@Component
public class SheetMeetingRepository implements MeetingRepository {

    private static final int ID = 0;
    private static final int DATE = 1;
    private static final int SPEAKER_ID = 2;
    private static final int COSPEAKER_ID = 3;
    private static final int LOCATION_ID = 4;
    private static final int TITLE = 5;
    private static final int SUMMARY = 6;
    private static final int DESCRIPTION = 7;
    private static final int REGISTRATION_ID = 8;
    private static final int VIDEO_LINK = 9;
    private static final int POSTER_LINK = 12;

    private final Sheet sheet;

    @Autowired
    public SheetMeetingRepository(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Optional<Meeting> by(MeetingID meetingID) {
        return all().stream().filter(m -> m.getId().equals(meetingID)).findFirst();
    }

    @Override
    public SortedSet<Meeting> all() {
        return new TreeSet<>(sheet.readLines(this::toMeeting, "Meetings"));
    }

    private Meeting toMeeting(String[] values) {
        Meeting meeting = new Meeting(
                new MeetingID(values[ID]),
                parseDate(values[DATE]),
                new SpeakerID(values[SPEAKER_ID]),
                new SpeakerID(values[COSPEAKER_ID]),
                new LocationID(values[LOCATION_ID]),
                values[TITLE]);
        setValue(values, SUMMARY, meeting::setSummary);
        setValue(values, DESCRIPTION, meeting::setDescription);
        setValue(values, REGISTRATION_ID, s -> meeting.setRegistrationID(new RegistrationID(s)));
        setValue(values, VIDEO_LINK, meeting::setVideoLink);
        setValue(values, POSTER_LINK, meeting::setPosterLink);
        return meeting;
    }

    @Override
    public SortedSet<Meeting> pastMeetings() {
        return all().stream().filter(Meeting::isPast).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public SortedSet<Meeting> upcomingMeetings() {
        return all().stream().filter(Meeting::isUpcoming).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public SortedSet<Meeting> pastMeetingsByYear(int year) {
        return all().stream().filter(m -> m.getDate().getYear() == year).collect(Collectors.toCollection(TreeSet::new));
    }

}
