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
package org.bdxjug.dashboard.members;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import java.util.List;

interface GoogleSheetAPI {

    static String apiKey() {
        return System.getenv("GOOGLE_SHEET_API_KEY");
    }

    static GoogleSheetAPI api() {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(r -> r.query("key", apiKey()))
                .target(GoogleSheetAPI.class, "https://sheets.googleapis.com/v4");
    }

    @RequestLine("GET /spreadsheets/{sheetId}/values:batchGet?majorDimension={majorDimension}&ranges={ranges}")
    SpreadSheet batchGet(@Param("sheetId") String sheetId, @Param("majorDimension") String majorDimension, @Param("ranges") String ranges);

    class SpreadSheet {
        String spreadsheetId;
        List<Range> valueRanges;
    }

    class Range {
        String range;
        String[][] values;
    }
}
