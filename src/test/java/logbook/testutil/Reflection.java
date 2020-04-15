package logbook.testutil;

import javax.management.ReflectionException;
import java.lang.reflect.Field;

/**
 * Clase utilitaria que permite asignar o extraer un valor de campo privado o público dentro de una objeto.
 */
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

    /**
     * Extrae el valor de un campo privado o público, sin la necesidad de invocar un método get.
     * Buscará el campo en el objeto en cuestion o en sus padres.
     *
     * @param affectedObject Objeto que contiene el campo
     * @param fieldName      Nombre del campo al que se le extraerá el valor
     * @return Valor del campo
     * @throws ReflectionException En caso de no existir el campo
     */
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

    /**
     * Asigna un valor a un campo privado o público de un objeto.
     * El campo será buscado en el objeto o en sus padres.
     *
     * @param affectedObject Objeto que contiene el campo
     * @param fieldName      Nombre del campo a asignar
     * @param newValue       Valor a asignar
     * @throws ReflectionException En caso de no existir el campo
     */
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
