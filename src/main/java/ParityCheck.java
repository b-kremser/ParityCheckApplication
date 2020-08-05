import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParityCheck {
    private final int[][] parityCheckMatrix;
    private int[][] generatorMatrix;
    private final int limit;
    private List<int[][]> validCodewords;
    private int hammingDistance = -1;

    /**
     * Creates a ParityCheck-object that can perform multiple actions on a given parity check matrix, such as
     * validating whether a codeword is valid or compute all possible codewords
     * @param parityCheckMatrixString a String that contains the parity check matrix in the Wolfram Alpha or Latex notation
     * @param limit the upper bound of possible values (e.g. for F2 = {0, 1}, limit = 2)
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
        if(!isValidMatrix(codeword) || codeword[0].length > 1)
            return false;

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

    public String getOriginalInputInformationAsString(){
        double originalSize = Math.log(codewordAmount()) / Math.log(limit); //log_{limit} (number of codewords)
        int encodedSize = parityCheckMatrix[0].length;
        double informationRate = originalSize / encodedSize;

        return "With the original generator matrix, a " + (int) originalSize +
                " x 1-word was transformed into a " + encodedSize + " x 1-codeword.\n" +
                "The information rate is " + (int) originalSize + "/" + encodedSize +
                "=" + informationRate + "; redundancy is " + (encodedSize - ((int) originalSize));
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
        return getMatrixAsString(parityCheckMatrix);
    }

    public String getGeneratorMatrixAsString() {
        if (generatorMatrix == null)
            calculateGeneratorMatrix();
        if (generatorMatrix == null) //checks whether calculating the generator matrix was successful
            return "A valid generator matrix could not be calculated, but might still exist";
        return getMatrixAsString(generatorMatrix);
    }

    private static String getMatrixAsString(int[][] matrix) {
        if (matrix.length==0 || matrix[0].length==0)
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int[] row : matrix) {
            sb.append('{');
            for (int i=0; i<row.length-1; ++i)
                sb.append(row[i]).append(",");
            sb.append(row[row.length-1]).append("},");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
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

    //generates the generator matrix if parity check matrix can be written as P=(-A | I ) where I is identity matrix
    private void calculateGeneratorMatrix(){
        if (validCodewords == null)
            calculateCodewords();

        //Original size = size of word before encoding
        int originalSize = (int) (Math.log(codewordAmount()) / Math.log(limit)); //log_{limit} (number of codewords)

        //Check if parity check matrix contains identity matrix
        try {
            for (int i = 0; i < parityCheckMatrix.length; ++i)
                for (int j = originalSize; j < originalSize + parityCheckMatrix.length; ++j)
                    if ((i != (j - originalSize) && parityCheckMatrix[i][j] != 0) || (i == (j - originalSize) && parityCheckMatrix[i][j] != 1))
                        return;
        } catch (Exception ignored) {return;} //for invalid parity check matrices, an array out of bounds exception might be thrown

        generatorMatrix = new int[originalSize+parityCheckMatrix.length][];
        //Fill in identity matrix at the top of generator matrix
        for (int i=0; i<originalSize; ++i) {
            generatorMatrix[i] = new int[originalSize];
            Arrays.fill(generatorMatrix[i], 0);
            generatorMatrix[i][i] = 1;
        }

        //calculate rest of the generator matrix
        for (int i=0; i<parityCheckMatrix.length; ++i) {
            generatorMatrix[originalSize+i] = new int[originalSize];
            for (int j=0; j<originalSize; j++) {
                generatorMatrix[i+originalSize][j] = (-1) * parityCheckMatrix[i][j];
                generatorMatrix[i+originalSize][j] = generatorMatrix[i+originalSize][j]<0 ?
                        (generatorMatrix[i+originalSize][j]%limit)+limit :
                        generatorMatrix[i+originalSize][j] % limit;
            }
        }
    }

    public int getHammingDistance() {
        if (validCodewords == null)
            calculateCodewords();
        if (hammingDistance != -1)
            return hammingDistance;
        if (validCodewords.size()<2)
            return 0;

        int distance = hammingDistance(validCodewords.get(0), validCodewords.get(1));
        for (int i=0; i<validCodewords.size(); ++i) {
            for (int j=i+1; j<validCodewords.size(); ++j)
                distance = Integer.min(distance, hammingDistance(validCodewords.get(i), validCodewords.get(j)));
        }
        hammingDistance = distance;
        return distance;
    }

    private int hammingDistance(int[][] firstVector, int[][] secondVector) {
        int distance = 0;
        for (int i=0; i<firstVector.length; ++i) {
            if (firstVector[i][0] != secondVector[i][0])
                distance++;
        }
        return distance;
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

    private static int[][] parseWolframAlphaMatrixString(String string, int limit){
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
