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

import java.util.SortedSet;
import org.bdxjug.api.domain.meetings.LocationRepository;
import org.bdxjug.api.domain.meetings.Meeting;
import org.bdxjug.api.domain.meetings.MeetingInfo;
import org.bdxjug.api.domain.meetings.MeetingRepository;
import org.bdxjug.api.domain.meetings.SpeakerRepository;
import org.bdxjug.api.domain.members.MemberRepository;
import org.bdxjug.api.domain.sponsors.SponsorRepository;
import org.bdxjug.api.domain.team.TeamMateRepository;
import org.bdxjug.api.infrastructure.meetup.MeetupConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class Web {

    private final MeetingInfo meetingInfo;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final SpeakerRepository speakerRepository;
    private final SponsorRepository sponsorRepository;
    private final LocationRepository locationRepository;
    private final TeamMateRepository teamMateRepository;
    private final MeetupConfiguration meetupConfiguration;

    @Autowired
    public Web(MeetingInfo meetingInfo,
               MeetingRepository meetingRepository,
               MemberRepository memberRepository,
               SpeakerRepository speakerRepository,
               SponsorRepository sponsorRepository,
               LocationRepository locationRepository,
               MeetupConfiguration meetupConfiguration,
               TeamMateRepository teamMateRepository) {
        this.meetingInfo = meetingInfo;
        this.meetingRepository = meetingRepository;
        this.memberRepository = memberRepository;
        this.speakerRepository = speakerRepository;
        this.sponsorRepository = sponsorRepository;
        this.locationRepository = locationRepository;
        this.meetupConfiguration = meetupConfiguration;
        this.teamMateRepository = teamMateRepository;
    }

    @RequestMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("sponsors", sponsorRepository.all());
        final Meeting meeting = meetingRepository.upcomingMeetings().iterator().next(); // TODO : handle cardinality
        model.addAttribute("meeting", meeting);
        speakerRepository.by(meeting.getSpeakerID()).ifPresent(speaker -> model.addAttribute("speaker", speaker)); 
        locationRepository.by(meeting.getLocationID()).ifPresent(location -> model.addAttribute("location", location));
        return "index";
    }

    @RequestMapping(value = "/admin")
    public RedirectView admin() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(meetupConfiguration.authorizeUri());
        return redirectView;
    }

    @RequestMapping(value = "/info")
    public String info(Model model) {
        model.addAttribute("sponsors", sponsorRepository.all());
        return "info";
    }

    @RequestMapping(value = "/meetings")
    public String meetings(Model model) {
        model.addAttribute("sponsors", sponsorRepository.all());
        model.addAttribute("info", meetingInfo);
        model.addAttribute("meetings", meetingRepository.pastMeetings());
        return "meetings";
    }

    @RequestMapping(value = "/members")
    public String members(Model model) {
        model.addAttribute("sponsors", sponsorRepository.all());
        model.addAttribute("members", memberRepository.all());
        return "members";
    }

    @RequestMapping(value = "/speakers")
    public String speakers(Model model) {
        model.addAttribute("sponsors", sponsorRepository.all());
        model.addAttribute("speakers", speakerRepository.all());
        return "speakers";
    }

    @RequestMapping(value = "/association")
    public String association(Model model) {
        model.addAttribute("sponsors", sponsorRepository.all());
        model.addAttribute("teamMates", teamMateRepository.all());
        return "association";
    }
}
