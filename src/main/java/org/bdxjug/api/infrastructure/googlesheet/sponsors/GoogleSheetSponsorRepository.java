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
package org.bdxjug.api.infrastructure.googlesheet.sponsors;

import org.bdxjug.api.domain.sponsors.Sponsor;
import org.bdxjug.api.domain.sponsors.SponsorRepository;
import org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository.parseDate;
import static org.bdxjug.api.infrastructure.googlesheet.GoogleSheetRepository.setValue;

@Component
public class GoogleSheetSponsorRepository implements SponsorRepository {

    private final GoogleSheetRepository repository;

    @Autowired
    public GoogleSheetSponsorRepository(GoogleSheetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Sponsor> all() {
        return repository.batchGet(this::toSponsor, "'Sponsors'!A2:Z");
    }

    private Sponsor toSponsor(String[] values) {
        Sponsor sponsor = new Sponsor(values[1], values[2]);
        setValue(values, 3, value -> sponsor.setEndOfValidity(parseDate(value)));
        setValue(values, 3, sponsor::setDescription);
        return sponsor;
    }
}
