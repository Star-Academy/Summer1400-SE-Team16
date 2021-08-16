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
            string[] expectedSearchQueryRequiredWords = { "need", "inform" };
            string[] expectedSearchQueryOptionalWords = { "hold", "fastest" };
            string[] expectedSearchQueryBannedWords = { "stress", "easili" };
            string input = "+fastest +holding -easily -stresses need information";
            SearchQuery actualSearchQuery = new SearchQuery(input);
            actualSearchQuery.RequiredWords.Should().BeEquivalentTo(expectedSearchQueryRequiredWords);
            actualSearchQuery.OptionalWords.Should().BeEquivalentTo(expectedSearchQueryOptionalWords);
            actualSearchQuery.BannedWords.Should().BeEquivalentTo(expectedSearchQueryBannedWords);
        }
    }
}
