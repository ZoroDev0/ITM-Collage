import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentMarksView extends JFrame {

    JTextField rollField;
    JTextField nameField;
    JTextField marksField;

    JButton addButton;
    JButton updateButton;
    JButton deleteButton;
    JButton averageButton;

    JTable table;
    DefaultTableModel tableModel;

    JLabel averageLabel;

    public StudentMarksView() {

        setTitle("Student Marks Viewer");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        // ================= FORM PANEL =================

        JPanel formPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        formPanel.setBorder(
                BorderFactory.createTitledBorder("Student Details")
        );

        formPanel.add(new JLabel("Roll No:"));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(new JLabel("Marks:"));

        rollField = new JTextField();
        nameField = new JTextField();
        marksField = new JTextField();

        formPanel.add(rollField);
        formPanel.add(nameField);
        formPanel.add(marksField);

        add(formPanel, BorderLayout.NORTH);

        // ================= TABLE =================

        String[] columns = {
                "Roll No",
                "Name",
                "Marks"
        };

        tableModel = new DefaultTableModel(columns, 0);

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Student Records")
        );

        add(scrollPane, BorderLayout.CENTER);

        // ================= BUTTON PANEL =================

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        averageButton = new JButton("Show Average");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(averageButton);

        // ================= AVERAGE =================

        averageLabel = new JLabel("Class Average: --");

        averageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(averageLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void clearFields() {
        rollField.setText("");
        nameField.setText("");
        marksField.setText("");
    }
}