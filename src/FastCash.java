import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FastCash extends JFrame {
    private static final int[] QUICK_AMOUNTS = {100, 500, 1000, 2000, 5000, 10000};

    private final String cardNo;

    public FastCash(String cardNo) {
        this.cardNo = cardNo;

        setTitle("Fast Cash");
        setLayout(null);
        getContentPane().setBackground(UITheme.BG);

        JLabel title = UITheme.title("Fast Cash");
        title.setBounds(120, 20, 200, 34);
        add(title);

        JPanel panel = new JPanel(new GridLayout(3, 2, 12, 12));
        panel.setBounds(45, 75, 340, 170);
        panel.setBackground(UITheme.PANEL);
        add(panel);

        for (int amount : QUICK_AMOUNTS) {
            JButton button = UITheme.successButton("Rs " + amount);
            button.addActionListener(e -> withdrawAmount(amount));
            panel.add(button);
        }

        JButton back = UITheme.primaryButton("Back");
        back.setBounds(45, 270, 150, 38);
        back.addActionListener(e -> goToMenu());
        add(back);

        JButton home = UITheme.primaryButton("Home");
        home.setBounds(235, 270, 150, 38);
        home.addActionListener(e -> goToMenu());
        add(home);

        setSize(440, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void withdrawAmount(int amount) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT balance FROM accounts WHERE card_no=?"
            );
            ps.setString(1, cardNo);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Account not found");
                return;
            }

            if (rs.getDouble("balance") < amount) {
                JOptionPane.showMessageDialog(this, "Insufficient Balance");
                return;
            }

            PreparedStatement update = con.prepareStatement(
                "UPDATE accounts SET balance = balance - ? WHERE card_no=?"
            );
            update.setDouble(1, amount);
            update.setString(2, cardNo);
            update.executeUpdate();

            PreparedStatement txn = con.prepareStatement(
                "INSERT INTO transactions (card_no, txn_type, amount) VALUES (?, ?, ?)"
            );
            txn.setString(1, cardNo);
            txn.setString(2, "FAST_CASH");
            txn.setDouble(3, amount);
            txn.executeUpdate();

            JOptionPane.showMessageDialog(this, "Please collect your cash: Rs " + amount);
            goToMenu();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Fast cash failed: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void goToMenu() {
        dispose();
        new ATMMenu(cardNo);
    }
}
