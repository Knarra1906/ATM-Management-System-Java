import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ATMScreen {

    public static void main(String[] args) {

        JFrame frame = new JFrame("ATM Login");
        frame.setSize(420, 420);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(45, 45, 45));

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(180, 200, 180));

        JLabel cardLabel = new JLabel("Enter Card Number:");
        JTextField cardField = new JTextField();

        JLabel pinLabel = new JLabel("Enter PIN:");
        JPasswordField pinField = new JPasswordField();

        JButton okBtn = new JButton("OK");
        okBtn.setBackground(Color.GREEN);
        okBtn.setForeground(Color.WHITE);
        okBtn.setFocusPainted(false);

        mainPanel.add(cardLabel);
        mainPanel.add(cardField);
        mainPanel.add(pinLabel);
        mainPanel.add(pinField);
        mainPanel.add(new JLabel());
        mainPanel.add(okBtn);

        // ===== KEYPAD =====
        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        keypadPanel.setBackground(new Color(45, 45, 45));

        for (int i = 1; i <= 9; i++) {
            keypadPanel.add(createKey(String.valueOf(i), pinField));
        }

        JButton clearBtn = new JButton("CLEAR");
        clearBtn.setBackground(Color.RED);
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFocusPainted(false);
        clearBtn.addActionListener(e -> pinField.setText(""));

        keypadPanel.add(clearBtn);
        keypadPanel.add(createKey("0", pinField));

        JButton dummy = new JButton();
        dummy.setVisible(false);
        keypadPanel.add(dummy);

        // ===== LOGIN LOGIC =====
        okBtn.addActionListener(e -> {

            String card = cardField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            if (card.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Please enter Card Number and PIN"
                );
                return;
            }

            try (Connection con = DBConnection.getConnection()) {

                PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE card_no=? AND pin=?"
                );

                ps.setString(1, card);
                ps.setString(2, pin);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    frame.dispose();
                    new ATMMenu(card);
                } else {
                    JOptionPane.showMessageDialog(
                        frame,
                        "Invalid Card Number or PIN",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    frame,
                    "Database Error"
                );
            }
        });

        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(keypadPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ===== KEYPAD BUTTON =====
    private static JButton createKey(String text, JPasswordField pinField) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        btn.addActionListener(e ->
            pinField.setText(pinField.getText() + text)
        );

        return btn;
    }
}
