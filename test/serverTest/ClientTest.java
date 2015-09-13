package serverTest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import serverRelated.ChatServer;

/**
 *
 * @author noncowi
 */
public class ClientTest {
    ChatServer cs = new ChatServer();
    public ClientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
       new Thread(new Runnable(){
           @Override
           public void run(){
               ChatServer.main(null);
           }
       }).start();
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        ChatServer.stopServer();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        
    }
@Test
public void testMsgFromClientToClient() throws IOException{
    String address = "localhost";
        int port = 9090;

        Socket s = new Socket(address, port);
        Scanner sc = new Scanner(s.getInputStream());
        PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
        Socket s2 = new Socket(address, port);
        Scanner sc2 = new Scanner(s2.getInputStream());
        PrintWriter writer2 = new PrintWriter(s2.getOutputStream(), true);

        
        writer.println("USER#pelle");
        writer2.println("USER#kurt");
        sc2.nextLine();
        String result;

        writer.println("MSG#kurt#Hej");

        result = sc2.nextLine();

        assertEquals("MSG#pelle#Hej", result);
        writer.println("STOP#");
        s.close();
}
    @Test
    public void testClient() throws IOException{
           String address = "localhost";
        int port = 9090;

        Socket s = new Socket(address, port);
        Scanner sc = new Scanner(s.getInputStream());
        PrintWriter writer = new PrintWriter(s.getOutputStream(), true);

        writer.println("USER#pelle");
        sc.nextLine();
        String result;

        writer.println("MSG#pelle#Hej");

        result = sc.nextLine();

        assertEquals("MSG#pelle#Hej", result);
        writer.println("STOP#");
        s.close();
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
