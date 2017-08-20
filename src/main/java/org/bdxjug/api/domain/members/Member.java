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
package org.bdxjug.api.domain.members;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Member implements Comparable<Member> {

    private final String firstName;
    private final String lastName;

    private LocalDate endOfValidity;
    private LocalDate firstRegistration;

    @Override
    public int compareTo(Member o) {
        return lastName.compareTo(o.lastName);
    }
}
