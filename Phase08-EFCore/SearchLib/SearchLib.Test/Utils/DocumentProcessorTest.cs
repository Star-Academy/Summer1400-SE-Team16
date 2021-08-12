using FluentAssertions;
using SearchLib.Utils;
using System.Collections.Generic;
using Xunit;

namespace SearchLib.Test.Utils
{
    public class DocumentProcessorTest
    {
        [Theory]
        [InlineData("HeLLO WorlD!", new string[] { "hello", "world" })]
        [InlineData("Hi I a.m A HumAn", new string[] { "hi", "i", "am", "a", "human" })]
        [InlineData("the ExPe-cted tesTiNg is , the, expect, test, is", new string[] { "the", "exp", "cted", "test", "is", "expect" })]
        void GetNormalizedWordsTest(string text, string[] expected)
        {
            string[] actual = new DocumentProcessor(text).GetNormalizedWords();
            actual.Should().BeEquivalentTo(expected);
        }

        [Fact]
        void DocumentProcessorConstructorTest()
        {
            string[] expected = { "job", "art", "the", "of", "creator", "creation", "for" };
            ISet<string> data = new HashSet<string>();
            data.Add("t&h*e");
            data.Add("aRt");
            data.Add("O@f");
            data.Add("Creations");
            data.Add("t*he");
            data.Add("jo&b");
            data.Add("foR#");
            data.Add("creators");
            string[] actual = new DocumentProcessor(data).GetNormalizedWords();
            actual.Should().BeEquivalentTo(expected);
        }
    }
}
