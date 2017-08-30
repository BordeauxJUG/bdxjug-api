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
package org.bdxjug.api.infrastructure.meetup;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

interface MeetupClient {

    interface Admin {
        @RequestLine("POST /{group}/events")
        @Headers("Content-Type: application/json")
        Event announceEvent(@Param("group") String group,
                            @RequestParam("name") String name,
                            @RequestParam("description") String description,
                            @RequestParam("description") long time,
                            @RequestParam("venue_id") String venue_id);
    }

    interface  Reader {
        @RequestLine("GET /{group}/events/{id}")
        Event getEvent(@Param("group") String group, @Param("id") String eventId);

        @RequestLine("GET /{group}/events/{id}/attendance")
        List<Attendee> getEventAttendance(@Param("group") String group, @Param("id") String eventId);
    }

    class Event {
        public String id;
        public String name;
        public long time;
        public int yes_rsvp_count;
        public Venue venue;
        public String link;
        public String description;
    }

    class Venue {
        public String name;
        public double lat;
        public double lon;
        public String address_1;
        public String city;
    }

    class Member {
        public String id;
        public String name;
    }

    class MemberResponse {
        public String response;
    }

    class Attendee {
        public String status;
        public Member member;
        public MemberResponse rsvp;
    }
}
