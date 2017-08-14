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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

abstract class GoogleSheetRepository<T> {

    private final GoogleSheetClient client;
    private final String sheetId;

    GoogleSheetRepository(GoogleSheetClient client, String sheetId) {
        this.client = client;
        this.sheetId = sheetId;
    }

    List<T> batchGet(Function<String[], T> constructor, String ranges) {
        List<T> results = new ArrayList<>();
        GoogleSheetClient.SpreadSheet sheet = client.batchGet(sheetId, "ROWS", ranges);
        sheet.valueRanges.stream().findFirst().map(r -> r.values).ifPresent(values -> {
            for (String[] value : values) {
                Optional.ofNullable(constructor.apply(value)).ifPresent(results::add);
            }
        });
        return results;
    }
}
