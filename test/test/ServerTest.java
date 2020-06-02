/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package test;



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
            Server.start(8085, this, new APITest());
            Thread.sleep(20000);
            Server.stop();
        } catch (java.io.IOException | InterruptedException ex) {
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