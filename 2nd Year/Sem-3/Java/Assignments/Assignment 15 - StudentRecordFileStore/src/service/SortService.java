package service;

import model.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * SortService.java
 * Provides sorting capabilities on ArrayList<Student> using custom Comparators.
 * Demonstrates Sorting algorithms and Comparators in DSA.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class SortService {

    public enum SortCriteria {
        STUDENT_ID("Student ID"),
        NAME("Student Name"),
        MARKS("Marks"),
        SEMESTER("Semester");

        private final String displayName;

        SortCriteria(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Sorts the provided ArrayList<Student> by the specified field and direction.
     * Uses Java Comparator with Collections.sort().
     */
    public static void sort(ArrayList<Student> students, SortCriteria criteria, boolean ascending) {
        if (students == null || students.size() <= 1 || criteria == null) {
            return;
        }

        Comparator<Student> comparator;

        switch (criteria) {
            case STUDENT_ID:
                comparator = (s1, s2) -> {
                    // Try numeric comparison if both are integers, otherwise fallback to lexicographical
                    try {
                        long id1 = Long.parseLong(s1.getId().trim());
                        long id2 = Long.parseLong(s2.getId().trim());
                        return Long.compare(id1, id2);
                    } catch (NumberFormatException e) {
                        return s1.getId().compareToIgnoreCase(s2.getId());
                    }
                };
                break;

            case NAME:
                comparator = (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName());
                break;

            case MARKS:
                comparator = (s1, s2) -> Double.compare(s1.getMarks(), s2.getMarks());
                break;

            case SEMESTER:
                comparator = (s1, s2) -> Integer.compare(s1.getSemester(), s2.getSemester());
                break;

            default:
                comparator = Comparator.comparing(Student::getId);
                break;
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        students.sort(comparator);
    }
}
