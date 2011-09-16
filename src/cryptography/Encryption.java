/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cryptography;

/**
 *
 * @author Bruce Nguyen
 */
import java.io.*;

public class Encryption {
    final private int SIZE_OF_EACH_ENCRYPTION_IN_BYTES = 4;
    final private int TIMES_ENCRYPTION = 2;

    private MatrixBasicFunctions MBFs = new MatrixBasicFunctions();
    private BasicFunctions BFs = new BasicFunctions();

    int[][][] D = new int[TIMES_ENCRYPTION][SIZE_OF_EACH_ENCRYPTION_IN_BYTES][SIZE_OF_EACH_ENCRYPTION_IN_BYTES];
    int[][][] key = new int[TIMES_ENCRYPTION][SIZE_OF_EACH_ENCRYPTION_IN_BYTES][1];

    public void encrypt(String inputFileName, String outputFileName, int irreducible_polynomial){
        int[][] input = new int[SIZE_OF_EACH_ENCRYPTION_IN_BYTES][1];
        int[][] output = new int[SIZE_OF_EACH_ENCRYPTION_IN_BYTES][1];

        try{
            FileInputStream inputStream = new FileInputStream(inputFileName);
            FileOutputStream outputStream = new FileOutputStream(outputFileName);
            int numberOfBytesInFile = inputStream.available();

            for(int i=0;i<((double)numberOfBytesInFile/SIZE_OF_EACH_ENCRYPTION_IN_BYTES);i++){

                if(i==(numberOfBytesInFile/SIZE_OF_EACH_ENCRYPTION_IN_BYTES)){
                    for(int k=0;k<numberOfBytesInFile%SIZE_OF_EACH_ENCRYPTION_IN_BYTES;k++)
                        input[k][1]=BFs.inverse(inputStream.read(),irreducible_polynomial);
                    for(int m=SIZE_OF_EACH_ENCRYPTION_IN_BYTES-1;m>=numberOfBytesInFile%SIZE_OF_EACH_ENCRYPTION_IN_BYTES;m--)
                        input[m][1] = 15;//15==FF
                }else{
                    for(int k=0;k<SIZE_OF_EACH_ENCRYPTION_IN_BYTES;k++)
                        input[k][1]=BFs.inverse(inputStream.read(),irreducible_polynomial);
                }

                for(int k=0;k<TIMES_ENCRYPTION;k++){
                    output = MBFs.plus(MBFs.multiply(D[k],input,irreducible_polynomial),key[k]);
                }

                for(int k=0;k<SIZE_OF_EACH_ENCRYPTION_IN_BYTES;k++)
                    outputStream.write(output[k][1]);

                //if(inputStream.)

                inputStream.close();
                outputStream.close();
            }

        }catch(Exception e){
            System.out.println("Ack Ack!!!");
        }

    }
}
