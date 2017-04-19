package fr.quintipio.javautils.utils;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Classe utilitaire pour le regex
 * @author Quentin
 */
public final class RegexUtils {

    /** The Constant ALPHNUM. */
    public static final String ALPHNUM               = "[A-Za-z0-9 ]*";                                                                        // NO_UCD (unused code)

    /** The Constant ALPHNUM_ACCENT. */
    public static final String ALPHNUM_ACCENT        = "[A-Za-z0-9àâäôöïîûùüèéêëç ]*";                                                         // NO_UCD (unused code)

    public static final String VERSION               = "[0-9]\\.[0-9]\\.[0-9]";

    /** The Constant ALPHNUM_SEPARATOR. azeb|aze|aze|aze|sdf|3251| */
    public static final String ALPHNUM_SEPARATOR     = "([a-zA-Z0-9]+\\|?)+";                                                                  // NO_UCD (unused code)

    /** The Constant CONTIENT_NUMERIC. */
    public static final String CONTIENT_NUMERIC      = ".*\\d.*";                                                                              // NO_UCD (unused code)

    /** The Constant DATE_ANNEE_2. */
    public static final String DATE_ANNEE_2          = "[0-9]{2}/[0-9]{2}/[0-9]{2}";

    /** The Constant DATE_ANNEE_2_SANS_SEP. */
    public static final String DATE_ANNEE_2_SANS_SEP = "[0-9]{6}";

    /** The Constant DATE_ANNEE_4. */
    public static final String DATE_ANNEE_4          = "[0-9]{2}/[0-9]{2}/[0-9]{4}";

    /** The Constant DATE_ANNEE_4_SANS_SEP. */
    public static final String DATE_ANNEE_4_SANS_SEP = "[0-9]{8}";
    /** The deb regex ext. */
    public static final String DEBREGEXEXT_WS        = "/(\\.|\\/)(";

    /** The Constant FINREGEXEXT_WS. */
    public static final String FINREGEXEXT_WS        = ")$/";

    /** The Constant IPV4. */
    public static final String IPV4                  = "\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}";                                            // NO_UCD (unused code)

    /** The Constant IPV6. */
    public static final String IPV6                  = "\\w{0,4}[:]\\w{0,4}[:]\\w{0,4}[:]\\w{0,4}[:]\\w{0,4}[:]\\w{0,4}[:]\\w{0,4}[:]\\w{0,4}"; // NO_UCD (unused code)

    // /** The Constant IPV4. */
    // public static final String IPV4 = "/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/";
    //
    // /** The Constant IPV6. */
    // public static final String IPV6 = "/^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|" + "(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|"
    // + "(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|"
    // + "(([0-9A-Fa-f]{1,4}:){6}((b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b))|" + "(([0-9A-Fa-f]{1,4}:){0,5}:((b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b))|"
    // + "(::([0-9A-Fa-f]{1,4}:){0,5}((b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b))|"
    // + "([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/";
    // /** The Constant IPV6_IPV4. */
    /** The Constant IPV6_IPV4. */
    public static final String IPV6_IPV4             = "/^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|"
                                                       + "(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|"
                                                       + "(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b))|(([0-9A-Fa-f]{1,4}:){0,5}:((b((25[0-5])|(1d{2})|(2[0-4]d)|"
                                                       + "(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b))|(::([0-9A-Fa-f]{1,4}:){0,5}((b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})| "
                                                       + "(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/";        // NO_UCD (unused code)

    /** The Constant REGEX_NOM_USER. */
    public static final String REGEX_NOM_USER        = "^[\\x2D\\x30-\\x39\\x40-\\x5A\\x61-\\x7A\\x5F]{4,14}$";                                // ^[a-zA-Z0-9_-]${4,14} avec un @ // NO_UCD (unused code)
    // En cas de modif de cette regex, merci de changer le commentaire dans la vue "dlgChangementPwd"
    // "/^[\\x20-\\x21\\x2A\\x2B\\x2D\\x2E\\x30\\x39\\x3F-\\x5A\\x61-\\x7A\\x5F-\\x5F]{4,14}$/";

    /** The Constant REGEXP_EMAIL. */
    public static final String REGEXP_EMAIL          = "^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$";                // NO_UCD (unused code)


    private RegexUtils() {}
    
    /**
     * Check alph num with separ.
     *
     * @param chaine
     *            the chaine
     * @return true, if successful
     */
    public static boolean checkAlphNumWithSepar(String chaine) {
        return matches(chaine, ALPHNUM_SEPARATOR);
    }

    /**
     * Check special.
     *
     * @param chaine
     *            the chaine
     * @return true, if successful
     */
    public static boolean checkContientNumeric(String chaine) {
        return matches(chaine, CONTIENT_NUMERIC);
    }

    /**
     * Check special.
     *
     * @param chaine
     *            the chaine
     * @return true, if successful
     */
    public static boolean checkContientSpecial(String chaine) {
        return !matches(chaine, ALPHNUM_ACCENT);
    }

    /**
     * Check ip v4.
     *
     * @param chaine
     *            the chaine
     * @return true, if successful
     */
    public static boolean checkIPV4(String chaine) {
        return matches(chaine, IPV4);
    }

    /**
     * Check ip v4 e t6.
     *
     * @param chaine
     *            the chaine
     * @return true, if successful
     */
    public static boolean checkIPV4ET6(String chaine) { // NO_UCD (unused code)
        return matches(chaine, IPV6_IPV4);
    }

    /**
     * Check ip v6.
     *
     * @param chaine
     *            the chaine
     * @return true, if successful
     */
    public static boolean checkIPV6(String chaine) { // NO_UCD (unused code)
        return matches(chaine, IPV6);
    }

    /**
     * Méthode pour valider une liste d'emails.
     *
     * @param mels
     *            the mels
     * @return true, if successful
     */
    public static boolean checkListeMail(List<String> mels) {
        boolean ok = true;
        if (!CollectionUtils.isEmpty(mels)) {
            for (final String test : mels) {
                if (!matches(test, REGEXP_EMAIL)) {
                    ok = false; // le résultat sera false meme si certains dans la liste sont valides
                    break;
                }
            }
        }
        return ok;
    }

    /**
     * Check nom utilisateur.
     *
     * @param chaine
     *            the chaine
     * @return true, if successful
     */
    public static boolean checkMail(String chaine) {
        return matches(chaine, REGEXP_EMAIL);
    }

    /**
     * Check nom utilisateur.
     *
     * @param chaine
     *            the chaine
     * @return true, if successful
     */
    public static boolean checkNomUtilisateur(String chaine) { // NO_UCD (unused code)
        return matches(chaine, REGEX_NOM_USER);
    }

    /**
     * Check the version
     *
     * @param chaine
     *            the chaine
     * @return true if successfull
     */
    public static boolean checkVersion(String chaine) {
        return matches(chaine, VERSION);
    }

    /**
     * Matches.
     *
     * @param chaine
     *            the chaine
     * @param regex
     *            the regex
     * @return true, if successful
     */
    public static boolean matches(String chaine, String regex) { // NO_UCD (use private)
        boolean check = false;
        if (!StringUtils.isEmpty(chaine)) {
            check = chaine.matches(regex);
        }
        return check;
    }

    /**
     * Nettoyer regex.
     *
     * @param pChaine
     *            the chaine
     * @return the string
     */
    public static String nettoyerRegex(String pChaine) {
        String vChaine = pChaine.replace(DEBREGEXEXT_WS, "");
        return vChaine.replace(FINREGEXEXT_WS, "");
    }

}
