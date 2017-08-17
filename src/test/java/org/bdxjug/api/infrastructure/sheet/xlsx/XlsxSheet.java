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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bdxjug.api.infrastructure.sheet.Sheet;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
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
            if (!isRowEmpty(row)) {
                int nbCells = row.getPhysicalNumberOfCells();
                String[] values = new String[nbCells];
                for (int j = 0; j < nbCells; j++) {
                    Cell cell = row.getCell(j);
                    if (isEmpty(cell)) {
                        values[j] = "";
                    } else if (isDate(cell)) {
                        values[j] = getDateValue(cell);
                    } else if (isFormula(cell) || isNumeric(cell)) {
                        values[j] = getNumericValue(cell).toPlainString();
                    } else {
                        values[j] = cell.toString().trim();
                    }
                }
                results.add(lineMapping.apply(values));
            }
        }
        return results;
    }

    private BigDecimal getNumericValue(Cell cell) {
        return new BigDecimal(cell.getNumericCellValue());
    }

    private boolean isNumeric(Cell cell) {
        return cell.getCellType() == Cell.CELL_TYPE_NUMERIC;
    }

    private boolean isFormula(Cell cell) {
        return cell.getCellType() == Cell.CELL_TYPE_FORMULA;
    }

    private boolean isEmpty(Cell cell) {
        return cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK;
    }

    private static String getDateValue(Cell cell) {
        Date dateCellValue = cell.getDateCellValue();
        ZonedDateTime localDate = dateCellValue.toInstant().atZone(ZoneId.systemDefault());
        return DATE_TIME_FORMATTER.format(localDate);
    }

    private static boolean isDate(Cell cell) {
        return (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && cell.toString().contains("-"))
            || (cell.getCellType() == Cell.CELL_TYPE_FORMULA && cell.toString().contains("DATE"));
    }

    private static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                return false;
        }
        return true;
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
