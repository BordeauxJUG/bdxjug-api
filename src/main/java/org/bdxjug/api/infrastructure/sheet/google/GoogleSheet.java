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
package org.bdxjug.api.infrastructure.sheet.google;

import org.bdxjug.api.infrastructure.sheet.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class GoogleSheet implements Sheet {

    private static final String SHEET_ID = "1io9i7-HyejSJYVa8EaZL_uJd5XErvoKzYXkMHkD_VOc";
    private static final String LINE_RANGE = "A2:Z";

    private final GoogleSheetClient client;

    @Autowired
    public GoogleSheet(GoogleSheetClient client) {
        this.client = client;
    }

    @Override
    public <T> List<T> readLines(Function<String[], T> lineMapping, String worksheet) {
        List<T> results = new ArrayList<>();
        String rangeValue = Optional.ofNullable(worksheet).map(w -> rangeWithWorksheet(LINE_RANGE, w)).orElse(LINE_RANGE);
        GoogleSheetClient.SpreadSheet sheet = client.batchGet(SHEET_ID, "ROWS", rangeValue);
        sheet.valueRanges.stream().findFirst().map(r -> r.values).ifPresent(values -> {
            for (String[] value : values) {
                Optional.ofNullable(lineMapping.apply(value)).ifPresent(results::add);
            }
        });
        return results;
    }

    private static String rangeWithWorksheet(String ranges, String worksheet) {
        return "'" + worksheet + "'!" + ranges;
    }

    }
