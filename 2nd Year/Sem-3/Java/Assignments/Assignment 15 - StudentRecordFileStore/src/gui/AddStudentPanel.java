package gui;

import exception.StudentException;
import model.Student;
import service.FileHandler;
import service.StudentManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static gui.UIConstants.*;

/**
 * AddStudentPanel.java
 * Dedicated page for adding a new Student entity.
 * Form and controls are horizontally centered for optimal aesthetics on all screen resolutions.
 * 
 * Demonstrates:
 * - Interface validation (Validatable.validate())
 * - OOP encapsulation
 * - Queue and Stack tracking
 * - FileHandler disk persistence
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class AddStudentPanel extends JPanel {

    private final StudentManager studentManager;
    private final FileHandler fileHandler;
    private final MainFrame parentFrame;

    // Fields
    private FormInputField txtId;
    private FormInputField txtName;
    private FormInputField txtCourse;
    private FormInputField txtSemester;
    private FormInputField txtMarks;
    private JLabel lblStatus;

    public AddStudentPanel(StudentManager studentManager, FileHandler fileHandler, MainFrame parentFrame) {
        this.studentManager = studentManager;
        this.fileHandler = fileHandler;
        this.parentFrame = parentFrame;

        // GridBagLayout anchors the form container in the horizontal center
        setLayout(new GridBagLayout());
        setBackground(COLOR_CREAM);
        setBorder(new EmptyBorder(16, 24, 20, 24));

        initUI();
    }

    private void initUI() {
        JPanel centerBox = new JPanel();
        centerBox.setLayout(new BoxLayout(centerBox, BoxLayout.Y_AXIS));
        centerBox.setOpaque(false);
        centerBox.setPreferredSize(new Dimension(640, 520));
        centerBox.setMaximumSize(new Dimension(640, 520));

        // Header Title (Centered)
        JLabel lblTitle = new JLabel("Add New Student Record");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY_DARK);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Enters student information into memory (ArrayList), registers Queue/Stack, and persists to file.");
        lblSub.setFont(FONT_SUBTITLE);
        lblSub.setForeground(COLOR_PRIMARY_BROWN);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerBox.add(lblTitle);
        centerBox.add(Box.createVerticalStrut(4));
        centerBox.add(lblSub);
        centerBox.add(Box.createVerticalStrut(18));

        // Form Card Container (Centered)
        WarmCard formCard = new WarmCard(14);
        formCard.setLayout(new BorderLayout(0, 14));
        formCard.setBorder(new EmptyBorder(22, 28, 22, 28));
        formCard.setPreferredSize(new Dimension(640, 420));
        formCard.setMaximumSize(new Dimension(640, 420));
        formCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Grid of Inputs
        JPanel fieldsCol = new JPanel();
        fieldsCol.setLayout(new BoxLayout(fieldsCol, BoxLayout.Y_AXIS));
        fieldsCol.setOpaque(false);

        txtId = new FormInputField("Student ID", "e.g. 101", VectorIcons.FIELD_ID);
        txtName = new FormInputField("Student Name", "e.g. Sasanka Kundu", VectorIcons.FIELD_USER);
        txtCourse = new FormInputField("Course", "e.g. B.Tech CSE", VectorIcons.FIELD_BOOK);
        txtSemester = new FormInputField("Semester (1, 2, ...)", "e.g. 2", VectorIcons.FIELD_LAYERS);
        txtMarks = new FormInputField("Marks (0 - 100)", "e.g. 85.5", VectorIcons.FIELD_PERCENT);

        fieldsCol.add(txtId);
        fieldsCol.add(Box.createVerticalStrut(8));
        fieldsCol.add(txtName);
        fieldsCol.add(Box.createVerticalStrut(8));
        fieldsCol.add(txtCourse);
        fieldsCol.add(Box.createVerticalStrut(8));
        fieldsCol.add(txtSemester);
        fieldsCol.add(Box.createVerticalStrut(8));
        fieldsCol.add(txtMarks);

        formCard.add(fieldsCol, BorderLayout.CENTER);

        // Action Buttons Row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(new EmptyBorder(12, 0, 0, 0));

        WarmButton btnSubmit = new WarmButton("+ Add Student", COLOR_PRIMARY_DARK, COLOR_CREAM, VectorIcons.createPlusIcon(14, 14, COLOR_CREAM));
        WarmButton btnClear = new WarmButton("Clear Fields", COLOR_NEUTRAL, COLOR_PRIMARY_DARK, VectorIcons.createCrossIcon(12, 12, COLOR_PRIMARY_DARK));

        btnSubmit.setPreferredSize(new Dimension(140, 38));
        btnClear.setPreferredSize(new Dimension(120, 38));

        btnSubmit.addActionListener(e -> handleAddStudent());
        btnClear.addActionListener(e -> clearFields());

        btnRow.add(btnSubmit);
        btnRow.add(btnClear);

        formCard.add(btnRow, BorderLayout.SOUTH);

        centerBox.add(formCard);

        // Status Label at bottom
        lblStatus = new JLabel("Ready to accept input.");
        lblStatus.setFont(FONT_SMALL);
        lblStatus.setForeground(COLOR_MUTED_BROWN);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblStatus.setBorder(new EmptyBorder(10, 0, 0, 0));
        centerBox.add(lblStatus);

        // Add to parent with GridBag constraint anchored to top-center
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 0, 0, 0);

        add(centerBox, gbc);
    }

    private void handleAddStudent() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String course = txtCourse.getText().trim();
        String semStr = txtSemester.getText().trim();
        String marksStr = txtMarks.getText().trim();

        try {
            if (id.isEmpty()) throw new StudentException("Student ID cannot be empty.");
            if (name.isEmpty()) throw new StudentException("Student Name cannot be empty.");
            if (course.isEmpty()) throw new StudentException("Course cannot be empty.");
            if (semStr.isEmpty()) throw new StudentException("Semester cannot be empty.");
            if (marksStr.isEmpty()) throw new StudentException("Marks cannot be empty.");

            int sem;
            try {
                sem = Integer.parseInt(semStr);
            } catch (NumberFormatException e) {
                throw new StudentException("Semester must be a valid whole number (e.g., 1, 2, 3).");
            }

            double marks;
            try {
                marks = Double.parseDouble(marksStr);
            } catch (NumberFormatException e) {
                throw new StudentException("Marks must be a valid numeric value (e.g., 85 or 92.5).");
            }

            // Create Student (Polymorphic creation, inheritance, validation interface)
            Student student = new Student(id, name, course, sem, marks);

            // Add to StudentManager (invokes validation, FIFO queue, Stack, and ArrayList)
            studentManager.addStudent(student);

            // Persist to file
            fileHandler.saveStudents(studentManager.getAllStudents());

            lblStatus.setText("Student '" + student.getName() + "' (ID: " + student.getId() + ") added and persisted successfully.");
            clearFields();
            parentFrame.refreshAllPanels();

            JOptionPane.showMessageDialog(this,
                    "Student record added successfully!\n" + student.displayInfo(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (StudentException ex) {
            lblStatus.setText("Validation notice: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Validation", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            lblStatus.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtCourse.setText("");
        txtSemester.setText("");
        txtMarks.setText("");
        txtId.getTextField().requestFocusInWindow();
    }
}
