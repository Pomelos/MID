TP Java EE 6
============

Ce TP d'une durée de 8h vous permettra de vous familiariser avec le développement et l'intégration d'applications Java EE 6.

Vous utiliserez Glassfish 3.1.1 sur le domaine par défaut *domain1* pour développer, tandis que vos projets seront construits avec Maven.

Un template Maven pour une application (web) Java EE 6 vous est fourni dans le répertoire [Template-JavaEE6-Maven](Template-JavaEE6-Maven).

Le scénario (parfois aussi appellée spécification fonctionnelle)
----------------------------------------------------------------

Vous allez écrire une première application qui sera un *chat board*. On utilise ce type d'application pour d'autres choses que clavarder de la pluie et du beau temps. Des entreprises se basent dessus pour communiquer entre membres d'équipes distribuées. On pensera au vénérables canaux IRC, aux conversations groupées sur Skype ou encore à des solutions web comme [Campfire](http://campfirenow.com/).

Votre splendide *chat board* ne supportera pas de mécanisme d'authentification particulière. La première connexion vous permettra de donner un signalement sur votre nom et email. Ces informations seront placées dans la session web de votre navigateur.

![Chat](Chat.jpg)

Une fois ces informations saisies, vous arriverez sur l'écran principal, à savoir le *chat board* tant attendu :

![Chat board](Chat_board.jpg)

Nous permettrons aux utilisateurs de signaler leur statut afin de notifier les autres participants de leur occupation : "parti faire les courses", "dans la cuisine", "au téléphone", "dans ton cloud", etc.

Ceci se fera en saisissant un message commençant par `/status` comme dans la session suivante :

    Brian > Plop, salut à tous
    Susie > Kikoooooo Brian, ça va ?
    Brian > Ouais LOL
    Brian > Bon c'est pas le tout. See ya.
    Susie > Bye!
    Brian> /status in the kitchen
    Susie > ... je me disais aussi ...
    Carlos > Eh eh eh

Votre application de chat board enverra les changements de statuts sur un canal de diffusion pour notifier les applications intéressées par le statut des participants au *chat board*.

Cela tombe bien, ce sera votre deuxième application : un *status board* qui permet de savoir à quoi vaque chaque participant. Ce type d'application peut se voir déployée sur un téléviseur par exemple.

La forme sera assez libre pour l'interface de cette application qui ou la page sera mise à jour toutes les 10 secondes, soit en AJAX, soit par un simple rafraichissement automatique de page.

![Busy_board](Busy_board.jpg)

Le programme !
--------------

Nous vous proposons un programme avec les étapes suivantes :

* spécifications fonctionelles
* application web de chat
* diffusion de status via JMS
* application web de status.

Vous pourrez ensuite selon le temps disponible approfondir avec :

* améliorations de la présentation
* persistence en bases de données avec JPA
* gestion d'upload de fichiers.

Step 1 : une application web de chat
------------------------------------

Facile !

1. Modéliser un participant par une classe **Participant** dans le package **chatapp.model**. Les valeurs par défaut des noms et emails seront **"N/A"** comme *not available*.

2. Rendre cette classe disponible pour injection de dépendance (`@Named`) et dans un scope session web (`@SessionScoped`). Cela vous permettra de facilement associer une instance de `Participant` à une session web.

3. Modifiez la JSP **index.jsp** pour rediriger de la racine de l'application à l'URL **join**

4. Créer une servlet **chatapp.presentation.JoinServlet** mappée sur l'URL **/join**.

5. Se faire injecter une instance de `chatapp.model.Participant` dans cette servlet.

6. Un HTTP GET sur cette Servlet présente l'écran de saisie nom / email. Un HTTP POST modifie ces valeurs dans l'instance de `chatapp.model.Participant` injectée, puis redirige en HTTP GET sur l'URL *board*.

7. La vue sera une JSP placée dans **WEB-INF/views/join.jsp**. Inspirez-vous de *TaskEE*, l'application du TD, pour sa rédaction et le passage Servlet -> JSP pour le rendu.

8. Créer une classe **chatapp.model.ChatMessage** pour modéliser un message de chat : participant, date et contenu textuel.

9. Créer un composant / EJB pour gérer les messages en mémoire dans une `java.util.LinkedList`. Cet EJB sera *singleton* et placé dans la classe `chatapp.components.ChatBoard`. Vous fournirez un accesseur sur la liste des messages ainsi qu'une méthode d'ajout de `ChatMessage`.

10. Créer une servlet **chatapp.presentation.BoardServlet** sur l'URL **board** et ayant pour JSP de vue **WEB-INF/views/board.jsp**.

11. Injectez via `@Inject` l'instance de `Participant`, et via `@EJB` celle de `ChatBoard`.

12. Répondez à HTTP GET en donnant la liste des messages sur le chatboard via `board.jsp`. Répondez à HTTP POST par l'ajout d'un message, puis la redirection sur HTTP GET.

Step 2 : diffuser les status
----------------------------

Nous allons créer 2 ressources sur notre serveur Glassfish :

* une *connection factory* pour pouvoir se connecter au serveur JMS, et
* une *destination* diffuser les messages.

Nous prendrons un *Topic* et non pas une *Queue* car ce sont des communications *1 à n* que nous supposons.

![Connection Factory](connection-factory.png)

![Destination](destination.png)

Le serveur JMS sera celui embarqué dans Glassfish dans la configuration par défaut de *domain1*. Nous pouvons lui faire un *ping* pour vérifier qu'il fonctionne. Si ce n'est pas le cas secouez sur le champ vos bras en signe de désespoir.

![Ping OpenMQ](ping-openmq.png)

La diffusion de statut depuis votre application nécessite les modifications suivantes.

1. Créer un EJB *stateless* dans la classe **chatapp.components.StatusBroadcaster**.

2. Injectez-lui via `@Resource` le *Topic* et la *Connection Factory**.

3. Ajoutez-lui une méthode publique **postUpdate(String who, String status)** qui envoie un `StringMessage` JMS. Un message sera encodé sous la forme textuelle `WHO(*.*)STATUS` comme dans `Brian(*.*)in the kitchen`.

4. Si vous n'avez pas compris les 2 points précédents, vous auriez sans doute gagné à écouter le cours, voire y assister.

5. Modifiez `BoardServlet` en lui injectant `StatusBroadcaster` avec `@Resource`. Pour chaque message commençant par `/status`, faire appel à ce composant pour poster un changement de statut de l´émetteur.

Step 3 : le status board
------------------------

Faire une deuxième application. Vous devriez à présent savoir comment faire.

Simplement, créez une Servlet `statusapp.presentation.StatusServlet` avec un rendu de vue depuis la JSP `WEB-INF/views/status.jsp`. Cette Servlet ne répond qu'eux HTTP GET.

Elle se rafraichit toutes les 10 secondes. Une façcon simple de procéder consiste à insérer une balise *meta* dans le header HTML :

    <meta http-equiv="refresh" content="10" />

Pour gérer les status, concevez un EJB *singleton* `statusapp.components.Statuses` qui stocke dans une `HashMap` les status sous la forme `NAME is STATUS`, comme dans `Brian is in the kitchen`.

Pour recevoir les messages, concevez un EJB *message-driven* `statusapp.components.StatusChangeReceiver`, mappé sur **jms/ParticipantStatusBroadcast** et qui pour chaque message reçu mets à jour une entrée dans `Statuses`.

Là encore, ne pas hésiter à se référer au cours ...

`StatusServlet` doit à présent lister les status au fur et à mesure que des messages sont reçus.

Step 4 : youpie, youpie
-----------------------

Si les 2 applications fonctionnent de pair, bravo, vous avez gagné. Vous savez écrire une petite application Java EE 6 et l'intégrer avec une autre.

Bravo encore. S'il vous reste du temps sur les 8h, voici une liste de *steps* pour approfondir vos connaissances.

Step 5 : pimp my app
--------------------

Vos applications ne ressemblent probablement à rien. Ce n'est pas grave : [intégrez la CSS Twitter Bootstrap](http://twitter.github.com/bootstrap/) et adaptez votre HTML pour en tirer partie.

En 15 minutes maximum vous devriez avoir quelque chose de plus présentable.

Rendez également le nom de chaque émetteur de message clickable pour envoyer un email. Pour ce faire, utiliser un lien HTML :

    <div class="message">
        <span class="name"><a href="mailto:brian.indakitchen@gmail.com">Brian</a></span>
        <span class="message-text">Kikooo à tous !!! LOL MDR XPTDR</span>
    </div>

Admirez le chef d'oeuvre et soyez fiers de vous.

Plus loin step 1 : de la persistence en base de données
-------------------------------------------------------

Glassfish est livré avec une base de donnée relationelle de très bonne facture : JavaDB, aussi connu sous le nom plus authentique de [Apache Derby](http://db.apache.org/derby/).

Il nous faut créer une base Derby. Nous la nommerons **ChatBoard** :

    # Terminal 1
    $ javadb/bin/startNetworkServer
    
    # Terminal 2
    $ javadb/bin/ij
    ij version 10.6
    ij > connect 'jdbc:derby://localhost:1527/ChatBoard;create=true';
    ij > exit;

Nous pouvons désormais créer un pool de connexions vers la base, puis donner un nom de ressource JDBC :

![Derby1](derby1.png)

![Derby2](derby2.png)

![Derby3](derby3.png)

La création devrait vous donner un message **Ping succeeded**. Si ce n'est pas le cas : vous avez un problème. Lisez le message d'erreur avant de vous rouler par terre en pleurant.

![Derby4](derby4.png)

Transformez `ChatMessage` en *entity bean*. La référence à `Participant` sera transformée en 2 attributs de la classe `ChatMessage`, de sorte que seul `ChatMessage` sera persistée en base de données.

Là encore le cours est votre ami si vous ne savez pas comment faire.

Il vous faut configurer JPA pour disposer d'un *entity manager* :

    # Dans src/main/resources/META-INF/persistence.xml
    
    <?xml version="1.0" encoding="UTF-8"?>
    <persistence xmlns="http://java.sun.com/xml/ns/persistence"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd"
                 version="1.0">
        <persistence-unit name="chatboard-pu">
            <jta-data-source>jdbc/ChatBoard</jta-data-source>
            <class>chatapp.model.ChatMessage</class>
            <properties>
                <property name="eclipselink.ddl-generation" value="create-tables"/>
                <property name="eclipselink.logging.level" value="FINEST"/>
            </properties>
        </persistence-unit>
    </persistence>

Transformer `chatapp.components.ChatBoard` pour ne plus utiliser une liste en mémoire, mais faire appel à un entity manager sur la *persistence unit* `chatboard-pu`. Transformez-le aussi en EJB stateless.

Plus loin step 2 (*aka* mode *warrior*) : upload de fichiers
------------------------------------------------------------

S'il vous reste du temps sur les 8 heures à cette étape alors il reste encore moyen de vous occuper.

Ajoutez la possibili†é de partager un fichier dans le chat.

1. Pour gérer un upload coté Servlet, regardez du coté de `@MultipartConfig
` dans les Servlets 3.

2. Pour gérer du contenu binaire coté base de données, regardez du coté de `@Blob`

Bonne chance :-)
