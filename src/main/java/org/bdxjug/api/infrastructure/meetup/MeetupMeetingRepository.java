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
package org.bdxjug.api.infrastructure.meetup;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.bdxjug.api.domain.meetings.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class MeetupMeetingRepository implements MeetingRepository {

    private static final String JUG_GROUP = "BordeauxJUG";
    private final MeetupClient meetupClient;

    private LoadingCache<String, List<MeetingAttendee>> attendeeByMeetingId;
    private LoadingCache<Status, List<Meeting>> meetings;

    enum Status {
        past,
        upcoming
    }

    @Autowired
    public MeetupMeetingRepository(MeetupClient meetupClient) {
        this.meetupClient = meetupClient;
    }

    private void loadCache() {
        if (meetings == null) {
            meetings = Caffeine.newBuilder()
                    .expireAfterAccess(30, TimeUnit.MINUTES)
                    .build(key ->
                            meetupClient.events(JUG_GROUP, key.name()).stream()
                                    .filter(e -> e.yes_rsvp_count > getMinimumYes(key))
                                    .map(MeetupMeetingRepository::toMeeting)
                                    .sorted(Comparator.comparing(Meeting::getDate).reversed())
                                    .collect(Collectors.toList()));
        }

        if (attendeeByMeetingId == null) {
            attendeeByMeetingId = Caffeine.newBuilder()
                    .expireAfterAccess(30, TimeUnit.MINUTES)
                    .build(key ->
                            meetupClient.eventAttendance(JUG_GROUP, key).stream()
                                    .filter(a -> !a.member.id.equals("0"))
                                    .filter(a -> a.rsvp.response.equals("yes"))
                                    .filter(a -> !a.member.name.equals("Bordeaux JUG"))
                                    .map(a -> new MeetingAttendee(a.member.id, a.member.name))
                                    .collect(Collectors.toList()));
        }
    }

    private int getMinimumYes(Status status) {
        if (status.equals(Status.past)) {
            return 17; // Filter JugOff
        }
        return 0;
    }

    @Override
    public List<Meeting> pastMeetings() {
        loadCache();
        return meetings.get(Status.past);
    }

    @Override
    public List<Meeting> upcomingMeetings() {
        loadCache();
        return meetings.get(Status.upcoming);
    }

    @Override
    public List<Meeting> pastMeetingsByYear(int year) {
        loadCache();
        return pastMeetings().stream().filter(m -> m.getDate().getYear() == year).collect(Collectors.toList());
    }

    @Override
    public List<MeetingAttendee> attendees(Meeting meeting) {
        loadCache();
        return attendeeByMeetingId.get(meeting.getId());
    }

    private static Meeting toMeeting(MeetupClient.Event e) {
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
