import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {

    JTextField cardField;
    JPasswordField pinField;
    JTextField activeField;

    public Login() {

        setTitle("ATM TERMINAL - SECURE LOGIN");
        setLayout(null);
        getContentPane().setBackground(new Color(45,45,45));

        // ===== SCREEN =====
        JPanel screen = new JPanel(null);
        screen.setBounds(30,20,425,150);
        screen.setBackground(new Color(180,200,180));
        screen.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        add(screen);

        JLabel c = new JLabel("CARD NUMBER:");
        c.setBounds(40,40,120,30);
        screen.add(c);

        cardField = new JTextField();
        cardField.setBounds(170,40,200,30);
        screen.add(cardField);

        JLabel p = new JLabel("PIN:");
        p.setBounds(40,90,120,30);
        screen.add(p);

        pinField = new JPasswordField();
        pinField.setBounds(170,90,200,30);
        screen.add(pinField);

        activeField = cardField;

        cardField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                activeField = cardField;
            }
        });

        pinField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                activeField = pinField;
            }
        });

        // ===== KEYPAD =====
        JPanel keypad = new JPanel(new GridLayout(4,3,10,10));
        keypad.setBounds(85,180,315,240);
        keypad.setBackground(new Color(45,45,45));
        add(keypad);

        for(int i=1;i<=9;i++) keypad.add(createKey(String.valueOf(i)));

        JButton clear = new JButton("CLEAR");
        clear.setBackground(Color.RED);
        clear.setForeground(Color.WHITE);
        clear.setFocusPainted(false);
        clear.addActionListener(e -> {
            cardField.setText("");
            pinField.setText("");
            activeField = cardField;
        });

        JButton zero = createKey("0");

        JButton ok = new JButton("OK");
        ok.setBackground(Color.GREEN);
        ok.setForeground(Color.WHITE);
        ok.setFocusPainted(false);
        ok.addActionListener(e -> handleLogin());

        keypad.add(clear);
        keypad.add(zero);
        keypad.add(ok);

        JButton signUp = new JButton("NEW USER? REGISTER HERE");
        signUp.setBounds(125,440,230,30);
        signUp.setBackground(Color.BLACK);
        signUp.setForeground(Color.WHITE);
        signUp.setFocusPainted(false);
        signUp.addActionListener(e -> {
            dispose();
            new SignUpPageOne();
        });
        add(signUp);

        setSize(500,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createKey(String text) {
        JButton b = new JButton(text);
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.addActionListener(e -> {
            if(activeField!=null)
                activeField.setText(activeField.getText()+text);
        });
        return b;
    }

    private void handleLogin() {

        String card = cardField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if(card.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please enter Card Number and PIN");
            return;
        }

        try(Connection con = DBConnection.getConnection()) {

            PreparedStatement ps =
                con.prepareStatement(
                    "SELECT * FROM users WHERE card_no=? AND pin=?"
                );

            ps.setString(1, card);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                dispose();
                new ATMMenu(card);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid Card Number or PIN",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Database Error");
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
