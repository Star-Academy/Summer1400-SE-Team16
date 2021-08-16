using SearchLib.Utils;
using System.Collections.Generic;

namespace SearchLib.Model
{
    public class InvertedIndex
    {
        public readonly SearchContext Context;

        private Dictionary<string, WordIndex> WordIndexes;

        public InvertedIndex(SearchContext context)
        {
            WordIndexes = new Dictionary<string, WordIndex>();
            Context = context;
        }

        public void AddWord(Document document, string word)
        {
            WordIndex index = FindIndexByWord(word);
            if (index == null)
            {
                index = new WordIndex(word);
                index.Documents.Add(document);
                WordIndexes[word] = index;
                Context.WordIndex.Add(index);
            }
            else
            {
                index.Documents.Add(document);
            }
        }

        public void Flush()
        {
            Context.SaveChanges();
        }

        public void FlushAndClearCache()
        {
            Flush();
            WordIndexes.Clear();
        }

        public ISet<Document> GetWordIndexes(string word)
        {
            WordIndex index = FindIndexByWord(word);
            return index == null ? null : index.Documents;
        }

        private WordIndex FindIndexByWord(string word)
        {
            if (WordIndexes.ContainsKey(word))
            {
                return WordIndexes[word];
            }
            WordIndex index = Context.WordIndex.Find(word);
            if (index != null)
            {
                Context.Entry(index).Collection(e => e.Documents).Load();
                WordIndexes[word] = index;
            }
            return index;
        }
    }
}
