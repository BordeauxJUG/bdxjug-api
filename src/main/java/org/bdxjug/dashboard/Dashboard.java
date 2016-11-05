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
import org.bdxjug.dashboard.members.Member;
import org.bdxjug.dashboard.members.MemberRepository;
import spark.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static spark.Spark.*;

public class Dashboard {

    private static final String ENV_PORT = "PORT";
    private static MeetingRepository meetingRepository;
    private static MemberRepository memberRepository;

    public static void main(String[] args) {
        setPort();
        setStaticFiles();
        initRepositories();

        ResponseTransformer jsonMapper = new Gson()::toJson;
        get("/api/meetings", Dashboard::meetings, jsonMapper);
        get("/api/meetings/:year", Dashboard::meetings, jsonMapper);
        get("/api/attendees/top", Dashboard::topAttendees, jsonMapper);
        get("/api/members", Dashboard::members, jsonMapper);

        after((request, res) -> res.type("application/json"));
    }

    private static void initRepositories() {
        meetingRepository = new MeetingRepository();
        memberRepository = new MemberRepository();
    }

    private static Integer getLimit(Request req, int defaultValue) {
        return ofNullable(req.queryParams("limit")).map(Integer::parseInt).orElse(defaultValue);
    }

    private static List<Member> members(Request req, Response res) {
        List<Member> allMembers = memberRepository.all();
        res.header("X-Count", String.valueOf(allMembers.size()));
        return allMembers;
    }

    private static List<Meeting> meetings(Request req, Response res) {
        Predicate<Meeting> filter = ofNullable(req.params("year"))
                .map(y -> (Predicate<Meeting>) m -> m.date().getYear() == Integer.parseInt(y))
                .orElse(meeting -> true);

        List<Meeting> allMeetings = meetingRepository.all();
        List<Meeting> filteredMeetings = allMeetings.stream().filter(filter).collect(Collectors.toList());
        res.header("X-TotalCount", String.valueOf(allMeetings.size()));
        res.header("X-Count", String.valueOf(filteredMeetings.size()));
        res.header("X-AverageAttendees", String.valueOf(filteredMeetings.stream().mapToInt(Meeting::nbAttendees).average().orElse(0d)));
        return filteredMeetings;
    }

    private static Map<String, Long> topAttendees(Request req, Response res) {
        int limit = getLimit(req, 10);
        List<Meeting> allMeetings = meetingRepository.all();
        Map<String, Long> countByAttendee = allMeetings.stream()
                .map(meetingRepository::attendees)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        MeetingAttendee::name, Collectors.counting()
                ));
        Map<String, Long> finalMap = new LinkedHashMap<>();
        countByAttendee.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        return finalMap;
    }

    private static void setPort() {
        ofNullable(System.getenv(ENV_PORT)).map(Integer::parseInt).ifPresent(Spark::port);
    }

    private static void setStaticFiles() {
        staticFiles.location("/public");
    }
}
