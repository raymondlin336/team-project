package gui.home;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Collection of small UI helper components used by HomeView.
 */
public final class HomeViewComponents {

    private HomeViewComponents() {}

    /** Simple panel with rounded corners. */
    public static class RoundedPanel extends JPanel {
        private final int cornerRadius;

        public RoundedPanel(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Shape round = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.setColor(getBackground());
            g2.fill(round);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    /** Border that draws a rounded rectangle. */
    public static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y,
                                int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0x4B5563)); // darker border
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = 12;
            insets.top = insets.bottom = 8;
            return insets;
        }
    }

    /** Wide pill-shaped button (used for "Add task"). */
    public static class PillButton extends JButton {
        public PillButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setBorder(new RoundedBorder(68));
            setBackground(new Color(0xF97316)); // default orange
            setForeground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = getBackground() != null ? getBackground() : new Color(0xF97316);
            Color border = bg.darker();

            if (getModel().isPressed()) {
                g2.setColor(bg.darker());
            } else {
                g2.setColor(bg);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Small circular buttons used for "[ ]" and "..." actions. */
    public static class CircleButton extends JButton {
        private Color idleColor = Color.WHITE;
        private Color pressedColor = new Color(0xE5E7EB);

        public CircleButton(String text) {
            super(text);
            setMargin(new Insets(7, 8, 10, 10));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setBorderPainted(false);
        }

        public void setIdleColor(Color idleColor) {
            this.idleColor = idleColor;
            repaint();
        }

        public void setPressedColor(Color pressedColor) {
            this.pressedColor = pressedColor;
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            int size = Math.max(d.width, d.height);
            d.setSize(size, size);
            return d;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color fill = (getModel().isArmed() || getModel().isPressed())
                    ? pressedColor
                    : idleColor;

            int size = Math.min(getWidth(), getHeight());
            g2.setColor(fill);
            g2.fillOval(0, 0, size - 1, size - 1);

            g2.setColor(fill.darker());
            g2.drawOval(0, 0, size - 1, size - 1);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
