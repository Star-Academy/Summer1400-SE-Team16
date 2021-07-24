package utils;

public class DocumentProcessor {

    private final PorterStemmer stemmer;
    private String data;

    public DocumentProcessor() {
        stemmer = new PorterStemmer();
    }

    public DocumentProcessor(String data) {
        stemmer = new PorterStemmer();
        this.data = data;
    }

    public DocumentProcessor toLowerCase() {
        data = data.toLowerCase();
        return this;
    }

    public DocumentProcessor removeSigns() {
        data = data.replaceAll("[^a-zA-Z0-9\\s]+", "");
        return this;
    }

    public DocumentProcessor stem() {
        data = stemmer.stemWord(data);
        return this;
    }

    @Override
    public String toString() {
        return data;
    }
}
