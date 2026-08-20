import java.util.Scanner;

public class StudentActivityManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        double attendance;
        double marks;
  
        System.out.print("Enter student attendance percentage: ");
        attendance = sc.nextDouble();

        System.out.print("Enter student marks (0-100): ");
        marks = sc.nextDouble();

        // Validate input
        if (attendance < 0 || attendance > 100 || marks < 0 || marks > 100) {
            System.out.println("Invalid input! Attendance and marks must be between 0 and 100.");
            sc.close();
            return;
        }

        // Exam eligibility
        boolean eligible;

        if (attendance >= 75) {
            eligible = true;
        } else {
            eligible = false;
        }

        // Performance category using nested if
        String performance;

        if (marks >= 0) {
            if (marks >= 90) {
                performance = "Excellent";
            } else if (marks >= 75) {
                performance = "Very Good";
            } else if (marks >= 60) {
                performance = "Good";
            } else if (marks >= 40) {
                performance = "Average";
            } else {
                performance = "Poor";
            }
        } else {
            performance = "Invalid";
        }

        // Menu loop
        while (true) {

            System.out.println("\n===== Student Activity Management System =====");
            System.out.println("1. Check Exam Eligibility");
            System.out.println("2. View Performance Category");
            System.out.println("3. View Student Details");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            // continue for invalid menu entry
            if (choice < 1 || choice > 4) {
                System.out.println("Invalid menu choice! Please try again.");
                continue;
            }

            switch (choice) {

                case 1:
                    if (eligible) {
                        System.out.println("Student is eligible to appear for the examination.");
                    } else {
                        System.out.println("Student is NOT eligible to appear for the examination.");
                        System.out.println("Minimum attendance required: 75%");
                    }
                    break;

                case 2:
                    System.out.println("Performance Category: " + performance);
                    break;

                case 3:
                    System.out.println("\n----- Student Details -----");
                    System.out.println("Attendance: " + attendance + "%");
                    System.out.println("Marks: " + marks);
                    System.out.println("Exam Eligible: " + (eligible ? "Yes" : "No"));
                    System.out.println("Performance: " + performance);
                    break;

                case 4:
                    System.out.println("Thank you for using Student Activity Management System!");
                    sc.close();
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

            // Exit the loop
            if (choice == 4) {
                break;
            }
        }
    }
}