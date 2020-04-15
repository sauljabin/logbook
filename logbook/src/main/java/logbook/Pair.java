package logbook;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

class Pair {
    private static final String NULL = "null";
    private static final String KEY_VALUE_FORMAT = "%s=\"%s\"";
    private static final String DEFAULT_CUSTOM_VALUE_FORMAT = "{}";

    private final String key;
    private final Object[] values;
    private final String valueFormat;

    Pair(String key, Object value) {
        this(key, DEFAULT_CUSTOM_VALUE_FORMAT, new Object[]{value});
    }

    Pair(String key, String valueFormat, Object[] values) {
        this.key = key == null ? NULL : key;
        this.valueFormat = valueFormat == null ? DEFAULT_CUSTOM_VALUE_FORMAT : valueFormat;
        this.values = values == null ? new Object[]{} : values;
    }

    boolean isValid() {
        return !key.isEmpty();
    }

    String getKeyFormat() {
        String cleanKey = key.replaceAll("[^a-zA-Z0-9_.]", "");
        return String.format(KEY_VALUE_FORMAT, cleanKey, valueFormat);
    }

    List<String> getStringValues() {
        return Arrays.stream(values)
                .map(this::valueToString)
                .map(this::cleanValue)
                .collect(toList());
    }

    private String valueToString(Object value) {
        if (value == null) {
            return NULL;
        }

        if (value instanceof Object[]) {
            return Arrays.toString((Object[]) value);
        } else if (value instanceof int[]) {
            return Arrays.toString((int[]) value);
        } else if (value instanceof double[]) {
            return Arrays.toString((double[]) value);
        } else if (value instanceof long[]) {
            return Arrays.toString((long[]) value);
        } else if (value instanceof boolean[]) {
            return Arrays.toString((boolean[]) value);
        } else if (value instanceof byte[]) {
            return Arrays.toString((byte[]) value);
        } else if (value instanceof short[]) {
            return Arrays.toString((short[]) value);
        } else if (value instanceof float[]) {
            return Arrays.toString((float[]) value);
        } else if (value instanceof char[]) {
            return Arrays.toString((char[]) value);
        }

        return value.toString();
    }

    private String cleanValue(String value) {
        return value.replace("'", "")
                .replace("\"", "")
                .replace("\n", " ")
                .trim();
    }
}
