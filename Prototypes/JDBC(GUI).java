//make a server-socket app that reads json from socket and parses it into the console.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
import java.sql.*;

public class Main extends JFrame{

    private JTextArea jsonArea;
    private JTextField portField;
    private JButton startButton;
    private JLabel ipLabel;

    public Main(){
        super("JSON Server");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        jsonArea = new JTextArea();
        jsonArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(jsonArea);
        add(scrollPane, BorderLayout.CENTER);

        startButton = new JButton("Start Server");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(portField.getText());
                startServer(port);
            }
        });
        JPanel portPanel = new JPanel();
        portPanel.add(new JLabel("Port: "));
        portField = new JTextField(20);
        portPanel.add(portField);
        add(portPanel, BorderLayout.WEST);
        add(startButton, BorderLayout.SOUTH);

        ipLabel = new JLabel();
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipLabel.setText("IP address:\t" + ip.getHostAddress());
        } catch (UnknownHostException e) {
            ipLabel.setText("Error: Could not get IP address");
        }
        add(ipLabel, BorderLayout.NORTH);

        setVisible(true);
    }

    public void startServer(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            jsonArea.append("Server started on port " + port + "\n");
                Socket socket = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = in.readLine();
                JSONObject json = new JSONObject(line);
                try {
                    String url = "jdbc:mysql://localhost:3306/testParsing";
                    String username = "root";
                    String password = "root";
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(url, username, password);
                    jsonArea.append("Successfully connected to MySQL database.\n");
                    try {
                        String sql = "INSERT INTO tablename (column1, column2, ...) VALUES (?, ?, ...)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
// set the values for the placeholders in the sql statement
                        stmt.setString(1, json.getString("key1"));
                        stmt.setString(2, json.getString("key2"));
// and so on
                        stmt.executeUpdate();
                        jsonArea.append("Successfully inserted data into MySQL database.\n");
                    } catch (SQLException e) {
                        jsonArea.append("Error: Could not insert data into MySQL database. " + e.getMessage() + "\n");
                    }
                } catch (SQLException e) {
                    jsonArea.append("Error: Could not connect to MySQL database. " + e.getMessage() + "\n");
                } catch (ClassNotFoundException e) {
                    jsonArea.append("Error: Could not find JDBC driver. " + e.getMessage() + "\n");
                }
                jsonArea.append(json.toString(4) + "\n");
        } catch (IOException e) {
            jsonArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}
