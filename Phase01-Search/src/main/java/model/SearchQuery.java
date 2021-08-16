package model;

import lombok.Getter;
import utils.DocumentProcessor;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class SearchQuery {

    private static final String REQUIRED_WORDS_REGEX = "(?<=\\s|^)([^+-]\\S+)(?=\\s|$)";
    private static final String OPTIONAL_WORDS_REGEX = "(?<=\\s|^)\\+(\\S+)(?=\\s|$)";
    private static final String BANNED_WORDS_REGEX = "(?<=\\s|^)-(\\S+)(?=\\s|$)";

    private final String[] requiredWords;
    private final String[] optionalWords;
    private final String[] bannedWords;

    public SearchQuery(String input) {
        this.optionalWords = getStemmedWordsFromInputByRegex(input, OPTIONAL_WORDS_REGEX);
        this.bannedWords = getStemmedWordsFromInputByRegex(input, BANNED_WORDS_REGEX);
        this.requiredWords = getStemmedWordsFromInputByRegex(input, REQUIRED_WORDS_REGEX);
    }

    private String[] getStemmedWordsFromInputByRegex(String input, String pattern) {
        Set<String> wordsSet = new HashSet<>();
        Matcher wordsMatcher = getMatcher(input, pattern);
        while (wordsMatcher.find()) {
            wordsSet.add(wordsMatcher.group(1));
        }
        return new DocumentProcessor(wordsSet).getNormalizedWords();
    }

    private Matcher getMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }
}