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
package org.bdxjug.api.meetings;

import java.time.LocalDate;

public class Meeting {

    private final String id;
    private final String name;
    private final LocalDate date;
    private int nbAttendees;

    public Meeting(String id, String name, LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public LocalDate date() {
        return date;
    }

    public int nbAttendees() {
        return nbAttendees;
    }

    public void setAttendance(int nbAttendees) {
        this.nbAttendees = nbAttendees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meeting meeting = (Meeting) o;

        return id.equals(meeting.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
