package chat_application;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author SHUBHAM NANDA
 */
public class Server_Read_Write_Thread implements Runnable{
    Socket Sock;
    private Scanner IO;
    private PrintWriter OUT;
    String Message = "";
    
    public Server_Read_Write_Thread(Socket X) {
        this.Sock = X;
    }

    public void run() {
        try {
            try {
                IO = new Scanner(Sock.getInputStream());
                OUT = new PrintWriter(Sock.getOutputStream());
                
                while (true) {
                    CheckConnection();
                    if (!IO.hasNext()) {
                        return;
                    }
                    
                    Message = IO.nextLine();
                    System.out.println("Client said: " + Message);
                    int r = -1;
                    for (int i = 1; i <= Server.SocketArray.size(); i++) {
                        int k = Message.indexOf(':');
                        if(!Message.subSequence(0, k).equals(Server.CurrentUsers.get(i - 1))){
                            Socket TempSock = (Socket) Server.SocketArray.get(i - 1);
                            PrintWriter Temp_OUT = new PrintWriter(TempSock.getOutputStream());
                            Temp_OUT.println(Message);
                            Temp_OUT.flush();
                            System.out.println("Sent to client: " + Server.CurrentUsers.get(i - 1));
                        } else {
                            if (Message.contains("has disconnected...XXX!!!"))
                                r = i - 1;
                        }
                    }
                    if (r != -1) {
                        Server.CurrentUsers.remove(r);
                        Server.SocketArray.remove(r);
                    }
                }
            } finally {
                //Sock.close();
            }
        } catch (Exception X) {
            System.out.println(X);
        }
    }

    public void CheckConnection() throws IOException {
        if (!Sock.isConnected()) {
            for (int i = 1; i <= Server.SocketArray.size(); i++) {
                if (Server.SocketArray.get(i) == Sock) {
                    Server.SocketArray.remove(i);
                }
            }
                
            for (int i = 1; i <= Server.SocketArray.size(); i++) {
                Socket TempSock = (Socket) Server.SocketArray.get(i - 1);
                PrintWriter Temp_OUT = new PrintWriter(TempSock.getOutputStream());
                Temp_OUT.println(TempSock.getLocalAddress().getHostName() + "disconnected...");
                Temp_OUT.flush();
                System.out.println(TempSock.getLocalAddress().getHostName() + "disconnected...");
            }
        }
    }
}
