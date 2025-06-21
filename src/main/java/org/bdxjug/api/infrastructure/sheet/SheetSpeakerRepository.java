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

import org.bdxjug.api.domain.meetings.Speaker;
import org.bdxjug.api.domain.meetings.SpeakerID;
import org.bdxjug.api.domain.meetings.SpeakerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.bdxjug.api.infrastructure.sheet.Sheet.setValue;

@Component
public class SheetSpeakerRepository implements SpeakerRepository {

    private static final int ID = 0;
    private static final int FIRST_NAME = 1;
    private static final int LAST_NAME = 2;
    private static final int TWITTER = 3;
    private static final int URL_AVATAR = 4;
    private static final int BIO = 5;

    private final Sheet sheet;

    @Autowired
    public SheetSpeakerRepository(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public SortedSet<Speaker> all() {
        return new TreeSet<>(sheet.readLines(this::toSpeaker, "Speakers"));
    }

    private Speaker toSpeaker(String[] value) {
        if (value == null || value.length == 0) {
            return null;
        }
        Speaker speaker = new Speaker(new SpeakerID(value[ID]), value[FIRST_NAME], value[LAST_NAME]);
        setValue(value, TWITTER, speaker::setTwitter);
        setValue(value, URL_AVATAR, speaker::setUrlAvatar);
        setValue(value, BIO, speaker::setBio);
        return speaker;
    }

}
