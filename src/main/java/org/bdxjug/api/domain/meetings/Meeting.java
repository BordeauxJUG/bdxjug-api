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

import lombok.Data;

import java.text.Normalizer;
import java.time.LocalDate;

@Data
public class Meeting implements Comparable<Meeting> {

    private final MeetingID id;
    private final LocalDate date;
    private final SpeakerID speakerID;
    private final SpeakerID coSpeakerID;
    private final LocationID locationID;
    private final String title;
    private String summary;
    private String description;
    private RegistrationID registrationID;
    private String videoLink;

    public boolean isPast() {
        return date.isBefore(LocalDate.now());
    }

    public boolean isUpcoming() {
        return date.isAfter(LocalDate.now().minusDays(1l));
    }

    public boolean isPublished() {
        return registrationID != null;
    }

    public String formattedUrl() {
        String normalizedTitle = Normalizer.normalize(title, Normalizer.Form.NFD);
        String accentRemovedTitle = normalizedTitle.replaceAll("\\p{M}", "");
        String url = id.getValue() + "-" + accentRemovedTitle.toLowerCase()
                .replace(" ", "_")
                .replace("|", "_")
                .replace("/", "_")
                .replace("\\", "_");
        return url;
    }

    @Override
    public int compareTo(Meeting o) {
        return o.date.compareTo(this.date);
    }
}
