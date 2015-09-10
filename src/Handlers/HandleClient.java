package Handlers;

import Fisse.ProtocolStrings;
import ca1.ChatServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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
public class HandleClient extends Thread {

    Scanner input;
    PrintWriter writer;
    Socket s;
    ChatServer chat;
    String clientName;

    public HandleClient(Socket socket, ChatServer cs) throws IOException {
        s = socket;
        this.chat = cs;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);

    }

    public void send(String msg) {
        writer.println(msg);
    }

    public String getClientName() {
        return this.clientName;
    }

    public void run() {
        writer.println("you are now logged into the chatserver. Write ur username");
        clientName = input.nextLine(); //IMPORTANT blocking call

        String message = input.nextLine(); //IMPORTANT blocking call

        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(ProtocolStrings.STOP)) {

            chat.send(message, this);

//            writer.println(message.toUpperCase());
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
        }
        {

            writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
            try {
                s.close();
                chat.removeHandler(this);
                Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
            } catch (IOException ex) {
                Logger.getLogger(HandleClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
