package gui;

import exception.StudentException;
import model.Student;
import service.FileHandler;
import service.SortService;
import service.SortService.SortCriteria;
import service.StudentManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static gui.UIConstants.*;

/**
 * RecordsPanel.java
 * Dedicated page for viewing, sorting, editing, deleting, undoing,
 * and paginating student records.
 * 
 * Demonstrates:
 * - ArrayList iteration & page slicing
 * - Sorting with custom Comparators
 * - Stack-based Undo system (Undo Last Action)
 * - Interactive Pagination (Prev, Next, Page Numbers, Page Size Selector)
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class RecordsPanel extends JPanel {

    private final StudentManager studentManager;
    private final FileHandler fileHandler;
    private final MainFrame parentFrame;

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel lblRecordCount;
    private JLabel lblUndoStatus;

    // Sorting controls
    private JComboBox<SortCriteria> cmbSortField;
    private JToggleButton btnSortOrder;

    // Pagination State & Controls
    private int currentPage = 1;
    private int pageSize = 8; // Default comfortable rows per page
    private JComboBox<Integer> cmbPageSize;
    private JPanel paginationButtonsPanel;

    private ArrayList<Student> currentList = new ArrayList<>();

    public RecordsPanel(StudentManager studentManager, FileHandler fileHandler, MainFrame parentFrame) {
        this.studentManager = studentManager;
        this.fileHandler = fileHandler;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(0, 12));
        setBackground(COLOR_CREAM);
        setBorder(new EmptyBorder(16, 26, 16, 26));

        initUI();
    }

    private void initUI() {
        // 1. Top Section: Title & Controls Bar (Sorting + Undo)
        JPanel topSection = new JPanel(new BorderLayout(14, 0));
        topSection.setOpaque(false);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel lblTitle = new JLabel("Student Records");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Manage student records with Pagination, Comparator sorting, inline editing, and Stack-based Undo.");
        lblSub.setFont(FONT_SUBTITLE);
        lblSub.setForeground(COLOR_PRIMARY_BROWN);

        titleBlock.add(lblTitle);
        titleBlock.add(lblSub);
        topSection.add(titleBlock, BorderLayout.WEST);

        // Sorting & Undo Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolbar.setOpaque(false);

        JLabel lblSort = new JLabel("Sort By:");
        lblSort.setFont(FONT_LABEL);
        lblSort.setForeground(COLOR_PRIMARY_DARK);

        cmbSortField = new JComboBox<>(SortCriteria.values());
        cmbSortField.setFont(FONT_INPUT);
        cmbSortField.setBackground(COLOR_CREAM_CARD);
        cmbSortField.setForeground(COLOR_PRIMARY_DARK);
        cmbSortField.setPreferredSize(new Dimension(120, 32));
        cmbSortField.addActionListener(e -> {
            currentPage = 1; // Reset to page 1 on sort change
            applySort();
        });

        btnSortOrder = new JToggleButton("Ascending");
        btnSortOrder.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnSortOrder.setBackground(COLOR_CREAM_CARD);
        btnSortOrder.setForeground(COLOR_PRIMARY_DARK);
        btnSortOrder.setFocusPainted(false);
        btnSortOrder.setPreferredSize(new Dimension(105, 32));
        btnSortOrder.addActionListener(e -> {
            btnSortOrder.setText(btnSortOrder.isSelected() ? "Descending" : "Ascending");
            currentPage = 1;
            applySort();
        });

        // Undo Button (Demonstrates Stack)
        WarmButton btnUndo = new WarmButton("Undo Last Action", COLOR_PRIMARY_DARK, COLOR_CREAM, VectorIcons.createUndoIcon(14, 14, COLOR_CREAM));
        btnUndo.setPreferredSize(new Dimension(150, 34));
        btnUndo.setToolTipText("Reverses the last Add, Update, or Delete operation using Stack (LIFO)");
        btnUndo.addActionListener(e -> handleUndo());

        toolbar.add(lblSort);
        toolbar.add(cmbSortField);
        toolbar.add(btnSortOrder);
        toolbar.add(Box.createHorizontalStrut(6));
        toolbar.add(btnUndo);

        topSection.add(toolbar, BorderLayout.EAST);
        add(topSection, BorderLayout.NORTH);

        // 2. Table Container
        WarmCard tableCard = new WarmCard(12);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(12, 14, 12, 14));

        String[] columns = {"#", "Student ID", "Name", "Course", "Semester", "Marks", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setFont(FONT_TABLE);
        studentTable.setRowHeight(38);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setShowVerticalLines(false);
        studentTable.setShowHorizontalLines(true);
        studentTable.setGridColor(COLOR_NEUTRAL);
        studentTable.setSelectionBackground(new Color(0xDC, 0xD0, 0xBF));
        studentTable.setSelectionForeground(COLOR_PRIMARY_DARK);

        JTableHeader header = studentTable.getTableHeader();
        header.setFont(FONT_TABLE_HEAD);
        header.setBackground(COLOR_CREAM_CARD);
        header.setForeground(COLOR_PRIMARY_DARK);
        header.setPreferredSize(new Dimension(0, 36));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_NEUTRAL));

        // Column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(36);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(90);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(160);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(90);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        studentTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);

        // Marks Pill
        studentTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
                p.setOpaque(true);
                p.setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? COLOR_CREAM_CARD : COLOR_CREAM_INPUT));

                JLabel pill = new JLabel(String.valueOf(value), SwingConstants.CENTER) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(COLOR_MARKS_BADGE);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                pill.setPreferredSize(new Dimension(38, 24));
                pill.setFont(new Font("Segoe UI", Font.BOLD, 11));
                pill.setForeground(COLOR_MARKS_TEXT);
                p.add(pill);
                return p;
            }
        });

        // Actions (Edit & Delete buttons)
        studentTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
                p.setOpaque(true);
                p.setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? COLOR_CREAM_CARD : COLOR_CREAM_INPUT));

                JLabel btnEdit = new JLabel(new ImageIcon(VectorIcons.createPencilIcon(11, 11, COLOR_PRIMARY_DARK)), SwingConstants.CENTER);
                btnEdit.setOpaque(true);
                btnEdit.setBackground(new Color(0xD8, 0xCB, 0xBA));
                btnEdit.setPreferredSize(new Dimension(24, 24));

                JLabel btnDel = new JLabel(new ImageIcon(VectorIcons.createTrashIcon(11, 11, COLOR_ACCENT_DELETE)), SwingConstants.CENTER);
                btnDel.setOpaque(true);
                btnDel.setBackground(new Color(0xE5, 0xC4, 0xBD));
                btnDel.setPreferredSize(new Dimension(24, 24));

                p.add(btnEdit);
                p.add(btnDel);
                return p;
            }
        });

        // Click listener for inline actions taking pagination slice into account!
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                int col = studentTable.columnAtPoint(e.getPoint());

                int actualIndex = (currentPage - 1) * pageSize + row;

                if (row >= 0 && actualIndex >= 0 && actualIndex < currentList.size()) {
                    Student selected = currentList.get(actualIndex);
                    if (col == 6) {
                        int cellX = e.getX() - studentTable.getCellRect(row, col, false).x;
                        if (cellX > 40) {
                            handleDelete(selected);
                        } else {
                            handleEditDialog(selected);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, COLOR_NEUTRAL));
        scrollPane.getViewport().setBackground(COLOR_CREAM_CARD);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        // 3. Bottom Section: Summary, Page Size Selector, Undo Status, and Pagination Controls
        JPanel bottomBar = new JPanel(new BorderLayout(14, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(4, 4, 4, 4));

        // Left Box: Record Range Summary + Rows Per Page Selector
        JPanel leftBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftBox.setOpaque(false);

        lblRecordCount = new JLabel("Showing 0 records");
        lblRecordCount.setFont(FONT_SMALL);
        lblRecordCount.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblRowsPerPage = new JLabel("Rows per page:");
        lblRowsPerPage.setFont(FONT_SMALL);
        lblRowsPerPage.setForeground(COLOR_PRIMARY_BROWN);

        cmbPageSize = new JComboBox<>(new Integer[]{5, 8, 10, 15, 20});
        cmbPageSize.setSelectedItem(pageSize);
        cmbPageSize.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cmbPageSize.setBackground(COLOR_CREAM_CARD);
        cmbPageSize.setForeground(COLOR_PRIMARY_DARK);
        cmbPageSize.setPreferredSize(new Dimension(58, 26));
        cmbPageSize.addActionListener(e -> {
            Integer selected = (Integer) cmbPageSize.getSelectedItem();
            if (selected != null && selected != pageSize) {
                pageSize = selected;
                currentPage = 1;
                populateTableRows();
            }
        });

        leftBox.add(lblRecordCount);
        leftBox.add(Box.createHorizontalStrut(8));
        leftBox.add(lblRowsPerPage);
        leftBox.add(cmbPageSize);

        // Center Box: Undo Stack Status
        lblUndoStatus = new JLabel("Stack Status: 0 action(s)");
        lblUndoStatus.setFont(FONT_SMALL);
        lblUndoStatus.setForeground(COLOR_MUTED_BROWN);

        // Right Box: Interactive Pagination (❮ Prev, 1, 2, 3..., Next ❯)
        paginationButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        paginationButtonsPanel.setOpaque(false);

        bottomBar.add(leftBox, BorderLayout.WEST);
        bottomBar.add(lblUndoStatus, BorderLayout.CENTER);
        bottomBar.add(paginationButtonsPanel, BorderLayout.EAST);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private void applySort() {
        SortCriteria criteria = (SortCriteria) cmbSortField.getSelectedItem();
        boolean ascending = !btnSortOrder.isSelected();
        SortService.sort(currentList, criteria, ascending);
        populateTableRows();
    }

    private void handleUndo() {
        try {
            String msg = studentManager.undoLastAction();
            fileHandler.saveStudents(studentManager.getAllStudents());
            parentFrame.refreshAllPanels();
            JOptionPane.showMessageDialog(this, msg, "Undo Completed", JOptionPane.INFORMATION_MESSAGE);
        } catch (StudentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Undo Notice", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleEditDialog(Student s) {
        JTextField fName = new JTextField(s.getName());
        JTextField fCourse = new JTextField(s.getCourse());
        JTextField fSem = new JTextField(String.valueOf(s.getSemester()));
        JTextField fMarks = new JTextField(String.valueOf(s.getMarks()));

        JPanel editPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        editPanel.add(new JLabel("Name:"));
        editPanel.add(fName);
        editPanel.add(new JLabel("Course:"));
        editPanel.add(fCourse);
        editPanel.add(new JLabel("Semester:"));
        editPanel.add(fSem);
        editPanel.add(new JLabel("Marks:"));
        editPanel.add(fMarks);

        int result = JOptionPane.showConfirmDialog(this, editPanel, "Update Student ID: " + s.getId(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int sem = Integer.parseInt(fSem.getText().trim());
                double marks = Double.parseDouble(fMarks.getText().trim());
                Student updated = new Student(s.getId(), fName.getText().trim(), fCourse.getText().trim(), sem, marks);

                studentManager.updateStudent(updated);
                fileHandler.saveStudents(studentManager.getAllStudents());
                parentFrame.refreshAllPanels();
                JOptionPane.showMessageDialog(this, "Student ID " + s.getId() + " updated successfully.", "Updated", JOptionPane.INFORMATION_MESSAGE);
            } catch (StudentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Semester and Marks must be valid numbers.", "Format Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void handleDelete(Student s) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete student '" + s.getName() + "' (ID: " + s.getId() + ")?\nThis action can be reversed via 'Undo Last Action'.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studentManager.deleteStudent(s.getId());
                fileHandler.saveStudents(studentManager.getAllStudents());
                parentFrame.refreshAllPanels();
                JOptionPane.showMessageDialog(this, "Student deleted. You can undo this action using the Undo button.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            } catch (StudentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Slices the current list for pagination and rebuilds pagination buttons.
     */
    private void populateTableRows() {
        tableModel.setRowCount(0);
        int totalRecords = currentList.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalRecords / pageSize));

        // Clamp currentPage
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalRecords);

        // Populate table slice
        for (int i = startIndex; i < endIndex; i++) {
            Student s = currentList.get(i);
            String marksStr = (s.getMarks() == (long) s.getMarks()) ?
                    String.valueOf((long) s.getMarks()) : String.format("%.2f", s.getMarks());

            tableModel.addRow(new Object[]{
                    String.valueOf(i + 1),
                    s.getId(),
                    s.getName(),
                    s.getCourse(),
                    String.valueOf(s.getSemester()),
                    marksStr,
                    "actions"
            });
        }

        // Update Summary Label
        if (totalRecords == 0) {
            lblRecordCount.setText("No student records found.");
        } else {
            lblRecordCount.setText("Showing " + (startIndex + 1) + "–" + endIndex + " of " + totalRecords + " records (Page " + currentPage + " of " + totalPages + ")");
        }

        lblUndoStatus.setText("Stack Status: " + studentManager.getUndoManager().getUndoCount() + " action(s) available to undo");

        // Rebuild Pagination Buttons
        rebuildPaginationControls(totalPages);
    }

    /**
     * Builds interactive Prev, Page 1, Page 2, Next buttons dynamically.
     */
    private void rebuildPaginationControls(int totalPages) {
        paginationButtonsPanel.removeAll();

        // 1. Previous Button
        WarmMiniButton btnPrev = new WarmMiniButton("❮");
        btnPrev.setToolTipText("Previous Page");
        btnPrev.setEnabled(currentPage > 1);
        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                populateTableRows();
            }
        });
        paginationButtonsPanel.add(btnPrev);

        // 2. Numbered Page Buttons
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, startPage + 4);
        if (endPage - startPage < 4) {
            startPage = Math.max(1, endPage - 4);
        }

        for (int p = startPage; p <= endPage; p++) {
            final int pageNumber = p;
            boolean isActive = (p == currentPage);
            WarmMiniButton pageBtn = new WarmMiniButton(String.valueOf(p), isActive);
            pageBtn.addActionListener(e -> {
                if (currentPage != pageNumber) {
                    currentPage = pageNumber;
                    populateTableRows();
                }
            });
            paginationButtonsPanel.add(pageBtn);
        }

        // 3. Next Button
        WarmMiniButton btnNext = new WarmMiniButton("❯");
        btnNext.setToolTipText("Next Page");
        btnNext.setEnabled(currentPage < totalPages);
        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                populateTableRows();
            }
        });
        paginationButtonsPanel.add(btnNext);

        paginationButtonsPanel.revalidate();
        paginationButtonsPanel.repaint();
    }

    public void refreshData() {
        this.currentList = studentManager.getAllStudents();
        applySort();
    }
}
