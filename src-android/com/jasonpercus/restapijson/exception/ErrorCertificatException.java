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
 * Cette classe permet d'afficher une erreur expliquant qu'il y a une erreur de certificat
 * @author Briguet
 * @version 1.0
 */
public class ErrorCertificatException extends Exception {

    
    
//CONSTRUCTOR
    /**
     * Crée une exception expliquant qu'il y a une erreur de certificat
     * @param message Correspond au message de l'exception
     */
    public ErrorCertificatException(String message) {
        super(message);
    }
    
    
    
}