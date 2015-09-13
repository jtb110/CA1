/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import serverRelated.ChatServer;
import client.ChatClient;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import serverRelated.ChatServer;

/**
 *
 * @author Jonas
 */
public class Tester {
    
    public Tester() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
        new Thread(new Runnable(){
            public void run(){
                ChatServer.main(null);
            }
            
        }).start();
    }
    
    @AfterClass
    public static void tearDownClass() {
        ChatServer.stopServer();
    }
    
    @Test
    public void msgProtocol() throws Exception{
        String ip = "localhost";
        int port = 9090;
        
        Socket soc = new Socket(ip, port);
        PrintWriter pw = new PrintWriter(soc.getOutputStream());
        Scanner scan = new Scanner(soc.getInputStream());
        
        pw.println("USER#Test");
        String result = scan.nextLine();
        result = scan.nextLine();
        pw.println("USER#Test#Kan du se dette?");
        result = scan.nextLine();
        assertEquals("USER#Test#Kan du se dette?", result);
        pw.println("STOP#");
        soc.close();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
