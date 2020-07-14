/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package com.jasonpercus.restapijson;



import com.jasonpercus.encryption.Cipher;
import com.jasonpercus.encryption.Key;
import com.jasonpercus.encryption.rsa.KeyPrivateRSA;
import com.jasonpercus.encryption.rsa.RSA;
import com.jasonpercus.restapijson.exception.ErrorConnectionException;
import com.jasonpercus.restapijson.exception.ErrorException;
import com.jasonpercus.json.JSON;



/**
 * Cette classe permet de se connecter à un serveur api rest
 * @author Briguet
 * @version 1.0
 */
public class Client {

    
    
//ATTRIBUT
    /**
     * Correspond à la classe qui va faire le rôle de client.
     * On passe par une sous classe pour permettre au vérificateur de présence de librairie de pouvoir faire son travail.
     * En tant normal, C_Client serait remonté à la place de client.
     * Autrement dit, la classe Client sert de proxy à C_Client
     */
    private C_Client client;
    
    
    
//CONSTRUCTORS
    /**
     * Crée un client à destination d'un serveur
     * @param url Correspond à l'url de connexion au serveur (sans les paramètres: ex ?name=durant&amp;age=52)
     */
    public Client(String url) {
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
        
        this.client = new C_Client(url);
    }

    /**
     * Crée un client à destination d'un serveur
     * @param url Correspond à l'url de connexion au serveur (sans les paramètres: ex ?name=durant&amp;age=52)
     * @param timeoutConnection Correpsond au timeout maximal de connexion
     */
    public Client(String url, int timeoutConnection) {
        //Vérifie que GSON, JSON et Encryption sont installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
        
        this.client = new C_Client(url, timeoutConnection);
    }
    
    
    
//METHODES PUBLICS
    /**
     * Renvoie si la communication cliente/serveur est chiffrée ou non (si la valeur ne peut être encore déterminée, false est renvoyé)
     * @return Retourne true, si la communication se fait de manière chiffrée avec le serveur
     */
    public boolean getEncrypt() {
        return client.getEncrypt();
    }

    /**
     * Permet de modifier l'encodage des requêtes envoyées à destination du serveur
     * @param charset Correspond à l'encodage (ex: ISO-8859-1, UTF-8...)
     */
    public void changeCharset(String charset) {
        client.changeCharset(charset);
    }

    /**
     * Demande au serveur d'exécuter une tâche
     * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
     * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
     * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
     * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
     */
    public void set(String context) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException {
        client.set(context);
    }

    /**
     * Demande au serveur d'exécuter une tâche
     * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
     * @param params Correspond à un tableau de paramètres d'url à envoyer au serveur (ce tableau va générer un fragment de l'url. Ex: ?id=0&amp;firstname=Bertrand)
     * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
     * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
     * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
     */
    public void set(String context, ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException {
        client.set(context, params);
    }

    /**
     * Demande au serveur d'exécuter une tâche
     * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
     * @param objToSend Correspond à l'objet à envoyer au serveur
     * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
     * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
     * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
     */
    public void set(String context, java.io.Serializable objToSend) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException {
        client.set(context, objToSend);
    }

    /**
     * Demande au serveur d'exécuter une tâche
     * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
     * @param objToSend Correspond à l'objet à envoyer au serveur
     * @param params Correspond à un tableau de paramètres d'url à envoyer au serveur (ce tableau va générer un fragment de l'url. Ex: ?id=0&amp;firstname=Bertrand)
     * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
     * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
     * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
     */
    public void set(String context, java.io.Serializable objToSend, ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException {
        client.set(context, objToSend, params);
    }

    /**
     * Récupère un objet renvoyé du serveur
     * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
     * @param classReturnObj Correspond à la classe de l'objet qui sera récupéré (Attention, si l'objet racine est une liste, prendre le type contenu dans la liste. Ex List&lsaquo;MonObjet&rsaquo; -&rsaquo; type = MonObjet). Utiliser Object.class s'il n'y aucun moyen de déterminer le type d'objet retourné
     * @return Retourne l'objet renvoyé du serveur
     * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
     * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
     * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
     */
    public Object get(String context, Class<?> classReturnObj) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException {
        return client.get(context, classReturnObj);
    }

    /**
     * Récupère un objet renvoyé du serveur
     * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
     * @param classReturnObj Correspond à la classe de l'objet qui sera récupéré (Attention, si l'objet racine est une liste, prendre le type contenu dans la liste. Ex List&lsaquo;MonObjet&rsaquo; -&rsaquo; type = MonObjet). Utiliser Object.class s'il n'y aucun moyen de déterminer le type d'objet retourné
     * @param params Correspond à un tableau de paramètres d'url à envoyer au serveur (ce tableau va générer un fragment de l'url. Ex: ?id=0&amp;firstname=Bertrand)
     * @return Retourne l'objet renvoyé du serveur
     * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
     * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
     * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
     */
    public Object get(String context, Class<?> classReturnObj, ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException {
        return client.get(context, classReturnObj, params);
    }

    /**
     * Récupère un objet renvoyé du serveur
     * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
     * @param classReturnObj Correspond à la classe de l'objet qui sera récupéré (Attention, si l'objet racine est une liste, prendre le type contenu dans la liste. Ex List&lsaquo;MonObjet&rsaquo; -&rsaquo; type = MonObjet). Utiliser Object.class s'il n'y aucun moyen de déterminer le type d'objet retourné
     * @param objToSend Correspond à l'objet à envoyer au serveur
     * @return Retourne l'objet renvoyé du serveur
     * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
     * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
     * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
     */
    public Object get(String context, Class<?> classReturnObj, java.io.Serializable objToSend) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException {
        return client.get(context, classReturnObj, objToSend);
    }

    /**
     * Récupère un objet renvoyé du serveur
     * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
     * @param classReturnObj Correspond à la classe de l'objet qui sera récupéré (Attention, si l'objet racine est une liste, prendre le type contenu dans la liste. Ex List&lsaquo;MonObjet&rsaquo; -&rsaquo; type = MonObjet). Utiliser Object.class s'il n'y aucun moyen de déterminer le type d'objet retourné
     * @param objToSend Correspond à l'objet à envoyer au serveur
     * @param params Correspond à un tableau de paramètres d'url à envoyer au serveur (ce tableau va générer un fragment de l'url. Ex: ?id=0&amp;firstname=Bertrand)
     * @return Retourne l'objet renvoyé du serveur
     * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
     * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
     * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
     */
    public Object get(String context, Class<?> classReturnObj, java.io.Serializable objToSend, ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException {
        return client.get(context, classReturnObj, objToSend, params);
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
                throw new java.lang.ClassNotFoundException("No \"JSON.jar\" installed. You can download it here: https://github.com/josephbriguet01/JSON/raw/master/dist/JSON.jar\nIts documentation can be downloaded here: https://github.com/josephbriguet01/JSON/raw/master/dist/JSON-javadoc.zip");
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
                throw new java.lang.ClassNotFoundException("No \"Encryption.jar\" installed. You can download it here: https://github.com/josephbriguet01/Encryption/raw/master/dist/Encryption.jar\nIts documentation can be downloaded here: https://github.com/josephbriguet01/Encryption/raw/master/dist/Encryption-javadoc.zip");
            } catch (ClassNotFoundException ex1) {
                ex1.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    
    
//CLASS
    /**
     * Cette classe permet de se connecter à un serveur api rest
     * @author Briguet
     * @version 1.0
     */
    private class C_Client {



    //ATTRIBUTS
        /**
         * Correspond à l'encodage des requêtes envoyées à destination du serveur
         */
        private String theCharset = "ISO-8859-1";

        /**
         * Correspond à l'url de base (http://192.168.1.1:8080/), sans les paramètres
         */
        private final String url;

        /**
         * Correpsond au timeout maximal de connexion
         */
        private final int timeoutConnection;

        /**
         * Détermine si la communication au serveur est chiffrée ou non. De base cette variable = null
         */
        private Boolean encrypt;

        /**
         * Correspond au certificat de communication chiffré entre client et serveur
         */
        private Certificat certificat;



    //CONSTRUCTORS
        /**
         * Crée un client à destination d'un serveur
         * @param url Correspond à l'url de connexion au serveur (sans les paramètres: ex ?name=durant&amp;age=52)
         */
        public C_Client(String url){
            this.encrypt = null;
            this.url = (url.lastIndexOf("/") == url.length()-1) ? url : url + "/";
            this.timeoutConnection = 10000;
        }

        /**
         * Crée un client à destination d'un serveur
         * @param url Correspond à l'url de connexion au serveur (sans les paramètres: ex ?name=durant&amp;age=52)
         * @param timeoutConnection Correpsond au timeout maximal de connexion
         */
        public C_Client(String url, int timeoutConnection) {
            this.url = url;
            this.timeoutConnection = timeoutConnection;
        }



    //METHODES PUBLICS
        /**
         * Renvoie si la communication cliente/serveur est chiffrée ou non (si la valeur ne peut être encore déterminée, false est renvoyé)
         * @return Retourne true, si la communication se fait de manière chiffrée avec le serveur
         */
        public boolean getEncrypt() {
            if(encrypt == null) return false;
            return encrypt;
        }

        /**
         * Permet de modifier l'encodage des requêtes envoyées à destination du serveur
         * @param charset Correspond à l'encodage (ex: ISO-8859-1, UTF-8...)
         */
        public void changeCharset(String charset){
            theCharset = charset;
        }

        /**
         * Demande au serveur d'exécuter une tâche
         * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
         * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
         * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
         * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
         */
        public void set(String context) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
            get(context, null, null, (ClientParam[]) null);
        }

        /**
         * Demande au serveur d'exécuter une tâche
         * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
         * @param params Correspond à un tableau de paramètres d'url à envoyer au serveur (ce tableau va générer un fragment de l'url. Ex: ?id=0&amp;firstname=Bertrand)
         * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
         * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
         * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
         */
        public void set(String context, ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
            get(context, null, null, params);
        }

        /**
         * Demande au serveur d'exécuter une tâche
         * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
         * @param objToSend Correspond à l'objet à envoyer au serveur
         * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
         * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
         * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
         */
        public void set(String context, java.io.Serializable objToSend) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
            get(context, null, objToSend, (ClientParam[]) null);
        }

        /**
         * Demande au serveur d'exécuter une tâche
         * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
         * @param objToSend Correspond à l'objet à envoyer au serveur
         * @param params Correspond à un tableau de paramètres d'url à envoyer au serveur (ce tableau va générer un fragment de l'url. Ex: ?id=0&amp;firstname=Bertrand)
         * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
         * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
         * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
         */
        public void set(String context, java.io.Serializable objToSend, ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
            get(context, null, objToSend, params);
        }

        /**
         * Récupère un objet renvoyé du serveur
         * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
         * @param classReturnObj Correspond à la classe de l'objet qui sera récupéré (Attention, si l'objet racine est une liste, prendre le type contenu dans la liste. Ex List&lsaquo;MonObjet&rsaquo; -&rsaquo; type = MonObjet). Utiliser Object.class s'il n'y aucun moyen de déterminer le type d'objet retourné
         * @return Retourne l'objet renvoyé du serveur
         * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
         * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
         * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
         */
        public Object get(String context, Class<?> classReturnObj) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
            return get(context, classReturnObj, null, (ClientParam[])null);
        }

        /**
         * Récupère un objet renvoyé du serveur
         * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
         * @param classReturnObj Correspond à la classe de l'objet qui sera récupéré (Attention, si l'objet racine est une liste, prendre le type contenu dans la liste. Ex List&lsaquo;MonObjet&rsaquo; -&rsaquo; type = MonObjet). Utiliser Object.class s'il n'y aucun moyen de déterminer le type d'objet retourné
         * @param params Correspond à un tableau de paramètres d'url à envoyer au serveur (ce tableau va générer un fragment de l'url. Ex: ?id=0&amp;firstname=Bertrand)
         * @return Retourne l'objet renvoyé du serveur
         * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
         * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
         * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
         */
        public Object get(String context, Class<?> classReturnObj, ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
            return get(context, classReturnObj, null, params);
        }

        /**
         * Récupère un objet renvoyé du serveur
         * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
         * @param classReturnObj Correspond à la classe de l'objet qui sera récupéré (Attention, si l'objet racine est une liste, prendre le type contenu dans la liste. Ex List&lsaquo;MonObjet&rsaquo; -&rsaquo; type = MonObjet). Utiliser Object.class s'il n'y aucun moyen de déterminer le type d'objet retourné
         * @param objToSend Correspond à l'objet à envoyer au serveur
         * @return Retourne l'objet renvoyé du serveur
         * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
         * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
         * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
         */
        public Object get(String context, Class<?> classReturnObj, java.io.Serializable objToSend) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
            return get(context, classReturnObj, objToSend, (ClientParam[])null);
        }

        /**
         * Récupère un objet renvoyé du serveur
         * @param context Correspond au chemin dans le serveur (http://192.168.1.1:8080/[context]?id=0&amp;firstname=Bertrand). Le context correspond au nom de la classe API (ou au nom définit par _getControllerApiContext()) + le nom de la méthode à interroger
         * @param classReturnObj Correspond à la classe de l'objet qui sera récupéré (Attention, si l'objet racine est une liste, prendre le type contenu dans la liste. Ex List&lsaquo;MonObjet&rsaquo; -&rsaquo; type = MonObjet). Utiliser Object.class s'il n'y aucun moyen de déterminer le type d'objet retourné
         * @param objToSend Correspond à l'objet à envoyer au serveur
         * @param params Correspond à un tableau de paramètres d'url à envoyer au serveur (ce tableau va générer un fragment de l'url. Ex: ?id=0&amp;firstname=Bertrand)
         * @return Retourne l'objet renvoyé du serveur
         * @throws java.net.MalformedURLException Si l'url de la demande cliente a mal été formée
         * @throws ErrorException S'il y a un message d'erreur renvoyé par le serveur (ex: 400, 404...)
         * @throws ErrorConnectionException Si le client ne peut se connecter au serveur
         */
        @SuppressWarnings({"UseSpecificCatch", "CallToPrintStackTrace"})
        public Object get(String context, Class<?> classReturnObj, java.io.Serializable objToSend, ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
            if(context == null) throw new java.lang.NullPointerException("Context is null");
            else{
                if(context.length() == 1 && context.indexOf("/") == 0) context = "";
                if(context.length() > 1 && context.indexOf("/") == 0) context = context.substring(1);
                if(context.contains("?")) throw new java.lang.IllegalArgumentException("context is malformed");
                Boolean isEncrypt = setEncrypt();
                if(isEncrypt != null){
                    boolean enc = isEncrypt;
                    String paramsSOld;
                    String paramsS = "";
                    if(params != null && params.length>0){
                        for(ClientParam p : params){
                            if(p != null){
                                if(!paramsS.isEmpty()) paramsS += "&";
                                    paramsS += p.toString();
                            }
                        }
                    }
                    paramsSOld = paramsS;
                    paramsS = encrypterParams(paramsS);
                    if(!paramsS.isEmpty()) paramsS = "?"+paramsS;
                    String paramsSBase = paramsS;
                    try {
                        java.net.URLConnection connection;
                        if(enc){
                            if(certificat == null){
                                getCertificat();
                                paramsS = encrypterParams(paramsSOld);
                                if(!paramsS.isEmpty()) paramsS = "?"+paramsS;
                            }
                            if(paramsS.isEmpty()) paramsS = "?"+Server.BASE_PARAM_HASH+"="+certificat.getHash();
                            else paramsS += "&"+Server.BASE_PARAM_HASH+"="+certificat.getHash();
                            connection = new java.net.URL(this.url+context+paramsS).openConnection();
                            connection.setConnectTimeout(timeoutConnection);
                            if(objToSend!=null){
                                String json = JSON.serialize(objToSend);
                                Cipher cph = (Cipher) Class.forName(certificat.getNameCipher()).getConstructor().newInstance();
                                json = cph.encrypt(this.certificat.getKey(), json);
                                connection.setDoOutput(true);
                                connection.getOutputStream().write(json.getBytes(getCharsetEncoderByte()));
                            }
                        }else{
                            connection = new java.net.URL(this.url+context+paramsS).openConnection();
                            connection.setConnectTimeout(timeoutConnection);
                            if(objToSend!=null){
                                String json = JSON.serialize(objToSend);
                                connection.setDoOutput(true);
                                connection.getOutputStream().write(json.getBytes(getCharsetEncoderByte()));
                            }
                        }

                        try {
                            java.io.InputStreamReader response = new java.io.InputStreamReader(connection.getInputStream(), theCharset);
                            String responseBody;
                            try (java.util.Scanner scanner = new java.util.Scanner(response)) {
                                responseBody = scanner.useDelimiter("\\A").next();
                            }
                            if (responseBody != null && classReturnObj != null) {
                                if(enc){
                                    Cipher cph = (Cipher) Class.forName(certificat.getNameCipher()).getConstructor().newInstance();
                                    responseBody = cph.decrypt(this.certificat.getKey(), responseBody);
                                }
                                if(responseBody.contains(Server.MESSAGE_CERTIFICAT_EXPIRED)){
                                    getCertificat();
                                    return get(context, classReturnObj, objToSend, params);
                                }else{
                                    JSON json = JSON.deserialize(classReturnObj, responseBody);
                                    if (json.isArray()) {
                                        return json.getList();
                                    } else {
                                        return json.getObj();
                                    }
                                }
                            } else {
                                return null;
                            }
                        } catch (java.io.FileNotFoundException e) {
                            throw new ErrorException(404, e.getMessage());
                        } catch (java.util.NoSuchElementException e) {
                            return null;
                        } catch (java.net.ConnectException e) {
                            throw new ErrorConnectionException(paramsS + " cannot be reached");
                        } catch (java.io.IOException e) {
                            throw new ErrorException(400, e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (java.net.ConnectException e) {
                        throw new ErrorConnectionException(paramsSBase + " cannot be reached");
                    } catch (java.io.IOException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException ex) {
                        java.util.logging.Logger.getLogger(C_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }



    //METHODE PRIVATE
        /**
         * Renvoie le StandardCharsets correspondant à l'encodage des requêtes
         * @return Retourne le StandardCharsets correspondant à l'encodage des requêtes
         */
        private java.nio.charset.Charset getCharsetEncoderByte() {
            if (theCharset.equals("ISO-8859-1")) {
                return java.nio.charset.StandardCharsets.ISO_8859_1;
            }
            if (theCharset.equals("UTF-8")) {
                return java.nio.charset.StandardCharsets.UTF_8;
            }
            return java.nio.charset.StandardCharsets.ISO_8859_1;
        }

        /**
         * Demande au serveur si la communication va être chiffrée
         * @return Retourne null si le serveur n'a pas pû renvoyer une réponse valable, true s'il y a chiffrement et false, s'il n'y en a pas
         */
        @SuppressWarnings({"UseSpecificCatch", "CallToPrintStackTrace"})
        private Boolean setEncrypt() {
            if(this.encrypt == null){
                try {
                    java.net.URLConnection connection = new java.net.URL(this.url+Server.CONTEXT_ENCRYPT).openConnection();
                    connection.setConnectTimeout(timeoutConnection);
                    java.io.InputStream response = connection.getInputStream();
                    String responseBody;
                    try (java.util.Scanner scanner = new java.util.Scanner(response)) {
                        responseBody = scanner.useDelimiter("\\A").next();
                    }
                    if(responseBody != null){
                        this.encrypt = (boolean) JSON.deserialize(Boolean.class, responseBody).getObj();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return this.encrypt;
        }

        /**
         * Récupère du serveur et change le certificat du client
         */
        @SuppressWarnings({"UseSpecificCatch", "CallToPrintStackTrace"})
        private void getCertificat(){
            if(this.encrypt){
                try {
                    RSA rsa = new RSA();
                    java.net.URLConnection connection = new java.net.URL(this.url+Server.CONTEXT_PUBLIC_CIPHER).openConnection();
                    connection.setConnectTimeout(timeoutConnection);
                    String json = JSON.serialize(rsa.generatePublicKey());
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(json.getBytes(getCharsetEncoderByte()));
                    java.io.InputStream response = connection.getInputStream();
                    String responseBody;
                    try (java.util.Scanner scanner = new java.util.Scanner(response)) {
                        responseBody = scanner.useDelimiter("\\A").next();
                    }
                    if(responseBody != null){
                        this.certificat = (Certificat) JSON.deserialize(Certificat.class, responseBody).getObj();
                        KeyPrivateRSA kpr = (KeyPrivateRSA) rsa.generatePrivateKey();
                        this.certificat.setKey((Key) Class.forName(this.certificat.getNameKeyCipher()).getConstructor(String.class).newInstance(rsa.decrypt(kpr, this.certificat.getEncryptedKey())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Renvoie les paramètres donnés par l'utilisateur, chiffrés (s'il y a un chiffrement entre le client et le serveur, sinon paramsBase est renvoyé tel quel)
         * @param paramsBase Correspond au paramètres donnés par l'utilisateur et qui doivent être chiffrés
         * @return Retourne les paramètres
         */
        private String encrypterParams(String paramsBase){
            if(paramsBase != null && paramsBase.length()>0){
                if(encrypt != null && encrypt && certificat != null){
                    try {
                        Cipher cph = (Cipher) Class.forName(certificat.getNameCipher()).getConstructor().newInstance();
                        String encryptedParams = cph.encrypt(this.certificat.getKey(), paramsBase).replace("=", Server.CHAR_EQUAL).replace("/", Server.CHAR_SLASH).replace("\\", Server.CHAR_ASLAS).replace("+", Server.CHAR_PLUS).replace("-", Server.CHAR_MINUS).replace("\n", Server.CHAR_SLASHN).replace("\r", Server.CHAR_SLASHR).replace("\t", Server.CHAR_SLASHT);
                        return Server.BASE_PARAMS_ENCRYPTED+"="+encryptedParams;
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException | ClassNotFoundException | NoSuchMethodException ex) {
                        java.util.logging.Logger.getLogger(C_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }else{
                    return paramsBase;
                }
            }else if(paramsBase != null && paramsBase.length() == 0){
                return paramsBase;
            }
            return null;
        }



    }
    

    
}