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



//CONSTANTS
    /**
     * Correspond au context de demande d'une clef publique
     */
    protected static final String CONTEXT_PUBLIC_CIPHER = "oIhBN4785Uvbr";

    /**
     * Correspond au context de demande de chiffrement
     */
    protected static final String CONTEXT_ENCRYPT = "pmYhtu236BfhF";

    /**
     * Correspond au message du serveur vers le client disant que le certificat a expiré et qu'il faut en générer un nouveau
     */
    protected static final String MESSAGE_CERTIFICAT_EXPIRED = "Ikhl823Gvhent";

    /**
     * Correspond au nom de la clef utilisée qui contiend pour valeur les paramètres chiffrés de l'utilisateur
     */
    protected static final String BASE_PARAMS_ENCRYPTED="inbev4e5gnexF";

    /**
     * Correspond au nom de la clef utilisée qui contiend pour valeur le hash du certificat utilisateur
     */
    protected static final String BASE_PARAM_HASH = "OhgjH853aHyfr";

    /**
     * Correspond au caractère =
     */
    protected static final String CHAR_EQUAL="uhJFnKLP487zv";

    /**
     * Correspond au caractère /
     */
    protected static final String CHAR_SLASH="mpYCneHEc789A";

    /**
     * Correspond au caractère \n
     */
    protected static final String CHAR_SLASHN="ikfnryd523r6d";

    /**
     * Correspond au caractère \r
     */
    protected static final String CHAR_SLASHR="apvnr7d5v2d3r";

    /**
     * Correspond au caractère \t
     */
    protected static final String CHAR_SLASHT="itlsnbpyFG523";

    /**
     * Correspond au caractère \
     */
    protected static final String CHAR_ASLAS="pvj58NIOpj32D";

    /**
     * Correspond au caractère +
     */
    protected static final String CHAR_PLUS= "igjf75cve32ed";

    /**
     * Correspond au caractère -
     */
    protected static final String CHAR_MINUS="gbornv6OHef2s";



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
    public Client(String url){
        this.encrypt = null;
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
    public void set(final String context) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
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
    public void set(final String context, final ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
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
    public void set(final String context, final java.io.Serializable objToSend) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
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
    public void set(final String context, final java.io.Serializable objToSend, final ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
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
    public Object get(final String context, final Class<?> classReturnObj) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
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
    public Object get(final String context, final Class<?> classReturnObj, final ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
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
    public Object get(final String context, final Class<?> classReturnObj, final java.io.Serializable objToSend) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{
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
    public Object get(final String context, final Class<?> classReturnObj,  final java.io.Serializable objToSend,  final ClientParam... params) throws java.net.MalformedURLException, ErrorException, ErrorConnectionException{

        System.out.println("context: "+context+" - classReturnObj: "+classReturnObj+" - objToSend: "+objToSend+" - params: "+java.util.Arrays.toString(params));
        final Object[] objs = new Object[1];
        final Exception[] exceptions = new Exception[2];

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    String contextNFinal = context;
                    Class<?> classReturnObjNFinal = classReturnObj;
                    java.io.Serializable objToSendNFinal = objToSend;
                    ClientParam[] paramsNFinal = params;


                    if(contextNFinal == null) throw new NullPointerException("Context is null");
                    else{
                        if(contextNFinal.length() == 1 && contextNFinal.indexOf("/") == 0) contextNFinal = "";
                        if(contextNFinal.length() > 1 && contextNFinal.indexOf("/") == 0) contextNFinal = contextNFinal.substring(1);
                        if(contextNFinal.contains("?")) throw new IllegalArgumentException("context is malformed");
                        Boolean isEncrypt = setEncrypt();
                        if(isEncrypt != null){
                            boolean enc = isEncrypt;
                            String paramsSOld;
                            String paramsS = "";
                            if(paramsNFinal != null && paramsNFinal.length>0){
                                for(ClientParam p : paramsNFinal){
                                    if(p != null) {
                                        if (!paramsS.isEmpty()) paramsS += "&";
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
                                    if(paramsS.isEmpty()) paramsS = "?"+Client.BASE_PARAM_HASH+"="+certificat.getHash();
                                    else paramsS += "&"+Client.BASE_PARAM_HASH+"="+certificat.getHash();
                                    connection = new java.net.URL(Client.this.url+contextNFinal+paramsS).openConnection();
                                    connection.setConnectTimeout(timeoutConnection);
                                    if(objToSendNFinal!=null){
                                        String json = JSON.serialize(objToSendNFinal);
                                        Cipher cph = (Cipher) Class.forName(certificat.getNameCipher()).getConstructor().newInstance();
                                        json = cph.encrypt(Client.this.certificat.getKey(), json);
                                        connection.setDoOutput(true);
                                        connection.getOutputStream().write(json.getBytes(getCharsetEncoderByte()));
                                    }
                                }else{
                                    connection = new java.net.URL(Client.this.url+contextNFinal+paramsS).openConnection();
                                    connection.setConnectTimeout(timeoutConnection);
                                    if(objToSendNFinal!=null){
                                        String json = JSON.serialize(objToSendNFinal);
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
                                    if (responseBody != null && classReturnObjNFinal != null) {
                                        if(enc){
                                            Cipher cph = (Cipher) Class.forName(certificat.getNameCipher()).getConstructor().newInstance();
                                            responseBody = cph.decrypt(Client.this.certificat.getKey(), responseBody);
                                        }
                                        if(responseBody.contains(Client.MESSAGE_CERTIFICAT_EXPIRED)){
                                            getCertificat();
                                            objs[0] = get(contextNFinal, classReturnObjNFinal, objToSendNFinal, paramsNFinal);
                                            return;
                                        }else{
                                            JSON json = JSON.deserialize(classReturnObjNFinal, responseBody);
                                            if (json.isArray()) {
                                                objs[0] = json.getList();
                                            } else {
                                                objs[0] = json.getObj();
                                            }
                                            return;
                                        }
                                    } else {
                                        objs[0] = null;
                                        return;
                                    }
                                } catch (java.io.FileNotFoundException e) {
                                    exceptions[0] = new ErrorException(404, e.getMessage());
                                } catch (java.util.NoSuchElementException e) {
                                    objs[0] = null;
                                    return;
                                } catch (java.net.ConnectException e) {
                                    exceptions[0] = new ErrorConnectionException(paramsS + " cannot be reached");
                                } catch (java.io.IOException e) {
                                    exceptions[0] = new ErrorException(404, e.getMessage());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } catch (java.net.ConnectException e) {
                                exceptions[1] = new ErrorConnectionException(paramsSBase + " cannot be reached");
                            } catch (java.io.IOException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException ex) {
                                java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            }
                        }
                    }
                    objs[0] = null;
                    return;
                }
            });
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Exception ex : exceptions){
            if(ex != null){
                if(ex instanceof ErrorException){
                    throw (ErrorException)ex;
                }
                else if(ex instanceof ErrorConnectionException){
                    throw (ErrorConnectionException)ex;
                }
            }
        }
        return objs[0];
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
                java.net.URLConnection connection = new java.net.URL(this.url+Client.CONTEXT_ENCRYPT).openConnection();
                connection.setConnectTimeout(timeoutConnection);
                java.io.InputStreamReader response = new java.io.InputStreamReader(connection.getInputStream(), theCharset);
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
                java.net.URLConnection connection = new java.net.URL(this.url+Client.CONTEXT_PUBLIC_CIPHER).openConnection();
                connection.setConnectTimeout(timeoutConnection);
                String json = JSON.serialize(rsa.generatePublicKey());
                connection.setDoOutput(true);
                connection.getOutputStream().write(json.getBytes(getCharsetEncoderByte()));
                java.io.InputStreamReader response = new java.io.InputStreamReader(connection.getInputStream(), theCharset);
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
                    String encryptedParams = cph.encrypt(this.certificat.getKey(), paramsBase);
                    encryptedParams = encryptedParams.replace("=", Client.CHAR_EQUAL);
                    encryptedParams = encryptedParams.replace("/", Client.CHAR_SLASH);
                    encryptedParams = encryptedParams.replace("\\", Client.CHAR_ASLAS);
                    encryptedParams = encryptedParams.replace("+", Client.CHAR_PLUS);
                    encryptedParams = encryptedParams.replace("-", Client.CHAR_MINUS);
                    encryptedParams = encryptedParams.replace("\n", "");
                    encryptedParams = encryptedParams.replace("\r", "");
                    encryptedParams = encryptedParams.replace("\t", "");
                    return Client.BASE_PARAMS_ENCRYPTED+"="+encryptedParams;
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException | ClassNotFoundException | NoSuchMethodException ex) {
                    java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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