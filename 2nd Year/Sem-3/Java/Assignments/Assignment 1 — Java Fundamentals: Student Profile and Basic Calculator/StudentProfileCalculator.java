public class StudentProfileCalculator {

    public static void main(String[] args) {

        // ===== Student Profile =====
        String name = "Rahul Sharma";
        int rollNo = 101;
        String course = "B.Tech Computer Engineering";
        String year = "Second Year";

        // ===== Calculator Values =====
        int firstNumber = 25;
        int secondNumber = 10;

        // ===== Arithmetic Operations =====
        int addition = firstNumber + secondNumber;
        int subtraction = firstNumber - secondNumber;
        int multiplication = firstNumber * secondNumber;
        double division = (double) firstNumber / secondNumber;

        // ===== Display Student Profile =====
        System.out.println("===== Student Profile =====");
        System.out.println();

        System.out.println("Name : " + name);
        System.out.println("Roll No : " + rollNo);
        System.out.println("Course : " + course);
        System.out.println("Year : " + year);

        // ===== Display Calculator =====
        System.out.println();
        System.out.println("===== Calculator =====");
        System.out.println();

        System.out.println("First Number : " + firstNumber);
        System.out.println("Second Number : " + secondNumber);
        System.out.println("Addition : " + addition);
        System.out.println("Subtraction : " + subtraction);
        System.out.println("Multiplication : " + multiplication);
        System.out.println("Division : " + division);
    }
}