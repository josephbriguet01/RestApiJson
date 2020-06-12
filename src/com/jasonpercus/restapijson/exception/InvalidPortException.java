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
 * Cette classe permet d'afficher une erreur concernant un port réseau
 * @author Briguet
 * @version 1.0
 */
public abstract class InvalidPortException extends RuntimeException{

    
    
//CONSTRUCTOR
    /**
     * Crée une exception abstraite expliquant qu'il y a une erreur de port
     * @param message Correspond au message d'erreur
     */
    public InvalidPortException(String message) {
        super(message);
    }
    
    
    
}