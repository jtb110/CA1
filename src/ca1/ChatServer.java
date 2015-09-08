/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca1;

import Fisse.ProtocolStrings;
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
  public void send(String msg){
      for(int i=0; i<cl.size();i++){
          cl.get(i).send(msg.toUpperCase());
      }
  }
  private static void handleClient(Socket socket) throws IOException {
    Scanner input = new Scanner(socket.getInputStream());
    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

    String message = input.nextLine(); //IMPORTANT blocking call
    Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ",message));
    while (!message.equals(ProtocolStrings.STOP)) {
      writer.println(message.toUpperCase());
      Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ",message.toUpperCase()));
      message = input.nextLine(); //IMPORTANT blocking call
    }
    writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
    socket.close();
    Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
  }
  public void  removeHandler(HandleClient hc){
  cl.remove(hc);
  }
  private void runServer()
  {
    int port = Integer.parseInt(properties.getProperty("port"));
    String ip = properties.getProperty("serverIp");
    
    Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Sever started. Listening on: "+port+", bound to: "+ip);
    try {
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(ip, port));
      do {
        Socket socket = serverSocket.accept(); //Important Blocking call
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connected to a client");    
        HandleClient Hc = new HandleClient(socket,this);
        Hc.start();
        cl.add(Hc);
//        handleClient(socket);
      } while (keepRunning);
    } 
    catch (IOException ex) {
      Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void main(String[] args) {
    new ChatServer().runServer();
  }

    @Override
    public void update(Observable o, Object o1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
