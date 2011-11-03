package cryptography2;

public class MatrixOps {
    public static int[] mul(int[][] a, int[] b) {
        if (a[0].length != b.length)
            return null;
        int[] result = new int[b.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = 0;
            for (int j = 0; j < b.length; j++) {
                int x = GF28.mul(a[i][j], b[j]);
                result[i] = GF28.add(result[i], x);
            }
        }
        return result;
    }

    public static int[] add(int[] a, int[] b) {
        if (a.length != b.length)
            return null;
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = GF28.add(a[i], b[i]);
        }
        return result;
    }
    
    /*Using GF28 to inverse each of the array elements*/
    public static int[] inv(int[] a) {
    	int[] r = new int[a.length];
    	for(int i=0; i<a.length; i++)
    		r[i] = GF28.inv(a[i]);
    	return r;
    }	
}
