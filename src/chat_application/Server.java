package chat_application;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author SHUBHAM NANDA
 */
public class Server {
    public static ArrayList<Socket> SocketArray = new ArrayList<Socket>();
    public static ArrayList<String> CurrentUsers = new ArrayList<String>();
    
    public static void main (String[] args) throws IOException {
        try {
            final int port = 8989;
            ServerSocket Server = new ServerSocket(port);
            System.out.println("Waiting for the clients...");
            
            while (true) {
                Socket Sock = Server.accept();
                SocketArray.add(Sock);
                System.out.println("Client connected from: " + Sock.getLocalAddress().getHostName());
                AddUserName(Sock);
                
                Server_Read_Write_Thread Chat = new Server_Read_Write_Thread(Sock);
                Thread X = new Thread(Chat);
                X.start();
            }
            
        } catch (Exception X) {
            System.out.print(X);
        }
    }

    public static void AddUserName(Socket X) throws IOException{
        Scanner IO = new Scanner(X.getInputStream());
        String UserName = IO.nextLine();
        CurrentUsers.add(UserName);
        int l = CurrentUsers.lastIndexOf(UserName);
        System.out.println("#?!" + CurrentUsers.get(l) + " connected...");
        
        for (int i = 1; i <= Server.SocketArray.size(); i++) {
            Socket TempSock = (Socket) Server.SocketArray.get(i - 1);
            PrintWriter OUT = new PrintWriter(TempSock.getOutputStream());
            OUT.println("#?!" + CurrentUsers.get(l));
            OUT.flush();
        }
    }
}
