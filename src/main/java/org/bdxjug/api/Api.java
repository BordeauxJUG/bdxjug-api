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

import org.bdxjug.api.members.Member;
import org.bdxjug.api.members.MemberRepository;
import org.bdxjug.api.speakers.Speaker;
import org.bdxjug.api.speakers.SpeakerRepository;
import org.bdxjug.api.sponsors.Sponsor;
import org.bdxjug.api.sponsors.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class Api {

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

    private final SponsorRepository sponsorRepository;
    private final SpeakerRepository speakerRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public Api(SponsorRepository sponsorRepository,
               SpeakerRepository speakerRepository,
               MemberRepository memberRepository) {
        this.sponsorRepository = sponsorRepository;
        this.speakerRepository = speakerRepository;
        this.memberRepository = memberRepository;
    }

    @RequestMapping("sponsors")
    public ResponseEntity sponsors() {
        List<Sponsor> allSponsors = sponsorRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(Headers.X_COUNT.headerName, String.valueOf(allSponsors.size()));
        return ResponseEntity.ok().headers(headers).body(allSponsors);
    }

    @RequestMapping("speakers")
    public ResponseEntity speakers() {
        List<Speaker> allSpeakers = speakerRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(Headers.X_COUNT.headerName, String.valueOf(allSpeakers.size()));
        return ResponseEntity.ok().headers(headers).body(allSpeakers);
    }

    @RequestMapping("members")
    public ResponseEntity members() {
        List<Member> allMembers = memberRepository.all();
        HttpHeaders headers = new HttpHeaders();
        headers.add(Headers.X_COUNT.headerName, String.valueOf(allMembers.size()));
        return ResponseEntity.ok().headers(headers).body(allMembers);
    }
}
