//may need to remove this package
package scheduler;

import java.net.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField userNameTextField;
    private JPasswordField passwordTextField;
    public static String host = "localhost";
    //team 7 port
    public static int port = 9031;
    static BufferedReader read;
    static PrintWriter output;

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws IOException {

        //create and display frame
        Login frame = new Login();
        frame.setVisible(true);

        // connect to the server
        Socket connection = new Socket("localhost", port);

        //create buffered reader to get input from server
        read = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        //create printwriter for sending login to server
        output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));

        String input = read.readLine().trim();

        System.out.println(input);

        //if input = correct, create a client gui, pass the connection and input/output and close current frame
        if (input.equals("correct")) {

            Client clientgui = new Client(read, output, connection);
            clientgui.setVisible(true);
            frame.dispose();

        }

    }

    /**
     * Create the frame.
     */
    public Login() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        userNameTextField = new JTextField();
        userNameTextField.setBounds(176, 120, 86, 20);
        contentPane.add(userNameTextField);
        userNameTextField.setColumns(10);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(176, 151, 86, 20);
        contentPane.add(passwordTextField);
        passwordTextField.setColumns(10);

        JLabel titleLable = new JLabel("Employee Login");
        titleLable.setBounds(150, 87, 200, 20);
        contentPane.add(titleLable);

        JLabel usernameLable = new JLabel("Username:");
        usernameLable.setBounds(95, 123, 100, 14);
        contentPane.add(usernameLable);

        JLabel passwordLable = new JLabel("Password:");
        passwordLable.setBounds(95, 154, 100, 14);
        contentPane.add(passwordLable);

        JButton SubmitButton = new JButton("Submit");

        //Send information to server when button is clicked
        SubmitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                output.println(userNameTextField.getText());
                output.println(passwordTextField.getText());
                output.flush();
                userNameTextField.setText("");
                passwordTextField.setText("");

            }
        });

        SubmitButton.setBounds(173, 192, 89, 23);
        contentPane.add(SubmitButton);
    }
}
