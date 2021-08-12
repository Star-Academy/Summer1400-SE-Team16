using FluentAssertions;
using SearchLib.Model;
using SearchLib.Utils;
using System.Collections.Generic;
using System.Linq;
using Xunit;

namespace SearchLib.Test.Utils
{
    public class InvertedIndexTest
    {

        private InvertedIndex Index;

        public InvertedIndexTest()
        {
            SearchContext context = new SearchContext("localhost", "root", "", "test_searchlib_inverted_index");
            context.Database.EnsureCreated();
            Index = new InvertedIndex(context);
        }

        [Theory]
        [InlineData(new string[] { "..\\..\\..\\..\\SampleEnglishData\\57110", "..\\..\\..\\..\\SampleEnglishData\\58043" }, "have")]
        [InlineData(new string[] { "..\\..\\..\\..\\SampleEnglishData\\58043" }, "same")]
        public void AddWordTest(string[] documents, string word)
        {
            foreach (string document in documents)
            {
                Index.AddWord(new Document(document), word);
            }
            ISet<string> actualDocuments = Index.GetWordIndexes(word).Select(e => e.Path).ToHashSet();
            actualDocuments.Should().BeEquivalentTo(documents.Select(e => new Document(e).Path));
        }
    }
}
