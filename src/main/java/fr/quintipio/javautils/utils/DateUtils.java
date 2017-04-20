package fr.quintipio.javautils.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Méthodes utilitaires pour les dates
 * @author Quentin
 */
public final class DateUtils {

    /** The Constant ARIANE_DATE_PATTERN. */
    public static final String      ARIANE_DATE_PATTERN      = "dd/MM/yyyy HH:mm:ss";
    /** The Constant DEFAULT_DATE_LOCAL. */
    private static final Locale     DEFAULT_DATE_LOCAL       = Locale.FRENCH;

    /** The Constant DEFAULT_DATE_PATTERN. */
    public static final String      DEFAULT_DATE_PATTERN     = "dd/MM/yyyy";

    /** The Constant NB_MILLISECONDS_PAR_JOUR. */
    private static final BigDecimal NB_MILLISECONDS_PAR_JOUR = new BigDecimal(24 * 60 * 60 * 1000);

    /** The Constant SMALL_DATE_MAX. */
    public static final Date        SMALL_DATE_MAX           = DateUtils.creer(2080, Calendar.DECEMBER, 31); // en fait c'est la date_max + 1

    /** The Constant SMALL_DATE_MIN. */
    public static final Date        SMALL_DATE_MIN           = DateUtils.creer(1899, Calendar.DECEMBER, 31); // en fait c'est la date_min - 1 jour

    private DateUtils(){};
    
    /**
     * Ajoute une valeur à l'un des composants d'une date. Les composants sont
     * définis par les constantes de {@link Calendar}. Exemple :
     *
     * <pre>
     * <code>
     * ajouter(new Date(), Calendar.DATE, 1);</code>
     * </pre>
     *
     * .
     *
     * @param date
     *            the date
     * @param champ
     *            the champ
     * @param valeur
     *            the valeur
     * @return the date
     */
    public static Date ajouter(Date date, int champ, int valeur) {
        if (date != null) {
            final Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.add(champ, valeur);
            return cal.getTime();
        }

        return null;
    }

    /**
     * Calcule le nombre d'années entre deux dates.
     *
     * @param date1
     *            Date
     * @param date2
     *            Date
     * @return nombre d'annees de différence entre les deux dates saisies
     */
    public static int calculerNombreAnnees(Date date1, Date date2) { // NO_UCD (unused code)
        return calculerNombreAnnees(date1, date2, false);
    }

    /**
     * Calcule le nombre d'années entre deux dates.
     * ATTENTION : Cette méthode renvoie la différence entre les années de chaque dates, et
     * non le nombre exact d'années réelles entre les deux. Par exemple, dans le cas
     * 28/12/2005 - 05/01/2006 elle renverra 1 an ! Pour plus de précision (Age, durée exacte, etc.)
     * utiliser la methode getJoursMoisAnneesEntre().
     *
     * @param date1
     *            Date
     * @param date2
     *            Date
     * @param isStrict
     *            Si vrai, le calcul du nombre d'annee respecte l'ordre de saisie
     *            des dates. Autrement, le résultat sera toujours positif.
     * @return nombre d'annees de différence entre les deux dates saisies
     */
    // NO_UCD (use private)
    public static int calculerNombreAnnees(Date date1, Date date2, boolean isStrict) {
        
        final Date dateMin;
        final Date dateMax;

        if (isStrict) {
            dateMin = date2;
            dateMax = date1;
        }
        else {
            if (date1.before(date2)) {
                dateMin = date1;
                dateMax = date2;
            }
            else {
                dateMin = date2;
                dateMax = date1;
            }
        }

        final int anneeMax = DateUtils.getChamp(dateMax, Calendar.YEAR);
        final int anneeMin = DateUtils.getChamp(dateMin, Calendar.YEAR);

        final int nbAnnees = anneeMax - anneeMin;
        return nbAnnees;
    }

    /**
     * Calcule le nombre de jours entre deux dates.
     *
     * @param date1
     *            Date
     * @param date2
     *            Date
     * @return nombre de jours de différence entre les deux dates saisies
     */
    public static int calculerNombreJours(Date date1, Date date2) {
        return calculerNombreJours(date1, date2, false);
    }

    /**
     * Calcule le nombre de jours entre deux dates.
     *
     * @param date1
     *            Date
     * @param date2
     *            Date
     * @param isStrict
     *            Si vrai, le calcul du nombre de jour respecte l'ordre de saisie
     *            des dates. Autrement, le résultat sera toujours positif.
     * @return nombre de jours de différence entre les deux dates saisies
     */
    // NO_UCD (use private)
    public static int calculerNombreJours(Date date1, Date date2, boolean isStrict) {
        final Date dateMin;
        final Date dateMax;

        if (isStrict) {
            dateMin = date2;
            dateMax = date1;
        }
        else {
            if (date1.before(date2)) {
                dateMin = date1;
                dateMax = date2;
            }
            else {
                dateMin = date2;
                dateMax = date1;
            }
        }

        final BigDecimal nbMilliseconds = new BigDecimal(dateMax.getTime() - dateMin.getTime() - 10000);
        final BigDecimal nbJours = nbMilliseconds.divide(NB_MILLISECONDS_PAR_JOUR, RoundingMode.HALF_UP);
        return nbJours.intValue();
    }

    /**
     * Controle la validité d'une date String en format jj/mm/yyyy
     *
     * @param date
     *            : la date à controler
     * @return true si la date est ok
     */
    public static boolean checkDate(String date) {

        if (StringUtils.isEmpty(date)) {
            return false;
        }
        Date d = parse(date);
        String dd = format(d, "dd/MM/yyyy");
        return date.equals(dd);
    }

    /**
     * Retourne la comparaison entre 2 Dates. Les Dates Null peuvent être mises en début ou en fin de liste. La comparaison de date de type java.sql.Date et java.util.date est transparente. <br>
     * <center>-1 si Première date < Seconde date. <br>
     * 1 si Première date > Seconde date. <br>
     * 0 si Première date = Seconde date.</center>
     *
     * @param date1
     *            Première date à comparer.
     * @param date2
     *            Seconde date à comparer.
     * @param nullEtVideEnPremier
     *            Faut-il mettre les null et vide en premier ?
     * @return inférieur, égal ou supérieur à 0 selon la règle des mèthodes standard "compareTo".
     */
    public static int compare(Date date1, Date date2, boolean nullEtVideEnPremier) {
        return ObjectUtils.compare((date1 != null) ? date1.getTime() : null, (date2 != null) ? date2.getTime() : null, nullEtVideEnPremier);
    }

    /**
     * Crée une date, initialisée à minuit.
     *
     * @param annee
     *            the annee
     * @param mois
     *            the mois
     * @param jour
     *            the jour
     * @return the date
     * @see #creer(int, int, int, int, int, int)
     */
    public static Date creer(int annee, int mois, int jour) {
        return creer(annee, mois, jour, 0, 0, 0);
    }

    /**
     * Crée une date.
     *
     * @param annee
     *            the annee
     * @param mois
     *            constante de {@link Calendar} : <code>Calendar.APRIL</code>, etc...
     * @param jour
     *            the jour
     * @param heures
     *            the heures
     * @param minutes
     *            the minutes
     * @param secondes
     *            the secondes
     * @return the date
     */
    // NO_UCD (use private)
    public static Date creer(int annee, int mois, int jour, int heures, int minutes, int secondes) {
        return new GregorianCalendar(annee, mois, jour, heures, minutes, secondes).getTime();
    }

    /**
     * Formate une date selon un motif par défaut : <code>dd/MM/yyyy</code>.
     *
     * @param d
     *            the d
     * @return the string
     * @see #format(Date, String)
     */
    public static String format(Date d) {
        return format(d, DEFAULT_DATE_PATTERN);
    }

    /**
     * Formate une date selon un motif défini.
     *
     * @param d
     *            date à formater
     * @param local
     *            the local
     * @param pattern
     *            motif de formatage
     * @return une chaîne de caractères avec la date formatée
     */
    public static String format(Date d, Locale local, String pattern) { // NO_UCD (unused code)
        if (d == null) {
            return null;
        }
        final String myPattern = ObjectUtils.defaultIfNull(pattern, DEFAULT_DATE_PATTERN);
        final Locale myLocal = (local != null) ? local : DEFAULT_DATE_LOCAL;
        return new SimpleDateFormat(myPattern, myLocal).format(d);
    }

    /**
     * Formate une date selon un motif défini.
     *
     * @param d
     *            date à formater
     * @param pattern
     *            motif de formatage
     * @return une chaîne de caractères avec la date formatée
     */
    public static String format(Date d, String pattern) {
        if (d == null) {
            return null;
        }
        final String myPattern = ObjectUtils.defaultIfNull(pattern, DEFAULT_DATE_PATTERN);
        return new SimpleDateFormat(myPattern).format(d);
    }

    /**
     * Format date heure.
     *
     * @param d
     *            the d
     * @return the string
     */
    public static String formatDateHeure(Date d) {
        return format(d, "dd/MM/yy à HH:mm:ss");
    }

    /**
     * Retourne la date au format String avec un formatage court (ex 01 janvier 2008).
     *
     * @param d
     *            date à formater.
     * @return date formatee
     */
    public static String formatText(Date d) { // NO_UCD (unused code)
        return format(d, "d MMMMM yyyy");
    }

    /**
     * Retourne un champ d'une {@link Date}. Cette méthode est un raccourci pour
     * :
     *
     * <pre>
     * <code>Calendar cal = new GregorianCalendar();cal.setTime(date);
     * return cal.get(champ);</code>
     * </pre>
     *
     * @param date
     *            the date
     * @param champ
     *            the champ
     * @return the champ
     */
    // NO_UCD (use private)
    public static int getChamp(Date date, int champ) {
        final Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(champ);
    }

    /**
     * Récupération de la date - 1.
     * Est un raccourci pour DateUtils.ajouter(date, Calendar.DAY_OF_MONTH, -1).
     *
     * @param d
     *            the d
     * @return the date moins1
     */
    public static Date getDateMoins1(Date d) {
        return DateUtils.ajouter(d, Calendar.DAY_OF_MONTH, -1);
    }

    /**
     * Récupération de la date + 1.
     * Est un raccourci pour DateUtils.ajouter(date, Calendar.DAY_OF_MONTH, 1).
     *
     * @param d
     *            the d
     * @return the date plus1
     */
    public static Date getDatePlus1(Date d) {
        return DateUtils.ajouter(d, Calendar.DAY_OF_MONTH, 1);
    }

    /**
     * Retourne la date actuelle (avec les heures).
     *
     * @return la date actuelle (avec les heures).
     */
    public static Date getMaintenant() {
        return new Date();
    }

    /**
     * Lit une date à partir d'une chaîne de caractères, de la forme <code>dd/MM/yyyy</code>.
     *
     * @param date
     *            la chaîne à parser.
     * @return la date.
     * @see #parse(String, String)
     */
    public static Date parse(String date) {
        return parse(date, DEFAULT_DATE_PATTERN);
    }

    /**
     * Lit une date à partir d'une chaîne de caractères, selon un motif défini.
     *
     * @param date
     *            the date
     * @param pattern
     *            the pattern
     * @return the date
     */
    public static Date parse(String date, String pattern) {
        final String myDate = StringUtils.trimToNull(date);
        if (myDate == null) {
            return null;
        }
        final String myPattern = ObjectUtils.defaultIfNull(pattern, DEFAULT_DATE_PATTERN);
        try {
            return new SimpleDateFormat(myPattern).parse(date);
        }
        catch (final ParseException e) {
            return null;
        }
    }

    /**
     * Retourne la date du jour (sans les heures).
     *
     * @return la date du jour.
     */
    public static Date getAujourdhui() {
        return arrondir(new Date(), Calendar.DATE);
    }

    /**
     * Arrondit une date sur un type de champ de {@link Calendar}. Par exemple,
     * si l'on arrondit une date sur le champ {@link Calendar#DATE}, les composantes
     * heures, minutes, secondes et millisecondes sont initialisées à zéro. Si l'on
     * arrondit sur le champ {@link Calendar#MONTH}, les composantes précédentes sont
     * réinitialisées <b>et</b> le jour est initialisé à 1. Avec le champ {@link Calendar#YEAR}, la date est en plus réinitialisée au 1er janvier de l'année.
     *
     * @param date
     *            the date
     * @param champ
     *            une valeur parmi : {@link Calendar#YEAR}, {@link Calendar#MONTH}, {@link Calendar#DATE}, {@link Calendar#DAY_OF_MONTH}, {@link Calendar#HOUR}, {@link Calendar#HOUR_OF_DAY}, {@link Calendar#MINUTE}, {@link Calendar#SECOND}, {@link Calendar#MILLISECOND}
     * @return the date
     */
    public static Date arrondir(Date date, int champ) {
        final Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        // on peut comparer la valeur de champ aux constantes de Calendar avec
        // un switch car ces valeurs font partie de l'API publique, et sont donc
        // valables quelque soit la JVM
        switch (champ) {
            case Calendar.YEAR:
                cal.set(Calendar.MONTH, Calendar.JANUARY);
                break;
            case Calendar.MONTH:
                cal.set(Calendar.DATE, 1);
                break;
            case Calendar.DATE:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                break;
            case Calendar.HOUR:
                cal.set(Calendar.MINUTE, 0);
                break;
            case Calendar.MINUTE:
                cal.set(Calendar.SECOND, 0);
                break;
            case Calendar.SECOND:
                cal.set(Calendar.MILLISECOND, 0);
                break;
            default:
                throw new RuntimeException("Valeur incorrecte pour l'argument 'champ' : " + champ);
        }

        return cal.getTime();
    }

    public static Date getDateSansHeureMinuteSeconde() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
