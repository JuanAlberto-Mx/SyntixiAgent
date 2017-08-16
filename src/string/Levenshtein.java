package string;

/**
 * <code>Levenshtein</code> class allows calculating the similarity between
 * two <code>Strings</code> by using the <code>Levenshtein</code> distance.
 *
 * @author Juan-Alberto Hern&aacute;ndez-Mart&iacute;nez
 * @version %I%, %G%
 */
public class Levenshtein {

    /**
     * Calculates the similarity (a number within <code>0</code> and <code>1</code>)
     * between two <code>Strings</code>.
     *
     * @param string1 the first <code>String</code>.
     * @param string2 the second <code>String</code>.
     * @return a number within <code>0</code> and <code>1</code>. The closer the
     *          value is to <code>1</code> the more similar the <code>Strings</code>
     *          are.
     */
    public static double similarity(String string1, String string2) {
        String longer = string1;
        String shorter = string2;

        if (string1.length() < string2.length()) {
            longer = string2;
            shorter = string1;
        }

        int longerLength = longer.length();

        if (longerLength == 0)
            return 1.0;

        return (longerLength - distance(longer, shorter)) / (double) longerLength;
    }

    /**
     * Calculates the <code>Levenshtein</code> distance.
     *
     * @param string1 the first <code>String</code>.
     * @param string2 the second <code>String</code>.
     * @return a number within <code>0</code> and <code>1</code>. The closer the
     *          value is to <code>1</code> the more similar the <code>Strings</code>
     *          are.
     */
    private static int distance(String string1, String string2) {
        string1 = string1.toLowerCase();
        string2 = string2.toLowerCase();

        int[] costs = new int[string2.length() + 1];

        for (int i = 0; i <= string1.length(); i++) {
            int lastValue = i;

            for (int j = 0; j <= string2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];

                        if (string1.charAt(i - 1) != string2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;

                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[string2.length()] = lastValue;
        }

        return costs[string2.length()];
    }
}