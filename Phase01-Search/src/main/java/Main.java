import exception.BaseDirectoryInvalidException;
import utils.DocumentScanner;
import utils.SearchEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SearchEngine engine;
        try {
            DocumentScanner documentScanner = new DocumentScanner(Path.of("SampleEnglishData"));
            engine = new SearchEngine(documentScanner.getIndex());
        } catch (BaseDirectoryInvalidException | IOException e) {
            e.printStackTrace();
            return;
        }
        String input = getInput(scanner);
        LinkedHashSet<Path> results = getResults(engine, input);
        for (Path result : results) {
            System.out.println(result.getFileName());
        }
    }

    private static String getInput(Scanner scanner) {
        return scanner.nextLine().trim().toLowerCase().replaceAll("\\s+", " ");
    }

    private static LinkedHashSet<Path> getResults(SearchEngine engine, String input) {
        Set<String> couldHaveWords = new HashSet<>();
        Set<String> mustHaveWords = new HashSet<>();
        Set<String> mustNotHaveWords = new HashSet<>();
        parseInput(input, couldHaveWords, mustHaveWords, mustNotHaveWords);
        return engine.search(mustHaveWords.toArray(String[]::new), couldHaveWords.toArray(String[]::new),
                mustNotHaveWords.toArray(String[]::new));
    }

    private static void parseInput(String input, Set<String> couldHaveWords, Set<String> mustHaveWords, Set<String> mustNotHaveWords) {
        Matcher matcher = getMatcher(input, "(?<=\\s|^)(\\+\\S+)(?=\\s|$)");
        while (matcher.find()) {
            couldHaveWords.add(matcher.group(1).substring(1));
        }
        matcher = getMatcher(input, "(?<=\\s|^)(-\\S+)(?=\\s|$)");
        while (matcher.find()) {
            mustNotHaveWords.add(matcher.group(1).substring(1));
        }
        matcher = getMatcher(input, "(?<=\\s|^)([^+-]\\S+)(?=\\s|$)");
        while (matcher.find()) {
            mustHaveWords.add(matcher.group(1));
        }
    }

    private static Matcher getMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }

}
