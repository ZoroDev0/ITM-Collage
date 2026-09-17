package gui;

import model.Student;
import service.FileHandler;
import service.StudentManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import static gui.UIConstants.*;

/**
 * DashboardPanel.java
 * High-level OVERVIEW page with KPIs, system health, paginated recent records,
 * and quick navigation shortcuts.
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class DashboardPanel extends JPanel {

    private final StudentManager studentManager;
    private final FileHandler fileHandler;
    private final MainFrame parentFrame;

    // KPI Labels
    private JLabel lblTotalStudents;
    private JLabel lblRecordsSaved;
    private JLabel lblFileStatus;
    private JLabel lblQueueStatus;

    // Recent Table & Pagination
    private DefaultTableModel recentTableModel;
    private JLabel lblRecentRange;
    private int recentPage = 1;
    private int recentPageSize = 5;
    private JPanel recentPaginationPanel;
    private JComboBox<Integer> cmbRecentPageSize;

    public DashboardPanel(StudentManager studentManager, FileHandler fileHandler, MainFrame parentFrame) {
        this.studentManager = studentManager;
        this.fileHandler = fileHandler;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(0, 18));
        setBackground(COLOR_CREAM);
        setBorder(new EmptyBorder(16, 24, 20, 24));

        initUI();
    }

    private void initUI() {
        // 1. Top Section: 4 Metric Cards in a Grid
        JPanel metricsGrid = new JPanel(new GridLayout(1, 4, 14, 0));
        metricsGrid.setOpaque(false);
        metricsGrid.setPreferredSize(new Dimension(0, 92));

        lblTotalStudents = new JLabel("0");
        lblRecordsSaved = new JLabel("0");
        lblFileStatus = new JLabel("Synchronized");
        lblQueueStatus = new JLabel("0 Pending");

        metricsGrid.add(createMetricCard("Total Students", lblTotalStudents, "Active in-memory", VectorIcons.createUsersIcon(22, 22, COLOR_PRIMARY_BROWN)));
        metricsGrid.add(createMetricCard("Records Saved", lblRecordsSaved, "In data/students.txt", VectorIcons.createDiskIcon(20, 20, COLOR_PRIMARY_BROWN)));
        metricsGrid.add(createMetricCard("File Store Status", lblFileStatus, "Normal read/write", VectorIcons.createFolderIcon(20, 20, COLOR_PRIMARY_BROWN)));
        metricsGrid.add(createMetricCard("Operation Queue", lblQueueStatus, "FIFO DSA queue", VectorIcons.createListIcon(20, 20, COLOR_PRIMARY_BROWN)));

        add(metricsGrid, BorderLayout.NORTH);

        // 2. Middle Section: Recent Students Preview (Left 65%) + Quick Actions Card (Right 35%)
        JPanel middleSplit = new JPanel(new BorderLayout(16, 0));
        middleSplit.setOpaque(false);

        // Recent Students Table Card
        WarmCard tableCard = new WarmCard(12);
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel tableHead = new JPanel(new BorderLayout());
        tableHead.setOpaque(false);

        JLabel lblRecentTitle = new JLabel("Recent Student Records");
        lblRecentTitle.setFont(FONT_SECTION);
        lblRecentTitle.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Paginated preview of latest student entries in ArrayList");
        lblSub.setFont(FONT_SMALL);
        lblSub.setForeground(COLOR_PRIMARY_BROWN);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);
        titleBlock.add(lblRecentTitle);
        titleBlock.add(lblSub);

        tableHead.add(titleBlock, BorderLayout.WEST);

        // Mini Table
        String[] cols = {"#", "Student ID", "Name", "Course", "Semester", "Marks"};
        recentTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable recentTable = new JTable(recentTableModel);
        recentTable.setFont(FONT_TABLE);
        recentTable.setRowHeight(34);
        recentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentTable.setShowVerticalLines(false);
        recentTable.setShowHorizontalLines(true);
        recentTable.setGridColor(COLOR_NEUTRAL);
        recentTable.getTableHeader().setFont(FONT_TABLE_HEAD);
        recentTable.getTableHeader().setBackground(COLOR_CREAM_CARD);
        recentTable.getTableHeader().setForeground(COLOR_PRIMARY_DARK);
        recentTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_NEUTRAL));

        // Column widths
        recentTable.getColumnModel().getColumn(0).setPreferredWidth(32);
        recentTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        recentTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        recentTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        recentTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        recentTable.getColumnModel().getColumn(5).setPreferredWidth(70);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        recentTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        recentTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);

        // Marks Pill Renderer
        recentTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
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
                pill.setPreferredSize(new Dimension(36, 22));
                pill.setFont(new Font("Segoe UI", Font.BOLD, 11));
                pill.setForeground(COLOR_MARKS_TEXT);
                p.add(pill);
                return p;
            }
        });

        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, COLOR_NEUTRAL));
        scrollPane.getViewport().setBackground(COLOR_CREAM_CARD);

        tableCard.add(tableHead, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Recent Table Bottom Pagination Bar
        JPanel recentBottomBar = new JPanel(new BorderLayout(10, 0));
        recentBottomBar.setOpaque(false);
        recentBottomBar.setBorder(new EmptyBorder(6, 2, 2, 2));

        // Left: Range + Rows selector
        JPanel leftRecentControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftRecentControls.setOpaque(false);

        lblRecentRange = new JLabel("Showing 0 records");
        lblRecentRange.setFont(FONT_SMALL);
        lblRecentRange.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblRows = new JLabel("Page size:");
        lblRows.setFont(FONT_SMALL);
        lblRows.setForeground(COLOR_PRIMARY_BROWN);

        cmbRecentPageSize = new JComboBox<>(new Integer[]{5, 10, 15});
        cmbRecentPageSize.setSelectedItem(recentPageSize);
        cmbRecentPageSize.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cmbRecentPageSize.setBackground(COLOR_CREAM_CARD);
        cmbRecentPageSize.setForeground(COLOR_PRIMARY_DARK);
        cmbRecentPageSize.setPreferredSize(new Dimension(54, 24));
        cmbRecentPageSize.addActionListener(e -> {
            Integer sel = (Integer) cmbRecentPageSize.getSelectedItem();
            if (sel != null && sel != recentPageSize) {
                recentPageSize = sel;
                recentPage = 1;
                populateRecentRows();
            }
        });

        leftRecentControls.add(lblRecentRange);
        leftRecentControls.add(Box.createHorizontalStrut(6));
        leftRecentControls.add(lblRows);
        leftRecentControls.add(cmbRecentPageSize);

        recentPaginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        recentPaginationPanel.setOpaque(false);

        recentBottomBar.add(leftRecentControls, BorderLayout.WEST);
        recentBottomBar.add(recentPaginationPanel, BorderLayout.EAST);

        tableCard.add(recentBottomBar, BorderLayout.SOUTH);

        // Quick Actions Card
        WarmCard actionsCard = new WarmCard(12);
        actionsCard.setPreferredSize(new Dimension(300, 0));
        actionsCard.setLayout(new BorderLayout(0, 12));
        actionsCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel lblActTitle = new JLabel("Quick Actions");
        lblActTitle.setFont(FONT_SECTION);
        lblActTitle.setForeground(COLOR_PRIMARY_DARK);
        actionsCard.add(lblActTitle, BorderLayout.NORTH);

        JPanel actionButtonsCol = new JPanel();
        actionButtonsCol.setLayout(new BoxLayout(actionButtonsCol, BoxLayout.Y_AXIS));
        actionButtonsCol.setOpaque(false);

        WarmButton btnGoAdd = new WarmButton("+ Add New Student", COLOR_PRIMARY_DARK, COLOR_CREAM, VectorIcons.createPlusIcon(14, 14, COLOR_CREAM));
        WarmButton btnGoRecords = new WarmButton("View All Records", COLOR_PRIMARY_BROWN, COLOR_CREAM, VectorIcons.createListIcon(14, 14, COLOR_CREAM));
        WarmButton btnGoSearch = new WarmButton("Search Directory", COLOR_NEUTRAL, COLOR_PRIMARY_DARK, VectorIcons.createSearchIcon(14, 14, COLOR_PRIMARY_DARK));

        btnGoAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnGoRecords.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnGoSearch.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnGoAdd.addActionListener(e -> parentFrame.navigateTo(MainFrame.PAGE_ADD));
        btnGoRecords.addActionListener(e -> parentFrame.navigateTo(MainFrame.PAGE_RECORDS));
        btnGoSearch.addActionListener(e -> parentFrame.navigateTo(MainFrame.PAGE_SEARCH));

        actionButtonsCol.add(btnGoAdd);
        actionButtonsCol.add(Box.createVerticalStrut(10));
        actionButtonsCol.add(btnGoRecords);
        actionButtonsCol.add(Box.createVerticalStrut(10));
        actionButtonsCol.add(btnGoSearch);

        // System Health Note in Action Card
        JPanel notePanel = new JPanel();
        notePanel.setLayout(new BoxLayout(notePanel, BoxLayout.Y_AXIS));
        notePanel.setOpaque(false);
        notePanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        JLabel noteTitle = new JLabel("Architecture Status:");
        noteTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        noteTitle.setForeground(COLOR_PRIMARY_DARK);

        JLabel noteDetail = new JLabel("<html>• Memory: ArrayList<br>• Undo: Stack Active<br>• Queue: FIFO Active<br>• File: data/students.txt</html>");
        noteDetail.setFont(FONT_SMALL);
        noteDetail.setForeground(COLOR_PRIMARY_BROWN);

        notePanel.add(noteTitle);
        notePanel.add(Box.createVerticalStrut(4));
        notePanel.add(noteDetail);

        actionButtonsCol.add(notePanel);

        actionsCard.add(actionButtonsCol, BorderLayout.CENTER);

        middleSplit.add(tableCard, BorderLayout.CENTER);
        middleSplit.add(actionsCard, BorderLayout.EAST);

        add(middleSplit, BorderLayout.CENTER);
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, String subtitle, Image icon) {
        WarmCard card = new WarmCard(10);
        card.setLayout(new BorderLayout(10, 0));
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel iconLabel = new JLabel(new ImageIcon(icon));

        JPanel textCol = new JPanel();
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
        textCol.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setForeground(COLOR_PRIMARY_BROWN);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSub.setForeground(COLOR_MUTED_BROWN);

        textCol.add(lblTitle);
        textCol.add(valueLabel);
        textCol.add(lblSub);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textCol, BorderLayout.CENTER);

        return card;
    }

    private void populateRecentRows() {
        recentTableModel.setRowCount(0);
        ArrayList<Student> all = studentManager.getAllStudents();
        int total = all.size();
        int maxPages = Math.max(1, (int) Math.ceil((double) total / recentPageSize));

        if (recentPage > maxPages) recentPage = maxPages;
        if (recentPage < 1) recentPage = 1;

        int startIndex = (recentPage - 1) * recentPageSize;
        int endIndex = Math.min(startIndex + recentPageSize, total);

        for (int i = startIndex; i < endIndex; i++) {
            Student s = all.get(i);
            String marksStr = (s.getMarks() == (long) s.getMarks()) ?
                    String.valueOf((long) s.getMarks()) : String.format("%.2f", s.getMarks());

            recentTableModel.addRow(new Object[]{
                    String.valueOf(i + 1),
                    s.getId(),
                    s.getName(),
                    s.getCourse(),
                    String.valueOf(s.getSemester()),
                    marksStr
            });
        }

        if (total == 0) {
            lblRecentRange.setText("No records yet.");
        } else {
            lblRecentRange.setText("Showing " + (startIndex + 1) + "–" + endIndex + " of " + total + " recent records (Page " + recentPage + " of " + maxPages + ")");
        }

        rebuildRecentPagination(maxPages);
    }

    private void rebuildRecentPagination(int totalPages) {
        recentPaginationPanel.removeAll();

        // Prev
        WarmMiniButton btnPrev = new WarmMiniButton("❮");
        btnPrev.setToolTipText("Previous Recent Records");
        btnPrev.setEnabled(recentPage > 1);
        btnPrev.addActionListener(e -> {
            if (recentPage > 1) {
                recentPage--;
                populateRecentRows();
            }
        });
        recentPaginationPanel.add(btnPrev);

        // Numbered Pages
        int startP = Math.max(1, recentPage - 2);
        int endP = Math.min(totalPages, startP + 4);
        if (endP - startP < 4) {
            startP = Math.max(1, endP - 4);
        }

        for (int p = startP; p <= endP; p++) {
            final int pageNumber = p;
            boolean isActive = (p == recentPage);
            WarmMiniButton pageBtn = new WarmMiniButton(String.valueOf(p), isActive);
            pageBtn.addActionListener(e -> {
                if (recentPage != pageNumber) {
                    recentPage = pageNumber;
                    populateRecentRows();
                }
            });
            recentPaginationPanel.add(pageBtn);
        }

        // Next
        WarmMiniButton btnNext = new WarmMiniButton("❯");
        btnNext.setToolTipText("Next Recent Records");
        btnNext.setEnabled(recentPage < totalPages);
        btnNext.addActionListener(e -> {
            if (recentPage < totalPages) {
                recentPage++;
                populateRecentRows();
            }
        });
        recentPaginationPanel.add(btnNext);

        recentPaginationPanel.revalidate();
        recentPaginationPanel.repaint();
    }

    /**
     * Refreshes dashboard metrics and recent entries.
     */
    public void refreshData() {
        int count = studentManager.getStudentCount();
        lblTotalStudents.setText(String.valueOf(count));
        lblRecordsSaved.setText(String.valueOf(count));
        lblQueueStatus.setText(studentManager.getOperationQueue().getPendingCount() + " Pending");
        lblFileStatus.setText("Synchronized");

        populateRecentRows();
    }
}
