using SearchLib.Exceptions;
using SearchLib.Model;
using System.Collections.Generic;
using System.IO;

namespace SearchLib.Utils
{
    class DocumentScanner
    {
        private readonly string BasePath;
        private readonly InvertedIndex Index;

        public DocumentScanner(string basePath)
        {
            BasePath = basePath;
            Index = new InvertedIndex();
            if (IsPathDirectory(basePath) && !IsPathFile(basePath))
            {
                IndexAllFilesInDirectory();
            }
            else if (!IsPathDirectory(basePath) && IsPathFile(basePath))
            {
                IndexFile(basePath);
            }
            else
            {
                throw new SearchException("Invalid Path");
            }
        }

        private void IndexAllFilesInDirectory()
        {
            IEnumerable<string> documentPaths = GetAllFilesInPath();
            foreach (string documentPath in documentPaths)
            {
                IndexFile(documentPath);
            }
        }

        private void IndexFile(string documentPath)
        {
            string[] words = GetProcessedDocumentPathData(documentPath);
            IndexDocumentWords(documentPath, words);
        }

        private IEnumerable<string> GetAllFilesInPath()
        {
            return Directory.EnumerateFiles(BasePath);
        }

        private string[] GetProcessedDocumentPathData(string documentPath)
        {
            string data = GetPathData(documentPath);
            return ProcessDocumentData(data);
        }

        private string GetPathData(string documentPath)
        {
            return File.ReadAllText(documentPath);
        }

        private string[] ProcessDocumentData(string data)
        {
            return new DocumentProcessor(data).GetNormalizedWords();
        }

        private void IndexDocumentWords(string documentPath, string[] words)
        {
            Document document = new Document(documentPath);
            foreach (string word in words)
            {
                Index.AddWord(document, word);
            }
        }

        private bool IsPathDirectory(string basePath)
        {
            return Directory.Exists(basePath);
        }

        private bool IsPathFile(string basePath)
        {
            return File.Exists(basePath);
        }

        public InvertedIndex GetIndex()
        {
            return Index;
        }
    }
}
