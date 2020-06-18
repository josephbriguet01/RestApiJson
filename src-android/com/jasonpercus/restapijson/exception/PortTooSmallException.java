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
 * Cette classe permet d'afficher une erreur concernant un port réseau trop petit
 * @author Briguet
 * @version 1.0
 */
public class PortTooSmallException extends InvalidPortException {
    
    
    
//CONSTRUCTOR
    /**
     * Crée une exception expliquant que le port réseau est trop petit
     * @param message Correspond au message d'erreur
     */
    public PortTooSmallException(String message) {
        super(message);
    }
    
    
    
}