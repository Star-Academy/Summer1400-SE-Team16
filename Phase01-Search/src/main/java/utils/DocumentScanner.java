package utils;

import exception.BaseDirectoryInvalidException;

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
        if (!Files.isDirectory(baseDirectory)) throw new BaseDirectoryInvalidException();
        index = new InvertedIndex();
        scanAndIndex();
    }

    private void scanAndIndex() throws IOException {
        Set<Path> documents = Files.walk(baseDirectory).filter(Files::isRegularFile).collect(Collectors.toSet());
        long totalTime = 0;
        for (Path document : documents) {
            String data = Files.readString(document);
            long startTime = System.currentTimeMillis();
            String[] words = new DocumentProcessor(data).toLowerCase().removeSigns().toStemmedSplit();
            for (String word : words) {
                index.addWord(document, word);
            }
            totalTime += System.currentTimeMillis() - startTime;
        }
        System.out.println(totalTime);
    }

    public InvertedIndex getIndex() {
        return index;
    }
}
