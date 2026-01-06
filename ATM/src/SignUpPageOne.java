import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignUpPageOne extends JFrame {

    JTextField name, father, dob, email, address, city, pincode, state;
    JRadioButton male, female;
    JComboBox<String> marital;

    public SignUpPageOne() {

        setTitle("Page 1 - Personal Details");
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        int y = 80;

        name = field("Name:", y); y += 40;
        father = field("Father's Name:", y); y += 40;

        // ===== DOB FIELD =====
        dob = field("DOB (DD/MM/YYYY):", y);
        dob.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {

                char c = e.getKeyChar();
                String text = dob.getText();

                // Allow only digits
                if (!Character.isDigit(c)) {
                    e.consume();
                    return;
                }

                // Auto insert '/'
                if (text.length() == 2 || text.length() == 5) {
                    dob.setText(text + "/");
                }

                // Limit length
                if (text.length() >= 10) {
                    e.consume();
                }
            }
        });
        y += 40;

        // ===== GENDER =====
        JLabel g = new JLabel("Gender:");
        g.setBounds(80, y, 150, 25);
        add(g);

        male = new JRadioButton("Male");
        female = new JRadioButton("Female");

        male.setBounds(250, y, 80, 25);
        female.setBounds(340, y, 80, 25);

        male.setFocusPainted(false);
        female.setFocusPainted(false);

        ButtonGroup bg = new ButtonGroup();
        bg.add(male);
        bg.add(female);

        add(male);
        add(female);
        y += 40;

        email = field("Email:", y); y += 40;

        // ===== MARITAL STATUS =====
        JLabel m = new JLabel("Marital Status:");
        m.setBounds(80, y, 150, 25);
        add(m);

        marital = new JComboBox<>(new String[]{"Single", "Married"});
        marital.setBounds(250, y, 200, 25);
        add(marital);
        y += 40;

        address = field("Address:", y); y += 40;
        city = field("City:", y); y += 40;
        pincode = field("Pincode:", y); y += 40;
        state = field("State:", y);

        JButton next = new JButton("NEXT");
        next.setBounds(250, y + 50, 200, 35);
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        next.setFocusPainted(false);
        next.addActionListener(e -> save());
        add(next);

        setSize(650, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== FIELD HELPER =====
    JTextField field(String label, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(80, y, 150, 25);
        add(l);

        JTextField t = new JTextField();
        t.setBounds(250, y, 200, 25);
        add(t);

        return t;
    }

    // ===== SAVE LOGIC =====
    void save() {

        // Trim values
        String nameVal = name.getText().trim();
        String fatherVal = father.getText().trim();
        String dobVal = dob.getText().trim();
        String emailVal = email.getText().trim();
        String addressVal = address.getText().trim();
        String cityVal = city.getText().trim();
        String pincodeVal = pincode.getText().trim();
        String stateVal = state.getText().trim();

        // ===== VALIDATIONS =====
        if (!nameVal.matches("[A-Za-z ]+")) {
            JOptionPane.showMessageDialog(this, "Name must contain only letters");
            return;
        }

        if (!fatherVal.matches("[A-Za-z ]+")) {
            JOptionPane.showMessageDialog(this, "Father name must contain only letters");
            return;
        }

        if (!dobVal.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this, "DOB must be in DD/MM/YYYY format");
            return;
        }

        if (!emailVal.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "Invalid Email ID");
            return;
        }

        if (!pincodeVal.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(this, "Pincode must be 6 digits");
            return;
        }

        if (!male.isSelected() && !female.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select Gender");
            return;
        }

        if (
            addressVal.isEmpty() ||
            cityVal.isEmpty() ||
            stateVal.isEmpty()
        ) {
            JOptionPane.showMessageDialog(this, "All fields are mandatory");
            return;
        }

        // ===== DATABASE INSERT =====
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO signup_page1 " +
                "(name, father_name, dob, gender, email, marital_status, address, city, pincode, state) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, nameVal);
            ps.setString(2, fatherVal);
            ps.setString(3, dobVal);
            ps.setString(4, male.isSelected() ? "Male" : "Female");
            ps.setString(5, emailVal);
            ps.setString(6, marital.getSelectedItem().toString());
            ps.setString(7, addressVal);
            ps.setString(8, cityVal);
            ps.setString(9, pincodeVal);
            ps.setString(10, stateVal);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                dispose();
                new SignUpPageTwo(rs.getInt(1));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error (Page 1)");
        }
    }
}
