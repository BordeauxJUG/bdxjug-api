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

public class Location {

    private final LocationID id;
    private final String name;
    private final String address;
    private String room;
    private final Geo geo;

    public Location(LocationID id, String name, String address, Geo geo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.geo = geo;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocationID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }


    public String getRoom() {
        return room;
    }

    public Geo getGeo() {
        return geo;
    }
}
