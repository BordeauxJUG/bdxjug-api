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
package org.bdxjug.api.infrastructure.sheet.xlsx;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bdxjug.api.infrastructure.sheet.Sheet;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
@Profile("test")
public class XlsxSheet implements Sheet {

    private final Workbook workbook;

    public XlsxSheet() {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("database-site.xlsx");) {
            this.workbook = WorkbookFactory.create(inputStream);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public <T> List<T> readLines(Function<String[], T> lineMapping, String sheetName) {
        List<T> results = new ArrayList<>();
        org.apache.poi.ss.usermodel.Sheet worksheet = sheetByName(sheetName).orElseThrow(IllegalStateException::new);
        int nbRows = worksheet.getPhysicalNumberOfRows();
        for (int i=1; i<nbRows; i++) {
            Row row = worksheet.getRow(i);
            int nbCells = row.getPhysicalNumberOfCells();
            String[] values = new String[nbCells];
            for (int j=0; j<nbCells; j++) {
                values[j] = row.getCell(j).toString();
            }
            results.add(lineMapping.apply(values));
        }
        return results;
    }

    private Optional<org.apache.poi.ss.usermodel.Sheet> sheetByName(String sheetName) {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().equals(sheetName)) {
                return Optional.of(sheet);
            }
        }
        return Optional.empty();
    }
}
