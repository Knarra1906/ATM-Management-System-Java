import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignUpPageTwo extends JFrame {

    JComboBox<String> religion, category, income, education;
    JTextField occupation, pan, aadhar;
    JRadioButton yes, no;
    int appNo;

    public SignUpPageTwo(int appNo) {

        this.appNo = appNo;

        setTitle("Page 2 - Additional Details");
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        int y = 80;

        religion = combo("Religion:", y,
                new String[]{"Hindu", "Muslim", "Christian", "Other"}); y += 40;

        category = combo("Category:", y,
                new String[]{"General", "OBC", "SC", "ST"}); y += 40;

        income = combo("Income:", y,
                new String[]{"<1L", "1-5L", "5-10L", ">10L"}); y += 40;

        education = combo("Education:", y,
                new String[]{"Graduate", "Post-Graduate", "Other"}); y += 40;

        occupation = field("Occupation:", y); y += 40;
        pan = field("PAN Number:", y); y += 40;
        aadhar = field("Aadhaar Number:", y); y += 40;

        JLabel s = new JLabel("Senior Citizen:");
        s.setBounds(80, y, 150, 25);
        add(s);

        yes = new JRadioButton("Yes");
        no = new JRadioButton("No");

        yes.setBounds(250, y, 80, 25);
        no.setBounds(340, y, 80, 25);

        yes.setFocusPainted(false);
        no.setFocusPainted(false);

        ButtonGroup bg = new ButtonGroup();
        bg.add(yes);
        bg.add(no);

        add(yes);
        add(no);

        JButton next = new JButton("NEXT");
        next.setBounds(250, y + 50, 200, 35);
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        next.setFocusPainted(false);
        next.addActionListener(e -> save());
        add(next);

        setSize(650, 550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== COMBO =====
    JComboBox<String> combo(String label, int y, String[] data) {
        JLabel lb = new JLabel(label);
        lb.setBounds(80, y, 150, 25);
        add(lb);

        JComboBox<String> c = new JComboBox<>(data);
        c.setBounds(250, y, 200, 25);
        add(c);

        return c;
    }

    // ===== FIELD =====
    JTextField field(String label, int y) {
        JLabel lb = new JLabel(label);
        lb.setBounds(80, y, 150, 25);
        add(lb);

        JTextField t = new JTextField();
        t.setBounds(250, y, 200, 25);
        add(t);

        return t;
    }

    // ===== SAVE LOGIC =====
    void save() {

        String occ = occupation.getText().trim();
        String panVal = pan.getText().trim();
        String aad = aadhar.getText().trim();

        // ===== VALIDATIONS =====
        if (occ.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Occupation is mandatory");
            return;
        }

        if (!aad.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this,
                    "Aadhaar must be exactly 12 digits");
            return;
        }

        if (!panVal.isEmpty() &&
                !panVal.matches("[A-Z]{5}[0-9]{4}[A-Z]")) {

            JOptionPane.showMessageDialog(this,
                    "PAN format must be ABCDE1234F");
            return;
        }

        if (!yes.isSelected() && !no.isSelected()) {
            JOptionPane.showMessageDialog(this,
                    "Please select Senior Citizen option");
            return;
        }

        // ===== DATABASE INSERT =====
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO signup_page2 " +
                "(app_no, religion, category, income, education, occupation, pan, aadhar, senior_citizen) " +
                "VALUES (?,?,?,?,?,?,?,?,?)"
            );

            ps.setInt(1, appNo);
            ps.setString(2, religion.getSelectedItem().toString());
            ps.setString(3, category.getSelectedItem().toString());
            ps.setString(4, income.getSelectedItem().toString());
            ps.setString(5, education.getSelectedItem().toString());
            ps.setString(6, occ);
            ps.setString(7, panVal);
            ps.setString(8, aad);
            ps.setString(9, yes.isSelected() ? "Yes" : "No");

            ps.executeUpdate();

            dispose();
            new SignUpPageThree();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Database Error (Page 2)");
        }
    }
}
