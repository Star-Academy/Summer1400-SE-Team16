package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
