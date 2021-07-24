package utils;

public class DocumentProcessor {

    private final PorterStemmer stemmer;
    private StringBuilder data;

    public DocumentProcessor() {
        stemmer = new PorterStemmer();
        data = new StringBuilder();
    }

    public DocumentProcessor(String data) {
        stemmer = new PorterStemmer();
        this.data = new StringBuilder(data);
    }

    public DocumentProcessor removeSigns() {

        data = new StringBuilder(data.toString().replaceAll("[.\"'!@#$%^&*()\\[\\]]+", ""));
        return this;
    }

    public DocumentProcessor stem() {
        data = new StringBuilder(stemmer.stemWord(data.toString()));
        return this;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
