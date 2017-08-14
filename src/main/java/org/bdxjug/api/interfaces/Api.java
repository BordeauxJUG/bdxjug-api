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
package org.bdxjug.api.interfaces;

import io.swagger.annotations.ApiOperation;
import org.bdxjug.api.domain.meetings.Meeting;
import org.bdxjug.api.domain.meetings.MeetingAttendee;
import org.bdxjug.api.domain.meetings.MeetingRepository;
import org.bdxjug.api.domain.members.Member;
import org.bdxjug.api.domain.members.MemberRepository;
import org.bdxjug.api.domain.speakers.Speaker;
import org.bdxjug.api.domain.speakers.SpeakerRepository;
import org.bdxjug.api.domain.sponsors.Sponsor;
import org.bdxjug.api.domain.sponsors.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@EnableSwagger2
@CrossOrigin(origins = "*", exposedHeaders = "origin, accept, content-type, X-Count, X-AverageAttendees")
@RestController
@RequestMapping("api")
public class Api {

    private static final String COUNT_HEADER = "X-Count";

    private final SponsorRepository googleSheetSponsorRepository;
    private final SpeakerRepository googleSheetSpeakerRepository;
    private final MemberRepository googleSheetMemberRepository;
    private final MeetingRepository meetupMeetingRepository;

    @Autowired
    public Api(SponsorRepository googleSheetSponsorRepository,
               SpeakerRepository googleSheetSpeakerRepository,
               MemberRepository googleSheetMemberRepository,
               MeetingRepository meetupMeetingRepository) {
        this.googleSheetSponsorRepository = googleSheetSponsorRepository;
        this.googleSheetSpeakerRepository = googleSheetSpeakerRepository;
        this.googleSheetMemberRepository = googleSheetMemberRepository;
        this.meetupMeetingRepository = meetupMeetingRepository;
    }

    @ApiOperation("Retrieve all sponsors")
    @GetMapping("sponsors")
    public ResponseEntity<List<Sponsor>> sponsors() {
        List<Sponsor> allSponsors = googleSheetSponsorRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allSponsors.size()));
        return ResponseEntity.ok().headers(headers).body(allSponsors);
    }

    @ApiOperation("Retrieve all speakers")
    @GetMapping("speakers")
    public ResponseEntity<List<Speaker>> speakers() {
        List<Speaker> allSpeakers = googleSheetSpeakerRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allSpeakers.size()));
        return ResponseEntity.ok().headers(headers).body(allSpeakers);
    }

    @ApiOperation("Retrieve all members")
    @GetMapping("members")
    public ResponseEntity<List<Member>> members() {
        List<Member> allMembers = googleSheetMemberRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMembers.size()));
        return ResponseEntity.ok().headers(headers).body(allMembers);
    }

    @ApiOperation("Retrieve all upcoming meetings")
    @GetMapping("meetings/upcoming")
    public ResponseEntity<List<Meeting>> upcomingMeetings() {
        List<Meeting> allMeetings = meetupMeetingRepository.upcomingMeetings();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMeetings.size()));
        return ResponseEntity.ok().headers(headers).body(allMeetings);
    }

    @ApiOperation("Retrieve all past meetings")
    @GetMapping("meetings/past")
    public ResponseEntity<List<Meeting>> pastMeetings() {
        return pastMeetings(null);
    }

    @ApiOperation("Retrieve all past meetings for a specific year")
    @GetMapping("meetings/past/{year}")
    public ResponseEntity<List<Meeting>> pastMeetings(@PathVariable("year") Integer year) {
        List<Meeting> allMeetings = ofNullable(year)
                .map(meetupMeetingRepository::pastMeetingsByYear)
                .orElseGet(meetupMeetingRepository::pastMeetings);
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMeetings.size()));
        headers.add("X-AverageAttendees", String.valueOf(allMeetings.stream().mapToInt(Meeting::getNbAttendees).average().orElse(0d)));
        return ResponseEntity.ok().headers(headers).body(allMeetings);
    }

    @GetMapping("attendees/top")
    public ResponseEntity topAttendees(@RequestParam(value = "limit", required = false) Integer limitParam) {
        Integer limit = Optional.ofNullable(limitParam).orElse(10);
        List<Meeting> allMeetings = meetupMeetingRepository.pastMeetings();
        Map<String, Long> countByAttendee = allMeetings.stream()
                .map(meetupMeetingRepository::attendees)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        MeetingAttendee::getName, Collectors.counting()
                ));
        Map<String, Long> finalMap = new LinkedHashMap<>();
        countByAttendee.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        return ResponseEntity.ok(finalMap);
    }
}