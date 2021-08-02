using FluentAssertions;
using SearchLib.Controller;
using SearchLib.Model;
using System.Collections.Generic;
using Xunit;

namespace SearchLib.Test.Controller
{
    public class AppControllerTest
    {
        private AppController controller;

        public AppControllerTest()
        {
            controller = new AppController();
            controller.Initialize();
        }

        [Fact]
        public void searchTest()
        {
            ISet<Document> firstActualDocument = controller.search("+faster information -old -we");
            ISet<Document> secondActualDocument = controller.search("+faster +information -old -we -have -is -I -how");
            ISet<Document> thirdActualDocument = controller.search("information -old -we -have -is -I -how");
            ISet<Document> firstExpectedDocument = new HashSet<Document>();
            firstExpectedDocument.Add(new Document("SampleEnglishData/59522"));
            firstExpectedDocument.Should().BeEquivalentTo(firstActualDocument);
            ISet<Document> secondExpectedDocument = new HashSet<Document>();
            secondExpectedDocument.Add(new Document("SampleEnglishData/59628"));
            secondExpectedDocument.Should().BeEquivalentTo(secondActualDocument);
            secondExpectedDocument.Should().BeEquivalentTo(thirdActualDocument);
        }
    }
}
