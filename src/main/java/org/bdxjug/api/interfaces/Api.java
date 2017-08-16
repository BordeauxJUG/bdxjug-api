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

import org.bdxjug.api.domain.meetings.*;
import org.bdxjug.api.domain.members.Member;
import org.bdxjug.api.domain.members.MemberRepository;
import org.bdxjug.api.domain.sponsors.Sponsor;
import org.bdxjug.api.domain.sponsors.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.SortedSet;

import static java.util.Optional.ofNullable;

@CrossOrigin(origins = "*", exposedHeaders = "origin, accept, content-type, X-Count, X-AverageAttendees")
@RestController
@RequestMapping("api")
public class Api {

    private static final String COUNT_HEADER = "X-Count";

    private final SponsorRepository sponsorRepository;
    private final SpeakerRepository speakerRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public Api(SponsorRepository sponsorRepository,
               SpeakerRepository speakerRepository,
               MemberRepository memberRepository,
               MeetingRepository meetingRepository,
               LocationRepository locationRepository) {
        this.sponsorRepository = sponsorRepository;
        this.speakerRepository = speakerRepository;
        this.memberRepository = memberRepository;
        this.meetingRepository = meetingRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("locations")
    public ResponseEntity<List<Location>> locations() {
        List<Location> allLocations = locationRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allLocations.size()));
        return ResponseEntity.ok().headers(headers).body(allLocations);
    }

    @GetMapping("sponsors")
    public ResponseEntity<List<Sponsor>> sponsors() {
        List<Sponsor> allSponsors = sponsorRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allSponsors.size()));
        return ResponseEntity.ok().headers(headers).body(allSponsors);
    }

    @GetMapping("speakers")
    public ResponseEntity<List<Speaker>> speakers() {
        List<Speaker> allSpeakers = speakerRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allSpeakers.size()));
        return ResponseEntity.ok().headers(headers).body(allSpeakers);
    }

    @GetMapping("members")
    public ResponseEntity<List<Member>> members() {
        List<Member> allMembers = memberRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMembers.size()));
        return ResponseEntity.ok().headers(headers).body(allMembers);
    }

    @GetMapping("meetings")
    public ResponseEntity<SortedSet<Meeting>> meetings() {
        SortedSet<Meeting> allMeetings = meetingRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMeetings.size()));
        return ResponseEntity.ok().headers(headers).body(allMeetings);
    }

    @GetMapping("meetings/upcoming")
    public ResponseEntity<SortedSet<Meeting>> upcomingMeetings() {
        SortedSet<Meeting> allMeetings = meetingRepository.upcomingMeetings();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMeetings.size()));
        return ResponseEntity.ok().headers(headers).body(allMeetings);
    }

    @GetMapping("meetings/past")
    public ResponseEntity<SortedSet<Meeting>> pastMeetings() {
        return pastMeetings(null);
    }

    @GetMapping("meetings/past/{year}")
    public ResponseEntity<SortedSet<Meeting>> pastMeetings(@PathVariable("year") Integer year) {
        SortedSet<Meeting> allMeetings = ofNullable(year)
                .map(meetingRepository::pastMeetingsByYear)
                .orElseGet(meetingRepository::pastMeetings);
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMeetings.size()));
        return ResponseEntity.ok().headers(headers).body(allMeetings);
    }

}
