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
 * Cette classe permet de test la classe Serveur du projet
 * @author Briguet
 * @version 1.0
 */
public class Test {
    
    
    
    /**
     * Lance une mutlitude de test sur le serveur API et affiche les tests qui échoue
     * @param args Correspond aux éventuelles arguments
     */
    public static void main(String[] args) {
        boolean debugResult = false;
        boolean debug = false;
        String base_url = "http://127.0.0.1:8085/APITest/";
        java.util.List<T> testsFailed = new java.util.ArrayList<>();
        T[] tests = getTest();
        for(int i=0;i<tests.length;i++){
            
            try {
                if(!debugResult) Client.sendRequest(base_url+tests[i].method+tests[i].parameter, tests[i].object);
                else System.out.println(Client.sendRequest(base_url+tests[i].method+tests[i].parameter, tests[i].object));
                if(!tests[i].expectedResult){
                    testsFailed.add(tests[i]);
                }
            } catch (java.net.MalformedURLException | ErrorException | ErrorConnectionException ex) {
                if(tests[i].expectedResult){
                    testsFailed.add(tests[i]);
                }
                if(debug){
                    if(ex instanceof ErrorException)
                        System.err.print(((ErrorException) ex).error+" ");
                    java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
            
        }
        
        for(T t : testsFailed){
            System.out.println(t);
        }
    }
    
    
    
//METHODE PRIVATE
    /**
     * Renvoie la liste des tests à affectuer sur le serveur
     * @return Retourne la liste des tests à effectuer sur le serveur
     */
    private static T[] getTest(){
        T[] tests = new T[]{
            new T("a", true, "", null),
            new T("b", true, "", null),
            new T("c", true, "", null),
            new T("d", true, "", null),
            new T("e", true, "", null),
            new T("a", false, "?", null),
            new T("b", false, "?", null),
            new T("c", false, "?", null),
            new T("d", false, "?", null),
            new T("e", false, "?", null),
            new T("a", false, "?id", null),
            new T("b", false, "?id", null),
            new T("c", false, "?id", null),
            new T("d", false, "?id", null),
            new T("e", false, "?id", null),
            new T("a", false, "?id=", null),
            new T("b", false, "?id=", null),
            new T("c", false, "?id=", null),
            new T("d", false, "?id=", null),
            new T("e", false, "?id=", null),
            new T("a", false, "", new int[]{}),
            new T("b", false, "", new int[]{}),
            new T("c", false, "", new int[]{}),
            new T("d", false, "", new int[]{}),
            new T("e", false, "", new int[]{}),
            new T("a", false, "?", new int[]{}),
            new T("b", false, "?", new int[]{}),
            new T("c", false, "?", new int[]{}),
            new T("d", false, "?", new int[]{}),
            new T("e", false, "?", new int[]{}),
            new T("a", false, "?id", new int[]{}),
            new T("b", false, "?id", new int[]{}),
            new T("c", false, "?id", new int[]{}),
            new T("d", false, "?id", new int[]{}),
            new T("e", false, "?id", new int[]{}),
            new T("a", false, "?id=", new int[]{}),
            new T("b", false, "?id=", new int[]{}),
            new T("c", false, "?id=", new int[]{}),
            new T("d", false, "?id=", new int[]{}),
            new T("e", false, "?id=", new int[]{}),
            new T("a", false, "?id=1", new int[]{}),
            new T("b", false, "?id=1", new int[]{}),
            new T("c", false, "?id=1", new int[]{}),
            new T("d", false, "?id=1", new int[]{}),
            new T("e", false, "?id=1", new int[]{}),
            new T("a", false, "", new User(0, "", "")),
            new T("b", false, "", new User(0, "", "")),
            new T("c", false, "", new User(0, "", "")),
            new T("d", false, "", new User(0, "", "")),
            new T("e", false, "", new User(0, "", "")),
            new T("a", false, "?", new User(0, "", "")),
            new T("b", false, "?", new User(0, "", "")),
            new T("c", false, "?", new User(0, "", "")),
            new T("d", false, "?", new User(0, "", "")),
            new T("e", false, "?", new User(0, "", "")),
            new T("a", false, "?id", new User(0, "", "")),
            new T("b", false, "?id", new User(0, "", "")),
            new T("c", false, "?id", new User(0, "", "")),
            new T("d", false, "?id", new User(0, "", "")),
            new T("e", false, "?id", new User(0, "", "")),
            new T("a", false, "?id=", new User(0, "", "")),
            new T("b", false, "?id=", new User(0, "", "")),
            new T("c", false, "?id=", new User(0, "", "")),
            new T("d", false, "?id=", new User(0, "", "")),
            new T("e", false, "?id=", new User(0, "", "")),
            new T("a", false, "?id=1", new User(0, "", "")),
            new T("b", false, "?id=1", new User(0, "", "")),
            new T("c", false, "?id=1", new User(0, "", "")),
            new T("d", false, "?id=1", new User(0, "", "")),
            new T("e", false, "?id=1", new User(0, "", "")),
            new T("f", true, "?z=127", null),
            new T("f", false, "?z=128", null),
            new T("f", false, "?z=2a7", null),
            new T("g", false, "?z=&y=", null),
            new T("g", false, "?z=127&y=", null),
            new T("g", false, "?z=128&y=127", null),
            new T("g", true, "?z=127&y=127", null),
            new T("g", true, "?z=127&z=127", null),
            new T("g", false, "?z=127&y=1a7", null),
            new T("g", false, "?z=127", null),
            new T("g", false, "?z=127&z=127&z=127", null),
            new T("h", false, "?z=&y=", null),
            new T("h", false, "?z=127&y=", null),
            new T("h", true, "?z=128&y=127", null),
            new T("h", true, "?z=127&y=127", null),
            new T("h", true, "?z=127&z=127", null),
            new T("h", false, "?z=127&y=1a7", null),
            new T("h", true, "?z=1a7&y=127", null),
            new T("h", false, "?z=127", null),
            new T("h", false, "?z=127&z=127&z=127", null),
            new T("i", true, "?z=&y=", null),
            new T("i", true, "?z=127&y=", null),
            new T("i", true, "?z=128&y=127", null),
            new T("i", true, "?z=127&y=127", null),
            new T("i", true, "?z=127&z=127", null),
            new T("i", true, "?z=127&y=1a7", null),
            new T("i", true, "?z=1a7&y=127", null),
            new T("i", false, "?z=127", null),
            new T("i", false, "?z=127&z=127&z=127", null),
            new T("j", false, "?z=&y=", null),
            new T("j", true, "?z=127&y=", null),
            new T("j", false, "?z=128&y=127", null),
            new T("j", true, "?z=127&y=127", null),
            new T("j", true, "?z=127&z=127", null),
            new T("j", true, "?z=127&y=1a7", null),
            new T("j", false, "?z=1a7&y=127", null),
            new T("j", false, "?z=127", null),
            new T("j", false, "?z=127&z=127&z=127", null),
            new T("k", true, "?i=Bonjour&j=false&k=2.3&l=3.14&m=500000000000&n=250000000&o=27000&p=-128", null),
            new T("k", false, "?i=Bonjour&j=true&k=2.3&l=3.14&m=25000000&n=500000000000&o=27000&p=-128", null),
            new T("k", false, "?i=Bonjour&j=fal&k=2.3&l=3.14&m=25000000&n=500000000000&o=27000&p=-128", null),
            new T("k", false, "?i=Bonjour&j=true&k=2.3&l=3.14.3&m=25000000&n=500000000000&o=27000&p=-128", null),
            new T("k", false, "?i=Bonjour&j=true&k=2,3&l=3.14&m=25000000&n=500000000000&o=27000&p=-128", null),
            new T("l", true, "", 15),
            new T("l", false, "", new int[]{}),
            new T("l", false, "", new int[]{153, 256}),
            new T("l", false, "", new long[]{500000000000L, 500000000000L}),
            new T("m", false, "", 15),
            new T("m", true, "", new int[]{}),
            new T("m", true, "", new int[]{153, 256}),
            new T("m", false, "", new long[]{500000000000L, 500000000000L}),
            new T("n", false, "", true),
            new T("n", true, "", new boolean[]{}),
            new T("n", true, "", new boolean[]{false, true}),
            new T("n", false, "", new long[]{500000000000L, 500000000000L}),
            new T("o", false, "", true),
            new T("o", false, "", "Bonjour"),
            new T("o", true, "", new User(0, "test", "test")),
            new T("o", false, "", new User[]{}),
            new T("o", false, "", new User[]{new User(0, "test0", "test0"), new User(1, "test1", "test1")}),
            new T("o", false, "", new long[]{500000000000L, 500000000000L}),
            new T("p", false, "", true),
            new T("p", false, "", "Bonjour"),
            new T("p", false, "", new User(0, "test", "test")),
            new T("p", true, "", new User[]{}),
            new T("p", true, "", new User[]{new User(0, "test0", "test0"), new User(1, "test1", "test1")}),
            new T("p", false, "", new long[]{500000000000L, 500000000000L}),
            new T("q", false, "?", new byte[]{}),
            new T("q", false, "?a", new byte[]{}),
            new T("q", false, "?a=", new byte[]{}),
            new T("q", false, "?a=128", new byte[]{}),
            new T("q", true, "?a=127", new byte[]{}),
            new T("q", false, "?a=127", null),
            new T("q", false, "?a=127", 53),
            new T("q", false, "?a=127&b", new byte[]{}),
            new T("q", false, "?a=127&b=", new byte[]{}),
            new T("q", false, "?a=127&b=3", new byte[]{}),
            new T("q", true, "?a=127", new byte[]{3}),
            new T("q", true, "?a=127", new byte[]{3, 2}),
            new T("q", true, "?a=127", new int[]{3, 2}),
            new T("q", false, "?a=127", new int[]{128, 2}),
            new T("r", false, "?", 2),
            new T("r", false, "?a", 2),
            new T("r", false, "?a=", 2),
            new T("r", true, "?a=3", 2),
            new T("r", true, "?a=3", -128),
            new T("r", false, "?a=3", -129),
            new T("r", false, "?a=3", new byte[]{}),
            new T("r", false, "?a=3", new byte[]{3}),
            new T("r", false, "?a=3", new byte[]{3, 4}),
            new T("r", true, "?a=3", 0),
            new T("r", true, "?a=3&", 0),
            new T("r", false, "?a=3&b", 0),
            new T("r", false, "?a=3&b=", 0),
            new T("r", false, "?a=3&b=4", 0),
            new T("s", false, "?a=3&b=4", 0),
            new T("s", false, "?a=3&b=4", -128),
            new T("s", false, "?a=3&b=4", -129),
            new T("s", true, "?a=3&b=4", new byte[]{3, 4}),
            new T("s", true, "?a=3&b=4", new byte[]{}),
            new T("s", false, "?a=3&b=4", null),
            new T("s", true, "?a=3&b=4&", new byte[]{}),
            new T("s", false, "?a=3&b=4&c", new byte[]{}),
            new T("s", false, "?a=3", new byte[]{}),
            new T("s", false, "?a=3&", new byte[]{}),
            new T("s", false, "?a=3&b", new byte[]{}),
            new T("s", false, "?a=3&b=", new byte[]{}),
            new T("t", true, "?a=3&b=4", 0),
            new T("t", false, "?a=3&b=", 0),
            new T("t", false, "?a=3&b", 0),
            new T("t", false, "?a=3&", 0),
            new T("t", false, "?a=3", 0),
            new T("t", false, "?a=3&b=4", new byte[]{}),
            new T("t", false, "?a=3&b=4", new byte[]{1}),
            new T("t", false, "?a=3&b=4", new int[]{1}),
            new T("t", false, "?a=3&b=4", 128),
            new T("t", true, "?a=3&b=4", 127),
            new T("t", true, "?a=3&b=4", 5),
            new T("t", true, "?a=3&b=4&", 5),
            new T("t", false, "?a=3&b=4&c", 5),
            new T("t", false, "?a=3&b=4&c=", 5),
            new T("t", false, "?a=3&b=4&c=a", 5),
            new T("t", false, "?a=3&b=bienvenue", 5),
            new T("t", false, "?a=3&b=4", new User(0, "test", "test")),
            new T("u", true, "?a=3&b=4", new User(0, "test", "test")),
            new T("u", false, "?a=3&b=128", new User(0, "test", "test")),
            new T("u", false, "?a=3&b=4", 14),
            new T("u", false, "?a=3&b=4", 255),
            new T("u", false, "?a=3&b=4", new int[]{0}),
            new T("u", false, "?a=3&b=4", new User[]{}),
            new T("u", false, "?a=3&b=4", new User[]{new User(0, "test", "test")}),
            new T("u", false, "?a=3&b=4", new User[]{new User(0, "test", "test"), new User(0, "test", "test")}),
            new T("u", true, "?a=3&b=127&", new User(0, "test", "test")),
            new T("u", false, "?a=3&b=128&", new User(0, "test", "test")),
            new T("u", false, "?a=3&b=127&c", new User(0, "test", "test")),
            new T("u", false, "?a=3&b=", new User(0, "test", "test")),
            new T("v", false, "?a=3&b=4", new User(0, "test", "test")),
            new T("v", false, "?a=3&b=128", new User(0, "test", "test")),
            new T("v", false, "?a=3&b=4", 14),
            new T("v", false, "?a=3&b=4", 255),
            new T("v", false, "?a=3&b=4", new int[]{0}),
            new T("v", true, "?a=3&b=4", new User[]{}),
            new T("v", true, "?a=3&b=4", new User[]{new User(0, "test", "test")}),
            new T("v", true, "?a=3&b=4", new User[]{new User(0, "test", "test"), new User(0, "test", "test")}),
            new T("v", true, "?a=3&b=127&", new User[]{new User(0, "test", "test"), new User(0, "test", "test")}),
            new T("v", false, "?a=3&b=128&", new User[]{new User(0, "test", "test"), new User(0, "test", "test")}),
            new T("v", false, "?a=3&b=127&c", new User[]{new User(0, "test", "test"), new User(0, "test", "test")}),
            new T("v", false, "?a=3&b=", new User[]{new User(0, "test", "test"), new User(0, "test", "test")}),
        };
        
        return tests;
    }
    
    
    
//CLASS
    /**
     * Cette classe permet de créer un objet test
     */
    private static class T{
        
        
        
    //ATTRIBUTS
        /**
         * Correspond au nom de la méthode de l'API qui va être testée
         */
        public String method;
        /**
         * Détermine si le test doit réussir ou pas pour pouvoir le valider
         */
        public boolean expectedResult;
        /**
         * Correspond aux paramètres du client envoyés vers le serveur, puis devant être redirigé sur la méthode
         */
        public String parameter;
        /**
         * Correspond à l'objet du client envoyé vers le serveur, puis devant être redirigé sur la méthode
         */
        public Object object;

        
        
    //CONSTRUCTOR
        /**
         * Crée un objet test
         * @param method Correspond au nom de la méthode de l'API qui va être testée
         * @param expectedResult Détermine si le test doit réussir ou pas pour pouvoir le valider
         * @param parameter Correspond aux paramètres du client envoyés vers le serveur, puis devant être redirigé sur la méthode
         * @param object Correspond à l'objet du client envoyé vers le serveur, puis devant être redirigé sur la méthode
         */
        public T(String method, boolean expectedResult, String parameter, Object object) {
            this.method = method;
            this.expectedResult = expectedResult;
            this.parameter = parameter;
            this.object = object;
        }

        
        
    //METHODE PUBLIC
        /**
         * Renvoie le test sous forme de chaîne de caractères
         * @return Retourne le test sous la forme d'une chaîne de caractères
         */
        @Override
        public String toString() {
            return "T{" + "method=" + method + ", expectedResult=" + expectedResult + ", parameter=" + parameter + ", object=" + object + '}';
        }
        
        
        
    }
    
    
    
}