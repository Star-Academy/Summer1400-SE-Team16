using SearchLib.Utils;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace SearchLib.Model
{
    public class SearchQuery
    {
        private static readonly string REQUIRED_WORDS_REGEX = "(?<=\\s|^)([^+-]\\S+)(?=\\s|$)";
        private static readonly string OPTIONAL_WORDS_REGEX = "(?<=\\s|^)\\+(\\S+)(?=\\s|$)";
        private static readonly string BANNED_WORDS_REGEX = "(?<=\\s|^)-(\\S+)(?=\\s|$)";

        public ISet<string> RequiredWords { get; }
        public ISet<string> OptionalWords { get; }
        public ISet<string> BannedWords { get; }
        public SearchQuery(string input)
        {
            OptionalWords = GetStemmedWordsFromInputByRegex(input, OPTIONAL_WORDS_REGEX);
            BannedWords = GetStemmedWordsFromInputByRegex(input, BANNED_WORDS_REGEX);
            RequiredWords = GetStemmedWordsFromInputByRegex(input, REQUIRED_WORDS_REGEX);
        }

        private ISet<string> GetStemmedWordsFromInputByRegex(string input, string pattern)
        {
            ISet<string> wordsSet = new HashSet<string>();
            MatchCollection matches = Regex.Matches(input, pattern);
            foreach (Match match in matches)
            {
                wordsSet.Add(match.Groups[1].Value);
            }
            return new DocumentProcessor(wordsSet).GetNormalizedWords();
        }
    }
}
