import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentFeedbackForm extends JFrame implements ActionListener {

    // Form components
    private JTextField nameField;
    private JTextField courseField;
    private JTextField ratingField;
    private JTextField commentsField;

    private JTextArea feedbackArea;

    // Buttons and menu items
    private JButton submitButton;

    private JMenuItem saveFeedbackItem;
    private JMenuItem clearFormItem;
    private JMenuItem exitItem;
    private JMenuItem aboutItem;

    public StudentFeedbackForm() {

        // =========================
        // FRAME SETUP
        // =========================
        setTitle("Student Feedback Form");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout: BorderLayout
        setLayout(new BorderLayout(10, 10));

        // =========================
        // MENU BAR
        // =========================
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        saveFeedbackItem = new JMenuItem("Save Feedback");
        clearFormItem = new JMenuItem("Clear Form");
        exitItem = new JMenuItem("Exit");

        aboutItem = new JMenuItem("About");

        // Add menu items
        fileMenu.add(saveFeedbackItem);
        fileMenu.add(clearFormItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(
                BorderFactory.createTitledBorder("Student Feedback Details")
        );

        JLabel nameLabel = new JLabel("Name:");
        JLabel courseLabel = new JLabel("Course:");
        JLabel ratingLabel = new JLabel("Rating:");
        JLabel commentsLabel = new JLabel("Comments:");

        nameField = new JTextField();
        courseField = new JTextField();
        ratingField = new JTextField();
        commentsField = new JTextField();

        formPanel.add(nameLabel);
        formPanel.add(nameField);

        formPanel.add(courseLabel);
        formPanel.add(courseField);

        formPanel.add(ratingLabel);
        formPanel.add(ratingField);

        formPanel.add(commentsLabel);
        formPanel.add(commentsField);

        // =========================
        // SUBMIT BUTTON
        // =========================

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        submitButton = new JButton("Submit Feedback");
        buttonPanel.add(submitButton);

        // =========================
        // FEEDBACK AREA
        // =========================

        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(feedbackArea);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Submitted Feedback")
        );

        // =========================
        // ADD TO FRAME
        // =========================

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Make feedback area larger
        scrollPane.setPreferredSize(new Dimension(650, 220));

        // =========================
        // EVENT LISTENERS
        // =========================

        submitButton.addActionListener(this);

        saveFeedbackItem.addActionListener(this);
        clearFormItem.addActionListener(this);
        exitItem.addActionListener(this);
        aboutItem.addActionListener(this);

        // =========================
        // DISPLAY FRAME
        // =========================

        setVisible(true);
    }

    // =====================================================
    // SUBMIT / SAVE FEEDBACK
    // =====================================================

    private void submitFeedback() {

        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        String ratingText = ratingField.getText().trim();
        String comments = commentsField.getText().trim();

        // Validate name
        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Name cannot be empty.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );

            nameField.requestFocus();
            return;
        }

        // Validate course
        if (course.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Course cannot be empty.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );

            courseField.requestFocus();
            return;
        }

        // Validate rating
        try {

            int rating = Integer.parseInt(ratingText);

            if (rating < 1 || rating > 5) {

                JOptionPane.showMessageDialog(
                        this,
                        "Rating must be between 1 and 5.",
                        "Invalid Rating",
                        JOptionPane.ERROR_MESSAGE
                );

                ratingField.requestFocus();
                return;
            }

            if (comments.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Comments cannot be empty.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                commentsField.requestFocus();
                return;
            }

            // Format feedback
            String feedback =
                    "Name: " + name +
                    " | Course: " + course +
                    " | Rating: " + rating +
                    "/5 | Comments: " + comments;

            // Append feedback to JTextArea
            feedbackArea.append(feedback + "\n\n");

            JOptionPane.showMessageDialog(
                    this,
                    "Feedback added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Clear form after submission
            clearForm();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Rating must be a valid number.",
                    "Invalid Rating",
                    JOptionPane.ERROR_MESSAGE
            );

            ratingField.requestFocus();
        }
    }

    // =====================================================
    // CLEAR FORM
    // =====================================================

    private void clearForm() {

        nameField.setText("");
        courseField.setText("");
        ratingField.setText("");
        commentsField.setText("");

        nameField.requestFocus();

        JOptionPane.showMessageDialog(
                this,
                "Form fields cleared.",
                "Clear Form",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // EXIT APPLICATION
    // =====================================================

    private void exitApplication() {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {

            JOptionPane.showMessageDialog(
                    this,
                    "Thank you for using Student Feedback Form.",
                    "Exit",
                    JOptionPane.INFORMATION_MESSAGE
            );

            System.exit(0);
        }
    }

    // =====================================================
    // ABOUT DIALOG
    // =====================================================

    private void showAbout() {

        JOptionPane.showMessageDialog(
                this,
                "Student Feedback Form v1.0\n\n"
                        + "Submitted by: Sasanka Sekhar Kundu\n"
                        + "Purpose: Collect and display student feedback.",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // ACTION LISTENER
    // =====================================================

    @Override
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        // Submit button
        if (source == submitButton) {

            submitFeedback();

        }

        // File -> Save Feedback
        else if (source == saveFeedbackItem) {

            submitFeedback();

        }

        // File -> Clear Form
        else if (source == clearFormItem) {

            clearForm();

        }

        // File -> Exit
        else if (source == exitItem) {

            exitApplication();

        }

        // Help -> About
        else if (source == aboutItem) {

            showAbout();
        }
    }

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new StudentFeedbackForm();
        });
    }
}