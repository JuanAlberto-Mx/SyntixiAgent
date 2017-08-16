package string;

import java.util.HashMap;
import java.util.Map;

/**
 * The <code>Damerau-Levenshtein</code> algorithm is an extension to the
 * <code>Levenshtein</code> algorithm which solves the edit distance problem
 * between a source <code>string</code> and a target <code>string</code> with
 * the following operations: <code>character insertion</code>,
 * <code>character deletion</code>, <code>character replacement</code>, and
 * <code>adjacent character swap</code>.
 * <p>
 * It is noteworthy that the <code>adjacent character swap</code> operation is
 * an edit that may be applied when two adjacent characters in the source
 * <code>string</code> match two adjacent characters in the target
 * <code>string</code>, but in reverse order, rather than a general allowance
 * for adjacent character swaps.
 * <p>
 * Current implementation allows the client to specify the costs of the various
 * edit operations with the restriction that the cost of two swap operations
 * must not be less than the cost of a delete operation followed by an insert
 * operation. This restriction is required to preclude two swaps involving the
 * same character being required for optimality which, in turn, enables a fast
 * dynamic programming solution.
 *
 * @author Juan-Alberto Hern&aacute;ndez-Mart&iacute;nez
 * @version %I%, %G%
 */
public class DamerauLevenshtein {

    /**
     * The costs for each operation.
     */
    private static int deleteCost, insertCost, replaceCost, swapCost;

    /**
     * Constructor for initializing the costs for each character operation.
     */
    public DamerauLevenshtein() {
        deleteCost = 1;
        insertCost = 1;
        replaceCost = 1;
        swapCost = 1;
    }

    /**
     * Constructor for initializing the costs for each character operation.
     *
     * @param deleteCost the cost of deleting a character.
     * @param insertCost the cost of inserting a character.
     * @param replaceCost the cost of replacing a character.
     * @param swapCost the cost of swapping two adjacent characters.
     */
    public DamerauLevenshtein(int deleteCost, int insertCost, int replaceCost, int swapCost) {
        if (2 * swapCost < insertCost + deleteCost)
            throw new IllegalArgumentException("Unsupported cost assignment");

        DamerauLevenshtein.deleteCost = deleteCost;
        DamerauLevenshtein.insertCost = insertCost;
        DamerauLevenshtein.replaceCost = replaceCost;
        DamerauLevenshtein.swapCost = swapCost;
    }

    /**
     * Computes the <code>Damerau-Levenshtein</code> distance between the specified
     * source <code>string</code> and the specified target <code>string</code>.
     *
     * @param source the source <code>string</code>.
     * @param target the target <code>string</code>.
     * @return the distance between two <code>strings</code>.
     */
    public static int similarity(String source, String target) {
        if (source.length() == 0)
            return target.length() * insertCost;

        if (target.length() == 0)
            return source.length() * deleteCost;

        int[][] table = new int[source.length()][target.length()];

        Map<Character, Integer> sourceIndexByCharacter = new HashMap<Character, Integer>();

        if (source.charAt(0) != target.charAt(0))
            table[0][0] = Math.min(replaceCost, deleteCost + insertCost);

        sourceIndexByCharacter.put(source.charAt(0), 0);

        for (int i = 1; i < source.length(); i++) {
            int deleteDistance = table[i - 1][0] + deleteCost;
            int insertDistance = (i + 1) * deleteCost + insertCost;
            int matchDistance = i * deleteCost + (source.charAt(i) == target.charAt(0) ? 0 : replaceCost);

            table[i][0] = Math.min(Math.min(deleteDistance, insertDistance), matchDistance);
        }

        for (int j = 1; j < target.length(); j++) {
            int deleteDistance = (j + 1) * insertCost + deleteCost;
            int insertDistance = table[0][j - 1] + insertCost;
            int matchDistance = j * insertCost + (source.charAt(0) == target.charAt(j) ? 0 : replaceCost);

            table[0][j] = Math.min(Math.min(deleteDistance, insertDistance), matchDistance);
        }

        for (int i = 1; i < source.length(); i++) {
            int maxSourceLetterMatchIndex = source.charAt(i) == target.charAt(0) ? 0 : -1;

            for (int j = 1; j < target.length(); j++) {
                Integer candidateSwapIndex = sourceIndexByCharacter.get(target.charAt(j));

                int jSwap = maxSourceLetterMatchIndex;
                int deleteDistance = table[i - 1][j] + deleteCost;
                int insertDistance = table[i][j - 1] + insertCost;
                int matchDistance = table[i - 1][j - 1];

                if (source.charAt(i) != target.charAt(j))
                    matchDistance += replaceCost;
                else
                    maxSourceLetterMatchIndex = j;

                int swapDistance;

                if (candidateSwapIndex != null && jSwap != -1) {
                    int iSwap = candidateSwapIndex;
                    int preSwapCost;

                    if (iSwap == 0 && jSwap == 0)
                        preSwapCost = 0;
                    else
                        preSwapCost = table[Math.max(0, iSwap - 1)][Math.max(0, jSwap - 1)];

                    swapDistance = preSwapCost + (i - iSwap - 1) * deleteCost + (j - jSwap - 1) * insertCost + swapCost;
                }
                else
                    swapDistance = Integer.MAX_VALUE;

                table[i][j] = Math.min(Math.min(Math.min(deleteDistance, insertDistance), matchDistance), swapDistance);
            }

            sourceIndexByCharacter.put(source.charAt(i), i);
        }

        return table[source.length() - 1][target.length() - 1];
    }
}