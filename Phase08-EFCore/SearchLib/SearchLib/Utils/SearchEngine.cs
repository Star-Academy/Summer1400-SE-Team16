using SearchLib.Exceptions;
using SearchLib.Model;
using System.Collections.Generic;
using System.Linq;

namespace SearchLib.Utils
{
    public class SearchEngine
    {
        public readonly InvertedIndex Index;

        public SearchEngine(SearchContext context)
        {
            Index = new InvertedIndex(context);
        }

        public ISet<Document> Search(SearchQuery query)
        {
            if (query == null)
            {
                throw new SearchException("corrupted search query");
            }
            HashSet<Document> resultsSet;
            try
            {
                resultsSet = GetCommonWordsIndexSet(query.RequiredWords);
            }
            catch (SearchException)
            {
                resultsSet = new HashSet<Document>();
            }
            if (IsArrayNotEmpty(query.OptionalWords))
            {
                resultsSet = AddOptionalWordsAndRemoveBannedWords(query, resultsSet);
            }
            else if (IsArrayNotEmpty(query.RequiredWords))
            {
                RemoveBannedWords(query.BannedWords, resultsSet);
            }
            return resultsSet;
        }

        private HashSet<Document> GetCommonWordsIndexSet(string[] resultsWords)
        {
            string minimumResultsWord = GetMinimumResultsWord(resultsWords);
            HashSet<Document> resultsSet = new HashSet<Document>(GetWordIndexes(minimumResultsWord));
            foreach (string resultsWord in resultsWords)
            {
                if (minimumResultsWord.Equals(resultsWord))
                {
                    continue;
                }
                resultsSet.IntersectWith(GetWordIndexes(resultsWord));
            }
            return resultsSet;
        }

        private string GetMinimumResultsWord(string[] resultsWords)
        {
            if (resultsWords == null || resultsWords.Length == 0) throw new SearchException();
            string minWord = null;
            int minLen = int.MaxValue;
            foreach (string word in resultsWords)
            {
                if (GetWordIndexes(word) == null)
                {
                    throw new SearchException();
                }
                if (GetWordIndexes(word).Count <= minLen)
                {
                    minLen = GetWordIndexes(word).Count;
                    minWord = word;
                }
            }
            return minWord;
        }

        private HashSet<Document> AddOptionalWordsAndRemoveBannedWords(SearchQuery query, HashSet<Document> resultsSet)
        {
            if (IsArrayNotEmpty(query.RequiredWords))
            {
                RemoveBannedWords(query.BannedWords, resultsSet);
                RemoveIndexesWithoutOptionalWords(query, resultsSet);
            }
            else
            {
                resultsSet = GetJointWordsIndexSet(query.OptionalWords);
                RemoveBannedWords(query.BannedWords, resultsSet);
            }
            return resultsSet;
        }

        private void RemoveIndexesWithoutOptionalWords(SearchQuery query, HashSet<Document> resultsSet)
        {
            resultsSet.RemoveWhere(wordIndex => !IsIndexFoundInOptionalWordsIndexes(query, wordIndex));
        }

        private bool IsIndexFoundInOptionalWordsIndexes(SearchQuery query, Document wordIndex)
        {
            return query.OptionalWords.Any(word => IsIndexInWordIndexes(wordIndex, word));
        }

        private HashSet<Document> GetJointWordsIndexSet(string[] resultsWords)
        {
            HashSet<Document> jointSet = new HashSet<Document>();
            foreach (string word in resultsWords)
            {
                if (GetWordIndexes(word) == null)
                {
                    continue;
                }
                jointSet.UnionWith(GetWordIndexes(word));
            }
            return jointSet;
        }

        private void RemoveBannedWords(string[] bannedWords, HashSet<Document> resultsSet)
        {
            if (!IsArrayNotEmpty(bannedWords))
            {
                return;
            }

            foreach (string word in bannedWords)
            {
                resultsSet.RemoveWhere(wordIndex => IsIndexInWordIndexes(wordIndex, word));
            }
        }

        private bool IsIndexInWordIndexes(Document wordIndex, string word)
        {
            if (GetWordIndexes(word) != null)
            {
                return GetWordIndexes(word).Contains(wordIndex);
            }
            return false;
        }

        private bool IsArrayNotEmpty(string[] wordsArray)
        {
            return wordsArray != null && wordsArray.Length != 0;
        }

        private ISet<Document> GetWordIndexes(string minimumResultsWord)
        {
            return Index.GetWordIndexes(minimumResultsWord);
        }
    }
}
