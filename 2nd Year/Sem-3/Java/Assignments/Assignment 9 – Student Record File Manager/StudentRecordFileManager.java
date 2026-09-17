import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class StudentRecordFileManager {

    static final String DIRECTORY_NAME = "StudentRecords";
    static final String FILE_NAME = "student.txt";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        File directory = new File(DIRECTORY_NAME);
        File recordFile = new File(directory, FILE_NAME);

        int choice;

        System.out.println("===== Student Record File Manager =====");

        do {
            System.out.println();
            System.out.println("1. Create Records Directory");
            System.out.println("2. Create Record File");
            System.out.println("3. Write Student Record");
            System.out.println("4. Display File Information");
            System.out.println("5. Read File Content");
            System.out.println("6. Append New Record");
            System.out.println("7. Delete Record File");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    createDirectory(directory);
                    break;

                case 2:
                    createRecordFile(directory, recordFile);
                    break;

                case 3:
                    writeRecord(directory, recordFile, scanner);
                    break;

                case 4:
                    displayFileInformation(recordFile, directory);
                    break;

                case 5:
                    readFileContent(recordFile);
                    break;

                case 6:
                    appendRecord(directory, recordFile, scanner);
                    break;

                case 7:
                    deleteRecordFile(recordFile);
                    break;

                case 8:
                    System.out.println("Exiting Student Record File Manager.");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter 1 to 8.");
            }

        } while (choice != 8);

        scanner.close();
    }

    // 1. Create Records Directory
    public static void createDirectory(File directory) {

        try {
            if (!directory.exists()) {

                if (directory.mkdir()) {
                    System.out.println("Directory created: "
                            + directory.getPath());
                } else {
                    System.out.println("Failed to create directory.");
                }

            } else {
                System.out.println("Directory already exists: "
                        + directory.getPath());
            }

        } catch (SecurityException e) {
            System.out.println("Error creating directory: "
                    + e.getMessage());
        }
    }

    // 2. Create Record File
    public static void createRecordFile(File directory, File recordFile) {

        try {

            // Make sure directory exists first
            if (!directory.exists()) {
                if (!directory.mkdir()) {
                    System.out.println("Failed to create directory.");
                    return;
                }
            }

            if (!recordFile.exists()) {

                if (recordFile.createNewFile()) {
                    System.out.println("File created: "
                            + recordFile.getPath());
                } else {
                    System.out.println("Failed to create record file.");
                }

            } else {
                System.out.println("Record file already exists: "
                        + recordFile.getPath());
            }

        } catch (IOException | SecurityException e) {
            System.out.println("Error creating file: "
                    + e.getMessage());
        }
    }

    // 3. Write Student Record
    public static void writeRecord(
            File directory,
            File recordFile,
            Scanner scanner) {

        try {

            if (!directory.exists()) {
                directory.mkdir();
            }

            if (!recordFile.exists()) {
                recordFile.createNewFile();
            }

            System.out.print("Enter student record: ");
            String record = scanner.nextLine();

            FileOutputStream outputStream =
                    new FileOutputStream(recordFile);

            outputStream.write((record + System.lineSeparator()).getBytes());

            outputStream.close();

            System.out.println("Record written successfully.");

        } catch (IOException | SecurityException e) {
            System.out.println("Error writing record: "
                    + e.getMessage());
        }
    }

    // 4. Display File Information
    public static void displayFileInformation(
            File recordFile,
            File directory) {

        try {

            if (!recordFile.exists()) {
                System.out.println("Record file does not exist.");
                return;
            }

            System.out.println();
            System.out.println("===== File Information =====");

            System.out.println("Name : " + recordFile.getName());
            System.out.println("Path : " + recordFile.getPath());
            System.out.println("Absolute Path : "
                    + recordFile.getAbsolutePath());
            System.out.println("Size : " + recordFile.length() + " bytes");
            System.out.println("Is File : " + recordFile.isFile());
            System.out.println("Is Directory : " + recordFile.isDirectory());

        } catch (SecurityException e) {
            System.out.println("Error displaying file information: "
                    + e.getMessage());
        }
    }

    // 5. Read File Content
    public static void readFileContent(File recordFile) {

        FileInputStream inputStream = null;

        try {

            if (!recordFile.exists()) {
                System.out.println("Record file does not exist.");
                return;
            }

            inputStream = new FileInputStream(recordFile);

            System.out.println();
            System.out.println("===== File Content =====");

            int data;

            // Read byte by byte until read() returns -1
            while ((data = inputStream.read()) != -1) {
                System.out.print((char) data);
            }

            System.out.println();

        } catch (IOException | SecurityException e) {
            System.out.println("Error reading file: "
                    + e.getMessage());

        } finally {

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing input stream: "
                        + e.getMessage());
            }
        }
    }

    // 6. Append New Record
    public static void appendRecord(
            File directory,
            File recordFile,
            Scanner scanner) {

        try {

            if (!directory.exists()) {
                directory.mkdir();
            }

            if (!recordFile.exists()) {
                recordFile.createNewFile();
            }

            System.out.print("Enter new student record: ");
            String record = scanner.nextLine();

            // true enables append mode
            FileOutputStream outputStream =
                    new FileOutputStream(recordFile, true);

            outputStream.write((record + System.lineSeparator()).getBytes());

            outputStream.close();

            System.out.println("Record appended successfully.");

        } catch (IOException | SecurityException e) {
            System.out.println("Error appending record: "
                    + e.getMessage());
        }
    }

    // 7. Delete Record File
    public static void deleteRecordFile(File recordFile) {

        try {

            if (!recordFile.exists()) {
                System.out.println("Record file does not exist.");
                return;
            }

            if (recordFile.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("File deletion failed.");
            }

        } catch (SecurityException e) {
            System.out.println("Error deleting file: "
                    + e.getMessage());
        }
    }
}