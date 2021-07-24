package utils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class InvertedIndex {

    private final HashMap<String, Set<Integer>> wordIndexes;
    private final HashMap<Path, Integer> documentIndexes;
    private int lastIndex;

    public InvertedIndex() {
        wordIndexes = new HashMap<>();
        documentIndexes = new HashMap<>();
        lastIndex = -1;
    }

    public void addWord(Path document, String word) {
        int documentId = documentIndexes.getOrDefault(document, -1);
        if (documentId == -1) {
            documentIndexes.put(document, ++lastIndex);
        }
        wordIndexes.computeIfAbsent(word, k -> new TreeSet<>());
        wordIndexes.get(word).add(documentId);
    }
}
