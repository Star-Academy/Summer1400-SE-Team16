using SearchLib.Exceptions;
using System;
using System.Collections.Generic;
using System.IO;

namespace SearchLib.Model
{
    public class Document
    {
        public int ID { get; set; }
        public string Path { get; set; }
        public DateTime LastCrawled { get; set; }
        public HashSet<WordIndex> WordIndexes { get; set; }

        public Document()
        {
        }

        public Document(string path)
        {
            if (!File.Exists(path))
            {
                throw new SearchException("Invalid Path");
            }
            this.Path = System.IO.Path.GetFullPath(path);
            WordIndexes = new HashSet<WordIndex>();
            LastCrawled = DateTime.Now;
        }

        public override bool Equals(object obj)
        {
            return obj is Document document &&
                   Path == document.Path;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(Path);
        }

        public override string ToString()
        {
            return Path;
        }
    }
}
