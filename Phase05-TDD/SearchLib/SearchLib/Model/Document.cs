using System;

namespace SearchLib.Model
{
    public class Document
    {
        private string DocumentPath;
        public Document(string DocumentPath)
        {
            this.DocumentPath = DocumentPath;
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
    }
}
