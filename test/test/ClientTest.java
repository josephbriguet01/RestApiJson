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
import com.jasonpercus.restapijson.ClientParam;
import com.jasonpercus.restapijson.exception.ErrorConnectionException;
import com.jasonpercus.restapijson.exception.ErrorException;



/**
 * Cette classe permet de tester un utilisateur client
 * @author Briguet
 * @version 1.0
 */
public class ClientTest {
    
    
    
//METHODE MAIN
    /**
     * Lance une demande au serveur API
     * @param args Correspond aux éventuelles arguments donnés
     */
    public static void main(String[] args){
        
        //Crée le client
        Client client = new Client("http://127.0.0.1:8085/");
        
        try {
            
            //Demande au serveur d'exécuter une tâche. Cela appellera la classe APITest puis la méthode b
            client.set("APITest/b");
            
            //Envoie plusieurs fois la même requête dans l'objectif de surcharger le serveur et voir sa résistance. Si cette même classe est utilisé plusieurs fois en même temps, cela test également, la résistance du serveur aux conflits de certificats
            for(int i=0;i<30000;i++){
                
                //Récupère un User renvoyé du serveur
                User u = (User)client.get("APITest/w", User.class, new P("Bertrand"), new P("Luc"), new P(15));
                
                //Affiche le user
                System.out.println(i + "\t\t" + u);
                
            }
            
        } catch (java.net.MalformedURLException | ErrorException | ErrorConnectionException ex) {
            if(ex instanceof ErrorException) System.err.println("ERROR "+((ErrorException)ex).getError());
            java.util.logging.Logger.getLogger(ClientTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }



    }
    
    
    
//CLASS
    /**
     * Cette classe permet de créer un paramètre. Celui-ci sera donné au client pour qu'il forme l'url.
     * En comparaison avec ClientParam, elle sert seulement à ne pas utiliser ClientParam et ainsi rendre plus visible le passage de paramètres au client (new P(), c'est plus petit que new ClientParam())
     */
    private static class P extends ClientParam{
        
        
        
    //CONSTRUCTOR
        /**
         * Crée un paramètre
         * @param b Correspond à la valeur du paramètre
         */
        public P(byte b) {
            super(b);
        }

        /**
         * Crée un paramètre
         * @param s Correspond à la valeur du paramètre
         */
        public P(short s) {
            super(s);
        }

        /**
         * Crée un paramètre
         * @param i Correspond à la valeur du paramètre
         */
        public P(int i) {
            super(i);
        }

        /**
         * Crée un paramètre
         * @param l Correspond à la valeur du paramètre
         */
        public P(long l) {
            super(l);
        }

        /**
         * Crée un paramètre
         * @param f Correspond à la valeur du paramètre
         */
        public P(float f) {
            super(f);
        }

        /**
         * Crée un paramètre
         * @param d Correspond à la valeur du paramètre
         */
        public P(double d) {
            super(d);
        }

        /**
         * Crée un paramètre
         * @param b Correspond à la valeur du paramètre
         */
        public P(boolean b) {
            super(b);
        }

        /**
         * Crée un paramètre
         * @param s Correspond à la valeur du paramètre
         */
        public P(String s) {
            super(s);
        }
        
        
        
    }
    
    
    
}