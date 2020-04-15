package logbook;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Clase singleton para obtener en tiempo de ejecución información de la biblioteca
 */
public class LogbookVersion {

    private static final String NAME_PROPERTY = "lib.name";
    private static final String VERSION_PROPERTY = "lib.version";
    private static final String VENDOR_PROPERTY = "lib.vendor";
    private static final String REVISION_PROPERTY = "lib.revision";
    private static final String PATH = "logbook-lib.properties";
    private static final Properties properties = new Properties();
    private static LogbookVersion instance;

    private LogbookVersion() throws LogbookException {
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(PATH));
        } catch (IOException e) {
            throw new LogbookException(e);
        }
    }

    /**
     * @return Instancia singleton
     * @throws LogbookException En caso de no encontrar el archivo de propiedades adjunto al jar ("logbook-lib.properties"). Ver {@link LogbookException}.
     */
    public static synchronized LogbookVersion instance() throws LogbookException {
        if (instance == null) {
            instance = new LogbookVersion();
        }
        return instance;
    }

    /**
     * @return Todas los atributos de la biblioteca: nombre, versión, proveedor, revisión
     */
    public Map<String, String> attributes() {
        return properties.stringPropertyNames()
                .stream()
                .collect(Collectors.toMap(String::toString, properties::getProperty));
    }

    /**
     * @return Nombre de la biblioteca "logbook"
     */
    public String name() {
        return properties.getProperty(NAME_PROPERTY);
    }

    /**
     * @return Versión actual de la biblioteca
     */
    public String version() {
        return properties.getProperty(VERSION_PROPERTY);
    }

    /**
     * @return El proveedor de la biblioteca "com.ingeint"
     */
    public String vendor() {
        return properties.getProperty(VENDOR_PROPERTY);
    }

    /**
     * @return Hash commit en el que se construyó esta versión
     */
    public String revision() {
        return properties.getProperty(REVISION_PROPERTY);
    }
}
