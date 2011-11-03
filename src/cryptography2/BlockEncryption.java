package cryptography2;

public class BlockEncryption extends BlockCipher {

	@Override
	public int[] calcRoundKey(int numRound) {
		// TODO Auto-generated method stub
		return keystream[numRound-1];
	}

	@Override
	public int[] calcMsg(int[] msg, int[] key) {
		// TODO Auto-generated method stub
		return Round.encrypt(msg, key);
	}

	@Override
	public int[] init(int[] msg) {
		/* Do message padding before encryption */
		return MatrixOps.add(msg, this.key);
	}

	@Override
	public int[] finalize(int[] msg) {
		return msg;
	}

}
