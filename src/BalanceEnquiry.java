import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BalanceEnquiry extends JFrame {
    private final String cardNo;
    private final JLabel balanceLabel;

    public BalanceEnquiry(String cardNo) {
        this.cardNo = cardNo;

        setTitle("Balance Enquiry");
        setLayout(null);
        getContentPane().setBackground(UITheme.BG);

        JLabel title = UITheme.title("Available Balance");
        title.setBounds(105, 20, 290, 34);
        add(title);

        JPanel panel = new JPanel(null);
        panel.setBounds(40, 75, 390, 180);
        panel.setBackground(UITheme.PANEL);
        add(panel);

        balanceLabel = new JLabel("", SwingConstants.CENTER);
        balanceLabel.setBounds(35, 35, 320, 40);
        balanceLabel.setForeground(UITheme.TEXT_DARK);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(balanceLabel);

        JButton back = UITheme.primaryButton("Back");
        back.setBounds(35, 105, 120, 35);
        back.addActionListener(e -> goToMenu());
        panel.add(back);

        JButton home = UITheme.successButton("Home");
        home.setBounds(235, 105, 120, 35);
        home.addActionListener(e -> goToMenu());
        panel.add(home);

        loadBalance();

        setSize(480, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadBalance() {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps =
                con.prepareStatement("SELECT balance FROM accounts WHERE card_no=?");
            ps.setString(1, cardNo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                balanceLabel.setText(String.format("Rs %.2f", rs.getDouble(1)));
            } else {
                balanceLabel.setText("Account not found");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error fetching balance: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            balanceLabel.setText("Unavailable");
        }
    }

    private void goToMenu() {
        dispose();
        new ATMMenu(cardNo);
    }
}
