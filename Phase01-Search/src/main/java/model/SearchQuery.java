package model;

import lombok.Getter;
import utils.DocumentProcessor;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class SearchQuery {

    private static final String MUST_HAVE_WORDS_REGEX = "(?<=\\s|^)([^+-]\\S+)(?=\\s|$)";
    private static final String COULD_HAVE_WORDS_REGEX = "(?<=\\s|^)\\+(\\S+)(?=\\s|$)";
    private static final String MUST_NOT_HAVE_WORDS_REGEX = "(?<=\\s|^)-(\\S+)(?=\\s|$)";

    private final String[] mustHaveWords;
    private final String[] couldHaveWords;
    private final String[] mustNotHaveWords;

    public SearchQuery(String input) {
        Set<String> couldHaveWords = getWordsFromInputByRegex(input, COULD_HAVE_WORDS_REGEX);
        Set<String> mustNotHaveWords = getWordsFromInputByRegex(input, MUST_NOT_HAVE_WORDS_REGEX);
        Set<String> mustHaveWords = getWordsFromInputByRegex(input, MUST_HAVE_WORDS_REGEX);
        this.mustHaveWords = new DocumentProcessor(mustHaveWords).toStemmedSplit();
        this.couldHaveWords = new DocumentProcessor(couldHaveWords).toStemmedSplit();
        this.mustNotHaveWords = new DocumentProcessor(mustNotHaveWords).toStemmedSplit();
    }

    private Set<String> getWordsFromInputByRegex(String input, String pattern) {
        Set<String> wordsSet = new HashSet<>();
        Matcher wordsMatcher = getMatcher(input, pattern);
        while (wordsMatcher.find()) {
            wordsSet.add(wordsMatcher.group(1));
        }
        return wordsSet;
    }

    private Matcher getMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }
}