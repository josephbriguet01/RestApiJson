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
    
    
    
//ATTRIBUT
    /**
     * Correspond à l'encodage des requêtes envoyées à destination du serveur
     */
    private static String theCharset = "ISO-8859-1";
    
    
    
//METHODES PUBLICS
    /**
     * Permet de modifier l'encodage des requêtes envoyées à destination du serveur
     * @param charset Correspond à l'encodage (ex: ISO-8859-1, UTF-8...)
     */
    public static void changeCharset(String charset){
        theCharset = charset;
    }
    
    /**
     * Envoie une requête en direction du serveur
     * @param url Correspond à l'url de connexion au serveur
     * @return Retourne un objet si le serveur renvoie un objet, sinon la méthode renvoie null
     * @throws java.net.MalformedURLException Si l'url n'est pas bien écrite
     * @throws ErrorException Si la requête génère une erreur auprès du serveur (ex: si la page demandée n'existe pas...)
     * @throws ErrorConnectionException Si le client n'arrive pas à se connecter au serveur
     */
    public static Object sendRequest(String url) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
        return sendRequest(url, null);
    }
    
    /**
     * Envoie une requête en direction du serveur
     * @param url Correspond à l'url de connexion au serveur
     * @param objectToPost Correspond à l'objet à envoyer au serveur
     * @return Retourne un objet si le serveur renvoie un objet, sinon la méthode renvoie null
     * @throws java.net.MalformedURLException Si l'url n'est pas bien écrite
     * @throws ErrorException Si la requête génère une erreur auprès du serveur (ex: si la page demandée n'existe pas...)
     * @throws ErrorConnectionException Si le client n'arrive pas à se connecter au serveur
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static Object sendRequest(String url, Object objectToPost) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException {
        try {
            java.net.URLConnection connection = new java.net.URL(url).openConnection();
            if(objectToPost!=null){
                String json = JSON.serialize(objectToPost);
                connection.setDoOutput(true);
                connection.getOutputStream().write(json.getBytes(getCharsetEncoderByte()));
            }
            try {
                java.io.InputStream response = connection.getInputStream();
                String responseBody;
                try (java.util.Scanner scanner = new java.util.Scanner(response)) {
                    responseBody = scanner.useDelimiter("\\A").next();
                }
                return responseBody;
            } catch (java.io.FileNotFoundException e) {
                throw new ErrorException(404, e.getMessage());
            } catch (java.util.NoSuchElementException e) {
                return null;
            } catch (java.net.ConnectException e) {
                throw new ErrorConnectionException(url + " cannot be reached");
            } catch (java.io.IOException e) {
                throw new ErrorException(400, e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (java.net.ConnectException e) {
            throw new ErrorConnectionException(url + " cannot be reached");
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
//METHODE PRIVATE
    /**
     * Renvoie le StandardCharsets correspondant à l'encodage des requêtes
     * @return Retourne le StandardCharsets correspondant à l'encodage des requêtes
     */
    private static java.nio.charset.Charset getCharsetEncoderByte() {
        if (theCharset.equals("ISO-8859-1")) {
            return java.nio.charset.StandardCharsets.ISO_8859_1;
        }
        if (theCharset.equals("UTF-8")) {
            return java.nio.charset.StandardCharsets.UTF_8;
        }
        return java.nio.charset.StandardCharsets.ISO_8859_1;
    }
    
    
    
}