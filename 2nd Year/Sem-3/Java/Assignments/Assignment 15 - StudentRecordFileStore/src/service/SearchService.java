package service;

import model.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * SearchService.java
 * Provides searching algorithms over student records.
 * Demonstrates Linear Search and Binary Search in DSA.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class SearchService {

    /**
     * Linear Search across all student attributes (ID, Name, Course).
     * Time Complexity: O(n)
     */
    public static ArrayList<Student> searchLinear(ArrayList<Student> students, String query) {
        ArrayList<Student> results = new ArrayList<>();
        if (students == null || query == null || query.trim().isEmpty()) {
            return (students != null) ? new ArrayList<>(students) : results;
        }

        String lowerQuery = query.trim().toLowerCase();
        for (Student s : students) {
            if (s != null) {
                boolean matchId = s.getId() != null && s.getId().toLowerCase().contains(lowerQuery);
                boolean matchName = s.getName() != null && s.getName().toLowerCase().contains(lowerQuery);
                boolean matchCourse = s.getCourse() != null && s.getCourse().toLowerCase().contains(lowerQuery);

                if (matchId || matchName || matchCourse) {
                    results.add(s);
                }
            }
        }
        return results;
    }

    /**
     * Linear Search filtering strictly by Name.
     */
    public static ArrayList<Student> searchByName(ArrayList<Student> students, String nameQuery) {
        ArrayList<Student> results = new ArrayList<>();
        if (students == null || nameQuery == null || nameQuery.trim().isEmpty()) {
            return results;
        }

        String lower = nameQuery.trim().toLowerCase();
        for (Student s : students) {
            if (s != null && s.getName() != null && s.getName().toLowerCase().contains(lower)) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * Linear Search filtering strictly by Course.
     */
    public static ArrayList<Student> searchByCourse(ArrayList<Student> students, String courseQuery) {
        ArrayList<Student> results = new ArrayList<>();
        if (students == null || courseQuery == null || courseQuery.trim().isEmpty()) {
            return results;
        }

        String lower = courseQuery.trim().toLowerCase();
        for (Student s : students) {
            if (s != null && s.getCourse() != null && s.getCourse().toLowerCase().contains(lower)) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * Binary Search to find a student by exact ID.
     * Prerequisite: The collection must be sorted by Student ID.
     * Time Complexity: O(log n)
     * 
     * If the input list is not already sorted, a sorted copy is created first.
     */
    public static Student binarySearchById(ArrayList<Student> students, String targetId) {
        if (students == null || students.isEmpty() || targetId == null || targetId.trim().isEmpty()) {
            return null;
        }

        String cleanTarget = targetId.trim();

        // Prepare sorted list by ID for binary search correctness
        ArrayList<Student> sortedList = new ArrayList<>(students);
        sortedList.sort(Comparator.comparing(Student::getId, String.CASE_INSENSITIVE_ORDER));

        int low = 0;
        int high = sortedList.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            Student midStudent = sortedList.get(mid);
            int cmp = midStudent.getId().compareToIgnoreCase(cleanTarget);

            if (cmp == 0) {
                return midStudent; // Record located
            } else if (cmp < 0) {
                low = mid + 1;     // Target is in upper half
            } else {
                high = mid - 1;    // Target is in lower half
            }
        }

        return null; // Not found
    }
}
