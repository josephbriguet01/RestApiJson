/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package test;



import com.jasonpercus.restapijson.Client;
import com.jasonpercus.restapijson.exception.ErrorConnectionException;
import com.jasonpercus.restapijson.exception.ErrorException;



/**
 * Cette classe permet de tester un utilisateur client
 * @author Briguet
 * @version 1.0
 */
public class ClientTest {
    
    
    
    /**
     * Lance une demande au serveur API
     * @param args Correspond aux éventuelles arguments donnés
     */
    public static void main(String[] args){
        try {
            System.out.println(Client.sendRequest("http://127.0.0.1:8085/APITest/b"));
        } catch (java.net.MalformedURLException | ErrorException | ErrorConnectionException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    
    
}