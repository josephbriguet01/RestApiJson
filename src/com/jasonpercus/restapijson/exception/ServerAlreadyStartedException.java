/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Juin 2020
 */
package com.jasonpercus.restapijson.exception;



/**
 * Cette classe permet d'afficher une erreur de connexion entre un client et un serveur
 * @author Briguet
 * @version 1.0
 */
public class ServerAlreadyStartedException extends RuntimeException {

    
    
//CONSTRUCTOR
    /**
     * Crée une exception expliquant que le serveur a déjà démarré
     * @param message Correspond au message d'erreur
     */
    public ServerAlreadyStartedException(String message) {
        super(message);
    }
    
    
    
}