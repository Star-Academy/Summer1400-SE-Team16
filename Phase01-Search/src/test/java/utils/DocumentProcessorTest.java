package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class DocumentProcessorTest {
    @Test
    void getNormalizedWordsTest() {
        String firstTest  = "HeLLO WorlD!";
        String secondTest = "Hi I a.m A HumAn";
        String thirdTest = "the ExPe-cted tesTiNg is , the, expect, test, is";
        String[] firstTestExpected = {"hello", "world"};
        String[] secondTestExpected = {"hi", "i", "am", "a", "human"};
        String[] thirdTestExpected = {"the", "exp", "cted", "test", "is", "the", "expect", "test", "is"};
        Assertions.assertAll(() -> {
            Assertions.assertArrayEquals(firstTestExpected, new DocumentProcessor(firstTest).getNormalizedWords());
            Assertions.assertArrayEquals(secondTestExpected, new DocumentProcessor(secondTest).getNormalizedWords());
            Assertions.assertArrayEquals(thirdTestExpected, new DocumentProcessor(thirdTest).getNormalizedWords());
        });
    }

    @Test
    void DocumentProcessorConstructorTest() {
        Set<String> firstTest = new HashSet<>();
        firstTest.add("t&h*e");
        firstTest.add("aRt");
        firstTest.add("O@f");
        firstTest.add("Creations");
        firstTest.add("t*he");
        firstTest.add("jo&b");
        firstTest.add("foR#");
        firstTest.add("creators");
        String[] firstTestExpected = {"job", "art", "the", "of", "the", "creator", "creation", "for"};
        Assertions.assertArrayEquals(firstTestExpected, new DocumentProcessor(firstTest).getNormalizedWords());
    }
}
