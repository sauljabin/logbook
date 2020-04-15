package logbook;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LogbookVersionTest {

    private LogbookVersion logbookVersion;

    @BeforeEach
    void setUp() throws LogbookException {
        logbookVersion = LogbookVersion.instance();
    }

    @Test
    public void shouldReturnName() {
        assertThat(logbookVersion.name())
                .isEqualTo("logbook");
    }

    @Test
    public void shouldReturnVersion() {
        assertThat(logbookVersion.version())
                .isNotNull();
    }

    @Test
    public void shouldReturnRevision() {
        assertThat(logbookVersion.revision())
                .isNotNull();
    }

    @Test
    public void shouldReturnVendor() {
        assertThat(logbookVersion.vendor())
                .isEqualTo("com.ingeint");
    }

    @Test
    public void shouldReturnCorrectPropertiesCount() {
        assertThat(logbookVersion.attributes())
                .hasSize(4);
    }

    @Test
    public void shouldReturnAllKeyAttributes() {
        assertThat(logbookVersion.attributes().keySet())
                .hasSameElementsAs(Arrays.asList(
                        "lib.version",
                        "lib.name",
                        "lib.revision",
                        "lib.vendor")
                );
    }

}
