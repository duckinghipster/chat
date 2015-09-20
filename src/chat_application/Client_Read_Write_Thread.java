package chat_application;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author SHUBHAM NANDA
 */
public class Client_Read_Write_Thread implements Runnable{
    Socket Sock;
    Scanner IO;
    Scanner Send = new Scanner(System.in);
    PrintWriter OUT;
    
    public Client_Read_Write_Thread(Socket X) {
        this.Sock = X;
    }
    
    public void run() {
        try {
            try {
                IO = new Scanner(Sock.getInputStream());
                OUT = new PrintWriter(Sock.getOutputStream());
                OUT.flush();
                
                while (true) {
                    String Message = IO.nextLine();
                    if (Message.contains("#?!")) {
                        String Temp1 = Message.substring(3);
                        Temp1 = Temp1.replace("[", "");
                        Temp1 = Temp1.replace("]", "");

                        String[] CurrentUsers = Temp1.split(", ");
                        if (!Temp1.equals(ClientGUI.UserName))
                            ClientGUI.TA_Chat.append(Message + " connected\n");
                    } else {
                        if (Message.contains("has disconnected...XXX!!!"))
                            ClientGUI.TA_Chat.append(Message + "\n");
                        else {
                            //System.out.println(Message);
                            int k = Message.indexOf(':');
                            String Encr_msg = Message.substring(k + 2, Message.length());
                            //System.out.println(Encr_msg);
                            String Decr_msg = AES.decrypt(Encr_msg);
                            
                            ClientGUI.TA_Chat.append(Message.subSequence(0, k) + ": " + Decr_msg + "\n");
                        };
                    }
                }
            }
            finally {
                Sock.close();
            }
        } catch (Exception X) {
            Thread.currentThread().isInterrupted();
        }
    }
    
    public void Disconnect() throws IOException{
        OUT.println(ClientGUI.UserName + ": has disconnected...XXX!!!");
        OUT.flush();
        Sock.close();
        JOptionPane.showMessageDialog(null, ClientGUI.UserName + " Disconnected!!!");
        System.exit(0);
    }
    
    public void Send(String X) throws Exception {
        ClientGUI.TA_Chat.append("Me: " + X + "\n");
        String Enc_msg = AES.encrypt(X);
        //System.out.println(Enc_msg);
        OUT.println(ClientGUI.UserName + ": " + Enc_msg);
        OUT.flush();
        ClientGUI.TF_TextMessage.setText("");
    }
}
