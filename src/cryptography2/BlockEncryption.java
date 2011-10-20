package cryptography2;

public class BlockEncryption extends BlockCipher {

    @Override
    public int[] calcRoundKey(int numRound) {
        // TODO Auto-generated method stub
        return keys[numRound - 1];
    }

    @Override
    public int[] calcMsg(int[] msg, int[] key) {
        // TODO Auto-generated method stub
        return Round.encrypt(msg, key);
    }

    @Override
    public int[] init(int[] msg) {
        /* Do message padding before encryption */
        int[] result = new int[4];
        if (msg.length != 4) {
            for (int i = 0; i < msg.length; i++)
                result[i] = msg[i];
            for (int i = msg.length; i < 4; i++)
                result[i] = (int) 255;
            return MatrixOps.add(result, this.key);
        }
        return MatrixOps.add(msg, this.key);
    }

    @Override
    public int[] finalize(int[] msg) {
        return msg;
    }

}
