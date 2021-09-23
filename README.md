# logbook

<a href="https://sdkman.io/jdks"><img alt="Java" src="https://img.shields.io/badge/-java-orange?logo=java&logoColor=white"></a>
<a href="https://github.com/sauljabin/logbook"><img alt="GitHub" src="https://badges.pufler.dev/updated/sauljabin/logbook?label=updated"></a>
<a href="https://github.com/sauljabin/logbook/blob/main/LICENSE"><img alt="MIT License" src="https://img.shields.io/github/license/sauljabin/logbook"></a>
<a href="https://github.com/sauljabin/logbook/actions"><img alt="GitHub Actions" src="https://img.shields.io/github/checks-status/sauljabin/logbook/main?label=tests"></a>
<a href="https://app.codecov.io/gh/sauljabin/logbook"><img alt="Codecov" src="https://img.shields.io/codecov/c/github/sauljabin/logbook"></a>

Logging using key value format.

Logbook uses [slf4j](http://www.slf4j.org/) as facade for logging.

### How to import it

Clone into your project:
```shell
git clone https://github.com/sauljabin/logbook.git
```

Include it at `settings.gradle`:
```groovy
include('logbook')
project(":logbook").projectDir = file("logbook/logbook")
```

Add it as a dependency at `build.gradle` (using [logback](http://logback.qos.ch/)):
```groovy
dependencies {
    implementation 'ch.qos.logback:logback-classic:1.2.6'
    implementation project(':logbook')
}
```

### Examples

```java
package logbook.demo;

import logbook.Logbook;
import org.slf4j.Logger;

import java.util.Arrays;

public class Demo {

    private static Logger logger = Logbook.logger(Demo.class);

    public static void main(String[] args) {
        Logbook.instance(logger).message("Hello world!!")
                .language("en")
                .info();

        Logbook.instance(logger).message("Log levels")
                .debug();

        Logbook.instance(logger).message("This is an exception")
                .exception(new RuntimeException("Oh Oh!!!"))
                .error();

        Logbook.instance(logger).message("This is an exception with stack trace")
                .exceptionWithStackTrace(new RuntimeException("Oh Oh!!!"))
                .error();

        Logbook.instance(logger).message("This is a log with parameters, arg1 {}, arg2 {} y arg3 {}", 1, '2', "3")
                .info();

        Logbook.instance(logger).message("Receiving arrays and lists")
                .add("array", new String[]{"1", "2"})
                .add("list", Arrays.asList("1", "2"))
                .info();

        Logbook.instance(logger).message("All values are enclosed in double quotes")
                .add("int", 1)
                .add("double", 3.14)
                .info();

        Logbook.instance(logger).message("Null supports")
                .add(null, "null key")
                .add("null value", null)
                .info();

        Logbook.instance(logger).message("Empty keys will be ignored")
                .add("", "empty key")
                .info();

        Logbook.instance(logger).message("Cleaning \"double\" 'single' quotes and\nline break")
                .info();
    }
}
```

Output:

```
17:57:36 [main] INFO logbook.demo.Demo message="Hello world!!" language="en"
17:57:36 [main] DEBUG logbook.demo.Demo message="Log levels"
17:57:36 [main] ERROR logbook.demo.Demo message="This is an exception" exception="java.lang.RuntimeException: Oh Oh!!!"
17:57:36 [main] ERROR logbook.demo.Demo message="This is an exception with stack trace" exception="java.lang.RuntimeException: Oh Oh!!!"
java.lang.RuntimeException: Oh Oh!!!
        at logbook.demo.Demo.main(Demo.java:24)
17:57:36 [main] INFO logbook.demo.Demo message="This is a log with parameters, arg1 1, arg2 2 y arg3 3"
17:57:36 [main] INFO logbook.demo.Demo message="Receiving arrays and lists" array="[1, 2]" list="[1, 2]"
17:57:36 [main] INFO logbook.demo.Demo message="All values are enclosed in double quotes" int="1" double="3.14"
17:57:36 [main] INFO logbook.demo.Demo message="Null supports" null="null key" nullvalue="null"
17:57:36 [main] INFO logbook.demo.Demo message="Empty keys will be ignored"
17:57:36 [main] INFO logbook.demo.Demo message="Cleaning double single quotes and line break"
```

### Development

Running test:
```shell
./gradlew test
```

Run the demo app:
```shell
./gradlew run
```
