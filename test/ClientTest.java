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
    public void testClient(){
        String address = "localhost";
        int port=9090;
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
