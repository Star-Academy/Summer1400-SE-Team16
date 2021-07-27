package controller;

import exception.BaseDirectoryInvalidException;
import exception.SearchException;
import model.Document;
import model.SearchQuery;
import utils.DocumentScanner;
import utils.SearchEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class AppController {

    private static final String BASE_DIRECTORY_URI = "SampleEnglishData";

    private static SearchEngine engine;

    public void init() throws BaseDirectoryInvalidException, IOException {
        Path baseDirectory = Path.of(BASE_DIRECTORY_URI);
        DocumentScanner documentScanner = new DocumentScanner(baseDirectory);
        engine = new SearchEngine(documentScanner.getIndex());
    }

    public Set<Document> search(String input) throws SearchException {
        if (engine == null) {
            throw new SearchException("indexes not initialized");
        }
        return getInputResults(input);
    }

    private Set<Document> getInputResults(String input) throws SearchException {
        SearchQuery query = new SearchQuery(input);
        return engine.search(query);
    }

    public void printResults(Set<Document> results) {
        for (Document result : results) {
            System.out.println(result.getDocumentPath().getFileName());
        }
    }

}
