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
 * Cette classe (lorsqu'elle est étendue) permet de créer un controleur qui sera à l'écoute des requêtes REST
 * 
 * Attention:
 *  - Les méthodes qui contiennent un _ devant le nom de la méthode public n'est pas pris en compte
 *  - De même pour les méthodes qui possèdent des tableaux d'objet non primitifs dans leur signature. Les remplacer par des listes d'objets non primitifs
 *  - De même que les méthodes surchargées ne fonctionne pas, seule une méthode (prise aléatoirement) sera prise en compte. Donc éviter les méthodes de même nom
 *  - De même que les méthodes statics ou abstract ne sont pas prise en compte
 * 
 * @author BRIGUET
 * @version 1.0
 */
public abstract class API {
    
    
    
    /**
     * Renvoie le context du controlleur API. Exemple si le controlleur API se trouve à la page (http://127.0.0.1:8080/Main) le contexte sera (Main). Si le controlleur API se trouve à la page (http://127.0.0.1:443/Main/User) le contexte sera (Main/User).
     * @return Renvoie le context du controleur API
     */
    public String _getControllerApiContext(){
        return getClass().getSimpleName();
    }
    
    
    
}