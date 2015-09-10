/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca1;

import Shared.ProtocolStrings;
import Handlers.HandleClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

public class ChatServer implements Observer {

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties");
    ArrayList<HandleClient> cl = new ArrayList<>();

    public static void stopServer() {
        keepRunning = false;
    }
    
    public void send(String msg, HandleClient client) {
        String[] out = msg.split("#");
        String[] clientNames = out[1].split(",");
        int j = 0;
        System.out.println("jeg er nu inde i chatservers hej");
        
        if (out[1].equals("*")) {
                System.out.println("jeg vil sende til alle");
            for (int i = 0; i < cl.size(); i++) {
                cl.get(i).send("MSG#"+client.getClientName()+"#: "+out[2]);
            }
        }else if (clientNames.length == 1&& !clientNames[0].equals("*")){
            for (int i = 0; i < cl.size(); i++) {
                System.out.println("jeg vil sende til en");
                System.out.println(clientNames[0]);
                if(cl.get(i).getClientName().equals(clientNames[0])){
                    HandleClient client1 = cl.get(i);
                    System.out.println("vi sender om lidt");
                            client1.send("MSG#"+client.getClientName()+"#: "+out[2]);
                
                System.out.println("nu prøver jeg at sende til en");
                System.out.println(out[2]);
            }
            }
        }else
        {
            
                System.out.println("jeg vil sende til flere");
            while(j<clientNames.length){
            for (int i = 0; i < cl.size(); i++){
                if(cl.get(i).getClientName().equals(clientNames[j]))
                cl.get(i).send("MSG#"+client.getClientName()+"#: "+out[2]);
            }
            j++;
            }
        }
            
        

    }

    private static void handleClient(Socket socket) throws IOException {
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(ProtocolStrings.STOP)) {
            writer.println(message.toUpperCase());
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
        }
        writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
        socket.close();
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
    }

    public void removeHandler(HandleClient hc) {
        cl.remove(hc);
    }

    private void runServer() {
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");

        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Sever started. Listening on: " + port + ", bound to: " + ip);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call'

                Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connected to a client");
                HandleClient Hc = new HandleClient(socket, this);
                new Thread (Hc).start();
                cl.add(Hc);
//        handleClient(socket);
            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
    new ChatServer().runServer();
//        String pik = "hej#pik#hej2";
//        String[] pik2 = pik.split("#");
//        System.out.println(pik2.length);
        
    }

    @Override
    public void update(Observable o, Object o1) {
        String nameList= "";
        for (int i = 0; i < cl.size(); i++) {
                
                nameList+=cl.get(i).getClientName()+",";
            }
        for (int i = 0; i < cl.size(); i++) {
                cl.get(i).send("USERLIST#"+nameList);
            }
        
    }
}
