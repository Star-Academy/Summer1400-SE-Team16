using System;

namespace SearchLib.Exceptions
{
    public class SearchException : Exception
    {
        public SearchException()
        {
        }

        public SearchException(string message) : base(message)
        {
        }
    }
}
