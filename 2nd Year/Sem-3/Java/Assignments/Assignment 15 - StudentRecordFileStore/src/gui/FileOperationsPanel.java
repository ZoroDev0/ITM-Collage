package gui;

import exception.StudentException;
import service.FileHandler;
import service.StudentManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

import static gui.UIConstants.*;

/**
 * FileOperationsPanel.java
 * Dedicated page for file persistence operations.
 * Demonstrates:
 * - Java File Handling (BufferedReader, BufferedWriter)
 * - Persisting ArrayList<Student> to data/students.txt
 * - Error and recovery reporting
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class FileOperationsPanel extends JPanel {

    private final StudentManager studentManager;
    private final FileHandler fileHandler;
    private final MainFrame parentFrame;

    private JLabel lblPath;
    private JLabel lblFileExists;
    private JLabel lblFileSize;
    private JLabel lblRecordCount;
    private JTextArea txtLog;

    public FileOperationsPanel(StudentManager studentManager, FileHandler fileHandler, MainFrame parentFrame) {
        this.studentManager = studentManager;
        this.fileHandler = fileHandler;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(0, 16));
        setBackground(COLOR_CREAM);
        setBorder(new EmptyBorder(20, 26, 20, 26));

        initUI();
    }

    private void initUI() {
        // Top Header
        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel lblTitle = new JLabel("File Operations & Persistence");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Manage persistence storage with standard Java I/O (FileReader, BufferedReader, FileWriter, BufferedWriter).");
        lblSub.setFont(FONT_SUBTITLE);
        lblSub.setForeground(COLOR_PRIMARY_BROWN);

        titleBlock.add(lblTitle);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(lblSub);

        add(titleBlock, BorderLayout.NORTH);

        // Center Split: Info & Controls on Left, File Status Log on Right
        JPanel centerSplit = new JPanel(new GridLayout(1, 2, 16, 0));
        centerSplit.setOpaque(false);

        // Left Card: File Details & Actions
        WarmCard leftCard = new WarmCard(12);
        leftCard.setLayout(new BorderLayout(0, 14));
        leftCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel leftTitle = new JPanel();
        leftTitle.setLayout(new BoxLayout(leftTitle, BoxLayout.Y_AXIS));
        leftTitle.setOpaque(false);

        JLabel lblStorage = new JLabel("Storage Metadata");
        lblStorage.setFont(FONT_SECTION);
        lblStorage.setForeground(COLOR_PRIMARY_DARK);
        leftTitle.add(lblStorage);
        leftCard.add(leftTitle, BorderLayout.NORTH);

        // Metadata grid
        JPanel metaGrid = new JPanel(new GridLayout(4, 2, 8, 10));
        metaGrid.setOpaque(false);

        lblPath = new JLabel(fileHandler.getFilePath());
        lblFileExists = new JLabel("Yes");
        lblFileSize = new JLabel("0 bytes");
        lblRecordCount = new JLabel(String.valueOf(studentManager.getStudentCount()));

        addMetaRow(metaGrid, "Storage Target:", lblPath);
        addMetaRow(metaGrid, "File Exists:", lblFileExists);
        addMetaRow(metaGrid, "File Size:", lblFileSize);
        addMetaRow(metaGrid, "In-Memory Records:", lblRecordCount);

        leftCard.add(metaGrid, BorderLayout.CENTER);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);

        WarmButton btnSave = new WarmButton("Save to File", COLOR_PRIMARY_DARK, COLOR_CREAM, VectorIcons.createDiskIcon(14, 14, COLOR_CREAM));
        WarmButton btnReload = new WarmButton("Reload from File", COLOR_NEUTRAL, COLOR_PRIMARY_DARK, VectorIcons.createRefreshIcon(14, 14, COLOR_PRIMARY_DARK));

        btnSave.setPreferredSize(new Dimension(135, 36));
        btnReload.setPreferredSize(new Dimension(145, 36));

        btnSave.addActionListener(e -> handleSave());
        btnReload.addActionListener(e -> handleReload());

        btnRow.add(btnSave);
        btnRow.add(btnReload);

        leftCard.add(btnRow, BorderLayout.SOUTH);

        // Right Card: File Activity Log
        WarmCard rightCard = new WarmCard(12);
        rightCard.setLayout(new BorderLayout(0, 10));
        rightCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel lblLogTitle = new JLabel("I/O Activity Console");
        lblLogTitle.setFont(FONT_SECTION);
        lblLogTitle.setForeground(COLOR_PRIMARY_DARK);
        rightCard.add(lblLogTitle, BorderLayout.NORTH);

        txtLog = new JTextArea();
        txtLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtLog.setBackground(COLOR_CREAM_INPUT);
        txtLog.setForeground(COLOR_PRIMARY_DARK);
        txtLog.setEditable(false);
        txtLog.setBorder(new EmptyBorder(8, 10, 8, 10));

        JScrollPane logScroll = new JScrollPane(txtLog);
        logScroll.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, COLOR_NEUTRAL));

        rightCard.add(logScroll, BorderLayout.CENTER);

        centerSplit.add(leftCard);
        centerSplit.add(rightCard);

        add(centerSplit, BorderLayout.CENTER);
    }

    private void addMetaRow(JPanel panel, String label, JLabel valueLabel) {
        JLabel l = new JLabel(label);
        l.setFont(FONT_LABEL);
        l.setForeground(COLOR_PRIMARY_BROWN);

        valueLabel.setFont(FONT_INPUT);
        valueLabel.setForeground(COLOR_PRIMARY_DARK);

        panel.add(l);
        panel.add(valueLabel);
    }

    private void handleSave() {
        try {
            fileHandler.saveStudents(studentManager.getAllStudents());
            appendLog("SUCCESS: Saved " + studentManager.getStudentCount() + " records to " + fileHandler.getFilePath());
            refreshData();
            JOptionPane.showMessageDialog(this, "All records have been saved to " + fileHandler.getFilePath() + "!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (StudentException ex) {
            appendLog("ERROR: Failed to save: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleReload() {
        try {
            var loaded = fileHandler.loadStudents();
            studentManager.setStudents(loaded);
            appendLog("SUCCESS: Reloaded " + loaded.size() + " records from " + fileHandler.getFilePath());
            parentFrame.refreshAllPanels();
            JOptionPane.showMessageDialog(this, "Reloaded " + loaded.size() + " records from " + fileHandler.getFilePath() + "!", "Reloaded", JOptionPane.INFORMATION_MESSAGE);
        } catch (StudentException ex) {
            appendLog("ERROR: Failed to reload: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Reload Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void appendLog(String msg) {
        txtLog.append(msg + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    public void refreshData() {
        File f = new File(fileHandler.getFilePath());
        lblFileExists.setText(f.exists() ? "Yes (" + f.getAbsolutePath() + ")" : "No (will create)");
        lblFileSize.setText(f.exists() ? f.length() + " bytes" : "0 bytes");
        lblRecordCount.setText(studentManager.getStudentCount() + " active student(s)");
    }
}
