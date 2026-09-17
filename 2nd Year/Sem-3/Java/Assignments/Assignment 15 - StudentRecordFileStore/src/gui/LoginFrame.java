package gui;

import auth.AuthenticationManager;
import exception.StudentException;
import service.FileHandler;
import service.StudentManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static gui.UIConstants.*;

/**
 * LoginFrame.java
 * Authentication entry screen for Student Record File Store.
 * 
 * Default Credentials:
 * - Username: admin
 * - Password: admin123
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class LoginFrame extends JFrame {

    private final StudentManager studentManager;
    private final FileHandler fileHandler;
    private final AuthenticationManager authManager;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;

    public LoginFrame(StudentManager studentManager, FileHandler fileHandler, AuthenticationManager authManager) {
        this.studentManager = studentManager;
        this.fileHandler = fileHandler;
        this.authManager = authManager;

        initUI();
    }

    private void initUI() {
        setTitle("Student Record File Store - Sign In");
        setSize(480, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_CREAM);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(36, 42, 36, 42));

        // 1. Branding Icon & Title
        JLabel capIcon = new JLabel(new ImageIcon(VectorIcons.createGraduationCap(44, 44, COLOR_PRIMARY_DARK)));
        capIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAppTitle = new JLabel("Student Record File Store");
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblAppTitle.setForeground(COLOR_PRIMARY_DARK);
        lblAppTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Please enter your credentials to access the system");
        lblSub.setFont(FONT_SUBTITLE);
        lblSub.setForeground(COLOR_PRIMARY_BROWN);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(capIcon);
        centerPanel.add(Box.createVerticalStrut(12));
        centerPanel.add(lblAppTitle);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(lblSub);
        centerPanel.add(Box.createVerticalStrut(24));

        // 2. Login Card Container
        WarmCard card = new WarmCard(14);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        card.setMaximumSize(new Dimension(400, 270));

        // Username
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(FONT_LABEL);
        lblUser.setForeground(COLOR_PRIMARY_DARK);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = createStyledTextField("e.g. admin");
        txtUsername.setText(AuthenticationManager.DEFAULT_USERNAME);
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(FONT_LABEL);
        lblPass.setForeground(COLOR_PRIMARY_DARK);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = createStyledPasswordField();
        txtPassword.setText(AuthenticationManager.DEFAULT_PASSWORD);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        // Error message label
        lblError = new JLabel(" ");
        lblError.setFont(FONT_SMALL);
        lblError.setForeground(COLOR_ACCENT_DELETE);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Submit Button
        WarmButton btnLogin = new WarmButton("Sign In to Dashboard", COLOR_PRIMARY_DARK, COLOR_CREAM, VectorIcons.createHomeIcon(14, 14, COLOR_CREAM));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> handleLogin());

        card.add(lblUser);
        card.add(Box.createVerticalStrut(4));
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(12));
        card.add(lblPass);
        card.add(Box.createVerticalStrut(4));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(6));
        card.add(lblError);
        card.add(Box.createVerticalStrut(10));
        card.add(btnLogin);

        centerPanel.add(card);
        centerPanel.add(Box.createVerticalStrut(16));

        // 3. Credentials Hint & Metadata
        JPanel hintBox = new JPanel();
        hintBox.setLayout(new BoxLayout(hintBox, BoxLayout.Y_AXIS));
        hintBox.setOpaque(false);
        hintBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblHint1 = new JLabel("Default Access: admin  |  admin123");
        lblHint1.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblHint1.setForeground(COLOR_PRIMARY_BROWN);
        lblHint1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblHint2 = new JLabel("Sasanka Sekhar Kundu (150096725118) • Java 21");
        lblHint2.setFont(FONT_SMALL);
        lblHint2.setForeground(COLOR_MUTED_BROWN);
        lblHint2.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintBox.add(lblHint1);
        hintBox.add(Box.createVerticalStrut(2));
        hintBox.add(lblHint2);

        centerPanel.add(hintBox);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(FONT_INPUT);
        field.setBackground(COLOR_CREAM_INPUT);
        field.setForeground(COLOR_PRIMARY_DARK);
        field.setCaretColor(COLOR_PRIMARY_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_NEUTRAL, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(FONT_INPUT);
        field.setBackground(COLOR_CREAM_INPUT);
        field.setForeground(COLOR_PRIMARY_DARK);
        field.setCaretColor(COLOR_PRIMARY_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_NEUTRAL, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        try {
            authManager.authenticate(username, password);

            // Hide Login and Launch MainFrame
            dispose();
            SwingUtilities.invokeLater(() -> {
                MainFrame main = new MainFrame(studentManager, fileHandler, authManager);
                main.setVisible(true);
            });

        } catch (StudentException ex) {
            lblError.setText(ex.getMessage());
            txtPassword.setText("");
            txtPassword.requestFocusInWindow();
        }
    }
}
