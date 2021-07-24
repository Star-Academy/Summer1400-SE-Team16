package utils;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine {
    ArrayList<String> mustHaveWords = new ArrayList<>();
    ArrayList<String> couldHaveWords = new ArrayList<>();
    ArrayList<String> mustNotHaveWords = new ArrayList<>();

    public void run() {
        scanWords();
    }

    private void scanWords() {
        Scanner scanner = new Scanner(System.in);
        String input =  scanner.nextLine();
        Matcher matcher = getMatcher(input, "(\\S+)(?=+)");
        while (matcher.find()) {
            couldHaveWords.add(matcher.group(1));
        }
        matcher = getMatcher(input, "(\\S+)(?=-)");
        while (matcher.find()) {
            mustNotHaveWords.add(matcher.group(1));
        }
        matcher = getMatcher(input, "(\\S+)(?=^(-|+)");
        while (matcher.find()) {
            mustHaveWords.add(matcher.group(1));
        }
    }

    public static Matcher getMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }
}
