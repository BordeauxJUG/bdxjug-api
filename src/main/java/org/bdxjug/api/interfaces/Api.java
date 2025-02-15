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

import io.swagger.v3.oas.annotations.Operation;
import org.bdxjug.api.application.AnnounceMeeting;
import org.bdxjug.api.domain.banner.BannerRepository;
import org.bdxjug.api.domain.meetings.Location;
import org.bdxjug.api.domain.meetings.LocationRepository;
import org.bdxjug.api.domain.meetings.Meeting;
import org.bdxjug.api.domain.meetings.MeetingID;
import org.bdxjug.api.domain.meetings.MeetingRepository;
import org.bdxjug.api.domain.meetings.Speaker;
import org.bdxjug.api.domain.meetings.SpeakerRepository;
import org.bdxjug.api.domain.members.Member;
import org.bdxjug.api.domain.members.MemberRepository;
import org.bdxjug.api.domain.sponsors.Sponsor;
import org.bdxjug.api.domain.sponsors.SponsorRepository;
import org.bdxjug.api.domain.team.TeamMateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
    private final BannerRepository bannerRepository;
    private final AnnounceMeeting announceMeeting;
    private final TeamMateRepository teamMateRepository;

    @Autowired
    public Api(SponsorRepository sponsorRepository,
               SpeakerRepository speakerRepository,
               MemberRepository memberRepository,
               MeetingRepository meetingRepository,
               LocationRepository locationRepository,
               BannerRepository bannerRepository,
               AnnounceMeeting announceMeeting,
               TeamMateRepository teamMateRepository) {
        this.sponsorRepository = sponsorRepository;
        this.speakerRepository = speakerRepository;
        this.memberRepository = memberRepository;
        this.meetingRepository = meetingRepository;
        this.locationRepository = locationRepository;
        this.bannerRepository = bannerRepository;
        this.announceMeeting = announceMeeting;
        this.teamMateRepository = teamMateRepository;
    }

    @Operation(summary = "Retrieve all locations")
    @GetMapping("locations")
    public ResponseEntity<List<Location>> locations() {
        List<Location> allLocations = locationRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allLocations.size()));
        return ResponseEntity.ok().headers(headers).body(allLocations);
    }

    @Operation(summary = "Retrieve all sponsors")
    @GetMapping("sponsors")
    public ResponseEntity<List<Sponsor>> sponsors() {
        List<Sponsor> allSponsors = sponsorRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allSponsors.size()));
        return ResponseEntity.ok().headers(headers).body(allSponsors);
    }

    @Operation(summary = "Retrieve all speakers")
    @GetMapping("speakers")
    public ResponseEntity<SortedSet<Speaker>> speakers() {
        SortedSet<Speaker> allSpeakers = speakerRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allSpeakers.size()));
        return ResponseEntity.ok().headers(headers).body(allSpeakers);
    }

    @Operation(summary = "Retrieve all members")
    @GetMapping("members")
    public ResponseEntity<SortedSet<Member>> members() {
        SortedSet<Member> allMembers = memberRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMembers.size()));
        return ResponseEntity.ok().headers(headers).body(allMembers);
    }

    @Operation(summary = "Retrieve all meetings")
    @GetMapping("meetings")
    public ResponseEntity<SortedSet<Meeting>> meetings() {
        SortedSet<Meeting> allMeetings = meetingRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMeetings.size()));
        return ResponseEntity.ok().headers(headers).body(allMeetings);
    }

    @Operation(summary = "Retrieve not published meetings")
    @GetMapping("meetings/not-published")
    public ResponseEntity<SortedSet<Meeting>> notPublishedMeetings() {
        SortedSet<Meeting> allMeetings = meetingRepository.all().stream().filter(m -> !m.isPublished()).collect(Collectors.toCollection(TreeSet::new));
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMeetings.size()));
        return ResponseEntity.ok().headers(headers).body(allMeetings);
    }

    @Operation(summary = "Retrieve a meeting by id")
    @GetMapping("meetings/{id}")
    public ResponseEntity<Meeting> meeting(@PathVariable("id") String id) {
        return meetingRepository.by(new MeetingID(id))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Operation(summary = "Announce a meeting")
    @PutMapping("meetings/{id}/announcement")
    public ResponseEntity<AnnounceMeeting.Announcement> publishMeeting(@PathVariable("id") String id, @RequestHeader("Authorization") String authorizationCode) {
        Optional<Meeting> meeting = meetingRepository.by(new MeetingID(id));
        if (meeting.isPresent()) {
            AnnounceMeeting.Announcement announcement = announceMeeting.execute(authorizationCode, meeting.get());
            return ResponseEntity.ok(announcement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Retrieve all upcoming meetings")
    @GetMapping("meetings/upcoming")
    public ResponseEntity<SortedSet<Meeting>> upcomingMeetings() {
        SortedSet<Meeting> allMeetings = meetingRepository.upcomingMeetings();
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMeetings.size()));
        return ResponseEntity.ok().headers(headers).body(allMeetings);
    }

    @Operation(summary = "Retrieve all past meetings")
    @GetMapping("meetings/past")
    public ResponseEntity<SortedSet<Meeting>> pastMeetings() {
        return pastMeetings(null);
    }

    @Operation(summary = "Retrieve all past meeting for a given year")
    @GetMapping("meetings/past/{year}")
    public ResponseEntity<SortedSet<Meeting>> pastMeetings(@PathVariable("year") Integer year) {
        SortedSet<Meeting> allMeetings = ofNullable(year)
                .map(meetingRepository::pastMeetingsByYear)
                .orElseGet(meetingRepository::pastMeetings);
        HttpHeaders headers = new HttpHeaders();
        headers.add(COUNT_HEADER, String.valueOf(allMeetings.size()));
        return ResponseEntity.ok().headers(headers).body(allMeetings);
    }

    @Operation(summary = "clear all cache used")
    @GetMapping("cache/clear")
    public void clearCache() {
        bannerRepository.clearCache();
        locationRepository.clearCache();
        meetingRepository.clearCache();
        memberRepository.clearCache();
        speakerRepository.clearCache();
        sponsorRepository.clearCache();
        teamMateRepository.clearCache();
    }

}
