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
            string firstInput = "+fastest +holding -easily -stresses need information";
            string[] firstRequiredWordsExpected = { "need", "inform" };
            string[] firstOptionalWordsExpected = { "hold", "fastest" };
            string[] firstBannedWordsExpected = { "stress", "easili" };
            SearchQuery firstSearchQuery = new SearchQuery(firstInput);
            firstRequiredWordsExpected.Should().BeEquivalentTo(firstSearchQuery.RequiredWords);
            firstOptionalWordsExpected.Should().BeEquivalentTo(firstSearchQuery.OptionalWords);
            firstBannedWordsExpected.Should().BeEquivalentTo(firstSearchQuery.BannedWords);
        }
    }
}
