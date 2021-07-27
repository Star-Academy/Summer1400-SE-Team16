package utils;

import exception.BaseDirectoryInvalidException;
import model.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentScanner {

    private final Path baseDirectory;
    private final InvertedIndex index;

    public DocumentScanner(Path baseDirectory) throws BaseDirectoryInvalidException, IOException {
        this.baseDirectory = baseDirectory;
        if (!isPathDirectory(baseDirectory)) throw new BaseDirectoryInvalidException();
        index = new InvertedIndex();
        scanAndIndex();
    }

    private void scanAndIndex() throws IOException {
        Set<Path> documentPaths = getAllFilesInPath();
        for (Path documentPath : documentPaths) {
            String[] words = getProcessedDocumentPathData(documentPath);
            indexDocumentWords(documentPath, words);
        }
    }

    private Set<Path> getAllFilesInPath() throws IOException {
        return Files.walk(baseDirectory).filter(Files::isRegularFile).collect(Collectors.toSet());
    }

    private String[] getProcessedDocumentPathData(Path documentPath) throws IOException {
        String data = getPathData(documentPath);
        return processDocumentData(data);
    }

    private String getPathData(Path documentPath) throws IOException {
        return Files.readString(documentPath);
    }

    private String[] processDocumentData(String data) {
        return new DocumentProcessor(data).getNormalizedWords();
    }

    private void indexDocumentWords(Path documentPath, String[] words) {
        Document document = new Document(documentPath);
        for (String word : words) {
            index.addWord(document, word);
        }
    }

    private boolean isPathDirectory(Path baseDirectory) {
        return Files.isDirectory(baseDirectory);
    }

    public InvertedIndex getIndex() {
        return index;
    }
}