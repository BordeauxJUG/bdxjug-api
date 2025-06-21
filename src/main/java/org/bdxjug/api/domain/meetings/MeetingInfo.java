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
package org.bdxjug.api.domain.meetings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeetingInfo {

    private final LocationRepository locationRepository;
    private final SpeakerRepository speakerRepository;

    @Autowired
    public MeetingInfo(LocationRepository locationRepository, SpeakerRepository speakerRepository) {
        this.locationRepository = locationRepository;
        this.speakerRepository = speakerRepository;
    }

    public Speaker speakerInfo(SpeakerID speakerID) {
        if (speakerID == null || speakerID.getValue() == null || speakerID.getValue().isEmpty()) return null;
        return speakerRepository.all().stream().filter(s -> s.getId().equals(speakerID)).findFirst().
                orElseThrow(() -> new IllegalStateException("No speaker for id" + speakerID));
    }

    public Location locationOf(Meeting meeting) {
        return locationRepository.all().stream().filter(s -> s.getId().equals(meeting.getLocationID())).findFirst().
                orElseThrow(() -> new IllegalStateException("No location for " + meeting));
    }
}
