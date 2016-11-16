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
package org.bdxjug.api.sponsors;

import org.bdxjug.api.interfaces.GoogleSheetAPI;

import java.util.ArrayList;
import java.util.List;

public class SponsorRepository {

    private static final GoogleSheetAPI API = GoogleSheetAPI.api();
    private static final String SHEET_ID = "1hjiS7OwgsNJxziJUKski2T7KGc0cTgfJT7iDpc2zr-I";

    public List<Sponsor> all() {
        List<Sponsor> sponsors = new ArrayList<>();
        GoogleSheetAPI.SpreadSheet sheet = API.batchGet(SHEET_ID, "ROWS", "A2:C");
        sheet.valueRanges.stream().findFirst().map(r -> r.values).ifPresent(values -> {
            for (String[] value : values) {
                Sponsor sponsor = new Sponsor(value[0], value[1], value[2]);
                sponsors.add(sponsor);
            }
        });
        return sponsors;    }
}
