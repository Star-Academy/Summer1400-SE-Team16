using FluentAssertions;
using SearchLib.Exceptions;
using SearchLib.Model;
using System;
using System.Collections.Generic;
using Xunit;

namespace SearchLib.Test.Controller
{
    public class SearchLibTest
    {
        private SearchLib Library;

        public SearchLibTest()
        {
            Library = new SearchLib();
        }

        [Fact]
        public void SearchTest()
        {
            ISet<Document> firstExpectedDocuments = new HashSet<Document>();
            firstExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\59522"));
            ISet<Document> secondExpectedDocuments = new HashSet<Document>();
            secondExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\59628"));
            ISet<Document> thirdExpectedDocuments = new HashSet<Document>();
            thirdExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\57110"));
            thirdExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\58796"));
            thirdExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\59122"));
            thirdExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\59207"));
            Library.CrawlPath("..\\..\\..\\..\\SampleEnglishData");
            ISet<Document> firstActualDocument = Library.Search("+faster information -old -we");
            ISet<Document> secondActualDocument = Library.Search("+faster +information -old -we -have -is -I -how");
            ISet<Document> thirdActualDocument = Library.Search("information -old -we -have -is -I -how");
            ISet<Document> fourthActualDocument = Library.Search("old male");
            firstExpectedDocuments.Should().BeEquivalentTo(firstActualDocument);
            secondExpectedDocuments.Should().BeEquivalentTo(secondActualDocument);
            secondExpectedDocuments.Should().BeEquivalentTo(thirdActualDocument);
            thirdExpectedDocuments.Should().BeEquivalentTo(fourthActualDocument);
        }

        [Fact]
        public void FileCrawlSearchTest()
        {
            ISet<Document> emptySet = new HashSet<Document>();
            ISet<Document> firstExpectedDocuments = new HashSet<Document>();
            firstExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\59522"));
            ISet<Document> secondExpectedDocuments = new HashSet<Document>();
            secondExpectedDocuments.Add(new Document("..\\..\\..\\..\\SampleEnglishData\\59628"));
            ISet<Document> noCrawlDocuments = Library.Search("test");
            emptySet.Should().BeEquivalentTo(noCrawlDocuments);
            Library.CrawlPath("..\\..\\..\\..\\SampleEnglishData\\59522");
            ISet<Document> firstActualDocuments = Library.Search("+faster information -old -we");
            ISet<Document> secondActualDocuments = Library.Search("+faster +information -old -we -have -is -I -how");
            ISet<Document> thirdActualDocuments = Library.Search("information -old -we -have -is -I -how");
            firstExpectedDocuments.Should().BeEquivalentTo(firstActualDocuments);
            emptySet.Should().BeEquivalentTo(secondActualDocuments);
            emptySet.Should().BeEquivalentTo(thirdActualDocuments);
            Library.CrawlPath("..\\..\\..\\..\\SampleEnglishData\\59628");
            ISet<Document> fourthActualDocuments = Library.Search("+faster +information -old -we -have -is -I -how");
            ISet<Document> fifthActualDocuments = Library.Search("information -old -we -have -is -I -how");
            secondExpectedDocuments.Should().BeEquivalentTo(fourthActualDocuments);
            secondExpectedDocuments.Should().BeEquivalentTo(fifthActualDocuments);
        }

        [Fact]
        public void InvalidPathCrawlTest()
        {
            Action act = () => Library.CrawlPath("RandomInvalidPath");
            act.Should().Throw<SearchException>().WithMessage("Invalid Path");
        }
    }
}
