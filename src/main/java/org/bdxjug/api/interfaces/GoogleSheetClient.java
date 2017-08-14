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
package org.bdxjug.api.interfaces;

import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface GoogleSheetClient {

    @RequestLine("GET /spreadsheets/{sheetId}/values:batchGet?majorDimension={majorDimension}&ranges={ranges}")
    SpreadSheet batchGet(@Param("sheetId") String sheetId, @Param("majorDimension") String majorDimension, @Param("ranges") String ranges);

    class SpreadSheet {
        public String spreadsheetId;
        public List<Range> valueRanges;
    }

    class Range {
        public String range;
        public String[][] values;
    }
}
