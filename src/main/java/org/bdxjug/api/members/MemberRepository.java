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
package org.bdxjug.api.members;

import org.bdxjug.api.Logger;
import org.bdxjug.api.interfaces.GoogleSheetAPI;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MemberRepository {

    private static final GoogleSheetAPI API = GoogleSheetAPI.api();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String IS_ACTIVE = "1";
    private static final String SHEET_ID = "19Kj1ltJzW0k_Y9PHnQsmyMrPONVIhDyeP-y5W7o8Ons";

    public List<Member> all() {
        List<Member> members = new ArrayList<>();
        GoogleSheetAPI.SpreadSheet sheet = API.batchGet(SHEET_ID, "ROWS", "A2:K");
        sheet.valueRanges.stream().findFirst().map(r -> r.values).ifPresent(values -> {
            for (String[] value : values) {
                toMember(value).ifPresent(members::add);
            }
        });
        return members;
    }

    private Optional<Member> toMember(String[] value) {
        final String firstName = value[0];
        final String lastName = value[1];
        final String firstRegistration = value[4];
        final String endOfValidity = value[6];
        final String active = value[10];
        if (IS_ACTIVE.equals(active)) {
            Member member = new Member(firstName, lastName);
            member.setFirstRegistration(parseDate(firstRegistration));
            member.setEndOfValidity(parseDate(endOfValidity));
            return Optional.of(member);
        }
        return Optional.empty();
    }

    static LocalDate parseDate(String endOfValidity) {
        try {
            return LocalDate.parse(endOfValidity, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            Logger.simple().log(e);
            return LocalDate.ofEpochDay(0);
        }
    }
}
