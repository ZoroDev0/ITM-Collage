import java.util.ArrayList;
import java.util.List;

public class StudentMarks {

    private int rollNo;
    private String name;
    private double marks;

    // Shared student data collection
    private static final List<StudentMarks> students = new ArrayList<>();

    public StudentMarks(int rollNo, String name, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.marks = marks;
    }

    // Getters
    public int getRollNo() {
        return rollNo;
    }

    public String getName() {
        return name;
    }

    public double getMarks() {
        return marks;
    }

    // Setters
    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    // Add student
    public static void addStudent(StudentMarks student) {
        students.add(student);
    }

    // Remove student
    public static void removeStudent(int index) {
        students.remove(index);
    }

    // Get all students
    public static List<StudentMarks> getStudents() {
        return students;
    }

    // Calculate average
    public static double calculateAverage() {

        if (students.isEmpty()) {
            return 0.0;
        }

        double total = 0;

        for (StudentMarks student : students) {
            total += student.getMarks();
        }

        return total / students.size();
    }
}