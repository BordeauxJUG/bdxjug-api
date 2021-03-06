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
package org.bdxjug.api.application;

import lombok.Data;
import org.bdxjug.api.domain.meetings.Meeting;
import org.bdxjug.api.domain.meetings.MeetingPublisher;
import org.bdxjug.api.domain.meetings.RegistrationID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnnounceMeeting {

    private final MeetingPublisher publisher;

    @Autowired
    public AnnounceMeeting(MeetingPublisher publisher) {
        this.publisher = publisher;
    }

    @Data
    public static class Announcement {
        private final RegistrationID registrationID;
        private final String registerLink;
    }

    public Announcement execute(String authorizationCode, Meeting meeting) {
        if (!meeting.isPublished()) {
            RegistrationID registrationID = this.publisher.publish(authorizationCode, meeting);
            meeting.setRegistrationID(registrationID);
        }
        return new Announcement(meeting.getRegistrationID(), publisher.registerLink(meeting.getRegistrationID()));
    }

}
