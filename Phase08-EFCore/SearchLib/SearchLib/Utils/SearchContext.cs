using Microsoft.EntityFrameworkCore;
using SearchLib.Model;

namespace SearchLib.Utils
{
    public class SearchContext : DbContext
    {
        public DbSet<WordIndex> WordIndex { get; set; }
        public DbSet<Document> Document { get; set; }

        private readonly string Server, Username, Password, DBName;

        public SearchContext(string server, string username, string password, string dBName)
        {
            Server = server;
            Username = username;
            Password = password;
            DBName = dBName;
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseMySQL(string.Format("server={0};database={3};user={1};password={2};SslMode=none", Server, Username, Password, DBName));
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Entity<WordIndex>(
                entity =>
                {
                    entity.HasKey(e => e.Word);
                });
            modelBuilder.Entity<Document>(
                entity =>
                {
                    entity.HasKey(e => e.ID);
                });
        }
    }
}
