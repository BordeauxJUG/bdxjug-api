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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Date;

@Component
public class MeetupPublisher implements MeetingPublisher {

    @Value("${bdxjug.meetup.group}")
    private String groupName;

    private final MeetupConfiguration meetupConfiguration;
    private final MeetingInfo meetingInfo;

    @Autowired
    public MeetupPublisher(MeetupConfiguration meetupConfiguration, MeetingInfo meetingInfo) {
        this.meetupConfiguration = meetupConfiguration;
        this.meetingInfo = meetingInfo;
    }

    @Override
    public RegistrationID publish(String authorizationCode, Meeting meeting) {
        MeetupClient.Admin admin = meetupConfiguration.admin(authorizationCode);
         MeetupClient.Event event = admin.announceEvent(groupName,
                meeting.getTitle(),
                meeting.getDescription(),
                toTime(meeting.getDate()),
                meetingInfo.locationOf(meeting).getVenueID().getValue());
        return new RegistrationID(event.id);
    }

    private long toTime(LocalDate date) {
        Instant instant = Instant.from(LocalDateTime.of(date, LocalTime.of(19, 0)).atZone(ZoneId.systemDefault()));
        return Date.from(instant).getTime();

    }

    @Override
    public String registerLink(RegistrationID registrationID) {
        return "https://www.meetup.com/" + groupName + "/events/" + registrationID.getValue();
    }
}
