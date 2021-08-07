using System.Text.Json.Serialization;

namespace First // Phase 4
{
    class Student
    {
        public int StudentNumber { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        [JsonIgnore]
        public double Average { get; set; }
    }

    class StudentScore
    {
        public int StudentNumber { get; set; }
        public string Lesson { get; set; }
        public double Score { get; set; }
    }
}
