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

import java.time.LocalDate;
import org.bdxjug.api.domain.sponsors.Sponsor;
import org.bdxjug.api.domain.sponsors.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.bdxjug.api.infrastructure.sheet.Sheet.parseDate;
import static org.bdxjug.api.infrastructure.sheet.Sheet.setValue;

@Component
public class SheetSponsorRepository implements SponsorRepository {

    private static final int NAME = 1;
    private static final int LOGO_URL = 2;
    private static final int END_OF_VALIDITY = 3;
    private static final int DESCRIPTION = 4;

    private final Sheet sheet;

    @Autowired
    public SheetSponsorRepository(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public List<Sponsor> all() {
        return sheet.readLines(this::toSponsor, "Sponsors").stream()
                .filter(s -> s.getEndOfValidity().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }

    private Sponsor toSponsor(String[] values) {
        Sponsor sponsor = new Sponsor(values[NAME], values[LOGO_URL]);
        setValue(values, END_OF_VALIDITY, value -> sponsor.setEndOfValidity(parseDate(value)));
        setValue(values, DESCRIPTION, sponsor::setDescription);
        return sponsor;
    }
}
