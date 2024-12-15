package QLDC_DoAn.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomButton extends JButton {
    private static CustomButton lastPressedButton = null;
    private Color hoverBackgroundColor = new Color(100, 200, 255);
    private Color pressedBackgroundColor = new Color(255, 0, 153);
    private Color normalBackgroundColor = new Color(255, 153, 204);
    private Color normalTextColor = Color.WHITE;
    private Color hoverTextColor = Color.WHITE;

    public CustomButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 16));
        setBackground(normalBackgroundColor);
        setForeground(normalTextColor);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor);
                setForeground(hoverTextColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (lastPressedButton != CustomButton.this) {
                    setBackground(normalBackgroundColor);
                    setForeground(normalTextColor);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (lastPressedButton != null && lastPressedButton != CustomButton.this) {
                    lastPressedButton.setBackground(lastPressedButton.normalBackgroundColor);
                    lastPressedButton.setForeground(lastPressedButton.normalTextColor);
                }

                setBackground(pressedBackgroundColor);
                lastPressedButton = CustomButton.this;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getParent().getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        GradientPaint gradient = new GradientPaint(0, 0, getBackground().darker(), 0, getHeight(), getBackground().brighter());
        g2.setPaint(gradient);
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
        super.paintComponent(g);
        g2.dispose();
    }
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
        g2.dispose();
    }
    @Override
    public void setContentAreaFilled(boolean b) {
    }
}