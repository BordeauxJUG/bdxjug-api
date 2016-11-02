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
package org.bdxjug.dashboard.meetings;

import java.util.List;
import java.util.stream.Collectors;

public class MeetingRepository {

    private static final MeetupAPI API = MeetupAPI.api();
    private static final String JUG_GROUP = "BordeauxJUG";

    public List<Meeting> all() {
        return API.pastEvents(JUG_GROUP).stream()
                .filter(e -> e.yes_rsvp_count > 17) // Filter JugOff
                .map(MeetingRepository::toMeeting)
                .collect(Collectors.toList());
    }

    public List<MeetingAttendee> attendees(Meeting meeting) {
        return API.eventAttendance(JUG_GROUP, meeting.id()).stream()
                .filter(a -> !a.member.id.equals("0"))
                .filter(a -> a.rsvp.response.equals("yes"))
                .filter(a -> !a.member.name.equals("Bordeaux JUG"))
                .map(a -> new MeetingAttendee(a.member.id, a.member.name))
                .collect(Collectors.toList());
    }

    private static Meeting toMeeting(MeetupAPI.Event e) {
        Meeting meeting = new Meeting(e.id, e.name, e.time);
        meeting.setAttendance(e.yes_rsvp_count);
        return meeting;
    }
}
