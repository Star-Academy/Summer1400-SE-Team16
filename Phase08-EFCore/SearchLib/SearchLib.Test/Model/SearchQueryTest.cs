using FluentAssertions;
using SearchLib.Model;
using Xunit;

namespace SearchLib.Test.Model
{
    public class SearchQueryTest
    {
        [Fact]
        void SearchQueryConstructorTest()
        {
            string[] requiredWordsExpected = { "need", "inform" };
            string[] optionalWordsExpected = { "hold", "fastest" };
            string[] bannedWordsExpected = { "stress", "easili" };
            string input = "+fastest +holding -easily -stresses need information";
            SearchQuery searchQuery = new SearchQuery(input);
            searchQuery.RequiredWords.Should().BeEquivalentTo(requiredWordsExpected);
            searchQuery.OptionalWords.Should().BeEquivalentTo(optionalWordsExpected);
            searchQuery.BannedWords.Should().BeEquivalentTo(bannedWordsExpected);
        }
    }
}
