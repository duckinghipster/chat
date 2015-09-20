package chat_application;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SHUBHAM NANDA
 */
public class ClientGUI {
    
    public static void main (String[] args) {
        MainWindow.setTitle(UserName + "'s Chat Box");
        MainWindow.setLocation(50, 50);
        MainWindow.setResizable(false);
        MWLayout();
        MWAction();
        MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainWindow.setVisible(true);
        MainWindow.setSize(500, 450);
        B_Send.setEnabled(false);
        B_Disconnect.setEnabled(false);
        B_Connect.setEnabled(true);
        B_Show.setEnabled(false);
    }
    
    private static void Connect(String host) {
        try {
            final int port = 8989;
            Socket Sock = new Socket(host, port);
            System.out.println("You are connected to: " + host);
            
            UserName = TF_UserName.getText();
            L_LoggedInAsBox.setText(UserName);
            Server.CurrentUsers.add(UserName);
            MainWindow.setTitle(UserName + "'s chat Box");
            LogInWindow.setVisible(false);
            B_Send.setEnabled(true);
            B_Disconnect.setEnabled(true);
            B_Disconnect.setBackground(Color.red);
            B_Connect.setEnabled(false);
            B_Signup.setEnabled(false);
            B_Show.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Successfully logged in");
                                    
            ChatClient = new Client_Read_Write_Thread(Sock);
            PrintWriter OUT = new PrintWriter(Sock.getOutputStream());
            OUT.println(UserName);
            OUT.flush();
            
            Thread X = new Thread(ChatClient);
            X.start();
            
        } catch (Exception X) {
            System.out.println("Invalid IP or Server not available");
            JOptionPane.showMessageDialog(null, "Invalid IP or Server not responding!!");
        }
    }

    private static void MWLayout() {
        MainWindow.setBackground(Color.WHITE);
        MainWindow.setSize(500, 320);
        MainWindow.getContentPane().setLayout(null);
        
        B_Send.setBackground(Color.blue);
        B_Send.setForeground(Color.WHITE);
        B_Send.setText("SEND");
        MainWindow.getContentPane().add(B_Send);
        B_Send.setBounds(400, 390, 90, 30);
        
        B_Disconnect.setBackground(Color.blue);
        B_Disconnect.setForeground(Color.WHITE);
        B_Disconnect.setText("DISCONNECT");
        MainWindow.getContentPane().add(B_Disconnect);
        B_Disconnect.setBounds(10, 10, 110, 25);
    
        B_Connect.setBackground(Color.blue);
        B_Connect.setForeground(Color.WHITE);
        B_Connect.setText("CONNECT");
        MainWindow.getContentPane().add(B_Connect);
        B_Connect.setBounds(130, 10, 110, 25);
        
        B_Help.setBackground(Color.blue);
        B_Help.setForeground(Color.WHITE);
        B_Help.setText("Help");
        MainWindow.getContentPane().add(B_Help);
        B_Help.setBounds(250, 10, 70, 25);
        
        B_Signup.setBackground(Color.blue);
        B_Signup.setForeground(Color.WHITE);
        B_Signup.setText("Signup");
        MainWindow.getContentPane().add(B_Signup);
        B_Signup.setBounds(400, 43, 75, 18);
        
        B_Show.setBackground(Color.blue);
        B_Show.setForeground(Color.WHITE);
        B_Show.setText("Online");
        //MainWindow.getContentPane().add(B_Show);
        B_Show.setBounds(320, 43, 75, 18);
        
        TF_TextMessage.setForeground(Color.BLUE);
        TF_TextMessage.requestFocus();
        MainWindow.getContentPane().add(TF_TextMessage);
        TF_TextMessage.setBounds(10, 390, 390, 30);
        
        TA_Chat.setColumns(20);
        TA_Chat.setFont(new Font("Verdana", 0, 12));
        TA_Chat.setForeground(Color.BLUE);
        TA_Chat.setLineWrap(true);
        TA_Chat.setRows(5);
        TA_Chat.setEditable(false);
        
        SP_Chat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_Chat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SP_Chat.setViewportView(TA_Chat);
        MainWindow.getContentPane().add(SP_Chat);
        SP_Chat.setBounds(10, 70, 480, 310);
        
        L_LoggedInAsBox.setHorizontalAlignment(SwingConstants.CENTER);
        L_LoggedInAsBox.setFont(new Font("Verdana", 0, 15));
        L_LoggedInAsBox.setForeground(Color.BLACK);
        L_LoggedInAsBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        MainWindow.getContentPane().add(L_LoggedInAsBox);
        L_LoggedInAsBox.setBounds(340, 10, 150, 25);
    }

    private static void MWAction() {
        B_Send.addActionListener(
            new ActionListener (){
                public void actionPerformed(ActionEvent e) {
                    if (!TF_TextMessage.getText().equals("")) {
                        try {
                            ChatClient.Send(TF_TextMessage.getText());
                        } catch (Exception ex) {
                            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        TF_TextMessage.requestFocus();
                    }
                }
            }
        );
        
        TF_TextMessage.addActionListener(
            new ActionListener (){
                public void actionPerformed(ActionEvent e) {
                    if (!TF_TextMessage.getText().equals("")) {
                        try {
                            ChatClient.Send(TF_TextMessage.getText());
                        } catch (Exception ex) {
                            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        TF_TextMessage.requestFocus();
                    }
                }
            }
        );
        
        B_Disconnect.addActionListener(
            new ActionListener (){
                public void actionPerformed(ActionEvent e) {
                    try {
                        ChatClient.Disconnect();
                    } catch (Exception Y) {
                        Y.printStackTrace();;
                    }
                }
            }
        );
        
        B_Connect.addActionListener(
            new ActionListener (){
                public void actionPerformed(ActionEvent e) {
                    JLabel L_UserName = new JLabel("UserName");
                    //JTextField TF_UserName = new JTextField();
                    JLabel L_Password = new JLabel("Password");
                    JPasswordField TF_Password = new JPasswordField();
                    JLabel L_IP = new JLabel("IP");
                    JTextField TF_IP = new JTextField();
                    TF_IP.setText("localhost");

                    JPanel panel = new JPanel();
                    panel.setLayout(new GridLayout(3, 3));
                    panel.add(L_UserName); 
                    panel.add(TF_UserName);
                    panel.add(L_Password); 
                    panel.add(TF_Password);
                    panel.add(L_IP); 
                    panel.add(TF_IP);
                    JOptionPane.showMessageDialog(null, panel);
                    try {
                        if (TF_IP.getText().equals("") || TF_UserName.getText().equals("") || TF_Password.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Fill Details");
                        } else {
                            int found = 0;
                            String st = null;
                            File in = new File("File.txt");   
                            Scanner s = new Scanner(in);
                            while (s.hasNext()) {
                                st = s.next();
                                //System.out.println(st);
                                if (TF_UserName.getText().equals(st)) {
                                    st = s.next();
                                    found = 1;
                                    break;
                                } else {
                                    st = s.next();
                                }
                            }
                            if (found == 0) {
                                JOptionPane.showMessageDialog(null, "First Signup");
                            } else {
                                if (TF_Password.getText().equals(st)) {
                                    Connect(TF_IP.getText());
                                } else {
                                    JOptionPane.showMessageDialog(null, "Login failed!!!Password do not match");
                                }
                            }
                        }
                    } catch (Exception ex) {
                            System.out.println(ex);
                    }
                    
                }
            }
        );
        
        B_Help.addActionListener(
            new ActionListener (){
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Multi Chat Client©SHUBHAM NANDA 2015\nPress Connect : To Connect to Server\nPress Send : To broadcast message to all available\nPress Disconnect : To Disconnect from Server");
                }
            }
        );
        
        B_Signup.addActionListener(
            new ActionListener (){
                public void actionPerformed(ActionEvent e) {
                    JLabel L_UserName = new JLabel("UserName");
                    //JTextField TF_UserName = new JTextField();
                    JLabel L_Password = new JLabel("Password");
                    JPasswordField TF_Password = new JPasswordField();
                    JLabel L_CPassword = new JLabel("Confirm Password");
                    JPasswordField TF_CPassword = new JPasswordField();

                    JPanel panel = new JPanel();
                    panel.setLayout(new GridLayout(3, 3));
                    panel.add(L_UserName); 
                    panel.add(TF_UserName);
                    panel.add(L_Password); 
                    panel.add(TF_Password);
                    panel.add(L_CPassword); 
                    panel.add(TF_CPassword);
                    JOptionPane.showMessageDialog(null, panel);
                    try {
                        if (TF_CPassword.getText().equals("") || TF_UserName.getText().equals("") || TF_Password.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Missing Details");
                        } else {
                            int found = 0;
                            String p = "", st = null;
                            File in = new File("File.txt");   
                            Scanner s = new Scanner(in);
                            while (s.hasNext()) {
                                st = s.next();
                                //System.out.println(st);
                                if (TF_UserName.getText().equals(st)) {
                                    found = 1;
                                    break;
                                } else {
                                    p += st;
                                    st = s.next();
                                    p += " " + st + "\n";
                                }
                            }
                            if (found == 1) {
                                JOptionPane.showMessageDialog(null, "UserName already exists");
                            } else {
                                FileOutputStream out = new FileOutputStream("File.txt");
                                PrintWriter pw = new PrintWriter(out);
                                if (TF_Password.getText().equals(TF_CPassword.getText())) {
                                    pw.println(p + TF_UserName.getText() + " " + TF_Password.getText());
                                    JOptionPane.showMessageDialog(null, "Successfully signed in");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Password do not match");
                                }
                                pw.close();
                                out.close();
                            }
                        }
                    } catch (Exception ex) {
                            System.out.println(ex);
                    }
                }
            }
        );
        B_Show.addActionListener(
            new ActionListener (){
                public void actionPerformed(ActionEvent e) {
                    
                }
            }
        );
    }
        
    private static Client_Read_Write_Thread ChatClient;
    public static String UserName = "Anonymous";
    
    public static JFrame MainWindow = new JFrame();
    private static JButton B_Show = new JButton();
    private static JButton B_Signup = new JButton();
    private static JButton B_Connect = new JButton();
    private static JButton B_Disconnect = new JButton();
    private static JButton B_Send = new JButton();
    private static JButton B_Help = new JButton();
    private static JLabel L_TextMessage = new JLabel("Message: ");
    public static JTextField TF_TextMessage = new JTextField(20);
    private static JLabel L_Chat = new JLabel();
    public static JTextArea TA_Chat = new JTextArea();
    private static JScrollPane SP_Chat = new JScrollPane();
    //private static JLabel L_Online = new JLabel();
    //public static JList JL_Online = new JList();
    //private static JScrollPane SP_Online = new JScrollPane();
    private static JLabel L_LoggedInAsBox = new JLabel();
    
    public static JFrame LogInWindow = new JFrame();
    public static JTextField TF_UserName = new JTextField(14);
}
