using System.Text.Json.Serialization;

namespace First
{
    class StudentScore
    {
        public int StudentNumber { get; set; }
        public double Score { get; set; }
        public string Lesson { get; set; }
    }

    class Student
    {
        public int StudentNumber { get; set; }
        public string LastName { get; set; }
        public string FirstName { get; set; }
        [JsonIgnore]
        public double Average { get; set; }
    }
}