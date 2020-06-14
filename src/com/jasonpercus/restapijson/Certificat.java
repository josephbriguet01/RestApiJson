/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Juin 2020
 */
package com.jasonpercus.restapijson;



import com.jasonpercus.encryption.Cipher;
import com.jasonpercus.encryption.Key;
import com.jasonpercus.encryption.rsa.KeyPublicRSA;
import com.jasonpercus.encryption.rsa.RSA;



/**
 * Cette classe permet au client et au serveur de communiquer de manière chiffrée
 * @author Briguet
 * @version 1.0
 */
public final class Certificat implements java.io.Serializable {
    
    
    
//ATTRIBUTS
    /**
     * Cette variable permet de verouiller l'utilisation du certificat lors de l'utilisation multithread
     */
    private final transient java.util.concurrent.locks.Lock verrou = new java.util.concurrent.locks.ReentrantLock();
    
    /**
     * Cette variable contient la clef de (dé)chiffrement client/server
     */
    private transient Key key;
    
    /**
     * Correspond à la date de création du certificat
     */
    private java.util.Date createDate;
    
    /**
     * Correspond à la date d'expiration du certificat
     */
    private java.util.Date expirationDate;
    
    /**
     * Correspond à la clef de (dé)chiffrement client/server (lors de la création du certificat, cette variable contient la clef en clair. A chaque envoi du certificat au client, le certificat est clonné, puis cette variable est chiffrée)
     */
    private String encryptedKey;
    
    /**
     * Correspond au nom de la classe du moteur de (dé)chiffrement
     */
    private String nameCipher;
    
    /**
     * Correspond au nom de la classe de la clef de (dé)chiffrement
     */
    private String nameKeyCipher;
    
    /**
     * Correspond à un identifiant unique que chaque client doit présenter pour signaler que leur certificat est valide. Si le hash ne correspond pas, c'est que le serveur a changé de certificat.
     */
    private String hash;

    
    
//CONSTRUCTORS
    /**
     * Crée un certificat vide
     */
    private Certificat() {
    }

    /**
     * Crée un certificat
     * @param cipher Correspond à la classe motrice de (dé)chiffrement
     * @param keyCipher Correspond à la classe de la clef de (dé)chiffrement
     * @param timeoutGenerateCertificat Correspond au temps de validité du certificat en seconde
     * @throws NoSuchMethodException Lorsqu'une méthode particulière est introuvable
     * @throws InstantiationException Lorsqu'une application tente de créer une instance d'une classe à l'aide de la méthode newInstance() dans la classe Class, mais que l'objet de classe spécifié ne peut pas être instancié
     * @throws IllegalAccessException Lorsqu'une application tente de créer de manière réfléchie une instance (autre qu'un tableau), de définir ou d'obtenir un champ, ou d'appeler une méthode, mais que la méthode en cours d'exécution n'a pas accès à la définition de la classe, du champ, méthode ou constructeur
     * @throws IllegalArgumentException Lorsqu'une méthode a subi un argument illégal ou inapproprié
     * @throws java.lang.reflect.InvocationTargetException InvocationTargetException est une exception vérifiée qui encapsule une exception levée par une méthode ou un constructeur invoqué
     */
    protected Certificat(Class cipher, Class keyCipher, int timeoutGenerateCertificat) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, java.lang.reflect.InvocationTargetException{
        generateCertificat(cipher, keyCipher, timeoutGenerateCertificat);
    }
    
    
    
//METHODES PROTECTEDS
    /**
     * Modifie le certificat actuel en regénérant les valeurs des variables
     * @param cipher Correspond à la classe motrice de (dé)chiffrement
     * @param keyCipher Correspond à la classe de la clef de (dé)chiffrement
     * @param timeoutGenerateCertificat Correspond au temps de validité du certificat en seconde
     * @throws NoSuchMethodException Lorsqu'une méthode particulière est introuvable
     * @throws InstantiationException Lorsqu'une application tente de créer une instance d'une classe à l'aide de la méthode newInstance() dans la classe Class, mais que l'objet de classe spécifié ne peut pas être instancié
     * @throws IllegalAccessException Lorsqu'une application tente de créer de manière réfléchie une instance (autre qu'un tableau), de définir ou d'obtenir un champ, ou d'appeler une méthode, mais que la méthode en cours d'exécution n'a pas accès à la définition de la classe, du champ, méthode ou constructeur
     * @throws IllegalArgumentException Lorsqu'une méthode a subi un argument illégal ou inapproprié
     * @throws java.lang.reflect.InvocationTargetException InvocationTargetException est une exception vérifiée qui encapsule une exception levée par une méthode ou un constructeur invoqué
     */
    protected void generateCertificat(Class cipher, Class keyCipher, int timeoutGenerateCertificat) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, java.lang.reflect.InvocationTargetException{
        verrou.lock();
        try {
            java.util.Date creationDate = new java.util.Date();
            key = ((Cipher) cipher.getConstructor().newInstance()).generateKey(16);
            this.createDate = creationDate;
            this.expirationDate = addSecondesToDate(timeoutGenerateCertificat, creationDate);
            this.encryptedKey = key.toString();
            this.nameCipher = cipher.getCanonicalName();
            this.nameKeyCipher = keyCipher.getCanonicalName();
            String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String chain = "";
            for (int i = 0; i < 128; i++) {
                chain += base.charAt((int) (Math.random() * base.length()));
            }
            this.hash = chain;
        } finally {
            verrou.unlock();
        }
    }

    /**
     * Renvoie la date de création du certificat
     * @return Retourne la date de création du certificat
     */
    protected java.util.Date getCreateDate() {
        return createDate;
    }

    /**
     * Renvoie la date d'expiration du certificat
     * @return Retourne la date d'expiration du certificat
     */
    protected java.util.Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Renvoie la clef de (dé)chiffrement (en vision clair à la création du certificat, ou chiffré lors d'un clonage du certificat)
     * @return Retourne la clef de (dé)chiffrement (en vision clair à la création du certificat, ou chiffré lors d'un clonage du certificat)
     */
    protected String getEncryptedKey() {
        return encryptedKey;
    }

    /**
     * Renvoie le nom complet de la classe motice de (dé)chiffrement
     * @return Retourne le nom complet de la classe motice de (dé)chiffrement
     */
    protected String getNameCipher() {
        return nameCipher;
    }

    /**
     * Renvoie le nom complet de la classe de la clef de (dé)chiffrement
     * @return Retourne le nom complet de la classe de la clef de (dé)chiffrement
     */
    protected String getNameKeyCipher() {
        return nameKeyCipher;
    }

    /**
     * Renvoie le hash du certificat
     * @return Retourne le hash du certificat
     */
    protected String getHash() {
        return hash;
    }

    /**
     * Renvoie la clef de (dé)chiffrement utilisée pour les communications clientes/serveurs
     * @return Retourne la clef de (dé)chiffrement utilisée pour les communications clientes/serveurs
     */
    protected synchronized Key getKey() {
        return key;
    }
    
    /**
     * Modifie la clef de (dé)chiffrement utilisée pour les communications clientes/serveurs
     * @param key Correspond à la nouvelle clef de (dé)chiffrement
     */
    protected synchronized void setKey(Key key){
        this.key = key;
    }

    /**
     * Clone le certificat et chiffre sa clef de (dé)chiffrement
     * @param key Correspond à la clef RSA qui va chiffrer la clef utilisée pour les communications clientes/serveurs
     * @return Retourne un certificat chiffré prêt à l'envoi au client
     */
    protected Certificat clone(KeyPublicRSA key) {
        Certificat c = new Certificat();
        c.setCreateDate(createDate);
        c.setExpirationDate(expirationDate);
        c.setEncryptedKey(new RSA().encrypt(key, encryptedKey));
        c.setNameCipher(nameCipher);
        c.setNameKeyCipher(nameKeyCipher);
        c.setHash(hash);
        return c;
    }
    
    /**
     * Renvoie si oui ou non le certificat est expiré
     * @return Retourne true s'il l'est, sinon false
     */
    protected boolean isExpired(){
        return (getExpirationDate().getTime() <= new java.util.Date().getTime());
    }

    
    
//METHODE PUBLIC
    /**
     * Renvoie le certificat sous la forme d'une chaîne de caractères
     * @return Retourne le certificat sous la forme d'une chaîne de caractères
     */
    @Override
    public String toString() {
        return "Certificat{" + "createDate=" + createDate + ", expirationDate=" + expirationDate + ", nameCipher=" + nameCipher + '}';
    }

    
    
//METHODES PRIVATES
    /**
     * Modifie la date de création du certificat
     * @param createDate Correspond à la nouvelle date de création
     */
    private void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Modifie la date d'expiration du certificat
     * @param expirationDate Correspond à la nouvelle date d'expiration
     */
    private void setExpirationDate(java.util.Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Modifie la clef de (dé)chiffrement
     * @param encryptedKey Correspond à la nouvelle clef
     */
    private void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    /**
     * Modifie le nom complet de la classe motrice de (dé)chiffrement
     * @param nameCipher Correspond au nouveau nom complet
     */
    private void setNameCipher(String nameCipher) {
        this.nameCipher = nameCipher;
    }

    /**
     * Modifie le nom complet de la clef de (dé)chiffrement
     * @param nameKeyCipher Correspond au nouveau nom complet
     */
    private void setNameKeyCipher(String nameKeyCipher) {
        this.nameKeyCipher = nameKeyCipher;
    }

    /**
     * Modifie le hash du certificat
     * @param hash Correspond au nouveau hash
     */
    private void setHash(String hash) {
        this.hash = hash;
    }
    
    
    
//METHODE PRIVATE STATIC
    /**
     *  Convenience method to add a specified number of minutes to a Date object
     *  From: http://stackoverflow.com/questions/9043981/how-to-add-minutes-to-my-date
     *  @param  seconds  The number of minutes to add
     *  @param  beforeTime  The time that will have minutes added to it
     *  @return  A date object with the specified number of minutes added to it 
     */
    private static java.util.Date addSecondesToDate(int seconds, java.util.Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 1000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        java.util.Date afterAddingMins = new java.util.Date(curTimeInMs + (seconds * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }
    
    
    
}