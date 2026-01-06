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
        getContentPane().setBackground(new Color(45, 45, 45));

        JPanel s = new JPanel(null);
        s.setBounds(50, 30, 300, 80);
        s.setBackground(new Color(180, 200, 180));
        add(s);

        JLabel l = new JLabel("ENTER AMOUNT:"); l.setBounds(20, 25, 100, 30); s.add(l);
        amtField = new JTextField(); amtField.setBounds(130, 25, 140, 30); s.add(amtField);

        JButton ok = new JButton("DEPOSIT");
        ok.setBounds(100, 150, 200, 40);
        ok.setBackground(new Color(0, 0, 100));
        ok.setForeground(Color.WHITE);
        ok.setFocusPainted(false);
        ok.addActionListener(e -> {
            try (Connection con = DBConnection.getConnection()) {
                double amt = Double.parseDouble(amtField.getText());
                PreparedStatement up = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE card_no=?");
                up.setDouble(1, amt); up.setString(2, cardNo);
                up.executeUpdate();
                JOptionPane.showMessageDialog(this, "Deposit Successful!");
                dispose(); new ATMMenu(cardNo);
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        add(ok);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}