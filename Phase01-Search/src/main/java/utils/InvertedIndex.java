package utils;

import model.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InvertedIndex {

    private final HashMap<String, HashSet<Document>> wordIndexes;

    public InvertedIndex() {
        wordIndexes = new HashMap<>();
    }

    public void addWord(Document document, String word) {
        wordIndexes.putIfAbsent(word, new HashSet<>());
        wordIndexes.get(word).add(document);
    }

    public Set<Document> getWordIndexes(String word) {
        return wordIndexes.get(word);
    }
}