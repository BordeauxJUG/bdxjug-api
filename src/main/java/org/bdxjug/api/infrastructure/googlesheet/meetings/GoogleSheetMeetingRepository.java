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

import org.bdxjug.api.domain.meetings.*;
import org.bdxjug.api.infrastructure.googlesheet.GoogleSheetClient;
import org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository.parseDate;
import static org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository.setValue;

@Component
public class GoogleSheetMeetingRepository implements MeetingRepository {

    private final GoogleSheetRepository repository;

    @Autowired
    public GoogleSheetMeetingRepository(GoogleSheetRepository repository) {
        this.repository = repository;
    }

    @Override
    public SortedSet<Meeting> all() {
        return new TreeSet<>(repository.batchGet(this::toMeeting, "'Meetings'!A2:Z"));
    }

    private Meeting toMeeting(String[] values) {
        Meeting meeting = new Meeting(
                new MeetingID(values[0]),
                parseDate(values[1]),
                new SpeakerID(values[2]),
                new LocationID(values[3]),
                values[4]);
        setValue(values, 5, meeting::setSummary);
        setValue(values, 6, meeting::setDescription);
        return meeting;
    }

    @Override
    public SortedSet<Meeting> pastMeetings() {
        return all().stream().filter(Meeting::isPast).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public SortedSet<Meeting> upcomingMeetings() {
        return all().stream().filter(Meeting::isUpcoming).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public SortedSet<Meeting> pastMeetingsByYear(int year) {
        return all().stream().filter(m -> m.getDate().getYear() == year).collect(Collectors.toCollection(TreeSet::new));
    }

}
