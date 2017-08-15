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
package org.bdxjug.api.infrastructure.googlesheet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

public abstract class GoogleSheetRepository<T> {

    private static final String SHEET_ID = "1io9i7-HyejSJYVa8EaZL_uJd5XErvoKzYXkMHkD_VOc";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final GoogleSheetClient client;

    protected GoogleSheetRepository(GoogleSheetClient client) {
        this.client = client;
    }

    protected List<T> batchGet(Function<String[], T> lineMapping, String ranges) {
        List<T> results = new ArrayList<>();
        GoogleSheetClient.SpreadSheet sheet = client.batchGet(SHEET_ID, "ROWS", ranges);
        sheet.valueRanges.stream().findFirst().map(r -> r.values).ifPresent(values -> {
            for (String[] value : values) {
                Optional.ofNullable(lineMapping.apply(value)).ifPresent(results::add);
            }
        });
        return results;
    }

    protected static void setValue(String[] value, int index, Consumer<String> setter) {
        if (value.length > index) {
            ofNullable(value[index]).ifPresent(setter);
        }
    }

    protected static LocalDate parseDate(String endOfValidity) {
        try {
            return LocalDate.parse(endOfValidity, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return LocalDate.ofEpochDay(0);
        }
    }
}
