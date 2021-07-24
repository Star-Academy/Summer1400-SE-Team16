package utils;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class SearchEngine {

    private final InvertedIndex index;

    public SearchEngine(InvertedIndex index) {
        this.index = index;
    }

    public LinkedHashSet<Path> search(String[] mustHaveWords, String[] couldHaveWords, String[] mustNotHaveWords) {
        LinkedHashSet<Integer> mustHaveWordsSet = getCommonWordsIndexSet(mustHaveWords);
        if (couldHaveWords.length != 0) {
            if (mustHaveWords.length != 0) {
                removeMustNotHaves(mustNotHaveWords, mustHaveWordsSet);
                removeWordsWithoutOccurrences(couldHaveWords, mustHaveWordsSet);
            } else {
                mustHaveWordsSet = getJointWordsIndexSet(couldHaveWords);
                removeMustNotHaves(mustNotHaveWords, mustHaveWordsSet);
            }
        } else if (mustHaveWords.length != 0) {
            removeMustNotHaves(mustNotHaveWords, mustHaveWordsSet);
        }
        return mustHaveWordsSet.stream().map(index::getDocumentByIndex).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void removeWordsWithoutOccurrences(String[] couldHaveWords, LinkedHashSet<Integer> mustHaveWordsSet) {
        for (Iterator<Integer> resultsIterator = mustHaveWordsSet.iterator(); resultsIterator.hasNext(); ) {
            Integer wordIndex = resultsIterator.next();
            boolean isFound = false;
            for (String word : couldHaveWords) {
                if (index.getWordIndexes(word).contains(wordIndex)) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                resultsIterator.remove();
            }
        }
    }

    private void removeMustNotHaves(String[] mustNotHaveWords, LinkedHashSet<Integer> mustHaveWordsSet) {
        if (mustNotHaveWords.length != 0) {
            for (String word : mustNotHaveWords) {
                mustHaveWordsSet.removeIf(wordIndex -> index.getWordIndexes(word).contains(wordIndex));
            }
        }
    }

    private LinkedHashSet<Integer> getJointWordsIndexSet(String[] resultsWords) {
        LinkedHashSet<Integer> jointSet = new LinkedHashSet<>();
        for (String word : resultsWords) {
            if (index.getWordIndexes(word) == null) continue;
            jointSet.addAll(index.getWordIndexes(word));
        }
        return jointSet;
    }

    private LinkedHashSet<Integer> getCommonWordsIndexSet(String[] resultsWords) {
        int minLen = Integer.MAX_VALUE;
        String minWord = null;
        for (String word : resultsWords) {
            if (index.getWordIndexes(word) == null) return new LinkedHashSet<>();
            if (index.getWordIndexes(word).size() < minLen) {
                minLen = index.getWordIndexes(word).size();
                minWord = word;
            }
        }
        if (minWord == null) return new LinkedHashSet<>();
        LinkedHashSet<Integer> resultsSet = new LinkedHashSet<>(index.getWordIndexes(minWord));
        for (String resultsWord : resultsWords) {
            if (minWord.equals(resultsWord)) continue;
            resultsSet.retainAll(index.getWordIndexes(resultsWord));
        }
        return resultsSet;
    }
}
