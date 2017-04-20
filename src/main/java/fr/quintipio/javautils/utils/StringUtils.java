package fr.quintipio.javautils.utils;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;


/**
 * Methodes utilitaires pour les Strings
 */
public final class StringUtils {

    /** Constante de chaine vide. */
    public static final String             EMPTY_STRING               = "";

    // private static final List<String> LIST_ACCENT_A = Arrays.asList("a", "à", "â", "ä");
    // private static final List<String> LIST_ACCENT_C = Arrays.asList("c", "ç");
    // private static final List<String> LIST_ACCENT_E = Arrays.asList("e", "é", "è", "ê", "ë");
    // private static final List<String> LIST_ACCENT_I = Arrays.asList("i", "î", "ï");
    // private static final List<String> LIST_ACCENT_O = Arrays.asList("o", "ô", "ö");
    // private static final List<String> LIST_ACCENT_U = Arrays.asList("u", "ù", "û", "ü");
    // le possessif "son" est volontairement omis pour permettre les recherche sur les métiers tournant autour du son (sonore)
    /** The Constant LIST_ADJECTIFS_POSSESSIFS. */
    private static final List<String>      LIST_ADJECTIFS_POSSESSIFS  = Arrays.asList("mon", "ton", "son", "ma", "ta", "sa", "notre", "votre", "leur", "mes", "tes", "ses", "nos", "vos", "leurs");

    /** The Constant LIST_APOSTROPHE. */
    private static final List<String>      LIST_APOSTROPHE            = Arrays.asList("l'", "d'", "t'", "m'", "qu'", "c'");

    // liste de mots à ignorer pour la recherche de métiers (ROME et appellations)
    // le "l'" n'est pas utile puisqu'on supprime les Apostrophes
    /** The Constant LIST_ARTICLES. */
    private static final List<String>      LIST_ARTICLES              = Arrays.asList("le", "au", "du", "à", "de", "la", "un", "une", "du", "les", "aux", "des");

    // la conjonction "car" est volontairement omise pour permettre les recherche sur les métiers contenant car (chauffeur de car)
    /** The Constant LIST_CONJONCTIONS. */
    private static final List<String>      LIST_CONJONCTIONS          = Arrays.asList("et", "ou", "ni", "mais", "or", "donc", "que", "quand", "comme", "si", "lorsque", "quoique", "puisque");

    /** Liste de correspondance entre accent / sans accent. * */
    private static final ArrayList<String> LIST_CORRESPONDANCE        = initCorrespondance();
    /** The Constant LIST_PREPOSITIONS. */
    private static final List<String>      LIST_PREPOSITIONS          = Arrays.asList("à", "après", "avant", "avec", "chez", "concernant", "contre", "dans", "de", "depuis", "derrière", "dès", "devant", "durant", "en", "entre", "envers", "hormis", "hors", "jusque", "malgré", "moyennant",
                                                                                      "nonobstant", "outre", "par", "parmi", "pendant", "pour", "près", "sans", "sauf", "selon", "sous", "suivant", "sur", "vers", "via", "dessus", "dessous", "loin");

    /** The Constant LIST_PRONOMS_DEMONSTRATIFS. */
    private static final List<String>      LIST_PRONOMS_DEMONSTRATIFS = Arrays.asList("ce", "ceci", "cela", "ça", "celui", "celui-ci", "celui-là", "celle", "celle-ci", "celle-là", "ceux", "ceux-ci", "ceux-là", "celles", "celles-ci", "celles-là");

    /** The Constant LIST_PRONOMS_INDEFINIS. */
    private static final List<String>      LIST_PRONOMS_INDEFINIS     = Arrays.asList("on", "uns", "unes", "autre", "autres", "aucun", "aucune", "aucuns", "aucunes", "certain", "certaine", "certains", "certaines", "tel", "telle", "tels", "telles", "tout", "toute", "tous", "toutes", "même", "mêmes",
                                                                                      "nul", "nulle", "nuls", "nulles", "personne", "autrui", "quiconque", "plusieurs", "rien", "quelque", "quelqu’un", " quelqu’une", "quelques", "chacun", "chacune");
    /** The Constant LIST_PRONOMS_PERSOS. */
    private static final List<String>      LIST_PRONOMS_PERSOS        = Arrays.asList("je", "me", "tu", "il", "elle", "nous", "vous", "ils", "elles", "leur", "on", "moi", "toi", "soi", "lui", "eux", "en", "y", "se", "te");

    /** The Constant LIST_PRONOMS_POSSESIFIFS. */
    private static final List<String>      LIST_PRONOMS_POSSESIFIFS   = Arrays.asList("mien", "tien", "sien", "mienne", "tienne", "sienne", "miens", "tiens", "siens", "miennes", "tiennes", "siennes", "nôtre", "vôtre", "leur", "nôtres", "vôtres", "leurs");

    /** The Constant LIST_PRONOMS_RELATIFS. */
    private static final List<String>      LIST_PRONOMS_RELATIFS      = Arrays.asList("qui", "que", "quoi", "dont", "où", "quel", "quels", "quelles", "lequel", "auquel", "duquel", "laquelle", "lesquels", "auxquels", "desquels", "lesquelles", "auxquelles", "desquelles");
    
    /** Index du dernier caractère accentué. * */
    private static final int               MAX                        = 255;
    /** Index du 1er caractère accentué. * */
    private static final int               MIN                        = 192;

    private StringUtils() {}
    
    /**
     * Ajoute le caractère sans l'accent dans le builder.
     *
     * @param builder
     *            builder
     * @param c
     *            caractere
     */
    private static void ajouteSansAccent(StringBuilder builder, char c) {
        if ((c >= MIN) && (c <= MAX)) {
            // ajout du caractère sans accent.
            final String newVal = LIST_CORRESPONDANCE.get(c - MIN);
            builder.append(newVal);
        }
        else {
            builder.append(c);
        }
    }

    /**
     * Stack trace to string.
     *
     * @param exception
     *            the exception
     * @return the string
     */
    public static String stackTraceToString(Exception exception) {
        String retour = null;
        StackTraceElement[] traceElement = exception.getStackTrace().clone();
        StringBuilder builder = new StringBuilder();
        builder.append(exception.toString() + " " + exception.getMessage() + "\n");
        for (StackTraceElement stackTraceElement : traceElement) {
            if (!stackTraceElement.toString().startsWith("org.") && !stackTraceElement.toString().startsWith("com.") && !stackTraceElement.toString().startsWith("javax.") && !stackTraceElement.toString().startsWith("sun.") && !stackTraceElement.toString().startsWith("java.lang")) {
                builder.append(stackTraceElement.toString() + "\n");
            }
        }
        // Varchar(4000) en base.
        if (builder.length() > 4000) {
            retour = builder.substring(0, 4000);
        }
        else {
            retour = builder.toString();
        }
        return retour;
    }

    /**
     * Décodage base64 (org.apache.commons-codec).
     *
     * @param base64
     *            the base64
     * @return the string
     */
    public static String base64ToString(String base64) throws Exception{ // NO_UCD (unused code)
        String result = null;
        if (!isEmpty(base64)) {
            try {
                final byte[] bytes = Base64.decodeBase64(base64);
                result = new String(bytes);
            }
            catch (final Exception e) {
                throw e;
            }
        }
        return result;
    }

    /**
     * Encodage base64 (org.apache.commons-codec). Attention, la classe 'Apache' ajoute '\r\n' en fin de chaine. Ce n'etait pas le cas avec les classes de Sun.
     * Cette difference n'est pas importante quand on encode des fichiers images en base 64 Mais elle est cruciale quand il s'agit de mot de passe !!! Nous
     * utilisons donc la methode trimToNull pour enlever les caractères d'espacement en debut ou fin de chaine.
     *
     * @param bytes
     *            the bytes
     * @return the string
     */
    public static String bytesToBase64(byte[] bytes) throws Exception{
        String result = null;
        if (bytes != null && bytes.length > 0) {
            try {
                result = trimToNull(Base64.encodeBase64String(bytes));
            }
            catch (final Exception e) {
                throw e;
            }
        }
        return result;
    }

    /**
     * Retourne la comparaison entre 2 Strings. Les Strings vides et Null peuvent ?tre mises en d?but ou en fin de liste.
     *
     * @param valeur1
     *            Premi?re valeur ? comparer.
     * @param valeur2
     *            Seconde valeur ? comparer.
     * @param nullEtVideEnPremier
     *            Faut-il mettre les null et vide en premier ?
     * @return inf?rieur, ?gal ou sup?rieur ? 0 selon la r?gle des m?thodes standard "compareTo".
     */
    public static int compare(String valeur1, String valeur2, boolean nullEtVideEnPremier) {
        return ObjectUtils.compare(StringUtils.trimToNull(valeur1), StringUtils.trimToNull(valeur2), nullEtVideEnPremier);
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
    public static String concat(String a, String b) { // NO_UCD (use private)
        return trimToBlank(a) + trimToBlank(b);
    }

    /**
     * Concatène deux tableaux. Deux tableaux <code>null</code> sont remplacées par une tableau vide.
     *
     * @param s1
     *            the s1
     * @param s2
     *            the s2
     * @return the string[]
     */
    public static String[] concatArrays(String[] s1, String[] s2) { // NO_UCD (unused code)
        String[] s = new String[0];
        if (s1 == null && s2 == null) {
            return s;
        }
        final int taille1 = (s1 == null) ? 0 : s1.length;
        final int taille2 = (s2 == null) ? 0 : s2.length;
        final int taille = taille1 + taille2;
        s = new String[taille];
        if (s1 != null) {
            for (int i = 0; i < s1.length; i++) {
                s[i] = s1[i];
            }
        }
        if (s2 != null) {
            for (int i = 0; i < s2.length; i++) {
                s[taille1 + i] = s2[i];
            }
        }
        return s;
    }

    /**
     * Recherche si une chaine de mots separes par un delimiteur contient un element donne. le delimiteur est donne a l'aide d'une expression reguliere. Par
     * exemple : <code>contains("banane, pomme; orange","[ ]*[,;][ ]*","pomme") = true</code>
     *
     * @param str
     *            the str
     * @param regexDelim
     *            the regex delim
     * @param searchString
     *            the search string
     * @return true, if contains
     */
    public static boolean contains(String str, String regexDelim, String searchString) { // NO_UCD (unused code)
        final List<String> tokens = split(str, regexDelim);
        if ((tokens != null) && (tokens.size() > 0)) {
            return tokens.contains(searchString);
        }
        return false;
    }

    /**
     * Contains all.
     *
     * @param str
     *            the str
     * @param list
     *            the list
     * @param caseSensitive
     *            the case sensitive
     * @return true, if successful
     */
    public static boolean containsAll(String str, List<String> list, boolean caseSensitive) {
        if (!caseSensitive) {
            str = str.toUpperCase();
        }

        Integer compteur = 0;
        for (String string : list) {
            if (str.contains(string)) {
                compteur++;
            }
        }
        return compteur == list.size();
    }

    /**
     * Default if empty.
     *
     * @param text
     *            the text
     * @param def
     *            the def
     * @return the string
     */
    public static String defaultIfEmpty(String text, String def) { // NO_UCD (unused code)
        return StringUtils.isEmpty(text) ? def : text;
    }

    /**
     * Effectue le traitement de remplacement des caractères accentués en caractères non accentués.
     *
     * @param chaine
     *            Chaine à convertir sans accent.
     * @return Chaine dont les accents ont été supprimé.
     */
    public static String effectueSansAccent(final String chaine) { // NO_UCD (use private)
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < chaine.length(); i++) {
            final char carVal = chaine.charAt(i);
            ajouteSansAccent(result, carVal);
        }
        return result.toString();
    }

    /**
     * Enlever apostrophe.
     *
     * @param champ
     *            the champ
     * @return the string
     */
    public static String enleverApostrophe(String champ) { // NO_UCD (unused code)
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < champ.length(); i++) {
            final char lettre = champ.charAt(i);
            if (lettre == '\'') {
                buf.append(" ");
            }
            else {
                buf.append(lettre);
            }
        }
        return buf.toString();
    }

    /**
     * Compare deux chaines de caractères.
     *
     * @param str1
     *            : première chaine
     * @param str2
     *            : deuxième chaine
     * @return : true si égal sinon false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    /**
     * Escape all non alpha num.
     *
     * @param text
     *            the text
     * @return the string
     */
    public static String escapeAllNonAlphaNum(String text) {
        return !StringUtils.isEmpty(text) ? text.replaceAll("[^A-Za-z0-9àâäôöïîûùüèéêëç'\\- ]", "") : "";
    }

    /**
     * Escape html comments.
     *
     * @param text
     *            the text
     * @return the string
     */
    public static String escapeHTMLComments(String text) { // NO_UCD (unused code)
        return !StringUtils.isEmpty(text) ? text.replaceAll("(?s)<!--\\[if(.*?)\\[endif\\] *-->", "") : "";
    }

    /**
     * Indique si la chaine1 finit par la chaine 2.
     *
     * @param chaine1
     *            the chaine1
     * @param chaine2
     *            the chaine2
     * @return the boolean
     */
    public static Boolean finieWith(String chaine1, String chaine2) { // NO_UCD (unused code)
        return (chaine1 != null) ? chaine1.endsWith(chaine2) : false;
    }

    /**
     * Retourne la valeur avec une majuscule au début.
     *
     * @param valeur
     *            valeur à formater.
     * @return valeur formattée.
     */
    public static String formatagePremiereLettreMajuscule(String valeur) {
        if (StringUtils.isEmpty(valeur)) {
            return valeur;
        }
        if (valeur.length() > 1) {
            return valeur.substring(0, 1).toUpperCase() + valeur.substring(1);
        }
        else { // if (valeur.length()>0) {
            return valeur.toUpperCase();
        }
    }

    /**
     * Retourne la valeur avec une majuscule au début.
     *
     * @param valeur
     *            valeur à formater.
     * @return valeur formattée.
     */
    public static String formatageSeulementPremiereLettreMajuscule(String valeur) { // NO_UCD (unused code)
        if (StringUtils.isEmpty(valeur)) {
            return valeur;
        }
        if (valeur.length() > 1) {
            return valeur.substring(0, 1).toUpperCase() + valeur.substring(1).toLowerCase();
        }
        else { // if (valeur.length()>0) {
            return valeur.toUpperCase();
        }
    }

    /**
     * Permet de formatter une chaine de recherche en une liste de mot clé en supprimant tous les mots de moins de 2 caracteres et les listes suivantes :
     * LIST_ARTICLES / LIST_CONJONCTIONS / LIST_PROPOSITIONS / LIST_PRONOMS_PERSOS / LIST_PRONOMS_RELATIFS.
     *
     * @param chaine
     *            the chaine
     * @return the list
     */
    public static List<String> formatChaineDeRechercheEnMotCle(String chaine) {
        final List<String> listMotCleFinal = new ArrayList<String>();
        // String aAccent = "[aàâä]";
        // String cAccent = "[cç]";
        // String eAccent = "[eéèêë]";
        // String iAccent = "[iîï]";
        // String oAccent = "[oôö]";
        // String uAccent = "[uùûü]";

        // on extrait tous les mots de la chaine de recherche.
        final List<String> listMotCle = split(chaine, " ");

        // Recherche des mots qui sont inutiles pour la recherche.
        String motCle = "";
        for (String motCleT : listMotCle) {
            motCle = escapeAllNonAlphaNum(motCleT); // Suppression de la ponctuation
            motCle = trimToNull(motCle);
            if (motCle != null) {
                motCle = motCle.toLowerCase(); // On travaille en minuscule
                // Verification si le mot commence par un article avec un '
                for (final String apostrophe : LIST_APOSTROPHE) {
                    if (motCle.startsWith(apostrophe)) {
                        // On supprime l'article en question
                        motCle = motCle.replace(apostrophe, "");
                    }
                }

                if (!LIST_ARTICLES.contains(motCle) && !LIST_CONJONCTIONS.contains(motCle) && !LIST_PREPOSITIONS.contains(motCle) && !LIST_PRONOMS_PERSOS.contains(motCle) && !LIST_PRONOMS_RELATIFS.contains(motCle) && !LIST_APOSTROPHE.contains(motCle) && !LIST_ADJECTIFS_POSSESSIFS.contains(motCle)
                    && !LIST_PRONOMS_DEMONSTRATIFS.contains(motCle) && !LIST_PRONOMS_POSSESIFIFS.contains(motCle) && !LIST_PRONOMS_INDEFINIS.contains(motCle)) {

                    if (motCle.contains("'")) {
                        motCle = motCle.replace("'", "''");
                    }

                    // Remplacement des accents pour la recherche, si l'accent saisi est mauvais.
                    // char[] listeCaractere = motCle.toCharArray();
                    // for (int i = 0; i < listeCaractere.length; i++) {
                    // if ( LIST_ACCENT_A.contains(listeCaractere[i]) ){
                    // motCle = motCle.replace("a", aAccent);
                    // break;
                    // }
                    //
                    // if ( LIST_ACCENT_C.contains(listeCaractere[i]) ){
                    // motCle = motCle.replace("c", cAccent);
                    // break;
                    // }
                    //
                    // if ( LIST_ACCENT_E.contains(listeCaractere[i]) ){
                    // motCle = motCle.replace("e", eAccent);
                    // break;
                    // }
                    //
                    // if ( LIST_ACCENT_I.contains(listeCaractere[i]) ){
                    // motCle = motCle.replace("i", iAccent);
                    // break;
                    // }
                    //
                    // if ( LIST_ACCENT_O.contains(listeCaractere[i]) ){
                    // motCle = motCle.replace("o", oAccent);
                    // break;
                    // }
                    //
                    // if ( LIST_ACCENT_U.contains(listeCaractere[i]) ){
                    // motCle = motCle.replace("u", uAccent);
                    // break;
                    // }
                    //
                    // }

                    listMotCleFinal.add(motCle);
                }
            }
        }
        return listMotCleFinal;
    }

    /**
     * Permet de formatter une chaine en minuscule et sans accent.
     *
     * @param chaine
     *            the chaine
     * @return chaine formatée
     */
    public static String formatChaineToMinusculeSansAccent(String chaine) {

        // on extrait tous les mots de la chaine de recherche.
        String phrase = sansAccent(chaine.trim().toLowerCase());
        return phrase;
    }

    /**
     * Recherche la correspondance des EL si présent.
     *
     * @param pValue
     *            the value
     * @return la même chaine avec les EL résolues
     */
    public static String formatELResolve(String pValue) { // NO_UCD (unused code)
        String vValue = pValue;
        if (vValue == null || vValue.length() == 0) {
            return "";
        }
        else {
            int posDebEL = vValue.indexOf("${");
            while (posDebEL > -1) {
                final int posEndEL = vValue.indexOf("}", posDebEL + 2);
                if (posEndEL > -1) {
                    CharSequence remplace;
                    try {
                        remplace = new StringBuilder(System.getProperty(vValue.subSequence(posDebEL + 2, posEndEL).toString()));
                    }
                    catch (final Exception e) {
                        remplace = new StringBuilder();
                    }
                    vValue = vValue.replace(vValue.subSequence(posDebEL, posEndEL + 1), remplace);
                    posDebEL = vValue.indexOf("${", posDebEL + remplace.length());
                }
                else {
                    posDebEL = -1;
                }
            }
            return vValue;
        }
    }

    /**
     * Les chaines qui ont un format date sont tranformées par Excel avec inversion jour/mois (Cas des éditions CSV). Cette méthode vérifie si la chaine est une
     * date et la modife en mm/jj/aaaa.
     *
     * @param chaine
     *            the chaine
     * @return chaine formatée si de type date
     */
    public static String formatIfCSVDate(String chaine) { // NO_UCD (unused code)
        if (chaine.matches("[0-9]{2}/[0-2]{2}/[0-9]{4}")) {
            final String ch = chaine.substring(3, 5) + "/" + chaine.substring(0, 2) + "/" + chaine.substring(6, 10);
            return ch;
        }
        return chaine;
    }

    /**
     * Formatage d'un numéro sur n caractères, les premiers contenant des zéros.
     *
     * @param nbCars
     *            le nombre de caractères 0.
     * @param numero
     *            le chiffre
     * @return le numéro formaté.
     */
    public static String formatNumber(int nbCars, int numero) { // NO_UCD (unused code)
        final String numStr = Integer.valueOf(numero).toString();
        final int numSize = numStr.length();
        if (numSize >= nbCars) {
            return numStr.substring(0, nbCars);
        }
        final StringBuilder buf = new StringBuilder();
        for (int i = 1; i <= (nbCars - numSize); i++) {
            buf.append("0");
        }
        buf.append(numStr);
        return buf.toString();
    }

    /**
     * Format requete.
     *
     * @param champ
     *            the champ
     * @return the string
     */
    public static String formatRequete(String champ) { // NO_UCD (unused code)
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < champ.length(); i++) {
            final char lettre = champ.charAt(i);
            if (lettre == '\'') {
                buf.append(lettre);
            }
            buf.append(lettre);
        }

        return buf.toString();
    }

    /**
     * Retourne une chaine en formattant les symboles de retour chariot (\n) par des vrais retour chariot.
     *
     * @param valeur
     *            valeur
     * @return valeur avec retour chariot.
     */
    public static String formatRetourChariot(String valeur) { // NO_UCD (unused code)
        if (StringUtils.isEmpty(valeur)) {
            return valeur;
        }
        return valeur.replace("\\n", "\n");
    }

    /**
     * Formattage d'une chaine (comme dans les logs).
     *
     * @param msg
     *            msg
     * @param args
     *            args
     * @return chaine formatée.
     */
    public static String formatString(String msg, Object... args) {
        return MessageFormat.format(msg.replaceAll("'", "''"), args);
    }

    /**
     * Initialisation de la liste de correspondance entre les caractères accentués et leur homologues non accentués.
     *
     * @return liste de correspondances
     */
    private static ArrayList<String> initCorrespondance() {
        final ArrayList<String> result = new ArrayList<String>();
        String car = null;

        car = "A";
        result.add(car); /* '\u00C0' À alt-0192 */
        result.add(car); /* '\u00C1' Á alt-0193 */
        result.add(car); /* '\u00C2' Â alt-0194 */
        result.add(car); /* '\u00C3' Ã alt-0195 */
        result.add(car); /* '\u00C4' Ä alt-0196 */
        result.add(car); /* '\u00C5' Å alt-0197 */
        car = "AE";
        result.add(car); /* '\u00C6' Æ alt-0198 */
        car = "C";
        result.add(car); /* '\u00C7' Ç alt-0199 */
        car = "E";
        result.add(car); /* '\u00C8' È alt-0200 */
        result.add(car); /* '\u00C9' É alt-0201 */
        result.add(car); /* '\u00CA' Ê alt-0202 */
        result.add(car); /* '\u00CB' Ë alt-0203 */
        car = "I";
        result.add(car); /* '\u00CC' Ì alt-0204 */
        result.add(car); /* '\u00CD' Í alt-0205 */
        result.add(car); /* '\u00CE' Î alt-0206 */
        result.add(car); /* '\u00CF' Ï alt-0207 */
        car = "D";
        result.add(car); /* '\u00D0' Ð alt-0208 */
        car = "N";
        result.add(car); /* '\u00D1' Ñ alt-0209 */
        car = "O";
        result.add(car); /* '\u00D2' Ò alt-0210 */
        result.add(car); /* '\u00D3' Ó alt-0211 */
        result.add(car); /* '\u00D4' Ô alt-0212 */
        result.add(car); /* '\u00D5' Õ alt-0213 */
        result.add(car); /* '\u00D6' Ö alt-0214 */
        car = "*";
        result.add(car); /* '\u00D7' × alt-0215 */
        car = "0";
        result.add(car); /* '\u00D8' Ø alt-0216 */
        car = "U";
        result.add(car); /* '\u00D9' Ù alt-0217 */
        result.add(car); /* '\u00DA' Ú alt-0218 */
        result.add(car); /* '\u00DB' Û alt-0219 */
        result.add(car); /* '\u00DC' Ü alt-0220 */
        car = "Y";
        result.add(car); /* '\u00DD' Ý alt-0221 */
        car = "Þ";
        result.add(car); /* '\u00DE' Þ alt-0222 */
        car = "B";
        result.add(car); /* '\u00DF' ß alt-0223 */
        car = "a";
        result.add(car); /* '\u00E0' à alt-0224 */
        result.add(car); /* '\u00E1' á alt-0225 */
        result.add(car); /* '\u00E2' â alt-0226 */
        result.add(car); /* '\u00E3' ã alt-0227 */
        result.add(car); /* '\u00E4' ä alt-0228 */
        result.add(car); /* '\u00E5' å alt-0229 */
        car = "ae";
        result.add(car); /* '\u00E6' æ alt-0230 */
        car = "c";
        result.add(car); /* '\u00E7' ç alt-0231 */
        car = "e";
        result.add(car); /* '\u00E8' è alt-0232 */
        result.add(car); /* '\u00E9' é alt-0233 */
        result.add(car); /* '\u00EA' ê alt-0234 */
        result.add(car); /* '\u00EB' ë alt-0235 */
        car = "i";
        result.add(car); /* '\u00EC' ì alt-0236 */
        result.add(car); /* '\u00ED' í alt-0237 */
        result.add(car); /* '\u00EE' î alt-0238 */
        result.add(car); /* '\u00EF' ï alt-0239 */
        car = "d";
        result.add(car); /* '\u00F0' ð alt-0240 */
        car = "n";
        result.add(car); /* '\u00F1' ñ alt-0241 */
        car = "o";
        result.add(car); /* '\u00F2' ò alt-0242 */
        result.add(car); /* '\u00F3' ó alt-0243 */
        result.add(car); /* '\u00F4' ô alt-0244 */
        result.add(car); /* '\u00F5' õ alt-0245 */
        result.add(car); /* '\u00F6' ö alt-0246 */
        car = "/";
        result.add(car); /* '\u00F7' ÷ alt-0247 */
        car = "0";
        result.add(car); /* '\u00F8' ø alt-0248 */
        car = "u";
        result.add(car); /* '\u00F9' ù alt-0249 */
        result.add(car); /* '\u00FA' ú alt-0250 */
        result.add(car); /* '\u00FB' û alt-0251 */
        result.add(car); /* '\u00FC' ü alt-0252 */
        car = "y";
        result.add(car); /* '\u00FD' ý alt-0253 */
        car = "þ";
        result.add(car); /* '\u00FE' þ alt-0254 */
        car = "y";
        result.add(car); /* '\u00FF' ÿ alt-0255 */
        result.add(car); /* '\u00FF' alt-0255 */

        return result;
    }

    /**
     * Insère un espace à une certaines frequence et dans un sens donné.<br/>
     * - de droite à gauche (pour les chiffres : 1000123 devient 1 000 123) par defaut<br/>
     *
     * @param chaine
     *            the chaine
     * @param pos
     *            the pos
     * @return the string
     */
    public static String insertSpace(String chaine, byte pos) { // NO_UCD (unused code)
        // org.apache.commons.codec.binary.StringUtils;
        // org.apache.commons.lang.StringUtils;
        return chaine;
    }

    /**
     * insert un espace après n charateres.
     *
     * @param string
     *            the string
     * @param length
     *            the length
     * @return the string
     */
    public static String insertSpace(String string, Integer length) { // NO_UCD (unused code)
        final String valeur = trimToBlank(string);
        final int index = valeur.length() > length ? length : -1;
        return index != -1 ? (valeur.substring(0, index) + " " + valeur.substring(index, valeur.length())) : valeur;
    }

    /**
     * Teste si une chaîne est <code>null</code>, vide ou ne contient que des espaces.
     *
     * @param str
     *            the str
     * @return true, if is empty
     */
    public static boolean isEmpty(String str) {
        return trimToNull(str) == null;
    }

    /**
     * Methode : isIn.
     *
     * @param name
     *            : la chaine a trouver.
     * @param valeurs
     *            : la chaine où l'on cherche.
     * @param separateur
     *            : le séparateur.
     * @return : vrai ou faux
     */
    public static boolean isIn(final String name, String valeurs, String separateur) { // NO_UCD (unused code)
        if (StringUtils.trimToNull(name) != null && StringUtils.trimToNull(valeurs) != null) {
            final StringTokenizer st = new StringTokenizer(valeurs, separateur);
            while (st.hasMoreTokens()) {
                if (st.nextToken().equals(name)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Indique si une chaine ne contient que des chiffres.
     *
     * @param chaine
     *            : La chaine à vérifier
     * @return true si aucune lettre n'est détecté, sinon false
     */
    public static boolean isNumber(String chaine) {

        for (int i = 0; i < chaine.length(); i++) {
            if (!Character.isDigit(chaine.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Appel au Stringutils d'apache.
     *
     * @param chaine
     *            the chaine
     * @return true, if is numerique only
     */
    public static boolean isNumeriqueOnly(String chaine) {
        return org.apache.commons.lang.StringUtils.isNumeric(chaine);
    }

    /**
     * Retourne la concat?nation des chaines d'une liste en utilisant le d?limiteur indiqu?. Exemple : join(["A", "B", "CD"], "/") --> "A/B/CD"
     *
     * @param liste
     *            la liste des chaines ? concat?ner.
     * @param delim
     *            le d?limiteur ? ins?rer entre chaque valeur.
     * @return la chaine contenant les valeurs concat?n?es.
     */
    public static String join(List<String> liste, String delim) { // NO_UCD (unused code)
        boolean mettreSeparateur = false;
        final StringBuilder builder = new StringBuilder();
        if (liste != null) {
            for (final String val : liste) {
                if (mettreSeparateur) {
                    builder.append(delim);
                }
                builder.append(val);
                mettreSeparateur = true;
            }
        }
        return builder.toString();
    }

    /**
     * Join list integer.
     *
     * @param liste
     *            the liste
     * @param delim
     *            the delim
     * @return the string
     */
    public static String joinListInteger(List<Integer> liste, String delim) { // NO_UCD (unused code)
        boolean mettreSeparateur = false;
        final StringBuilder builder = new StringBuilder();
        if (liste != null) {
            for (final Integer val : liste) {
                if (mettreSeparateur) {
                    builder.append(delim);
                }
                builder.append(val);
                mettreSeparateur = true;
            }
        }
        return builder.toString();
    }

    /**
     * Retourne la distance de levenshtein entre deux mots.
     *
     * @param a
     *            the a
     * @param b
     *            the b
     * @return the int
     */
    public static int levenshtein(String a, String b) { // NO_UCD (unused code)
        return org.apache.commons.lang.StringUtils.getLevenshteinDistance(a, b);
    }

    /**
     * Génère n espaces HTML non "breakable".
     *
     * @param nb
     *            the nb
     * @return the string
     */
    public static String nbsp(Integer nb) { // NO_UCD (unused code)
        Integer nbr = nb;
        if (nbr == null) {
            nbr = 1;
        }
        final StringBuilder buf = new StringBuilder();
        for (int i = 1; i <= nbr.intValue(); i++) {
            buf.append("&#160;");
        }
        return buf.toString();
    }

    /**
     * Normalize.
     *
     * @param pValue
     *            the value
     * @return the string
     */
    public static String normalize(String pValue) { // NO_UCD (use private)
        String vValue = StringUtils.sansAccent(pValue);
        vValue = vValue.replaceAll("@", "a");
        vValue = vValue.replaceAll("\\W", " ");
        vValue = StringUtils.stripSpacesSentence(vValue);
        vValue = vValue.trim();
        vValue = vValue.replaceAll(" ", "-");
        return vValue;
    }

    /**
     * Normalize url.
     *
     * @param pUrl
     *            the url
     * @return the string
     */
    public static String normalizeUrl(String pUrl) { // NO_UCD (unused code)
        String vUrl = StringUtils.normalize(pUrl);
        return vUrl.toLowerCase();
    }

    /**
     * Ajoute autant de 'caractere' au début de la chaine 'valeur' pour obtenir la taille 'tailleTotale'.
     *
     * @param valeur
     *            chaine de base
     * @param tailleTotale
     *            taille totale de la chaine de retour
     * @param caractere
     *            caractère à ajouter
     * @return la chaine avec les caractères ajoutés.
     */
    public static String padLeft(String valeur, int tailleTotale, char caractere) {
        final StringBuilder retour = new StringBuilder();
        final String valeurPasNull = StringUtils.trimToBlank(valeur);
        for (int i = valeurPasNull.length(); i < tailleTotale; i++) {
            retour.append(caractere);
        }
        if (valeurPasNull.length() <= tailleTotale) {
            retour.append(valeurPasNull);
        }
        else {
            retour.append(valeurPasNull, 0, tailleTotale);
        }

        return retour.toString();
    }

    /**
     * Ajoute autant de 'caractere' à la fin de la chaine 'valeur' pour obtenir la taille 'tailleTotale'.
     *
     * @param valeur
     *            chaine de base
     * @param tailleTotale
     *            taille totale de la chaine de retour
     * @param caractere
     *            caractère à ajouter
     * @return la chaine avec les caractères ajoutés.
     */
    public static String padRight(String valeur, int tailleTotale, char caractere) { // NO_UCD (unused code)
        final StringBuilder retour = new StringBuilder();
        final String valeurPasNull = StringUtils.trimToBlank(valeur);
        if (valeurPasNull.length() <= tailleTotale) {
            retour.append(valeurPasNull);
        }
        else {
            retour.append(valeurPasNull, 0, tailleTotale);
        }

        for (int i = retour.length(); i < tailleTotale; i++) {
            retour.append(caractere);
        }
        return retour.toString();
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
    public static String prefixIfNotNull(String a, String b) { // NO_UCD (unused code)
        final String trimToBlank = trimToBlank(b);
        if (!"".equals(trimToBlank)) {
            return trimToBlank(a) + trimToBlank;
        }
        return "";
    }

    /**
     * Retourne la valeur avec une minuscule au début.
     *
     * @param valeur
     *            valeur à formater.
     * @return valeur formattée.
     */
    // TODO from UCDetector: Method "StringUtils.premLettMin(String)" is only called from tests
    public static String premLettMin(String valeur) { // NO_UCD (test only)
        if (StringUtils.isEmpty(valeur)) {
            return valeur;
        }
        if (valeur.length() > 1) {
            return valeur.substring(0, 1).toLowerCase() + valeur.substring(1);
        }
        else { // if (valeur.length()>0) {
            return valeur.toLowerCase();
        }
    }

    /**
     * Génère une chaîne de 32 caractères aléatoires.
     *
     * @param longueur
     *            la longueur de la chaine
     * @return string longueur
     */
    public static String randomString(Integer longueur) {
        String gen = "";
        StringBuilder sb = new StringBuilder("abcdefghijklmnopqrstuvwxyz0123456789&é-è_çà=ùû$£¤ï@#");
        for (Integer i = 0; i <= longueur; i++) {
            gen += sb.charAt(MathUtils.arrondirVersEntierInferieur(Math.random() * sb.length()));
        }
        return gen;
    }

    /**
     * Remplacer apostrophe.
     *
     * @param champ
     *            the champ
     * @return the string
     */
    public static String remplacerApostrophe(String champ) { // NO_UCD (unused code)
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < champ.length(); i++) {
            final char lettre = champ.charAt(i);
            if (lettre == '\'') {
                buf.append("' ");
            }
            else {
                buf.append(lettre);
            }
        }
        return buf.toString();
    }

    /**
     * Remplace ces caractères spéciaux : ’ en ' ‘ en ' « en " » en ". <br/>
     * <br/>
     * NB : cette méthode etait utile quand la base n'était pas encodée en UTF-8 <br/>
     * NB2 : pour verifier le charset utilisé par la base de données (Oracle) :<br/>
     * select * from <font color=red>nls_database_parameters</font> <br/>
     * Le charset utilisé par votre client :<br/>
     * select * from <font color=red>nls_session_parameters</font>
     *
     * @param param
     *            the chaine
     * @return the string
     */
    public static String replaceSpecialsChars(String param) { // NO_UCD (unused code)
        if (isEmpty(param)) {
            return param;
        }
        String chaine = param;
        chaine = chaine.replaceAll("‘", "'");
        chaine = chaine.replaceAll("’", "'");
        chaine = chaine.replaceAll("«", "\"");
        chaine = chaine.replaceAll("»", "\"");
        return chaine;
    }

    /**
     * Similaire au replaceSpecialChars mais uniquement pour les "_".
     *
     * @param param
     *            the param
     * @return the string
     */
    public static String replaceUnderscore(String param) { // NO_UCD (unused code)
        if (isEmpty(param)) {
            return param;
        }
        String chaine = param;
        chaine = chaine.replaceAll("_", ""); // efface les '_' underscore
        return chaine;
    }

    /**
     * Reverse.
     *
     * @param value
     *            the value
     * @return the string
     */
    public static String reverse(String value) {
        if (isEmpty(value)) {
            return value;
        }
        else {
            final StringBuilder result = new StringBuilder();
            for (int i = value.length() - 1; 0 <= i; i--) {
                result.append(value.charAt(i));
            }
            return result.toString();
        }
    }

    /**
     * Transforme une chaine pouvant contenir des accents dans une version sans accent.
     *
     * @param chaine
     *            Chaine à convertir sans accent.
     * @return Chaine en majuscule dont les accents ont été supprimé.
     */
    public static String sansAccent(final String chaine) {
        if (StringUtils.isEmpty(chaine)) {
            return chaine;
        }
        return effectueSansAccent(chaine);
    }

    /**
     * Transforme une chaine pouvant contenir des accents dans une version sans accent en majuscule.
     *
     * @param chaine
     *            Chaine a convertir sans accent.
     * @return Chaine dont les accents ont été supprimé.
     */
    public static String sansAccentEnMajuscule(final String chaine) {
        if (StringUtils.isEmpty(chaine)) {
            return chaine;
        }
        return effectueSansAccent(chaine).toUpperCase();
    }

    /**
     * D?coupe une cha?ne de caract?res suivant un d?limiteur.
     *
     * @param str
     *            the str
     * @param regexDelim
     *            the regex delim
     * @return the list< string>
     */
    public static List<String> split(String str, String regexDelim) {
        return Arrays.asList(str.split(regexDelim));
    }

    /**
     * D?coupe une cha?ne de caract?res suivant un d?limiteur, avec une utilisation du délimiteur limitée à "limitUseRegex" fois.
     *
     * @param str
     *            the str
     * @param regexDelim
     *            the regex delim
     * @param limitUseRegex
     *            limit use regex
     * @return the list< string>
     */
    public static List<String> split(String str, String regexDelim, int limitUseRegex) { // NO_UCD (unused code)
        return Arrays.asList(str.split(regexDelim, limitUseRegex));
    }

    /**
     * Formate le nom SQL d'une table vers son nom HQL.
     *
     * @param nomTable
     *            the nom table
     * @return the string
     */
    public static String sqlVersHql(final String nomTable) { // NO_UCD (unused code)
        String nomFinal = nomTable;
        if (nomFinal.contains("_")) {
            // on splitte suivant l'underscore
            final List<String> l = StringUtils.split(nomFinal, "_");
            final StringBuilder buf = new StringBuilder();
            for (final String mot : l) {
                buf.append(StringUtils.formatagePremiereLettreMajuscule(mot.toLowerCase()));
            }
            nomFinal = buf.toString();
        }
        else {
            nomFinal = StringUtils.formatagePremiereLettreMajuscule(nomFinal.toLowerCase());
        }
        return nomFinal;
    }

    /**
     * Indique si la chaine1 commence par la chaine2.
     *
     * @param chaine1
     *            Chaine à tester
     * @param chaine2
     *            Chaine qui doit être contenu
     * @return Si la chaine1 commence par la chaine2
     */
    public static Boolean startWith(String chaine1, String chaine2) {
        return (chaine1 != null) ? chaine1.startsWith(chaine2) : false;
    }

    /**
     * Encodage par sun misc base64.
     *
     * @param str
     *            the str
     * @return the string
     */
    public static String stringToBase64(String str) throws Exception { // NO_UCD (unused code)
        String result = null;
        if (!isEmpty(str)) {
            result = bytesToBase64(str.getBytes());
        }
        return result;
    }

    /**
     * Transforme une chaine en liste de lettres.
     *
     * @param lettres
     *            the lettres
     * @return the list< string>
     */
    public static List<String> stringToList(String lettres) { // NO_UCD (unused code)
        final List<String> listeRetour = new ArrayList<String>();
        for (int i = 0; i < lettres.length(); i++) {
            String carac = "";
            carac += lettres.charAt(i);
            listeRetour.add(carac);
        }
        return listeRetour;
    }

    /**
     * Methode : stringToMap.
     *
     * @param valeurs
     *            .
     * @param separateur1
     *            .
     * @param separateur2
     *            .
     * @return :
     */
    public static Map<String, String> stringToMap(String valeurs, String separateur1, String separateur2) { // NO_UCD (unused code)
        final Map<String, String> mapFormation = new HashMap<String, String>();
        if (valeurs != null) {
            final List<String> listeFormation = StringUtils.split(valeurs, separateur1);
            for (final String formation : listeFormation) {
                final String[] tab = formation.split(separateur2);
                if (tab.length > 1) {
                    mapFormation.put(tab[0], tab[1]);
                }
            }
        }
        return mapFormation;
    }

    /**
     * Suppression de tous les espaces dans la chaine de caracteres fournie.
     *
     * @param chaine
     *            the chaine
     * @return the string
     */
    public static String stripSpaces(String chaine) {
        return (chaine != null) ? chaine.replaceAll("\\s", "") : null;
    }

    /**
     * Suppression de tous les espaces superflus dans la chaîne de caractères fournie, représentant une phrase.
     *
     * @param chaine
     *            la chaîne
     * @return une phrase où une chaîne vide(si la chqîne est vide ou nulle).
     */
    public static String stripSpacesSentence(String chaine) { // NO_UCD (use private)
        return (chaine != null) ? trimToBlank(chaine.replaceAll("\\s+", " ")) : "";
    }

    /**
     * Retourne une nouvelle chaîne tronquée à partir de l'indice "beginIndex" de la chaîne, d'une longueur définie par "lenght". Si l'indice est inférieure à
     * 0, l'indice est remplacé par 0. Si la longueur est inférieure à 1, la longueur est remplacée par 1. Si l'indice est supérieur au dernier indice de la
     * chaîne, l'indice est remplacé par indice.length(). Si la longueur est supérieure à la longueur entre l'indice et la longueur de la chaîne, la longueur
     * est remplacée par "longueur de la chaîne - indice).
     *
     * @param string
     *            the string
     * @param beginIndex
     *            the begin index
     * @param length
     *            the length
     * @return the string
     */
    public static String subString(String string, Integer beginIndex, Integer length) {
        String newString = string;
        Integer newBeginIndex = beginIndex;
        Integer newLength = length;
        if (StringUtils.isEmpty(string)) {
            newString = EMPTY_STRING;
        }
        else {
            if (beginIndex == null || beginIndex.intValue() < 0) {
                newBeginIndex = 0;
            }
            else if (beginIndex.intValue() > string.length()) {
                newBeginIndex = string.length();
            }
            if (length == null || length.intValue() < 1) {
                newLength = 1;
            }
            else if ((newBeginIndex.intValue() + length) > string.length()) {
                newLength = string.length() - newBeginIndex.intValue();
            }
            newString = newString.substring(newBeginIndex, newBeginIndex + newLength);
        }
        return newString;
    }

    /**
     * Transforme un tableau de cha?nes de caract?res en un tableau de <code>int</code>. Si le param?tre <code>values</code> vaut <code>null</code>, un tableau
     * vide est renvoy?.
     *
     * @param values
     *            the values
     * @return the int[]
     */
    public static int[] toInt(String[] values) { // NO_UCD (unused code)
        if (values == null) {
            return new int[0];
        }
        final int len = values.length;
        final int[] intValues = new int[len];
        for (int i = 0; i < len; ++i) {
            intValues[i] = Integer.parseInt(values[i]);
        }
        return intValues;
    }

    /**
     * Transforme un tableau de cha?nes de caract?res en un tableau de <code>long</code>. Si le param?tre <code>values</code> vaut <code>null</code>, un tableau
     * vide est renvoy?.
     *
     * @param values
     *            the values
     * @return the long[]
     */
    public static long[] toLong(String[] values) { // NO_UCD (unused code)
        if (values == null) {
            return new long[0];
        }
        final int len = values.length;
        final long[] longValues = new long[len];
        for (int i = 0; i < len; ++i) {
            longValues[i] = Long.parseLong(values[i]);
        }
        return longValues;
    }

    /**
     * Transforme une chaine de caractère en majuscule.
     *
     * @param chaine
     *            Chaine a convertir.
     * @return Chaine en majuscule.
     */
    public static String toUpperCase(final String chaine) {
        if (StringUtils.isEmpty(chaine)) {
            return chaine;
        }
        return chaine.toUpperCase();
    }

    /**
     * Supprime les caracteres d'espacement (espace, tabulation, etc...) en debut et en fin de chaine. Si le résultat est une chaine vide, ou si la chaine en
     * entrée vaut <code>null</code>, une chaine vide est renvoyée.
     *
     * @param str
     *            the str
     * @return the string
     */
    public static String trimToBlank(String str) {
        final String newStr = trimToNull(str);
        return (newStr != null) ? newStr : EMPTY_STRING;
    }

    /**
     * Supprime les caractères d'espacement (espace, tabulation, etc...) en début et en fin de chaîne. Si le résultat est une chaîne vide, ou si la chaîne en
     * entrée vaut <code>null</code>, la valeur <code>null</code> est renvoyée. Attention, quand un integer vaut null et que l'on envoie un String.valueOf de
     * cet<br>
     * Integer, le string n'est pas null mais contient 'null' .......!
     *
     * @param str
     *            the str
     * @return the string
     */
    public static String trimToNull(String str) {
        if (str == null || str.equals("null")) {
            return null;
        }
        final String newStr = str.trim();

        return (newStr.length() == 0) ? null : newStr;
    }

    /**
     * Truncate.
     *
     * @param string
     *            the string
     * @param length
     *            the length
     * @return the string
     */
    public static String truncate(String string, Integer length) { // NO_UCD (unused code)
        return truncate(string, length, EMPTY_STRING);
    }

    /**
     * Truncate.
     *
     * @param string
     *            the string
     * @param length
     *            the length
     * @param truncCharacter
     *            the trunc character
     * @return the string
     */
    public static String truncate(String string, Integer length, String truncCharacter) { // NO_UCD (use private)
        final String newString;
        if (StringUtils.isEmpty(string)) {
            newString = EMPTY_STRING;
        }
        else {
            if (length == null || length.intValue() == 0 || string.length() < length.intValue()) {
                newString = string;
            }
            else {
                newString = StringUtils.concat(string.substring(0, length.intValue()), truncCharacter);
            }
        }
        return newString;
    }

    /**
     * Retourne la valeur de la methode <code>toString()</code> d'un objet. Si l'objet vaut <code>null</code>, la valeur <code>null</code> est renvoyee.
     *
     * @param obj
     *            the obj
     * @return the string
     */
    public static String valueOf(Object obj) {
        return (obj != null) ? obj.toString() : null;
    }

    /**
     * formate une chaine en une chaine sans accent ni apostrophe ni aucun espace
     * pour s'en servir en tant qu'id.
     *
     * @param chaine
     *            la chaine en entree
     * @return chaine sans accent sans apostrophe sans espace
     */
    public static String makeAnId(String chaine) {
        String chaineSansAccent = effectueSansAccent(chaine);
        String chaineSansAppo = enleverApostrophe(chaineSansAccent);
        return stripSpaces(chaineSansAppo);
    }

    /**
     * Normalise un nom de fichier en enlevant les caractères ² & ' = ~ # { [ ` @ ] } ° + $ ! ¨ £ % µ § € ^
     * Concatene tous les carctères en minuscules non accentuées
     *
     * @param badFileName
     *            the bad file name
     * @return the string
     */
    public static String normalizeFileName(String badFileName) {
        String bad = badFileName.replaceAll("²", "");
        bad = bad.replaceAll("&", "");
        bad = bad.replaceAll("=", "");
        bad = bad.replaceAll("~", "");
        bad = bad.replaceAll("#", "");
        bad = bad.replaceAll("\\{", "");
        bad = bad.replaceAll("\\[", "");
        bad = bad.replaceAll("`", "");
        bad = bad.replaceAll("^", "");
        bad = bad.replaceAll("@", "");
        bad = bad.replaceAll("]", "");
        bad = bad.replaceAll("}", "");
        bad = bad.replaceAll("°", "");
        bad = bad.replaceAll("\\+", "");
        bad = bad.replaceAll("¨", "");
        bad = bad.replaceAll("\\$", "");
        bad = bad.replaceAll("!", "");
        bad = bad.replaceAll("£", "");
        bad = bad.replaceAll("%", "");
        bad = bad.replaceAll("µ", "");
        bad = bad.replaceAll("§", "");
        bad = bad.replaceAll("€", "");
        bad = bad.replaceAll("\\^", "");

        String normalizedFileName = makeAnId(bad).toLowerCase();

        return normalizedFileName;

    }
}
