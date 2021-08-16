using FluentAssertions;
using SearchLib.Model;
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
            Index = new InvertedIndex();
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
            ISet<Document> expectedDocuments = documents.Select(e => new Document(e)).ToHashSet();
            ISet<Document> actualDocuments = Index.GetWordIndexes(word);
            actualDocuments.Should().BeEquivalentTo(expectedDocuments);
        }
    }
}
