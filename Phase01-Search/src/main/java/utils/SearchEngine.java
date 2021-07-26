package utils;

import exception.SearchException;
import model.Document;
import model.SearchQuery;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class SearchEngine {

    private final InvertedIndex index;

    public SearchEngine(InvertedIndex index) {
        this.index = index;
    }

    public LinkedHashSet<Document> search(SearchQuery query) throws SearchException {
        if (query == null) throw new SearchException("corrupted search query");
        LinkedHashSet<Document> resultsSet;
        try {
            resultsSet = getCommonWordsIndexSet(query.getMustHaveWords());
        } catch (SearchException e) {
            resultsSet = new LinkedHashSet<>();
        }
        if (isArrayNotEmpty(query.getCouldHaveWords())) {
            resultsSet = addCouldHaveWords(query, resultsSet);
        } else if (isArrayNotEmpty(query.getMustHaveWords())) {
            removeMustNotHaveWords(query.getMustNotHaveWords(), resultsSet);
        }
        return resultsSet;
    }

    private LinkedHashSet<Document> getCommonWordsIndexSet(String[] resultsWords) throws SearchException {
        String minimumResultsWord = getMinimumResultsWord(resultsWords);
        LinkedHashSet<Document> resultsSet = new LinkedHashSet<>(getWordIndexes(minimumResultsWord));
        for (String resultsWord : resultsWords) {
            if (minimumResultsWord.equals(resultsWord)) continue;
            resultsSet.retainAll(getWordIndexes(resultsWord));
        }
        return resultsSet;
    }

    private String getMinimumResultsWord(String[] resultsWords) throws SearchException {
        if (resultsWords == null || resultsWords.length == 0) throw new SearchException();
        String minWord = null;
        int minLen = Integer.MAX_VALUE;
        for (String word : resultsWords) {
            if (getWordIndexes(word) == null) throw new SearchException();
            if (getWordIndexes(word).size() <= minLen) {
                minLen = getWordIndexes(word).size();
                minWord = word;
            }
        }
        return minWord;
    }

    private LinkedHashSet<Document> addCouldHaveWords(SearchQuery query, LinkedHashSet<Document> resultsSet) {
        if (isArrayNotEmpty(query.getMustHaveWords())) {
            removeMustNotHaveWords(query.getMustNotHaveWords(), resultsSet);
            removeIndexesWithoutWordsOccurrenceInCouldHaveWords(query.getCouldHaveWords(), resultsSet);
        } else {
            resultsSet = getJointWordsIndexSet(query.getCouldHaveWords());
            removeMustNotHaveWords(query.getMustNotHaveWords(), resultsSet);
        }
        return resultsSet;
    }

    private void removeIndexesWithoutWordsOccurrenceInCouldHaveWords(String[] couldHaveWords, Set<Document> resultsSet) {
        for (Iterator<Document> resultsIterator = resultsSet.iterator(); resultsIterator.hasNext(); ) {
            Document wordIndex = resultsIterator.next();
            boolean isFound = false;
            for (String word : couldHaveWords) {
                if (getWordIndexes(word).contains(wordIndex)) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                resultsIterator.remove();
            }
        }
    }

    private LinkedHashSet<Document> getJointWordsIndexSet(String[] resultsWords) {
        LinkedHashSet<Document> jointSet = new LinkedHashSet<>();
        for (String word : resultsWords) {
            if (getWordIndexes(word) == null) continue;
            jointSet.addAll(getWordIndexes(word));
        }
        return jointSet;
    }

    private void removeMustNotHaveWords(String[] mustNotHaveWords, Set<Document> resultsSet) {
        if (isArrayNotEmpty(mustNotHaveWords)) {
            for (String word : mustNotHaveWords) {
                resultsSet.removeIf(wordIndex -> getWordIndexes(word).contains(wordIndex));
            }
        }
    }

    private boolean isArrayNotEmpty(String[] wordsArray) {
        return wordsArray != null && wordsArray.length != 0;
    }

    private Set<Document> getWordIndexes(String minimumResultsWord) {
        return index.getWordIndexes(minimumResultsWord);
    }
}