package controller;

import exception.BaseDirectoryInvalidException;
import exception.SearchException;
import model.Document;
import model.SearchQuery;
import utils.DocumentScanner;
import utils.SearchEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;

public class AppController {

    private static final String BASE_DIRECTORY_URI = "SampleEnglishData";

    private static SearchEngine engine;

    public void init() throws BaseDirectoryInvalidException, IOException {
        Path baseDirectory = Path.of(BASE_DIRECTORY_URI);
        DocumentScanner documentScanner = new DocumentScanner(baseDirectory);
        engine = new SearchEngine(documentScanner.getIndex());
    }

    public void search(String input) throws SearchException {
        if (engine == null) {
            throw new SearchException("indexes not initialized");
        }
        LinkedHashSet<Document> results = getInputResults(input);
        printResults(results);
    }

    private LinkedHashSet<Document> getInputResults(String input) throws SearchException {
        SearchQuery query = new SearchQuery(input);
        return engine.search(query);
    }

    private void printResults(LinkedHashSet<Document> results) {
        for (Document result : results) {
            System.out.println(result.getDocumentPath().getFileName());
        }
    }

}
