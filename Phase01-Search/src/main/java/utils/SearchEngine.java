package utils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class SearchEngine {

    private final InvertedIndex index;

    public SearchEngine(InvertedIndex index) {
        this.index = index;
    }

    public LinkedHashSet<Path> search(String[] mustHaveWords, String[] couldHaveWords, String[] mustNotHaveWords) {
        LinkedHashSet<Integer> mustHaveWordsSet = getWordsIndexSet(mustHaveWords);
        if (couldHaveWords.length != 0) {
            LinkedHashSet<Integer> couldHaveWordsSet = getJointWordsIndexSet(couldHaveWords);
            if (mustHaveWords.length != 0) {
                mustHaveWordsSet.retainAll(couldHaveWordsSet);
            } else {
                mustHaveWordsSet.addAll(couldHaveWordsSet);
            }
        }
        if (mustNotHaveWords.length != 0) {
            LinkedHashSet<Integer> mustNotHaveWordsSet = getJointWordsIndexSet(mustNotHaveWords);
            mustHaveWordsSet.removeAll(mustNotHaveWordsSet);
        }
        return mustHaveWordsSet.stream().map(index::getDocumentByIndex).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private LinkedHashSet<Integer> getJointWordsIndexSet(String[] resultsWords) {
        LinkedHashSet<Integer> jointSet = new LinkedHashSet<>();
        for (String word : resultsWords) {
            if (index.getWordIndexes(word) == null) continue;
            jointSet.addAll(index.getWordIndexes(word));
        }
        return jointSet;
    }

    private LinkedHashSet<Integer> getWordsIndexSet(String[] resultsWords) {
        LinkedHashSet<Integer> resultsSet = new LinkedHashSet<>();
        for (int i = 0; i < resultsWords.length; i++) {
            if (index.getWordIndexes(resultsWords[i]) == null) continue;
            if (i == 0) {
                resultsSet.addAll(index.getWordIndexes(resultsWords[i]));
            } else {
                resultsSet.retainAll(index.getWordIndexes(resultsWords[i]));
            }
        }
        return resultsSet;
    }
}
