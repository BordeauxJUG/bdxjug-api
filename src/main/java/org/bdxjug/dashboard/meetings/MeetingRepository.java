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
        return API.pastEvents(JUG_GROUP).stream().map(MeetingRepository::toMeeting).collect(Collectors.toList());
    }

    private static Meeting toMeeting(MeetupAPI.Event e) {
        Meeting meeting = new Meeting(e.id, e.name, e.time);
        meeting.setNbParticipants(e.yes_rsvp_count);
        return meeting;
    }
}
