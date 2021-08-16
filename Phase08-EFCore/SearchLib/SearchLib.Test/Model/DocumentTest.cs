using FluentAssertions;
using SearchLib.Exceptions;
using SearchLib.Model;
using System;
using Xunit;

namespace SearchLib.Test.Model
{
    public class DocumentTest
    {
        [Fact]
        public void InvalidDocumentPathTest()
        {
            Action act = () => new Document("RandomNonExistingPath");
            act.Should().Throw<SearchException>().WithMessage("Invalid Path");
        }

        [Fact]
        public void ValidDocumentPathTest()
        {
            new Document("..\\..\\..\\..\\SampleEnglishData\\57110");
        }
    }
}
