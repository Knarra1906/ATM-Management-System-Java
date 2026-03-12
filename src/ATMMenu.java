import javax.swing.*;
import java.awt.*;

public class ATMMenu extends JFrame {

    private final String cardNo;

    public ATMMenu(String cardNo) {
        this.cardNo = cardNo;

        setTitle("ATM Menu");
        setLayout(null);
        getContentPane().setBackground(UITheme.BG);

        JLabel title = UITheme.title("Choose Transaction");
        title.setBounds(130, 20, 260, 34);
        add(title);

        JPanel screen = new JPanel(null);
        screen.setBounds(32, 65, 435, 340);
        screen.setBackground(UITheme.PANEL);
        screen.setBorder(BorderFactory.createLineBorder(new Color(120, 140, 130), 2));
        add(screen);

        JButton deposit = menuButton("Deposit", 34, 34);
        JButton withdraw = menuButton("Withdraw", 232, 34);
        JButton balance = menuButton("Balance", 34, 98);
        JButton fastCash = menuButton("Fast Cash", 232, 98);
        JButton logout = UITheme.dangerButton("Logout");
        logout.setBounds(132, 240, 170, 40);

        deposit.addActionListener(e -> {
            dispose();
            new Deposit(cardNo);
        });
        withdraw.addActionListener(e -> {
            dispose();
            new Withdraw(cardNo);
        });
        balance.addActionListener(e -> {
            dispose();
            new BalanceEnquiry(cardNo);
        });
        fastCash.addActionListener(e -> {
            dispose();
            new FastCash(cardNo);
        });
        logout.addActionListener(e -> {
            dispose();
            new Login();
        });

        screen.add(deposit);
        screen.add(withdraw);
        screen.add(balance);
        screen.add(fastCash);
        screen.add(logout);

        setSize(500, 470);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton menuButton(String text, int x, int y) {
        JButton btn = UITheme.primaryButton(text);
        btn.setBounds(x, y, 170, 40);
        return btn;
    }
}
