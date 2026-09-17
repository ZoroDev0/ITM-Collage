package service;

import exception.StudentException;
import model.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * FileHandler.java
 * Handles persistent storage for Student records using standard Java File I/O.
 * Demonstrates File Handling with BufferedReader and BufferedWriter.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class FileHandler {

    private final String directoryPath;
    private final String filePath;

    public FileHandler() {
        this("data", "data/students.txt");
    }

    public FileHandler(String directoryPath, String filePath) {
        this.directoryPath = directoryPath;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    /**
     * Ensures that the data directory and students.txt file exist.
     * Creates them if they are missing.
     */
    public void ensureFileExists() throws StudentException {
        try {
            File dir = new File(directoryPath);
            if (!dir.exists()) {
                boolean dirCreated = dir.mkdirs();
                if (!dirCreated && !dir.exists()) {
                    throw new StudentException("Failed to create directory: " + directoryPath);
                }
            }

            File file = new File(filePath);
            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                if (!fileCreated && !file.exists()) {
                    throw new StudentException("Failed to create file: " + filePath);
                }
            }
        } catch (IOException e) {
            throw new StudentException("I/O Error while creating data store: " + e.getMessage(), e);
        }
    }

    /**
     * Saves an ArrayList of Student objects to students.txt.
     * Uses FileWriter and BufferedWriter.
     */
    public void saveStudents(ArrayList<Student> students) throws StudentException {
        ensureFileExists();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath, false));

            for (Student student : students) {
                if (student != null) {
                    writer.write(student.toFileString());
                    writer.newLine();
                }
            }
            writer.flush();
        } catch (IOException e) {
            throw new StudentException("Failed to write student records to file: " + e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Warning: Could not close file writer: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Loads student records from students.txt into an ArrayList<Student>.
     * Uses FileReader and BufferedReader.
     * Malformed records are handled gracefully without crashing.
     */
    public ArrayList<Student> loadStudents() throws StudentException {
        ensureFileExists();

        ArrayList<Student> loadedList = new ArrayList<>();
        BufferedReader reader = null;
        int lineNumber = 0;
        int skippedCount = 0;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                try {
                    String[] parts = line.split("\\|", -1);

                    if (parts.length < 5) {
                        System.err.println("Malformed record at line " + lineNumber + ": insufficient columns -> " + line);
                        skippedCount++;
                        continue;
                    }

                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String course = parts[2].trim();
                    int semester = Integer.parseInt(parts[3].trim());
                    double marks = Double.parseDouble(parts[4].trim());

                    if (id.isEmpty() || name.isEmpty() || course.isEmpty() || semester <= 0 || marks < 0 || marks > 100) {
                        System.err.println("Invalid data values at line " + lineNumber + ": " + line);
                        skippedCount++;
                        continue;
                    }

                    Student student = new Student(id, name, course, semester, marks);
                    loadedList.add(student);

                } catch (NumberFormatException nfe) {
                    System.err.println("Number format error at line " + lineNumber + ": " + nfe.getMessage() + " in line: " + line);
                    skippedCount++;
                } catch (Exception ex) {
                    System.err.println("Unexpected error parsing line " + lineNumber + ": " + ex.getMessage());
                    skippedCount++;
                }
            }

            if (skippedCount > 0) {
                System.out.println("File loaded with " + loadedList.size() + " records. Skipped " + skippedCount + " malformed lines.");
            }

        } catch (IOException e) {
            throw new StudentException("Failed to read student records from file: " + e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Warning: Could not close file reader: " + e.getMessage());
                }
            }
        }

        return loadedList;
    }
}
