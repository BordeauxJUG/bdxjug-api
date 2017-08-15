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
package org.bdxjug.api.infrastructure.googlesheet.members;

import org.bdxjug.api.domain.members.Member;
import org.bdxjug.api.domain.members.MemberRepository;
import org.bdxjug.api.infrastructure.googlesheet.GoogleSheetClient;
import org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoogleSheetMemberRepository extends GoogleSheetRepository<Member> implements MemberRepository {

    private static final String IS_ACTIVE = "1";

    @Autowired
    public GoogleSheetMemberRepository(GoogleSheetClient client) {
        super(client);
    }

    @Override
    public List<Member> all() {
        return batchGet(this::toMember, "'Members'!A2:Z");
    }

    private Member toMember(String[] value) {
        final String firstName = value[0];
        final String lastName = value[1];
        final String firstRegistration = value[4];
        final String endOfValidity = value[6];
        final String active = value[10];
        if (IS_ACTIVE.equals(active)) {
            Member member = new Member(firstName, lastName);
            member.setFirstRegistration(parseDate(firstRegistration));
            member.setEndOfValidity(parseDate(endOfValidity));
            return member;
        }
        return null;
    }


}
