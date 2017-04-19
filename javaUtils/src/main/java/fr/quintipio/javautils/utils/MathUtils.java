package fr.quintipio.javautils.utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Classe d'utilitaire liés aux nombres
 * @author Quentin
 */
public final class MathUtils {
    
    private MathUtils() {}
    
    /**
     * Retourne une valeur arrondie vers l'entier inférieur.
     *
     * @param a
     *            the a
     * @return the int
     */
    public static int arrondirVersEntierInferieur(double a) {
        return (int) Math.floor(a);

    }

    /**
     * Retourne une valeur arrondie vers l'entier supérieur.
     *
     * @param a
     *            the a
     * @return the int
     */
    public static int arrondirVersEntierSuperieur(double a) {
        final double arrondi = Math.floor(a);
        if (!equals(a, arrondi)) {
            return (int) arrondi + 1;
        }
        return (int) arrondi;
    }

    /**
     * Verification de la validité suivant la formule de Luhn.
     *
     * @param pValue
     *            the value
     * @return <code>true</code> la chaine est valide, <code>false</code> la chaine est invalide
     */
    public static boolean checkLuhn(String pValue) {
        String vValue = StringUtils.reverse(pValue);
        int sum = 0;
        for (int i = 0; i < vValue.length(); i++) {
            int digit = Integer.parseInt(vValue.substring(i, i + 1));
            if (((i + 1) % 2) == 0) {
                digit = digit * 2;
            }
            if (digit > 9) {
                digit = digit - 9;
            }
            sum += digit;
        }

        return (sum % 10) == 0;
    }

    /**
     * Compare l'égalité stricte de deux nombres.
     *
     * @param a
     *            le premier nombre.
     * @param b
     *            le second nombre.
     * @return true si les valeurs des deux nombres sont identiques.
     */
    public static Boolean equals(Number a, Number b) {
        final Boolean result;
        if (a == null && b == null) {
            result = true;
        }
        else if ((a == null && b != null) || (a != null && b == null)) {
            result = false;
        }
        else {
            // compare en finalité les valeurs afin d'eviter d'avoir des erreurs de type d'objet :
            // En effet si on compare un Long de valeur 1 et un Integer de valeur 1 --> equals retourne false
            // pourtant le résultat attendu est true.
            result = a.toString().equals(b.toString());
        }

        return result;
    }

    /**
     * Formattage d'un double avec des chiffres après la virgule si chiffres utiles (pas 0).
     * Exemples, pour une précision de 2 :
     * 1.11111 --> 1.11
     * 1.0 --> 1
     *
     * @param n
     *            nombre à formatter
     * @param maxPrecision
     *            precision maximum
     * @return nombre formatté
     */
    public static String formatDoubleMaxPrecision(final Double n, final int maxPrecision) { // NO_UCD (use private)
        if (n == null) {
            return "";
        }
        final String formatApresVirgule = StringUtils.padLeft("", maxPrecision, '#');
        String chaine = StringUtils.formatString("{0,number,0." + formatApresVirgule + "}", n).replace(",", ".");
        if (chaine.endsWith(".")) {
            chaine = chaine.substring(0, chaine.length() - 1);
        }
        return chaine;
    }

    /**
     * Teste si un nombre est compris entre deux autres nombres.
     *
     * @param valeur
     *            le nombre
     * @param borneInf
     *            la borne min
     * @param borneMax
     *            la borne max
     * @return tru ou false
     */
    public static boolean isBetween(Integer valeur, Integer borneInf, Integer borneMax) {
        if (valeur.compareTo(borneInf) >= 0 && valeur.compareTo(borneMax) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * Teste si un {@link Number} vaut zéro ou <code>null</code>.
     *
     * @param n
     *            the n
     * @return true, if is zero
     */
    public static boolean isZero(Number n) {
        if (n == null) {
            return true;
        }
        if (n instanceof BigDecimal) {
            return equals(n, BigDecimal.ZERO);
        }
        if (n instanceof Double) {
            return equals(n, 0d);
        }
        if (n instanceof Float) {
            return equals(n, 0f);
        }
        return n.longValue() == 0;
    }

    /**
     * Recalcule (suivant un ratio) une suite de valeurs séparées par des virgules.
     * La chaine en sortie est la suite de ces nouvelles valeurs.
     *
     * @param zone
     *            la zone
     * @param ratio
     *            le ratio
     * @param precision
     *            la precision
     * @return la suite de ces nouvelles valeurs
     */
    public static String resize(String zone, Integer ratio, int precision) {
        final StringBuilder calc = new StringBuilder(StringUtils.EMPTY_STRING);
        String val = "";
        for (String valT : StringUtils.split(zone, ",")) {
            val = StringUtils.trimToBlank(valT);
            final Double pourcentage = Double.parseDouble(String.valueOf(ratio)) / 100;
            calc.append(formatDoubleMaxPrecision(Double.parseDouble(val) * pourcentage, precision)).append(",");
        }
        return calc.substring(0, calc.lastIndexOf(","));
    }

    /**
     * Verif number is in liste.
     *
     * @param number
     *            the number
     * @param liste
     *            the liste
     * @return true, if successful
     */
    public static boolean verifNumberIsInListe(Integer number, List<Integer> liste) {
        boolean retour = false;

        if (number != null || liste != null) {
            if (liste.size() > 0) {
                for (Integer integer : liste) {
                    if (equals(integer, number)) {
                        retour = true;
                        break;
                    }
                }
            }
        }
        return retour;
    }

}
