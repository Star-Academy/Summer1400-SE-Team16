using Microsoft.EntityFrameworkCore;
using SearchLib.Exceptions;
using SearchLib.Model;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;

namespace SearchLib.Utils
{
    class DocumentScanner
    {
        private readonly string BasePath;
        private readonly InvertedIndex Index;
        private readonly SearchContext Context;
        private HashSet<Document> PathDocuments;
        private HashSet<Document> DocumentsToScan;

        public DocumentScanner(InvertedIndex index, string basePath)
        {
            BasePath = basePath;
            Index = index;
            Context = index.Context;
            PathDocuments = new HashSet<Document>();
            DocumentsToScan = new HashSet<Document>();
            PreparePathForIndexing();
            foreach (Document document in DocumentsToScan)
            {
                IndexFile(document);
            }
            Index.FlushAndClearCache();
        }

        private void PreparePathForIndexing()
        {
            if (IsPathDirectory(BasePath))
            {
                PrepareAllNewDocumentsInDirectoryForIndexing();
            }
            else if (IsPathFile(BasePath))
            {
                PrepareANewDocumentForIndexing();
            }
            else
            {
                throw new SearchException("Invalid Path");
            }
        }

        private void PrepareAllNewDocumentsInDirectoryForIndexing()
        {
            PathDocuments = GetBasePathDocumentsFromDB();
            IEnumerable<string> documentPaths = GetAllFilesInPath();
            foreach (string documentPath in documentPaths)
            {
                PutDocumentInDB(documentPath);
            }
            Index.Flush();
            PathDocuments = GetBasePathDocumentsFromDB();
            foreach (Document document in PathDocuments)
            {
                if (!IsFileSameAfterLastCrawl(document))
                {
                    DocumentsToScan.Add(document);
                }
            }
        }

        private HashSet<Document> GetBasePathDocumentsFromDB()
        {
            return Context.Document
                            .FromSqlRaw(string.Format(
                                "SELECT * FROM document WHERE Path LIKE '{0}\\\\\\\\%'",
                                Regex.Replace(Path.GetFullPath(BasePath), "(?<!\\\\)\\\\(?!\\\\)", "\\\\\\\\")))
                                .ToHashSet();
        }

        private IEnumerable<string> GetAllFilesInPath()
        {
            return Directory.EnumerateFiles(BasePath);
        }

        private void PrepareANewDocumentForIndexing()
        {
            Document DBDocument = GetDocumentFromDB(BasePath);
            if (DBDocument == null)
            {
                PutDocumentInDB(BasePath);
                Index.Flush();
                DBDocument = GetDocumentFromDB(BasePath);
            }
            if (!IsFileSameAfterLastCrawl(DBDocument))
            {
                DocumentsToScan.Add(DBDocument);
            }
        }

        private void IndexFile(Document document)
        {
            ISet<string> words = GetProcessedDocumentPathData(document);
            IndexDocumentWords(document, words);
        }

        private ISet<string> GetProcessedDocumentPathData(Document document)
        {
            string data = GetPathData(document.Path);
            return ProcessDocumentData(data);
        }

        private string GetPathData(string documentPath)
        {
            return File.ReadAllText(documentPath);
        }

        private ISet<string> ProcessDocumentData(string data)
        {
            return new DocumentProcessor(data).GetNormalizedWords();
        }

        private void IndexDocumentWords(Document document, ISet<string> words)
        {
            foreach (string word in words)
            {
                Index.AddWord(document, word);
            }
            document.LastCrawled = DateTime.Now;
        }

        private void PutDocumentInDB(string path)
        {
            Document document = new Document(path);
            Document DBDocument = PathDocuments.FirstOrDefault(e => e.Path == document.Path);
            if (DBDocument == null)
            {
                document.LastCrawled = DateTime.MinValue;
                Context.Document.Add(document);
            }
        }

        private Document GetDocumentFromDB(string path)
        {
            return Context.Document.FirstOrDefault(e => e.Path == Path.GetFullPath(path));
        }

        private bool IsPathDirectory(string basePath)
        {
            return !File.Exists(basePath) && Directory.Exists(basePath);
        }

        private bool IsPathFile(string basePath)
        {
            return File.Exists(basePath) && !Directory.Exists(basePath);
        }
        private bool IsFileSameAfterLastCrawl(Document document)
        {
            return DateTime.Compare(document.LastCrawled, new FileInfo(document.Path).LastWriteTime) > 0;
        }
    }
}
