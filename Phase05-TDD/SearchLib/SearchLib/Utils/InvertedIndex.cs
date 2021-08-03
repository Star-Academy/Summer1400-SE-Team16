using System.Collections.Generic;

namespace SearchLib.Model
{
    public class InvertedIndex
    {
        private readonly Dictionary<string, HashSet<Document>> WordIndexes;

        public InvertedIndex()
        {
            WordIndexes = new Dictionary<string, HashSet<Document>>();
        }

        public void AddWord(Document document, string word)
        {
            if (!WordIndexes.ContainsKey(word))
            {
                WordIndexes[word] = new HashSet<Document>();
            }
            WordIndexes[word].Add(document);
        }

        public ISet<Document> GetWordIndexes(string word)
        {
            return WordIndexes.ContainsKey(word) ? WordIndexes[word] : null;
        }

        public void JoinIndex(InvertedIndex invertedIndex)
        {
            foreach (KeyValuePair<string, HashSet<Document>> wordIndex in invertedIndex.WordIndexes)
            {
                if (WordIndexes.ContainsKey(wordIndex.Key))
                {
                    WordIndexes[wordIndex.Key].UnionWith(wordIndex.Value);
                }
                else
                {
                    WordIndexes[wordIndex.Key] = wordIndex.Value;
                }
            }
        }
    }
}
