import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Deposit extends JFrame {
    JTextField amtField;
    String cardNo;

    public Deposit(String cardNo) {
        this.cardNo = cardNo;
        setTitle("DEPOSIT CASH");
        setLayout(null);
        getContentPane().setBackground(UITheme.BG);

        JLabel title = UITheme.title("Deposit Cash");
        title.setBounds(100, 20, 220, 34);
        add(title);

        JPanel s = new JPanel(null);
        s.setBounds(35, 70, 330, 120);
        s.setBackground(UITheme.PANEL);
        add(s);

        JLabel l = new JLabel("Enter Amount:");
        l.setBounds(25, 25, 100, 30);
        l.setForeground(UITheme.TEXT_DARK);
        s.add(l);

        amtField = UITheme.inputField();
        amtField.setBounds(135, 25, 160, 32);
        s.add(amtField);

        JButton ok = UITheme.successButton("Deposit");
        ok.setBounds(95, 75, 140, 36);
        ok.addActionListener(e -> {
            try (Connection con = DBConnection.getConnection()) {
                double amt = parseAmount();
                PreparedStatement up = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE card_no=?");
                up.setDouble(1, amt); up.setString(2, cardNo);
                int updated = up.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("Account not found for card number " + cardNo);
                }

                PreparedStatement txn = con.prepareStatement(
                    "INSERT INTO transactions (card_no, txn_type, amount) VALUES (?, ?, ?)"
                );
                txn.setString(1, cardNo);
                txn.setString(2, "DEPOSIT");
                txn.setDouble(3, amt);
                txn.executeUpdate();

                JOptionPane.showMessageDialog(this, "Deposit Successful!");
                dispose(); new ATMMenu(cardNo);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    this,
                    "Deposit failed: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        s.add(ok);

        JButton back = UITheme.primaryButton("Back");
        back.setBounds(35, 215, 150, 38);
        back.addActionListener(e -> goToMenu());
        add(back);

        JButton home = UITheme.primaryButton("Home");
        home.setBounds(215, 215, 150, 38);
        home.addActionListener(e -> goToMenu());
        add(home);

        setSize(420, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private double parseAmount() {
        String text = amtField.getText().trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Enter an amount.");
        }

        double amount = Double.parseDouble(text);
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        return amount;
    }

    private void goToMenu() {
        dispose();
        new ATMMenu(cardNo);
    }
}
