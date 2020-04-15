package logbook.demo;

import logbook.Logbook;

import java.util.Arrays;

public class App {

    private static Logbook logbook = Logbook.instance(App.class);

    public static void main(String[] args) {
        logbook.info();
        logbook.warn();
        logbook.error();
        logbook.trace();
        logbook.debug();

        logbook.message("Hola Mundo!!")
                .language("español")
                .info();

        logbook.message("Este es un log con excepción")
                .exception(new RuntimeException("Oh Oh!!!"))
                .info();

        logbook.message("Este es un log con excepción y stack trace")
                .exceptionWithStackTrace(new RuntimeException("Oh Oh!!!"))
                .info();

        logbook.message("Este es un log con formato y parámetros, arg1 {}, arg2 {} y arg3 {}", 1, '2', "3")
                .info();

        logbook.message("Este en un error")
                .exception(new RuntimeException(":("))
                .error();

        logbook.message("Se puede recibir arreglos de objetos o listas")
                .add("array", new String[]{"1", "2"})
                .add("list", Arrays.asList("1", "2"))
                .info();

        logbook.message("Todos los valores se encierran en doble comillas")
                .add("int", 1)
                .add("double", 3.14)
                .info();        

        logbook.message("Soporta nulos")
                .add(null, "claveNula")
                .add("valorNulo", null)
                .info();

        logbook.message("Soporta arreglos con nulos")
                .add("array", new Object[]{null, "not null"})
                .info();

        logbook.message("Ignora claves vacías")
                .add("", "clave vacía")
                .info();

        logbook.message("Limpia comillas \"dobles\", 'simples' y salto de\nlínea")
                .info();
    }
}
