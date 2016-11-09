package org.bdxjug.api;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface Logger {

    void log(String message);

    default void log(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        log(writer.toString());
    }

    static Logger simple() {
        return System.out::println;
    }
}
