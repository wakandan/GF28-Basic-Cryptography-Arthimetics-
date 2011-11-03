package functions;

import functions.Ops;

public class MatrixOps {
    Ops ops;

//    int[][] add(int[][] a, int[][] b) {
//        int[][] result = new int[a.length][b[0].length];
//
//        if (a[0].length != b.length)
//            return null;
//
//        for (int i = 0; i < a.length; i++)
//            for (int j = 0; j < b[0].length; j++) {
//                result[i][j] = 0;
//                for (int k = 0; k < b.length; k++)
//                    result[i][j] = ops.add(result[i][j],
//                            ops.mul(a[i][k], b[i][k]));
//            }
//
//        return result;
//    }
    /*Matrix addition in GF28. This is the implementation for 
     * one dimension matrix only. For 2 dimension adding, please
     * uncomment the above part*/
    int[] add(int[] a, int[] b) {
        if (a.length != b.length)
            return null;
        int[] result = new int[a.length];

        for (int i = 0; i < a.length; i++)
            result[i] = a[i]+b[i];
        return result;
    }

       
//    int[][] mul(int[][] a, int[][] b) {
//        int[][] result = new int[a.length][a[0].length];
//        if ((a.length != b.length) && (a[0].length != b[0].length))
//            return null;
//
//        for (int i = 0; i < a.length; i++)
//            for (int j = 0; j < a[0].length; j++) {
//                result[i][j] = ops.add(result[i][j], ops.mul(a[i][j], b[i][j]));
//            }
//
//        return result;
//    }
    
    /*Matrix multiplication in GF28. This is the implementation for
     * 4x4 matrix mulplies with 4x1 matrix only. For matrix multiplication 
     * for 4x4 with 4x4, please uncomment the above*/
    int[] mul(int[][] a, int[] b) {
        int[] result = new int[a.length];
        if ((a.length != b.length) && (a[0].length != b.length))
            return null;

        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++) {
                result[i] = ops.add(result[i], ops.mul(a[i][j], b[i]));
            }

        return result;
    }

    public void setOps(Ops ops) {
        this.ops = ops;
    }

    /*Function S in the equation. Apply function S again on the
     * result we have the inversion of function S*/
    public int[] funcS(int[] a) {
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            if (a[i] == 0)
                result[i] = 0;
            else
                result[i] = ops.inv(a[i]);
        }
        return result;
    }       

    /*Inverse a matrix in GF2_8.
     * /*Reference http://www.cg.info.hiroshima-cu.ac.jp/~miyazaki/knowledge/teche23.html*/
//    public int[][] minv(int[][] a){
//        
//    }
    
    public int[][] cofactorMatrix(int[][] a, int i, int j){
        int[][] result = new int[a.length][a[0].length];
        for(int i=0; i<a.length; i++) {
            for(int j=0; j<a[0].length; j++) {
                
            }
        }
    }
    
}
