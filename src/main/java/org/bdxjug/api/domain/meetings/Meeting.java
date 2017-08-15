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

import java.time.LocalDate;

public class Meeting implements Comparable<Meeting> {

   private final MeetingID id;
   private final LocalDate date;
   private final SpeakerID speaker;
   private final LocationID location;
   private final String title;
   private String summary;
   private String description;

    public Meeting(MeetingID id, LocalDate date, SpeakerID speaker, LocationID location, String title) {
        this.id = id;
        this.date = date;
        this.speaker = speaker;
        this.location = location;
        this.title = title;
    }

    public MeetingID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public SpeakerID getSpeaker() {
        return speaker;
    }

    public LocationID getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPast() {
        return date.isBefore(LocalDate.now());
    }

    public boolean isUpcoming() {
        return date.isAfter(LocalDate.now());
    }

    @Override
    public int compareTo(Meeting o) {
        return o.date.compareTo(this.date);
    }
}
