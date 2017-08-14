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
    private String description;
    private String registrationLink;
    private Location location;

    public Meeting(String id, String name, LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public void setAttendance(int nbAttendees) {
        this.nbAttendees = nbAttendees;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegistrationLink(String registrationLink) {
        this.registrationLink = registrationLink;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getNbAttendees() {
        return nbAttendees;
    }

    public String getDescription() {
        return description;
    }

    public String getRegistrationLink() {
        return registrationLink;
    }

    public Location getLocation() {
        return location;
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
