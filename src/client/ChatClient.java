/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 *
 * @author noncowi
 */
public class ChatClient extends Observable implements Runnable {

    boolean listening;
    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    String msg;

    public ChatClient() {
        listening = true;
    }

    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, this.port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
    }

    public void sendUsername(String msg) {
        this.msg = "USER#" + msg;
        output.println(this.msg);
    }

    @Override
    public void run() {
        String inputmsg;
        while (listening) {
            inputmsg = input.nextLine();
            setChanged();
            notifyObservers(inputmsg);
        }
    }

    private void sendToAll(String msg) {
        this.msg = "MSG#*#" + msg;
        output.println(this.msg);
    }

    private void sendToUsers(String msg, String users) {
        this.msg = "MSG#" + users + "#" + msg;
        output.println(this.msg);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        ChatClient client = new ChatClient();
        client.connect("localhost", 9090);
        client.addObserver(new Observer() {

            @Override
            public void update(Observable o, Object o1) {
                System.out.println(o1);
            }
        });
        new Thread(client).start();
        Thread.sleep(500);
        client.sendUsername("kurt");
        client.sendToAll("hej");
        client.sendToUsers("hej med jer","pelle,kurt,jonas");
    }

}