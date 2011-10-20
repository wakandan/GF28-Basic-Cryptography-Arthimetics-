package cryptography2;

public abstract class BlockCipher {
    protected static final int NUM_ROUND = 3;

    public int[] key;

    public abstract int[] calcRoundKey(int roundNum);

    public abstract int[] calcMsg(int[] msg, int[] key);

    public abstract int[] init(int[] msg);

    public abstract int[] finalize(int[] msg);

    public int[][] initKeys(int[] key, int num_round) {
        /*
         * Generate key set for use. Each round will just reference a key, no
         * need to generate again
         */
        int[][] results = new int[num_round][key.length];
        int[] tmpKey = key;
        for (int i = 0; i < num_round; i++) {
            results[i] = Key.encrypt(tmpKey, i + 1);
            tmpKey = results[i];
        }
        return results;
    }

    public int[][] keys;

    protected void printarray(int[] a) {
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
        System.out.println("");
    }

    public int[] work(int[] msg, int[] key) {
        this.key = key;
        int[] roundKey = key;
        int[] tmpMsg = init(msg);
        keys = initKeys(key, NUM_ROUND);
        for (int i = 1; i <= NUM_ROUND; i++) {
            roundKey = calcRoundKey(i);
            /* First round, need to use an iv value */
            tmpMsg = calcMsg(tmpMsg, roundKey);
        }
        return finalize(tmpMsg);
    }
}
