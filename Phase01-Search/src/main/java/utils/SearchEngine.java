package utils;

import exception.SearchException;
import model.Document;
import model.SearchQuery;

import java.util.HashSet;
import java.util.Set;

public class SearchEngine {

    private final InvertedIndex index;

    public SearchEngine(InvertedIndex index) {
        this.index = index;
    }

    public Set<Document> search(SearchQuery query) throws SearchException {
        if (query == null) throw new SearchException("corrupted search query");
        HashSet<Document> resultsSet;
        try {
            resultsSet = getCommonWordsIndexSet(query.getRequiredWords());
        } catch (SearchException e) {
            resultsSet = new HashSet<>();
        }
        if (isArrayNotEmpty(query.getOptionalWords())) {
            resultsSet = addOptionalWordsAndRemoveBannedWords(query, resultsSet);
        } else if (isArrayNotEmpty(query.getRequiredWords())) {
            removeBannedWords(query.getBannedWords(), resultsSet);
        }
        return resultsSet;
    }

    private HashSet<Document> getCommonWordsIndexSet(String[] resultsWords) throws SearchException {
        String minimumResultsWord = getMinimumResultsWord(resultsWords);
        HashSet<Document> resultsSet = new HashSet<>(getWordIndexes(minimumResultsWord));
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

    private HashSet<Document> addOptionalWordsAndRemoveBannedWords(SearchQuery query, HashSet<Document> resultsSet) {
        if (isArrayNotEmpty(query.getRequiredWords())) {
            removeBannedWords(query.getBannedWords(), resultsSet);
            removeIndexesWithoutOptionalWords(query, resultsSet);
        } else {
            resultsSet = getJointWordsIndexSet(query.getOptionalWords());
            removeBannedWords(query.getBannedWords(), resultsSet);
        }
        return resultsSet;
    }

    private void removeIndexesWithoutOptionalWords(SearchQuery query, HashSet<Document> resultsSet) {
        resultsSet.removeIf(wordIndex -> !isIndexFoundInOptionalWordsIndexes(query, wordIndex));
    }

    private boolean isIndexFoundInOptionalWordsIndexes(SearchQuery query, Document wordIndex) {
        boolean isFound = false;
        for (String word : query.getOptionalWords()) {
            if (getWordIndexes(word).contains(wordIndex)) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    private HashSet<Document> getJointWordsIndexSet(String[] resultsWords) {
        HashSet<Document> jointSet = new HashSet<>();
        for (String word : resultsWords) {
            if (getWordIndexes(word) == null) continue;
            jointSet.addAll(getWordIndexes(word));
        }
        return jointSet;
    }

    private void removeBannedWords(String[] bannedWords, Set<Document> resultsSet) {
        if (!isArrayNotEmpty(bannedWords)) return;
        for (String word : bannedWords) {
            resultsSet.removeIf(wordIndex -> getWordIndexes(word).contains(wordIndex));
        }
    }

    private boolean isArrayNotEmpty(String[] wordsArray) {
        return wordsArray != null && wordsArray.length != 0;
    }

    private Set<Document> getWordIndexes(String minimumResultsWord) {
        return index.getWordIndexes(minimumResultsWord);
    }
}