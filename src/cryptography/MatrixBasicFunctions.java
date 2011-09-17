/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cryptography;

/**
 * 
 * @author Bruce Nguyen
 */
public class MatrixBasicFunctions {
    private BasicFunctions BFs = new BasicFunctions();

    public int[][] multiply(int[][] a, int[][] b, int irreducible_polynomial) {
        int[][] result = new int[a.length][b[0].length];

        if (a[0].length != b.length)
            return null;

        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b[0].length; j++) {
                result[i][j] = 0;
                for (int k = 0; k < b.length; k++)
                    result[i][j] = BFs.plus(result[i][j], BFs.multiply(a[i][k],
                            b[i][k], irreducible_polynomial));
            }

        return result;
    }

    /*
     * public int[] multiply(int[][] a, int[] b, int irreducible_polynomial){
     * int[] result = new int[b.length];
     * 
     * 
     * if(a[0].length!=b.length)return null;
     * 
     * for(int i=0;i<a.length;i++){ result[i]=0; for(int k=0;k<b.length;k++)
     * result[i] =
     * BFs.plus(result[i],BFs.multiply(a[i][k],b[k],irreducible_polynomial)); }
     * return result; }
     */

    public int[][] plus(int[][] a, int[][] b) {
        int[][] result = new int[a.length][a[0].length];
        if ((a.length != b.length) && (a[0].length != b[0].length))
            return null;

        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++) {
                result[i][j] = BFs.plus(a[i][j], b[i][j]);
            }

        return result;
    }

    /*
     * public int[] plus(int[] a, int[] b){ int[] result = new int[a.length];
     * if(a.length!=b.length) return null;
     * 
     * for(int i=0;i<a.length;i++) result[i]=BFs.plus(a[i],b[i]);
     * 
     * return result; }
     */
}
