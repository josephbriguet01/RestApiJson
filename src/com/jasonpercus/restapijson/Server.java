/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package com.jasonpercus.restapijson;



import com.jasonpercus.restapijson.exception.ErrorStartException;
import com.jasonpercus.json.JSON;



/**
 * Cette classe permet de créer, lancer un serveur API et de rediriger les requêtes d'un client vers la classe API
 * @author Briguet
 * @version 1.0
 */
public class Server {

    
    
//ATTRIBUTS
    /**
     * Correspond à la liste des objets Mapping qui contient un serveur, une classe API, un listener et un id
     */
    private static final java.util.List<Mapping> MAPPINGS = new java.util.ArrayList<>();
    
    /**
     * Correspond à l'encodage des requêtes envoyées à destination du client
     */
    private static String theCharset = "ISO-8859-1";


    
//METHODES PUBLICS
    /**
     * Permet de modifier l'encodage des requêtes envoyées à destination du client
     * @param charset Correspond à l'encodage (ex: ISO-8859-1, UTF-8...)
     */
    public static void changeCharset(String charset){
        theCharset = charset;
    }
    
    /**
     * Renvoie si oui ou non le ou les serveurs sont bien tous lancés
     * @return Retourne true, s'il l'est ou s'ils le sont, sinon false
     */
    public static boolean isStart(){
        for(Mapping m : MAPPINGS){
            if(m.server == null) return false;
        }
        return true;
    }
    
    /**
     * Renvoie si oui ou non le serveur est bien lancé
     * @param id Correspond à l'id du serveur
     * @return Retourne true, s'il l'est sinon false
     */
    public static Boolean isStart(String id){
        for(Mapping m : MAPPINGS){
            if(m.is(id)){
                return m.server != null;
            }
        }
        return null;
    }
    
    /**
     * Renvoie le listener d'un serveur
     * @param id Correspond à l'id du serveur dont on veut récupérer le listener
     * @return Retourne le listener d'un serveur
     */
    public static IServer getListener(String id){
        for(Mapping m : MAPPINGS){
            if(m.is(id)){
                return m.listener;
            }
        }
        return null;
    }
    
    /**
     * Modifie le listene d'un serveur
     * @param id Correspond à l'id du serveur dont on veut modifier le listener
     * @param listener Correspond au nouveau listener
     */
    public static void setListener(String id, IServer listener){
        for(Mapping m : MAPPINGS){
            if(m.is(id)){
                m.listener = listener;
            }
        }
    }
    
    /**
     * Démarre un serveur API
     * @param apis Correpsond à la liste des controleurs API qu'aura en charge le serveur
     * @throws java.io.IOException S'il y a une erreur au démarrage du serveur
     */
    public static void start(API... apis) throws java.io.IOException{
        start(8080, apis);
    }

    /**
     * Démarre un serveur API
     * @param port Correspond au port d'écoute des requêtes
     * @param apis Correpsond à la liste des controleurs API qu'aura en charge le serveur
     * @throws java.io.IOException S'il y a une erreur au démarrage du serveur
     */
    public static void start(int port, API... apis) throws java.io.IOException{
        start(null, port, apis);
    }
    
    /**
     * Démarre un serveur API
     * @param id Correpsond à l'id unique donné pour ce serveur
     * @param apis Correpsond à la liste des controleurs API qu'aura en charge le serveur
     * @throws java.io.IOException S'il y a une erreur au démarrage du serveur
     */
    public static void start(String id, API... apis) throws java.io.IOException{
        start(id, 8080, apis);
    }
    
    /**
     * Démarre un serveur API
     * @param id Correpsond à l'id unique donné pour ce serveur
     * @param port Correspond au port d'écoute des requêtes
     * @param apis Correpsond à la liste des controleurs API qu'aura en charge le serveur
     * @throws java.io.IOException S'il y a une erreur au démarrage du serveur
     */
    public static void start(String id, int port, API... apis) throws java.io.IOException{
        start(id, port, null, apis);
    }
    
    /**
     * Démarre un serveur API
     * @param listener Correspond à l'objet listener de ce serveur
     * @param apis Correpsond à la liste des controleurs API qu'aura en charge le serveur
     * @throws java.io.IOException S'il y a une erreur au démarrage du serveur
     */
    public static void start(IServer listener, API... apis) throws java.io.IOException{
        start(8080, listener, apis);
    }
    
    /**
     * Démarre un serveur API
     * @param port Correspond au port d'écoute des requêtes
     * @param listener Correspond à l'objet listener de ce serveur
     * @param apis Correpsond à la liste des controleurs API qu'aura en charge le serveur
     * @throws java.io.IOException S'il y a une erreur au démarrage du serveur
     */
    public static void start(int port, IServer listener, API... apis) throws java.io.IOException{
        start(null, port, listener, apis);
    }
    
    /**
     * Démarre un serveur API
     * @param id Correpsond à l'id unique donné pour ce serveur
     * @param listener Correspond à l'objet listener de ce serveur
     * @param apis Correpsond à la liste des controleurs API qu'aura en charge le serveur
     * @throws java.io.IOException S'il y a une erreur au démarrage du serveur
     */
    public static void start(String id, IServer listener, API... apis) throws java.io.IOException{
        start(id, 8080, listener, apis);
    }
    
    /**
     * Démarre un serveur API
     * @param id Correpsond à l'id unique donné pour ce serveur
     * @param port Correspond au port d'écoute des requêtes
     * @param listener Correspond à l'objet listener de ce serveur
     * @param apis Correpsond à la liste des controleurs API qu'aura en charge le serveur
     * @throws java.io.IOException S'il y a une erreur au démarrage du serveur
     */
    public static void start(String id, int port, IServer listener, API... apis) throws java.io.IOException {
        if(apis == null) throw new ErrorStartException("APIs array is null");
        else if(apis.length == 0) throw new ErrorStartException("APIs array is empty");
        else{
            Mapping mapping = null;
            for(Mapping m : MAPPINGS){
                if(m.is(id)){
                    mapping = m;
                }
            }
            if(mapping != null){
                if (mapping.server == null) {
                    mapping.apis = apis;
                    mapping.listener = listener;
                    mapping.server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(port), 0);
                    createContext(mapping.server, mapping.apis);
                    mapping.server.setExecutor(null); // creates a default executor
                    mapping.server.start();
                    if(mapping.listener != null) mapping.listener.serverRestIsStarted(mapping.id);
                }
            }else{
                com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(port), 0);
                createContext(server, apis);
                server.setExecutor(null); // creates a default executor
                server.start();
                if(id == null || id.isEmpty()) id = "http://127.0.0.1:"+port+"/";
                MAPPINGS.add(new Mapping(id, server, listener, apis));
                if(listener != null) listener.serverRestIsStarted(id);
            }
        }
    }
    
    /**
     * Arrête le ou les serveurs lancés
     */
    public static void stop(){
        for(Mapping m : MAPPINGS){
            if(m.server != null){
                if(m.listener != null) m.listener.serverRestIsStopped(m.id);
                m.server.stop(1);
                m.server = null;
            }
        }
    }

    /**
     * Arrête un serveur
     * @param id Correspond à l'id du serveur à arrêter
     */
    public static void stop(String id) {
        Mapping mapping = null;
        for(Mapping m : MAPPINGS){
            if(m.is(id)){
                mapping = m;
            }
        }
        if(mapping != null){
            if (mapping.server != null) {
                if(mapping.listener != null) mapping.listener.serverRestIsStopped(mapping.id);
                mapping.server.stop(1);
                mapping.server = null;
            }
        }
    }

    
    
//METHODES PRIVATES
    /**
     * Crée un processus qui écoutera un appel à une url donnée
     * @param server Correspond au serveur http lancé
     * @param apis Correspond à la liste des classes contrôleuses
     */
    private static void createContext(com.sun.net.httpserver.HttpServer server, API... apis) {
        for (API api : apis) {
            if (api._getControllerApiContext() != null && api._getControllerApiContext().length() > 0) {
                java.lang.reflect.Method[] methods = api.getClass().getDeclaredMethods();
                for (java.lang.reflect.Method method : methods) {
                    if (authorizeCreateContext(method)) {
                        String context = formatContext(api._getControllerApiContext());
                        if (context != null) {
//                            System.out.println(method.getName());
                            ServerHandler myhandler = new ServerHandler(context + "/" + method.getName(), api, method);
                            server.createContext(myhandler.getContext(), myhandler);
                        }
                    }
                }
            }
        }
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

    
    
//CLASS
    /**
     * Cette classe permet au serveur de faire le lien entre les demandes de l'utilisateur et les entrées dans l'API
     */
    private static class ServerHandler implements com.sun.net.httpserver.HttpHandler {

        
        
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
        public ServerHandler(String context, API api, java.lang.reflect.Method method) {
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
        public void handle(com.sun.net.httpserver.HttpExchange exchange) {
            containBody = true;
            java.util.List<UrlParameter> urlParamters = getURLParams(exchange);
            String bodyJson = getBodyJson(exchange);
            java.util.List<MethodesParameter> methodParameters = getMethodParams();
            
            int countParametersReceived = urlParamters.size() + ((containBody) ? 1 : 0);
            if(countParametersReceived == methodParameters.size()){
                int index = 0;
                Object[] objs = new Object[countParametersReceived];
                for(MethodesParameter methodParameter : methodParameters){
                    if(index<urlParamters.size()){
                        UrlParameter urlParamter = urlParamters.get(index);
                        try{
                            objs[index] = getCastedObjectPrimitive(methodParameter.typeName, urlParamter.value);
                        }catch(java.lang.NumberFormatException e){
                            returnError(exchange, 400);
                            return;
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
                                JSON json = JSON.deserialize(Class.forName(methodParameter.typeName/*.replace("[", "").replace("]", "")*/), bodyJson);
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
            try {
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=" + theCharset);
                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }

        /**
         * Renvoie au client un objet
         * @param exchange Correspond à la demande de l'utilisateur
         * @param obj Correspond à l'objet à envoyer à l'utilisateur
         */
        @SuppressWarnings("UseSpecificCatch")
        private void returnObj(com.sun.net.httpserver.HttpExchange exchange, Object obj) {
            try {
                String json = JSON.serialize(obj);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=" + theCharset);
                exchange.sendResponseHeaders(200, json.length());
                exchange.getResponseBody().write(json.getBytes(getCharsetEncoderByte()));
                exchange.close();
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
            try {
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=" + theCharset);
                exchange.sendResponseHeaders(errorCode, 0);
                exchange.getResponseBody().close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        private java.util.List<UrlParameter> getURLParams(com.sun.net.httpserver.HttpExchange exchange) {
            java.util.List<UrlParameter> params = new java.util.ArrayList<>();
            String url = exchange.getRequestURI().getQuery();
            if (url != null) {
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
                    list.add(MethodesParameter.getParameter(method, i));
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
                java.io.InputStream requestBody = exchange.getRequestBody();
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
         * Renvoie l'encoder correspondant à la chaîne encodeuse de la classe serveur
         * @return Retourne l'encodeur correspondant à la chaîne encodeuse de la classe serveur
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

            
            
        }

        /**
         * Cette classe permet de représenter un paramètre de la méthode du point d'entré
         */
        private static class MethodesParameter {

            
            
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

            
            
        //CONSTRUCTOR
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
            public static MethodesParameter getParameter(java.lang.reflect.Method m, int index) {
                
                


                
                java.lang.reflect.Parameter[] ps = m.getParameters();
                java.lang.reflect.Parameter p = ps[index];
                boolean isPri = isPrimitive(p.getType());
                boolean isArr = isArray(p.getType());
                boolean isObj = isObject(p.getType());
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
     * Cette classe permet de créer un objet associateur d'un id de serveur, un serveur, son listener et sa classe API (contenant toutes ses méthodes points d'entrés)
     */
    private static class Mapping{
        
        
        
    //ATTRIBUTS
        /**
         * Correspond à l'id du serveur
         */
        public String id;
        /**
         * Correspond au serveur
         */
        public com.sun.net.httpserver.HttpServer server;
        /**
         * Correspond au listener du serveur
         */
        public IServer listener;
        /**
         * Correspond à la classe API du serveur (contenant toutes ses méthodes points d'entrés)
         */
        public API[] apis;

        
        
    //CONSTRUCTOR
        /**
         * Crée un objet associateur d'un id de serveur, un serveur, son listener et sa classe API (contenant toutes ses méthodes points d'entrés)
         * @param id Correspond à l'id du serveur
         * @param server Correspond au serveur
         * @param listener Correspond au listener du serveur
         * @param apis Correspond à la classe API du serveur (contenant toutes ses méthodes points d'entrés)
         */
        public Mapping(String id, com.sun.net.httpserver.HttpServer server, IServer listener, API[] apis) {
            this.id = id;
            this.server = server;
            this.listener = listener;
            this.apis = apis;
        }
        
        
        
    //METHODE PUBLIC
        /**
         * Détermine si un objet mapping à le même id que celui donné en paramètre
         * @param otherId Correspond à l'autre id qui détermine s'il existe un serveur ayant le même id que celui-ci
         * @return Retourne true si l'objet mapping à le même id que celui donné en paramètre
         */
        public boolean is(String otherId){
            return id.equals(otherId);
        }
        
        
        
    }

    
    
}