using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SearchLib.Model
{
    public class SearchQuery
    {
        public string[] RequiredWords { get; }
        public string[] OptionalWords { get; }
        public string[] BannedWords { get; }
        public SearchQuery(string input)
        {

        }
    }
}
