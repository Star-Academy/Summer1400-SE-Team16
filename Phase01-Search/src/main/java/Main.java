import exception.BaseDirectoryInvalidException;
import model.Document;
import model.SearchQuery;
import utils.DocumentScanner;
import utils.SearchEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class Main {

    private static final String BASE_DIRECTORY_URI = "SampleEnglishData";

    public static void main(String[] args) {
        startSearchEngine();
    }

    private static void startSearchEngine() {
        SearchEngine engine = initEngineIndexes();
        if (engine == null) return;
        Scanner scanner = new Scanner(System.in);
        String input = getInput(scanner);
        LinkedHashSet<Document> results = getResults(engine, input);
        printResults(results);
    }

    private static void printResults(LinkedHashSet<Document> results) {
        for (Document result : results) {
            System.out.println(result.getDocumentPath().getFileName());
        }
    }

    private static SearchEngine initEngineIndexes() {
        SearchEngine engine;
        try {
            Path baseDirectory = Path.of(BASE_DIRECTORY_URI);
            DocumentScanner documentScanner = new DocumentScanner(baseDirectory);
            engine = new SearchEngine(documentScanner.getIndex());
        } catch (BaseDirectoryInvalidException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return engine;
    }

    private static String getInput(Scanner scanner) {
        return scanner.nextLine();
    }

    private static LinkedHashSet<Document> getResults(SearchEngine engine, String input) {
        SearchQuery query = new SearchQuery(input);
        return engine.search(query);
    }
}
