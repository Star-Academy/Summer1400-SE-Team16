using SearchLib.Exceptions;
using System;
using System.IO;

namespace SearchLib.Model
{
    public class Document
    {
        private string DocumentPath;
        public Document(string documentPath)
        {
            if (!File.Exists(documentPath))
            {
                throw new SearchException("Invalid Path");
            }
            this.DocumentPath = Path.GetFullPath(documentPath);
        }

        public override bool Equals(object obj)
        {
            return obj is Document document &&
                   DocumentPath == document.DocumentPath;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(DocumentPath);
        }

        public override string ToString()
        {
            return DocumentPath;
        }
    }
}
