import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChatRoom {
    static JTextArea chatArea;
    static JTextField msgField;
    static JButton sendBtn;
    static String username;
    static Connection con;

    public static void main(String[] args) {
        try {
            con = DBConnection.getConnection();

            username = JOptionPane.showInputDialog("Enter username:");
            String password = JOptionPane.showInputDialog("Enter password:");

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?"
            );
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "User not found. Registering...");
                ps = con.prepareStatement(
                    "INSERT INTO users(username,password) VALUES(?,?)"
                );
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
            }

            JFrame frame = new JFrame("Chatroom - " + username);
            frame.setSize(500, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            chatArea = new JTextArea();
            chatArea.setEditable(false);
            msgField = new JTextField(30);
            sendBtn = new JButton("Send");

            JPanel panel = new JPanel();
            panel.add(msgField);
            panel.add(sendBtn);

            frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
            frame.add(panel, BorderLayout.SOUTH);

            loadMessages();

            sendBtn.addActionListener(e -> sendMessage());
            msgField.addActionListener(e -> sendMessage());

            frame.setVisible(true);

        } catch (Exception e) { e.printStackTrace(); }
    }

    static void loadMessages() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM messages ORDER BY time ASC");
            chatArea.setText("");
            while (rs.next()) {
                chatArea.append(rs.getString("time") + " | " +
                                rs.getString("username") + ": " +
                                rs.getString("message") + "\n");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    static void sendMessage() {
        try {
            String msg = msgField.getText().trim();
            if (msg.isEmpty()) return;
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO messages(username,message) VALUES(?,?)"
            );
            ps.setString(1, username);
            ps.setString(2, msg);
            ps.executeUpdate();

            chatArea.append(username + ": " + msg + "\n");
            msgField.setText("");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
