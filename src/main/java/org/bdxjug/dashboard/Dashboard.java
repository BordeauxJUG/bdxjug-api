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
import org.bdxjug.dashboard.meetings.MeetingRepository;
import spark.Spark;

import static java.util.Optional.ofNullable;
import static spark.Spark.*;

public class Dashboard {

    public static void main(String[] args) {
        MeetingRepository meetingRepository = new MeetingRepository();
        Gson gson = new Gson();

        setPort();

        get("/api/meetings", (req, res) -> {
            res.type("application/json");
            return meetingRepository.all();
        }, gson::toJson);
    }

    private static void setPort() {
        ofNullable(System.getenv("PORT")).map(Integer::parseInt).ifPresent(Spark::port);
    }
}
