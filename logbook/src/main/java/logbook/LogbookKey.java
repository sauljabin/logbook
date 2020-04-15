package logbook;

enum LogbookKey {
    ACTION("action"),
    PACKAGE("package"),
    CLASS("class"),
    ENDPOINT("endpoint"),
    SERVICE("service"),
    EXCEPTION("exception"),
    HTTP_STATUS("httpStatus"),
    HTTP_METHOD("httpMethod"),
    TRANSACTION("transaction"),
    VALUE("value"),
    TYPE("type"),
    SESSION("session"),
    TRACK("track"),
    REQUEST("request"),
    CODE("code"),
    METHOD("method"),
    ENVIRONMENT("environment"),
    STATUS("status"),
    MESSAGE("message"),
    NAME("name"),
    DURATION("duration"),
    LANGUAGE("language"),
    ARGUMENTS("arguments"),
    ID("id"),
    FAIL("fail"),
    SUCCESS("success"),
    DAY("day"),
    MONTH("month"),
    DATE("date"),
    YEAR("year"),
    TIME("time"),
    DATE_TIME("dateTime"),
    TIME_ZONE("timeZone");

    private final String toStringKey;

    LogbookKey(String toStringKey) {
        this.toStringKey = toStringKey;
    }

    @Override
    public String toString() {
        return toStringKey;
    }
}
