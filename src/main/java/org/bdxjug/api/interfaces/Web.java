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

import java.time.LocalDate;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.bdxjug.api.domain.banner.BannerRepository;
import org.bdxjug.api.domain.meetings.LocationRepository;
import org.bdxjug.api.domain.meetings.Meeting;
import org.bdxjug.api.domain.meetings.MeetingInfo;
import org.bdxjug.api.domain.meetings.MeetingRepository;
import org.bdxjug.api.domain.meetings.SpeakerRepository;
import org.bdxjug.api.domain.members.MemberRepository;
import org.bdxjug.api.domain.sponsors.Sponsor;
import org.bdxjug.api.domain.sponsors.SponsorRepository;
import org.bdxjug.api.domain.team.TeamMate;
import org.bdxjug.api.domain.team.TeamMateRepository;
import org.bdxjug.api.infrastructure.meetup.MeetupConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private final BannerRepository bannerRepository;
    private final MeetupConfiguration meetupConfiguration;

    @Autowired
    public Web(MeetingInfo meetingInfo,
               MeetingRepository meetingRepository,
               MemberRepository memberRepository,
               SpeakerRepository speakerRepository,
               SponsorRepository sponsorRepository,
               LocationRepository locationRepository,
               MeetupConfiguration meetupConfiguration,
               TeamMateRepository teamMateRepository, 
               BannerRepository bannerRepository) {
        this.meetingInfo = meetingInfo;
        this.meetingRepository = meetingRepository;
        this.memberRepository = memberRepository;
        this.speakerRepository = speakerRepository;
        this.sponsorRepository = sponsorRepository;
        this.locationRepository = locationRepository;
        this.meetupConfiguration = meetupConfiguration;
        this.teamMateRepository = teamMateRepository;
        this.bannerRepository = bannerRepository;
    }

    @RequestMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("sponsors", sponsorRepository.all());
        final SortedSet<Meeting> upcomings = meetingRepository.upcomingMeetings();
        // TODO : handle cardinality
        final Meeting meeting = upcomings.isEmpty() ? meetingRepository.pastMeetings().first() : upcomings.iterator().next();
        model.addAttribute("meeting", meeting);
        speakerRepository.by(meeting.getSpeakerID()).ifPresent(speaker -> model.addAttribute("speaker", speaker)); 
        locationRepository.by(meeting.getLocationID()).ifPresent(location -> model.addAttribute("location", location));
        bannerRepository.get().ifPresent(banner -> model.addAttribute("banner", banner));
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

    @GetMapping(value = "/speakers")
    public String speakers(Model model) {
        model.addAttribute("sponsors", sponsorRepository.all());
        model.addAttribute("speakers", speakerRepository.all());
        return "speakers";
    }

    @RequestMapping(value = "/association", method = {RequestMethod.GET, RequestMethod.POST})
    public String association(@ModelAttribute AssociationModel association, Model model) {
        
        model.addAttribute("sponsors", sponsorRepository.all());
        final int currentYear = LocalDate.now().getYear();
        association.setCurrentYear(currentYear);
        int year = association.getYear();
        association.setTeamMates(teamMateRepository.byYear(year));
        association.setYears(IntStream
                .rangeClosed(FIRST_TEAM_YEAR, currentYear)
                .map(i -> FIRST_TEAM_YEAR - i + currentYear)
                .boxed()
                .collect(Collectors.toList()));
                
        return "association";
    }
    private static final int FIRST_TEAM_YEAR = 2010;
    
    public static class AssociationModel {
        private int year;
        private int currentYear;
        private List<Integer> years;
        private SortedSet<TeamMate> teamMates;

        public int getYear() {
            return year == 0 ? currentYear : year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getCurrentYear() {
            return currentYear;
        }

        public void setCurrentYear(int currentYear) {
            this.currentYear = currentYear;
        }

        public List<Integer> getYears() {
            return years;
        }

        public void setYears(List<Integer> years) {
            this.years = years;
        }

     

        public SortedSet<TeamMate> getTeamMates() {
            return teamMates;
        }

        public void setTeamMates(SortedSet<TeamMate> teamMates) {
            this.teamMates = teamMates;
        }
        
    }
}
