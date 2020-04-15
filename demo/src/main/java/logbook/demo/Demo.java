package logbook.demo;

import logbook.Logbook;
import org.slf4j.Logger;

import java.util.Arrays;

public class Demo {

    private static Logger logger = Logbook.logger(Demo.class);

    public static void main(String[] args) {
        Logbook.instance(logger).info();
        Logbook.instance(logger).warn();
        Logbook.instance(logger).error();
        Logbook.instance(logger).trace();
        Logbook.instance(logger).debug();

        Logbook.instance(logger).message("Hola Mundo!!")
                .language("español")
                .info();

        Logbook.instance(logger).message("Este es un log con excepción")
                .exception(new RuntimeException("Oh Oh!!!"))
                .info();

        Logbook.instance(logger).message("Este es un log con excepción y stack trace")
                .exceptionWithStackTrace(new RuntimeException("Oh Oh!!!"))
                .info();

        Logbook.instance(logger).message("Este es un log con formato y parámetros, arg1 {}, arg2 {} y arg3 {}", 1, '2', "3")
                .info();

        Logbook.instance(logger).message("Este en un error")
                .exception(new RuntimeException(":("))
                .error();

        Logbook.instance(logger).message("Se puede recibir arreglos de objetos o listas")
                .add("array", new String[]{"1", "2"})
                .add("list", Arrays.asList("1", "2"))
                .info();

        Logbook.instance(logger).message("Todos los valores se encierran en doble comillas")
                .add("int", 1)
                .add("double", 3.14)
                .info();        

        Logbook.instance(logger).message("Soporta nulos")
                .add(null, "claveNula")
                .add("valorNulo", null)
                .info();

        Logbook.instance(logger).message("Soporta arreglos con nulos")
                .add("array", new Object[]{null, "not null"})
                .info();

        Logbook.instance(logger).message("Ignora claves vacías")
                .add("", "clave vacía")
                .info();

        Logbook.instance(logger).message("Limpia comillas \"dobles\", 'simples' y salto de\nlínea")
                .info();

        Logbook logbook1 = Logbook.instance(logger);
        logbook1.message("Por");
        logbook1.add("message2", "partes");
        logbook1.add("message3", "separadas");
        logbook1.info();

        Logbook.instance(logger).message("Multiples veces").info().error();
    }
}
