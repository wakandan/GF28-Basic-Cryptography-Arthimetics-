package cryptography2;

public class Round {    
    private static final int[][] M = {{0x4e, 0xc7, 0x87, 0x49},
                               {0x49, 0x4e, 0xc7, 0x87},
                               {0x87, 0x49, 0x4e, 0xc7},
                               {0xc7, 0x87, 0x49, 0x4e}}; 
    private static final int[][] M1 = {{105, 187, 210, 105}, 
                                {105, 105, 187, 210}, 
                                {210, 105, 105, 187}, 
                                {187, 210, 105, 105}};
    public static int[] encrypt(int[] msg, int[] key) {
        /*Representation of 1 encryption round*/
        int[] inp = {msg[2], msg[1], msg[0], msg[3]};
        inp = MatrixOps.inv(inp);
        int[] mulResult = MatrixOps.mul(M, inp);
        int[] result = MatrixOps.add(mulResult, key);
        return result;
    }
    public static int[] decrypt(int[] msg, int[] key) {
        /*Representation of 1 decryption round*/
        int[] msg2 = MatrixOps.add(msg, key);
        int[] tmp = MatrixOps.mul(M1, msg2);
        int[] result = {tmp[2], tmp[1], tmp[0], tmp[3]};
        result = MatrixOps.inv(result);
        return result;
    }
}
