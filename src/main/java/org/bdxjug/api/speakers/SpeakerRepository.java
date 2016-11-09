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

import org.bdxjug.api.interfaces.GoogleSheetAPI;

import java.util.ArrayList;
import java.util.List;

public class SpeakerRepository {

    private static final GoogleSheetAPI API = GoogleSheetAPI.api();
    private static final String SHEET_ID = "1-v3BG67SzcxuiQpb_wkLiwCAG4RGwn2nGn0uAbWG6yg";

    public List<Speaker> all() {
        List<Speaker> speakers = new ArrayList<>();
        GoogleSheetAPI.SpreadSheet sheet = API.batchGet(SHEET_ID, "ROWS", "A2:C");
        sheet.valueRanges.stream().findFirst().map(r -> r.values).ifPresent(values -> {
            for (String[] value : values) {
                speakers.add(new Speaker(value[0], value[1], value[2]));
            }
        });
        return speakers;
    }

}
