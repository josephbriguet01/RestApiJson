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
 * Cette classe permet d'afficher une erreur expliquant qu'aucune api n'a été donnée au serveur
 * @author Briguet
 * @version 1.0
 */
public class ApiNotDefinedException extends RuntimeException {

    
    
//CONSTRUCTOR
    /**
     * Crée une exception expliquant qu'aucune api n'a été donnée au serveur
     * @param message Correspond au message de l'exception
     */
    public ApiNotDefinedException(String message) {
        super(message);
    }
    
    
    
}