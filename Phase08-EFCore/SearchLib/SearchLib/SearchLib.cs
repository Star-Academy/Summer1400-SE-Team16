using SearchLib.Model;
using SearchLib.Utils;
using System.Collections.Generic;

namespace SearchLib
{
    public class SearchLib
    {
        private readonly SearchEngine Engine;

        public SearchLib(string server, string username, string password, string DBName)
        {
            SearchContext Context = new SearchContext(server, username, password, DBName);
            Context.Database.EnsureCreated();
            Engine = new SearchEngine(Context);
        }

        public void CrawlPath(string path)
        {
            new DocumentScanner(Engine.Index, path);
        }

        public ISet<Document> Search(string word)
        {
            return Engine.Search(new SearchQuery(word));
        }
    }
}
