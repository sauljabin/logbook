package logbook;

import org.junit.jupiter.api.Test;

import static logbook.testutil.Random.getRandomString;
import static org.assertj.core.api.Assertions.assertThat;

class PairTest {

    @Test
    void shouldReturnInvalidWhenKeyIsEmpty() {
        Pair pair = new Pair("", null);

        assertThat(pair.isValid())
                .isFalse();
    }

    @Test
    void shouldReturnValidWhenKeyIsNotEmpty() {
        Pair pair = new Pair(getRandomString(), null);

        assertThat(pair.isValid())
                .isTrue();
    }

    @Test
    void shouldReturnFormatForLogger() {
        String key = getRandomString();
        Pair pair = new Pair(key, null);

        assertThat(pair.getKeyFormat())
                .isEqualTo(String.format("%s=\"{}\"", key));
    }

    @Test
    void shouldReturnStringNullWhenValueIsNull() {
        String key = getRandomString();
        Pair pair = new Pair(key, null);

        assertThat(pair.getStringValues())
                .containsExactly("null");
    }

    @Test
    void shouldReturnStringNullWhenKeyIsNull() {
        Pair pair = new Pair(null, null);

        assertThat(pair.getKeyFormat())
                .isEqualTo("null=\"{}\"");
    }

    @Test
    void shouldReturnDefaultFormatWhenFormatIsNull() {
        Pair pair = new Pair(null, null, null);

        assertThat(pair.getKeyFormat())
                .isEqualTo("null=\"{}\"");
    }

    @Test
    void shouldReturnFormatWhenIsNotNuLL() {
        String key = getRandomString();
        String format = "format {}";
        Pair pair = new Pair(key, format, null);

        assertThat(pair.getKeyFormat())
                .isEqualTo(String.format("%s=\"%s\"", key, format));
    }

    @Test
    void shouldCleanKeyWheItHasSpecialChar() {
        Pair pair = new Pair("valid $& ' \" \nKey", null);

        assertThat(pair.getKeyFormat())
                .isEqualTo("validKey=\"{}\"");
    }

    @Test
    void shouldReturnCleanValueWhenItHasANewLine() {
        Pair pair = new Pair(null, "valid\nValue");

        assertThat(pair.getStringValues())
                .containsExactly("valid Value");
    }

    @Test
    void shouldReturnCleanValueWhenItHasNewQuote() {
        Pair pair = new Pair(null, "valid'Value");

        assertThat(pair.getStringValues())
                .containsExactly("validValue");
    }

    @Test
    void shouldReturnCleanValueWhenItHasDoubleQuote() {
        Pair pair = new Pair(null, "valid\"Value");

        assertThat(pair.getStringValues())
                .containsExactly("validValue");
    }
}
