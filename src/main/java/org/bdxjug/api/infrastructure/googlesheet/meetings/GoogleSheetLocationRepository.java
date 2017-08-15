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
package org.bdxjug.api.infrastructure.googlesheet.meetings;

import org.bdxjug.api.domain.meetings.*;
import org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository.setValue;

@Component
public class GoogleSheetLocationRepository implements LocationRepository {

    private final GoogleSheetRepository repository;

    @Autowired
    public GoogleSheetLocationRepository(GoogleSheetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Location> all() {
        return repository.batchGet(this::toLocation, "'Locations'!A2:Z");
    }

    private Location toLocation(String[] value) {
        Geo geo = new Geo(Double.parseDouble(value[3]), Double.parseDouble(value[4]));
        Location location = new Location(new LocationID(value[0]), value[1], value[2], geo);
        setValue(value, 5, location::setRoom);
        return location;
    }

}
