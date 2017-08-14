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
package org.bdxjug.api.infrastructure.googlesheet;

import org.bdxjug.api.domain.members.Member;
import org.bdxjug.api.domain.members.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GoogleSheetMemberRepository implements MemberRepository {

    private static final String IS_ACTIVE = "1";
    private static final String SHEET_ID = "19Kj1ltJzW0k_Y9PHnQsmyMrPONVIhDyeP-y5W7o8Ons";
    private final GoogleSheetClient client;

    @Autowired
    public GoogleSheetMemberRepository(GoogleSheetClient client) {
        this.client = client;
    }

    @Override
    public List<Member> all() {
        List<Member> members = new ArrayList<>();
        GoogleSheetClient.SpreadSheet sheet = client.batchGet(SHEET_ID, "ROWS", "A2:K");
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
            member.setFirstRegistration(LocalDates.parseDate(firstRegistration));
            member.setEndOfValidity(LocalDates.parseDate(endOfValidity));
            return Optional.of(member);
        }
        return Optional.empty();
    }


}
