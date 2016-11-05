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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static spark.Spark.*;

public class Dashboard {

    private static final String PROD_MODE = "PROD_MODE";
    private static final String ENV_PORT = "PORT";

    public static void main(String[] args) {
        setPort();
        setStaticFiles();

        MeetingRepository meetingRepository = new MeetingRepository();
        MemberRepository memberRepository = new MemberRepository();

        get("/api/meetings", (req, res) -> meetings(meetingRepository, res), new Gson()::toJson);
        get("/api/attendees/top", (req, res) -> topAttendees(meetingRepository), new Gson()::toJson);
        get("/api/members", (req, res) -> members(memberRepository, res), new Gson()::toJson);

        after((request, res) -> res.type("application/json"));
    }

    private static List<Member> members(MemberRepository memberRepository, Response res) {
        List<Member> allMembers = memberRepository.all();
        res.header("X-Count", String.valueOf(allMembers.size()));
        return allMembers;
    }

    private static List<Meeting> meetings(MeetingRepository meetingRepository, Response res) {
        List<Meeting> allMeetings = meetingRepository.all();
        res.header("X-Count", String.valueOf(allMeetings.size()));
        res.header("X-AverageAttendees", String.valueOf(allMeetings.stream().mapToInt(Meeting::nbAttendees).average().orElse(0d)));
        return allMeetings;
    }

    private static Map<String, Long> topAttendees(MeetingRepository meetingRepository) {
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
                .limit(10)
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        return finalMap;
    }

    private static void setStaticFiles() {
        if (Boolean.valueOf(System.getProperty(PROD_MODE))) {
            staticFiles.location("/public");
        } else {
            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources/public";
            staticFiles.externalLocation(projectDir + staticDir);
        }
    }

    private static void setPort() {
        ofNullable(System.getenv(ENV_PORT)).map(Integer::parseInt).ifPresent(Spark::port);
    }
}
