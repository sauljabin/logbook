package logbook;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LogbookExceptionTest {

    @Test
    void shouldReturnCorrectMessage() {
        String expectedMessage = "expected message";
        LogbookException logbookException = new LogbookException(expectedMessage);

        assertThat(logbookException.getMessage())
                .isEqualTo(expectedMessage);
    }

    @Test
    void shouldGetCause() {
        RuntimeException expectedCause = new RuntimeException();
        LogbookException logbookException = new LogbookException(expectedCause);

        assertThat(logbookException.getCause())
                .isSameAs(expectedCause);
    }
}
