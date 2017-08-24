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

import org.bdxjug.api.domain.meetings.Meeting;
import org.bdxjug.api.domain.meetings.MeetingInfo;
import org.bdxjug.api.domain.meetings.MeetingPublisher;
import org.bdxjug.api.domain.meetings.RegistrationID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeetupPublisher implements MeetingPublisher {

    private final MeetupClient client;
    private final MeetingInfo meetingInfo;

    @Autowired
    public MeetupPublisher(MeetupClient client, MeetingInfo meetingInfo) {
        this.client = client;
        this.meetingInfo = meetingInfo;
    }

    @Override
    public RegistrationID publish(Meeting meeting) {
        MeetupClient.AnnounceEvent announceEvent = new MeetupClient.AnnounceEvent();
        announceEvent.name = meeting.getTitle();
        announceEvent.announce = false;
        announceEvent.description = meeting.getDescription();
        announceEvent.time = meeting.getDate().toEpochDay();
        announceEvent.venue_id = meetingInfo.locationOf(meeting).getVenueID().getValue();
        MeetupClient.Event event = client.announceEvent("BordeauxJUG", announceEvent);
        return new RegistrationID(event.id);
    }

    @Override
    public String registerLink(RegistrationID registrationID) {
        return "https://www.meetup.com/BordeauxJUG/events/" + registrationID.getValue();
    }
}
