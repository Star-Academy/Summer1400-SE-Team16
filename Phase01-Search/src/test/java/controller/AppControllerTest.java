package controller;

import exception.SearchException;
import lombok.SneakyThrows;
import model.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

class AppControllerTest {
    OutputStream myOut;
    AppController appController;


    @SneakyThrows
    @BeforeEach
    void init() {
        myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
        appController = new AppController();
        appController.init();
    }

    @SneakyThrows
    @Test
    void searchTest() {
        Set<Document> firstActualDocument = appController.search("+faster information -old -we");
        Set<Document> secondActualDocument = appController.search("+faster +information -old -we -have -is -I -how");
        Set<Document> thirdActualDocument = appController.search("information -old -we -have -is -I -how");
        Set<Document> expectedDocument = new HashSet<>();
        expectedDocument.add(new Document(Path.of("SampleEnglishData/59522")));
        Assertions.assertArrayEquals(expectedDocument.toArray(), firstActualDocument.toArray());
        expectedDocument.remove(new Document(Path.of("SampleEnglishData/59522")));
        expectedDocument.add(new Document(Path.of("SampleEnglishData/59628")));
        Assertions.assertArrayEquals(expectedDocument.toArray(), secondActualDocument.toArray());
        Assertions.assertArrayEquals(expectedDocument.toArray(), thirdActualDocument.toArray());
    }

    @SneakyThrows
    @Test
    void printResultsTest() {
        Set<Document> actualDocument = appController.search("+fast information -old -we");
        appController.printResults(actualDocument);
        Assertions.assertEquals("59154" + System.lineSeparator() +
                "58152" + System.lineSeparator() + "59499" + System.lineSeparator(), myOut.toString());
    }

    @SneakyThrows
    @Test
    void afterSearchExceptionTest() {
        Field field = appController.getClass().getDeclaredField("engine");
        field.setAccessible(true);
        field.set(appController, null);
        Assertions.assertThrows(SearchException.class, () -> appController.search("+fantastic"));
    }
}
