package serverRelated;

import shared.ProtocolStrings;
import serverRelated.ChatServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author noncowi
 */
public class HandleClient extends Observable implements Runnable {

    Scanner input;
    PrintWriter writer;
    Socket s;
    ChatServer chat;
    String clientName;

    public HandleClient(Socket socket, ChatServer cs) throws IOException {
        s = socket;
        this.chat = cs;
        addObserver(cs);
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);

    }

    public void send(String msg) {
        writer.println(msg);
    }

    public void setClientName(String name) {

        this.clientName = name;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void run() {
//        writer.println("MSG#Server#you are now logged into the chatserver. Write ur username");
        String message = input.nextLine(); //IMPORTANT blocking call
        String[] msg = message.split("#");
        if (msg[0].equals("USER")) {
            setClientName(msg[1]);
            setChanged();
            hasChanged();
            notifyObservers(clientName);
        }
        message = input.nextLine();
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(ProtocolStrings.STOP)) {
            msg = message.split("#");
            if (msg[0].equals("MSG")){
            chat.send(message, this);
            }else{
                send("MSG#Server#Plz use the right protocol");
            }

//            writer.println(message.toUpperCase());
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
            
//            chat.send(message, this);
        }
        

            writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
            setChanged();
            hasChanged();
            
            chat.removeHandler(this);
            notifyObservers();
            try {
                s.close();
                Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
            } catch (IOException ex) {
                Logger.getLogger(HandleClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
}
