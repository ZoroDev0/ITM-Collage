package gui;

import model.Person;
import model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static gui.UIConstants.*;

/**
 * AboutPanel.java
 * Dedicated page summarizing the project metadata and viva concepts checklist.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class AboutPanel extends JPanel {

    private final MainFrame parentFrame;

    public AboutPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(0, 16));
        setBackground(COLOR_CREAM);
        setBorder(new EmptyBorder(20, 26, 20, 26));

        initUI();
    }

    private void initUI() {
        JPanel mainBox = new JPanel();
        mainBox.setLayout(new BoxLayout(mainBox, BoxLayout.Y_AXIS));
        mainBox.setOpaque(false);

        // Header Title
        JLabel lblTitle = new JLabel("About Student Record File Store");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("College Mini-Project Demonstration of Java OOP, DSA, and Core Java Principles.");
        lblSub.setFont(FONT_SUBTITLE);
        lblSub.setForeground(COLOR_PRIMARY_BROWN);

        mainBox.add(lblTitle);
        mainBox.add(Box.createVerticalStrut(4));
        mainBox.add(lblSub);
        mainBox.add(Box.createVerticalStrut(18));

        // Author Card
        WarmCard authorCard = new WarmCard(12);
        authorCard.setLayout(new BorderLayout(14, 0));
        authorCard.setBorder(new EmptyBorder(16, 20, 16, 20));
        authorCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        JLabel avatar = new JLabel("SK", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PRIMARY_BROWN);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setPreferredSize(new Dimension(46, 46));
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        avatar.setForeground(COLOR_CREAM);

        JPanel authorText = new JPanel();
        authorText.setLayout(new BoxLayout(authorText, BoxLayout.Y_AXIS));
        authorText.setOpaque(false);

        JLabel lblName = new JLabel("Sasanka Sekhar Kundu");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblName.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblRoll = new JLabel("Roll Number: 150096725118  |  Java 21 (OpenJDK 21.0.10)  |  Java Swing");
        lblRoll.setFont(FONT_INPUT);
        lblRoll.setForeground(COLOR_PRIMARY_BROWN);

        JLabel lblMotto = new JLabel("\"Code Today, A Better Tomorrow.\"");
        lblMotto.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblMotto.setForeground(COLOR_MUTED_BROWN);

        authorText.add(lblName);
        authorText.add(Box.createVerticalStrut(2));
        authorText.add(lblRoll);
        authorText.add(Box.createVerticalStrut(2));
        authorText.add(lblMotto);

        authorCard.add(avatar, BorderLayout.WEST);
        authorCard.add(authorText, BorderLayout.CENTER);

        mainBox.add(authorCard);
        mainBox.add(Box.createVerticalStrut(16));

        // 3 Pillars Checklist (OOP, DSA, Core Java)
        JPanel pillarsGrid = new JPanel(new GridLayout(1, 3, 16, 0));
        pillarsGrid.setOpaque(false);

        pillarsGrid.add(createConceptCard("OOP Concepts", new String[]{
                "Encapsulation (Private fields, getters/setters)",
                "Abstraction (Abstract Person base class)",
                "Inheritance (Student extends Person)",
                "Polymorphism (Runtime displayInfo() dispatch)",
                "Interface (Validatable contract)",
                "Method Overriding (displayInfo(), toString())"
        }));

        pillarsGrid.add(createConceptCard("DSA Concepts", new String[]{
                "ArrayList (Primary Student store in-memory)",
                "Stack (UndoManager for LIFO mutation undo)",
                "Queue (OperationQueue for FIFO transactions)",
                "Searching (Linear Search & Binary Search)",
                "Sorting (Custom Comparators ID/Name/Marks)"
        }));

        pillarsGrid.add(createConceptCard("Core Java & UI", new String[]{
                "File Handling (BufferedReader & BufferedWriter)",
                "Persistence (data/students.txt storage)",
                "Exception Handling (Custom StudentException)",
                "Multi-Page GUI (Single MainFrame CardLayout)",
                "Authentication (admin / admin123 Login)",
                "Subpixel Antialiasing & Vector Icons"
        }));

        mainBox.add(pillarsGrid);
        mainBox.add(Box.createVerticalStrut(16));

        // Polymorphism Test Action Card
        WarmCard polyCard = new WarmCard(12);
        polyCard.setLayout(new BorderLayout(12, 0));
        polyCard.setBorder(new EmptyBorder(14, 20, 14, 20));
        polyCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel lblPolyDesc = new JLabel("<html><b>Viva Proof:</b> Click to execute runtime polymorphic dispatch (<code>Person person = new Student(...)</code>)</html>");
        lblPolyDesc.setFont(FONT_INPUT);
        lblPolyDesc.setForeground(COLOR_PRIMARY_DARK);

        WarmButton btnTestPoly = new WarmButton("Test Polymorphism", COLOR_PRIMARY_BROWN, COLOR_CREAM, VectorIcons.createGraduationCap(14, 14, COLOR_CREAM));
        btnTestPoly.setPreferredSize(new Dimension(170, 34));
        btnTestPoly.addActionListener(e -> {
            // Polymorphic dispatch demonstration
            Person polymorphicStudent = new Student("101", "Rahul Sharma", "B.Tech CSE", 2, 85.0);
            String output = polymorphicStudent.displayInfo(); // invokes Student.displayInfo()
            JOptionPane.showMessageDialog(this,
                    "Runtime Polymorphism Executed Successfully:\n\n" +
                    "Reference Type: Person\n" +
                    "Actual Object:  Student\n" +
                    "Dispatched:     " + output,
                    "Polymorphism Proof",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        polyCard.add(lblPolyDesc, BorderLayout.CENTER);
        polyCard.add(btnTestPoly, BorderLayout.EAST);

        mainBox.add(polyCard);

        add(mainBox, BorderLayout.NORTH);
    }

    private JPanel createConceptCard(String title, String[] items) {
        WarmCard card = new WarmCard(12);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_SECTION);
        lblTitle.setForeground(COLOR_PRIMARY_DARK);
        card.add(lblTitle, BorderLayout.NORTH);

        JPanel itemsCol = new JPanel();
        itemsCol.setLayout(new BoxLayout(itemsCol, BoxLayout.Y_AXIS));
        itemsCol.setOpaque(false);

        for (String item : items) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
            row.setOpaque(false);

            JLabel check = new JLabel("✔");
            check.setFont(new Font("Segoe UI", Font.BOLD, 12));
            check.setForeground(new Color(0x3E, 0x6E, 0x3E));

            JLabel lblText = new JLabel(item);
            lblText.setFont(FONT_SMALL);
            lblText.setForeground(COLOR_PRIMARY_BROWN);

            row.add(check);
            row.add(lblText);
            itemsCol.add(row);
        }

        card.add(itemsCol, BorderLayout.CENTER);
        return card;
    }
}
