package org.example;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**

 User Registration using Swing
 @author javaguides.net
 */
public class UserRegistration extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField city;
    private JTextField university;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserRegistration frame = new UserRegistration();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveDataToDatabase() {
        try {
            // Step 1: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Step 2: Open a connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swing_demo", "root", "root");

            // Step 3: Execute a query
            Statement stmt = conn.createStatement();

            String sql = "INSERT INTO testable (Name, Surname, City, University) " +
                    "VALUES ('" + firstname.getText() + "', '" + lastname.getText() + "', '" + city.getText() + "', '" + university.getText() + "')";
            stmt.executeUpdate(sql);

            // Step 4: Clean-up environment
            stmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Data saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Create the frame.
     */

    public UserRegistration() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\User\\Desktop\\STDM.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewUserRegister = new JLabel("New User Register");
        lblNewUserRegister.setFont(new Font("Times New Roman", Font.PLAIN, 42));
        lblNewUserRegister.setBounds(362, 52, 325, 50);
        contentPane.add(lblNewUserRegister);

        JLabel lblName = new JLabel("First name");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblName.setBounds(58, 152, 99, 43);
        contentPane.add(lblName);

        JLabel lblNewLabel = new JLabel("Last name");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel.setBounds(58, 243, 110, 29);
        contentPane.add(lblNewLabel);

        JLabel lblCity = new JLabel("City");
        lblCity.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblCity.setBounds(58, 324, 124, 36);
        contentPane.add(lblCity);

        JLabel lblUniversity = new JLabel("University");
        lblUniversity.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblUniversity.setBounds(58, 414, 124, 36);
        contentPane.add(lblUniversity);

        firstname = new JTextField();
        firstname.setFont(new Font("Tahoma", Font.PLAIN, 32));
        firstname.setBounds(214, 151, 228, 50);
        contentPane.add(firstname);
        firstname.setColumns(10);

        lastname = new JTextField();
        lastname.setFont(new Font("Tahoma", Font.PLAIN, 32));
        lastname.setBounds(214, 235, 228, 50);
        contentPane.add(lastname);
        lastname.setColumns(10);

        city = new JTextField();
        city.setFont(new Font("Tahoma", Font.PLAIN, 32));
        city.setBounds(214, 323, 228, 50);
        contentPane.add(city);
        city.setColumns(10);

        university = new JTextField();
        university.setFont(new Font("Tahoma", Font.PLAIN, 32));
        university.setBounds(214, 413, 228, 50);
        contentPane.add(university);
        university.setColumns(10);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnSubmit.setBounds(496, 505, 124, 50);
        contentPane.add(btnSubmit);

        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String name = firstname.getText();
                String surname = lastname.getText();
                String cityName = city.getText();
                String univ = university.getText();
                if(name.isEmpty() || surname.isEmpty() || cityName.isEmpty() || univ.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                } else {
//Code to save data in the database
                    saveDataToDatabase();
                }
            }
        });
    }
}
