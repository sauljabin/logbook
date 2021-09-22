package logbook.testutil;

import java.lang.reflect.Field;

public final class Reflection {

    private Reflection() {
        throw new IllegalStateException("Utility class");
    }

    private static Field getField(Class<?> aClass, String fieldName) throws NoSuchFieldException {
        if (aClass == null) {
            throw new NoSuchFieldException("No such field: " + fieldName);
        }

        try {
            return aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return getField(aClass.getSuperclass(), fieldName);
        }
    }

    public static Object getFieldValue(Object affectedObject, String fieldName) throws NoSuchFieldException {
        if (affectedObject == null) {
            throw new IllegalArgumentException("Null affected object");
        }

        if (fieldName == null) {
            throw new IllegalArgumentException("Null field name");
        }

        Field affectedField = getField(affectedObject.getClass(), fieldName);
        affectedField.setAccessible(true);

        try {
            return affectedField.get(affectedObject);
        } catch (IllegalAccessException e) {
            throw new NoSuchFieldException(e.getMessage());
        }
    }

    public static void setFieldValue(Object affectedObject, String fieldName, Object newValue) throws NoSuchFieldException {
        if (affectedObject == null) {
            throw new IllegalArgumentException("Null affected object");
        }

        if (fieldName == null) {
            throw new IllegalArgumentException("Null field name");
        }

        Field affectedField = getField(affectedObject.getClass(), fieldName);
        affectedField.setAccessible(true);

        try {
            affectedField.set(affectedObject, newValue);
        } catch (IllegalAccessException e) {
            throw new NoSuchFieldException(e.getMessage());
        }
    }

}
