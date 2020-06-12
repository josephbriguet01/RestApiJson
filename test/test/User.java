/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package test;

import java.io.Serializable;




/**
 * Cette classe représente un utilisateur
 * @author Briguet
 * @version 1.0
 */
public class User implements Serializable {
    
    
    
//ATTRIBUTS
    /**
     * Correspond à l'id de l'utilisateur
     */
    private final int id;
    /**
     * Correspond au prénom de l'utilisateur
     */
    private final String firstname;
    /**
     * Correspond au nom de l'utilisateur
     */
    private final String lastname;

    
    
//CONSTRUCTOR
    /**
     * Crée un utilisateur
     * @param id Correspond à l'id de l'utilisateur
     * @param firstname Correspond au prénom de l'utilisateur
     * @param lastname Correspond au nom de l'utilisateur
     */
    public User(int id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    
    
//METHODE PUBLIC
    /**
     * Renvoie l'utilisateur sous la forme d'une chaîne de carctères
     * @return Retourne l'utilisateur sous la forme d'une chaîne de carctères
     */
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + '}';
    }
    
    
    
}