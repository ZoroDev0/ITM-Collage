import java.util.Scanner;

public class StudentActivityManagementSystem {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        double attendance;
        int marks;

        // ===== Attendance Input =====
        while (true) {
            System.out.print("Enter attendance percentage (0-100): ");

            if (scanner.hasNextDouble()) {
                attendance = scanner.nextDouble();

                if (attendance >= 0 && attendance <= 100) {
                    break;
                } else {
                    System.out.println("Invalid attendance! Please enter a value between 0 and 100.");
                }
            } else {
                System.out.println("Invalid input! Please enter a numeric attendance percentage.");
                scanner.next();
            }
        }

        // ===== Marks Input =====
        while (true) {
            System.out.print("Enter marks (0-100): ");

            if (scanner.hasNextInt()) {
                marks = scanner.nextInt();

                if (marks >= 0 && marks <= 100) {
                    break;
                } else {
                    System.out.println("Invalid marks! Please enter marks between 0 and 100.");
                }
            } else {
                System.out.println("Invalid input! Please enter numeric marks.");
                scanner.next();
            }
        }

        // ===== Main Menu =====
        while (true) {

            System.out.println();
            System.out.println("===== Student Activity Management System =====");
            System.out.println();
            System.out.println("1. Check Attendance Eligibility");
            System.out.println("2. View Performance Category");
            System.out.println("3. Exit");
            System.out.println();

            System.out.print("Enter your choice: ");

            // Validate menu input
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter 1, 2, or 3.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    // Attendance Eligibility
                    System.out.println();
                    System.out.println("===== Attendance Eligibility =====");

                    if (attendance >= 75) {
                        System.out.println("Attendance : " + attendance + "%");
                        System.out.println("Status : Eligible to appear for the examination.");
                    } else {
                        System.out.println("Attendance : " + attendance + "%");
                        System.out.println("Status : Not eligible to appear for the examination.");
                    }
                    break;

                case 2:
                    // Performance Category
                    System.out.println();
                    System.out.println("===== Performance Category =====");
                    System.out.println("Marks : " + marks);

                    if (marks >= 90) {
                        System.out.println("Category : Excellent");
                    } else if (marks >= 70) {
                        System.out.println("Category : Good");
                    } else if (marks >= 60) {
                        System.out.println("Category : Average");
                    } else {
                        // Nested if
                        if (marks < 60) {
                            System.out.println("Category : Needs Improvement");
                        }
                    }
                    break;

                case 3:
                    // Exit
                    System.out.println();
                    System.out.println("Thank you for using the Student Activity Management System.");
                    System.out.println("Program exited successfully.");
                    scanner.close();
                    break;

                default:
                    System.out.println();
                    System.out.println("Invalid menu choice! Please select 1, 2, or 3.");
                    continue;
            }

            // Exit the main menu loop
            if (choice == 3) {
                break;
            }
        }
    }
}