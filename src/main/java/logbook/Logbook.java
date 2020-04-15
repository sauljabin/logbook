package logbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Esta clase fue creada con el objetivo de abstraer la complejidad del
 * manejo de logs en formato clave/valor, utilizado para monitoreo de aplicativos.
 * Utiliza el patrón builder para construir un log.
 * <p>
 * Ejemplo de instancia:
 * <p>
 * {@code Logbook logbook = Logbook.instance(App.class);}
 * <p>
 * Ejemplo de uso en formato builder:
 * <p>
 * {@code logbook.message("Hola Mundo!!").language("español").info();}
 * <p>
 * Ejemplo de salida:
 * <p>
 * {@code 08:07:26 [main] INFO App message="Hola Mundo!!" language="español"}
 */
public class Logbook {

    private final Logger logger;
    private final List<Pair> pairs;
    private Throwable exception;

    private Logbook(Logger logger) {
        this.logger = logger;
        pairs = new ArrayList<>();
    }

    private Logbook(Logger logger, List<Pair> pairs, Throwable exception) {
        this.logger = logger;
        this.pairs = pairs;
        this.exception = exception;
    }

    /**
     * Crea una instancia de un objeto Logger.
     *
     * @param origin Clase que escribe los logs
     * @return Objeto Logger
     * @see <a href="https://www.slf4j.org/api/org/slf4j/LoggerFactory.html#getLogger(java.lang.Class)" target="_blank">LoggerFactory.getLogger</a>
     */
    public static Logger logger(Class<?> origin) {
        return LoggerFactory.getLogger(origin);
    }

    /**
     * Esta clase es un wrapper de <a href="https://www.slf4j.org/manual.html" target="_blank">slf4j</a>.
     * Crea una instancia de Logbook.
     *
     * @param origin Clase que escribe los logs
     * @return Objeto builder
     * @see <a href="https://www.slf4j.org/api/org/slf4j/LoggerFactory.html#getLogger(java.lang.Class)" target="_blank">LoggerFactory.getLogger</a>
     */
    public static Logbook instance(Class<?> origin) {
        return new Logbook(LoggerFactory.getLogger(origin));
    }

    /**
     * Esta clase es un wrapper de <a href="https://www.slf4j.org/manual.html" target="_blank">slf4j</a>. Recibe un logger previamente creado.
     * Crea una instancia de Logbook.
     *
     * @param logger Logger previamente creado. Ejemplo: {@code LoggerFactory.getLogger(App.class);}.
     * @return Objeto builder
     * @see <a href="https://www.slf4j.org/api/org/slf4j/LoggerFactory.html#getLogger(java.lang.Class)" target="_blank">LoggerFactory.getLogger</a>
     */
    public static Logbook instance(Logger logger) {
        return new Logbook(logger);
    }

    /**
     * Permite agregar parejas clave y valor al log.
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.add("mensaje", "Hola Mundo!!").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App mensaje="Hola Mundo!!"}
     *
     * @param key   Nombre de la variable del log
     * @param value Valor de la variable en log
     * @return Objeto builder
     */
    public Logbook add(String key, Object value) {
        return add(key, null, value);
    }

    private Logbook add(LogbookKey key, Object value) {
        return add(key.toString(), value);
    }

    /**
     * Permite agregar parejas clave y valor al log a través de un formato string.
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.add("message", "arg1 {}, arg2 {} y arg3 {}", 1, '2', "3").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 10:20:18 [main] INFO App message="arg1 1, arg2 2 y arg3 3"}
     *
     * @param key         Nombre de la variable en el log
     * @param valueFormat Formato para el valor
     * @param values      Objetos a ser formateados
     * @return Objeto builder
     * @see <a href="https://www.slf4j.org/faq.html#logging_performance" target="_blank">Logging Performance</a>
     * @see <a href="https://www.slf4j.org/apidocs/org/slf4j/helpers/MessageFormatter.html" target="_blank">MessageFormat</a>
     */
    public Logbook add(String key, String valueFormat, Object... values) {
        List<Pair> pairsCopy = new ArrayList<>(pairs);
        pairsCopy.add(new Pair(key, valueFormat, values));
        Logbook logbook = new Logbook(logger, pairsCopy, exception);
        return logbook;
    }

    private Logbook add(LogbookKey key, String valueFormat, Object... values) {
        return add(key.toString(), valueFormat, values);
    }

    /**
     * Agrega un mensaje al log con clave "message".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.message("Hola Mundo!!").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App message="Hola Mundo!!"}
     *
     * @param value Valor a agregar
     * @return Objeto builder
     */
    public Logbook message(String value) {
        return add(LogbookKey.MESSAGE, value);
    }

    /**
     * Agrega un mensaje al log con clave "message" a través de un formato.
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.message("arg1 {}, arg2 {} y arg3 {}", 1, '2', "3").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 10:20:18 [main] INFO App message="arg1 1, arg2 2 y arg3 3"}
     *
     * @param format Formato
     * @param values Objetos a ser formateados
     * @return Objeto builder
     * @see #add(String, String, Object...)
     * @see <a href="https://www.slf4j.org/faq.html#logging_performance" target="_blank">Logging Performance</a>
     * @see <a href="https://www.slf4j.org/apidocs/org/slf4j/helpers/MessageFormatter.html" target="_blank">MessageFormat</a>
     */
    public Logbook message(String format, Object... values) {
        return add(LogbookKey.MESSAGE, format, values);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "exception".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.exception("Error!!!").error();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] ERROR App exception="Error!!!"}
     *
     * @param exception Valor de la excepción como cadena
     * @return Objeto builder
     */
    public Logbook exception(String exception) {
        return add(LogbookKey.EXCEPTION, exception);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "exception".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.exception(new RuntimeException("Error!!!")).error();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 10:20:18 [main] ERROR App exception="java.lang.RuntimeException: Error!!!"}
     *
     * @param exception Excepción generada
     * @return Objeto builder
     */
    public Logbook exception(Throwable exception) {
        return add(LogbookKey.EXCEPTION, exception);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "exception".
     * Además imprime el stack trace de la misma.
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.exceptionWithStackTrace("Mensaje personalizado", new RuntimeException("Error!!!")).error();}
     * <p>
     * Ejemplo de salida:
     * {@code
     * 10:20:18 [main] ERROR App exception="Mensaje personalizado"
     * java.lang.RuntimeException: Error!!!
     * at App.main(App.java:24)
     * }
     *
     * @param message   Mensaje personalizado. Valor para la clave "exception"
     * @param exception Excepción generada
     * @return Objeto builder
     * @see <a href="https://www.slf4j.org/faq.html#paramException" target="_blank">Parameterize Exception</a>
     */
    public Logbook exceptionWithStackTrace(String message, Throwable exception) {
        this.exception = exception;
        return add(LogbookKey.EXCEPTION, message);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "exception".
     * Además imprime el stack trace de la misma.
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.exceptionWithStackTrace(new RuntimeException("Error!!!")).error();}
     * <p>
     * Ejemplo de salida:
     * {@code
     * 10:20:18 [main] ERROR App exception="java.lang.RuntimeException: Error!!!"
     * java.lang.RuntimeException: Error!!!
     * at App.main(App.java:24)
     * }
     *
     * @param exception Excepción generada
     * @return Objeto builder
     * @see <a href="https://www.slf4j.org/faq.html#paramException" target="_blank">Parameterize Exception</a>
     */
    public Logbook exceptionWithStackTrace(Throwable exception) {
        this.exception = exception;
        return add(LogbookKey.EXCEPTION, exception);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "endpoint".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.endpoint("/info").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App endpoint="/info"}
     *
     * @param endpoint Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook endpoint(String endpoint) {
        return add(LogbookKey.ENDPOINT, endpoint);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "service".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.service("userService").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App service="userService"}
     *
     * @param service Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook service(String service) {
        return add(LogbookKey.SERVICE, service);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "name".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.name("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App name="valor"}
     *
     * @param name Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook name(String name) {
        return add(LogbookKey.NAME, name);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "duration".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.duration(0.2).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App duration="0.2"}
     *
     * @param duration Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook duration(double duration) {
        return add(LogbookKey.DURATION, duration);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "duration".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.duration(10000).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App duration="10000"}
     *
     * @param duration Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook duration(long duration) {
        return add(LogbookKey.DURATION, duration);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "status".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.status("fail").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App status="fail"}
     *
     * @param status Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook status(String status) {
        return add(LogbookKey.STATUS, status);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "status" y valor "fail".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.fail().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App status="fail"}
     *
     * @return Objeto builder
     */
    public Logbook fail() {
        return add(LogbookKey.STATUS, LogbookKey.FAIL);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "status" y valor "success".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.success().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App status="success"}
     *
     * @return Objeto builder
     */
    public Logbook success() {
        return add(LogbookKey.STATUS, LogbookKey.SUCCESS);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "environment".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.environment("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App environment="valor"}
     *
     * @param environment Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook environment(String environment) {
        return add(LogbookKey.ENVIRONMENT, environment);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "method".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.javaMethod("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App method="valor"}
     *
     * @param javaMethod Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook javaMethod(String javaMethod) {
        return add(LogbookKey.METHOD, javaMethod);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "method".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.javaMethod(Dummy.class.getDeclaredMethod("toString")).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App method="toString"}
     *
     * @param javaMethod Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook javaMethod(Method javaMethod) {
        return add(LogbookKey.METHOD, javaMethod == null ? null : javaMethod.getName());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "class".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.javaClass("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App class="valor"}
     *
     * @param javaClass Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook javaClass(String javaClass) {
        return add(LogbookKey.CLASS, javaClass);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "class".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.javaClass(Dummy.class).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App class="app.Dummy"}
     *
     * @param javaClass Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook javaClass(Class<?> javaClass) {
        return add(LogbookKey.CLASS, javaClass == null ? null : javaClass.getName());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "package".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.javaPackage("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App package="valor"}
     *
     * @param javaPackage Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook javaPackage(String javaPackage) {
        return add(LogbookKey.PACKAGE, javaPackage);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "package".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.javaPackage(Dummy.class).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App package="app"}
     *
     * @param javaClass Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook javaPackage(Class<?> javaClass) {
        return add(LogbookKey.PACKAGE, javaClass == null ? null : javaClass.getPackage().getName());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "package".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.javaPackage(Dummy.class.getPackage()).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App package="app"}
     *
     * @param javaPackage Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook javaPackage(Package javaPackage) {
        return add(LogbookKey.PACKAGE, javaPackage == null ? null : javaPackage.getName());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "code".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.code("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App code="valor"}
     *
     * @param code Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook code(String code) {
        return add(LogbookKey.CODE, code);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "track".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.track(UUID.randomUUID()).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App track="123e4567-e89b-42d3-a456-556642440000"}
     *
     * @param track Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook track(UUID track) {
        return add(LogbookKey.TRACK, track);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "track".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.track("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App track="valor"}
     *
     * @param track Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook track(String track) {
        return add(LogbookKey.TRACK, track);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "request".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.request(UUID.randomUUID()).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App request="123e4567-e89b-42d3-a456-556642440000"}
     *
     * @param request Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook request(UUID request) {
        return add(LogbookKey.REQUEST, request);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "request".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.request("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App request="valor"}
     *
     * @param request Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook request(String request) {
        return add(LogbookKey.REQUEST, request);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "session".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.session(UUID.randomUUID()).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App session="123e4567-e89b-42d3-a456-556642440000"}
     *
     * @param session Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook session(UUID session) {
        return add(LogbookKey.SESSION, session);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "session".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.session("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App session="valor"}
     *
     * @param session Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook session(String session) {
        return add(LogbookKey.SESSION, session);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "id".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.id(UUID.randomUUID()).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App id="123e4567-e89b-42d3-a456-556642440000"}
     *
     * @param id Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook id(UUID id) {
        return add(LogbookKey.ID, id);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "id".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.id("3").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App id="3"}
     *
     * @param id Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook id(String id) {
        return add(LogbookKey.ID, id);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "type".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.type("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App type="valor"}
     *
     * @param type Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook type(String type) {
        return add(LogbookKey.TYPE, type);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "value".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.value("valor").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App value="valor"}
     *
     * @param value Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook value(String value) {
        return add(LogbookKey.VALUE, value);
    }


    /**
     * Agrega un elemento clave/valor al log con clave "transaction".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.transaction(UUID.randomUUID()).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App transaction="123e4567-e89b-42d3-a456-556642440000"}
     *
     * @param transaction Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook transaction(UUID transaction) {
        return add(LogbookKey.TRANSACTION, transaction);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "transaction".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.transaction("123e4567-e89b-42d3-a456-556642440000").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App transaction="123e4567-e89b-42d3-a456-556642440000"}
     *
     * @param transaction Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook transaction(String transaction) {
        return add(LogbookKey.TRANSACTION, transaction);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "httpMethod".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.httpMethod("GET").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App httpMethod="GET"}
     *
     * @param httpMethod Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook httpMethod(String httpMethod) {
        return add(LogbookKey.HTTP_METHOD, httpMethod);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "httpStatus".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.httpStatus("500").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App httpStatus="500"}
     *
     * @param httpStatus Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook httpStatus(String httpStatus) {
        return add(LogbookKey.HTTP_STATUS, httpStatus);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "httpStatus".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.httpStatus(500).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App httpStatus="500"}
     *
     * @param httpStatus Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook httpStatus(int httpStatus) {
        return add(LogbookKey.HTTP_STATUS, httpStatus);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "language".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.language("es").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App language="es"}
     *
     * @param language Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook language(String language) {
        return add(LogbookKey.LANGUAGE, language);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "arguments".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.arguments(new Object[]{"1", "2"}).info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App arguments="[1, 2]"}
     *
     * @param arguments Valor a imprimir en log
     * @return Objeto builder
     */
    public Logbook arguments(Object[] arguments) {
        return add(LogbookKey.ARGUMENTS, arguments);
    }

    /**
     * Logs de mensaje en level INFO.
     * Limpia el mensaje previo, esto permite reutilizar el objeto y construir un mensaje nuevo.
     */
    public void info() {
        logger.info(createStringFormat(), createArgumentsList());
    }

    /**
     * Logs de mensaje en level DEBUG.
     * Limpia el mensaje previo, esto permite reutilizar el objeto y construir un mensaje nuevo.
     */
    public void debug() {
        logger.debug(createStringFormat(), createArgumentsList());
    }

    /**
     * Logs de mensaje en level ERROR.
     * Limpia el mensaje previo, esto permite reutilizar el objeto y construir un mensaje nuevo.
     */
    public void error() {
        logger.error(createStringFormat(), createArgumentsList());
    }

    /**
     * Logs de mensaje en level TRACE.
     * Limpia el mensaje previo, esto permite reutilizar el objeto y construir un mensaje nuevo.
     */
    public void trace() {
        logger.trace(createStringFormat(), createArgumentsList());
    }

    /**
     * Logs de mensaje en level WARN.
     * Limpia el mensaje previo, esto permite reutilizar el objeto y construir un mensaje nuevo.
     */
    public void warn() {
        logger.warn(createStringFormat(), createArgumentsList());
    }

    private String createStringFormat() {
        return pairs.stream()
                .filter(Pair::isValid)
                .map(Pair::getKeyFormat)
                .collect(joining(" "));
    }

    private Object[] createArgumentsList() {
        List<Object> arguments = pairs.stream()
                .filter(Pair::isValid)
                .flatMap(pair -> pair.getStringValues().stream())
                .collect(toList());

        if (exception != null) {
            arguments.add(exception);
        }

        return arguments.toArray();
    }

    /**
     * Agrega un elemento clave/valor al log con clave "day".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.day().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App day="20"}
     *
     * @return Objeto builder
     */
    public Logbook day() {
        return add(LogbookKey.DAY, LocalDate.now().getDayOfMonth());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "month".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.month().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App month="11"}
     *
     * @return Objeto builder
     */
    public Logbook month() {
        return add(LogbookKey.MONTH, LocalDate.now().getMonthValue());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "date".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.date().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App month="2019-12-11"}
     *
     * @return Objeto builder
     */
    public Logbook date() {
        return add(LogbookKey.DATE, LocalDate.now());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "year".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.year().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App year="2019"}
     *
     * @return Objeto builder
     */
    public Logbook year() {
        return add(LogbookKey.YEAR, LocalDate.now().getYear());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "month".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.month().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App month="JULY"}
     *
     * @return Objeto builder
     */
    public Logbook monthName() {
        return add(LogbookKey.MONTH, LocalDate.now().getMonth());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "day".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.day().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App day="MONDAY"}
     *
     * @return Objeto builder
     */
    public Logbook dayName() {
        return add(LogbookKey.DAY, LocalDate.now().getDayOfWeek());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "time".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.time().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App time="22:20:12.523"}
     *
     * @return Objeto builder
     */
    public Logbook time() {
        return dateTime(LogbookKey.TIME.toString(), "HH:mm:ss.SSS");
    }

    /**
     * Agrega un elemento clave/valor al log con clave "dateTime".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.dateTime().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App dateTime="2019-11-20 22:20:12.523 +0000"}
     *
     * @return Objeto builder
     */
    public Logbook dateTime() {
        return dateTime("yyyy-MM-dd HH:mm:ss.SSS Z");
    }

    /**
     * Agrega un elemento clave/valor al log con clave "timeZone".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.timeZone().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App timeZone="-0500"}
     *
     * @return Objeto builder
     */
    public Logbook timeZone() {
        return dateTime(LogbookKey.TIME_ZONE.toString(), "Z");
    }

    /**
     * Agrega un elemento clave/valor al log con clave "timeZone".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.timeZoneName().info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App timeZone="America/Guayaquil"}
     *
     * @return Objeto builder
     */
    public Logbook timeZoneName() {
        return add(LogbookKey.TIME_ZONE, ZonedDateTime.now().getZone());
    }

    /**
     * Agrega un elemento clave/valor al log con clave "dateTime".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.dateTime("yyyy-MM-dd HH:mm:ss.SSS Z").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App dateTime="2019-11-20 22:20:12.523 +0000"}
     *
     * @param format Formato de fecha
     * @return Objeto builder
     */
    public Logbook dateTime(String format) {
        return dateTime(LogbookKey.DATE_TIME.toString(), format);
    }

    /**
     * Agrega un elemento clave/valor al log.
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.dateTime("dateTime", "yyyy-MM-dd HH:mm:ss.SSS Z").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App dateTime="2019-11-20 22:20:12.523 +0000"}
     *
     * @param format Formato de fecha
     * @return Objeto builder
     */
    public Logbook dateTime(String key, String format) {
        return add(key, ZonedDateTime.now().format(DateTimeFormatter.ofPattern(format)));
    }

    /**
     * Agrega un elemento clave/valor al log con clave "time".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.time("HH:mm:ss.SSS").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App time="22:20:12.523"}
     *
     * @param format Formato de fecha
     * @return Objeto builder
     */
    public Logbook time(String format) {
        return dateTime(LogbookKey.TIME.toString(), format);
    }

    /**
     * Agrega un elemento clave/valor al log con clave "date".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.date("yyyy-MM-dd").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App date="2019-11-20"}
     *
     * @param format Formato de fecha
     * @return Objeto builder
     */
    public Logbook date(String format) {
        return dateTime(LogbookKey.DATE.toString(), format);
    }


    /**
     * Agrega un mensaje al log con clave "action".
     * <p>
     * Ejemplo de uso en formato builder:
     * <p>
     * {@code logbook.action("main-key").info();}
     * <p>
     * Ejemplo de salida:
     * <p>
     * {@code 08:07:26 [main] INFO App action="main-key"}
     *
     * @param value Valor a agregar
     * @return Objeto builder
     */
    public Logbook action(String value) {
        return add(LogbookKey.ACTION, value);
    }
}
