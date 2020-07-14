/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package com.jasonpercus.restapijson;



import com.jasonpercus.restapijson.exception.ErrorConnectionException;
import com.jasonpercus.restapijson.exception.ErrorException;
import com.jasonpercus.json.JSON;



/**
 * Cette classe permet de se connecter à un serveur api rest
 * @author Briguet
 * @version 1.0
 */
public class Client {



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
     * Correspond au timeout maximal de connexion
     */
    private final int timeoutConnection;



//CONSTRUCTORS
    /**
     * Crée un client à destination d'un serveur
     * @param url Correspond à l'url de connexion au serveur (sans les paramètres: ex ?name=durant&amp;age=52)
     */
    public Client(String url){
        this.url = (url.lastIndexOf("/") == url.length()-1) ? url : url + "/";
        this.timeoutConnection = 10000;
    }

    /**
     * Crée un client à destination d'un serveur
     * @param url Correspond à l'url de connexion au serveur (sans les paramètres: ex ?name=durant&amp;age=52)
     * @param timeoutConnection Correpsond au timeout maximal de connexion
     */
    public Client(String url, int timeoutConnection) {
        this.url = url;
        this.timeoutConnection = timeoutConnection;
    }



//METHODES PUBLICS
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
                
            String paramsS = "";
            if(params != null && params.length>0){
                for(ClientParam p : params){
                    if(p != null){
                        if(!paramsS.isEmpty()) paramsS += "&";
                            paramsS += p.toString();
                    }
                }
            }
            if(!paramsS.isEmpty()) paramsS = "?"+paramsS;
            String paramsSBase = paramsS;
            try {
                java.net.URLConnection connection;
                    
                connection = new java.net.URL(this.url + context + paramsS).openConnection();
                connection.setConnectTimeout(timeoutConnection);
                if(objToSend!=null){
                    String json = JSON.serialize(objToSend);
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(json.getBytes(getCharsetEncoderByte()));
                }
                    
                try {
                    java.io.InputStreamReader response = new java.io.InputStreamReader(connection.getInputStream(), theCharset);
                    String responseBody;
                    try (java.util.Scanner scanner = new java.util.Scanner(response)) {
                        responseBody = scanner.useDelimiter("\\A").next();
                    }
                    if (responseBody != null && classReturnObj != null) {
                        JSON json = JSON.deserialize(classReturnObj, responseBody);
                        return (json.isArray()) ? json.getList() : json.getObj();
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
            } catch (java.io.IOException | SecurityException | IllegalArgumentException ex) {
                java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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



}