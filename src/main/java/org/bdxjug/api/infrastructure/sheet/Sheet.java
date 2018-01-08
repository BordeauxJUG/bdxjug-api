package org.bdxjug.api.infrastructure.sheet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

public interface Sheet {

    <T> List<T> readLines(Function<String[], T> lineMapping, String worksheet);

    static void setValue(String[] value, int index, Consumer<String> setter) {
        if (value.length > index) {
            ofNullable(value[index]).ifPresent(setter);
        }
    }

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, org.bdxjug.api.infrastructure.sheet.google.GoogleSheet.DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return LocalDate.ofEpochDay(0);
        }
    }

    static Boolean parseBoolean(String bool) {
        return "true".equalsIgnoreCase(bool);
    }
}
