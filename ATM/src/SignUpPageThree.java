import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;

public class SignUpPageThree extends JFrame {

    JRadioButton savings, salary;
    JCheckBox confirm;

    public SignUpPageThree() {

        setTitle("Page 3 - Account Details");
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // ===== TITLE =====
        JLabel title = new JLabel("Page 3 : Account Details", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(150, 20, 300, 30);
        add(title);

        // ===== ACCOUNT TYPE =====
        JLabel accType = new JLabel("Account Type:");
        accType.setBounds(150, 80, 150, 25);
        add(accType);

        savings = new JRadioButton("Savings");
        salary = new JRadioButton("Salary");

        savings.setBounds(300, 80, 100, 25);
        salary.setBounds(400, 80, 100, 25);

        savings.setFocusPainted(false);
        salary.setFocusPainted(false);

        ButtonGroup bg = new ButtonGroup();
        bg.add(savings);
        bg.add(salary);

        add(savings);
        add(salary);

        // ===== GENERATE CARD & PIN =====
        Random r = new Random();
        String card =
            String.valueOf(5040936000000000L + Math.abs(r.nextLong() % 90000000));
        String pin = String.format("%04d", r.nextInt(10000));

        JLabel cardLabel = new JLabel("Card Number : " + card);
        cardLabel.setBounds(200, 130, 350, 25);
        add(cardLabel);

        JLabel pinLabel = new JLabel("PIN : " + pin);
        pinLabel.setBounds(200, 160, 350, 25);
        add(pinLabel);

        // ===== CONFIRMATION =====
        confirm = new JCheckBox("I confirm that the above details are correct");
        confirm.setBounds(150, 200, 350, 30);
        confirm.setFocusPainted(false);
        add(confirm);

        // ===== SUBMIT BUTTON =====
        JButton submit = new JButton("SUBMIT");
        submit.setBounds(200, 250, 100, 35);
        submit.setBackground(Color.GREEN);
        submit.setForeground(Color.WHITE);
        submit.setFocusPainted(false);
        submit.setEnabled(false);
        add(submit);

        confirm.addActionListener(e ->
            submit.setEnabled(confirm.isSelected())
        );

        submit.addActionListener(e -> {

            if (!savings.isSelected() && !salary.isSelected()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select Account Type",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try (Connection con = DBConnection.getConnection()) {

                PreparedStatement ps1 = con.prepareStatement(
                    "INSERT INTO users (card_no, pin, role) VALUES (?,?,?)"
                );
                ps1.setString(1, card);
                ps1.setString(2, pin);
                ps1.setString(3, "User");
                ps1.executeUpdate();

                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO accounts (card_no, balance) VALUES (?,0)"
                );
                ps2.setString(1, card);
                ps2.executeUpdate();

                JOptionPane.showMessageDialog(
                    this,
                    "Account Created Successfully!\nPlease login using Card & PIN"
                );

                dispose();
                new Login();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    this,
                    "Database Error (Page 3)"
                );
            }
        });

        // ===== CANCEL BUTTON =====
        JButton cancel = new JButton("CANCEL");
        cancel.setBounds(330, 250, 100, 35);
        cancel.setBackground(Color.RED);
        cancel.setForeground(Color.WHITE);
        cancel.setFocusPainted(false);
        cancel.addActionListener(e -> dispose());
        add(cancel);

        setSize(600, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
