package com.github.paritycheckapplication;

public class Application {
    public static void main(String[] args) {
        //Those values are used if no valid arguments are in args
        String parityCheckMatrixString = "{{0,1,1,1,1,0,0,0},{1,0,1,1,0,1,0,0},{1,1,0,1,0,0,1,0},{1,1,1,0,0,0,0,1}}";   //So far only Wolfram Alpha syntax is supported
        int limit = 2;                         //the upper bound of possible values (e.g. for F2 = {0, 1}, limit = 2)
        ParityCheck parityCheckObject;

        if (args.length>=1) {
            if (args.length>=2) {
                try {
                    limit = Integer.parseInt(args[1]);
                    if (limit <= 1) {
                        System.out.println("The limit has to be >= 2, so limit=2 will be used");
                        limit = 2;
                    }
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
        System.out.println("Original generator matrix: " + parityCheckObject.getGeneratorMatrixAsString());
        System.out.println(parityCheckObject.getOriginalInputInformationAsString());
        System.out.println("Hamming Distance: " + parityCheckObject.getHammingDistance());
        System.out.println("Number of valid codewords: " + parityCheckObject.codewordAmount());
        System.out.println("Valid Codewords: \n" + parityCheckObject.getValidCodewordsAsString());
    }
}
