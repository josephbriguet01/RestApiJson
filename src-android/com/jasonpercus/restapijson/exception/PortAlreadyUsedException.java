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
 * Cette classe permet d'afficher une erreur concernant un port réseau déjà utilisé
 * @author Briguet
 * @version 1.0
 */
public class PortAlreadyUsedException extends InvalidPortException {
    
    
    
//CONSTRUCTOR
    /**
     * Crée une exception expliquant que le port réseau déjà utilisé
     * @param message Correspond au message d'erreur
     */
    public PortAlreadyUsedException(String message) {
        super(message);
    }
    
    
    
}