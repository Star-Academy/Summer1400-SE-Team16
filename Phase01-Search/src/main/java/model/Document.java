package model;

import lombok.Getter;

import java.nio.file.Path;

@Getter
public class Document implements Comparable<Document> {

    private final int id;
    private final Path documentPath;

    public Document(Path documentPath) {
        this.documentPath = documentPath;
        id = documentPath.hashCode();
    }

    @Override
    public int compareTo(Document o) {
        return Integer.compare(id, o.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Document) {
            return ((Document) obj).id == id;
        }
        return false;
    }
}
