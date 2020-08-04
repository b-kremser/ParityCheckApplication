import java.util.ArrayList;
import java.util.List;

public class ParityCheck {
    private final int[][] parityCheckMatrix;
    private final int limit;
    private List<int[][]> validCodewords;

    /**
     * Creates a CodeWordChecker-object that can perform multiple actions on a given parity check matrix, such as
     * validating whether a codeword is valid or compute all possible codewords
     * @param parityCheckMatrixString a String that contains the parity check matrix in the Wolfram Alpha or Latex notation
     * @param limit the upper bound of possible values (e.g. F2 = {0, 1}, so the limit = 2)
     */
    public ParityCheck(String parityCheckMatrixString, int limit) {
        parityCheckMatrix = parseMatrixString(parityCheckMatrixString, limit);
        if (!isValidMatrix(parityCheckMatrix))
            throw new RuntimeException("An error occurred while building this matrix");
        this.limit = limit;
    }

    public boolean isValidCodeWord(String s){
        return isValidCodeWord(parseMatrixString(s, limit));
    }

    public boolean isValidCodeWord(int[][] codeword) {
        for (int[] parityRow : parityCheckMatrix) {
            int result = 0;
            for (int i=0; i<parityRow.length; ++i)
                result += (parityRow[i]*codeword[i][0]);
            result = result<0 ? result%limit + limit : result%limit;
            if (result != 0)
                return false;
        }
        return true;
    }

    public int codewordAmount() {
        if (validCodewords == null)
            calculateCodewords();

        return validCodewords.size();
    }

    public List<int[][]> getValidCodewords() {
        return validCodewords;
    }

    public String getValidCodewordsAsString() {
        if (validCodewords == null)
            calculateCodewords();

        StringBuilder sb = new StringBuilder();
        for (int[][] codeword : validCodewords) {
            sb.append('(');
            for (int i=0; i<codeword.length-1; ++i)
                sb.append(codeword[i][0]).append(", ");
            sb.append(codeword[codeword.length-1][0]).append(")\n");
        }
        return sb.toString();
    }

    public String getParityCheckMatrixAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int[] row : parityCheckMatrix) {
            sb.append('{');
            for (int i=0; i<row.length-1; ++i)
                sb.append(row[i]).append(",");
            sb.append(row[row.length-1]).append("}");
        }
        sb.append("}");
        return sb.toString();
    }

    private void calculateCodewords() {
        validCodewords = new ArrayList<>();
        int codewordLength = parityCheckMatrix[0].length;
        for (int i=0; i<Math.pow(limit, codewordLength); ++i) {
            int[][] codeword = new int[codewordLength][];
            for (int j=0; j<codewordLength; ++j) {
                codeword[j] = new int[1];
                codeword[j][0] = (int) ((i / (Math.pow(limit, codewordLength-j-1))) % limit);
            }
            if (isValidCodeWord(codeword))
                validCodewords.add(codeword);
        }
    }

    private static int[][] parseMatrixString(String string, int limit) {
        string = string.replaceAll("\\s","");
        if (string.charAt(0)=='\\')
            return parseLatexMatrixString(string, limit);
        return parseWolframAlphaMatrixString(string, limit);
    }

    //Todo add LatexParser
    private static int[][] parseLatexMatrixString(String string, int limit) {
        throw new RuntimeException("Latex Strings are not supported yet");
    }

    public static int[][] parseWolframAlphaMatrixString(String string, int limit){
        String[] matrixRows = string.split("},\\{");
        int[][] matrixEntries = new int[matrixRows.length][];

        int i=0;
        for (String row : matrixRows) {
            String[] digits = row.replaceAll("\\{","").replaceAll("}","")
                    .split(",");
            matrixEntries[i] = new int[digits.length];
            for (int j=0; j<matrixEntries[i].length; ++j) {
                int parsedInt = Integer.parseInt(digits[j]);
                parsedInt =parsedInt<0 ? parsedInt%limit + limit : parsedInt%limit;
                matrixEntries[i][j] = parsedInt;
            }
            i++;
        }
        return matrixEntries;
    }

    /**
     * Checks whether all rows have the same amount of values in them
     */
    private static boolean isValidMatrix(int[][] matrix) {
        int rowSize = matrix[0].length;
        for (int[] row : matrix)
            if (row.length != rowSize)
                return false;

        return true;
    }
}
