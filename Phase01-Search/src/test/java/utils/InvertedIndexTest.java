package utils;

import model.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

class InvertedIndexTest {
    InvertedIndex invertedIndex;
    
    @BeforeEach
    void init() {
        invertedIndex = new InvertedIndex();
    }

    @Test
    void addWordTest() {
        invertedIndex.addWord(new Document(Path.of("SampleEnglishData/57110")), "have");
        invertedIndex.addWord(new Document(Path.of("SampleEnglishData/58043")), "have");
        invertedIndex.addWord(new Document(Path.of("SampleEnglishData/58043")), "same");
        Set<Document> firstExpectedDocuments = new HashSet<>();
        Set<Document> secondExpectedDocuments = new HashSet<>();
        firstExpectedDocuments.add(new Document(Path.of("SampleEnglishData/57110")));
        firstExpectedDocuments.add(new Document(Path.of("SampleEnglishData/58043")));
        secondExpectedDocuments.add(new Document(Path.of("SampleEnglishData/58043")));
        Assertions.assertAll(() -> {
            Assertions.assertArrayEquals(firstExpectedDocuments.toArray(), invertedIndex.getWordIndexes("have").toArray());
            Assertions.assertArrayEquals(secondExpectedDocuments.toArray(), invertedIndex.getWordIndexes("same").toArray());
        });
    }
}
