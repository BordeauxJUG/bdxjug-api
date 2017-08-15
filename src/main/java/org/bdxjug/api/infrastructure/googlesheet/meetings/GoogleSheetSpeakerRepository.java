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
package org.bdxjug.api.infrastructure.googlesheet.meetings;

import org.bdxjug.api.domain.meetings.Speaker;
import org.bdxjug.api.domain.meetings.SpeakerID;
import org.bdxjug.api.domain.meetings.SpeakerRepository;
import org.bdxjug.api.infrastructure.googlesheet.GoogleSheetClient;
import org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoogleSheetSpeakerRepository extends GoogleSheetRepository<Speaker> implements SpeakerRepository {

    @Autowired
    public GoogleSheetSpeakerRepository(GoogleSheetClient client) {
        super(client);
    }

    @Override
    public List<Speaker> all() {
        return batchGet(this::toSpeaker, "'Speakers'!A2:Z");
    }

    private Speaker toSpeaker(String[] value) {
        Speaker speaker = new Speaker(new SpeakerID(value[0]), value[1], value[2]);
        setValue(value, 3, speaker::setTwitter);
        setValue(value, 4, speaker::setUrlAvatar);
        setValue(value, 5, speaker::setBio);
        return speaker;
    }

}
