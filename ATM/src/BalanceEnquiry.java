import javax.swing.*;
import java.sql.*;

public class BalanceEnquiry {

    public BalanceEnquiry(String cardNo) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps =
                con.prepareStatement("SELECT balance FROM accounts WHERE card_no=?");
            ps.setString(1, cardNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                    "Available Balance: ₹" + rs.getDouble(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching balance");
        }
    }
}
