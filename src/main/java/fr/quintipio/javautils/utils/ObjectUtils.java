package fr.quintipio.javautils.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe utilitaire liés au objets en général
 * @author Quentin
 */
public class ObjectUtils {
    /**
     * Les 2 elements sont egaux.
     */
    public static final int COMPARE_EGALITE               = 0;

    /**
     * Le premier element est le plus grand.
     */
    public static final int COMPARE_PLUS_GRAND_EN_PREMIER = 1;

    /**
     * Le premier element est le plus petit.
     */
    public static final int COMPARE_PLUS_PETIT_EN_PREMIER = -1;

    private ObjectUtils() {}

      /**
         * Transfert les données d'un modèle contenu dans inputObject dans un nouvel objet de type outputClass à condition que les variables soient les mêmes des deux côtés.
         * @param inputObject l'objet en entré contenant les données à copier
         * @param outputClass le type d'objet souhaité en sortie
         * @param listeAttributExclus une liste des noms d'attributs dont les setter à remplir sont à exclure (null si aucun)
         * @return le nouvel objet de type outputClass avec les données d'inputObject à l'intérieur
         * @throws InstantiationException
         * @throws NoSuchMethodException
         * @throws SecurityException
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
         @SuppressWarnings("unchecked")
         public static <O,I> O transferDataModel(I inputObject, Class<O> outputClass,final List<String> listeAttributExclus) throws InstantiationException, NoSuchMethodException  , IllegalAccessException , InvocationTargetException {

               List<String> listeExclu = new ArrayList<>();
               if(listeAttributExclus != null) {
                      listeAttributExclus.stream().forEach(x -> listeExclu.add("set"+StringUtils.formatageSeulementPremiereLettreMajuscule(x)));
               }

               O outputData = outputClass.newInstance();
              
               //récupération des setters et pour chacun appel du getter s'il existe
               Arrays.asList(outputData.getClass().getDeclaredMethods()).stream().filter(x -> x.getName().startsWith("set")).collect(Collectors.toList()).stream().forEach(y -> {
                      if(!listeExclu.stream().anyMatch(s -> s.equals(y.getName()))) {
                             //pour chaque setter, on récupère le nom de la variable
                             String nameMethod = y.getName().replaceFirst("set", "");

                             //vérification que le getter associé existe bien
                             if(Arrays.asList(inputObject.getClass().getDeclaredMethods()).stream().filter(x -> x.getName().equals("get"+nameMethod)).count() > 0) {

                                    // si le getter existe, récupération du getter
                                    Method getter;
                                    try {
                                          getter = inputObject.getClass().getDeclaredMethod("get"+nameMethod);
                                          y.invoke(outputData, getter.invoke(inputObject));
                                    } catch (Exception vEx) {
                                          
                                    }
                                    //appel du setter rempli par le getter

                             }
                      }
               });
               return outputData;
         }


        /**
     * <p>
     * Retourne l'objet cloné en profondeur (toutes les propriétés de l'objet sont clonées en profondeur).
     * </p>
     * <p>
     * Ne pas utiliser cette méthode pour des objets clonés en profondeur par l'API, essentiellement des types "Simple" comme java.util.Date, ou pour des objets immuables (Long, Double, String ... dont le clonage est inutile).
     * </p>
     *
     * @param <T>
     *            type générique
     * @param o
     *            l'objet
     * @return Objet l'objet totalement cloné
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(T o) {

        if (isIterableObject(o)) {
            return CollectionUtils.cloneCollection(o);
        }
        else if (!isDeepClonableObject(o)) {
            throw new RuntimeException("vous devez utiliser directement la méthode clone de l'api. clone()");
        }
        // Recupération de l'ensemble des données membres de l'objet
        final Set<Field> fields = getFields(o);

        try {
            // récupération de la classe de l'objet
            final Class<T> clazz = (Class<T>) o.getClass();
            // récupération du constructeur par defaut
            final Constructor<T> constr = clazz.getDeclaredConstructor();
            // rend le constructeur par défaut accessible (si il est private ou protected)
            constr.setAccessible(true);
            // instanciation de l'objet
            final T newObject = constr.newInstance();

            for (final Field f : fields) {

                // si la donnee membre n'est pas final ni static
                if (!Modifier.isStatic(f.getModifiers()) && (!Modifier.isFinal(f.getModifiers()))) {

                    // rend la donnée membre accessible (il y a de grande chance qu'elle soit privée)
                    // l'Exception IllegalAccessException ne devrait jamais être levée.
                    f.setAccessible(true);
                    // récupération de l'instance de la donnée membre à cloner
                    // si un type primitif est récupéré il devient objet via le principe de l'autoboxing
                    final Object objetAcloner = f.get(o);
                    if (objetAcloner != null) {
                        /*
                         * si la donnée membre est d'un type clonable (non immuable (String, Integer, Double..)) alors on va tenter de la cloner. Les types
                         * ArrayList, HashSet impémentante Collection sont clonés naturellement mais en faisant une copie de surface(la référence de la
                         * Collection uniquement est clonée), tout comme les Map
                         */
                        if (isDeepClonableObject(objetAcloner) && !(objetAcloner instanceof Boolean)) { // Correction provisoire
                            // Visualisation de la méthode clone de l'objet,
                            // si elle n'existe pas on fait un appel recursif à clone de ObjectUtils
                            try {
                                // récupération de la méthode clone de l'objet par réflexion
                                final Method m = objetAcloner.getClass().getMethod("clone");
                                // invocation de la méthode clone de cet objet
                                final Object oClone = m.invoke(objetAcloner);
                                // modifie la reference de l'objet complexe présent au sein de l'objet source.
                                f.set(newObject, oClone);
                            }
                            catch (final NoSuchMethodException e) {
                                f.set(newObject, clone(objetAcloner));
                            }
                        }
                        else if (isIterableObject(objetAcloner)) {
                            // on modifie l'objet source cloné afin qu'il dispose de l'instance cloné du type itérable.
                            f.set(newObject, CollectionUtils.cloneCollection(objetAcloner));
                        }
                        else {
                            // l'objet doit être immuable.
                            f.set(newObject, objetAcloner);
                        }
                    }
                }
            }
            return newObject;
        }
        catch (final Exception e) {
            throw new RuntimeException("Erreur durant l'opération de clonage, " + "vérifiez que tout les objets disposent d'un constructeur par défaut" + " : " + e.getMessage(), e);
        }
    }

    /**
     * Retourne la comparaison entre 2 booleans. Permet de trier une liste, en indiquant la valeur par défaut du boolean et en indiquant si les valeurs Vrai
     * doivent être en premier dans la liste.
     *
     * @param <T>
     *            type générique
     * @param valeur1
     *            Première valeur à comparer.
     * @param valeur2
     *            Seconde valeur à comparer.
     * @param nullEnPremier
     *            Valeur par défaut à utiliser en cas de valeur null
     * @return inférieur, égal ou supérieur à 0 selon la règle des méthodes standard "compareTo".
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends Comparable> int compare(T valeur1, T valeur2, boolean nullEnPremier) {
        if (valeur1 == null) {
            if (valeur2 == null) {
                return COMPARE_EGALITE;
            }
            else {
                if (nullEnPremier) {
                    return COMPARE_PLUS_PETIT_EN_PREMIER;
                }
                else {
                    return COMPARE_PLUS_GRAND_EN_PREMIER;
                }
            }
        }
        else {
            if (valeur2 == null) {
                if (nullEnPremier) {
                    return COMPARE_PLUS_GRAND_EN_PREMIER;
                }
                else {
                    return COMPARE_PLUS_PETIT_EN_PREMIER;
                }
            }
            else {
                if (java.sql.Timestamp.class.isAssignableFrom(valeur1.getClass())) {
                    return -1 * valeur2.compareTo(valeur1);
                }
                return valeur1.compareTo(valeur2);
            }
        }
    }

    /**
     * Retourne une valeur par défaut pour un objet si celui-ci est <code>null</code>.
     *
     * @param <T>
     *            type générique
     * @param obj
     *            the obj
     * @param def
     *            the def
     * @return the T
     */
    public static <T> T defaultIfNull(T obj, T def) {
        return (obj == null) ? def : obj;
    }

    /**
     * Retourne Vrai si les 2 objets sont identiques ou tous les 2 nuls.
     *
     * @param <T>
     *            type générique
     * @param o1
     *            Premier objet à comparer
     * @param o2
     *            Second objet à comparer
     * @return Vrai si les 2 objets sont identiques ou tous les 2 nuls.
     */
    public static <T> boolean equals(T o1, T o2) { // NO_UCD (use private)
        if (o1 == null) {
            return (o2 == null);
        }
        else {
            if (o2 == null) {
                return false;
            }
            else {
                if (java.sql.Timestamp.class.isAssignableFrom(o1.getClass())) {
                    return o2.equals(o1);
                }
                return o1.equals(o2);
            }
        }
    }

    /**
     * Retourne vrai si les 2 objets sont identiques et qu'ils ne sont pas nuls.
     *
     * @param <T>
     *            type générique
     * @param o1
     *            Premier objet à comparer
     * @param o2
     *            Second objet à comparer
     * @return vrai si les 2 objets sont identiques et qu'ils ne sont pas nuls.
     */
    public static <T> boolean equalsAndNotNull(T o1, T o2) { // NO_UCD (unused code)
        if ((o1 == null) && (o2 == null)) {
            return false;
        }

        return ObjectUtils.equals(o1, o2);
    }

    /**
     * Retourne l'ensemble des champs d'une classe, y compris ceux hérités.
     *
     * @param clazz
     *            the clazz
     * @return the fields
     */
    public static Set<Field> getFields(Class<?> clazz) { // NO_UCD (use private)
        final Set<Field> fields = new HashSet<Field>();
        if (clazz == null) {
            return fields;
        }

        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            final Field[] fieldsArray = c.getDeclaredFields();
            fields.addAll(Arrays.asList(fieldsArray));
        }

        return fields;
    }

    /**
     * Retourne l'ensemble des champs d'un objet, y compris ceux qui sont hérités.
     *
     * @param obj
     *            the obj
     * @return the fields
     */
    public static Set<Field> getFields(Object obj) { // NO_UCD (use private)
        if (obj == null) {
            return new HashSet<Field>();
        }
        return getFields(obj.getClass());
    }

    /**
     * Vérification que la nature de l'objet, est un objet clonable en profondeur, et bien évidement non immuable.
     *
     * @param o
     *            l'objet à vérifier.
     * @return true si l'objet est dit clonable en profondeur, false dans l'autre cas.
     */
    public static Boolean isDeepClonableObject(Object o) {
        // les objets immuables (ne peuvent être modifiés après création) sont omis
        // le type Double, Integer, Long, BigDecimal sont des objets immuables et héritent tous de number
        return ((o != null) && !(o instanceof Number) && !(o instanceof String) && !(o instanceof Enum) && !(o instanceof Boolean) && !isIterableObject(o));
    }

    /**
     * Retourne Vrai si l'objet o1 appartient à la liste o2.
     *
     * @param <T>
     *            type générique
     * @param o1
     *            l'objet à trouver
     * @param o2
     *            liste dans laquelle rechercher
     * @return Vrai si l'objet o1 appartient à la liste o2.
     */
    public static <T> boolean isIn(T o1, List<T> o2) { // NO_UCD (use private)
        if (o2 == null || o2.isEmpty()) {
            return false;
        }
        return o2.contains(o1);
    }

    /**
     * Retourne Vrai si l'objet o1 appartient à la liste d'objets objets.
     *
     * @param <T>
     *            type générique
     * @param o1
     *            l'objet à trouver
     * @param objets
     *            objets dans lesquels il faut rechercher
     * @return Vrai si l'objet o1 appartient à la liste d'objets.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean isIn(T o1, T... objets) { // NO_UCD (unused code)
        return isIn(o1, Arrays.asList(objets));

    }

    /**
     * Permet de connaître si l'objet est itérable.
     *
     * @param o
     *            l'objet à vérifier.
     * @return true si l'objet est une "collection", false dans le cas contraire.
     */
    public static Boolean isIterableObject(Object o) {
        return (o instanceof Object[] || o instanceof Collection || o instanceof Map);
    }

    /**
     * Concatène deux chaînes. Une chaîne <code>null</code> est remplacée par une chaîne vide.
     *
     * @param a
     *            the a
     * @param b
     *            the b
     * @return the string
     */
    public static String prefixIfNotNull(String a, Object b) { // NO_UCD (unused code)
        if (b != null) {
            final String trimToBlank = b.toString();
            if (!"".equals(trimToBlank)) {
                return a + trimToBlank;
            }
        }
        return "";
    }

    /**
     * Désérialisation d'un objet.
     *
     * @param bytes
     *            the bytes
     * @return object
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    public static Object readObject(final byte[] bytes) throws IOException, ClassNotFoundException {
        if (bytes == null) {
            return null;
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ObjectInputStream ois = new ObjectInputStream(bais);
        try {
            return ois.readObject();
        }
        finally {
            if (ois != null) {
                ois.close();
            }
            if (bais != null) {
                bais.close();
            }
        }
    }

    /**
     * Permet de modifier les données membres d'un objet.
     *
     * @param objet
     *            l'objet.
     * @param mapNomChampValeur
     *            en clef le nom du champ et en valeur la valeur a affecter.
     */
    public static void setFieldsFromObject(final Object objet, final Map<String, Object> mapNomChampValeur) { // NO_UCD (unused code)
        final Set<Field> fields = getFields(objet);

        try {
            for (final Field f : fields) {
                final String name = f.getName();
                if (mapNomChampValeur.containsKey(name)) {
                    // l'Exception IllegalAccessException ne devrait jamais être levée.
                    f.setAccessible(true);
                    f.set(objet, mapNomChampValeur.get(name));
                }
            }
        }
        catch (final Exception e) {
            throw new RuntimeException("Erreur durant l'opération de modification des données membres : " + e.getMessage());
        }
    }

    /**
     * Sérialisation d'un objet.
     *
     * @param object
     *            the object
     * @return tableau de byte.
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static byte[] storeObject(final Serializable object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] retour = null;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            retour = baos.toByteArray();
        }
        finally {
            if (oos != null) {
                oos.close();
            }
            if (baos != null) {
                baos.reset();
                baos = null;
            }
        }

        return retour;
    }

    /**
     * Methode : testNullAlorsBlanc.
     *
     * @param obj
     *            : valeur a tester.
     * @return : valeur ou blanc.
     */
    public static String testNullAlorsBlanc(final Object obj) {
        return (obj != null) ? obj.toString() : "";
    }

}
