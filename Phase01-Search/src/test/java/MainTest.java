import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

class MainTest {
    InputStream myIn;
    InputStream STDIN;
    OutputStream myOut;
    PrintStream STDOUT;

    @BeforeEach
    void setUp() {
        STDIN = System.in;
        STDOUT = System.out;
        myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
    }

    void setupInput(String input) {
        myIn = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(myIn);
    }

    @SneakyThrows
    @Test
    void mainTest() {
        setupInput("really in -name -art -are -end +out +how" + System.lineSeparator() + "*exit");
        Main main = new Main();
        Method method = main.getClass().getDeclaredMethod("startSearchEngine", null);
        method.setAccessible(true);
        method.invoke(main, null);
        Assertions.assertEquals("59019" + System.lineSeparator() +
                "59153" + System.lineSeparator() +
                "59345" + System.lineSeparator() +
                "59257" + System.lineSeparator() +
                "59057" + System.lineSeparator(), myOut.toString());
    }
}
