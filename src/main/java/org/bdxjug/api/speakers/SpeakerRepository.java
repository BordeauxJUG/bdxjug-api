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
package org.bdxjug.api.speakers;

import org.bdxjug.api.interfaces.GoogleSheetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
public class SpeakerRepository {

    private static final String SHEET_ID = "1-v3BG67SzcxuiQpb_wkLiwCAG4RGwn2nGn0uAbWG6yg";
    private final GoogleSheetClient client;

    @Autowired
    public SpeakerRepository(GoogleSheetClient client) {
        this.client = client;
    }

    public List<Speaker> all() {
        List<Speaker> speakers = new ArrayList<>();
        GoogleSheetClient.SpreadSheet sheet = client.batchGet(SHEET_ID, "ROWS", "A2:D");
        sheet.valueRanges.stream().findFirst().map(r -> r.values).ifPresent(values -> {
            for (String[] value : values) {
                Speaker speaker = new Speaker(value[0], value[1], value[2]);
                if (value.length > 3) {
                    ofNullable(value[3]).ifPresent(speaker::setTwitter);
                }
                speakers.add(speaker);
            }
        });
        return speakers;
    }

}
