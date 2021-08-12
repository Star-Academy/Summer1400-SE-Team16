using SearchLib.Exceptions;
using SearchLib.Model;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace SearchLib.Utils
{
    class DocumentScanner
    {
        private readonly string BasePath;
        private readonly InvertedIndex Index;
        private readonly SearchContext Context;

        public DocumentScanner(InvertedIndex index, string basePath)
        {
            BasePath = basePath;
            Index = index;
            Context = index.Context;
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
            Index.FlushAndClearCache();
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
            Document DBDocument = FindDocumentByPath(document.Path);
            if (DBDocument == null)
            {
                Context.Document.Add(document);
                Index.Flush();
                DBDocument = FindDocumentByPath(document.Path);
            }
            else if (DateTime.Compare(DBDocument.LastCrawled, new FileInfo(DBDocument.Path).LastWriteTime) > 0)
            {
                return;
            }
            foreach (string word in words)
            {
                Index.AddWord(DBDocument, word);
            }
            DBDocument.LastCrawled = DateTime.Now;
        }

        private Document FindDocumentByPath(string path)
        {
            return Context.Document.FirstOrDefault(e => e.Path == path);
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
