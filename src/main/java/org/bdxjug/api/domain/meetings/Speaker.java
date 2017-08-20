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
package org.bdxjug.api.domain.meetings;

import lombok.Data;

@Data
public class Speaker implements Comparable<Speaker> {

    private final SpeakerID id;
    private final String firstName;
    private final String lastName;

    private String bio;
    private String urlAvatar;
    private String twitter;

    @Override
    public int compareTo(Speaker o) {
        return this.lastName.compareTo(o.lastName);
    }
}
