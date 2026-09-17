import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class StudentCourseEnrollmentManager {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ArrayList<String> enrolledStudents = new ArrayList<>();
        LinkedList<String> waitingQueue = new LinkedList<>();

        int choice;

        System.out.println("===== Student Course Enrollment Manager =====");

        do {
            System.out.println();
            System.out.println("1. Add Student to Enrollment List");
            System.out.println("2. Display Enrolled Students");
            System.out.println("3. Add Student to Waiting Queue");
            System.out.println("4. Promote Student from Queue");
            System.out.println("5. Search Student in Enrollment List");
            System.out.println("6. Remove Student from Enrollment List");
            System.out.println("7. Display Waiting Queue");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter student name to enroll: ");
                    String studentName = scanner.nextLine();

                    enrolledStudents.add(studentName);

                    System.out.println("Student enrolled successfully.");
                    break;

                case 2:
                    System.out.println();
                    System.out.println("===== Enrolled Students =====");

                    if (enrolledStudents.isEmpty()) {
                        System.out.println("No students are currently enrolled.");
                    } else {
                        int index = 1;

                        for (String student : enrolledStudents) {
                            System.out.println(index + ". " + student);
                            index++;
                        }

                        System.out.println(
                                "Total Enrolled Students: "
                                        + enrolledStudents.size()
                        );
                    }
                    break;

                case 3:
                    System.out.print("Enter student name for waiting queue: ");
                    String waitingStudent = scanner.nextLine();

                    waitingQueue.addLast(waitingStudent);

                    System.out.println("Student added to waiting queue.");
                    break;

                case 4:
                    System.out.println();
                    System.out.println("Promoting first student from waiting queue...");

                    try {
                        String promotedStudent = waitingQueue.removeFirst();

                        enrolledStudents.add(promotedStudent);

                        System.out.println(
                                promotedStudent
                                        + " moved to Enrollment List."
                        );

                    } catch (NoSuchElementException e) {
                        System.out.println(
                                "Error: Waiting queue is empty. No student can be promoted."
                        );
                    }
                    break;

                case 5:
                    System.out.print("Enter student name to search: ");
                    String searchName = scanner.nextLine();

                    // for-each loop to safely traverse the ArrayList
                    boolean found = false;

                    for (String student : enrolledStudents) {
                        if (student.equalsIgnoreCase(searchName)) {
                            found = true;
                            break;
                        }
                    }

                    if (found && enrolledStudents.contains(searchName)) {
                        int studentIndex = enrolledStudents.indexOf(searchName);

                        System.out.println(
                                "Student found at index: " + studentIndex
                        );
                    } else if (found) {
                        // Handles case-insensitive matching
                        int studentIndex = -1;

                        for (int i = 0; i < enrolledStudents.size(); i++) {
                            if (enrolledStudents.get(i)
                                    .equalsIgnoreCase(searchName)) {
                                studentIndex = i;
                                break;
                            }
                        }

                        System.out.println(
                                "Student found at index: " + studentIndex
                        );
                    } else {
                        System.out.println("Student not found in enrollment list.");
                    }
                    break;

                case 6:
                    System.out.print("Enter student name to remove: ");
                    String removeName = scanner.nextLine();

                    try {
                        boolean removed = enrolledStudents.remove(removeName);

                        if (removed) {
                            System.out.println(
                                    "Student removed successfully."
                            );
                        } else {
                            System.out.println(
                                    "Student not found in enrollment list."
                            );
                        }

                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(
                                "Error: Invalid student index."
                        );
                    }
                    break;

                case 7:
                    System.out.println();
                    System.out.println("===== Waiting Queue =====");

                    if (waitingQueue.isEmpty()) {
                        System.out.println("Waiting queue is empty.");
                    } else {
                        int queuePosition = 1;

                        for (String student : waitingQueue) {
                            System.out.println(
                                    queuePosition + ". " + student
                            );
                            queuePosition++;
                        }

                        System.out.println(
                                "Total Waiting Students: "
                                        + waitingQueue.size()
                        );
                    }
                    break;

                case 8:
                    System.out.println(
                            "Exiting Student Course Enrollment Manager."
                    );
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please enter a number from 1 to 8."
                    );
            }

        } while (choice != 8);

        scanner.close();
    }
}