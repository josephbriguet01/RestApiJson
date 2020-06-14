/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Juin 2020
 */
package com.jasonpercus.restapijson;



/**
 * Cette classe permet de créer un paramètre. Celui-ci sera donné au client pour qu'il forme l'url.
 * @author Briguet
 * @version 1.0
 */
public class ClientParam implements java.io.Serializable, Comparable<ClientParam> {
    
    
    
//ATTRIBUTS
    /**
     * Correspond au nom de la clef du paramètre
     */
    private String key;
    
    /**
     * Correspond à l'objet primitif donnés par l'utilisateur (byte, short, int, long, float, double, boolean, String)
     */
    private Object obj;

    
    
//CONSTRUCTORS
    /**
     * Crée un paramètre vide (ne peut être utilisé par le développeur)
     */
    private ClientParam() {
        
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
        
    }
    
    /**
     * Crée un paramètre
     * @param b Correspond à la valeur du paramètre
     */
    public ClientParam(byte b) {
        this.key = getGenerateChain(10);
        this.obj = b;
        
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
    }
    
    /**
     * Crée un paramètre
     * @param s Correspond à la valeur du paramètre
     */
    public ClientParam(short s) {
        this.key = getGenerateChain(10);
        this.obj = s;
        
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
    }
    
    /**
     * Crée un paramètre
     * @param i Correspond à la valeur du paramètre
     */
    public ClientParam(int i) {
        this.key = getGenerateChain(10);
        this.obj = i;
        
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
    }
    
    /**
     * Crée un paramètre
     * @param l Correspond à la valeur du paramètre
     */
    public ClientParam(long l) {
        this.key = getGenerateChain(10);
        this.obj = l;
        
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
    }
    
    /**
     * Crée un paramètre
     * @param f Correspond à la valeur du paramètre
     */
    public ClientParam(float f) {
        this.key = getGenerateChain(10);
        this.obj = f;
        
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
    }
    
    /**
     * Crée un paramètre
     * @param d Correspond à la valeur du paramètre
     */
    public ClientParam(double d) {
        this.key = getGenerateChain(10);
        this.obj = d;
        
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
    }
    
    /**
     * Crée un paramètre
     * @param b Correspond à la valeur du paramètre
     */
    public ClientParam(boolean b) {
        this.key = getGenerateChain(10);
        this.obj = b;
        
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
    }
    
    /**
     * Crée un paramètre
     * @param s Correspond à la valeur du paramètre
     */
    public ClientParam(String s) {
        this.obj = s;
        this.key = getGenerateChain(10);
        
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
    }

    
    
//METHODES PUBLICS
    /**
     * Modifie le nom de la clef. Attention, utiliser cette methode peut être dangereux
     * @param key Correspond à la nouvelle clef
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Renvoie le paramètre sous la forme d'une chaîne de caractère [key_generated]=[value]
     * @return Retourne le paramètre sous la forme d'une chaîne de caractère
     */
    @Override
    public String toString() {
        return key+"="+obj;
    }
    
    /**
     * Renvoie le hashCode du paramètre
     * @return Retourne le hashCode du paramètre
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + java.util.Objects.hashCode(this.key);
        hash = 53 * hash + java.util.Objects.hashCode(this.obj);
        return hash;
    }

    /**
     * Renvoie true si obj est égal à l'objet courant
     * @param obj Correspond à l'objet à comparer au courant
     * @return Retourne true, s'ils sont identiques, sinon false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientParam other = (ClientParam) obj;
        if (!java.util.Objects.equals(this.key, other.key)) {
            return false;
        }
        return java.util.Objects.equals(this.obj, other.obj);
    }

    /**
     * Compare un paramètre avec celui-ci
     * @param o Correspond à l'autre paramètre à comparer avec celui-ci
     * @return Retourne un nombre négatif si l'objet courant précède l'autre paramètre, 0 si l'objet courant est positionner pareillement à l'autre paramètre et un nombre positif si l'objet courant succède l'autre paramètre
     */
    @Override
    public int compareTo(ClientParam o) {
        return toString().compareTo(o.toString());
    }
    
    
    
//METHODE PRIVATE
    /**
     * Génère une chaîne String
     * @param size Correspond à la taille de la chaîne générée
     * @return Retourne une chaîne générée
     */
    private String getGenerateChain(int size){
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String chain = "";
        for(int i=0;i<size;i++){
            chain += base.charAt((int) (Math.random() * base.length()));
        }
        return chain;
    }
    
    
    
//METHODES PRIVATES STATICS
    /**
     * Génère une erreur si la librairie Gson n'est pas installée
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private static void checkGsonInstalled(){
        try {
            Class.forName("com.google.gson.Gson");
        } catch (ClassNotFoundException ex) {
            try {
                throw new java.lang.ClassNotFoundException("No \"gson-2.8.2.jar\" installed. You can download it here: https://jar-download.com/artifacts/com.google.code.gson/gson/2.8.2/source-code");
            } catch (ClassNotFoundException ex1) {
                ex1.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    /**
     * Génère une erreur si la librairie Gson n'est pas installée
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private static void checkJSONInstalled(){
        try {
            Class.forName("com.jasonpercus.json.JSON");
        } catch (ClassNotFoundException ex) {
            try {
                throw new java.lang.ClassNotFoundException("No \"JSON.jar\" installed. You can download it here: https://github.com/josephbriguet01/JSON/blob/19f902243d36b919a223a5c76a4b74d252cc0532/dist/JSON.jar\nIts documentation can be downloaded here: https://github.com/josephbriguet01/JSON/blob/19f902243d36b919a223a5c76a4b74d252cc0532/dist/JSON-javadoc.zip");
            } catch (ClassNotFoundException ex1) {
                ex1.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    /**
     * Génère une erreur si la librairie Gson n'est pas installée
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private static void checkEncryptionInstalled(){
        try {
            Class.forName("com.jasonpercus.encryption.Cipher");
        } catch (ClassNotFoundException ex) {
            try {
                throw new java.lang.ClassNotFoundException("No \"Encryption.jar\" installed. You can download it here: https://github.com/josephbriguet01/Encryption/blob/master/dist/Encryption.jar\nIts documentation can be downloaded here: https://github.com/josephbriguet01/Encryption/blob/master/dist/Encryption-javadoc.zip");
            } catch (ClassNotFoundException ex1) {
                ex1.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    
    
}