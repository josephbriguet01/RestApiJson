/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package test;



import com.jasonpercus.encryption.aes.AES;
import com.jasonpercus.encryption.aes.KeyAES;
import com.jasonpercus.restapijson.IServer;
import com.jasonpercus.restapijson.Server;



/**
 * Cette classe permet de tester la mise en service d'un serveur API
 * @author Briguet
 * @version 1.0
 */
public class ServerTest implements IServer {
    
    
    
//MAIN
    /**
     * Crée et lance un serveur API
     * @param args Correspond aux éventuelles arguments donnés en paramètre
     */
    public static void main(String[] args) {
        new ServerTest().launch();
    }
    
    
    
//METHODES PUBLICS
    /**
     * Crée et lance un serveur API
     */
    private void launch(){
        try {
            Server server = new Server("SERVER", 8085, this, AES.class, KeyAES.class, 5, new APITest());
            server.start();
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(ServerTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /**
     * Lorsque le serveur API s'est bien lancé
     * @param id Correspond à l'id du serveur lancé
     */
    @Override
    public void serverRestIsStarted(String id) {
        System.out.println(id+" is started");
    }

    /**
     * Lorsque le serveur API s'est arrêté
     * @param id Correspond à l'id du serveur arrêté
     */
    @Override
    public void serverRestIsStopped(String id) {
        System.out.println(id+" is stopped");
    }
    
    
    
}