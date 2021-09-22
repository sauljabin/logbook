package logbook.demo;

import logbook.Logbook;
import org.slf4j.Logger;

import java.util.Arrays;

public class Demo {

    private static Logger logger = Logbook.logger(Demo.class);

    public static void main(String[] args) {
        Logbook.instance(logger).message("Hello world!!")
                .language("en")
                .info();

        Logbook.instance(logger).message("Log levels")
                .debug();

        Logbook.instance(logger).message("This is an exception")
                .exception(new RuntimeException("Oh Oh!!!"))
                .error();

        Logbook.instance(logger).message("This is an exception with stack trace")
                .exceptionWithStackTrace(new RuntimeException("Oh Oh!!!"))
                .error();

        Logbook.instance(logger).message("This is a log with parameters, arg1 {}, arg2 {} y arg3 {}", 1, '2', "3")
                .info();

        Logbook.instance(logger).message("Receiving arrays and lists")
                .add("array", new String[]{"1", "2"})
                .add("list", Arrays.asList("1", "2"))
                .info();

        Logbook.instance(logger).message("All values are enclosed in double quotes")
                .add("int", 1)
                .add("double", 3.14)
                .info();

        Logbook.instance(logger).message("Null supports")
                .add(null, "null key")
                .add("null value", null)
                .info();

        Logbook.instance(logger).message("Empty keys will be ignored")
                .add("", "empty key")
                .info();

        Logbook.instance(logger).message("Cleaning \"double\" 'single' quotes and\nline break")
                .info();
    }
}
