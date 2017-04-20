package fr.quintipio.javautils.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Méthode utilitaire pour les collections
 * @author Quentin
 */
public final class CollectionUtils {

    private CollectionUtils(){}
    
    /**
     * Ajoute le contenu de la liste à inserer dans la liste destination.
     *
     * @param <T>
     *            le type.
     * @param listeDestination
     *            la liste destination
     * @param listeAInserer
     *            la liste a inserer
     * @return the list<T>
     */
    public static <T> List<T> addAll(List<T> listeDestination, List<T> listeAInserer) { // NO_UCD (unused code)
        if (listeDestination == null && listeAInserer == null) {
            return new ArrayList<T>();
        }
        if (listeDestination == null && listeAInserer != null) {
            return new ArrayList<T>(listeAInserer);
        }
        if (listeDestination != null && listeAInserer == null) {
            return new ArrayList<T>(listeDestination);
        }
        final List<T> listeRetour = new ArrayList<T>(listeDestination);
        listeRetour.addAll(listeAInserer);
        return listeRetour;
    }

    /**
     * Ajoute un objet dans une map avec plusieurs clés.
     *
     * @param <T>
     *            type des objets de la clé
     * @param map
     *            map dans laquelle il faut ajouter des données
     * @param objets
     *            données à ajouter
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> void addInListMap(Map map, T... objets) { // NO_UCD (unused code)
        Map mapBoucle = map;
        final int nbObjets = objets.length;

        for (int i = 0; i < nbObjets - 2; i++) {
            Map map2 = (Map) mapBoucle.get(objets[i]);
            if (map2 == null) {
                map2 = new HashMap();
                mapBoucle.put(objets[i], map2);
            }
            mapBoucle = map2;
        }

        final T cleResultat = objets[nbObjets - 2];
        List listeResultats = (List) mapBoucle.get(cleResultat);
        if (listeResultats == null) {
            listeResultats = new ArrayList();
            mapBoucle.put(cleResultat, listeResultats);
        }
        listeResultats.add(objets[nbObjets - 1]);
    }

    /**
     * Ajoute un objet dans une map avec plusieurs clés.
     *
     * @param <T>
     *            type des objets de la clé
     * @param map
     *            map dans laquelle il faut ajouter des données
     * @param objets
     *            données à ajouter
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> void addInMap(Map map, T... objets) { // NO_UCD (unused code)
        Map mapBoucle = map;
        final int nbObjets = objets.length;

        for (int i = 0; i < nbObjets - 2; i++) {
            Map map2 = (Map) mapBoucle.get(objets[i]);
            if (map2 == null) {
                map2 = new HashMap();
                mapBoucle.put(objets[i], map2);
            }
            mapBoucle = map2;
        }

        mapBoucle.put(objets[nbObjets - 2], objets[nbObjets - 1]);
    }

    /**
     * Ajoute la valeur à la liste des enregistrements classés à la clé dans la map.
     *
     * @param <K>
     *            type de la cle
     * @param <V>
     *            Type des valeurs
     * @param map
     *            map contenant les infos
     * @param cle
     *            cle de la valeur a ajouter
     * @param valeur
     *            valeur a ajouter
     */
    public static <K, V> void ajouteValeurDansListeMap(Map<K, List<V>> map, K cle, V valeur) { // NO_UCD (unused code)
        List<V> listePourCle = map.get(cle);
        if (listePourCle == null) {
            listePourCle = new ArrayList<V>();
            map.put(cle, listePourCle);
        }
        listePourCle.add(valeur);
    }

    /**
     * Ajoute la valeur à un enregistrement dans la map.
     *
     * @param map
     *            contenant les infos
     * @param cle
     *            de la valeur a ajouter
     * @param valeur
     *            a ajouter
     */
    // NO_UCD (unused code)
    public static void ajouteValeurDansMap(Map<String, Integer> map, final String cle, final Integer valeur) { // NO_UCD (unused code)
        final Integer tmp = map.get(cle);
        if (tmp == null) {
            map.put(cle, valeur);
        }
        else {
            map.remove(cle);
            map.put(cle, tmp + valeur);
        }
    }

    /**
     * clone un objet itérable de tout type ({@link Map}, {@link Collection}, Object[]) en profondeur.
     *
     * @param <T>
     *            le type de retour.
     * @param iterateObject
     *            l'objet itérable à cloner.
     * @return l'objet itérable cloné.
     */
    @SuppressWarnings("unchecked")
    public static <T> T cloneCollection(T iterateObject) {
        try {
            if (iterateObject == null) {
                return null;
            }
            else if (iterateObject.getClass().isArray()) {
                // clonage du tableau.
                return (T) cloneTableau((Object[]) iterateObject);
            }
            else if (iterateObject instanceof Collection) {
                // clonage de la collection.
                return (T) cloneCollectionPrivate((Collection<?>) iterateObject);
            }
            else if (iterateObject instanceof Map) {
                // clonage de la map.
                return (T) cloneMap((Map<?, ?>) iterateObject);
            }
            else {
                throw new CloneNotSupportedException("L'objet passé en paramètre n'est pas un objet itérable.");
            }
        }
        catch (final Exception e) {
            throw new RuntimeException("Erreur durant l'opération de clonage, " + "vérifiez que tout les objets disposent d'un constructeur par défaut " + ": " + e);
        }
    }

    /**
     * clone une {@link Collection} en profondeur.
     *
     * @param <T>
     *            le type de retour.
     * @param collectionAcloner
     *            la collection à cloner.
     * @return la collection clonée.
     * @throws Exception
     *             l'exception possible durant le clonage.
     */
    @SuppressWarnings("unchecked")
    private static <T> T cloneCollectionPrivate(Collection<T> collectionAcloner) throws Exception {
        // invocation de la méthode clone de cette collection (on la clone naturellement)
        final Collection<T> collectionClone = (Collection<T>) invokeClone(collectionAcloner);

        // si la collection contient des elements
        if (!collectionAcloner.isEmpty()) {
            // on vide la collection puisqu'on va la remplir avec des références clonées si les objets sont non immuables
            collectionClone.clear();

            // Création d'un iterateur sur la collection afin de cloner les
            // elements complexes de la collection
            final Iterator<T> it = collectionAcloner.iterator();

            // parcours des éléments
            while (it.hasNext()) {
                final Object objetAcloner = it.next();

                // vérification de la nature des objets contenus dans la collection
                if (ObjectUtils.isDeepClonableObject(objetAcloner)) {
                    try {
                        // ajout de l'élément cloné par l'intermédaire de la méthode
                        // clone de l'objet à la collection clonée.
                        collectionClone.add((T) invokeClone(objetAcloner));
                    }
                    catch (final NoSuchMethodException e) {
                        // si la méthode clone n'existe pas on utilise alors ObjectUtils.clone();
                        collectionClone.add((T) ObjectUtils.clone(objetAcloner));
                    }
                }
                else if (ObjectUtils.isIterableObject(objetAcloner)) {
                    // l'objet est itérable
                    collectionClone.add((T) cloneCollection(objetAcloner));
                }
                else {
                    // l'objet doit être immuable
                    collectionClone.add((T) objetAcloner);
                }
            }
        }
        return (T) collectionClone;
    }

    /**
     * clone une {@link Map} en profondeur.
     *
     * @param <T>
     *            le type de retour.
     * @param <U>
     *            the generic type
     * @param mapAcloner
     *            la map à cloner.
     * @return la map clonée.
     * @throws Exception
     *             l'exception possible durant le clonage.
     */
    @SuppressWarnings("unchecked")
    private static <T, U> Map<T, U> cloneMap(Map<T, U> mapAcloner) throws Exception {
        // invocation de la méthode clone de cette map (on la clone naturellement)
        final Map<T, U> mapClone = (Map<T, U>) invokeClone(mapAcloner);

        // si la map contient des elements
        if (!mapAcloner.isEmpty()) {
            // on vide la map puisqu'on va la remplir avec des références clonées si les objets sont non immuables
            mapClone.clear();

            // Création d'un iterateur sur les clefs de la map afin de cloner les
            // elements
            final Iterator<T> it = mapAcloner.keySet().iterator();

            // parcours des éléments
            while (it.hasNext()) {
                final Object key = it.next();
                final Object objetAcloner = mapAcloner.get(key);

                // vérification de la nature des objets contenus dans la map
                if (ObjectUtils.isDeepClonableObject(objetAcloner)) {
                    try {
                        // ajout de l'élément cloné par l'intermédaire de la méthode
                        // clone de l'objet à la map clonée.
                        mapClone.put((T) key, (U) invokeClone(objetAcloner));
                    }
                    catch (final NoSuchMethodException e) {
                        // si la méthode clone n'existe pas on utilise alors ObjectUtils.clone();
                        mapClone.put((T) key, (U) ObjectUtils.clone(objetAcloner));
                    }
                }
                else if (ObjectUtils.isIterableObject(objetAcloner)) {
                    // l'objet est itérable
                    mapClone.put((T) key, (U) cloneCollection(objetAcloner));
                }
                else {
                    // l'objet doit être immuable
                    mapClone.put((T) key, (U) objetAcloner);
                }
            }
        }
        return mapClone;
    }

    /**
     * clone un Tableau d'objet en profondeur.
     *
     * @param <T>
     *            le type de retour.
     * @param <U>
     *            le type des elements du tableau.
     * @param tableauAcloner
     *            la tableau à cloner.
     * @return le tableau cloné.
     * @throws Exception
     *             l'exception possible durant le clonage.
     */
    @SuppressWarnings("unchecked")
    private static <T, U> T cloneTableau(U[] tableauAcloner) throws Exception {
        // clone naturellement le tableau
        if (tableauAcloner == null) {
            return null;
        }
        return (T) cloneCollection(CollectionUtils.creerList(tableauAcloner)).toArray(tableauAcloner.clone());
    }

    /**
     * Concatène tous les éléments d'une collection en une chaîne de caractères séparés par un espace.
     *
     * @param liste
     *            Liste à formatter
     * @return Chaîne de caractères non nulle
     */
    public static String collectionToString(Collection<String> liste) { // NO_UCD (unused code)
        return collectionToString(liste, " ", Boolean.TRUE);
    }

    /**
     * Concatène tous les éléments d'une collection en une chaîne de caractères séparés par un espace.
     *
     * @param liste
     *            Liste à formatter
     * @param withNull
     *            Conserve ou non les valeurs nulles
     * @return Chaîne de caractères non nulle
     */
    public static String collectionToString(Collection<String> liste, Boolean withNull) { // NO_UCD (unused code)
        return collectionToString(liste, " ", withNull);
    }

    /**
     * Concatène tous les éléments d'une collection en une chaîne de caractères séparés par un espace.
     *
     * @param liste
     *            Liste à formatter
     * @param separateur
     *            le séparateur des éléments de la liste
     * @return Chaîne de caractères non nulle
     */
    public static String collectionToString(Collection<String> liste, String separateur) { // NO_UCD (unused code)
        return collectionToString(liste, separateur, Boolean.TRUE);
    }

    /**
     * Concatène tous les éléments d'une collection en une chaîne de caractères séparés par un séparateur désiré.
     *
     * @param liste
     *            Liste à formatter
     * @param separateur
     *            le séparateur des éléments de la liste
     * @param withNull
     *            Conserve ou non les valeurs nulles
     * @return Chaîne de caractères non nulle
     */
    public static String collectionToString(Collection<String> liste, String separateur, Boolean withNull) { // NO_UCD (use private)
        final String sep = ObjectUtils.defaultIfNull(separateur, " ");
        final StringBuilder stringBuilder = new StringBuilder("");
        if (!isEmpty(liste)) {
            for (final String string : liste) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(sep);
                }
                stringBuilder.append(!StringUtils.isEmpty(string) ? string : (withNull ? " null " : " "));
            }
        }
        return stringBuilder.toString().trim();

    }

    /**
     * Création d'une List à partir d'une collection.
     *
     * @param <T>
     *            type des éléments de la liste
     * @param col
     *            collection contenant les éléments à ajouter dans la liste
     * @return La liste avec les éléments de la colllection
     */
    public static <T> List<T> creerList(Collection<T> col) {
        if (CollectionUtils.isEmpty(col)) {
            return new ArrayList<T>();
        }
        else {
            return new ArrayList<T>(col);
        }
    }

    /**
     * Création d'une List à partir d'un tableau.
     *
     * @param <T>
     *            type des éléments de la liste
     * @param objets
     *            éléments à ajouter dans la liste
     * @return La liste avec les éléments de la collection
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> creerList(T... objets) {
        if (objets == null) {
            return new ArrayList<T>();
        }
        else {
            final List<T> l = new ArrayList<T>(objets.length);
            for (final T o : objets) {
                l.add(o);
            }
            return l;
        }
    }

    /**
     * Création d'un Set à partir d'une collection.
     *
     * @param <T>
     *            type des éléments de la liste
     * @param col
     *            collection contenant les éléments à ajouter dans le set
     * @return Le set avec les éléments de la colllection
     */
    public static <T> Set<T> creerSet(Collection<T> col) {
        if (CollectionUtils.isEmpty(col)) {
            return new HashSet<T>();
        }
        else {
            return new HashSet<T>(col);
        }
    }

    /**
     * Création d'un Set à partir d'un tableau.
     *
     * @param <T>
     *            type des éléments de la liste
     * @param objets
     *            éléments à ajouter dans le set
     * @return Le set avec les éléments de la colllection
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> creerSet(T... objets) { // NO_UCD (unused code)
        if (objets == null) {
            return new HashSet<T>();
        }
        else {
            final Set<T> s = new HashSet<T>(objets.length);
            for (final T o : objets) {
                s.add(o);
            }
            return s;
        }
    }

    /**
     * Renvoie la difference de deux listes d'objets de type T. Le champ identifiant l'objet doit posséder un getter si nomChampIdentifiant est renseigné.
     *
     * @param <T>
     *            le type.
     * @param liste1
     *            la première liste.
     * @param liste2
     *            la seconde liste.
     * @param nomChampIdentifiant
     *            la chaîne correspondant à l'identifiant ou null pour des listes simples.
     * @return Liste de T, la différence entre les 2 listes.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> difference(List<T> liste1, List<T> liste2, String nomChampIdentifiant) { // NO_UCD (unused code)
        String getterChampIdentifiant = null;
        if (nomChampIdentifiant != null) {
            getterChampIdentifiant = "get" + nomChampIdentifiant.substring(0, 1).toUpperCase();
            if (nomChampIdentifiant.length() > 1) {
                getterChampIdentifiant += nomChampIdentifiant.substring(1);
            }
        }

        final List<T> listeDiff = new ArrayList<T>();

        final Set<Object> idsListe1 = new HashSet<Object>();
        if (getterChampIdentifiant != null) {
            for (final T objetT : liste1) {
                Object idObjetT;
                try {
                    idObjetT = objetT.getClass().getMethod(getterChampIdentifiant, new Class[] {}).invoke(objetT, new Object[] {});
                }
                catch (final Exception e) {
                    return Collections.EMPTY_LIST;
                }
                idsListe1.add(idObjetT);
            }
        }
        else {
            idsListe1.addAll(liste1);
        }

        final Set<Object> idsListe2 = new HashSet<Object>();
        if (getterChampIdentifiant != null) {
            for (final T objetT : liste2) {
                Object idObjetT;
                try {
                    idObjetT = objetT.getClass().getMethod(getterChampIdentifiant, new Class[] {}).invoke(objetT, new Object[] {});
                }
                catch (final Exception e) {
                    return Collections.EMPTY_LIST;
                }
                idsListe2.add(idObjetT);
            }
        }
        else {
            idsListe2.addAll(liste2);
        }

        int i = 0;
        final Iterator<?> it1 = idsListe1.iterator();

        // parcours des identifiants des éléments de la liste 1
        while (it1.hasNext()) {
            final Object o = it1.next();
            if (!idsListe2.contains(o)) {
                listeDiff.add(liste1.get(i));
            }
            i++;
        }

        final Iterator<?> it2 = idsListe2.iterator();
        i = 0;

        // parcours des identifiants des éléments de la liste 2
        while (it2.hasNext()) {
            final Object o = it2.next();
            if (!idsListe1.contains(o) && !listeDiff.contains(o)) {
                listeDiff.add(liste2.get(i));
            }
            i++;
        }

        return listeDiff;
    }

    /**
     * Vérifie si 2 collections sont totalement disjointes.
     *
     * @param c1
     *            la premiere instance de collection
     * @param c2
     *            la seconde instance de collection
     * @return un Booléen, True si les collections sont disjointes, False sinon
     */
    public static Boolean disjoint(Collection<?> c1, Collection<?> c2) { // NO_UCD (unused code)
        if (c1 == null && c2 == null) {
            return Boolean.FALSE;
        }
        if (c1 == null || c2 == null) {
            return Boolean.TRUE;
        }
        return Collections.disjoint(c1, c2);
    }

    /**
     * Permet de mettre les valeurs d'une collection sans accent (les tableaux, List, et Set).
     *
     * @param <T>
     *            le type.
     * @param iterateObject
     *            l'objet à traiter.
     * @return la collection dont les valeurs ont été mises en majuscule.
     */
    public static <T> T elementSansAccens(T iterateObject) { // NO_UCD (unused code)
        return elementSansAccensToUpperCase(iterateObject, Boolean.FALSE);
    }

    /**
     * Permet de mettre les valeurs d'une collection en majuscule sans accent (les tableaux, List, et Set).
     *
     * @param <T>
     *            le type.
     * @param iterateObject
     *            l'objet à traiter.
     * @return la collection dont les valeurs ont été mises en majuscule.
     */
    public static <T> T elementSansAccensMajuscule(T iterateObject) { // NO_UCD (unused code)
        return elementSansAccensToUpperCase(iterateObject, Boolean.TRUE);
    }

    /**
     * Permet de mettre les valeurs d'une collection en majuscule sans accent (les tableaux, List, et Set).
     *
     * @param <T>
     *            le type.
     * @param iterateObject
     *            l'objet à traiter.
     * @param isUpperCase
     *            true en majuscule, si non false.
     * @return la collection dont les valeurs ont été mises en majuscule.
     */
    @SuppressWarnings("unchecked")
    private static <T> T elementSansAccensToUpperCase(T iterateObject, Boolean isUpperCase) {
        if (ObjectUtils.isIterableObject(iterateObject)) {
            if (iterateObject instanceof Object[]) {
                return elementTableauPourSansAccens(iterateObject, isUpperCase);
            }
            else if (iterateObject instanceof Collection) {
                final Object[] liste = ((Collection<?>) iterateObject).toArray();
                return (T) ((Collection<?>) Arrays.asList(elementTableauPourSansAccens(liste, isUpperCase)));
            }
            return iterateObject;
        }
        else {
            return iterateObject;
        }
    }

    /**
     * remplace chacune des chaines contenues dans le tableau par des chaines en majuscule sans accent.
     *
     * @param <T>
     *            le type.
     * @param iterateObject
     *            le tableau.
     * @param isUpperCase
     *            true en majuscule, si non false.
     * @return le tableau.
     */
    @SuppressWarnings("unchecked")
    private static <T> T elementTableauPourSansAccens(T iterateObject, Boolean isUpperCase) {
        final Object[] tableau = (Object[]) iterateObject;
        for (int i = 0; i < tableau.length; i++) {
            if (tableau[i] instanceof String) {
                if (isUpperCase) {
                    tableau[i] = StringUtils.sansAccentEnMajuscule(tableau[i].toString());
                }
                else {
                    tableau[i] = StringUtils.sansAccent(tableau[i].toString());
                }
            }
        }

        return (T) tableau;
    }

    /**
     * Retourne l'ensemble des valeurs (avec un DISTINCT) de la colonne 'nomChamp' dans la liste des résultats de la requête 'liste'.
     *
     * @param <T>
     *            Type des éléments de retour.
     * @param liste
     *            Liste des résultats de la requête.
     * @param nomChamp
     *            Nom du champ des données à récupérer.
     * @return L'ensemble des valeurs (avec un DISTINCT) de la colonne.
     */
    public static <T> List<T> findEnsemble(List<Map<String, ?>> liste, String nomChamp) { // NO_UCD (unused code)
        final Map<T, ?> mapInfos = formatMapClefValeur(liste, nomChamp, null);
        return new ArrayList<T>(mapInfos.keySet());
    }

    /**
     * Retourne l'objet associé à l'ensemble des clés indiquées.
     *
     * @param <T>
     *            Type de retour
     * @param <U>
     *            Type des cles
     * @param map
     *            map contenant les informations
     * @param cles
     *            cles dans l'ordre à utiliser
     * @return Valeur contenue en utilisant les clés
     */
    @SuppressWarnings("unchecked")
    public static <T, U> T findInMap(Map<?, ?> map, U... cles) { // NO_UCD (unused code)
        Map<?, ?> mapBoucle = map;
        for (int i = 0; i < cles.length - 1; i++) {
            final Map<?, ?> map2 = (Map<?, ?>) mapBoucle.get(cles[i]);
            if (map2 == null) {
                return null;
            }
            mapBoucle = map2;
        }

        final T obj = (T) mapBoucle.get(cles[cles.length - 1]);
        return obj;
    }

    /**
     * Formate une liste provenant d'une requete HQL en une map à double entrees.
     *
     * @param <T>
     *            Type de la premiere cle.
     * @param <U>
     *            Type de la seconde cle.
     * @param <V>
     *            Type de la valeur.
     * @param liste
     *            liste des infos.
     * @param id1
     *            identifiant de la premiere cle.
     * @param id2
     *            identifiant de la seconde cle.
     * @param valeur
     *            valeur.
     * @return Map à double entrees contenant les infos de la liste.
     */
    @SuppressWarnings("unchecked")
    public static <T, U, V> Map<T, Map<U, V>> formatMapClef2Niveaux(List<Map<String, ?>> liste, String id1, String id2, String valeur) { // NO_UCD (unused code)

        final Map<T, Map<U, V>> mapRetour = new HashMap<T, Map<U, V>>();
        if (!isEmpty(liste)) {
            final boolean ligneComplete = StringUtils.isEmpty(valeur);
            for (final Map<String, ?> map : liste) {
                final T valeurId1 = (T) map.get(id1);
                final U valeurId2 = (U) map.get(id2);
                final V valeurValeur;
                if (ligneComplete) {
                    valeurValeur = (V) map;
                }
                else {
                    valeurValeur = (V) map.get(valeur);
                }
                Map<U, V> mapId1 = mapRetour.get(valeurId1);
                if (mapId1 == null) {
                    mapId1 = new HashMap<U, V>();
                    mapRetour.put(valeurId1, mapId1);
                }
                mapId1.put(valeurId2, valeurValeur);
            }
        }
        return mapRetour;
    }

    /**
     * Formate une liste provenant d'une requete HQL en une map à triple entrees.
     *
     * @param <T>
     *            Type de la premiere cle.
     * @param <U>
     *            Type de la seconde cle.
     * @param <V>
     *            Type de la troisieme cle.
     * @param <W>
     *            Type de la valeur.
     * @param liste
     *            liste des infos.
     * @param id1
     *            identifiant de la premiere cle.
     * @param id2
     *            identifiant de la seconde cle.
     * @param id3
     *            identifiant de la troisieme cle.
     * @param valeur
     *            valeur.
     * @return Map à triple entrees contenant les infos de la liste.
     */
    @SuppressWarnings("unchecked")
    public static <T, U, V, W> Map<T, Map<U, Map<V, W>>> formatMapClef3Niveaux(List<Map<String, ?>> liste, String id1, String id2, String id3, String valeur) { // NO_UCD (unused code)

        final Map<T, Map<U, Map<V, W>>> mapRetour = new HashMap<T, Map<U, Map<V, W>>>();
        if (!isEmpty(liste)) {
            final boolean ligneComplete = StringUtils.isEmpty(valeur);
            for (final Map<String, ?> map : liste) {
                final T valeurId1 = (T) map.get(id1);
                final U valeurId2 = (U) map.get(id2);
                final V valeurId3 = (V) map.get(id3);
                final W valeurValeur;
                if (ligneComplete) {
                    valeurValeur = (W) map;
                }
                else {
                    valeurValeur = (W) map.get(valeur);
                }
                Map<U, Map<V, W>> mapId1 = mapRetour.get(valeurId1);
                if (mapId1 == null) {
                    mapId1 = new HashMap<U, Map<V, W>>();
                    mapRetour.put(valeurId1, mapId1);
                }
                Map<V, W> mapId2 = mapId1.get(valeurId2);
                if (mapId2 == null) {
                    mapId2 = new HashMap<V, W>();
                    mapId1.put(valeurId2, mapId2);
                }
                mapId2.put(valeurId3, valeurValeur);
            }
        }
        return mapRetour;
    }

    /**
     * Formatte une liste de Map en Map(key,T). l'identifiant passé en paramètre permet la reconnaissance de la clef de la map générée. Elle permet de
     * concaténer n valeurs pour une clef.
     *
     * @param <T>
     *            type des éléments de retour une liste de valeur.
     * @param <U>
     *            type de la clé des éléments de retour.
     * @param liste
     *            la liste contenant un ensemble de données non formattées.
     * @param identifiant
     *            la chaîne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur
     *            la chaîne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée. Si la valeur est
     *            nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    public static <T, U> Map<U, List<T>> formatMapClefEnsembleValeurs(List<Map<String, ?>> liste, String identifiant, String valeur) { // NO_UCD (unused code)
        return formatMapClefEnsembleValeursAvecOuSansLimitateur(liste, identifiant, valeur, 0);
    }

    /**
     * Formatte une liste de Map en Map(key,T). l'identifiant passé en paramètre permet la reconnaissance de la clef de la map générée. Elle permet de
     * concaténer n valeurs pour une clef avec une limitation.
     *
     * @param <T>
     *            type des éléments de retour une liste de valeur.
     * @param <U>
     *            type de la clé des éléments de retour.
     * @param liste
     *            la liste contenant un ensemble de données non formattées.
     * @param identifiant
     *            la chaîne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur
     *            la chaîne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée.
     * @param limitation
     *            la limitation du nombre de valeur de la liste liée à une clef. Si la valeur est nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    public static <T, U> Map<U, List<T>> formatMapClefEnsembleValeursAvecLimitateur(List<Map<String, ?>> liste, String identifiant, String valeur, Integer limitation) { // NO_UCD (unused code)
        return formatMapClefEnsembleValeursAvecOuSansLimitateur(liste, identifiant, valeur, limitation);
    }

    /**
     * Utilisé par formatMapClefEnsembleValeurs et par formatMapClefEnsembleValeursAvecOuSansLimitateur.
     *
     * @param <T>
     *            type des éléments de retour une liste de valeur.
     * @param <U>
     *            type de la clé des éléments de retour.
     * @param liste
     *            la liste contenant un ensemble de données non formattées.
     * @param identifiant
     *            la chaîne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur
     *            la chaîne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée.
     * @param limitation
     *            la limitation du nombre de valeur de la liste liée à une clef. Si la valeur est nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    @SuppressWarnings("unchecked")
    private static <T, U> Map<U, List<T>> formatMapClefEnsembleValeursAvecOuSansLimitateur(List<Map<String, ?>> liste, String identifiant, String valeur, Integer limitation) {
        final Map<U, List<T>> mapKeyValue = new HashMap<U, List<T>>();
        final Map<U, Integer> mapLimitation = new HashMap<U, Integer>();
        if (!isEmpty(liste)) {
            final boolean ligneComplete = StringUtils.isEmpty(valeur);
            for (final Map<String, ?> map : liste) {
                final U key = (U) map.get(identifiant);
                if (mapKeyValue.containsKey(key)) {
                    final Integer limitationEnCours = mapLimitation.get(key);
                    if (limitationEnCours < limitation || limitation == 0) {
                        final List<T> listeResult = mapKeyValue.get(key);
                        if (ligneComplete) {
                            listeResult.add((T) map);
                        }
                        else {
                            listeResult.add((T) map.get(valeur));
                        }
                        mapKeyValue.put(key, listeResult);
                        mapLimitation.put(key, limitationEnCours + 1);
                    }
                }
                else {
                    final List<T> listeResult = new ArrayList<T>();
                    if (ligneComplete) {
                        listeResult.add((T) map);
                    }
                    else {
                        listeResult.add((T) map.get(valeur));
                    }
                    mapKeyValue.put(key, listeResult);
                    mapLimitation.put(key, 1);
                }
            }
        }
        return mapKeyValue;

    }

    /**
     * Formatte une liste de Map en Map(key,T). l'identifiant passé en paramètre permet la reconnaissance de la clef de la map générée. Elle permet de
     * concaténer n valeurs uniques pour une clef.
     *
     * @param <T>
     *            type des éléments de retour un set de valeur.
     * @param <U>
     *            type de la clé des éléments de retour.
     * @param liste
     *            la liste contenant un ensemble de données non formattées.
     * @param identifiant
     *            la chaîne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur
     *            la chaîne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée. Si la valeur est
     *            nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    @SuppressWarnings("unchecked")
    public static <T, U> Map<U, Set<T>> formatMapClefSetValeurs(List<Map<String, ?>> liste, String identifiant, String valeur) { // NO_UCD (unused code)
        final Map<U, Set<T>> mapKeyValue = new HashMap<U, Set<T>>();
        if (!isEmpty(liste)) {
            final boolean ligneComplete = StringUtils.isEmpty(valeur);
            for (final Map<String, ?> map : liste) {
                final U key = (U) map.get(identifiant);
                if (mapKeyValue.containsKey(key)) {
                    final Set<T> setResult = mapKeyValue.get(key);
                    if (ligneComplete) {
                        setResult.add((T) map);
                    }
                    else {
                        setResult.add((T) map.get(valeur));
                    }
                    mapKeyValue.put(key, setResult);
                }
                else {
                    final Set<T> setResult = new HashSet<T>();
                    if (ligneComplete) {
                        setResult.add((T) map);
                    }
                    else {
                        setResult.add((T) map.get(valeur));
                    }
                    mapKeyValue.put(key, setResult);
                }
            }
        }
        return mapKeyValue;
    }

    /**
     * Formatte une liste de Map en Map(key,T). l'identifiant passé en paramètre permet la reconnaissance de la clef de la map générée.
     *
     * @param <T>
     *            type des éléments de retour.
     * @param <U>
     *            type de la clé des éléments de retour.
     * @param liste
     *            la liste contenant un ensemble de données non formattées.
     * @param identifiant
     *            la chaÃ®ne de caractères décrivant la clef d'accès à la key de la Map retournée
     * @param valeur
     *            la chaÃ®ne de caractères décrivant la clef d'accès à la valeur mise en correspondance avec la key dans la Map retournée. Si la valeur est
     *            nulle, la map sera considéré comme valeur.
     * @return la map formattée.
     */
    @SuppressWarnings("unchecked")
    public static <T, U> Map<U, T> formatMapClefValeur(List<Map<String, ?>> liste, String identifiant, String valeur) { // NO_UCD (use private)
        final Map<U, T> mapKeyValue = new HashMap<U, T>();
        if (!isEmpty(liste)) {
            final boolean ligneComplete = StringUtils.isEmpty(valeur);
            for (final Map<String, ?> map : liste) {
                final U key = (U) map.get(identifiant);
                if (ligneComplete) {
                    mapKeyValue.put(key, (T) map);
                }
                else {
                    final T value = (T) map.get(valeur);
                    mapKeyValue.put(key, value);
                }
            }
        }
        return mapKeyValue;
    }

    /**
     * Formate une Map(String, T) en List(T).
     *
     * @param <T>
     *            type des éléments de retour.
     * @param map
     *            la map contenant un ensemble de données non formattées.
     * @param identifiant
     *            la chaîne de caractères décrivant la clef à utiliser pour remplir la liste, si l'identifiant est nul, toutes les valeurs de la map, pour
     *            toutes les clefs seront contenues dans la liste.
     * @return la liste.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> formatMapEnliste(Map<String, ?> map, String identifiant) { // NO_UCD (unused code)
        final List<T> liste = new ArrayList<T>();
        if (map != null) {
            for (final String key : map.keySet()) {
                if (!StringUtils.isEmpty(identifiant)) {
                    if (identifiant.equals(key)) {
                        liste.add((T) map.get(key));
                    }
                }
                else {
                    liste.add((T) map.get(key));
                }
            }
        }
        return liste;
    }

    /**
     * Supprime les doublons du champ identifiant de la Liste de map.
     *
     * @param <T>
     *            type des éléments de retour
     * @param <U>
     *            type de la clé
     * @param liste
     *            la liste à dédoublonner
     * @param identifiant
     *            le champ sur lequel dédoublonner
     * @return la liste sans doublons
     */
    public static <T, U> List<T> formatMapListeClefValeur(List<Map<String, ?>> liste, String identifiant) { // NO_UCD (unused code)
        final Map<U, T> mapKeyValue = formatMapClefValeur(liste, identifiant, null);
        final List<T> listKeyValue = new ArrayList<T>();
        for (final U mapKey : mapKeyValue.keySet()) {
            listKeyValue.add(mapKeyValue.get(mapKey));
        }

        return listKeyValue;
    }

    /**
     * Récupère les éléments d'une collection apparaissant plus d'une fois dans la collection.
     *
     * @param <T>
     *            le type.
     * @param c
     *            l'instance de collection
     * @return un Set des éléments apparaissant plus d'une fois dans la collection.
     */
    public static <T> Set<T> getDuplicateElement(Collection<T> c) { // NO_UCD (unused code)
        if (CollectionUtils.isEmpty(c)) {
            return Collections.emptySet();
        }
        final Set<T> set = new HashSet<T>();
        for (final T t : c) {
            if (Collections.frequency(c, t) != 1) {
                set.add(t);
            }
        }
        return set;
    }

    /**
     * Retourne l'élément "i" d'une liste.
     *
     * @param <T>
     *            type des éléments de la liste.
     * @param l
     *            la liste
     * @param i
     *            l'indice
     * @return the element
     */
    public static <T> T getElement(List<T> l, Integer i) { // NO_UCD (unused code)
        if (i < 0 || CollectionUtils.size(l) <= i) {
            return null;
        }
        return l.get(i);
    }

    /**
     * Récupération du premier élément d'une liste.
     *
     * @param <T>
     *            type générique.
     * @param liste
     *            la liste.
     * @return le dernier élément.
     */
    public static <T> T getFirstElement(List<T> liste) {
        if (isEmpty(liste)) {
            return null;
        }
        else {
            return liste.get(0);
        }
    }

    /**
     * Récupération de la premiere map d'une liste d'éléments.
     *
     * @param liste
     *            la liste.
     * @return la premiere map.
     */
    public static Map<String, ?> getFirstMap(List<Map<String, ?>> liste) {
        if (isEmpty(liste)) {
            return Collections.emptyMap();
        }
        else {
            return liste.get(0);
        }
    }

    /**
     * Récupération du dernier élément d'une liste.
     *
     * @param <T>
     *            type générique.
     * @param liste
     *            la liste.
     * @return le dernier élément.
     */
    public static <T> T getLastElement(List<T> liste) { // NO_UCD (unused code)
        if (isEmpty(liste)) {
            return null;
        }
        else {
            return liste.get(liste.size() - 1);
        }
    }

    /**
     * Permet de diposer d'une liste d'identifiant unique provenant d'une requête de type "SELECT new Map()" par l'intermédiare d'un alias passé en paramètre.
     *
     * @param liste
     *            la liste provenant d'une requête oÃ¹ autres.
     * @param identifiant
     *            l'alias servant de clef afin de rechercher la valeur adéquate dans la map.
     * @return la liste d'identifiant recherché.
     */
    public static Set<Integer> getListeIdentifiantFromMapHql(final List<Map<Object, ?>> liste, final String identifiant) {
        final Set<Integer> listeIdentifiant = new HashSet<Integer>();
        if (!isEmpty(liste)) {
            for (final Map<Object, ?> map : liste) {
                final Integer id = (Integer) map.get(identifiant);
                if (id != null) {
                    listeIdentifiant.add(id);
                }
            }
        }
        return listeIdentifiant;
    }

    /**
     * Permet de compter le nombre d'occurence dans une liste <String>.
     *
     * @param list
     *            the list
     * @return the nombre occurence
     */
    public static Map<String, Integer> getNombreOccurence(List<String> list) { // NO_UCD (unused code)
        final Set<String> setList = new HashSet<String>();
        final Map<String, Integer> mapFinal = new HashMap<String, Integer>();
        Integer nb = 0;
        for (final String occurence : list) {
            if (!setList.contains(occurence)) {
                setList.add(occurence);
                mapFinal.put(occurence, 1);
            }
            else {
                setList.add(occurence);
                nb = mapFinal.get(occurence);
                nb++;
                mapFinal.remove(occurence);
                mapFinal.put(occurence, nb);
            }
        }
        return mapFinal;
    }

    /**
     * Vérifie si les éléments d'une collection apparaissent une et une seule fois dans la collection.
     *
     * @param <T>
     *            le type.
     * @param c
     *            l'instance de collection
     * @return un booléen, True si les éléments apparaissent une et une seule fois, False sinon
     */
    public static <T> Boolean hasNoDuplicateElement(Collection<T> c) { // NO_UCD (unused code)
        if (CollectionUtils.isEmpty(c)) {
            return Boolean.TRUE;
        }
        final Set<T> set = new HashSet<T>(c);
        for (final T t : set) {
            if (Collections.frequency(c, t) != 1) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Renvoie l'intersection de deux listes d'objets de type T. Le champ identifiant l'objet doit posséder un getter.
     *
     * @param <T>
     *            le type.
     * @param liste1
     *            la première liste.
     * @param liste2
     *            la seconde liste.
     * @param nomChampIdentifiant
     *            la chaîne correspondant à l'identifiant.
     * @return Liste de T, l'intersection des 2 listes.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> intersect(List<T> liste1, List<T> liste2, String nomChampIdentifiant) { // NO_UCD (unused code)
        String getterChampIdentifiant = "get" + nomChampIdentifiant.substring(0, 1).toUpperCase();
        if (nomChampIdentifiant.length() > 1) {
            getterChampIdentifiant += nomChampIdentifiant.substring(1);
        }

        final List<T> listeInter = new ArrayList<T>();

        final Set<Object> idsListe1 = new HashSet<Object>();

        for (final T objetT : liste1) {
            Object idObjetT;
            try {
                idObjetT = objetT.getClass().getMethod(getterChampIdentifiant, new Class[] {}).invoke(objetT, new Object[] {});
            }
            catch (final Exception e) {
                return Collections.EMPTY_LIST;
            }
            idsListe1.add(idObjetT);
        }
        for (final T objetT : liste2) {
            try {
                final Object idObjetT = objetT.getClass().getMethod(getterChampIdentifiant, new Class[] {}).invoke(objetT, new Object[] {});
                if (idsListe1.contains(idObjetT)) {
                    listeInter.add(objetT);
                }
            }
            catch (final Exception e) {
                return Collections.EMPTY_LIST;
            }
        }

        return listeInter;
    }

    /**
     * Invoque la méthode clone d'un objet.
     *
     * @param objetAcloner
     *            l'objet.
     * @return l'objet cloné.
     * @throws Exception
     *             l'exception possible lors de l'appel de la méthode.
     */
    private static Object invokeClone(Object objetAcloner) throws Exception {
        // récupération de la méthode clone de l'objet par réflexion
        Method m1 = null;
        Object oo = null;
        try {

            m1 = objetAcloner.getClass().getMethod("clone");
            // invocation de la méthode clone de cet objet
            oo = m1.invoke(objetAcloner);
        }
        catch (final Exception e) {
            throw e;
        }
        return oo;
    }

    /**
     * Teste si une {@link Collection} est vide ou <code>null</code>.
     *
     * @param <T>
     *            type générique
     * @param col
     *            La collection à vérifier
     * @return vrai si la collection est null ou vide.
     */
    public static <T> boolean isEmpty(Collection<T> col) {
        return (col == null) || (col != null && col.isEmpty());
    }

    /**
     * Regroupement d'une collection splittée. Doublons possibles.
     *
     * @param <T>
     *            type des elements de la collection
     * @param colCol
     *            collection de collection
     * @return liste avec les elements
     */
    public static <T> List<T> join(Collection<Collection<T>> colCol) { // NO_UCD (unused code)
        final List<T> liste = new ArrayList<T>();
        for (final Collection<T> col : colCol) {
            liste.addAll(col);
        }
        return liste;
    }

    /**
     * Regroupement d'une collection splittée. Doublons possibles.
     *
     * @param <U>
     *            type des elements de la collection
     * @param <T>
     *            type de la collection
     * @param colCol
     *            collection de collection
     * @return liste avec les elements
     */
    @SuppressWarnings("unchecked")
    public static <U, T extends Collection<U>> List<U> join(T... colCol) { // NO_UCD (unused code)
        final List<U> liste = new ArrayList<U>();
        for (final Collection<U> col : colCol) {
            if (col != null) {
                liste.addAll(col);
            }
        }
        return liste;
    }

    /**
     * Regroupement d'une collection splittée. Doublons impossibles.
     *
     * @param <T>
     *            type des elements de la collection
     * @param colCol
     *            collection de collection
     * @return ensemble avec les elements
     */
    public static <T> Set<T> joinSansDoublons(Collection<Collection<T>> colCol) { // NO_UCD (unused code)
        final Set<T> ensemble = new HashSet<T>();
        for (final Collection<T> col : colCol) {
            ensemble.addAll(col);
        }
        return ensemble;
    }

    /**
     * Regroupement d'une collection splittée sans doublons possibles.
     *
     * @param <U>
     *            type des elements de la collection
     * @param <T>
     *            type de la collection
     * @param colCol
     *            collection de collection
     * @return set avec les elements
     */
    @SuppressWarnings("unchecked")
    public static <U, T extends Collection<U>> Set<U> joinSansDoublons(T... colCol) { // NO_UCD (unused code)
        final Set<U> set = new HashSet<U>();
        for (final Collection<U> col : colCol) {
            set.addAll(col);
        }
        return set;
    }

    /**
     * Retourne une string a partir d'une map<String, String>.
     *
     * @param map
     *            the map
     * @param separateur1
     *            the separateur1
     * @param separateur2
     *            the separateur2
     * @return the string
     */
    public static String mapToString(Map<?, ?> map, String separateur1, String separateur2) { // NO_UCD (unused code)
        final StringBuilder builder = new StringBuilder();
        if (map == null || separateur1 == null || separateur2 == null) {
            return builder.toString();
        }
        for (final Map.Entry<?, ?> e : map.entrySet()) {
            builder.append(e.getKey()).append(separateur1).append(e.getValue()).append(separateur2);
        }
        return builder.toString();
    }

    /**
     * ConÃ§oit une liste du type T d'une taille absolue définie par nb. Renvoi donc les n occurrences de la liste de départ, et des lignes vides d'instances
     * distinctes, si besoin est.
     *
     * @param <T>
     *            le type
     * @param liste
     *            la liste de départ.
     * @param nb
     *            le nombre d'élément que doit faire la liste de retour.
     * @param obj
     *            une instance d'un objet du type T initialisée par le constructeur par défaut.
     * @return la liste avec la taille absolue.
     */
    public static <T> List<T> obtenirListeAbsolue(final List<T> liste, final Integer nb, final T obj) { // NO_UCD (unused code)
        final List<T> listeRetour = new ArrayList<T>();
        int cpt = 0;
        for (final T t : liste) {
            listeRetour.add(t);
            cpt++;
            if (cpt == nb) {
                break;
            }
        }

        int tailleFinale = listeRetour.size();

        while (tailleFinale != nb) {
            listeRetour.add(ObjectUtils.clone(obj));
            tailleFinale++;
        }

        return listeRetour;
    }

    /**
     * Retourne le nombre d'éléments de la collection.
     *
     * @param <T>
     *            type générique
     * @param col
     *            collection
     * @return nombre d'éléments
     */
    public static <T> int size(Collection<T> col) { // NO_UCD (use private)
        if (col == null) {
            return 0;
        }
        else {
            return col.size();
        }
    }

    /**
     * Soustraction : enleve de la liste A tous ceux déjà présents dans la liste B. Cette méthode diffère de celle nommée 'difference' qui elle soustrait dans
     * les deux listes.
     *
     * @param <T>
     *            type quelconque
     * @param listeA
     *            the liste a
     * @param listeB
     *            the liste b
     * @return liste finale
     */
    public static <T> List<T> soustraction(List<T> listeA, List<T> listeB) { // NO_UCD (unused code)
        listeA.removeAll(listeB);

        final List<T> listeFinale = new ArrayList<T>(listeA);
        return listeFinale;
    }

    /**
     * Découpe une liste d'éléments en plusieurs listes plus petites pour que les requêtes HQL s'exécutent correctement.
     *
     * @param <T>
     *            type générique
     * @param liste
     *            La liste à découper
     * @return Les listes composées des éléments de la liste de départ. Chaque liste a une taille permettant une requête HQL.
     */
    public static <T> List<List<T>> split(List<T> liste) {
        return split(liste, 800);
    }

    /**
     * Découpe une liste d'éléments en plusieurs listes.
     *
     * @param <T>
     *            type générique
     * @param liste
     *            La liste à découper
     * @param tailleMax
     *            Le nombre d'éléments maximum à mettre dans les listes de retour
     * @return Les listes composées des éléments de la liste de départ. Chaque liste a une taille inférieure à tailleMax.
     */
    public static <T> List<List<T>> split(List<T> liste, int tailleMax) {
        if (liste == null) {
            return null;
        }
        final int tailleListe = liste.size();
        if (tailleListe == 0) {
            return Collections.emptyList();
        }
        if (tailleListe <= tailleMax) {
            return Collections.singletonList(liste);
        }

        final BigDecimal t = new BigDecimal(tailleListe);
        final BigDecimal tMax = new BigDecimal(tailleMax);
        final BigDecimal nbSousListe = t.divide(tMax, RoundingMode.UP);

        final List<List<T>> listeRetour = new ArrayList<List<T>>();

        for (int i = 1; i <= nbSousListe.intValue(); i++) {
            listeRetour.add(CollectionUtils.sublist(liste, i, tailleMax));
        }

        return listeRetour;
    }

    /**
     * Sublist.
     *
     * @param <T>
     *            the generic type
     * @param list
     *            the list
     * @return the list
     */
    public static <T> List<T> sublist(List<T> list) { // NO_UCD (unused code)
        return CollectionUtils.sublist(list, 0);
    }

    /**
     * Sublist.
     *
     * @param <T>
     *            the generic type
     * @param list
     *            the list
     * @param indiceSousListe
     *            the indice sous liste
     * @return the list
     */
    public static <T> List<T> sublist(List<T> list, Integer indiceSousListe) { // NO_UCD (use private)
        return CollectionUtils.sublist(list, indiceSousListe, 200);
    }

    /**
     * Créer une sous liste à partir de la liste passée en paramètre.
     *
     * @param <T>
     *            Le type des éléments de la liste.
     * @param list
     *            La liste totale
     * @param indiceSousListe
     *            l'indice de sous liste a récupérer
     * @param nombreElementsSousListe
     *            le nombre d'éléments à mettre par sous liste.
     * @return La sous liste contenant uniquement les éléments de l'indice de sous liste spécifié.
     */
    public static <T> List<T> sublist(List<T> list, Integer indiceSousListe, Integer nombreElementsSousListe) { // NO_UCD (use private)
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        Integer indiceSousListeTMP = ObjectUtils.defaultIfNull(indiceSousListe, 0);
        indiceSousListeTMP = (indiceSousListeTMP < 0) ? 0 : indiceSousListeTMP;
        Integer nombreElementsSousListeTMP = ObjectUtils.defaultIfNull(nombreElementsSousListe, 200);
        nombreElementsSousListeTMP = (nombreElementsSousListeTMP < 1) ? 1 : nombreElementsSousListeTMP;

        if (nombreElementsSousListeTMP > list.size()) {
            return list;
        }

        final int indiceDebut = nombreElementsSousListeTMP * (indiceSousListeTMP - 1);
        final int indiceFin = Math.min(indiceDebut + nombreElementsSousListeTMP, list.size());

        return new ArrayList<>(list.subList(indiceDebut, indiceFin));
    }

}
