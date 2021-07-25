package utils;

public class DocumentProcessor {

    private final PorterStemmer stemmer;
    private String data;

    public DocumentProcessor(String data) {
        stemmer = new PorterStemmer();
        this.data = data;
    }

    public DocumentProcessor(Iterable<? extends CharSequence> data) {
        stemmer = new PorterStemmer();
        this.data = String.join(" ", data);
        if (this.data.equals("")) {
            this.data = null;
        }
    }

    public DocumentProcessor toLowerCase() {
        data = data.toLowerCase();
        return this;
    }

    public DocumentProcessor removeSigns() {
        data = data.replaceAll("[-?]+", " ").replaceAll("[^a-zA-Z0-9\\s]+", "");
        return this;
    }

    public String[] toStemmedSplit() {
        if (data == null) return new String[0];
        String[] dataSplit = data.split("\\s+");
        for (int i = 0; i < dataSplit.length; i++) {
            dataSplit[i] = stemmer.stemWord(dataSplit[i]);
        }
        return dataSplit;
    }
}
