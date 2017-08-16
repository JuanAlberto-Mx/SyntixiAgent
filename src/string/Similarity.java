package string;

/**
 * <code>Similarity</code> class establishes the percentage of similarity acceptable
 * to consider two <code>Strings</code> as similar.
 *
 * @author Juan-Alberto Hern&aacute;ndez-Mart&iacute;nez
 * @version %I%, %G%
 */
public class Similarity {

    /**
     * Percentage of similarity established to consider two <code>Strings</code>
     * as similar.
     */
    private static final double PERCENTAGE_SIMILARITY = 0.70;

    /**
     * Gets the percentage of similarity established to validate two <code>Strings</code>.
     *
     * @return the percentage of similarity established.
     */
    public static double getPercentageSimilarity() {
        return PERCENTAGE_SIMILARITY;
    }

    /**
     * Prints the resulting similarity.
     *
     * @param string1 the first <code>String</code>.
     * @param string2 the second <code>String</code>.
     */
    public static void printLevenshteinSimilarity(String string1, String string2) {
        System.out.println(String.format("%.3f is the similarity between \"%s\" and \"%s\"", Levenshtein.similarity(string1, string2), string1, string2));
    }

    public static void printDamerauLevenshteinSimilarity(String string1, String string2) {
        System.out.println(String.format("%.3f is the similarity between \"%s\" and \"%s\"", DamerauLevenshtein.similarity(string1, string2), string1, string2));
    }
}