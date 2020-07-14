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
import com.jasonpercus.encryption.rsa.KeyPublicRSA;
import com.jasonpercus.json.JSON;
import com.jasonpercus.restapijson.exception.ApiNotDefinedException;
import com.jasonpercus.restapijson.exception.ErrorCertificatException;
import com.jasonpercus.restapijson.exception.ErrorStartException;
import com.jasonpercus.restapijson.exception.PortAlreadyUsedException;
import com.jasonpercus.restapijson.exception.PortTooLargeException;
import com.jasonpercus.restapijson.exception.PortTooSmallException;
import com.jasonpercus.restapijson.exception.ServerAlreadyStartedException;



/**
 * Cette classe permet de créer, lancer un serveur API et de rediriger les requêtes d'un client vers la classe API
 * @author Briguet
 * @version 1.1
 */
public class Server {
    
    
    
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
    
    
    
//ATTRIBUTS
    /**
     * Correspond à l'id unique donné pour ce serveur
     */
    private String id;
    
    /**
     * Correspond au port d'écoute des requêtes
     */
    private int port;
    
    /**
     * Correspond à l'objet listener de ce serveur
     */
    private IServer listener;
    
    /**
     * Correspond à la classe permettant de (dé)chiffrer les communications entre client/serveur
     */
    private Class cipher;
    
    /**
     * Correspond à la classe de la clef de (dé)chiffrage
     */
    private Class keyCipher;
    
    /**
     * Correspond à la liste des controleurs API qu'aura en charge le serveur
     */
    private API[] apis;
    
    /**
     * Correspond au nombre de seconde pour qu'un certificat expire
     */
    private int timeoutGenerateCertificat = 1200;
    
    /**
     * Correspond à l'encodage des requêtes envoyées à destination du client
     */
    private String charset = "ISO-8859-1";
    
    /**
     * Détermine si le serveur est lancé
     */
    private boolean started;
    
    /**
     * Détermine si oui ou non la communication cliente/serveur doit se faire de manière chiffrée
     */
    private boolean encrypt;
    
    /**
     * Correspond au serveur
     */
    public com.sun.net.httpserver.HttpServer server;
    
    /**
     * Correspond au certificat de chiffrement client/serveur
     */
    private volatile Certificat certificat = null;

    
    
//CONSTRUCTORS
    /**
     * Crée un serveur API
     * @param apis Correspond à la liste des controleurs API qu'aura en charge le serveur
     */
    public Server(API... apis) {
        init(null, 8080, apis);
    }

    /**
     * Crée un serveur API
     * @param port Correspond au port d'écoute des requêtes
     * @param apis Correspond à la liste des controleurs API qu'aura en charge le serveur
     */
    public Server(int port, API... apis) {
        init(null, port, apis);
    }

    /**
     * Crée un serveur API
     * @param id Correspond à l'id unique donné pour ce serveur
     * @param apis Correspond à la liste des controleurs API qu'aura en charge le serveur
     */
    public Server(String id, API... apis) {
        init(id, 8080, apis);
    }

    /**
     * Crée un serveur API
     * @param id Correspond à l'id unique donné pour ce serveur
     * @param port Correspond au port d'écoute des requêtes
     * @param apis Correspond à la liste des controleurs API qu'aura en charge le serveur
     */
    public Server(String id, int port, API... apis) {
        init(id, port, apis);
    }
    
    
    
//GETTERS
    /**
     * Renvoie l'id unique donné pour ce serveur
     * @return Retourne l'id unique donné pour ce serveur
     */
    public String getId() {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        return id;
        
    }

    /**
     * Renvoie le port d'écoute des requêtes
     * @return Retourne le port d'écoute des requêtes
     */
    public int getPort() {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        return port;
        
    }

    /**
     * Renvoie l'objet listener de ce serveur
     * @return Retourne l'objet listener de ce serveur
     */
    public IServer getListener() {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        return listener;
        
    }

    /**
     * Renvoie la classe permettant de (dé)chiffrer les communications entre client/serveur
     * @return Retourne la classe permettant de (dé)chiffrer les communications entre client/serveur
     */
    public Class getCipher() {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        return cipher;
        
    }

    /**
     * Renvoie la classe de la clef de (dé)chiffrage
     * @return Retourne la classe de la clef de (dé)chiffrage
     */
    public Class getKeyCipher() {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        return keyCipher;
        
    }

    /**
     * Renvoie la liste des controleurs API qu'aura en charge le serveur
     * @return Retourne la liste des controleurs API qu'aura en charge le serveur
     */
    public API[] getApis() {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        return apis;
        
    }

    /**
     * Renvoie le nombre de seconde pour qu'un certificat expire
     * @return Retourne le nombre de seconde pour qu'un certificat expire
     */
    public int getTimeoutGenerateCertificat() {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        return timeoutGenerateCertificat;
        
    }

    /**
     * Renvoie l'encodage des requêtes envoyées à destination du client
     * @return Retourne l'encodage des requêtes envoyées à destination du client
     */
    public String getCharset() {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        return charset;
        
    }

    /**
     * Renvoie si oui ou non le serveur est lancé
     * @return Retourne true s'il l'est, sinon false
     */
    public boolean isStarted() {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        return started;
        
    }

    
    
//SETTERS
    /**
     * Modifie l'id unique donné pour ce serveur (ne peut être changé lorsque le serveur est lancé)
     * @param id Correspond au nouvel id
     */
    public void setId(String id) {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        if(started) throw new ServerAlreadyStartedException("Cannot change this variable when the server is started");
        else this.id = (id == null || id.isEmpty()) ? this.id = "http://127.0.0.1:"+port+"/" : id;
        
    }

    /**
     * Modifie le port d'écoute des requêtes (ne peut être changé lorsque le serveur est lancé)
     * @param port Correspond au nouveau port
     */
    public void setPort(int port) {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        if(started) throw new ServerAlreadyStartedException("Cannot change this variable when the server is started");
        else{
            if(port < 0) throw new PortTooSmallException("Port (" + port + ") is too small. The port must be greater than 0");
            else if(port > 65535) throw new PortTooLargeException("Port (" + port + ") is too large. The port must be less than 65535");
            else{
                if(portIsUsed(port)) throw new PortAlreadyUsedException("Port (" + port + ") is already used");
                else this.port = port;
            }
        }
        
    }

    /**
     * Modifie l'objet listener de ce serveur
     * @param listener Correspond au nouvel objet listener
     */
    public void setListener(IServer listener) {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        this.listener = listener;
        
    }

    /**
     * Modifie la classe permettant de (dé)chiffrer les communications entre client/serveur et la classe de la clef de (dé)chiffrage (ne peut être changé lorsque le serveur est lancé) et 
     * @param cipher Correspond à la nouvelle classe permettant de (dé)chiffrer les communications entre client/serveur
     * @param keyCipher Correspond à la nouvelle classe de la clef de (dé)chiffrage
     */
    public void setEncrypt(Class cipher, Class keyCipher){
        setCipher(cipher);
        setKeyCipher(keyCipher);
    }

    /**
     * Modifie la classe permettant de (dé)chiffrer les communications entre client/serveur, la classe de la clef de (dé)chiffrage et le nombre de seconde pour qu'un certificat expire (ne peut être changé lorsque le serveur est lancé) et 
     * @param cipher Correspond à la nouvelle classe permettant de (dé)chiffrer les communications entre client/serveur
     * @param keyCipher Correspond à la nouvelle classe de la clef de (dé)chiffrage
     * @param timeoutGenerateCertificat Correspond au nouveau nombre de seconde pour qu'un certificat expire
     */
    public void setEncrypt(Class cipher, Class keyCipher, int timeoutGenerateCertificat){
        setCipher(cipher);
        setKeyCipher(keyCipher);
        setTimeoutGenerateCertificat(timeoutGenerateCertificat);
    }
    
    /**
     * Modifie la classe permettant de (dé)chiffrer les communications entre client/serveur (ne peut être changé lorsque le serveur est lancé)
     * @param cipher Correspond à la nouvelle classe permettant de (dé)chiffrer les communications entre client/serveur
     */
    public void setCipher(Class cipher) {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        if(started) throw new ServerAlreadyStartedException("Cannot change this variable when the server is started");
        else this.cipher = cipher;
        
    }

    /**
     * Modifie la classe de la clef de (dé)chiffrage (ne peut être changé lorsque le serveur est lancé)
     * @param keyCipher Correspond à la nouvelle classe de la clef de (dé)chiffrage
     */
    public void setKeyCipher(Class keyCipher) {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        if(started) throw new ServerAlreadyStartedException("Cannot change this variable when the server is started");
        else this.keyCipher = keyCipher;
        
    }

    /**
     * Modifie la liste des controleurs API qu'aura en charge le serveur (ne peut être changé lorsque le serveur est lancé)
     * @param apis Correspond à la nouvelle liste des controleurs API qu'aura en charge le serveur
     */
    public void setApis(API[] apis) {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        if(started) throw new ServerAlreadyStartedException("Cannot change this variable when the server is started");
        else{
            if(apis == null || apis.length == 0) throw new ApiNotDefinedException("No API object given");
            else this.apis = apis;
        }
        
    }

    /**
     * Modifie le nombre de seconde pour qu'un certificat expire (la valeur sera effective sur les prochains certificats générés par le serveur)
     * @param timeoutGenerateCertificat Correspond au nouveau nombre de seconde pour qu'un certificat expire
     */
    public void setTimeoutGenerateCertificat(int timeoutGenerateCertificat) {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        this.timeoutGenerateCertificat = (timeoutGenerateCertificat<0) ? 60 : timeoutGenerateCertificat;
        
    }

    /**
     * Modifie l'encodage des requêtes envoyées à destination du client
     * @param charset Correspond au nouvel encodage des requêtes envoyées à destination du client
     */
    public void setCharset(String charset) {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        this.charset = (charset == null) ? "ISO-8859-1" : charset;
        
    }
    
    
    
//METHODES PUBLICS
    /**
     * Démarre le serveur API
     * @throws java.io.IOException S'il y a une erreur au démarrage du serveur
     */
    public void start() throws java.io.IOException {
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        if(apis == null) throw new ErrorStartException("APIs array is null");
        else if(apis.length == 0) throw new ErrorStartException("APIs array is empty");
        else{
            if(isStarted()) throw new ServerAlreadyStartedException("Server is started");
            else{
                this.encrypt = (this.cipher != null && this.keyCipher != null);
                this.server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(port), 0);
                createContext();
                this.server.setExecutor(null); // creates a default executor
                this.server.start();
                if(this.listener != null) this.listener.serverRestIsStarted(this.id);
                this.started = true;
            }
        }
        
    }
    
    /**
     * Arrête le serveur API
     */
    public void stop(){
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        if(this.listener != null) this.listener.serverRestIsStopped(this.id);
        this.server.stop(1);
        this.server = null;
        this.started = false;
        
    }
    
    /**
     * Génère un nouveau certificat
     */
    public void newCertificat(){
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        try {
            if(getCertificat() == null) this.certificat = new Certificat(this.cipher, this.keyCipher, this.timeoutGenerateCertificat);
            else generateNewCertificat();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
    }
    
    
    
//METHODES PRIVATES
    /**
     * Crée un serveur API
     * @param id Correspond à l'id unique donné pour ce serveur
     * @param port Correspond au port d'écoute des requêtes
     * @param apis Correspond à la liste des controleurs API qu'aura en charge le serveur
     */
    private void init(String id, int port, API[] apis){
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkLibs();
        
        //Initialisation du port
        if(port < 0) throw new PortTooSmallException("Port (" + port + ") is too small. The port must be greater than 0");
        else if(port > 65535) throw new PortTooLargeException("Port (" + port + ") is too large. The port must be less than 65535");
        else{
            if(portIsUsed(port)) throw new PortAlreadyUsedException("Port (" + port + ") is already used");
            else this.port = port;
        }
        
        //Initialisation de l'id
        this.id = (id == null || id.isEmpty()) ? this.id = "http://127.0.0.1:"+port+"/" : id;
        
        //Initialisation de(s) api(s)
        if(apis == null || apis.length == 0) throw new ApiNotDefinedException("No API object given");
        else this.apis = apis;
        
        //Détermine si le serveur est lancé
        this.started = false;
        
    }
    
    /**
     * Vérifie que GSON, JSON et Encryption soient installés
     */
    private void checkLibs(){
        
        //Vérifie que GSON, JSON et Encryption soient installés
        checkGsonInstalled();
        checkJSONInstalled();
        checkEncryptionInstalled();
        
    }
    
    /**
     * Renvoie le certificat du serveur
     * @return Retourne le certificat du serveur
     */
    private synchronized Certificat getCertificat(){
        return certificat;
    }
    
    /**
     * Génère un nouveau certificat
     * @throws NoSuchMethodException Lorsqu'une méthode particulière est introuvable
     * @throws InstantiationException Lorsqu'une application tente de créer une instance d'une classe à l'aide de la méthode newInstance() dans la classe Class, mais que l'objet de classe spécifié ne peut pas être instancié
     * @throws IllegalAccessException Lorsqu'une application tente de créer de manière réfléchie une instance (autre qu'un tableau), de définir ou d'obtenir un champ, ou d'appeler une méthode, mais que la méthode en cours d'exécution n'a pas accès à la définition de la classe, du champ, méthode ou constructeur
     * @throws IllegalArgumentException Lorsqu'une méthode a subi un argument illégal ou inapproprié
     * @throws java.lang.reflect.InvocationTargetException InvocationTargetException est une exception vérifiée qui encapsule une exception levée par une méthode ou un constructeur invoqué
     */
    private void generateNewCertificat() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, java.lang.reflect.InvocationTargetException{
        getCertificat().generateCertificat(cipher, keyCipher, timeoutGenerateCertificat);
    }
    
    /**
     * Affiche l'exception sans la traiter
     * @param exception Correspond à la classe de l'exception à lever
     * @param message Correspond au message de l'exception
     */
    private void executeException(Class exception, String message){
        try {
            throw ((Exception) exception.getConstructor(String.class).newInstance(message));
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Crée tous les contextes du serveur
     */
    private void createContext(){
        createContextIsEncrypted();
        createContextGetCertificat();
        createContextBase();
    }
    
    /**
     * Crée le contexte qui cherche à vérifier si les communications avec le serveur se font de manière chiffrés
     */
    private void createContextIsEncrypted(){
        server.createContext("/" + CONTEXT_ENCRYPT, new HandlerIsEncrypted());
    }
    
    /**
     * Crée le contexte qui permet d'envoyer un certificat au client
     */
    private void createContextGetCertificat(){
        if(encrypt){
            server.createContext("/" + CONTEXT_PUBLIC_CIPHER, new HandlerGetCertificat());
            if(getCertificat() == null) newCertificat();
        }
    }
    
    /**
     * Crée le contexte général qui écoute les demande du client et qui les redirigent sur les méthodes des classes API
     */
    private void createContextBase(){
        for (API api : this.apis) {
            if (api._getControllerApiContext() != null && api._getControllerApiContext().length() > 0) {
                java.lang.reflect.Method[] methods = api.getClass().getDeclaredMethods();
                for (java.lang.reflect.Method method : methods) {
                    if (authorizeCreateContext(method)) {
                        String context = formatContext(api._getControllerApiContext());
                        if (context != null) {
                            HandlerBase myhandler = new HandlerBase(context + "/" + method.getName(), api, method);
                            server.createContext(myhandler.getContext(), myhandler);
                        }
                    }
                }
            }
        }
    }

    /**
     * Renvoie l'encoder correspondant à la chaîne encodeuse de la classe serveur
     * @return Retourne l'encodeur correspondant à la chaîne encodeuse de la classe serveur
     */
    private java.nio.charset.Charset getCharsetEncoderByte() {
        switch(this.charset){
            case "UTF-8":
                return java.nio.charset.StandardCharsets.UTF_8;
            default:
                return java.nio.charset.StandardCharsets.ISO_8859_1;
        }
    }
    
    /**
     * Renvoie la classe de (dé)chiffrement
     * @return Retourne la classe de (dé)chiffrement
     * @throws Exception S'il y a une erreur pour générer la classe de (dé)chiffrement
     */
    private Cipher getCipherByCertificat() throws Exception {
        return (Cipher) Class.forName(getCertificat().getNameCipher()).getConstructor().newInstance();
    }
    
    /**
     * Renvoie la clef de (dé)chiffrement du certificat
     * @return Retourne la clef
     */
    private Key getKeyByCertificat(){
        return getCertificat().getKey();
    }
    
    
    
//METHODES PRIVATES STATICS
    /**
     * Détermine si le port TCP est ouvert
     * @param port Correspond au port à tester
     * @return Retourne true, s'il est ouvert, sinon false
     */
    private static boolean portIsUsed(int port){
        try {
            java.net.ServerSocket socket = new java.net.ServerSocket(port);
            socket.close();
            return false;
        } catch (java.io.IOException e) {
            return true;
        }
    }

    /**
     * Renvoie true si la classe représente un objet et non une classe byte, short, int, long, float, double, boolean, String
     * @param c Correspond à la classe à tester
     * @return Retourne true si la classe représente un objet et non une classe byte, short, int, long, float, double, boolean, String
     */
    private static boolean isObject(Class c) {
        return isObject(c.getSimpleName());
    }

    /**
     * Renvoie true si le nom du type représente un objet et non une classe byte, short, int, long, float, double, boolean, String
     * @param c Correspond au type à tester
     * @return Retourne true si le nom du type représente un objet et non une classe byte, short, int, long, float, double, boolean, String
     */
    private static boolean isObject(String type) {
        return !isPrimitive(type);
    }

    /**
     * Renvoie true si la classe représente la classe byte, short, int, long, float, double, boolean, String
     * @param c Correspond à la classe à tester
     * @return Retourne true si la classe correspond à la classe byte, short, int, long, float, double, boolean, String
     */
    private static boolean isPrimitive(Class c) {
        return isPrimitive(c.getSimpleName());
    }

    /**
     * Renvoie true si le nom du type représente la classe byte, short, int, long, float, double, boolean, String
     * @param type Correspond au type à tester
     * @return Retourne true si le nom du type correspond à la classe byte, short, int, long, float, double, boolean, String
     */
    private static boolean isPrimitive(String type) {
        java.util.List<String> types = new java.util.ArrayList<>();
        types.add("byte");
        types.add("short");
        types.add("int");
        types.add("long");
        types.add("float");
        types.add("double");
        types.add("boolean");
        types.add("String");

        return (types.contains(type));
    }

    /**
     * Renvoie true si la classe représente un tableau (ex: byte[], Voiture[]...)
     * @param c Correspond à la classe à tester
     * @return Retourne true si la classe représente un tableau (ex: byte[], Voiture[]...)
     */
    private static boolean isArray(Class c) {
        return isArray(c.getSimpleName());
    }

    /**
     * Renvoie true si la nom du type représente un tableau (ex: byte[], Voiture[]...)
     * @param c Correspond au type à tester
     * @return Retourne true si le nom du type représente un tableau (ex: byte[], Voiture[]...)
     */
    private static boolean isArray(String type) {
        int indexOpen = type.lastIndexOf("[");
        int indexClose = type.lastIndexOf("]");
        return (indexOpen > 0 && indexClose > -1 && (indexOpen + 1) == indexClose);
    }
    
    /**
     * Renvoie true si la nom du type représente un tableau primitif: byte[], short[], int[], long[], float[], double[], boolean[], String[]
     * @param c Correspond au type à tester
     * @return Retourne true si la nom du type représente un tableau primitif: byte[], short[], int[], long[], float[], double[], boolean[], String[]
     */
    private static boolean isArrayPrimitive(String type){
        java.util.List<String> types = new java.util.ArrayList<>();
        types.add("byte[]");
        types.add("short[]");
        types.add("int[]");
        types.add("long[]");
        types.add("float[]");
        types.add("double[]");
        types.add("boolean[]");
        types.add("String[]");

        return (types.contains(type));
    }
    
    /**
     * Renvoie le format du context (context = http://127.0.0.1:8080/[context]...) modifié pour qu'il correspondent à ce que le serveur souhaite. C-à-d, pas de / en fin de chaîne mais un / au début
     * @param context Correspond au context donné indirectement par l'utilisateur
     * @return Retourne le context modifié
     */
    private static String formatContext(String context){
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("^([a-zA-Z]{1}[a-zA-Z0-9_]*)|([/]{1}[a-zA-Z0-9_]+[/]?)$");
        java.util.regex.Matcher m = p.matcher(context);
        boolean b = m.matches();
        if(b){
            if(context.lastIndexOf("/") == context.length()-1) context = context.substring(0, context.length()-1);
            if(context.charAt(0) == '/') return context;
            else return "/"+context;
        }
        return null;
    }
    
    /**
     * Renvoie si oui ou non le serveur peut créer cette entrée
     * @param m Correspond à l'entrée (la demande de l'utilisateur)
     * @return Retourne true, si le serveur peut créer l'entrée, sinon false
     */
    private static boolean authorizeCreateContext(java.lang.reflect.Method m){
        if(java.lang.reflect.Modifier.isStatic(m.getModifiers()) || java.lang.reflect.Modifier.isAbstract(m.getModifiers()) || java.lang.reflect.Modifier.isPrivate(m.getModifiers()) || java.lang.reflect.Modifier.isProtected(m.getModifiers())) return false;
        if((m.getName().contains("_") && m.getName().indexOf("_")>0) || (!m.getName().contains("_"))){
            java.lang.reflect.Parameter[] ps = m.getParameters();
            int pass = 0;
            for(java.lang.reflect.Parameter p : ps){
                if(isObject(p.getType()) && pass == 0) pass = 1;
                else if(isPrimitive(p.getType()) && pass == 1) return false;
                else if(isObject(p.getType()) && pass == 1) return false;
                if(p.getType().getCanonicalName().lastIndexOf("]") == p.getType().getCanonicalName().length()-1 && !isArrayPrimitive(p.getType().getCanonicalName())) return false;
            }
            return true;
        }else return false;
    }
    
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
     * Cette classe permet au serveur de recevoir la demande du client qui demande s'il faut encrypter 
     */
    private class HandlerIsEncrypted implements com.sun.net.httpserver.HttpHandler {
        
        
        
    //METHODE PUBLIC
        /**
         * Cette méthode est appelée automatiquement par le serveur lorsque l'utilisateur fait une demande sur ce point d'entré
         * @param exchange Correspond au message de l'utilisateur envoyé sur ce point d'entré
         */
        @Override
        public void handle(com.sun.net.httpserver.HttpExchange exchange) throws java.io.IOException {
            
            //Reçoit l'éventuelle donnée envoyé par le client, mais on ne traite pas la donnée. D'où le fait que la ligne ne commance pas par String str =
            receive(exchange);
            
            //Crée la réponse qui dit si oui ou non le serveur encrypte ou pas
            String response = createResponse();
            
            //Envoie la réponse au client
            send(exchange, response);
            
        }
        
        
        
    //METHODES PRIVATES
        /**
         * Crée la réponse qui dit si oui ou non le serveur encrypte ou pas
         * @return Retourne la réponse
         */
        private String createResponse(){
            boolean response = (Server.this.encrypt);
            return JSON.serialize(response);
        }
        
        /**
         * Reçoit la demande du client
         * @param exchange Correspond à la demande de l'utilisateur
         * @return Retourne la demande du client
         */
        private String receive(com.sun.net.httpserver.HttpExchange exchange){
            return new Header(exchange).receiveBody();
        }
        
        /**
         * Envoie la réponse qui dit si oui ou non le serveur encrypte ou pas
         * @param exchange Correspond à la demande de l'utilisateur
         * @param isEncrypted Correspond à la réponse JSON à envoyer au client
         */
        @SuppressWarnings("UseSpecificCatch")
        private void send(com.sun.net.httpserver.HttpExchange exchange, String isEncrypted){
            try {
                new Header(exchange).sendSuccessMessage_WithException(isEncrypted);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        
        
        
    }
    
    /**
     * Cette classe permet au serveur de recevoir la demande de création d'un certificat du client puis de l'envoyer
     */
    private class HandlerGetCertificat implements com.sun.net.httpserver.HttpHandler {

        
        
    //METHODE PUBLIC
        /**
         * Cette méthode est appelée automatiquement par le serveur lorsque l'utilisateur fait une demande sur ce point d'entré
         * @param exchange Correspond au message de l'utilisateur envoyé sur ce point d'entré
         */
        @Override
        public void handle(com.sun.net.httpserver.HttpExchange exchange) throws java.io.IOException {
            
            //Récupère la clef publique RSA du message utilisateur
            KeyPublicRSA key = getPublicKey(exchange);
            
            //Si la clef est null alors on s'arrête là
            if(key == null){
                new Header(exchange).sendError400();
                return;
            }
            
            //Si le certificat a expiré, alors on en génère un autre et si cette génération plante on renvoie une erreur 404 au client
            if (certificateIsExpired()) {
                try {
                    generateNewCertificat();
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException ex) {
                    new Header(exchange).sendError404();
                    Server.this.executeException(ErrorCertificatException.class, "Certificat cannot be created.");
                    return;
                }
            }
            
            
            //On clone le nouveau et on chiffre au passage la clef de communication du certificat avec la clef publique
            Certificat certificat = Server.this.getCertificat().clone(key);
            
            //Serialise le certificat
            String json = JSON.serialize(certificat);
            
            //Envoie le certificat
            new Header(exchange).sendSuccessMessage(json);
            
        }
        
        
        
    //METHODES PRIVATES
        /**
         * Récupère la clef publique RSA du message utilisateur
         * @param exchange Correspond à la demande de l'utilisateur
         * @return Retourne la clef publique RSA qui servira à chiffrer la clef symétrique du certificat créé pour ce client
         */
        private KeyPublicRSA getPublicKey(com.sun.net.httpserver.HttpExchange exchange){
            
            String body = new Header(exchange).receiveBody();
            
            if(body == null) return null;
            
            Key key = (Key) com.jasonpercus.json.JSON.deserialize(KeyPublicRSA.class, body).getObj();
            
            if(key == null) return null;
            
            return new KeyPublicRSA(key.toString());
            
        }
        
        /**
         * Renvoie si oui ou non le certificat est expiré ou non
         * @return Retourne true si le certificat existe et qu'il est expiré
         */
        private boolean certificateIsExpired(){
            return (getCertificat() != null && getCertificat().isExpired());
        }
        
        
        
    }
    
    /**
     * Cette classe permet au serveur de faire le lien entre les demandes de l'utilisateur et les entrées dans l'API
     */
    private class HandlerBase implements com.sun.net.httpserver.HttpHandler {

        
        
    //ATTRIBUTS
        /**
         * Correspond au chemin d'une entrée dans l'API
         */
        private final String context;
        /**
         * Correspond à la classe qui rescence toutes les entrées demandable par l'utilisateur
         */
        private final API api;
        /**
         * Correspond au nom de la méthode faisant office d'entrée dans l'API
         */
        private final java.lang.reflect.Method method;
        /**
         * Détermine si le message reçu de l'utilisateur contient un objet
         */
        private boolean containBody;

        
        
    //CONSTRUCTOR
        /**
         * Crée un point d'entré pour l'utiilisateur dans le serveur API
         * @param context Correspond au chemin du point d'entré
         * @param api Correspond à la classe contenant toutes les méthodes points d'entré
         * @param method Correspond à la méthode faisant office de point d'entré
         */
        public HandlerBase(String context, API api, java.lang.reflect.Method method) {
            this.context = context;
            this.api = api;
            this.method = method;
        }

        
        
    //METHODES PUBLICS
        /**
         * Renvoie le chemin du point d'entré dans le serveur
         * @return Retourne le chemin
         */
        public String getContext() {
            return context;
        }

        /**
         * Cette méthode est appelée automatiquement par le serveur lorsque l'utilisateur fait une demande sur ce point d'entré
         * @param exchange Correspond au message de l'utilisateur envoyé sur ce point d'entré
         */
        @Override
        @SuppressWarnings("UseSpecificCatch")
        public synchronized void handle(com.sun.net.httpserver.HttpExchange exchange) {
            if(verifyExpiredCertificat(exchange)) return;
            containBody = true;
            
            java.util.List<UrlParameter> urlParamters = null;
            try{
                urlParamters = getURLParams(exchange);
            }catch(Exception e){
                returnError(exchange, 400);
            }
            if(certificatIsValid(urlParamters)){
            
                //Récupère l'objet envoyé du client
                String bodyJson = (getCertificat()!=null) ? getBodyDecrypted(exchange) : getBodyJson(exchange);
                
                java.util.List<MethodesParameter> methodParameters = getMethodParams();
                
                int urlParametersSize = (urlParamters==null) ? 0 : urlParamters.size();
                int countParametersReceived = urlParametersSize + ((containBody) ? 1 : 0);
                if(countParametersReceived == methodParameters.size()){
                    int index = 0;
                    Object[] objs = new Object[countParametersReceived];
                    for(MethodesParameter methodParameter : methodParameters){
                        if(index<urlParametersSize){
                            UrlParameter urlParamter = (urlParamters == null) ? null : urlParamters.get(index);
                            if(urlParamter != null){
                                try{
                                    objs[index] = getCastedObjectPrimitive(methodParameter.typeName, urlParamter.value);
                                }catch(java.lang.NumberFormatException e){
                                    returnError(exchange, 400);
                                    return;
                                }
                            }
                        }else{
                            if(isArrayPrimitive(methodParameter.typeName)){
                                try{
                                    objs[index] = getCastedObjectArray(methodParameter.typeName, bodyJson);
                                }catch(java.lang.NumberFormatException e){
                                    returnError(exchange, 400);
                                    return;
                                }
                            }else if(isPrimitive(methodParameter.typeName)){
                                try{
                                    objs[index] = getCastedObjectPrimitive(methodParameter.typeName, bodyJson);
                                }catch(java.lang.NumberFormatException e){
                                    returnError(exchange, 400);
                                    return;
                                }
                            }else{
                                try {
                                    JSON json = JSON.deserialize(Class.forName(methodParameter.typeName), bodyJson);
                                    objs[index] = (json.isArray()) ? json.getList() : json.getObj();
                                } catch (Exception ex) {
                                    returnError(exchange, 400);
                                    return;
                                }
                            }
                        }
                        index++;
                    }

                    try {
                        if(canReturn()){
                            Object o = invokeObject(objs);
                            returnObj(exchange, o);
                        }else{
                            invokeVoid(objs);
                            returnVoid(exchange);
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException ex) {
                        returnError(exchange, 400);
                    }
                }else{
                    returnError(exchange, 400);
                }
            }
        }
        
        
        
    //METHODES PRIVATES
        /**
         * Renvoie l'objet value casté du nom de la classe primitive donnée en paramètre
         * @param nameType Correspond au nom de la classe primitive qui castera l'objet
         * @param value Correspond à l'objet à caster
         * @return Retourne l'objet casté
         * @throws java.lang.NumberFormatException S'il y a une erreur de conversion
         */
        private Object getCastedObjectPrimitive(String nameType, String value) throws java.lang.NumberFormatException{
            switch (nameType) {
                case "byte":
                    return Byte.parseByte(value);
                case "short":
                    return Short.parseShort(value);
                case "int":
                    return Integer.parseInt(value);
                case "long":
                    return Long.parseLong(value);
                case "float":
                    return Float.parseFloat(value);
                case "double":
                    return Double.parseDouble(value);
                case "boolean":
                    return Boolean.parseBoolean(value);
                case "String":
                    return value;
            }
            throw new java.lang.NumberFormatException();
        }

        /**
         * Renvoie l'objet value casté du nom de la classe tableau primitive donnée en paramètre
         * @param nameType Correspond au nom de la classe tableau primitif qui castera l'objet
         * @param value Correspond à l'objet à caster
         * @return Retourne l'objet casté
         * @throws java.lang.NumberFormatException S'il y a une erreur de conversion
         */
        private Object getCastedObjectArray(String nameType, String value) throws java.lang.NumberFormatException{
            if(nameType.equals("byte[]")){
                String newValue = value;
                if(newValue.length()>=2 && newValue.indexOf("[") == 0 && newValue.lastIndexOf("]") == newValue.length()-1){
                    newValue = newValue.replace("[", "").replace("]", "").replace(" ", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\0", "");
                    String[] split = newValue.split(",");
                    int size;
                    switch (split.length) {
                        case 0: size = 0; break;
                        case 1: size = (split[0].length() <= 0) ? 0 : 1; break;
                        default: size = split.length; break;
                    }
                    byte[] array = new byte[size];
                    for (int i = 0; i < size; i++) array[i] = Byte.parseByte(split[i]);
                    return array;
                }else{
                    throw new java.lang.NumberFormatException();
                }
            }
            if(nameType.equals("short[]")){
                String newValue = value;
                if(newValue.length()>=2 && newValue.indexOf("[") == 0 && newValue.lastIndexOf("]") == newValue.length()-1){
                    newValue = newValue.replace("[", "").replace("]", "").replace(" ", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\0", "");
                    String[] split = newValue.split(",");
                    int size;
                    switch (split.length) {
                        case 0: size = 0; break;
                        case 1: size = (split[0].length() <= 0) ? 0 : 1; break;
                        default: size = split.length; break;
                    }
                    short[] array = new short[size];
                    for (int i = 0; i < size; i++) array[i] = Short.parseShort(split[i]);
                    return array;
                }else{
                    throw new java.lang.NumberFormatException();
                }
            }
            if(nameType.equals("int[]")){
                String newValue = value;
                if(newValue.length()>=2 && newValue.indexOf("[") == 0 && newValue.lastIndexOf("]") == newValue.length()-1){
                    newValue = newValue.replace("[", "").replace("]", "").replace(" ", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\0", "");
                    String[] split = newValue.split(",");
                    int size;
                    switch (split.length) {
                        case 0: size = 0; break;
                        case 1: size = (split[0].length() <= 0) ? 0 : 1; break;
                        default: size = split.length; break;
                    }
                    int[] array = new int[size];
                    for (int i = 0; i < size; i++) array[i] = Integer.parseInt(split[i]);
                    return array;
                }else{
                    throw new java.lang.NumberFormatException();
                }
            }
            if(nameType.equals("long[]")){
                String newValue = value;
                if(newValue.length()>=2 && newValue.indexOf("[") == 0 && newValue.lastIndexOf("]") == newValue.length()-1){
                    newValue = newValue.replace("[", "").replace("]", "").replace(" ", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\0", "");
                    String[] split = newValue.split(",");
                    int size;
                    switch (split.length) {
                        case 0: size = 0; break;
                        case 1: size = (split[0].length() <= 0) ? 0 : 1; break;
                        default: size = split.length; break;
                    }
                    long[] array = new long[size];
                    for (int i = 0; i < size; i++) array[i] = Long.parseLong(split[i]);
                    return array;
                }else{
                    throw new java.lang.NumberFormatException();
                }
            }
            if(nameType.equals("float[]")){
                String newValue = value;
                if(newValue.length()>=2 && newValue.indexOf("[") == 0 && newValue.lastIndexOf("]") == newValue.length()-1){
                    newValue = newValue.replace("[", "").replace("]", "").replace(" ", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\0", "");
                    String[] split = newValue.split(",");
                    int size;
                    switch (split.length) {
                        case 0: size = 0; break;
                        case 1: size = (split[0].length() <= 0) ? 0 : 1; break;
                        default: size = split.length; break;
                    }
                    float[] array = new float[size];
                    for (int i = 0; i < size; i++) array[i] = Float.parseFloat(split[i]);
                    return array;
                }else{
                    throw new java.lang.NumberFormatException();
                }
            }
            if(nameType.equals("double[]")){
                String newValue = value;
                if(newValue.length()>=2 && newValue.indexOf("[") == 0 && newValue.lastIndexOf("]") == newValue.length()-1){
                    newValue = newValue.replace("[", "").replace("]", "").replace(" ", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\0", "");
                    String[] split = newValue.split(",");
                    int size;
                    switch (split.length) {
                        case 0: size = 0; break;
                        case 1: size = (split[0].length() <= 0) ? 0 : 1; break;
                        default: size = split.length; break;
                    }
                    double[] array = new double[size];
                    for (int i = 0; i < size; i++) array[i] = Double.parseDouble(split[i]);
                    return array;
                }else{
                    throw new java.lang.NumberFormatException();
                }
            }
            if(nameType.equals("boolean[]")){
                String newValue = value;
                if(newValue.length()>=2 && newValue.indexOf("[") == 0 && newValue.lastIndexOf("]") == newValue.length()-1){
                    newValue = newValue.replace("[", "").replace("]", "").replace(" ", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\0", "");
                    String[] split = newValue.split(",");
                    int size;
                    switch (split.length) {
                        case 0: size = 0; break;
                        case 1: size = (split[0].length() <= 0) ? 0 : 1; break;
                        default: size = split.length; break;
                    }
                    boolean[] array = new boolean[size];
                    for (int i = 0; i < size; i++){
                        if(split[i].toLowerCase().equals("true")||split[i].toLowerCase().equals("false")){
                            array[i] = Boolean.parseBoolean(split[i].toLowerCase());
                        }else{
                            throw new java.lang.NumberFormatException();
                        }
                    }
                    return array;
                }else{
                    throw new java.lang.NumberFormatException();
                }
            }
            if(nameType.equals("String[]")){
                String newValue = value;
                if(newValue.length()>=2 && newValue.indexOf("[") == 0 && newValue.lastIndexOf("]") == newValue.length()-1){
                    newValue = newValue.replace("[", "").replace("]", "").replace(" ", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\0", "");
                    String[] split = newValue.split(",");
                    return split;
                }else{
                    throw new java.lang.NumberFormatException();
                }
            }
            throw new java.lang.NumberFormatException();
        }
        
        /**
         * Appelle la méthode (type retour: void) de l'objet en lui fournissant des paramètres
         * @param params Correspond au paramètres à fournir à la méthode
         */
        private void invokeVoid(Object... params) throws IllegalAccessException, IllegalArgumentException, java.lang.reflect.InvocationTargetException {
            if (params == null) {
                method.invoke(api);
            } else {
                method.invoke(api, params);
            }
        }

        /**
         * Appelle la méthode de l'objet en lui fournissant des paramètres
         * @param params Correspond au paramètres à fournir à la méthode
         * @return Retourne l'objet de la méthode invoquée
         */
        private Object invokeObject(Object... params) throws IllegalAccessException, IllegalArgumentException, java.lang.reflect.InvocationTargetException {
            if (params == null) {
                return method.invoke(api);
            } else {
                return method.invoke(api, params);
            }
        }

        /**
         * Renvoie au client un objet void (c'est-à-dire rien)
         * @param exchange Correspond à la demande de l'utilisateur
         */
        @SuppressWarnings("UseSpecificCatch")
        private void returnVoid(com.sun.net.httpserver.HttpExchange exchange) {
            new Header(exchange).sendSuccessMessage(null);
        }

        /**
         * Renvoie au client un objet
         * @param exchange Correspond à la demande de l'utilisateur
         * @param obj Correspond à l'objet à envoyer à l'utilisateur
         */
        @SuppressWarnings("UseSpecificCatch")
        private void returnObj(com.sun.net.httpserver.HttpExchange exchange, Object obj) {
            try {
                //Correspond au json de l'objet à envoyer au client
                String json = JSON.serialize(obj);
                
                //Si le certificat n'est pas null, alors le JSON est chiffré
                if(getCertificat() != null)
                    json = getCipherByCertificat().encrypt(getCertificat().getKey(), json);
                
                //Envoie le JSON chiffré au client
                new Header(exchange).sendSuccessMessage_WithException(json);
                
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }

        /**
         * Renvoie un code d'erreur au client (ex: 404)
         * @param exchange Correspond à la demande de l'utilisateur
         * @param errorCode Correspond au code d'erreur à envoyer au client
         */
        @SuppressWarnings("UseSpecificCatch")
        private void returnError(com.sun.net.httpserver.HttpExchange exchange, int errorCode) {
            switch(errorCode){
                case 404:
                    new Header(exchange).sendError404();
                    break;
                default:
                    new Header(exchange).sendError400();
                    break;
            }
        }

        /**
         * Détermine si la méthode de cet objet renvoie quelque chose ou un void
         * @return Retourne true, si elle renvoie un objet, sinon false s'il s'agit d'un void
         */
        private boolean canReturn() {
            String typeReturn = method.getReturnType().getCanonicalName();
            return !typeReturn.equals("void");
        }

        /**
         * Renvoie un tableau de paramètres trasmis dans l'url du client
         * @param exchange Correspond à la demande du client
         * @return Retourne un tableau de paramètres
         */
        private java.util.List<UrlParameter> getURLParams(com.sun.net.httpserver.HttpExchange exchange) throws Exception {
            java.util.List<UrlParameter> params = new java.util.ArrayList<>();
            String url = exchange.getRequestURI().getQuery();
            if(url == null) return params;
            if(url.contains(Server.BASE_PARAMS_ENCRYPTED+"=")){
                String[] parameters = url.split("&");
                UrlParameter urlTempo = null;
                if (parameters != null) {
                    for (String parameter : parameters) {
                        String[] entries = parameter.split("=");
                        if (entries != null && entries.length == 2) {
                            if(entries[0].equals(Server.BASE_PARAMS_ENCRYPTED)){
                                urlTempo = new UrlParameter(entries[0], entries[1]);
                            }
                        } else if (entries != null && entries.length == 1) {
                            if(entries[0].equals(Server.BASE_PARAMS_ENCRYPTED)){
                                urlTempo = new UrlParameter(entries[0], "");
                            }
                        }
                    }
                }
                if(urlTempo != null){
                    String replacer = urlTempo.value.replace(Server.CHAR_ASLAS, "\\").replace(Server.CHAR_EQUAL, "=").replace(Server.CHAR_MINUS, "-").replace(Server.CHAR_PLUS, "+").replace(Server.CHAR_SLASH, "/").replace(Server.CHAR_SLASHN, "\n").replace(Server.CHAR_SLASHR, "\r").replace(Server.CHAR_SLASHT, "\t");
                    if(replacer != null && replacer.length()>0 && getCertificat() != null){
                        try{
                            String decrypted = getCipherByCertificat().decrypt(getKeyByCertificat(), replacer);
                            url = url.replace(urlTempo.key+"="+urlTempo.value, decrypted);
                        }catch(javax.crypto.BadPaddingException e){
                            throw e;
                        }
                    }
                }
            }
            if (url != null) {
                if(url.length() == 0) return params;
                String[] parameters = url.split("&");
                if (parameters != null) {
                    for (String parameter : parameters) {
                        String[] entries = parameter.split("=");
                        if (entries != null && entries.length == 2) {
                            params.add(new UrlParameter(entries[0], entries[1]));
                        } else if (entries != null && entries.length == 1) {
                            params.add(new UrlParameter(entries[0], ""));
                        }
                    }
                }
            }
            return params;
        }

        /**
         * Renvoie un tableau de paramètres (ex; String arg0, int arg1, float arg2...) de la méthode de l'objet
         * @return Retourne un tableau de paramètres de la méthode de l'objet
         */
        private java.util.List<MethodesParameter> getMethodParams() {
            java.util.List<MethodesParameter> list = new java.util.ArrayList<>();
            java.lang.reflect.Parameter[] ps = method.getParameters();
            if (ps != null) {
                for (int i = 0; i < ps.length; i++) {
                    list.add(new MethodesParameter().getParameter(method, i));
                }
            }
            return list;
        }

        /**
         * Renvoie le texte json de l'objet transmit par l'utilisateur
         * @param exchange Correspond à la demande de l'utilisateur
         * @return Retourne le texte json de l'objet transmit par l'utilisateur
         */
        private String getBodyJson(com.sun.net.httpserver.HttpExchange exchange) {
            String requestJson;
            try {
                java.io.InputStreamReader requestBody = new java.io.InputStreamReader(exchange.getRequestBody(), Server.this.charset);
                try (java.util.Scanner scanner = new java.util.Scanner(requestBody)) {
                    requestJson = scanner.useDelimiter("\\A").next();
                }
                if (requestJson != null && requestJson.equals("null")) {
                    requestJson = null;
                }
            } catch (Exception e) {
                requestJson = null;
                containBody = false;
            }
            return requestJson;
        }
        
        /**
         * Renvoie l'index du paramètre contenant le hash
         * @param params Correspond à la liste des paramètres d'url
         * @return Retourne l'index du paramètre contenant le hash
         */
        private int containsHash(java.util.List<UrlParameter> params){
            for(int i=0;i<params.size();i++){
                if(params.get(i).key.equals(BASE_PARAM_HASH)) return i;
            }
            return -1;
        }
        
        /**
         * Renvoie true si le hash contenu dans l'url est égale au hash du certificat
         * @param urlParamters Correspond à la liste des paramètres d'url
         * @return Retourne true si le hash est identique ou s'il n'y a pas de hash dans l'url
         */
        private boolean certificatIsValid(java.util.List<UrlParameter> urlParamters){
            int indexHash = containsHash(urlParamters);
            if(indexHash>-1){
                UrlParameter param = urlParamters.get(indexHash);
                if(param.value.equals(getCertificat().getHash())){
                    urlParamters.remove(indexHash);
                    return true;
                }else{
                    urlParamters.remove(indexHash);
                    return false;
                }
            }else{
                return true;
            }
        }
        
        /**
         * Renvoie l'objet body déchiffré
         * @param exchange Correspond à la demande de l'utilisateur
         * @return Retourne l'objet post déchiffré
         */
        private String getBodyDecrypted(com.sun.net.httpserver.HttpExchange exchange){
            try {
                String jsonEncrypted = getBodyJson(exchange);
                Cipher cipher = (Cipher) Class.forName(getCertificat().getNameCipher()).getConstructor().newInstance();
                return cipher.decrypt(getCertificat().getKey(), jsonEncrypted);
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            return null;
        }
        
        /**
         * Lorsque le certificat est expiré le serveur renvoie un message au client à ce propos
         * @param exchange Correspond à la demande de l'utilisateur
         * @return Retourne true si oui le certificat a expiré
         */
        private boolean verifyExpiredCertificat(com.sun.net.httpserver.HttpExchange exchange){
            if(getCertificat() == null) return false;
            if(getCertificat().isExpired()){
                try {
                    
                    Header header = new Header(exchange);
                
                    header.receiveBody();
                    
                    String json = JSON.serialize(MESSAGE_CERTIFICAT_EXPIRED);
                    
                    if(getCertificat() != null)
                        json = getCipherByCertificat().encrypt(certificat.getKey(), json);
                    
                    header.sendSuccessMessage_WithException(json);
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                return true;
            }
            return false;
        }

        
        
    //CLASS
        /**
         * Cette classe permet de représenter un paramètre transmit par l'utilisateur dans l'url
         */
        private class UrlParameter {

            
            
        //ATTRIBUTS
            /**
             * Correspond à la clef du paramètre
             */
            public String key;
            /**
             * Correspond à la valeur du paramètre
             */
            public String value;

            
            
        //CONSTRUCTOR
            /**
             * Crée un paramètre d'url
             * @param key Correspond à la clef du paramètre
             * @param value Correspond à la valeur du paramètre
             */
            public UrlParameter(String key, String value) {
                this.key = key;
                this.value = value;
            }

            
            
        //METHODE PUBLIC
            /**
             * Renvoie l'objet sous la forme d'une chaîne de caractères
             * @return Retourne l'objet sous la forme d'une chaîne de caractères
             */
            @Override
            public String toString() {
                return "UrlParameter{" + "key=" + key + ", value=" + value + '}';
            }

            
            
        }

        /**
         * Cette classe permet de représenter un paramètre de la méthode du point d'entré
         */
        private class MethodesParameter {

            
            
        //ATTRIBUTS
            /**
             * Correspond au nom du type du paramètre
             */
            public String typeName;
            /**
             * Correspond au nom de la variable du paramètre
             */
            public String valueName;
            /**
             * Détermine si l'objet est un tableau (lorsqu'il s'agit d'un tableau, il s'agit également d'un objet)
             */
            public boolean isArray;
            /**
             * Détermine si l'objet est un objet primitif (byte, short, int, long, float, double, boolean, String)
             */
            public boolean isPrimitive;
            /**
             * Détermine s'il s'agit uniquement d'un objet
             */
            public boolean isObject;

            
            
        //CONSTRUCTORS
            /**
             * Crée un paramètre vide
             */
            public MethodesParameter() {
            }

            /**
             * Crée un paramètre de la méthode point d'entré
             * @param typeName Correspond au nom du type du paramètre
             * @param valueName Correspond au nom de la variable du paramètre
             */
            private MethodesParameter(String typeName, String valueName) {
                this.typeName = typeName;
                this.valueName = valueName;
            }

            
            
        //METHODE PUBLIC
            /**
             * Crée et renvoie un MethodesParameter en fonction de la méthode point d'entré et de l'index du paramètre dans la signature de la méthode
             * @param m Correspond à la méthode point d'entré
             * @param index Correspond à l'index du paramètre dans la signature de la méthode (ex: Imaginons la signature de la méthode suivante myMethod(String arg0, int arg1), alors,  myMethod, 0 = String arg0)
             * @return Retourne un MethodesParameter
             */
            public MethodesParameter getParameter(java.lang.reflect.Method m, int index) {
                
                java.lang.reflect.Parameter[] ps = m.getParameters();
                java.lang.reflect.Parameter p = ps[index];
                boolean isPri = Server.isPrimitive(p.getType());
                boolean isArr = Server.isArray(p.getType());
                boolean isObj = Server.isObject(p.getType());
                MethodesParameter mp;
                if ((isObj && !isArr) || (isObj && isArr)) {
                    java.lang.reflect.Type[] ts = m.getGenericParameterTypes();
                    java.lang.reflect.Type t = ts[index];
                    String nameT = t.getTypeName();
                    if(isObj && !isArr && nameT.contains("java.util.List<") && nameT.contains(">")&& !nameT.equals("java.util.List<>") && "java.util.List<".length() < nameT.length()-1){
                        mp = new MethodesParameter(nameT.substring("java.util.List<".length(), nameT.length()-1), p.getName());
                    }else{
                        mp = new MethodesParameter(p.getType().getCanonicalName(), p.getName());
                    }
                } else {
                    mp = new MethodesParameter(p.getType().getSimpleName(), p.getName());
                }
                mp.isArray = isArr;
                mp.isPrimitive = isPri;
                mp.isObject = isObj;
                return mp;
            }
            
            
            
        }

        
        
    }
    
    /**
     * Cette classe permet de gérer les envois et les réceptions http
     */
    private class Header {
        
        
        
    //ATTRIBUTS
        /**
         * Correspond à la demande de l'utilisateur
         */
        private final com.sun.net.httpserver.HttpExchange he;

        
        
    //CONSTRUCTOR
        /**
         * Crée un header
         * @param he Correspond à la demande de l'utilisateur
         */
        public Header(com.sun.net.httpserver.HttpExchange he) {
            this.he = he;
        }
    
        
        
    //METHODES PUBLICS
        /**
         * Renvoie ce que contient le body du message client
         * @return Retourne ce que contient le body du message client ou null, s'il n'y a rien
         */
        public String receiveBody(){
            String objJsonReceived;
            try {
                java.io.InputStreamReader requestBody = new java.io.InputStreamReader(he.getRequestBody(), Server.this.charset);
                try (java.util.Scanner scanner = new java.util.Scanner(requestBody)) {
                    objJsonReceived = scanner.useDelimiter("\\A").next();
                }
                if (objJsonReceived != null && objJsonReceived.equals("null")) {
                    objJsonReceived = null;
                }
            } catch (Exception e) {
                objJsonReceived = null;
            }
            return objJsonReceived;
        }
        
        
        /**
         * Envoie un message de succès au client
         * @param message Correspond au message à envoyer (null si l'on souhaite seulement envoyer le code 200 sans objet)
         */
        @SuppressWarnings("UseSpecificCatch")
        public void sendSuccessMessage(String message){
            try {
                sendSuccessMessage_WithException(message);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Envoie un message de succès au client
         * @param message Correspond au message à envoyer (null si l'on souhaite seulement envoyer le code 200 sans objet)
         * @throws IOException Si une erreur survient dans la transmission
         */
        public void sendSuccessMessage_WithException(String message) throws Exception{
            setHeader();
            setCodeResponse(200, message);
            setResponse(message);
            closeHeader();
        }
        
        /**
         * Envoie une erreur 400 au client
         * @throws IOException Si une erreur survient dans la transmission
         */
        public void sendError400(){
            try {
                sendError400_WithException();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Envoie une erreur 400 au client
         * @throws IOException Si une erreur survient dans la transmission
         */
        public void sendError400_WithException() throws Exception{
            setHeader();
            setCodeResponse(400, null);
            closeHeader();
        }
        
        /**
         * Envoie une erreur 404 au client
         * @throws IOException Si une erreur survient dans la transmission
         */
        public void sendError404(){
            try {
                sendError404_WithException();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Envoie une erreur 404 au client
         * @throws IOException Si une erreur survient dans la transmission
         */
        public void sendError404_WithException() throws Exception{
            setHeader();
            setCodeResponse(404, null);
            closeHeader();
        }
        
        /**
         * Définit le header de la réponse du serveur au client
         */
        public void setHeader(){
            this.he.getResponseHeaders().set("Content-Type", "application/json; charset=" + Server.this.getCharset());
        }

        /**
         * Définit le code de réponse du serveur au client
         * @param code Correspond au code de réponse
         * @throws IOException Si le code de réponse ne peut être défini
         */
        public void setCodeResponse(int code) throws Exception{
            this.setCodeResponse(code, null);
        }

        /**
         * Définit le code de réponse du serveur au client
         * @param code Correspond au code de réponse
         * @param responseToSend Correspond au texte à renvoyer au client
         * @throws IOException Si le code de réponse ne peut être défini
         */
        public void setCodeResponse(int code, String responseToSend) throws Exception{
            this.he.sendResponseHeaders(code, (responseToSend == null) ? 0 : responseToSend.length());
        }

        /**
         * Définit la réponse du serveur au client
         * @param responseToSend Correspond au texte à renvoyer au client
         * @throws IOException Si la réponse ne peut être défini
         */
        public void setResponse(String responseToSend) throws Exception{
            if(responseToSend != null)
                this.he.getResponseBody().write(responseToSend.getBytes(getCharsetEncoderByte()));
        }

        /**
         * Ferme la demande serveur/cliente
         */
        public void closeHeader(){
            this.he.close();
        }
        
        
        
    }
    
    
    
}