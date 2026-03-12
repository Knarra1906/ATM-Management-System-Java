import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {

    private final JTextField cardField;
    private final JPasswordField pinField;
    private JTextField activeField;

    public Login() {
        setTitle("ATM - Secure Login");
        setLayout(null);
        getContentPane().setBackground(UITheme.BG);

        JLabel title = UITheme.title("ATM Access");
        title.setBounds(130, 18, 240, 34);
        add(title);

        JPanel screen = new JPanel(null);
        screen.setBounds(34, 65, 430, 170);
        screen.setBackground(UITheme.PANEL);
        screen.setBorder(BorderFactory.createLineBorder(new Color(120, 140, 130), 2));
        add(screen);

        JLabel c = new JLabel("Card Number");
        c.setBounds(34, 38, 110, 24);
        c.setForeground(UITheme.TEXT_DARK);
        screen.add(c);

        cardField = UITheme.inputField();
        cardField.setBounds(150, 34, 240, 32);
        screen.add(cardField);

        JLabel p = new JLabel("PIN");
        p.setBounds(34, 95, 110, 24);
        p.setForeground(UITheme.TEXT_DARK);
        screen.add(p);

        pinField = UITheme.passwordField();
        pinField.setBounds(150, 91, 240, 32);
        screen.add(pinField);

        activeField = cardField;
        cardField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                activeField = cardField;
            }
        });
        pinField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                activeField = pinField;
            }
        });

        JPanel keypad = new JPanel(new GridLayout(4, 3, 10, 10));
        keypad.setBounds(94, 250, 300, 220);
        keypad.setBackground(UITheme.BG);
        add(keypad);

        for (int i = 1; i <= 9; i++) keypad.add(createKey(String.valueOf(i)));

        JButton clear = UITheme.dangerButton("CLEAR");
        clear.addActionListener(e -> {
            cardField.setText("");
            pinField.setText("");
            activeField = cardField;
            cardField.requestFocus();
        });

        JButton zero = createKey("0");

        JButton ok = UITheme.successButton("LOGIN");
        ok.addActionListener(e -> handleLogin());

        keypad.add(clear);
        keypad.add(zero);
        keypad.add(ok);

        JButton signUp = UITheme.primaryButton("NEW USER? REGISTER HERE");
        signUp.setBounds(134, 482, 230, 34);
        signUp.addActionListener(e -> {
            dispose();
            new SignUpPageOne();
        });
        add(signUp);

        setSize(500, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createKey(String text) {
        JButton b = UITheme.primaryButton(text);
        b.addActionListener(e -> {
            if (activeField != null) {
                activeField.setText(activeField.getText() + text);
            }
        });
        return b;
    }

    private void handleLogin() {
        String card = cardField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if (!card.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "Card Number must be 16 digits.");
            return;
        }

        if (!pin.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "PIN must be 4 digits.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT card_no FROM users WHERE card_no=? AND pin=?"
            );
            ps.setString(1, card);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dispose();
                new ATMMenu(card);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid Card Number or PIN",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Database Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
