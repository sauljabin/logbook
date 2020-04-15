package logbook;

/**
 * Excepción que puede ser lanzada por la clase {@link LogbookVersion} en caso de error.
 *
 * @see LogbookVersion
 */
public class LogbookException extends Exception {
    public LogbookException(Throwable throwable) {
        super(throwable);
    }

    public LogbookException(String message) {
        super(message);
    }
}
