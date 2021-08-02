using FluentAssertions;
using SearchLib.Utils;
using System.Collections.Generic;
using Xunit;

namespace SearchLib.Test.Utils
{
    public class DocumentProcessorTest
    {
        [Fact]
        void GetNormalizedWordsTest()
        {
            string firstTest = "HeLLO WorlD!";
            string secondTest = "Hi I a.m A HumAn";
            string thirdTest = "the ExPe-cted tesTiNg is , the, expect, test, is";
            string[] firstTestExpected = { "hello", "world" };
            string[] secondTestExpected = { "hi", "i", "am", "a", "human" };
            string[] thirdTestExpected = { "the", "exp", "cted", "test", "is", "the", "expect", "test", "is" };
            firstTestExpected.Should().BeEquivalentTo(new DocumentProcessor(firstTest).GetNormalizedWords());
            secondTestExpected.Should().BeEquivalentTo(new DocumentProcessor(secondTest).GetNormalizedWords());
            thirdTestExpected.Should().BeEquivalentTo(new DocumentProcessor(thirdTest).GetNormalizedWords());
        }

        [Fact]
        void DocumentProcessorConstructorTest()
        {
            ISet<string> firstTest = new HashSet<string>();
            firstTest.Add("t&h*e");
            firstTest.Add("aRt");
            firstTest.Add("O@f");
            firstTest.Add("Creations");
            firstTest.Add("t*he");
            firstTest.Add("jo&b");
            firstTest.Add("foR#");
            firstTest.Add("creators");
            string[] firstTestExpected = { "job", "art", "the", "of", "the", "creator", "creation", "for" };
            firstTestExpected.Should().BeEquivalentTo(new DocumentProcessor(firstTest).GetNormalizedWords());
        }
    }
}
