package exception;

/**
 * StudentException.java
 * Custom Exception class for handling student validation, business rule violations,
 * and operation errors in the Student Record File Store application.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class StudentException extends Exception {

    public StudentException() {
        super("A student record error occurred.");
    }

    public StudentException(String message) {
        super(message);
    }

    public StudentException(String message, Throwable cause) {
        super(message, cause);
    }
}
