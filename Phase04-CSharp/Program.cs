using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;

namespace First // Phase 4
{
    class Program
    {
        static void Main(string[] args)
        {
            foreach (Student Student in GetTopThreeStudents())
            {
                PrintStudentData(Student);
            }
        }

        private static List<Student> GetTopThreeStudents()
        {
            List<Student> Students = DeserializeJsonFile<Student>("Students.json");
            List<StudentScore> StudentScores = DeserializeJsonFile<StudentScore>("Scores.json");
            AssignStudentAverages(Students, StudentScores);
            return Students.OrderByDescending(o => o.Average).Take(3).ToList();
        }

        private static void AssignStudentAverages(List<Student> Students, List<StudentScore> StudentScores)
        {
            foreach (Student Student in Students)
            {
                Student.Average = StudentScores.Where(o => o.StudentNumber == Student.StudentNumber).Average(o => o.Score);
            }
        }

        private static List<T> DeserializeJsonFile<T>(string FileName) => JsonSerializer.Deserialize<List<T>>(File.ReadAllText(FileName));

        private static void PrintStudentData(Student Student)
        {
            Console.WriteLine("{0,-20}{1,-20}{2,-5}", Student.FirstName, Student.LastName, Student.Average.ToString("0.00"));
        }
    }
}
