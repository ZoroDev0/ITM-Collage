package gui;

import model.Student;
import service.SearchService;
import service.StudentManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static gui.UIConstants.*;

/**
 * SearchPanel.java
 * Dedicated page for searching student records with Interactive Pagination.
 * Demonstrates:
 * - Linear Search (traversal over ArrayList across ID/Name/Course)
 * - Binary Search (O(log n) exact lookup by Student ID on sorted data)
 * - Result set pagination (Page sizing, Prev/Next, Page numbers)
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class SearchPanel extends JPanel {

    private final StudentManager studentManager;
    private final MainFrame parentFrame;

    private JTextField txtSearchQuery;
    private JComboBox<String> cmbSearchAlgorithm;
    private JTable resultsTable;
    private DefaultTableModel resultsModel;
    private JLabel lblSearchInfo;
    private JLabel lblPageRange;

    // Pagination State
    private int currentPage = 1;
    private int pageSize = 8;
    private JComboBox<Integer> cmbPageSize;
    private JPanel paginationButtonsPanel;
    private ArrayList<Student> currentSearchResults = new ArrayList<>();

    public SearchPanel(StudentManager studentManager, MainFrame parentFrame) {
        this.studentManager = studentManager;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(0, 14));
        setBackground(COLOR_CREAM);
        setBorder(new EmptyBorder(16, 26, 16, 26));

        initUI();
    }

    private void initUI() {
        // 1. Top Header & Search Criteria Card
        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setOpaque(false);

        JLabel lblTitle = new JLabel("Search Student Directory");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Search across in-memory records using Linear Search O(n) or Binary Search O(log n) with Pagination.");
        lblSub.setFont(FONT_SUBTITLE);
        lblSub.setForeground(COLOR_PRIMARY_BROWN);

        topBox.add(lblTitle);
        topBox.add(Box.createVerticalStrut(4));
        topBox.add(lblSub);
        topBox.add(Box.createVerticalStrut(14));

        // Search Bar Card
        WarmCard searchCard = new WarmCard(12);
        searchCard.setLayout(new BorderLayout(12, 0));
        searchCard.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel leftInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftInputs.setOpaque(false);

        JLabel lblAlgorithm = new JLabel("Search Mode:");
        lblAlgorithm.setFont(FONT_LABEL);
        lblAlgorithm.setForeground(COLOR_PRIMARY_DARK);

        cmbSearchAlgorithm = new JComboBox<>(new String[]{
                "Linear Search (All Fields: ID, Name, Course)",
                "Binary Search (Exact Student ID - O(log n))"
        });
        cmbSearchAlgorithm.setFont(FONT_INPUT);
        cmbSearchAlgorithm.setBackground(COLOR_CREAM_CARD);
        cmbSearchAlgorithm.setForeground(COLOR_PRIMARY_DARK);
        cmbSearchAlgorithm.setPreferredSize(new Dimension(280, 34));

        leftInputs.add(lblAlgorithm);
        leftInputs.add(cmbSearchAlgorithm);

        txtSearchQuery = new JTextField();
        txtSearchQuery.setFont(FONT_INPUT);
        txtSearchQuery.setForeground(COLOR_PRIMARY_DARK);
        txtSearchQuery.setPreferredSize(new Dimension(220, 34));
        txtSearchQuery.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_NEUTRAL, 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        txtSearchQuery.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    executeSearch();
                }
            }
        });

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBtns.setOpaque(false);

        WarmButton btnSearch = new WarmButton("Search", COLOR_PRIMARY_DARK, COLOR_CREAM, VectorIcons.createSearchIcon(12, 12, COLOR_CREAM));
        WarmButton btnReset = new WarmButton("Show All", COLOR_NEUTRAL, COLOR_PRIMARY_DARK, VectorIcons.createListIcon(12, 12, COLOR_PRIMARY_DARK));

        btnSearch.setPreferredSize(new Dimension(100, 34));
        btnReset.setPreferredSize(new Dimension(105, 34));

        btnSearch.addActionListener(e -> executeSearch());
        btnReset.addActionListener(e -> showAll());

        rightBtns.add(btnSearch);
        rightBtns.add(btnReset);

        searchCard.add(leftInputs, BorderLayout.WEST);
        searchCard.add(txtSearchQuery, BorderLayout.CENTER);
        searchCard.add(rightBtns, BorderLayout.EAST);

        topBox.add(searchCard);
        add(topBox, BorderLayout.NORTH);

        // 2. Results Card & Table
        WarmCard resultsCard = new WarmCard(12);
        resultsCard.setLayout(new BorderLayout(0, 10));
        resultsCard.setBorder(new EmptyBorder(14, 16, 14, 16));

        String[] cols = {"#", "Student ID", "Student Name", "Course", "Semester", "Marks"};
        resultsModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        resultsTable = new JTable(resultsModel);
        resultsTable.setFont(FONT_TABLE);
        resultsTable.setRowHeight(36);
        resultsTable.setGridColor(COLOR_NEUTRAL);
        resultsTable.getTableHeader().setFont(FONT_TABLE_HEAD);
        resultsTable.getTableHeader().setBackground(COLOR_CREAM_CARD);
        resultsTable.getTableHeader().setForeground(COLOR_PRIMARY_DARK);

        // Column widths
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(36);
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(90);
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(160);
        resultsTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        resultsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        resultsTable.getColumnModel().getColumn(5).setPreferredWidth(80);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        resultsTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        resultsTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);

        // Marks Pill Renderer
        resultsTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, COLOR_NEUTRAL));
        scrollPane.getViewport().setBackground(COLOR_CREAM_CARD);

        resultsCard.add(scrollPane, BorderLayout.CENTER);

        // 3. Bottom Card Footer: Algorithm Search Info + Interactive Pagination Toolbar
        JPanel bottomContainer = new JPanel(new BorderLayout(0, 6));
        bottomContainer.setOpaque(false);
        bottomContainer.setBorder(new EmptyBorder(6, 2, 2, 2));

        lblSearchInfo = new JLabel("Ready. Enter a query or click Show All.");
        lblSearchInfo.setFont(FONT_SMALL);
        lblSearchInfo.setForeground(COLOR_PRIMARY_BROWN);
        bottomContainer.add(lblSearchInfo, BorderLayout.NORTH);

        // Pagination row
        JPanel paginationRow = new JPanel(new BorderLayout(14, 0));
        paginationRow.setOpaque(false);

        // Left: Page Range + Rows per page
        JPanel leftPageControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPageControls.setOpaque(false);

        lblPageRange = new JLabel("Showing 0 results");
        lblPageRange.setFont(FONT_SMALL);
        lblPageRange.setForeground(COLOR_PRIMARY_DARK);

        JLabel lblRows = new JLabel("Rows per page:");
        lblRows.setFont(FONT_SMALL);
        lblRows.setForeground(COLOR_PRIMARY_BROWN);

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
                populateResultsPage();
            }
        });

        leftPageControls.add(lblPageRange);
        leftPageControls.add(Box.createHorizontalStrut(6));
        leftPageControls.add(lblRows);
        leftPageControls.add(cmbPageSize);

        // Right: Pagination Buttons (❮, 1, 2, ❯)
        paginationButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        paginationButtonsPanel.setOpaque(false);

        paginationRow.add(leftPageControls, BorderLayout.WEST);
        paginationRow.add(paginationButtonsPanel, BorderLayout.EAST);

        bottomContainer.add(paginationRow, BorderLayout.SOUTH);
        resultsCard.add(bottomContainer, BorderLayout.SOUTH);

        add(resultsCard, BorderLayout.CENTER);
    }

    private void executeSearch() {
        String query = txtSearchQuery.getText().trim();
        int mode = cmbSearchAlgorithm.getSelectedIndex();
        ArrayList<Student> all = studentManager.getAllStudents();
        ArrayList<Student> results = new ArrayList<>();

        if (query.isEmpty()) {
            showAll();
            return;
        }

        if (mode == 1) {
            // Binary Search Mode (O(log n))
            long start = System.nanoTime();
            Student found = SearchService.binarySearchById(all, query);
            long elapsed = System.nanoTime() - start;

            if (found != null) {
                results.add(found);
                lblSearchInfo.setText("Binary Search O(log n): Located Student ID '" + query + "' in " + elapsed + " ns.");
            } else {
                lblSearchInfo.setText("Binary Search O(log n): No matching record found for Student ID '" + query + "'.");
            }
        } else {
            // Linear Search Mode (O(n))
            long start = System.nanoTime();
            results = SearchService.searchLinear(all, query);
            long elapsed = System.nanoTime() - start;

            lblSearchInfo.setText("Linear Search O(n): Found " + results.size() + " record(s) across directory in " + (elapsed / 1000) + " µs.");
        }

        this.currentSearchResults = results;
        this.currentPage = 1;
        populateResultsPage();
    }

    private void showAll() {
        txtSearchQuery.setText("");
        this.currentSearchResults = studentManager.getAllStudents();
        this.currentPage = 1;
        lblSearchInfo.setText("Displaying all records from in-memory ArrayList.");
        populateResultsPage();
    }

    /**
     * Slices the current search results for the active page and renders rows.
     */
    private void populateResultsPage() {
        resultsModel.setRowCount(0);
        int total = currentSearchResults.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) total / pageSize));

        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        for (int i = startIndex; i < endIndex; i++) {
            Student s = currentSearchResults.get(i);
            String marksStr = (s.getMarks() == (long) s.getMarks()) ?
                    String.valueOf((long) s.getMarks()) : String.format("%.2f", s.getMarks());

            resultsModel.addRow(new Object[]{
                    String.valueOf(i + 1),
                    s.getId(),
                    s.getName(),
                    s.getCourse(),
                    String.valueOf(s.getSemester()),
                    marksStr
            });
        }

        if (total == 0) {
            lblPageRange.setText("No matching results found.");
        } else {
            lblPageRange.setText("Showing " + (startIndex + 1) + "–" + endIndex + " of " + total + " results (Page " + currentPage + " of " + totalPages + ")");
        }

        rebuildPaginationButtons(totalPages);
    }

    private void rebuildPaginationButtons(int totalPages) {
        paginationButtonsPanel.removeAll();

        // Previous Button
        WarmMiniButton btnPrev = new WarmMiniButton("❮");
        btnPrev.setToolTipText("Previous Page");
        btnPrev.setEnabled(currentPage > 1);
        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                populateResultsPage();
            }
        });
        paginationButtonsPanel.add(btnPrev);

        // Numbered Buttons
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
                    populateResultsPage();
                }
            });
            paginationButtonsPanel.add(pageBtn);
        }

        // Next Button
        WarmMiniButton btnNext = new WarmMiniButton("❯");
        btnNext.setToolTipText("Next Page");
        btnNext.setEnabled(currentPage < totalPages);
        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                populateResultsPage();
            }
        });
        paginationButtonsPanel.add(btnNext);

        paginationButtonsPanel.revalidate();
        paginationButtonsPanel.repaint();
    }

    public void refreshData() {
        showAll();
    }
}
