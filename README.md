﻿﻿Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
# RestApiJson

### Introduction
RestApiJson est une librairie qui a pour but de simplifier la vie du développeur lorsqu'il s'agit de créer un serveur API Json. Il suffit que le client envoie une requête au serveur, et celui-ci appelera automatiquement la méthode qui correspond à la requête. Ainsi, il aura une réponse à renvoyer au client. 
Il y a donc 3 grandes classes à retenir:
* Server -> *Qui écoute les requêtes clientes*
* Client -> *Qui envoie des requêtes au serveur*
* API -> *Qui exécute les requêtes clientes et que le serveur s'occupera de renvoyer au client*

### Création d'une classe API
La classe API doit ***obligatoirement*** étendre `com.jasonpercus.restapijson.API`. Voici un exemple:
```java
public class UserAPI extends API {
    
    //Exemple d'appel: http://[ip_server]:[port_server]/UserAPI/getAll
    public List<User> getAll(){
        ...
    }
    
    //Exemple d'appel: http://[ip_server]:[port_server]/UserAPI/get?index=0
    public User get(int index){
        ...
    }
    
    //Exemple d'appel: http://[ip_server]:[port_server]/UserAPI
    //Ajout du User dans le corps de la requête cliente
    public void add(User user){
        ...
    }
    
    //Exemple d'appel: http://[ip_server]:[port_server]/UserAPI
    //Ajout du User dans le corps de la requête cliente
    public void remove(User user){
        
    }
    
    //Exemple d'appel: http://[ip_server]:[port_server]/UserAPI?index=1
    //Ajout du User dans le corps de la requête
    public void set(int index, User user){
        ...
    }
    
}
```
Donc, dans l'exemple ci-dessus, la classe UserAPI correspond à la classe API (voir introduction).

**Attention**, les méthodes `abstract`, `static`, `private` et `protected` ne seront pas lu par le serveur. De même pour les méthodes commençant par un underscore `_` (exemple: `public int _getAge()`).

**Attention**, les paramètres primitifs doivent toujours passer en premier. Donc la méthode `public void set(User user, int index){...}` ne fonctionne pas. `User` et `int` doivent être inversé. Voir la liste des paramètres primitifs plus bas.

**Attention**, le type primitif `char`en java ne peut être utilisé dans les signatures de méthodes

Si l'on ne souhaite pas que le nom de la classe API fasse office de nom d'API et soit utilisé dans l'URL de la requête cliente, il suffit de redéfinir la méthode `_getControllerApiContext()`. Exemple ci-dessous:

```java
public class UserAPI extends API {
    
    @Override
    public String _getControllerApiContext() {
        return "MyAPI";
    }
    
    //Exemple d'appel: http://[ip_server]:[port_server]/MyAPI/getAll
    public List<User> getAll(){
        ...
    }
    
    //On constate dans l'exemple d'appel UserAPI a été remplacé par MyAPI.
    //Car la méthode _getControllerApiContext a été redéfinie 
    
}
```

La structure d'appel distante (d'un client) d'une méthode d'API est la suivante: `http://` `ip_serveur` `:` `port_serveur` `/` `nom_classe_api` `/` `nom_methode_api` + `?argument...`

### Création et démarrage d'un serveur
`MyServer` = à l'id du serveur (voir plus bas)
`8080` = au port découte du serveur (par défaut il s'agit de 8080)
`this` = correspond au listener qui sera utilisé lorsque le serveur a démarré ou arrêté
`new UserAPI()` = à l'API qui va exécuter les requêtes clientes

```java
Server.start("MyServer", 8080, this, new UserAPI());
```

Il est possible de lancer plusieurs serveurs (sur des ports d'écoutes différents évidemment), d'où l'intérêt d'avoir un id unique par serveur. Si celui-ci n'est pas communiqué, un id par défaut sera créé automatiquement.

### Arrêt d'un ou de plusieurs serveurs
```java
Server.stop("MyServer");
```

Il est possible de stopper tous les serveurs en activité avec:
```java
Server.stop();
```

### Connaître l'état d'un serveur
Pour déterminer si un serveur est lancé:
```java
boolean started = Server.isStart("MyServer");
```

Pour déterminer si tous les serveurs sont lancés:
```java
boolean started = Server.isStart();
```
Si un seul serveur n'est pas lancé, alors `isStart()` renverra `false`.

Il est également possible de connaître l'état d'un serveur en temps réel, graçe aux évènements (d'où l'intérêt d'un listener. Voir plus haut). Pour cela il suffit d'implémenter la méthode `IServer`.

A chaque démarrage de serveur, il suffit de donner en paramètre le listener. Et celui-ci sera en temps réel mis au courant du démarrage et de l'arrêt du serveur.

### Création d'un client
Il est facile de créer un client. Car il y a seulement deux méthodes principales.

```java
//Création d'un objet
User user = ...;

//Appelle la requête de récupération de tous les utilisateurs. Voir API définie plus haut
List<User> users = Client.sendRequest("http://192.168.1.1:8080/UserAPI/getAll");

//Appelle la requête d'ajout d'un user. Voir API définie plus haut
Client.sendRequest("http://192.168.1.1:8080/UserAPI/add", user);
```

Evidemment la requête `Client.sendRequest("http://.../getAll")` et `Client.sendRequest("http://.../add", user)` renvoie toujours un objet ou null si le serveur n'a rien à renvoyer comme dans le cas de `.../add`.

Il est possible de recevoir une erreur lors de l'envoie d'une requête. Ce peut être dû au fait que le chemin dans le serveur n'existe pas. Exemple: `Client.sendRequest("http://192.168.1.1:8080/MyNewAPI/getAll")` renvoie une erreur `ErrorException` avec le code `404`. Si la requête n'est pas comprise du serveur alors le code sera `400`. Si le serveur n'a pas été trouvé lors de l'envoi de la requête cliente, alors l'erreur sera du type `ErrorConnectionException`. Et si le serveur ne peut démarrer (par exemple parce que le port TCP est déjà utilisé), alors l'erreur sera du type `ErrorStartException`.

### Types primitifs
Ils sont quasiment identiques de ceux utilisés en java, mais avec quelques différences. En voici la liste:
* byte
* short
* int
* long
* float
* double
* boolean
* String

**Le type `char` n'existe pas.**
**En revanche, le type `String` existe.**

### Bibliothèques
Pour que le projet `RestApiJson` fonctionne, deux bibliothèques sont nécessaires:
* [gson-2.8.2.jar](https://jar-download.com/artifacts/com.google.code.gson/gson/2.8.2/source-code "gson-2.8.2.jar")
* [JSON.jar](https://github.com/josephbriguet01/JSON/tree/master/dist "JSON.jar")

## Accès au projet GitHub => [ici](https://github.com/josephbriguet01/RestApiJson "Accès au projet Git RestApiJson")