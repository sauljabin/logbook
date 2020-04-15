package logbook.testutil;

import com.github.javafaker.Faker;

import java.util.UUID;

public class Random {

    private static final String FAKER_REGEX = "[a-zA-Z]{5}";

    private static Faker faker = new Faker();

    public static String getRandomString() {
        return faker.regexify(FAKER_REGEX);
    }

    public static int getRandomInt() {
        return faker.number().randomDigit();
    }

    public static double getRandomDouble() {
        return faker.number().randomDouble(3, 0, 10);
    }

    public static long getRandomLong() {
        return faker.number().randomNumber();
    }

    public static boolean getRandomBoolean() {
        return faker.bool().bool();
    }

    public static byte getRandomByte() {
        return (byte) faker.number().randomDigit();
    }

    public static short getRandomShort() {
        return (short) faker.number().randomDigit();
    }

    public static float getRandomFloat() {
        return (float) faker.random().nextDouble();
    }

    public static char getRandomChar() {
        return faker.regexify("[a-zA-Z]{1}").charAt(0);
    }

    public static UUID getRandomUUID() {
        return UUID.randomUUID();
    }
}
