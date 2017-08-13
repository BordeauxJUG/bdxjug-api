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
package org.bdxjug.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bdxjug.api.meetings.Meeting;
import org.bdxjug.api.meetings.MeetingAttendee;
import org.bdxjug.api.meetings.MeetingRepository;
import org.bdxjug.api.members.Member;
import org.bdxjug.api.members.MemberRepository;
import org.bdxjug.api.speakers.Speaker;
import org.bdxjug.api.speakers.SpeakerRepository;
import org.bdxjug.api.sponsors.Sponsor;
import org.bdxjug.api.sponsors.SponsorRepository;
import spark.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static spark.Spark.*;

public class Server {

    private enum Headers {
        ORIGIN("origin"),
        ACCEPT("accept"),
        CONTENT_TYPE("content-type"),
        X_COUNT("X-Count"),
        X_AVERAGE_ATTENDEES("X-AverageAttendees");

        private final String headerName;
        Headers(String headerName) {
            this.headerName = headerName;
        }
        static String join(String separator) {
            return Arrays.stream(values()).map(h -> h.headerName).collect(Collectors.joining(separator));
        }
    }
    private static final String METHODS = "GET, POST, PUT, DELETE, OPTIONS, HEAD";

    private static MeetingRepository meetingRepository;
    private static MemberRepository memberRepository;
    private static SpeakerRepository speakerRepository;
    private static SponsorRepository sponsorRepository;

    public static void main(String[] args) {
        setPort();
        setStaticFiles();
        initRepositories();

        enableCORS("*", METHODS, Headers.join(","));

        ResponseTransformer jsonMapper = configureGson(false)::toJson;
        get("/api/meetings/upcoming", Server::upcomingMeetings, jsonMapper);
        get("/api/meetings/past", Server::pastMeetings, jsonMapper);
        get("/api/meetings/past/:year", Server::pastMeetings, jsonMapper);
        get("/api/attendees/top", Server::topAttendees, jsonMapper);
        get("/api/members", Server::members, jsonMapper);
        get("/api/speakers", Server::speakers, jsonMapper);

        after((req, res) -> res.type("application/json"));
    }

    static Gson configureGson(boolean pretty) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (pretty) {
            gsonBuilder.setPrettyPrinting();
        }
        return gsonBuilder.create();
    }

    private static void initRepositories() {
        meetingRepository = new MeetingRepository();
        memberRepository = new MemberRepository();
        speakerRepository = new SpeakerRepository();
        sponsorRepository = new SponsorRepository();
    }

    private static Integer getLimit(Request req, int defaultValue) {
        return ofNullable(req.queryParams("limit")).map(Integer::parseInt).orElse(defaultValue);
    }

    private static List<Speaker> speakers(Request req, Response res) {
        List<Speaker> allSpeakers = speakerRepository.all();
        res.header(Headers.X_COUNT.headerName, String.valueOf(allSpeakers.size()));
        return allSpeakers;
    }

    private static List<Member> members(Request req, Response res) {
        List<Member> allMembers = memberRepository.all();
        res.header(Headers.X_COUNT.headerName, String.valueOf(allMembers.size()));
        return allMembers;
    }

    private static List<Meeting> upcomingMeetings(Request req, Response res) {
        List<Meeting> allMeetings = meetingRepository.upcomingMeetings();
        res.header(Headers.X_COUNT.headerName, String.valueOf(allMeetings.size()));
        return allMeetings;
    }

    private static List<Meeting> pastMeetings(Request req, Response res) {
        List<Meeting> allMeetings = ofNullable(req.params("year"))
                .map(Integer::parseInt)
                .map(y -> meetingRepository.pastMeetingsByYear(y))
                .orElseGet(() -> meetingRepository.pastMeetings());
        res.header(Headers.X_COUNT.headerName, String.valueOf(allMeetings.size()));
        res.header(Headers.X_AVERAGE_ATTENDEES.headerName, String.valueOf(allMeetings.stream().mapToInt(Meeting::nbAttendees).average().orElse(0d)));
        return allMeetings;
    }

    private static Map<String, Long> topAttendees(Request req, Response res) {
        int limit = getLimit(req, 10);
        List<Meeting> allMeetings = meetingRepository.pastMeetings();
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
        ofNullable(Configuration.PORT.value()).map(Integer::parseInt).ifPresent(Spark::port);
    }

    private static void setStaticFiles() {
        staticFiles.location("/public");
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {
        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", origin);
            res.header("Access-Control-Request-Method", methods);
            res.header("Access-Control-Allow-Headers", headers);
        });
    }
}
