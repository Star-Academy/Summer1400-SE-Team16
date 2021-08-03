using FluentAssertions;
using SearchLib.Model;
using System.Collections.Generic;
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

        [Fact]
        public void AddWordTest()
        {
            Index.AddWord(new Document("..\\..\\..\\..\\SampleEnglishData\\57110"), "have");
            Index.AddWord(new Document("..\\..\\..\\..\\SampleEnglishData\\58043"), "have");
            Index.AddWord(new Document("..\\..\\..\\..\\SampleEnglishData\\58043"), "same");
            ISet<Document> firstExpectedDocuments = new HashSet<Document>();
            ISet<Document> secondExpectedDocuments = new HashSet<Document>();
            firstExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\57110"));
            firstExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\58043"));
            secondExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\58043"));
            firstExpectedDocuments.Should().BeEquivalentTo(Index.GetWordIndexes("have"));
            secondExpectedDocuments.Should().BeEquivalentTo(Index.GetWordIndexes("same"));
        }
    }
}
