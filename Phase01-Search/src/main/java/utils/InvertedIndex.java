package utils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class InvertedIndex {

    private final HashMap<String, Set<Integer>> wordIndexes;
    private final HashMap<Integer, Path> documentIndexes;

    public InvertedIndex() {
        wordIndexes = new HashMap<>();
        documentIndexes = new HashMap<>();
    }

    public void addWord(Path document, String word) {
        documentIndexes.putIfAbsent(document.hashCode(), document);
        wordIndexes.putIfAbsent(word, new TreeSet<>());
        wordIndexes.get(word).add(document.hashCode());
    }

    public void join(InvertedIndex index) {
        documentIndexes.putAll(index.documentIndexes);
        for (Map.Entry<String, Set<Integer>> wordIndex : index.wordIndexes.entrySet()) {
            if (wordIndexes.containsKey(wordIndex.getKey())) {
                wordIndexes.get(wordIndex.getKey()).addAll(wordIndex.getValue());
            } else {
                wordIndexes.put(wordIndex.getKey(), wordIndex.getValue());
            }
        }
    }

    public Set<Integer> getWordIndexes(String word) {
        return wordIndexes.get(word);
    }

    public Path getDocumentByIndex(int index) {
        return documentIndexes.get(index);
    }
}
