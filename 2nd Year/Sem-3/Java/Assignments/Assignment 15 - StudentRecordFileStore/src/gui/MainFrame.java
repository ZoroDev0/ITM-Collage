package gui;

import auth.AuthenticationManager;
import service.FileHandler;
import service.StudentManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static gui.UIConstants.*;

/**
 * MainFrame.java
 * The single primary JFrame container after successful authentication.
 * Uses CardLayout for clean multi-page navigation across:
 * - Dashboard (Overview)
 * - Add Student
 * - View Records
 * - Search
 * - File Operations
 * - About
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class MainFrame extends JFrame {

    public static final String PAGE_DASHBOARD = "Dashboard";
    public static final String PAGE_ADD       = "Add Student";
    public static final String PAGE_RECORDS   = "View Records";
    public static final String PAGE_SEARCH    = "Search";
    public static final String PAGE_FILE_OPS  = "File Operations";
    public static final String PAGE_ABOUT     = "About";

    private final StudentManager studentManager;
    private final FileHandler fileHandler;
    private final AuthenticationManager authManager;

    // CardLayout Container
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Sub-panels
    private DashboardPanel dashboardPanel;
    private AddStudentPanel addStudentPanel;
    private RecordsPanel recordsPanel;
    private SearchPanel searchPanel;
    private FileOperationsPanel fileOpsPanel;
    private AboutPanel aboutPanel;

    // UI Widgets
    private JLabel lblBreadcrumb;
    private JLabel lblDateTime;
    private final Map<String, SidebarNavItem> navButtons = new HashMap<>();

    public MainFrame(StudentManager studentManager, FileHandler fileHandler, AuthenticationManager authManager) {
        this.studentManager = studentManager;
        this.fileHandler = fileHandler;
        this.authManager = authManager;

        initComponents();
        navigateTo(PAGE_DASHBOARD);
    }

    private void initComponents() {
        setTitle("Student Record File Store | Sasanka Sekhar Kundu (150096725118)");
        setSize(1260, 800);
        setMinimumSize(new Dimension(1080, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_CREAM);
        setLayout(new BorderLayout(0, 0));

        // 1. Left Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // 2. Center Content Area (Top Breadcrumbs Header + CardLayout Center)
        JPanel contentArea = new JPanel(new BorderLayout(0, 0));
        contentArea.setBackground(COLOR_CREAM);

        contentArea.add(createTopBreadcrumbBar(), BorderLayout.NORTH);

        // CardLayout Panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_CREAM);

        dashboardPanel  = new DashboardPanel(studentManager, fileHandler, this);
        addStudentPanel = new AddStudentPanel(studentManager, fileHandler, this);
        recordsPanel    = new RecordsPanel(studentManager, fileHandler, this);
        searchPanel     = new SearchPanel(studentManager, this);
        fileOpsPanel    = new FileOperationsPanel(studentManager, fileHandler, this);
        aboutPanel      = new AboutPanel(this);

        cardPanel.add(dashboardPanel, PAGE_DASHBOARD);
        cardPanel.add(addStudentPanel, PAGE_ADD);
        cardPanel.add(recordsPanel, PAGE_RECORDS);
        cardPanel.add(searchPanel, PAGE_SEARCH);
        cardPanel.add(fileOpsPanel, PAGE_FILE_OPS);
        cardPanel.add(aboutPanel, PAGE_ABOUT);

        contentArea.add(cardPanel, BorderLayout.CENTER);
        add(contentArea, BorderLayout.CENTER);
    }

    private JPanel createTopBreadcrumbBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(COLOR_CREAM);
        bar.setBorder(new EmptyBorder(16, 26, 10, 26));

        lblBreadcrumb = new JLabel("Dashboard");
        lblBreadcrumb.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBreadcrumb.setForeground(COLOR_PRIMARY_BROWN);

        // Date and Time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy • hh:mm a");
        lblDateTime = new JLabel(LocalDateTime.now().format(dtf));
        lblDateTime.setFont(FONT_SMALL);
        lblDateTime.setForeground(COLOR_MUTED_BROWN);

        bar.add(lblBreadcrumb, BorderLayout.WEST);
        bar.add(lblDateTime, BorderLayout.EAST);

        return bar;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(COLOR_PRIMARY_DARK);
        sidebar.setBorder(new EmptyBorder(24, 18, 20, 18));

        // Top Branding
        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setOpaque(false);

        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandRow.setOpaque(false);

        JLabel capIcon = new JLabel(new ImageIcon(VectorIcons.createGraduationCap(28, 28, COLOR_CREAM)));
        brandRow.add(capIcon);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel lbl1 = new JLabel("Student Record");
        lbl1.setFont(FONT_APP_TITLE);
        lbl1.setForeground(Color.WHITE);

        JLabel lbl2 = new JLabel("File Store");
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl2.setForeground(COLOR_CREAM);

        titleBlock.add(lbl1);
        titleBlock.add(lbl2);
        brandRow.add(titleBlock);

        topBox.add(brandRow);
        topBox.add(Box.createVerticalStrut(28));

        // Navigation Items
        JPanel navList = new JPanel();
        navList.setLayout(new BoxLayout(navList, BoxLayout.Y_AXIS));
        navList.setOpaque(false);

        addNavItem(navList, PAGE_DASHBOARD, VectorIcons.NAV_DASHBOARD);
        addNavItem(navList, PAGE_ADD, VectorIcons.NAV_ADD);
        addNavItem(navList, PAGE_RECORDS, VectorIcons.NAV_TABLE);
        addNavItem(navList, PAGE_SEARCH, VectorIcons.NAV_SEARCH);
        addNavItem(navList, PAGE_FILE_OPS, VectorIcons.NAV_FILE);
        addNavItem(navList, PAGE_ABOUT, VectorIcons.NAV_ABOUT);

        topBox.add(navList);
        sidebar.add(topBox, BorderLayout.NORTH);

        // Bottom Box: Student details + Logout Button
        JPanel bottomBox = new JPanel();
        bottomBox.setLayout(new BoxLayout(bottomBox, BoxLayout.Y_AXIS));
        bottomBox.setOpaque(false);

        JLabel lblQuote = new JLabel("<html><i>\"Small Records,<br>Big Possibilities.\"</i></html>");
        lblQuote.setFont(FONT_SMALL);
        lblQuote.setForeground(COLOR_MUTED_BROWN);
        bottomBox.add(lblQuote);
        bottomBox.add(Box.createVerticalStrut(12));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x3E, 0x2C, 0x1A));
        sep.setBackground(new Color(0x3E, 0x2C, 0x1A));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        bottomBox.add(sep);
        bottomBox.add(Box.createVerticalStrut(12));

        // Profile details
        JPanel profileRow = new JPanel(new BorderLayout(8, 0));
        profileRow.setOpaque(false);

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
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        avatar.setForeground(COLOR_CREAM);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);

        JLabel lblAuthor = new JLabel("Sasanka Sekhar Kundu");
        lblAuthor.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblAuthor.setForeground(Color.WHITE);

        JLabel lblRoll = new JLabel("Roll: 150096725118");
        lblRoll.setFont(FONT_SMALL);
        lblRoll.setForeground(COLOR_NEUTRAL);

        details.add(lblAuthor);
        details.add(lblRoll);

        profileRow.add(avatar, BorderLayout.WEST);
        profileRow.add(details, BorderLayout.CENTER);
        bottomBox.add(profileRow);
        bottomBox.add(Box.createVerticalStrut(14));

        // Logout Button
        WarmButton btnLogout = new WarmButton("Logout", new Color(0x3E, 0x2C, 0x1A), COLOR_CREAM, VectorIcons.createCrossIcon(11, 11, COLOR_CREAM));
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btnLogout.addActionListener(e -> handleLogout());
        bottomBox.add(btnLogout);

        sidebar.add(bottomBox, BorderLayout.SOUTH);

        return sidebar;
    }

    private void addNavItem(JPanel container, String pageName, int iconType) {
        SidebarNavItem item = new SidebarNavItem(pageName, iconType, e -> navigateTo(pageName));
        navButtons.put(pageName, item);
        container.add(item);
        container.add(Box.createVerticalStrut(6));
    }

    public void navigateTo(String pageName) {
        cardLayout.show(cardPanel, pageName);

        // Update breadcrumb
        if (PAGE_DASHBOARD.equals(pageName)) {
            lblBreadcrumb.setText("Dashboard");
        } else {
            lblBreadcrumb.setText("Dashboard  /  " + pageName);
        }

        // Update active nav button
        for (Map.Entry<String, SidebarNavItem> entry : navButtons.entrySet()) {
            entry.getValue().setActive(entry.getKey().equals(pageName));
        }

        // Refresh destination page
        if (PAGE_DASHBOARD.equals(pageName)) {
            dashboardPanel.refreshData();
        } else if (PAGE_RECORDS.equals(pageName)) {
            recordsPanel.refreshData();
        } else if (PAGE_SEARCH.equals(pageName)) {
            searchPanel.refreshData();
        } else if (PAGE_FILE_OPS.equals(pageName)) {
            fileOpsPanel.refreshData();
        }
    }

    public void refreshAllPanels() {
        dashboardPanel.refreshData();
        recordsPanel.refreshData();
        fileOpsPanel.refreshData();
        searchPanel.refreshData();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            authManager.logout();
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame(studentManager, fileHandler, authManager).setVisible(true));
        }
    }

    /**
     * Custom Sidebar Navigation Pill
     */
    public static class SidebarNavItem extends JPanel {
        private final String label;
        private final int iconType;
        private boolean isActive = false;
        private boolean isHovered = false;

        public SidebarNavItem(String label, int iconType, ActionListener onClick) {
            this.label = label;
            this.iconType = iconType;
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(194, 38));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (onClick != null) {
                        onClick.actionPerformed(new ActionEvent(SidebarNavItem.this, ActionEvent.ACTION_PERFORMED, label));
                    }
                }
            });
        }

        public void setActive(boolean active) {
            this.isActive = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            if (isActive) {
                g2.setColor(COLOR_CREAM);
                g2.fillRoundRect(0, 0, width, height, 8, 8);
            } else if (isHovered) {
                g2.setColor(new Color(0x3E, 0x2C, 0x1A));
                g2.fillRoundRect(0, 0, width, height, 8, 8);
            }

            Color fg = isActive ? COLOR_PRIMARY_DARK : COLOR_CREAM;

            Image icon = VectorIcons.createNavIcon(iconType, 16, 16, fg);
            if (icon != null) {
                g2.drawImage(icon, 14, (height - 16) / 2, null);
            }

            g2.setFont(new Font("Segoe UI", isActive ? Font.BOLD : Font.PLAIN, 12));
            FontMetrics fm = g2.getFontMetrics();
            int textY = (height - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(fg);
            g2.drawString(label, 40, textY);

            g2.dispose();
        }
    }
}
