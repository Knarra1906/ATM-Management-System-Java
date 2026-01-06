import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ATMMenu extends JFrame {

    String cardNo;

    public ATMMenu(String cardNo) {

        this.cardNo = cardNo;

        setTitle("ATM MENU");
        setLayout(null);
        getContentPane().setBackground(new Color(45, 45, 45));

        // ===== SCREEN PANEL =====
        JPanel screen = new JPanel(null);
        screen.setBounds(40, 30, 400, 250);
        screen.setBackground(new Color(170, 190, 170));
        screen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(screen);

        JLabel title = new JLabel("SELECT TRANSACTION", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(50, 10, 300, 30);
        screen.add(title);

        // ===== BUTTONS =====
        JButton deposit = createBtn("DEPOSIT", 40, 60);
        JButton withdraw = createBtn("WITHDRAW", 210, 60);
        JButton balance = createBtn("BALANCE", 40, 120);
        JButton logout = createBtn("LOGOUT", 210, 120);

        deposit.addActionListener(e -> {
            dispose();
            new Deposit(cardNo);
        });

        withdraw.addActionListener(e -> {
            dispose();
            new Withdraw(cardNo);
        });

        balance.addActionListener(e -> checkBalance());

        logout.addActionListener(e -> {
            dispose();
            new Login();
        });

        screen.add(deposit);
        screen.add(withdraw);
        screen.add(balance);
        screen.add(logout);

        setSize(500, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ===== COMMON BUTTON STYLE =====
    private JButton createBtn(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 150, 40);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        return btn;
    }

    // ===== BALANCE ENQUIRY =====
    private void checkBalance() {
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps =
                con.prepareStatement(
                    "SELECT balance FROM accounts WHERE card_no=?"
                );
            ps.setString(1, cardNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Available Balance : ₹" + rs.getDouble("balance"),
                    "Balance Enquiry",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Account not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Database Error",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
