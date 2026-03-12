import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SignUpPageThree extends JFrame {
    private static final DateTimeFormatter DOB_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int appNo;
    private final boolean adultApplicant;
    private final String card;
    private final String pin;
    JRadioButton savings, salary;
    JCheckBox confirm;
    JTextField nomineeField;
    JLabel statusLabel;
    JLabel cardLabel;
    JLabel pinLabel;

    public SignUpPageThree(int appNo) {
        this.appNo = appNo;
        this.adultApplicant = isAdultApplicant(appNo);
        this.card = adultApplicant ? generateCardNumber() : null;
        this.pin = adultApplicant ? String.format("%04d", new Random().nextInt(10000)) : null;

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

        statusLabel = new JLabel();
        statusLabel.setBounds(150, 115, 320, 25);
        add(statusLabel);

        JLabel nomineeLabel = new JLabel("Nominee Name:");
        nomineeLabel.setBounds(150, 145, 150, 25);
        add(nomineeLabel);

        nomineeField = new JTextField();
        nomineeField.setBounds(300, 145, 200, 28);
        add(nomineeField);

        cardLabel = new JLabel();
        cardLabel.setBounds(200, 130, 350, 25);
        add(cardLabel);

        pinLabel = new JLabel();
        pinLabel.setBounds(200, 160, 350, 25);
        add(pinLabel);

        configureAccountSection(nomineeLabel);

        // ===== CONFIRMATION =====
        confirm = new JCheckBox("I confirm that the above details are correct");
        confirm.setBounds(150, 210, 350, 30);
        confirm.setFocusPainted(false);
        add(confirm);

        // ===== SUBMIT BUTTON =====
        JButton submit = new JButton("SUBMIT");
        submit.setBounds(200, 260, 100, 35);
        submit.setBackground(Color.GREEN);
        submit.setForeground(Color.WHITE);
        submit.setFocusPainted(false);
        submit.setEnabled(false);
        add(submit);

        confirm.addActionListener(e ->
            submit.setEnabled(confirm.isSelected())
        );

        submit.addActionListener(e -> submitAccount());

        // ===== CANCEL BUTTON =====
        JButton cancel = new JButton("CANCEL");
        cancel.setBounds(330, 260, 100, 35);
        cancel.setBackground(Color.RED);
        cancel.setForeground(Color.WHITE);
        cancel.setFocusPainted(false);
        cancel.addActionListener(e -> dispose());
        add(cancel);

        setSize(600, 380);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void configureAccountSection(JLabel nomineeLabel) {
        if (adultApplicant) {
            statusLabel.setText("Applicant is 18 or older. ATM card can be issued.");
            nomineeLabel.setVisible(false);
            nomineeField.setVisible(false);
            cardLabel.setText("Card Number : " + card);
            pinLabel.setText("PIN : " + pin);
        } else {
            statusLabel.setText("Applicant is below 18. Only Joint Account is allowed.");
            savings.setEnabled(false);
            salary.setEnabled(false);
            nomineeLabel.setVisible(true);
            nomineeField.setVisible(true);
            cardLabel.setText("ATM Card : Not issued for applicants below 18");
            pinLabel.setText("Account Type : Joint Account");
        }
    }

    private boolean isAdultApplicant(int appNo) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT dob FROM signup_page1 WHERE app_no=?"
            );
            ps.setInt(1, appNo);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new IllegalStateException("Application not found.");
            }

            LocalDate dob = LocalDate.parse(rs.getString("dob"), DOB_FORMATTER);
            return Period.between(dob, LocalDate.now()).getYears() >= 18;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to verify applicant age: " + ex.getMessage(), ex);
        }
    }

    private String generateCardNumber() {
        Random random = new Random();
        return String.valueOf(5040936000000000L + Math.abs(random.nextLong() % 90000000));
    }

    private void submitAccount() {
        String accountType;
        String nomineeName = nomineeField.getText().trim();

        if (adultApplicant) {
            if (!savings.isSelected() && !salary.isSelected()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select Account Type",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            accountType = savings.isSelected() ? "Savings" : "Salary";
        } else {
            if (!nomineeName.matches("[A-Za-z ]{3,}")) {
                JOptionPane.showMessageDialog(
                    this,
                    "Enter nominee name using letters only.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            accountType = "Joint";
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement updateDetails = con.prepareStatement(
                "UPDATE signup_page2 SET account_type=?, nominee_name=?, card_issued=? WHERE app_no=?"
            );
            updateDetails.setString(1, accountType);
            if (adultApplicant) {
                updateDetails.setNull(2, Types.VARCHAR);
            } else {
                updateDetails.setString(2, nomineeName);
            }
            updateDetails.setString(3, adultApplicant ? "Yes" : "No");
            updateDetails.setInt(4, appNo);
            updateDetails.executeUpdate();

            if (adultApplicant) {
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
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Joint account created with nominee.\nATM card will be issued only after the applicant turns 18."
                );
            }

            dispose();
            new Login();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Database Error (Page 3): " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
