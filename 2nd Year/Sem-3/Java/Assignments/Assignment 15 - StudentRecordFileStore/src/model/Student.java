package model;

import exception.StudentException;
import interfaces.Validatable;

/**
 * Student.java
 * Concrete Student entity extending Person and implementing Validatable.
 * Demonstrates Inheritance, Method Overriding, Encapsulation, and Interfaces.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class Student extends Person implements Validatable {

    // Subclass-specific encapsulated fields
    private String course;
    private int semester;
    private double marks;

    // Default Constructor
    public Student() {
        super();
    }

    // Parameterized Constructor
    public Student(String studentId, String name, String course, int semester, double marks) {
        super(studentId, name);
        this.course = course;
        this.semester = semester;
        this.marks = marks;
    }

    // Copy Constructor (used by Undo Stack to capture exact snapshots)
    public Student(Student other) {
        super(other.getId(), other.getName());
        this.course = other.getCourse();
        this.semester = other.getSemester();
        this.marks = other.getMarks();
    }

    // Student ID aliases delegating to Person base id
    public String getStudentId() {
        return getId();
    }

    public void setStudentId(String studentId) {
        setId(studentId);
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    /**
     * Implements Validatable interface contract.
     * Enforces core domain integrity rules.
     */
    @Override
    public void validate() throws StudentException {
        if (getId() == null || getId().trim().isEmpty()) {
            throw new StudentException("Student ID cannot be empty.");
        }
        if (getName() == null || getName().trim().isEmpty()) {
            throw new StudentException("Student Name cannot be empty.");
        }
        if (course == null || course.trim().isEmpty()) {
            throw new StudentException("Course cannot be empty.");
        }
        if (semester <= 0) {
            throw new StudentException("Semester must be a positive integer greater than 0.");
        }
        if (marks < 0.0 || marks > 100.0) {
            throw new StudentException("Marks must be between 0 and 100.");
        }
    }

    /**
     * Overrides abstract displayInfo() from Person.
     * Demonstrates Method Overriding and Polymorphic dispatch.
     */
    @Override
    public String displayInfo() {
        String marksStr = (marks == (long) marks) ? String.format("%d", (long) marks) : String.format("%.2f", marks);
        return "Student ID: " + getId() + " | Name: " + getName() + " | Course: " + course +
               " | Semester: " + semester + " | Marks: " + marksStr;
    }

    /**
     * Formats the student object into a pipe-delimited string for file persistence.
     * Example: 101|Rahul Sharma|B.Tech CSE|2|85
     */
    public String toFileString() {
        String marksStr = (marks == (long) marks) ? String.format("%d", (long) marks) : String.format("%.2f", marks);
        return getId() + "|" + getName() + "|" + course + "|" + semester + "|" + marksStr;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID='" + getId() + '\'' +
                ", Name='" + getName() + '\'' +
                ", Course='" + course + '\'' +
                ", Semester=" + semester +
                ", Marks=" + marks +
                '}';
    }
}
