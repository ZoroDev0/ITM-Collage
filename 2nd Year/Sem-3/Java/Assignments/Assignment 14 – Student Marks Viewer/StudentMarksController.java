import javax.swing.*;

public class StudentMarksController {

    private StudentMarksView view;

    public StudentMarksController(StudentMarksView view) {

        this.view = view;

        // Button event handling
        view.addButton.addActionListener(e -> addStudent());

        view.updateButton.addActionListener(e -> updateStudent());

        view.deleteButton.addActionListener(e -> deleteStudent());

        view.averageButton.addActionListener(e -> showAverage());

        // Add sample records
        addSampleData();
    }

    // ================= ADD =================

    private void addStudent() {

        try {

            String rollText = view.rollField.getText().trim();
            String name = view.nameField.getText().trim();
            String marksText = view.marksField.getText().trim();

            if (rollText.isEmpty() ||
                    name.isEmpty() ||
                    marksText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        view,
                        "Please fill all fields.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            int rollNo = Integer.parseInt(rollText);
            double marks = Double.parseDouble(marksText);

            if (marks < 0 || marks > 100) {

                JOptionPane.showMessageDialog(
                        view,
                        "Marks must be between 0 and 100.",
                        "Invalid Marks",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            StudentMarks student =
                    new StudentMarks(rollNo, name, marks);

            // Add to Model
            StudentMarks.addStudent(student);

            // Add to JTable
            view.tableModel.addRow(
                    new Object[]{
                            rollNo,
                            name,
                            marks
                    }
            );

            JOptionPane.showMessageDialog(
                    view,
                    "Student added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            view.clearFields();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    view,
                    "Roll No and Marks must be numeric.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= UPDATE =================

    private void updateStudent() {

        try {

            int selectedRow = view.table.getSelectedRow();

            if (selectedRow < 0) {
                throw new ArrayIndexOutOfBoundsException(
                        "No student row selected."
                );
            }

            String rollText = view.rollField.getText().trim();
            String name = view.nameField.getText().trim();
            String marksText = view.marksField.getText().trim();

            if (rollText.isEmpty() ||
                    name.isEmpty() ||
                    marksText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        view,
                        "Enter all details before updating.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            int rollNo = Integer.parseInt(rollText);
            double marks = Double.parseDouble(marksText);

            if (marks < 0 || marks > 100) {

                JOptionPane.showMessageDialog(
                        view,
                        "Marks must be between 0 and 100.",
                        "Invalid Marks",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // Update Model
            StudentMarks student =
                    StudentMarks.getStudents().get(selectedRow);

            student.setRollNo(rollNo);
            student.setName(name);
            student.setMarks(marks);

            // Update JTable
            view.tableModel.setValueAt(
                    rollNo,
                    selectedRow,
                    0
            );

            view.tableModel.setValueAt(
                    name,
                    selectedRow,
                    1
            );

            view.tableModel.setValueAt(
                    marks,
                    selectedRow,
                    2
            );

            JOptionPane.showMessageDialog(
                    view,
                    "Student record updated successfully.",
                    "Update Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            view.clearFields();

        } catch (ArrayIndexOutOfBoundsException e) {

            JOptionPane.showMessageDialog(
                    view,
                    "Please select a student row first.",
                    "Selection Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    view,
                    "Roll No and Marks must be numeric.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= DELETE =================

    private void deleteStudent() {

        try {

            int selectedRow = view.table.getSelectedRow();

            if (selectedRow < 0) {
                throw new ArrayIndexOutOfBoundsException(
                        "No student row selected."
                );
            }

            // Remove from Model
            StudentMarks.removeStudent(selectedRow);

            // Remove from JTable
            view.tableModel.removeRow(selectedRow);

            JOptionPane.showMessageDialog(
                    view,
                    "Student deleted successfully.",
                    "Delete Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            view.averageLabel.setText(
                    String.format(
                            "Class Average: %.2f",
                            StudentMarks.calculateAverage()
                    )
            );

        } catch (ArrayIndexOutOfBoundsException e) {

            JOptionPane.showMessageDialog(
                    view,
                    "Please select a student row first.",
                    "Selection Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= AVERAGE =================

    private void showAverage() {

        double average =
                StudentMarks.calculateAverage();

        view.averageLabel.setText(
                String.format(
                        "Class Average: %.2f",
                        average
                )
        );

        // Launch JavaFX preview
        JavaFXAveragePreview.showAverage(average);
    }

    // ================= SAMPLE DATA =================

    private void addSampleData() {

        StudentMarks student1 =
                new StudentMarks(101, "Rahul Sharma", 88);

        StudentMarks student2 =
                new StudentMarks(102, "Priya Verma", 92);

        StudentMarks student3 =
                new StudentMarks(103, "Aman Singh", 85);

        StudentMarks student4 =
                new StudentMarks(104, "Ananya Gupta", 95);

        StudentMarks.addStudent(student1);
        StudentMarks.addStudent(student2);
        StudentMarks.addStudent(student3);
        StudentMarks.addStudent(student4);

        view.tableModel.addRow(
                new Object[]{101, "Rahul Sharma", 88}
        );

        view.tableModel.addRow(
                new Object[]{102, "Priya Verma", 92}
        );

        view.tableModel.addRow(
                new Object[]{103, "Aman Singh", 85}
        );

        view.tableModel.addRow(
                new Object[]{104, "Ananya Gupta", 95}
        );
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            StudentMarksView view =
                    new StudentMarksView();

            new StudentMarksController(view);
        });
    }
}