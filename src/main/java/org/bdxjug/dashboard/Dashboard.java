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
package org.bdxjug.dashboard;

import com.google.gson.Gson;
import org.bdxjug.dashboard.meetings.Meeting;
import org.bdxjug.dashboard.meetings.MeetingAttendee;
import org.bdxjug.dashboard.meetings.MeetingRepository;
import spark.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static spark.Spark.*;

public class Dashboard {

    public static void main(String[] args) {
        MeetingRepository meetingRepository = new MeetingRepository();

        setPort();

        List<Meeting> allMeetings = meetingRepository.all();
        get("/api/meetings", (req, res) -> {
            res.header("X-Count", String.valueOf(allMeetings.size()));
            res.header("X-AverageAttendees", String.valueOf(allMeetings.stream().mapToInt(Meeting::nbAttendees).average().orElse(0d)));
            res.type("application/json");
            return allMeetings;
        }, new Gson()::toJson);

        get("/api/attendees/top", (req, res) -> {
            Map<String, Long> countByAttendee = allMeetings.stream()
                    .map(meetingRepository::attendees)
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(
                            MeetingAttendee::name, Collectors.counting()
                    ));
            Map<String, Long> finalMap = new LinkedHashMap<>();
            countByAttendee.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
            res.type("application/json");
            return finalMap;
        }, new Gson()::toJson);

    }

    private static void setPort() {
        ofNullable(System.getenv("PORT")).map(Integer::parseInt).ifPresent(Spark::port);
    }
}
