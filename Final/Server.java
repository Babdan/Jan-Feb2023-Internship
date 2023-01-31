package org.example;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import javax.swing.*;
import org.json.JSONObject;
public class Server {
    private JFrame frame;
    private JTextField ipField, portField;
    private JTextArea jsonDataArea;
    private JButton startServerButton, stopServerButton, clearTextAreaButton;
    private ServerSocket serverSocket;
    private Socket socket;
    private String ip;
    private int port;
    private PrintWriter out;
    private BufferedReader in;
    private Connection conn;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Server().createAndShowUI();
            }
        });
    }
    private void createAndShowUI() {
        frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ipField = new JTextField(15);
        portField = new JTextField(5);
        jsonDataArea = new JTextArea(20, 30);
        jsonDataArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jsonDataArea);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        JLabel ipLabel = new JLabel("IP:");
        topPanel.add(ipLabel);
        topPanel.add(ipField);
        JLabel portLabel = new JLabel("Port:");
        topPanel.add(portLabel);
        topPanel.add(portField);
        frame.add(topPanel, BorderLayout.NORTH);
        // Add a "Start Server" button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        JButton startServerButton = new JButton("Start Server");
        startServerButton.setBackground(Color.GREEN);
        startServerButton.setOpaque(true);
        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jsonDataArea.append("Initializing server... \n");
                startServer();
            }
        });
        JPanel startServerPanel = new JPanel();
        startServerPanel.add(startServerButton);
        // Add a "Stop Server" button
        JButton stopServerButton = new JButton("Stop Server");
        stopServerButton.setBackground(Color.RED);
        stopServerButton.setOpaque(true);
        stopServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
        JPanel stopServerPanel = new JPanel();
        stopServerPanel.add(stopServerButton);
        clearTextAreaButton = new JButton("Clear Textarea");
        clearTextAreaButton.setBackground(Color.YELLOW);
        clearTextAreaButton.setOpaque(true);
        clearTextAreaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jsonDataArea.setText("");
            }
        });
        Panel clearTextAreaPanel = new Panel();
        clearTextAreaPanel.add(clearTextAreaButton);
        // Add the components to the frame
        bottomPanel.add(startServerPanel, BorderLayout.WEST);
        bottomPanel.add(stopServerPanel, BorderLayout.EAST);
        bottomPanel.add(clearTextAreaPanel, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        // Connect to the database
        connectToDatabase();
        frame.pack();
        frame.setVisible(true);
    }
    private void startServer() {
      new Thread(new Runnable() {
          @Override
          public void run() {
        // Get the IP and port from the text fields
        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());
        try {
            // Create the ServerSocket
            serverSocket = new ServerSocket(port);
            jsonDataArea.append("Server started on IP: " + ip + " and Port: " + port + "\nWaiting for client to connect...\n");
            // Listen for incoming connections
            socket = serverSocket.accept();
            jsonDataArea.append("Client connected\n\n");
            // Create the input and output streams
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true) {
                jsonDataArea.setCaretPosition( jsonDataArea.getDocument().getLength());
                // Read JSON data from the input stream
                String jsonData = in.readLine();
                jsonDataArea.append("Received data: " + jsonData + "\n\n");
                // Determine if the data is valid JSON
                if (!jsonData.startsWith("{") || !jsonData.endsWith("}")) {
                    jsonDataArea.append("Incoming data is not valid JSON.\nPlease retry.\n\n");
                    continue;
                }
                // Parse the JSON data
                JSONObject jsonObject = new JSONObject(jsonData);
                if (!jsonObject.has("Name") || !jsonObject.has("Surname") || !jsonObject.has("City")) {
                    jsonDataArea.append("Incoming JSON data is not suitable for overwriting the database.\nPlease retry.\n\n");
                    continue;
                }
                String name = jsonObject.getString("Name");
                String surname = jsonObject.getString("Surname");
                String city = jsonObject.getString("City");
                String university = jsonObject.getString("University");
                // Insert the data into the database
                String sql = "INSERT INTO testable (Name, Surname, City, University) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, name);
                statement.setString(2, surname);
                statement.setString(3, city);
                statement.setString(4, university);
                statement.executeUpdate();
                jsonDataArea.append("Data successfully inserted into database\n\n");
            }
        } catch (IOException | SQLException e) {
            if (socket.isClosed()) {
                return;
            }
            jsonDataArea.append("Error inserting data into database: " + e.getMessage() + "\n\n");
        }
    }
    }).start();
    }
    private void stopServer() {
        // Check if the server is running
        if (serverSocket == null) {
            jsonDataArea.append("Server is not running!\n");
            return;
        }
        try {
            // Close the socket, input and output streams, and the ServerSocket
            socket.close();
            in.close();
            out.close();
            serverSocket.close();
            serverSocket = null;
            jsonDataArea.append("Server stopped.\n");
        } catch (IOException e) {
            jsonDataArea.append("Error: " + e.getMessage() + "\n");
        }
    }
    private void connectToDatabase() {
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.jdbc.Driver");
            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swing_demo", "root", "root");
            jsonDataArea.append("Connected to database.\n");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error connecting to database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
