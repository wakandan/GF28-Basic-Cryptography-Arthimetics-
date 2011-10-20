package cryptography2;

/**
 *
 * @author Nguyen Quoc Phuong
 */

//convetion: 
//   ip == 'irreducible_polynomial'

import java.io.*;
import java.util.*;

public class GF28 {
    static private final int constant = 256;

    static public void initialized(String FileName, int ip) {
        BasicFunctions BFs = new BasicFunctions();
        try {
            FileWriter output = new FileWriter(FileName);

            // plus and minus
            for (int i = 0; i < constant; i++) {
                for (int j = 0; j < constant; j++) {
                    output.write(BFs.add(i, j, ip) + " ");
                }
                output.write("\n");
            }

            // multiply
            for (int i = 0; i < constant; i++) {
                for (int j = 0; j < constant; j++) {
                    output.write(BFs.multiply(i, j, ip) + " ");
                }
                output.write("\n");
            }

            // divide
            for (int i = 0; i < constant; i++) {
                for (int j = 0; j < constant; j++) {
                    output.write(BFs.divide(i, j, ip) + " ");
                }
                output.write("\n");
            }

            // addition and minus function
            for (int i = 0; i < constant; i++) {
                output.write(BFs.inverse(i, ip) + " ");
            }

            output.close();

        } catch (Exception e) {
            System.out.println("File " + FileName + " Not Found!");
        }

    }

    static int[][] plus = new int[constant][constant];

    static int[][] multiply = new int[constant][constant];

    static int[][] divide = new int[constant][constant];

    static int[] inverse = new int[constant];

    public static void init(String FileName) {
        try {
            Scanner input = new Scanner(new File(FileName));
            for (int i = 0; i < constant; i++)
                for (int j = 0; j < constant; j++)
                    plus[i][j] = input.nextInt();

            for (int i = 0; i < constant; i++)
                for (int j = 0; j < constant; j++)
                    multiply[i][j] = input.nextInt();

            for (int i = 0; i < constant; i++)
                for (int j = 0; j < constant; j++)
                    divide[i][j] = input.nextInt();

            for (int i = 0; i < constant; i++)
                inverse[i] = input.nextInt();
        } catch (FileNotFoundException e) {
            System.out.println("File "+FileName+" Not Found!");
        }
    }

    public static int add(int a, int b) {
        return plus[a][b];
    }

    public static int mul(int a, int b) {
        return multiply[a][b];
    }

    public static int div(int a, int b) {
        return divide[a][b];
    }

    public static int inv(int a) {
        return inverse[a];
    }

}
