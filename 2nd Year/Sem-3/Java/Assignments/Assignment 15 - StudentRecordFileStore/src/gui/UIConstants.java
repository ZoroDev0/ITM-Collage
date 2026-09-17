package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * UIConstants.java
 * Shared constants, palette, typography, and reusable custom components
 * for the Student Record File Store application.
 * 
 * Strict Palette:
 * - Primary Dark:   #291C0E
 * - Primary Brown:  #6E473B
 * - Muted Brown:    #A78D78
 * - Neutral:        #BEB5A9
 * - Cream:          #E1D4C2
 * 
 * Part of "Student Record File Store - OOP + DSA Edition"
 * Author: Sasanka Sekhar Kundu (Roll: 150096725118)
 */
public class UIConstants {

    // Palette
    public static final Color COLOR_PRIMARY_DARK  = new Color(0x29, 0x1C, 0x0E); // #291C0E
    public static final Color COLOR_PRIMARY_BROWN = new Color(0x6E, 0x47, 0x3B); // #6E473B
    public static final Color COLOR_MUTED_BROWN   = new Color(0xA7, 0x8D, 0x78); // #A78D78
    public static final Color COLOR_NEUTRAL       = new Color(0xBE, 0xB5, 0xA9); // #BEB5A9
    public static final Color COLOR_CREAM         = new Color(0xE1, 0xD4, 0xC2); // #E1D4C2
    public static final Color COLOR_CREAM_CARD    = new Color(0xEB, 0xE0, 0xD0); // Richer cream for cards
    public static final Color COLOR_CREAM_INPUT   = new Color(0xF2, 0xEB, 0xDF); // Soft warm input bg
    public static final Color COLOR_ACCENT_DELETE = new Color(0x8A, 0x42, 0x34); // Warm muted rust-brown
    public static final Color COLOR_MARKS_BADGE   = new Color(0xCB, 0xD7, 0xC3); // Soft warm sage
    public static final Color COLOR_MARKS_TEXT    = new Color(0x2A, 0x47, 0x22); // Forest green-brown

    // Typography
    public static final Font FONT_APP_TITLE   = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_HEADER      = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_EYEBROW     = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_SUBTITLE    = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SECTION     = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_LABEL       = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_INPUT       = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON      = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_TABLE       = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TABLE_HEAD  = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_SMALL       = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Reusable WarmCard container with rounded corners and subtle neutral border.
     */
    public static class WarmCard extends JPanel {
        private final int radius;
        private final Color bgColor;

        public WarmCard(int radius) {
            this(radius, COLOR_CREAM_CARD);
        }

        public WarmCard(int radius, Color bg) {
            this.radius = radius;
            this.bgColor = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(COLOR_NEUTRAL);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Flat, high-contrast, antialiased button with subtle hover & pressed micro-interactions.
     */
    public static class WarmButton extends JButton {
        private final Color bgNormal;
        private final Color fgColor;
        private final Image iconImg;
        private boolean isHovered = false;
        private boolean isPressed = false;

        public WarmButton(String text, Color bg, Color fg, Image icon) {
            super(text);
            this.bgNormal = bg;
            this.fgColor = fg;
            this.iconImg = icon;

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setFont(FONT_BUTTON);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(130, 36));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    isPressed = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            Color paintBg = bgNormal;
            if (isPressed) {
                paintBg = bgNormal.darker();
            } else if (isHovered) {
                paintBg = brighten(bgNormal, 1.15f);
            }

            g2.setColor(paintBg);
            g2.fillRoundRect(0, 0, width, height, 8, 8);

            g2.setColor(new Color(0, 0, 0, 20));
            g2.drawRoundRect(0, 0, width - 1, height - 1, 8, 8);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int iconWidth = (iconImg != null) ? iconImg.getWidth(null) + 6 : 0;
            int totalContentWidth = iconWidth + textWidth;

            int startX = (width - totalContentWidth) / 2;
            int textY = (height - fm.getHeight()) / 2 + fm.getAscent() + (isPressed ? 1 : 0);

            if (iconImg != null) {
                int iconY = (height - iconImg.getHeight(null)) / 2 + (isPressed ? 1 : 0);
                g2.drawImage(iconImg, startX, iconY, null);
                startX += iconImg.getWidth(null) + 6;
            }

            g2.setColor(fgColor);
            g2.drawString(getText(), startX, textY);
            g2.dispose();
        }

        private Color brighten(Color c, float factor) {
            int r = Math.min(255, (int) (c.getRed() * factor));
            int g = Math.min(255, (int) (c.getGreen() * factor));
            int b = Math.min(255, (int) (c.getBlue() * factor));
            return new Color(r, g, b, c.getAlpha());
        }
    }

    /**
     * Small pagination and action button.
     */
    public static class WarmMiniButton extends JButton {
        public WarmMiniButton(String text) {
            this(text, false);
        }

        public WarmMiniButton(String text, boolean isActive) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(28, 28));

            Color bg = isActive ? COLOR_PRIMARY_DARK : COLOR_CREAM_INPUT;
            Color fg = isActive ? COLOR_CREAM : COLOR_PRIMARY_DARK;

            setBackground(bg);
            setForeground(fg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

            g2.setColor(COLOR_NEUTRAL);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(getForeground());
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    /**
     * Labeled form field with vector icon and focus state.
     */
    public static class FormInputField extends JPanel {
        private final JTextField textField;
        private boolean isFocused = false;

        public FormInputField(String labelText, String placeholder, int iconType) {
            setLayout(new BorderLayout(0, 4));
            setOpaque(false);

            JPanel labelRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
            labelRow.setOpaque(false);

            JLabel lbl = new JLabel(labelText);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(COLOR_PRIMARY_DARK);

            JLabel star = new JLabel("*");
            star.setFont(new Font("Segoe UI", Font.BOLD, 11));
            star.setForeground(COLOR_PRIMARY_BROWN);

            labelRow.add(lbl);
            labelRow.add(star);
            add(labelRow, BorderLayout.NORTH);

            JPanel inputContainer = new JPanel(new BorderLayout(8, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(COLOR_CREAM_INPUT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                    if (isFocused) {
                        g2.setColor(COLOR_PRIMARY_BROWN);
                        g2.setStroke(new BasicStroke(1.5f));
                    } else {
                        g2.setColor(COLOR_NEUTRAL);
                        g2.setStroke(new BasicStroke(1.0f));
                    }
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            inputContainer.setOpaque(false);
            inputContainer.setPreferredSize(new Dimension(0, 34));
            inputContainer.setBorder(new EmptyBorder(0, 8, 0, 8));

            JLabel iconLabel = new JLabel(new ImageIcon(VectorIcons.createFieldIcon(iconType, 15, 15, COLOR_MUTED_BROWN)));
            inputContainer.add(iconLabel, BorderLayout.WEST);

            textField = new JTextField() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getText().isEmpty() && !isFocused && placeholder != null) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g2.setFont(getFont());
                        g2.setColor(COLOR_MUTED_BROWN);
                        FontMetrics fm = g2.getFontMetrics();
                        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                        g2.drawString(placeholder, 0, y);
                        g2.dispose();
                    }
                }
            };
            textField.setFont(FONT_INPUT);
            textField.setForeground(COLOR_PRIMARY_DARK);
            textField.setCaretColor(COLOR_PRIMARY_DARK);
            textField.setOpaque(false);
            textField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    inputContainer.repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    inputContainer.repaint();
                }
            });

            inputContainer.add(textField, BorderLayout.CENTER);
            add(inputContainer, BorderLayout.CENTER);
        }

        public String getText() {
            return textField.getText();
        }

        public void setText(String t) {
            textField.setText(t);
        }

        public JTextField getTextField() {
            return textField;
        }
    }

    /**
     * Vector icon utilities rendered using pure Java2D (zero external dependencies).
     */
    public static class VectorIcons {
        public static final int FIELD_ID      = 11;
        public static final int FIELD_USER    = 12;
        public static final int FIELD_BOOK    = 13;
        public static final int FIELD_LAYERS  = 14;
        public static final int FIELD_PERCENT = 15;

        public static final int NAV_DASHBOARD = 1;
        public static final int NAV_ADD       = 2;
        public static final int NAV_TABLE     = 3;
        public static final int NAV_SEARCH    = 4;
        public static final int NAV_FILE      = 5;
        public static final int NAV_ABOUT     = 6;

        public static Image createGraduationCap(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);

            Polygon cap = new Polygon();
            cap.addPoint(w / 2, 4);
            cap.addPoint(w - 2, h / 2 - 2);
            cap.addPoint(w / 2, h - 8);
            cap.addPoint(2, h / 2 - 2);
            g2.fillPolygon(cap);

            g2.setStroke(new BasicStroke(2.0f));
            g2.drawArc(6, h / 2 - 4, w - 12, 10, 180, 180);
            g2.drawLine(w - 4, h / 2 - 2, w - 4, h - 4);
            g2.fillOval(w - 6, h - 5, 4, 4);
            g2.dispose();
            return img;
        }

        public static Image createHomeIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            Polygon roof = new Polygon();
            roof.addPoint(w / 2, 2);
            roof.addPoint(w - 2, 8);
            roof.addPoint(2, 8);
            g2.fillPolygon(roof);
            g2.fillRect(4, 7, w - 8, h - 9);
            g2.dispose();
            return img;
        }

        public static Image createFolderIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(2, 4, w - 4, h - 6, 4, 4);
            g2.fillRoundRect(2, 2, 7, 5, 2, 2);
            g2.dispose();
            return img;
        }

        public static Image createCalendarIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(2, 4, w - 4, h - 6, 4, 4);
            g2.drawLine(2, 9, w - 2, 9);
            g2.drawLine(6, 2, 6, 5);
            g2.drawLine(w - 6, 2, w - 6, 5);
            g2.fillRect(5, 12, 3, 3);
            g2.fillRect(10, 12, 3, 3);
            g2.fillRect(15, 12, 3, 3);
            g2.dispose();
            return img;
        }

        public static Image createUsersIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillOval(6, 3, 10, 10);
            g2.fillArc(2, 12, 18, 14, 0, 180);
            g2.fillOval(16, 5, 7, 7);
            g2.fillArc(15, 13, 11, 10, 0, 180);
            g2.dispose();
            return img;
        }

        public static Image createSearchIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawOval(2, 2, w - 7, h - 7);
            g2.drawLine(w - 5, h - 5, w - 1, h - 1);
            g2.dispose();
            return img;
        }

        public static Image createPlusIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawLine(w / 2, 2, w / 2, h - 2);
            g2.drawLine(2, h / 2, w - 2, h / 2);
            g2.dispose();
            return img;
        }

        public static Image createPencilIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawLine(2, h - 2, w - 4, 2);
            g2.drawLine(2, h - 2, 6, h - 2);
            g2.drawLine(2, h - 2, 2, h - 6);
            g2.dispose();
            return img;
        }

        public static Image createTrashIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawLine(2, 3, w - 2, 3);
            g2.drawLine(w / 2 - 2, 1, w / 2 + 2, 1);
            g2.drawRect(3, 4, w - 6, h - 5);
            g2.drawLine(w / 2, 5, w / 2, h - 3);
            g2.dispose();
            return img;
        }

        public static Image createCrossIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawLine(2, 2, w - 2, h - 2);
            g2.drawLine(w - 2, 2, 2, h - 2);
            g2.dispose();
            return img;
        }

        public static Image createDiskIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(2, 2, w - 4, h - 4, 3, 3);
            g2.fillRect(4, 2, w - 8, 4);
            g2.drawRect(4, h - 5, w - 8, 3);
            g2.dispose();
            return img;
        }

        public static Image createRefreshIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawArc(2, 2, w - 4, h - 4, 45, 270);
            g2.drawLine(w / 2, 1, w / 2 + 3, 3);
            g2.drawLine(w / 2, 5, w / 2 + 3, 3);
            g2.dispose();
            return img;
        }

        public static Image createListIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(4, 3, w - 2, 3);
            g2.drawLine(4, 7, w - 2, 7);
            g2.drawLine(4, 11, w - 2, 11);
            g2.fillOval(1, 2, 2, 2);
            g2.fillOval(1, 6, 2, 2);
            g2.fillOval(1, 10, 2, 2);
            g2.dispose();
            return img;
        }

        public static Image createUndoIcon(int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawArc(3, 4, w - 6, h - 6, 45, 240);
            g2.drawLine(3, 7, 7, 7);
            g2.drawLine(3, 7, 3, 3);
            g2.dispose();
            return img;
        }

        public static Image createNavIcon(int type, int w, int h, Color color) {
            switch (type) {
                case NAV_DASHBOARD: return createHomeIcon(w, h, color);
                case NAV_ADD:       return createPlusIcon(w, h, color);
                case NAV_TABLE:     return createListIcon(w, h, color);
                case NAV_SEARCH:    return createSearchIcon(w, h, color);
                case NAV_FILE:      return createFolderIcon(w, h, color);
                case NAV_ABOUT:     return createGraduationCap(w, h, color);
                default:            return createHomeIcon(w, h, color);
            }
        }

        public static Image createFieldIcon(int type, int w, int h, Color color) {
            Image img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) img.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.2f));

            switch (type) {
                case FIELD_ID:
                    g2.drawRoundRect(1, 2, w - 2, h - 4, 3, 3);
                    g2.fillOval(3, 4, 4, 4);
                    g2.drawLine(8, 5, w - 3, 5);
                    g2.drawLine(8, 8, w - 3, 8);
                    break;
                case FIELD_USER:
                    g2.drawOval(w / 2 - 3, 1, 6, 6);
                    g2.drawArc(2, 7, w - 4, 10, 0, 180);
                    break;
                case FIELD_BOOK:
                    g2.drawRect(2, 2, w - 4, h - 4);
                    g2.drawLine(w / 2, 2, w / 2, h - 2);
                    break;
                case FIELD_LAYERS:
                    g2.drawLine(2, 4, w - 2, 4);
                    g2.drawLine(2, 8, w - 2, 8);
                    g2.drawLine(2, 12, w - 2, 12);
                    break;
                case FIELD_PERCENT:
                    g2.drawLine(w - 3, 2, 3, h - 2);
                    g2.fillOval(2, 2, 3, 3);
                    g2.fillOval(w - 5, h - 5, 3, 3);
                    break;
            }
            g2.dispose();
            return img;
        }
    }
}
