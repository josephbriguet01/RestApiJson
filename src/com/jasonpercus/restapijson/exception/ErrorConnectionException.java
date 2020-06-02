/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package com.jasonpercus.restapijson.exception;



/**
 * Cette classe permet d'afficher une erreur de connexion entre un client et un serveur
 * @author Briguet
 * @version 1.0
 */
public class ErrorConnectionException extends Exception {

    
    
//CONSTRUCTOR
    /**
     * Crée une exception
     * @param message Correspond au message de l'erreur
     */
    public ErrorConnectionException(String message) {
        super(message);
    }
    
    
    
}