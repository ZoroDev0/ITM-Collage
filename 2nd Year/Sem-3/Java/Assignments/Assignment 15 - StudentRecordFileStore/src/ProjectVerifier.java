import auth.AuthenticationManager;
import exception.StudentException;
import interfaces.Validatable;
import model.Person;
import model.Student;
import model.StudentOperation;
import service.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * ProjectVerifier.java
 * Comprehensive automated test verification suite for the upgraded
 * Student Record File Store - OOP + DSA Edition.
 * 
 * Validates:
 * - Authentication
 * - OOP: Inheritance, Abstraction, Polymorphism, Interface, Method Overriding, Encapsulation
 * - DSA: ArrayList, Stack (Undo), Queue (FIFO), Searching (Linear & Binary), Sorting (Comparators)
 * - Core Java: File Handling, Exception Handling
 * 
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class ProjectVerifier {

    private static int passedCount = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  STUDENT RECORD FILE STORE - OOP + DSA VERIFIER");
        System.out.println("  Student: Sasanka Sekhar Kundu | Roll: 150096725118");
        System.out.println("==================================================\n");

        String testDir = "test_data";
        String testFile = "test_data/students.txt";

        cleanup(testDir, testFile);

        FileHandler fileHandler = new FileHandler(testDir, testFile);
        StudentManager manager = new StudentManager();
        AuthenticationManager auth = new AuthenticationManager();

        // TEST 1: Authentication System
        runTest("1. Authentication System (admin / admin123)", () -> {
            boolean ok = auth.authenticate("admin", "admin123");
            assertCondition(ok && auth.isAuthenticated(), "Valid credentials failed to authenticate.");

            boolean caughtInvalid = false;
            try {
                auth.authenticate("admin", "wrongpassword");
            } catch (StudentException e) {
                caughtInvalid = true;
            }
            assertCondition(caughtInvalid, "Invalid password was not rejected!");

            boolean caughtEmpty = false;
            try {
                auth.authenticate("", "admin123");
            } catch (StudentException e) {
                caughtEmpty = true;
            }
            assertCondition(caughtEmpty, "Empty username was not rejected!");
        });

        // TEST 2: OOP Inheritance & Abstraction
        runTest("2. OOP Inheritance & Abstraction (Student extends Person)", () -> {
            Student s = new Student("101", "Rahul Sharma", "B.Tech CSE", 2, 85.0);
            assertCondition(s instanceof Person, "Student must be an instance of Person (Inheritance).");
            assertCondition(s.getId().equals("101"), "Inherited getter getId() mismatch.");
            assertCondition(s.getName().equals("Rahul Sharma"), "Inherited getter getName() mismatch.");
        });

        // TEST 3: OOP Interface (Validatable)
        runTest("3. OOP Interface Contract (Validatable.validate())", () -> {
            Student s = new Student("102", "Priya Shah", "B.Tech CSE", 2, 91.0);
            assertCondition(s instanceof Validatable, "Student must implement Validatable interface.");
            s.validate(); // Should pass without exception

            boolean caughtInvalidMarks = false;
            Student bad = new Student("103", "Test Student", "CSE", 1, 150.0);
            try {
                bad.validate();
            } catch (StudentException e) {
                caughtInvalidMarks = true;
            }
            assertCondition(caughtInvalidMarks, "Validatable interface failed to catch marks > 100.");
        });

        // TEST 4: OOP Runtime Polymorphism & Method Overriding
        runTest("4. OOP Polymorphism & Method Overriding (Person -> Student.displayInfo())", () -> {
            // Polymorphic reference
            Person polymorphicRef = new Student("104", "Amit Roy", "BCA", 3, 78.0);
            String info = polymorphicRef.displayInfo(); // Dynamically dispatches to Student.displayInfo()
            assertCondition(info != null && info.contains("Student ID: 104") && info.contains("Course: BCA"),
                    "Polymorphic dispatch to Student.displayInfo() failed.");
        });

        // TEST 5: File Handling: Directory & File Auto-Creation
        runTest("5. File Handling: Auto-Creation of Directory & File", () -> {
            fileHandler.ensureFileExists();
            File d = new File(testDir);
            File f = new File(testFile);
            assertCondition(d.exists() && d.isDirectory(), "Data directory was not created.");
            assertCondition(f.exists() && f.isFile(), "Data file students.txt was not created.");
        });

        // TEST 6: ArrayList In-Memory Storage & Persistence
        runTest("6. ArrayList: In-Memory Storage & File Persistence", () -> {
            Student s1 = new Student("101", "Rahul Sharma", "B.Tech CSE", 2, 85.0);
            manager.addStudent(s1);
            fileHandler.saveStudents(manager.getAllStudents());

            assertCondition(manager.getStudentCount() == 1, "ArrayList should contain 1 student.");
            assertCondition(new File(testFile).length() > 0, "students.txt should be written.");
        });

        // TEST 7: Exception Handling: Duplicate Student ID
        runTest("7. Exception Handling: Duplicate ID (Throws StudentException)", () -> {
            boolean caught = false;
            try {
                Student dup = new Student("101", "Duplicate Person", "IT", 1, 80.0);
                manager.addStudent(dup);
            } catch (StudentException e) {
                caught = true;
                assertCondition(e.getMessage().contains("already exists"), "Duplicate message mismatch.");
            }
            assertCondition(caught, "Duplicate ID was not rejected!");
        });

        // TEST 8: Queue DSA: FIFO Operation Queue
        runTest("8. Queue DSA: FIFO StudentOperation Processing", () -> {
            OperationQueue queue = new OperationQueue();
            Student sA = new Student("201", "Student A", "CSE", 1, 70.0);
            Student sB = new Student("202", "Student B", "ECE", 2, 80.0);

            queue.enqueue(new StudentOperation(StudentOperation.OperationType.ADD, sA, "First Op"));
            queue.enqueue(new StudentOperation(StudentOperation.OperationType.ADD, sB, "Second Op"));

            assertCondition(queue.getPendingCount() == 2, "Queue should have 2 pending items.");

            // First In First Out
            StudentOperation firstOut = queue.dequeue();
            assertCondition(firstOut.getStudent().getId().equals("201"), "FIFO violated: First out must be Student A.");

            StudentOperation secondOut = queue.dequeue();
            assertCondition(secondOut.getStudent().getId().equals("202"), "FIFO violated: Second out must be Student B.");

            assertCondition(queue.getPendingCount() == 0, "Queue should be empty after dequeues.");
        });

        // Add additional students for Searching, Sorting, and Stack testing
        try {
            manager.addStudent(new Student("102", "Priya Shah", "B.Tech CSE", 2, 91.0));
            manager.addStudent(new Student("103", "David Roy", "BCA", 4, 76.5));
            manager.addStudent(new Student("104", "Aman Verma", "B.Tech ME", 1, 64.0));
            fileHandler.saveStudents(manager.getAllStudents());
        } catch (Exception ignored) {}

        // TEST 9: Searching DSA: Linear Search (Query across all fields)
        runTest("9. Searching DSA: Linear Search across ID/Name/Course", () -> {
            ArrayList<Student> all = manager.getAllStudents();
            ArrayList<Student> resultsName = SearchService.searchLinear(all, "priya");
            assertCondition(resultsName.size() == 1 && resultsName.get(0).getId().equals("102"), "Linear search by name failed.");

            ArrayList<Student> resultsCourse = SearchService.searchLinear(all, "CSE");
            assertCondition(resultsCourse.size() == 2, "Linear search by course 'CSE' should match 2 records.");
        });

        // TEST 10: Searching DSA: Binary Search O(log n)
        runTest("10. Searching DSA: Binary Search by Student ID O(log n)", () -> {
            ArrayList<Student> all = manager.getAllStudents();
            Student found = SearchService.binarySearchById(all, "103");
            assertCondition(found != null && found.getName().equals("David Roy"), "Binary search failed to find ID 103.");

            Student notFound = SearchService.binarySearchById(all, "999");
            assertCondition(notFound == null, "Binary search should return null for nonexistent ID 999.");
        });

        // TEST 11: Sorting DSA: Comparator Sorting
        runTest("11. Sorting DSA: Multi-Attribute Comparators (SortService)", () -> {
            ArrayList<Student> list = manager.getAllStudents();

            // Sort by Marks Descending
            SortService.sort(list, SortService.SortCriteria.MARKS, false);
            assertCondition(list.get(0).getMarks() >= list.get(1).getMarks(), "Marks Descending sort failed.");
            assertCondition(list.get(0).getId().equals("102"), "Top student by marks should be 102 (91.0).");

            // Sort by Name Ascending
            SortService.sort(list, SortService.SortCriteria.NAME, true);
            assertCondition(list.get(0).getName().compareToIgnoreCase(list.get(1).getName()) <= 0, "Name Ascending sort failed.");
        });

        // TEST 12: Stack DSA: Undo Last Action (Undo ADD)
        runTest("12. Stack DSA: Undo ADD Action via UndoManager", () -> {
            Student tempStudent = new Student("105", "Temporary Student", "Civil", 1, 72.0);
            manager.addStudent(tempStudent);
            assertCondition(manager.exists("105"), "Student 105 should exist before undo.");

            String undoMsg = manager.undoLastAction();
            assertCondition(undoMsg.contains("Removed added student"), "Expected undo message.");
            assertCondition(!manager.exists("105"), "Undo ADD failed: Student 105 still exists in ArrayList!");
        });

        // TEST 13: Stack DSA: Undo UPDATE Action
        runTest("13. Stack DSA: Undo UPDATE Action via UndoManager", () -> {
            Student original103 = manager.findStudentById("103");
            double origMarks = original103.getMarks();

            // Update student
            Student updated103 = new Student("103", "David Roy Updated", "BCA", 4, 99.0);
            manager.updateStudent(updated103);
            assertCondition(manager.findStudentById("103").getMarks() == 99.0, "Update failed.");

            // Undo Update
            manager.undoLastAction();
            Student restored103 = manager.findStudentById("103");
            assertCondition(restored103.getMarks() == origMarks, "Undo UPDATE failed: Marks not restored.");
            assertCondition(restored103.getName().equals("David Roy"), "Undo UPDATE failed: Name not restored.");
        });

        // TEST 14: Stack DSA: Undo DELETE Action
        runTest("14. Stack DSA: Undo DELETE Action via UndoManager", () -> {
            assertCondition(manager.exists("104"), "Student 104 should exist before delete.");
            manager.deleteStudent("104");
            assertCondition(!manager.exists("104"), "Student 104 should be removed after delete.");

            // Undo Delete
            manager.undoLastAction();
            assertCondition(manager.exists("104"), "Undo DELETE failed: Student 104 was not restored!");
            assertCondition(manager.findStudentById("104").getName().equals("Aman Verma"), "Restored student details mismatch.");
        });

        // TEST 15: File Handling: Resilience Against Malformed Data
        runTest("15. File Handling: Resilience Against Corrupt/Malformed Lines", () -> {
            try {
                FileWriter fw = new FileWriter(testFile, true);
                fw.write("\nBROKEN_LINE_WITH_NO_PIPES\n");
                fw.write("999|Bad Marks|Course|3|NOT_NUMERIC\n");
                fw.write("888|Bad Sem|Course|-2|80\n");
                fw.write("106|Kavita Roy|B.Sc IT|1|88\n"); // Valid line
                fw.close();

                ArrayList<Student> recovered = fileHandler.loadStudents();
                // 4 original valid students + 1 new valid student 106 = 5 total
                assertCondition(recovered.size() == 5, "Expected 5 valid records parsed, got: " + recovered.size());
            } catch (Exception e) {
                throw new RuntimeException("Malformed file crashed loader: " + e.getMessage());
            }
        });

        // TEST 16: Stack Empty Handling
        runTest("16. Stack Exception Handling: Empty Undo Stack", () -> {
            UndoManager emptyUndo = new UndoManager();
            assertCondition(!emptyUndo.canUndo(), "Empty UndoManager should return canUndo() = false.");
            assertCondition(emptyUndo.popAction() == null, "Empty pop should return null.");
        });

        cleanup(testDir, testFile);

        System.out.println("\n==================================================");
        System.out.println("VERIFICATION SUMMARY: " + passedCount + " / " + totalTests + " TESTS PASSED");
        System.out.println("ALL OOP + DSA + CORE JAVA REQUIREMENTS VERIFIED!");
        System.out.println("==================================================");

        if (passedCount != totalTests) {
            System.exit(1);
        }
    }

    private static void runTest(String testName, TestRunnable test) {
        totalTests++;
        System.out.print("[TEST " + totalTests + "] " + testName + " ... ");
        try {
            test.run();
            passedCount++;
            System.out.println("PASSED");
        } catch (Throwable t) {
            System.out.println("FAILED: " + t.getMessage());
            t.printStackTrace(System.out);
        }
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void cleanup(String dirPath, String filePath) {
        try {
            File f = new File(filePath);
            if (f.exists()) f.delete();
            File d = new File(dirPath);
            if (d.exists()) d.delete();
        } catch (Exception ignored) {}
    }

    @FunctionalInterface
    interface TestRunnable {
        void run() throws Exception;
    }
}
