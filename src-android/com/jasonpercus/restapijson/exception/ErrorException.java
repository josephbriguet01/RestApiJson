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
 * Cette classe permet d'afficher une erreur 400, 404... html
 * @author Briguet
 * @version 1.0
 */
public class ErrorException extends Exception {

    
    
//ATTRIBUT
    /**
     * Correspond au code d'erreur html
     */
    public int error;
    
    
    
//CONSTRUCTOR
    /**
     * Crée une exception
     * @param error Correspond au code d'erreur html (ex: 400, 404...)
     * @param message Correspond au message d'erreur
     */
    public ErrorException(int error, String message) {
        super(message);
        this.error = error;
    }

    
    
//METHODE PUBLIC
    /**
     * Renvoie le code d'erreur html (ex: 400, 404...)
     * @return Retourne le code d'erreur html (ex: 400, 404...)
     */
    public int getError() {
        return error;
    }
    
    
    
}