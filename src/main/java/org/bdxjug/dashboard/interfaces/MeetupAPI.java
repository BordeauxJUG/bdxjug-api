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
package org.bdxjug.dashboard.interfaces;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.bdxjug.dashboard.Configuration;

import java.util.List;

public interface MeetupAPI {

    static String apiKey() {
        return Configuration.MEETUP_API_KEY.value().orElseThrow(IllegalStateException::new);
    }

    static MeetupAPI api() {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(r -> r.query("key", apiKey()))
                .target(MeetupAPI.class, "https://api.meetup.com");
    }

    @RequestLine("GET /{group}/events?status=past")
    List<Event> pastEvents(@Param("group") String group);

    @RequestLine("GET /{group}/events/{event}/attendance")
    List<Attendee> eventAttendance(@Param("group") String group, @Param("event") String event);

    class Event {
        public String id;
        public String name;
        public long time;
        public int yes_rsvp_count;
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
