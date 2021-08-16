using SearchLib.Model;
using SearchLib.Utils;
using System.Collections.Generic;

namespace SearchLib
{
    public class SearchLib
    {

        private SearchEngine Engine;

        public SearchLib()
        {
            Engine = new SearchEngine();
        }

        public void CrawlPath(string path)
        {
            DocumentScanner crawler = new DocumentScanner(path);
            Engine.JoinIndex(crawler.GetIndex());
        }

        public ISet<Document> Search(string word)
        {
            return Engine.Search(new SearchQuery(word));
        }
    }
}
