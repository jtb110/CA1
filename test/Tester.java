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
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
