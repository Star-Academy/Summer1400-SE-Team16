package utils;

import model.Document;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class InvertedIndex {

    private final HashMap<String, TreeSet<Document>> wordIndexes;

    public InvertedIndex() {
        wordIndexes = new HashMap<>();
    }

    public void addWord(Document document, String word) {
        wordIndexes.putIfAbsent(word, new TreeSet<>());
        wordIndexes.get(word).add(document);
    }

    public Set<Document> getWordIndexes(String word) {
        return wordIndexes.get(word);
    }
}