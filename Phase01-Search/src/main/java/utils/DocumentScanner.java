package utils;

import exception.BaseDirectoryInvalidException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
        for (Path document : documents) {
            String data = Files.readString(document);
            String[] words = new DocumentProcessor(data).toLowerCase().removeSigns().stem().toString().split("\\s+");
            Arrays.stream(words).forEach(e -> index.addWord(document, e));
        }
    }

    public InvertedIndex getIndex() {
        return index;
    }
}
