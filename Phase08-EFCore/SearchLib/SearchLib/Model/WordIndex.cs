using System.Collections.Generic;

namespace SearchLib.Model
{
    public class WordIndex
    {
        public string Word { get; set; }
        public HashSet<Document> Documents { get; set; }

        public WordIndex()
        {
        }

        public WordIndex(string word)
        {
            Word = word;
            Documents = new HashSet<Document>();
        }
    }
}
