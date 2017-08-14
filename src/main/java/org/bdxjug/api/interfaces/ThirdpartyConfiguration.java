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

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThirdpartyConfiguration {

    @Value("${bdxjug.google-sheet.key}")
    private String gsKey;
    @Value("${bdxjug.meetup.key}")
    private String meetupKey;

    @Bean
    public GoogleSheetClient googleSheetsClient() {
        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(r -> r.query("key", gsKey))
                .target(GoogleSheetClient.class, "https://sheets.googleapis.com/v4");
    }

    @Bean
    public MeetupClient meetupClient() {
        return Feign.builder()
                .logger(new Slf4jLogger(MeetupClient.class))
                .logLevel(Logger.Level.FULL)
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(r -> r.query("key", meetupKey))
                .target(MeetupClient.class, "https://api.meetup.com");
    }
}
