import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UITheme {

    public static final Color BG = new Color(18, 27, 34);
    public static final Color PANEL = new Color(236, 245, 238);
    public static final Color PRIMARY = new Color(20, 92, 122);
    public static final Color SUCCESS = new Color(32, 125, 74);
    public static final Color DANGER = new Color(166, 54, 54);
    public static final Color TEXT_DARK = new Color(23, 23, 23);

    private UITheme() {}

    public static JLabel title(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(Color.WHITE);
        return label;
    }

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        return btn;
    }

    public static JButton successButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(SUCCESS);
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(DANGER);
        return btn;
    }

    public static JTextField inputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(170, 170, 170)),
            new EmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }

    public static JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(170, 170, 170)),
            new EmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }
}
