//may need to remove this package
package scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class schedule_server {

    public static void main(String[] args) {

        Socket sock = null;
        ServerSocket sockL = null;
        System.out.println("Server Listening......");

        //creates server socket
        try {
            sockL = new ServerSocket(9031);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        //connects to incoming connections
        while (true) {
            try {
                sock = sockL.accept();
                System.out.println("connection Established");
                ServerThread st = new ServerThread(sock);
                st.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
    }
}

class ServerThread extends Thread {

    String currentUser = "";
    String clientInput = "";
    String serverOutput = "";
    String line = null;
    BufferedReader is = null;
    PrintWriter os = null;
    Socket s = null;
    ResultSet rs = null;

    //passes connection to thread
    public ServerThread(Socket s) {
        this.s = s;
    }

    //runs thread
    public void run() {
        // connect to database
        dbConnection db = new dbConnection();

        try {
            //***********************edit this line to connect to MySQL database******************************************
            db.connectDB("jdbc:mysql://localhost:3306/scheduler", "root", "brasil"); //replace "scheduler" with db name
                                                                                    //replace "root" with db username
                                                                                    //replace "brasil" with db password
            //************************************************************************************************************
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //keeps thread alive
        while (true) {
            String lusername = "";
            String lpassword = "";
            String dbUsername = null;
            String dbPassword = null;
            boolean correctLogin = false;
            Statement statement = null;

            System.out.println("listening for connection");

            //creates input and output objects
            try {
                is = new BufferedReader(new InputStreamReader(s.getInputStream()));
                os = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }

            //check to see if login data is the same as database data for valid login
            while (correctLogin != true) {
                System.out.println("No correct answer yet");

                // Get message from login username and password attempt
                try {
                    statement = db.getConnection().createStatement();

                    lusername = is.readLine();
                    lpassword = is.readLine();

                    System.out.println(lusername);
                    System.out.println(lpassword);

                    // find out if the username passed in exists to decide if we
                    // check for a password or not
                    ResultSet exists = null;

                    exists = statement.executeQuery("select count(*) from login where exists "
                            + "(select * from login where username ='" + lusername + "')");

                    exists.next();

                    int count = 0;
                    count = exists.getInt(1);

                    // if the username doesn't exist in db, check password, if not
                    // correct do nothing
                    if (count != 0) {
                        ResultSet resultSet = null;
                        resultSet = statement.executeQuery("Select * from login where username = '" + lusername + "'");

                        // move the cursor to point at the first row
                        resultSet.next();

                        dbUsername = resultSet.getString(2);
                        dbPassword = resultSet.getString(3);
                    }

                    System.out.println(dbUsername);
                    System.out.println(dbPassword);

                    //if data matches, send 'correct' to client
                    if (lusername.equals(dbUsername) && lpassword.equals(dbPassword)) {
                        correctLogin = true;

                        System.out.println("correct");

                        //keep track of the user that is logged in
                        currentUser = lusername;

                        os.println("correct");
                        os.flush();

                        System.out.println("Sent correct to client");

                    } else {
                        System.out.println("Inccorect login attempt bro!");

                    }

                    //if IOException is thrown, stop the thread and close connection
                } catch (IOException e) {
                    e.printStackTrace();
                    db.closeStatement();
                    db.closeConnections();
                    this.stop();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("We just got the message");

            //listens to requests from client
            while (true) {
                try {
                    clientInput = is.readLine();

                    System.out.println(clientInput);

                    //if client request is 'getSchedule', return schedule for current user
                    if (clientInput.equals("getSchedule")) {

                        System.out.println("result set");
                        rs = db.getSchedule(currentUser);
                        rs.next();

                        serverOutput = "Monday:" + rs.getString(2) + " Tuesday: " + rs.getString(3) + " Wednesday: "
                                + rs.getString(4) + " Thursday: " + rs.getString(5) + " Friday: " + rs.getString(6)
                                + " Saturday: " + rs.getString(7) + " Sunday: " + rs.getString(8);

                        System.out.println(serverOutput);

                        os.println(serverOutput);
                        os.flush();
                    }

                    //if client request is 'close' break out of while loop to close connection
                    if (clientInput.equals("close")) {
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            //closes statement, connection, and kills thread
            db.closeStatement();
            db.closeConnections();
            this.stop();

        }
    }
}
