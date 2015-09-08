/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca1;

import java.util.Properties;
import utils.Utils;

/**
 *
 * @author Jonas
 */
public class ChatServer {

    private static final Properties properties = Utils.initProperties("server.properties");

    public void runServer() {

        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
