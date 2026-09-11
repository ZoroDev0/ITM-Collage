import java.util.Scanner;

public class StudentMarksManagementSystem {

    // Display all student marks
    public static void displayMarks(String[] students, String[] subjects, int[][] marks) {

        System.out.println();
        System.out.println("===== All Student Marks =====");

        System.out.printf("%-12s %-10s %-10s %-10s%n",
                "Student", "Java", "Python", "DBMS");

        for (int i = 0; i < marks.length; i++) {
            System.out.printf("%-12s %-10d %-10d %-10d%n",
                    students[i],
                    marks[i][0],
                    marks[i][1],
                    marks[i][2]);
        }
    }

    // Calculate total marks for one student
    public static int calculateTotal(int[] studentMarks) {

        int total = 0;

        for (int mark : studentMarks) {
            total += mark;
        }

        return total;
    }

    // Calculate total marks for all students
    public static int[] calculateAllTotals(int[][] marks) {

        int[] totals = new int[marks.length];

        for (int i = 0; i < marks.length; i++) {
            totals[i] = calculateTotal(marks[i]);
        }

        return totals;
    }

    // Calculate average marks for one student
    public static double calculateAverage(int[] studentMarks) {

        return (double) calculateTotal(studentMarks) / studentMarks.length;
    }

    // Display totals and averages
    public static void displayTotalsAndAverages(
            String[] students,
            int[][] marks,
            int[] totals) {

        System.out.println();
        System.out.println("===== Student Totals and Averages =====");

        for (int i = 0; i < marks.length; i++) {

            double average = calculateAverage(marks[i]);

            System.out.println(
                    students[i] + " Total : " + totals[i]
            );

            System.out.printf(
                    students[i] + " Average : %.2f%n",
                    average
            );

            System.out.println();
        }
    }

    // Find highest scorer
    public static int findHighestScorer(int[] totals) {

        int highestIndex = 0;

        for (int i = 1; i < totals.length; i++) {

            if (totals[i] > totals[highestIndex]) {
                highestIndex = i;
            }
        }

        return highestIndex;
    }

    // Search for a particular mark using Linear Search
    public static boolean searchMarks(int[][] marks, int searchMark) {

        for (int i = 0; i < marks.length; i++) {

            for (int j = 0; j < marks[i].length; j++) {

                if (marks[i][j] == searchMark) {
                    return true;
                }
            }
        }

        return false;
    }

    // Display details of where a mark was found
    public static void searchAndDisplayMarks(
            String[] students,
            String[] subjects,
            int[][] marks,
            int searchMark) {

        boolean found = false;

        System.out.println();
        System.out.println("===== Linear Search =====");
        System.out.println("Searching for mark : " + searchMark);

        for (int i = 0; i < marks.length; i++) {

            for (int j = 0; j < marks[i].length; j++) {

                if (marks[i][j] == searchMark) {

                    System.out.println(
                            "Found " + searchMark +
                            " marks in " + students[i] +
                            " - " + subjects[j]
                    );

                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println(
                    "Mark " + searchMark + " was not found."
            );
        }
    }

    // Bubble Sort student totals from highest to lowest
    public static void bubbleSort(
            int[] totals,
            int[] studentIndexes) {

        for (int i = 0; i < totals.length - 1; i++) {

            for (int j = 0; j < totals.length - 1 - i; j++) {

                if (totals[j] < totals[j + 1]) {

                    // Swap totals
                    int tempTotal = totals[j];
                    totals[j] = totals[j + 1];
                    totals[j + 1] = tempTotal;

                    // Swap student indexes
                    int tempIndex = studentIndexes[j];
                    studentIndexes[j] = studentIndexes[j + 1];
                    studentIndexes[j + 1] = tempIndex;
                }
            }
        }
    }

    // Display sorted student totals
    public static void displaySortedTotals(
            String[] students,
            int[] totals,
            int[] studentIndexes) {

        System.out.println();
        System.out.println("===== Student Totals Ranking =====");

        for (int i = 0; i < totals.length; i++) {

            int studentIndex = studentIndexes[i];

            System.out.println(
                    (i + 1) + ". " +
                    students[studentIndex] +
                    " : " + totals[i]
            );
        }
    }

    // Display highest marks in each subject
    public static void displaySubjectHighestMarks(
            String[] subjects,
            int[][] marks) {

        System.out.println();
        System.out.println("===== Subject-Wise Highest Marks =====");

        for (int j = 0; j < subjects.length; j++) {

            int highest = marks[0][j];

            for (int i = 1; i < marks.length; i++) {

                if (marks[i][j] > highest) {
                    highest = marks[i][j];
                }
            }

            System.out.println(
                    "Highest " + subjects[j] +
                    " Marks : " + highest
            );
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Student names
        String[] students = {
                "Student 1",
                "Student 2",
                "Student 3",
                "Student 4",
                "Student 5"
        };

        // Subject names
        String[] subjects = {
                "Java",
                "Python",
                "DBMS"
        };

        // 2-D array containing student marks
        int[][] marks = {
                {78, 82, 75},
                {90, 85, 88},
                {65, 72, 70},
                {88, 91, 85},
                {55, 60, 58}
        };

        // 1-D array containing student totals
        int[] totals = calculateAllTotals(marks);

        // ===== Main Menu =====

        while (true) {

            System.out.println();
            System.out.println("===== Student Marks Management System =====");
            System.out.println();
            System.out.println("1. Display All Marks");
            System.out.println("2. Calculate Student Total");
            System.out.println("3. Calculate Student Average");
            System.out.println("4. Find Highest Scorer");
            System.out.println("5. Search Marks");
            System.out.println("6. Sort Student Totals");
            System.out.println("7. Display Subject-Wise Highest Marks");
            System.out.println("8. Exit");
            System.out.println();

            System.out.print("Enter your choice: ");

            // Validate menu input
            if (!scanner.hasNextInt()) {

                System.out.println(
                        "Invalid input! Please enter a number from 1 to 8."
                );

                scanner.next();

                continue;
            }

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    displayMarks(students, subjects, marks);
                    break;

                case 2:
                    System.out.print(
                            "Enter student number (1-5): "
                    );

                    if (!scanner.hasNextInt()) {
                        System.out.println(
                                "Invalid input! Please enter a student number."
                        );
                        scanner.next();
                        continue;
                    }

                    int totalStudent = scanner.nextInt();

                    if (totalStudent >= 1 && totalStudent <= 5) {

                        int index = totalStudent - 1;

                        System.out.println(
                                students[index] +
                                " Total : " +
                                calculateTotal(marks[index])
                        );

                    } else {

                        System.out.println(
                                "Invalid student number!"
                        );
                    }

                    break;

                case 3:
                    System.out.print(
                            "Enter student number (1-5): "
                    );

                    if (!scanner.hasNextInt()) {
                        System.out.println(
                                "Invalid input! Please enter a student number."
                        );
                        scanner.next();
                        continue;
                    }

                    int averageStudent = scanner.nextInt();

                    if (averageStudent >= 1 &&
                            averageStudent <= 5) {

                        int index = averageStudent - 1;

                        System.out.printf(
                                "%s Average : %.2f%n",
                                students[index],
                                calculateAverage(marks[index])
                        );

                    } else {

                        System.out.println(
                                "Invalid student number!"
                        );
                    }

                    break;

                case 4:

                    int highestIndex =
                            findHighestScorer(totals);

                    System.out.println();
                    System.out.println(
                            "===== Highest Scorer ====="
                    );

                    System.out.println(
                            "Highest Scorer: " +
                            students[highestIndex]
                    );

                    System.out.println(
                            "Total Marks : " +
                            totals[highestIndex]
                    );

                    break;

                case 5:

                    System.out.print(
                            "Enter mark to search: "
                    );

                    if (!scanner.hasNextInt()) {

                        System.out.println(
                                "Invalid input! Please enter a mark."
                        );

                        scanner.next();

                        continue;
                    }

                    int searchMark = scanner.nextInt();

                    if (searchMark < 0 ||
                            searchMark > 100) {

                        System.out.println(
                                "Invalid mark! Enter a value between 0 and 100."
                        );

                        continue;
                    }

                    searchAndDisplayMarks(
                            students,
                            subjects,
                            marks,
                            searchMark
                    );

                    break;

                case 6:

                    int[] sortedTotals = totals.clone();

                    int[] studentIndexes =
                            new int[students.length];

                    for (int i = 0;
                         i < studentIndexes.length;
                         i++) {

                        studentIndexes[i] = i;
                    }

                    bubbleSort(
                            sortedTotals,
                            studentIndexes
                    );

                    displaySortedTotals(
                            students,
                            sortedTotals,
                            studentIndexes
                    );

                    break;

                case 7:

                    displaySubjectHighestMarks(
                            subjects,
                            marks
                    );

                    break;

                case 8:

                    System.out.println();
                    System.out.println(
                            "Thank you for using the Student Marks Management System."
                    );

                    System.out.println(
                            "Program exited successfully."
                    );

                    scanner.close();

                    break;

                default:

                    System.out.println(
                            "Invalid menu choice! Please select 1 to 8."
                    );

                    continue;
            }

            // Exit the menu loop
            if (choice == 8) {
                break;
            }
        }
    }
}