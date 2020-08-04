public class Application {
    public static void main(String[] args) {
        //Those values are used if no valid arguments are in args
        String parityCheckMatrixString = "{{1,1,1,1,0},{0,1,0,0,1}}"; //So far only Wolfram Alpha syntax supported
        int limit = 2;
        ParityCheck parityCheckObject;

        if (args.length>=1) {
            if (args.length>=2) {
                try {
                    limit = Integer.parseInt(args[1]);
                } catch (Exception ignored){}
            }
            try {
                parityCheckObject = new ParityCheck(args[0], limit);
            } catch (Exception ignored) {
                parityCheckObject = new ParityCheck(parityCheckMatrixString, limit);
            }
        }
        else
            parityCheckObject = new ParityCheck(parityCheckMatrixString, limit);


        System.out.println("Parity check matrix: " + parityCheckObject.getParityCheckMatrixAsString());
        System.out.println("Rank of the parity check matrix: " + parityCheckObject.getRank());
        System.out.println(parityCheckObject.getOriginalInputInformationAsString());
        System.out.println("Number of codewords: " + parityCheckObject.codewordAmount());
        System.out.println("Codewords: \n" + parityCheckObject.getValidCodewordsAsString());
    }
}
