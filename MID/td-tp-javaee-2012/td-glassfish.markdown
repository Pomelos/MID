# TD GlassFish #

L'objectif de ce TD est de vous familiariser avec Java EE 6 à travers un serveur d'application qui implémente cette spécification : [GlassFish](http://glassfish.org/).

## Installation de GlassFish ##

Récupérez la dernière version de *GlassFish (Open Source Distribution)* depuis la [page de téléchargement du projet](http://glassfish.java.net/public/downloadsindex.html). Vous devez récupérer une archive ZIP.

Au vu du volume de l'archive décompressée et de votre tendance naturelle à avoir un répertoire *home* surchargé vous travaillerez dans **/tmp/**. Une autre raison moins sujette à polémiques sont les entrées sorties nécessairement plus rapides sur un disque locales que sur un montage réseau.

Décompressez l'archive dans **/tmp/VOUS/glassfish** en prenant soin de remplacer *VOUS* par quelque chose de plus intelligent. Vous aurez une préférence naturelle pour l'outil en ligne de commande *unzip*.

## Démarrage ##

Déplacez vous dans le répertoire principal de GlassFish et lancez l'environnement par défaut au doux nom de *domain1* :

    bin/asadmin start-domain domain1

Quelles informations importantes voyez-vous sur la console ?

Connectez-vous à [l’interface web de la console d’administration](http://localhost:4848/). 

<span class="label warning">Note</span> : le premier démarrage de la console est un peu long car Glassfish télécharge les modules de l’interface.

Faites le tour des différentes sections de la console d’administration (applications, resources, configuration, ...). Notez la présence du bouton *help* en haut à droite de l’interface.

<span class="label notice">Note</span> : la console web d’administration propose les mêmes fonctionnalité que le script en ligne de commande asadmin.

Que contiennent les répertoires suivants ?

* `bin/`
* `javadb/`
* `mq/`
* `pkg/`
* `glassfish/`
* `glassfish/lib/`
* `glassfish/domains/`
* `glassfish/domains/domain1/`
* `glassfish/domains/domain1/applications/`
* `glassfish/domains/domain1/autodeploy/`
* `glassfish/domains/domain1/config/`
* `glassfish/domains/domain1/lib/`
* `glassfish/domains/domain1/logs/`

Arrêtez votre serveur Glassfish :

    bin/asadmin stop-domain domain1

## Domaines ##

GlassFish est installé par défaut avec un domaine du doux nom de *domain1*. Un domaine est une instance de serveur qui possède sa propre configuration, ses propres logs, ses propres applications déployées, etc. Il est ainsi possible de faire tourner plusieurs instances autonomes de GlassFish sur un même serveur pour peu que les domaines utilisent des ports réseaux différents pour les services qu’ils vont faire tourner (ex : 2 domaines qui chercheraient à lancer leurs consoles d’administration sur le port 4848).

Une utilisation pragmatique des domaines est de pouvoir disposer de configurations différentes selon les usages :

* une configuration de développement, similaire à celle livrée par défaut dans domain1
* une configuration de production, avec authentification sur la console d’administration,  des restrictions de sécurité, un audit permanent des différents services
* une configuration en cluster avec réplication des données de session web
* ... et plus encore.

Pour créer un domaine, nous allons utiliser le script à tout faire **asadmin**. L’aide de asadmin s’invoque comme ceci :

    bin/asadmin help

La commande pour créer un domaine étant create-domain, la liste des options est disponible depuis :

    bin/asadmin help create-domain

1. Créez un domaine nommé **devel**, dont l’administrateur est **toto**, dont le mot de passe est **coin-coin**, et dont la console d’administration est disponible sur le port **14848**.
2. Lancez le domaine, connectez-vous sur la console d’administration.
3. Où sont les fichiers correspondants au domaine ?
4. Arrêtez puis supprimez le domaine avec la commande `delete-domain` de `asadmin`.

## Tiens, une todo-list ... ##

Soyons originaux et utilisons une application de todo-list !

Le projet est fourni dans le répertoire **TaskEE** (*Tasks Enterprise Edition*). Inspectez le contenu de ce projet d'une infinie complexité.

1. Pourquoi le *scope* de la dépendance Maven `javax:javaee-api:6.0` est-il `provided` ?
2. Comment la todo-list est-elle gardée en mémoire ? Quel est l'effet d'un arret du serveur ?
3. Quel sont les effets de `@SessionScoped` et `@Named` sur la classe `TaskList` ?
4. Comment `TaskServlet` fait-elle pour traiter les ajouts / suppressions de taches ? Pour faire le rendu HTML ?
5. Que produit `mvn package` comme artifact à déployer ?

## Déployons, déployons ##

Pour déployer, rien de plus simple :

    asadmin deploy chemin/vers/le/war

Pour voir les applications déployées :

    asadmin list-applications

Pour dé-déployer :

    asadmin undeploy taskee-1.0-SNAPSHOT

0. Ouvrez une console ou onglet de console dédiée au suivi des logs. Pour cela vous ferez un `tail -f chemin/vers/le/fichier/qui/va/bien.log`
1. Faites-le. Avec le plus grand sérieux. Et regardez les logs aussi.
2. Cherchez comment faire les opérations équivalentes depuis l'interface d'administration.
3. Faites-le aussi.
4. Accédez et essayez l'application.
5. Ouvrez un autre navigateur que celui que vous avez sous les yeux et connectez-vous à l'application. Que se passe-t-il comme prévu ?

## Cluster ##

GlassFish supporte la gestion de clusters. Nous allons créer un cluster avec une instance locale, une instance sur une machine distante, et enfin déployer TaskEE sur le cluster.

### Mise en place ###

Créer un cluster : 

    asadmin create-cluster cluster
    asadmin list-clusters
    asadmin list-instances

À ce stade le cluster existe, mais il n'a pas d'instances.

Commençons par une instance locale :

    asadmin create-local-instance --cluster cluster local-instance-1
    asadmin list-instances -l
    asadmin start-cluster cluster
    asadmin list-instances -l
    asadmin list-nodes -l

Vous devez avoir 1 cluster avec 1 instance sur votre machine. Notez le numéro de port HTTP de votre instance.

Ajoutez une deuxième instance locale nommée **local-instance-2** et assurez-vous de son bon fonctionnement.

Vous pouvez jouer à démarrer / arreter les instances :

    asadmin start-instance nom
    asadmin stop-instance nom
    asadmin list-instances -l
    (...)

### Déployer TaskEE ###

Pour déployer TaskEE sur le cluster, rien de plus simple :

    asadmin deploy --target cluster chemin/qui/va/bien.war
    
Vérifiez que l'application est bien disponible sur chaque instance.

On peut aussi vérifier le déploiement sur le cluster :

    asadmin list-applications cluster

Faites un `undeploy` de TaskEE du cluster. Vérifier.

### Ajouter un noeud distant ###

<span class="label notice">Trop bien !</span> GlassFish est capable de s'installer sur une machine distante pourvu que OpenSSH et Java soient disponibles.

Les connexions SSH peuvent se faire sans mot de passe si vous possédez un jeu de clés public / privées. La procédure est largement documentée, mais GlassFish peut simplifier le travail pour vous.

Prenez l'adresse IP de la machine voisine. Vous allez l'utiliser comme machine distante.

    asadmin setup-ssh --sshuser votre-login adresse-ip

Ceci va générer une clé SSH au besoin, et l'envoyer sur la machine distante. Vous pouvez vérifier que vous vous connectez sans mot de passe :

    ssh votre-login@adresse-ip
    exit

Si cela ne marche pas vous devrez juste saisir votre mot de passe au besoin. On a connu des situations plus critiques.

Demandons à GlassFish de s'installer dans un répertoire de `/tmp/` sur la machine distante :

    asadmin install-node --installdir /tmp/mon-login-INVADERS/glassfish adresse-ip
    
Admirez le travail.

Il vous faut désormais enregistrer cette installation distante en tant que *noeud* GlassFish. La commande ressemble à celle-ci :

    asadmin create-node-ssh --nodehost adresse-ip --installdir /tmp/mon-login-INVADERS/glassfish plop

Vous remplacerez `plop` par un nom à donner au noeud, sauf si vous etes un(e) fan absolue de `plop`.

Il ne vous reste plus qu'à créer une instance distante sur ce noeud, le tout par SSH :

    asadmin create-instance --cluster cluster --node plop plop-instance-1

Par défaut l'instance doit etre off. Vérifiez, démarrez-là :

    asadmin list-instances -l
    asadmin start-instance plop-instance-1
    (...)

Le tout est piloté par SSH depuis votre machine ...

### Déployons de nouveau ###

Déployez TaskEE sur le cluster. Vérifiez que l'application est déployée sur toutes les instances. Testez là.

Les todo-lists sont-elles synchronisées ? La réponse devrait etre non.

### Réplication de session + failover ###

Il faut toucher un peu la configuration pour que la réplication de session fonctionne.

    asadmin set my-first-cluster-config.availability-service.availability-enabled=true

Il faut ensuite re-déployer l'application avec le paramètre `--availabilityenabled=true` en supplément.

Les todo-lists deviennent-elles synchronisées ?

Jouez à arreter / démarrer des instances pour voir ce qui se passe.

## Frontal HTTP (avancé) ##

Dans la réalité vraie, on déploie un serveur HTTP dit frontal. Il dispatche ensuite aux instances du cluster.

Configurez Apache avec `mod_proxy` et `mod_proxy_balancer` pour distribuer les requetes aux instances du cluster.
