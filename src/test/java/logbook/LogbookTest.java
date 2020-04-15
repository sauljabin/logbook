package logbook;

import logbook.testutil.Dummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static logbook.testutil.Random.*;
import static logbook.testutil.Reflection.getFieldValue;
import static logbook.testutil.Reflection.setFieldValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LogbookTest {

    private Logbook logbook;
    private Logger logger;
    private String randomValue;
    private String randomKey;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        logbook = Logbook.instance(LogbookTest.class);
        logger = mock(Logger.class);
        setFieldValue(logbook, "logger", logger);

        randomKey = getRandomString();
        randomValue = getRandomString();
    }

    @Test
    void shouldCreateLogger() {
        Logger logger = Logbook.logger(LogbookTest.class);
        assertThat(logger)
                .isNotNull();
    }

    @Test
    void shouldCreateLogbook() throws NoSuchFieldException {
        logbook = Logbook.instance(LogbookTest.class);
        Object output = getFieldValue(logbook, "logger");
        assertThat(output)
                .isNotNull();

        assertThat(output)
                .isInstanceOf(Logger.class);

        Object pairs = getFieldValue(logbook, "pairs");
        assertThat(pairs)
                .isNotNull();
    }

    @Test
    void shouldSetLogger() throws NoSuchFieldException {
        Logger logger = LoggerFactory.getLogger(LogbookTest.class);
        logbook = Logbook.instance(logger);
        Object output = getFieldValue(logbook, "logger");
        assertThat(output)
                .isNotNull();

        assertThat(output)
                .isSameAs(logger);

        Object pairs = getFieldValue(logbook, "pairs");
        assertThat(pairs)
                .isNotNull();
    }

    @Test
    void shouldInvokeLoggerWithLevelInfo() {
        logbook.info();

        verify(logger)
                .info(anyString(), any(Object[].class));
    }

    @Test
    void shouldCleanAfterInvoke() throws NoSuchFieldException {
        logbook.add(randomKey, randomValue)
                .info();

        List<Pair> pairs = (List<Pair>) getFieldValue(logbook, "pairs");
        assertThat(pairs)
                .hasSize(0);
    }

    @Test
    void shouldCreateNewLogbook() {
        Logbook addLogbook = logbook.add(randomKey, randomValue);

        addLogbook.info();

        assertThat(addLogbook)
                .isNotSameAs(logbook);
    }

    @Test
    void shouldInvokeLoggerWithLevelInfoAndReceiveKeyValueArguments() {
        logbook.add(randomKey, randomValue)
                .info();

        verify(logger)
                .info(randomKey + "=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintAction() {
        logbook.action(randomValue)
                .info();

        verify(logger)
                .info("action=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldAddQuotesWhenStringIsEmpty() {
        logbook.add("message", "")
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{""});
    }

    @Test
    void shouldSkipKeyWhenKeyStringIsEmpty() {
        logbook.add(randomKey, randomValue)
                .add("", "")
                .info();

        verify(logger)
                .info(randomKey + "=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintNullWhenKeyIsNull() {
        String message = getRandomString();
        logbook.add(null, message)
                .info();

        verify(logger)
                .info("null=\"{}\"", new Object[]{message});
    }

    @Test
    void shouldPrintNullWhenValue() {
        logbook.add("message", null)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{"null"});
    }

    @Test
    void shouldAddQuotesWhenStringHaveWhiteSpace() {
        String message = String.format("%s %s", getRandomString(), getRandomString());
        logbook.add("message", message)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{message});
    }

    @Test
    void shouldRemoveNewLine() {
        String randomString1 = getRandomString();
        String randomString2 = getRandomString();
        String message = String.format("\n%s\n%s", randomString1, randomString2);
        logbook.add("message", message)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("%s %s", randomString1, randomString2)});
    }

    @Test
    void shouldTrimText() {
        String message = String.format(" %s ", randomValue);
        logbook.add("message", message)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldCleanKey() {
        logbook.add(" Me's$ sag e 1*\n# ", randomValue)
                .info();

        verify(logger)
                .info("Message1=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldRemoveSingleQuotes() {
        String message = String.format("'%s'", randomValue);
        logbook.add("message", message)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldRemoveDoubleQuotes() {
        String message = String.format("\"%s\"", randomValue);
        logbook.add("message", message)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldRemoveDoubleQuotesAndKeepWrapperQuotes() {
        String randomString1 = getRandomString();
        String randomString2 = getRandomString();
        String randomString3 = getRandomString();
        String message = String.format("\"%s\" %s %s", randomString1, randomString2, randomString3);
        logbook.add("message", message)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("%s %s %s", randomString1, randomString2, randomString3)});
    }

    @Test
    void shouldInvokeLogWithMultiplePairs() {
        String message1 = getRandomString();
        String message2 = getRandomString();
        logbook.add("message1", message1)
                .add("message2", message2)
                .info();

        verify(logger)
                .info("message1=\"{}\" message2=\"{}\"", new Object[]{message1, message2});
    }

    @Test
    void shouldPrintInteger() {
        int value = getRandomInt();

        logbook.add("int", value)
                .info();

        verify(logger)
                .info("int=\"{}\"", new Object[]{Integer.toString(value)});
    }

    @Test
    void shouldPrintFloat() {
        float value = getRandomFloat();

        logbook.add("float", value)
                .info();

        verify(logger)
                .info("float=\"{}\"", new Object[]{Float.toString(value)});
    }

    @Test
    void shouldPrintDouble() {
        double value = getRandomDouble();

        logbook.add("double", value)
                .info();

        verify(logger)
                .info("double=\"{}\"", new Object[]{Double.toString(value)});
    }

    @Test
    void shouldPrintClassType() {
        logbook.add("value", new Dummy())
                .info();

        verify(logger)
                .info("value=\"{}\"", new Object[]{"To String Dummy"});
    }

    @Test
    void shouldPrintMessage() {
        logbook.message(randomValue)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintEndpoint() {
        logbook.endpoint(randomValue)
                .info();

        verify(logger)
                .info("endpoint=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintService() {
        logbook.service(randomValue)
                .info();

        verify(logger)
                .info("service=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintName() {
        logbook.name(randomValue)
                .info();

        verify(logger)
                .info("name=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintDurationDouble() {
        double duration = getRandomDouble();
        logbook.duration(duration)
                .info();

        verify(logger)
                .info("duration=\"{}\"", new Object[]{Double.toString(duration)});
    }

    @Test
    void shouldPrintDurationLong() {
        long duration = getRandomLong();
        logbook.duration(duration)
                .info();

        verify(logger)
                .info("duration=\"{}\"", new Object[]{Long.toString(duration)});

    }

    @Test
    void shouldPrintStatus() {
        logbook.status(randomValue)
                .info();

        verify(logger)
                .info("status=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintFailStatus() {
        logbook.fail()
                .info();

        verify(logger)
                .info("status=\"{}\"", new Object[]{"fail"});
    }

    @Test
    void shouldPrintSuccessStatus() {
        logbook.success()
                .info();

        verify(logger)
                .info("status=\"{}\"", new Object[]{"success"});
    }

    @Test
    void shouldPrintEnvironment() {
        logbook.environment(randomValue)
                .info();

        verify(logger)
                .info("environment=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintMethodFromString() {
        logbook.javaMethod(randomValue)
                .info();

        verify(logger)
                .info("method=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintMethodFromMethod() throws NoSuchMethodException {
        logbook.javaMethod(Dummy.class.getDeclaredMethod("toString"))
                .info();

        verify(logger)
                .info("method=\"{}\"", new Object[]{"toString"});
    }

    @Test
    void shouldPrintNullWhenMethodIsNull() throws NoSuchMethodException {
        logbook.javaMethod((Method) null)
                .info();

        verify(logger)
                .info("method=\"{}\"", new Object[]{"null"});
    }

    @Test
    void shouldPrintClassFromString() {
        logbook.javaClass(randomValue)
                .info();

        verify(logger)
                .info("class=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintClassFromClass() {
        logbook.javaClass(Logbook.class)
                .info();

        verify(logger)
                .info("class=\"{}\"", new Object[]{"logbook.Logbook"});
    }

    @Test
    void shouldPrintNullWhenClassIsNull() {
        logbook.javaClass((Class<?>) null)
                .info();

        verify(logger)
                .info("class=\"{}\"", new Object[]{"null"});
    }

    @Test
    void shouldPrintPackageFromString() {
        logbook.javaPackage(randomValue)
                .info();

        verify(logger)
                .info("package=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintPackageFromClass() {
        logbook.javaPackage(Logbook.class)
                .info();

        verify(logger)
                .info("package=\"{}\"", new Object[]{"logbook"});
    }

    @Test
    void shouldPrintNullWhenClassIsNullInJavaPackageMethod() {
        logbook.javaPackage((Class<?>) null)
                .info();

        verify(logger)
                .info("package=\"{}\"", new Object[]{"null"});
    }


    @Test
    void shouldPrintNullWhenPackageIsNullInJavaPackageMethod() {
        logbook.javaPackage((Package) null)
                .info();

        verify(logger)
                .info("package=\"{}\"", new Object[]{"null"});
    }

    @Test
    void shouldPrintPackageFromPackage() {
        logbook.javaPackage(Logbook.class.getPackage())
                .info();

        verify(logger)
                .info("package=\"{}\"", new Object[]{"logbook"});
    }


    @Test
    void shouldPrintCode() {
        logbook.code(randomValue)
                .info();

        verify(logger)
                .info("code=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintType() {
        logbook.type(randomValue)
                .info();

        verify(logger)
                .info("type=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintValue() {
        logbook.value(randomValue)
                .info();

        verify(logger)
                .info("value=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintTrack() {
        UUID randomUUID = getRandomUUID();
        logbook.track(randomUUID)
                .info();

        verify(logger)
                .info("track=\"{}\"", new Object[]{randomUUID.toString()});
    }

    @Test
    void shouldPrintTrackString() {
        logbook.track(randomValue)
                .info();

        verify(logger)
                .info("track=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintRequest() {
        UUID randomUUID = getRandomUUID();
        logbook.request(randomUUID)
                .info();

        verify(logger)
                .info("request=\"{}\"", new Object[]{randomUUID.toString()});
    }

    @Test
    void shouldPrintRequestString() {
        logbook.request(randomValue)
                .info();

        verify(logger)
                .info("request=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintId() {
        UUID randomUUID = getRandomUUID();
        logbook.id(randomUUID)
                .info();

        verify(logger)
                .info("id=\"{}\"", new Object[]{randomUUID.toString()});
    }

    @Test
    void shouldPrintIdString() {
        logbook.id(randomValue)
                .info();

        verify(logger)
                .info("id=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldTransactionTrack() {
        UUID randomUUID = getRandomUUID();
        logbook.transaction(randomUUID)
                .info();

        verify(logger)
                .info("transaction=\"{}\"", new Object[]{randomUUID.toString()});
    }

    @Test
    void shouldPrintTransactionString() {
        logbook.transaction(randomValue)
                .info();

        verify(logger)
                .info("transaction=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintSession() {
        UUID randomUUID = getRandomUUID();
        logbook.session(randomUUID)
                .info();

        verify(logger)
                .info("session=\"{}\"", new Object[]{randomUUID.toString()});
    }

    @Test
    void shouldPrintLanguage() {
        logbook.language(randomValue)
                .info();

        verify(logger)
                .info("language=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintStringException() {
        logbook.exception(randomValue)
                .info();

        verify(logger)
                .info("exception=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintException() {
        logbook.exception(new RuntimeException(randomValue))
                .info();

        verify(logger)
                .info("exception=\"{}\"", new Object[]{String.format("java.lang.RuntimeException: %s", randomValue)});
    }

    @Test
    void shouldPrintSessionString() {
        logbook.session(randomValue)
                .info();

        verify(logger)
                .info("session=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintHTTPMethodString() {
        logbook.httpMethod(randomValue)
                .info();

        verify(logger)
                .info("httpMethod=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintHTTPStatusString() {
        logbook.httpStatus(randomValue)
                .info();

        verify(logger)
                .info("httpStatus=\"{}\"", new Object[]{randomValue});
    }

    @Test
    void shouldPrintHTTPStatusInt() {
        int status = getRandomInt();
        logbook.httpStatus(status)
                .info();

        verify(logger)
                .info("httpStatus=\"{}\"", new Object[]{Integer.toString(status)});
    }

    @Test
    void shouldPrintDay() {
        logbook.day()
                .info();

        String expectedDay = Integer.toString(LocalDate.now().getDayOfMonth());

        verify(logger)
                .info("day=\"{}\"", new Object[]{expectedDay});
    }

    @Test
    void shouldPrintDayName() {
        logbook.dayName()
                .info();

        String expectedDay = LocalDate.now().getDayOfWeek().toString();

        verify(logger)
                .info("day=\"{}\"", new Object[]{expectedDay});
    }

    @Test
    void shouldPrintMonth() {
        logbook.month()
                .info();

        String expectedMonth = Integer.toString(LocalDate.now().getMonthValue());

        verify(logger)
                .info("month=\"{}\"", new Object[]{expectedMonth});
    }

    @Test
    void shouldPrintMonthName() {
        logbook.monthName()
                .info();

        String expectedMonth = LocalDate.now().getMonth().toString();

        verify(logger)
                .info("month=\"{}\"", new Object[]{expectedMonth});
    }

    @Test
    void shouldPrintYear() {
        logbook.year()
                .info();

        String expectedYear = Integer.toString(LocalDate.now().getYear());

        verify(logger)
                .info("year=\"{}\"", new Object[]{expectedYear});
    }

    @Test
    void shouldPrintDate() {
        logbook.date()
                .info();

        String expectedDate = LocalDate.now().toString();

        verify(logger)
                .info("date=\"{}\"", new Object[]{expectedDate});
    }

    @Test
    void shouldPrintTime() {
        logbook.time()
                .info();

        verify(logger)
                .info(eq("time=\"{}\""), new Object[]{matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}")});
    }

    @Test
    void shouldPrintDateTime() {
        logbook.dateTime()
                .info();

        verify(logger)
                .info(eq("dateTime=\"{}\""), new Object[]{matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3} [-+]\\d{4}")});
    }

    @Test
    void shouldPrintDateTimeFormat() {
        String expectedFormat = "yyyy-MM-dd HH:mm:ss.SSS Z";
        logbook.dateTime(expectedFormat)
                .info();

        verify(logger)
                .info(eq("dateTime=\"{}\""), new Object[]{matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3} [-+]\\d{4}")});
    }

    @Test
    void shouldPrintDateTimeFormatAndKey() {
        String expectedFormat = "yyyy-MM-dd HH:mm:ss.SSS Z";
        String expectedKey = "newDate";
        logbook.dateTime(expectedKey, expectedFormat)
                .info();

        verify(logger)
                .info(eq(expectedKey + "=\"{}\""), new Object[]{matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3} [-+]\\d{4}")});
    }

    @Test
    void shouldPrintTimeFormat() {
        logbook.time("HH:mm:ss.SSS")
                .info();

        verify(logger)
                .info(eq("time=\"{}\""), new Object[]{matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}")});
    }

    @Test
    void shouldPrintDateFormat() {
        logbook.date("yyyy-MM-dd")
                .info();

        verify(logger)
                .info(eq("date=\"{}\""), new Object[]{matches("\\d{4}-\\d{2}-\\d{2}")});
    }

    @Test
    void shouldPrintTimeZone() {
        logbook.timeZone()
                .info();

        verify(logger)
                .info(eq("timeZone=\"{}\""), new Object[]{matches("[-+]\\d{4}")});
    }

    @Test
    void shouldPrintTimeName() {
        logbook.timeZoneName()
                .info();

        String expectedTimeZone = ZonedDateTime.now().getZone().toString();

        verify(logger)
                .info(eq("timeZone=\"{}\""), new Object[]{matches(expectedTimeZone)});
    }

    @Test
    void shouldInvokeDebug() throws NoSuchFieldException {
        logbook.debug();

        verify(logger)
                .debug(anyString(), any(Object[].class));

        List<Pair> pairs = (List<Pair>) getFieldValue(logbook, "pairs");
        assertThat(pairs)
                .hasSize(0);
    }

    @Test
    void shouldInvokeError() throws NoSuchFieldException {
        logbook.error();

        verify(logger)
                .error(anyString(), any(Object[].class));

        List<Pair> pairs = (List<Pair>) getFieldValue(logbook, "pairs");
        assertThat(pairs)
                .hasSize(0);
    }

    @Test
    void shouldInvokeTrace() throws NoSuchFieldException {
        logbook.trace();

        verify(logger)
                .trace(anyString(), any(Object[].class));

        List<Pair> pairs = (List<Pair>) getFieldValue(logbook, "pairs");
        assertThat(pairs)
                .hasSize(0);
    }

    @Test
    void shouldInvokeWarn() throws NoSuchFieldException {
        logbook.warn();

        verify(logger)
                .warn(anyString(), any(Object[].class));

        List<Pair> pairs = (List<Pair>) getFieldValue(logbook, "pairs");
        assertThat(pairs)
                .hasSize(0);
    }

    @Test
    void shouldProcessObjectList() {
        String randomString1 = getRandomString();
        String randomString2 = getRandomString();
        String randomString3 = getRandomString();

        logbook.add("message", Arrays.asList(randomString1, randomString2, randomString3))
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomString1, randomString2, randomString3)});
    }

    @Test
    void shouldProcessObjectArray() {
        String randomString1 = getRandomString();
        String randomString2 = getRandomString();
        String randomString3 = getRandomString();
        String[] objects = {randomString1, randomString2, randomString3};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomString1, randomString2, randomString3)});
    }

    @Test
    void shouldProcessObjectArrayEmpty() {
        String[] objects = {};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{"[]"});
    }

    @Test
    void shouldProcessObjectArrayNull() {
        logbook.add("message", null)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{"null"});
    }

    @Test
    void shouldProcessObjectArrayWithNull() {
        String randomString1 = getRandomString();
        String[] objects = {randomString1, null, null};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, null, null]", randomString1)});
    }

    @Test
    void shouldProcessDummyArray() {
        Dummy[] objects = {new Dummy(), new Dummy()};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{"[To String Dummy, To String Dummy]"});
    }

    @Test
    void shouldProcessIntArray() {
        int randomInt1 = getRandomInt();
        int randomInt2 = getRandomInt();
        int randomInt3 = getRandomInt();
        int[] objects = {randomInt1, randomInt2, randomInt3};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomInt1, randomInt2, randomInt3)});
    }

    @Test
    void shouldProcessDoubleArray() {
        double randomDouble1 = getRandomDouble();
        double randomDouble2 = getRandomDouble();
        double randomDouble3 = getRandomDouble();
        double[] objects = {randomDouble1, randomDouble2, randomDouble3};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomDouble1, randomDouble2, randomDouble3)});
    }

    @Test
    void shouldProcessLongArray() {
        long randomLong1 = getRandomLong();
        long randomLong2 = getRandomLong();
        long randomLong3 = getRandomLong();
        long[] objects = {randomLong1, randomLong2, randomLong3};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomLong1, randomLong2, randomLong3)});
    }

    @Test
    void shouldProcessBooleanArray() {
        boolean randomBoolean1 = getRandomBoolean();
        boolean randomBoolean2 = getRandomBoolean();
        boolean randomBoolean3 = getRandomBoolean();
        boolean[] objects = {randomBoolean1, randomBoolean2, randomBoolean3};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomBoolean1, randomBoolean2, randomBoolean3)});
    }

    @Test
    void shouldProcessByteArray() {
        byte randomByte1 = getRandomByte();
        byte randomByte2 = getRandomByte();
        byte randomByte3 = getRandomByte();
        byte[] objects = {randomByte1, randomByte2, randomByte3};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomByte1, randomByte2, randomByte3)});
    }

    @Test
    void shouldProcessShortArray() {
        short randomShort1 = getRandomShort();
        short randomShort2 = getRandomShort();
        short randomShort3 = getRandomShort();
        short[] objects = {randomShort1, randomShort2, randomShort3};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomShort1, randomShort2, randomShort3)});
    }

    @Test
    void shouldProcessFloatArray() {
        float randomFloat1 = getRandomFloat();
        float randomFloat2 = getRandomFloat();
        float randomFloat3 = getRandomFloat();
        float[] objects = {randomFloat1, randomFloat2, randomFloat3};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomFloat1, randomFloat2, randomFloat3)});
    }

    @Test
    void shouldProcessCharArray() {
        char randomChar1 = getRandomChar();
        char randomChar2 = getRandomChar();
        char randomChar3 = getRandomChar();
        char[] objects = {randomChar1, randomChar2, randomChar3};

        logbook.add("message", objects)
                .info();

        verify(logger)
                .info("message=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomChar1, randomChar2, randomChar3)});
    }

    @Test
    void shouldPrintArguments() {
        String randomString1 = getRandomString();
        String randomString2 = getRandomString();
        String randomString3 = getRandomString();
        Object[] arguments = {randomString1, randomString2, randomString3};

        logbook.arguments(arguments)
                .info();

        verify(logger)
                .info("arguments=\"{}\"", new Object[]{String.format("[%s, %s, %s]", randomString1, randomString2, randomString3)});
    }

    @Test
    void shouldPrintEmptyArguments() {
        Object[] arguments = {};

        logbook.arguments(arguments)
                .info();

        verify(logger)
                .info("arguments=\"{}\"", new Object[]{"[]"});
    }

    @Test
    void shouldPrintLogWithMultipleArgs() {
        String randomString1 = getRandomString();
        String randomString2 = getRandomString();
        logbook.add("message1", "arg1 {}, arg2 {}", randomString1, randomString2)
                .add("message2", "arg1 {}, arg2 {}", randomString1, randomString2)
                .info();

        verify(logger)
                .info("message1=\"arg1 {}, arg2 {}\" message2=\"arg1 {}, arg2 {}\"", randomString1, randomString2, randomString1, randomString2);
    }

    @Test
    void shouldPrintMessageWithMultipleArgs() {
        String randomString1 = getRandomString();
        String randomString2 = getRandomString();
        logbook.message("arg1 {}, arg2 {}", randomString1, randomString2)
                .info();

        verify(logger)
                .info("message=\"arg1 {}, arg2 {}\"", new Object[]{randomString1, randomString2});
    }

    @Test
    void shouldPrintStringExceptionAndStackTrace() {
        RuntimeException exception = new RuntimeException(randomValue);
        logbook.exceptionWithStackTrace(randomValue, exception)
                .info();

        verify(logger)
                .info("exception=\"{}\"", new Object[]{randomValue, exception});
    }

    @Test
    void shouldPrintExceptionAndStackTrace() {
        RuntimeException exception = new RuntimeException(randomValue);
        logbook.exceptionWithStackTrace(exception)
                .info();

        verify(logger)
                .info("exception=\"{}\"", new Object[]{String.format("java.lang.RuntimeException: %s", randomValue), exception});
    }

}
