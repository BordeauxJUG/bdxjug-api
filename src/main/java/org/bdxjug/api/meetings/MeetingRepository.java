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
package org.bdxjug.api.meetings;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.bdxjug.api.interfaces.MeetupAPI;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MeetingRepository {

    private static final MeetupAPI API = MeetupAPI.api();
    private static final String JUG_GROUP = "BordeauxJUG";

    private final LoadingCache<String, List<MeetingAttendee>> attendeeByMeetingId;
    private final LoadingCache<Status, List<Meeting>> meetings;

    enum Status {
        past,
        upcoming
    }

    public MeetingRepository() {
        meetings = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(key ->
                    API.events(JUG_GROUP, key.name()).stream()
                    .filter(e -> e.yes_rsvp_count > getMinimumYes(key))
                    .map(MeetingRepository::toMeeting)
                    .sorted(Comparator.comparing(Meeting::date).reversed())
                    .collect(Collectors.toList()));

        attendeeByMeetingId = Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(key ->
                        API.eventAttendance(JUG_GROUP, key).stream()
                        .filter(a -> !a.member.id.equals("0"))
                        .filter(a -> a.rsvp.response.equals("yes"))
                        .filter(a -> !a.member.name.equals("Bordeaux JUG"))
                        .map(a -> new MeetingAttendee(a.member.id, a.member.name))
                        .collect(Collectors.toList()));
    }

    private int getMinimumYes(Status status) {
        if (status.equals(Status.past)) {
            return 17; // Filter JugOff
        }
        return 0;
    }

    public List<Meeting> pastMeetings() {
        return meetings.get(Status.past);
    }

    public List<Meeting> upcomingMeetings() {
        return meetings.get(Status.upcoming);
    }

    public List<Meeting> pastMeetingsByYear(int year) {
        return pastMeetings().stream().filter(m -> m.date().getYear() == year).collect(Collectors.toList());
    }

    public List<MeetingAttendee> attendees(Meeting meeting) {
        return attendeeByMeetingId.get(meeting.id());
    }

    private static Meeting toMeeting(MeetupAPI.Event e) {
        Meeting meeting = new Meeting(e.id, e.name, Instant.ofEpochMilli(e.time).atZone(ZoneId.systemDefault()).toLocalDate());
        meeting.setAttendance(e.yes_rsvp_count);
        meeting.setDescription(e.description);
        meeting.setRegistrationLink(e.link);
        if (e.venue != null) {
            String address = e.venue.address_1 + ", " + e.venue.city;
            meeting.setLocation(new Location(e.venue.name, address, new Geo(e.venue.lat, e.venue.lon)));
        }
        return meeting;
    }
}
