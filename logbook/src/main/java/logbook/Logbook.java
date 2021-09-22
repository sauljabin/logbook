package logbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


public class Logbook {

    private final Logger logger;
    private final List<Pair> pairs = new ArrayList<>();
    private Throwable exception;

    private Logbook(Logger logger) {
        this.logger = logger;
    }


    public static Logger logger(Class<?> origin) {
        return LoggerFactory.getLogger(origin);
    }


    public static Logbook instance(Class<?> origin) {
        return new Logbook(LoggerFactory.getLogger(origin));
    }

    public static Logbook instance(Logger logger) {
        return new Logbook(logger);
    }


    public Logbook add(String key, Object value) {
        return add(key, null, value);
    }

    private Logbook add(LogbookKey key, Object value) {
        return add(key.toString(), value);
    }

    public Logbook add(String key, String valueFormat, Object... values) {
        pairs.add(new Pair(key, valueFormat, values));
        return this;
    }

    private Logbook add(LogbookKey key, String valueFormat, Object... values) {
        return add(key.toString(), valueFormat, values);
    }

    public Logbook message(String value) {
        return add(LogbookKey.MESSAGE, value);
    }

    public Logbook message(String format, Object... values) {
        return add(LogbookKey.MESSAGE, format, values);
    }

    public Logbook exception(String exception) {
        return add(LogbookKey.EXCEPTION, exception);
    }

    public Logbook exception(Throwable exception) {
        return add(LogbookKey.EXCEPTION, exception);
    }

    public Logbook exceptionWithStackTrace(String message, Throwable exception) {
        this.exception = exception;
        return add(LogbookKey.EXCEPTION, message);
    }

    public Logbook exceptionWithStackTrace(Throwable exception) {
        this.exception = exception;
        return add(LogbookKey.EXCEPTION, exception);
    }

    public Logbook endpoint(String endpoint) {
        return add(LogbookKey.ENDPOINT, endpoint);
    }

    public Logbook service(String service) {
        return add(LogbookKey.SERVICE, service);
    }

    public Logbook name(String name) {
        return add(LogbookKey.NAME, name);
    }

    public Logbook duration(double duration) {
        return add(LogbookKey.DURATION, duration);
    }

    public Logbook duration(long duration) {
        return add(LogbookKey.DURATION, duration);
    }

    public Logbook status(String status) {
        return add(LogbookKey.STATUS, status);
    }

    public Logbook fail() {
        return add(LogbookKey.STATUS, LogbookKey.FAIL);
    }

    public Logbook success() {
        return add(LogbookKey.STATUS, LogbookKey.SUCCESS);
    }

    public Logbook environment(String environment) {
        return add(LogbookKey.ENVIRONMENT, environment);
    }

    public Logbook javaMethod(String javaMethod) {
        return add(LogbookKey.METHOD, javaMethod);
    }

    public Logbook javaMethod(Method javaMethod) {
        return add(LogbookKey.METHOD, javaMethod == null ? null : javaMethod.getName());
    }

    public Logbook javaClass(String javaClass) {
        return add(LogbookKey.CLASS, javaClass);
    }

    public Logbook javaClass(Class<?> javaClass) {
        return add(LogbookKey.CLASS, javaClass == null ? null : javaClass.getName());
    }

    public Logbook javaPackage(String javaPackage) {
        return add(LogbookKey.PACKAGE, javaPackage);
    }

    public Logbook javaPackage(Class<?> javaClass) {
        return add(LogbookKey.PACKAGE, javaClass == null ? null : javaClass.getPackage().getName());
    }

    public Logbook javaPackage(Package javaPackage) {
        return add(LogbookKey.PACKAGE, javaPackage == null ? null : javaPackage.getName());
    }

    public Logbook code(String code) {
        return add(LogbookKey.CODE, code);
    }

    public Logbook track(UUID track) {
        return add(LogbookKey.TRACK, track);
    }

    public Logbook track(String track) {
        return add(LogbookKey.TRACK, track);
    }

    public Logbook request(UUID request) {
        return add(LogbookKey.REQUEST, request);
    }

    public Logbook request(String request) {
        return add(LogbookKey.REQUEST, request);
    }

    public Logbook session(UUID session) {
        return add(LogbookKey.SESSION, session);
    }

    public Logbook session(String session) {
        return add(LogbookKey.SESSION, session);
    }

    public Logbook id(UUID id) {
        return add(LogbookKey.ID, id);
    }

    public Logbook id(String id) {
        return add(LogbookKey.ID, id);
    }

    public Logbook type(String type) {
        return add(LogbookKey.TYPE, type);
    }

    public Logbook value(String value) {
        return add(LogbookKey.VALUE, value);
    }


    public Logbook transaction(UUID transaction) {
        return add(LogbookKey.TRANSACTION, transaction);
    }

    public Logbook transaction(String transaction) {
        return add(LogbookKey.TRANSACTION, transaction);
    }

    public Logbook httpMethod(String httpMethod) {
        return add(LogbookKey.HTTP_METHOD, httpMethod);
    }

    public Logbook httpStatus(String httpStatus) {
        return add(LogbookKey.HTTP_STATUS, httpStatus);
    }

    public Logbook httpStatus(int httpStatus) {
        return add(LogbookKey.HTTP_STATUS, httpStatus);
    }

    public Logbook language(String language) {
        return add(LogbookKey.LANGUAGE, language);
    }

    public Logbook arguments(Object[] arguments) {
        return add(LogbookKey.ARGUMENTS, arguments);
    }

    public Logbook info() {
        logger.info(createStringFormat(), createArgumentsList());
        return this;
    }

    public Logbook debug() {
        logger.debug(createStringFormat(), createArgumentsList());
        return this;
    }

    public Logbook error() {
        logger.error(createStringFormat(), createArgumentsList());
        return this;
    }

    public Logbook trace() {
        logger.trace(createStringFormat(), createArgumentsList());
        return this;
    }

    public Logbook warn() {
        logger.warn(createStringFormat(), createArgumentsList());
        return this;
    }

    private String createStringFormat() {
        return pairs.stream()
                .filter(Pair::isValid)
                .map(Pair::getKeyFormat)
                .collect(joining(" "));
    }

    private Object[] createArgumentsList() {
        List<Object> arguments = pairs.stream()
                .filter(Pair::isValid)
                .flatMap(pair -> pair.getStringValues().stream())
                .collect(toList());

        if (exception != null) {
            arguments.add(exception);
        }

        return arguments.toArray();
    }

    public Logbook day() {
        return add(LogbookKey.DAY, LocalDate.now().getDayOfMonth());
    }

    public Logbook month() {
        return add(LogbookKey.MONTH, LocalDate.now().getMonthValue());
    }

    public Logbook date() {
        return add(LogbookKey.DATE, LocalDate.now());
    }

    public Logbook year() {
        return add(LogbookKey.YEAR, LocalDate.now().getYear());
    }

    public Logbook monthName() {
        return add(LogbookKey.MONTH, LocalDate.now().getMonth());
    }

    public Logbook dayName() {
        return add(LogbookKey.DAY, LocalDate.now().getDayOfWeek());
    }

    public Logbook time() {
        return dateTime(LogbookKey.TIME.toString(), "HH:mm:ss.SSS");
    }

    public Logbook dateTime() {
        return dateTime("yyyy-MM-dd HH:mm:ss.SSS Z");
    }

    public Logbook timeZone() {
        return dateTime(LogbookKey.TIME_ZONE.toString(), "Z");
    }

    public Logbook timeZoneName() {
        return add(LogbookKey.TIME_ZONE, ZonedDateTime.now().getZone());
    }

    public Logbook dateTime(String format) {
        return dateTime(LogbookKey.DATE_TIME.toString(), format);
    }

    public Logbook dateTime(String key, String format) {
        return add(key, ZonedDateTime.now().format(DateTimeFormatter.ofPattern(format)));
    }

    public Logbook time(String format) {
        return dateTime(LogbookKey.TIME.toString(), format);
    }

    public Logbook date(String format) {
        return dateTime(LogbookKey.DATE.toString(), format);
    }


    public Logbook action(String value) {
        return add(LogbookKey.ACTION, value);
    }
}
