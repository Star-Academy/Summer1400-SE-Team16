using FluentAssertions;
using SearchLib.Exceptions;
using SearchLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using Xunit;

namespace SearchLib.Test.Controller
{
    public class SearchLibTest
    {
        private SearchLib Library;

        [Theory]
        [InlineData("+faster information -old -we", new string[] { "..\\..\\..\\..\\SampleEnglishData\\59522" })]
        [InlineData("+faster +information -old -we -have -is -I -how", new string[] { "..\\..\\..\\..\\SampleEnglishData\\59628" })]
        [InlineData("information -old -we -have -is -I -how", new string[] { "..\\..\\..\\..\\SampleEnglishData\\59628" })]
        [InlineData("old male", new string[] { "..\\..\\..\\..\\SampleEnglishData\\57110", "..\\..\\..\\..\\SampleEnglishData\\58796",
            "..\\..\\..\\..\\SampleEnglishData\\59122", "..\\..\\..\\..\\SampleEnglishData\\59207" })]
        public void SearchTest(string query, string[] expected)
        {
            Library = new SearchLib("localhost", "root", "", "test_searchlib_general");
            Library.CrawlPath("..\\..\\..\\..\\SampleEnglishData");
            ISet<Document> actualDocuments = Library.Search(query);
            ISet<Document> expectedDocuments = expected.Select(e => new Document(e)).ToHashSet();
            actualDocuments.Should().BeEquivalentTo(expectedDocuments);
        }

        [Theory]
        [InlineData(new string[] { "..\\..\\..\\..\\SampleEnglishData\\59522" }, "+faster information -old -we", new string[] { "..\\..\\..\\..\\SampleEnglishData\\59522" })]
        [InlineData(new string[] { "..\\..\\..\\..\\SampleEnglishData\\59522", "..\\..\\..\\..\\SampleEnglishData\\59628" },
            "+faster +information -old -we -have -is -I -how", new string[] { "..\\..\\..\\..\\SampleEnglishData\\59628" })]
        [InlineData(new string[] { "..\\..\\..\\..\\SampleEnglishData\\59522", "..\\..\\..\\..\\SampleEnglishData\\59628" },
            "information -old -we -have -is -I -how", new string[] { "..\\..\\..\\..\\SampleEnglishData\\59628" })]
        public void FileCrawlSearchTest(string[] crawlPaths, string query, string[] expected)
        {
            Library = new SearchLib("localhost", "root", "", "test_searchlib_partial");
            foreach (string crawlPath in crawlPaths)
            {
                Library.CrawlPath(crawlPath);
            }
            ISet<Document> actualDocuments = Library.Search(query);
            ISet<Document> expectedDocuments = expected.Select(e => new Document(e)).ToHashSet();
            actualDocuments.Should().BeEquivalentTo(expectedDocuments);
        }

        [Fact]
        public void InvalidPathCrawlTest()
        {
            Library = new SearchLib("localhost", "root", "", "test_searchlib_general");
            Action act = () => Library.CrawlPath("RandomInvalidPath");
            act.Should().Throw<SearchException>().WithMessage("Invalid Path");
        }
    }
}
