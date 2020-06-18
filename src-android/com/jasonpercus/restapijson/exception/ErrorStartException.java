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
 * Cette classe permet d'afficher une erreur de démarrage du serveur
 * @author Briguet
 * @version 1.0
 */
public class ErrorStartException extends RuntimeException {

    
    
//CONSTRUCTOR
    /**
     * Crée une exception
     * @param message Correspond au message d'erreur
     */
    public ErrorStartException(String message) {
        super(message);
    }
    
    
    
}