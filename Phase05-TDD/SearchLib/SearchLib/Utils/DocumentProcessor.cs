using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace SearchLib.Utils
{
    public class DocumentProcessor
    {
        private static readonly PorterStemmer Stemmer;

        static DocumentProcessor()
        {
            Stemmer = new PorterStemmer();
        }

        private string Data;
        public DocumentProcessor(ISet<string> data)
        {
            if (data != null && data.Count > 0)
            {
                this.Data = string.Join(" ", data);
            }
        }

        public DocumentProcessor(string data)
        {
            this.Data = data;
        }

        private void ToLowerCase()
        {
            if (Data == null) return;
            Data = Data.ToLower();
        }

        private void RemoveSigns()
        {
            if (Data == null) return;
            Data = Regex.Replace(Data, "[-?]+", " ");
            Data = Regex.Replace(Data, "[^a-zA-Z0-9\\s]+", "");
        }

        private string[] ToStemmedSplit()
        {
            if (Data == null) return new string[0];
            string[] dataSplit = Regex.Split(Data, "\\s+");
            for (int i = 0; i < dataSplit.Length; i++)
            {
                dataSplit[i] = Stemmer.StemWord(dataSplit[i]);
            }
            return dataSplit;
        }

        public string[] GetNormalizedWords()
        {
            ToLowerCase();
            RemoveSigns();
            return ToStemmedSplit();
        }
    }
}
