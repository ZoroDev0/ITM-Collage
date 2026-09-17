import auth.AuthenticationManager;
import gui.LoginFrame;
import service.FileHandler;
import service.StudentManager;

import javax.swing.*;

/**
 * Main.java
 * Application entry point for Student Record File Store - OOP + DSA Edition.
 * 
 * College Mini-Project:
 * Student Name: Sasanka Sekhar Kundu
 * Roll Number:  150096725118
 * 
 * Demonstrates:
 * - OOP: Abstraction, Encapsulation, Inheritance, Polymorphism, Interface, Method Overriding
 * - DSA: ArrayList, Stack (Undo), Queue (FIFO Operations), Searching (Linear + Binary), Sorting (Comparators)
 * - Core: File Handling (data/students.txt), Exception Handling (StudentException)
 */
public class Main {

    public static void main(String[] args) {
        // Enable subpixel text antialiasing for high-DPI screens
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Set System Look and Feel for native, crisp UI presentation
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Note: Native look and feel could not be initialized, falling back to default.");
        }

        // Initialize Core Services
        StudentManager studentManager = new StudentManager();
        FileHandler fileHandler = new FileHandler();
        AuthenticationManager authManager = new AuthenticationManager();

        // Preload persistent data from data/students.txt
        try {
            var loadedStudents = fileHandler.loadStudents();
            studentManager.setStudents(loadedStudents);
            System.out.println("Startup: Successfully loaded " + loadedStudents.size() + " student records from " + fileHandler.getFilePath());
        } catch (Exception e) {
            System.err.println("Startup Notice: Started with empty record store (" + e.getMessage() + ")");
        }

        // Launch the Login Screen on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            System.out.println("==================================================");
            System.out.println("  Student Record File Store - OOP + DSA Edition");
            System.out.println("  Student: Sasanka Sekhar Kundu | Roll: 150096725118");
            System.out.println("  Credentials: admin / admin123");
            System.out.println("==================================================");

            LoginFrame loginFrame = new LoginFrame(studentManager, fileHandler, authManager);
            loginFrame.setVisible(true);
        });
    }
}
