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
package org.bdxjug.api.infrastructure.sheet;

import org.bdxjug.api.domain.meetings.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.bdxjug.api.infrastructure.sheet.Sheet.setValue;

@Component
public class SheetLocationRepository implements LocationRepository {

    private static final int ID = 0;
    private static final int NAME = 1;
    private static final int ADDRESS = 2;
    private static final int LAT = 3;
    private static final int LNG = 4;
    private static final int ROOM = 5;
    private static final int VENUE_ID = 5;

    private final Sheet sheet;

    @Autowired
    public SheetLocationRepository(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Optional<Location> by(LocationID locationID) {
        return all().stream().filter(l -> l.getId().equals(locationID)).findFirst();
    }

    @Override
    public List<Location> all() {
        return sheet.readLines(this::toLocation, "Locations");
    }

    private Location toLocation(String[] value) {
        Geo geo = new Geo(Double.parseDouble(value[LAT]), Double.parseDouble(value[LNG]));
        Location location = new Location(new LocationID(value[ID]), value[NAME], value[ADDRESS], geo);
        setValue(value, ROOM, location::setRoom);
        setValue(value, VENUE_ID, s -> location.setVenueID(new VenueID(s)));
        return location;
    }

}
