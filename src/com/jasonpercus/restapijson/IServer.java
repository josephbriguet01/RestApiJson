/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package com.jasonpercus.restapijson;



/**
 * Cette interface permet à un objet d'être à l'écoute des évènement du serveur
 * @author Briguet
 * @version 1.0
 */
public interface IServer {
    
    
    
    /**
     * Lorsque le serveur API s'est bien lancé
     * @param id Correspond à l'id du serveur lancé
     */
    public void serverRestIsStarted(String id);
    
    /**
     * Lorsque le serveur API s'est arrêté
     * @param id Correspond à l'id du serveur arrêté
     */
    public void serverRestIsStopped(String id);
    
    
    
}