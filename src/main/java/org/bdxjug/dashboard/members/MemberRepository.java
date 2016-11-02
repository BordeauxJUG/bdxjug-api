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
package org.bdxjug.dashboard.members;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberRepository {

    private static final GoogleSheetAPI API = GoogleSheetAPI.api();

    public List<Member> all() {
        List<Member> members = new ArrayList<>();
        GoogleSheetAPI.SpreadSheet sheet = API.batchGet("19Kj1ltJzW0k_Y9PHnQsmyMrPONVIhDyeP-y5W7o8Ons", "ROWS", "A2:B");
        Optional<String[][]> values = sheet.valueRanges.stream().findFirst().map(r -> r.values);
        if (values.isPresent()) {
            for (String[] value : values.get()) {
                final String firstName;
                final String lastName;
                if (value.length == 2) {
                    firstName = value[0];
                    lastName = value[1];
                } else {
                    firstName = "";
                    lastName = value[0];
                }
                members.add(new Member(firstName, lastName));
            }
        }
        return members;
    }
}
