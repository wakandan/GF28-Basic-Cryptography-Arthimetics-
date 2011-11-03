package cryptography2;

public class BlockDecryption extends BlockCipher {

	@Override
	public int[] calcRoundKey(int roundNum) {
		/* Apply key in the reverse order */
		return keystream[keystream.length-roundNum];
	}

	@Override
	public int[] calcMsg(int[] msg, int[] key) {
		return Round.decrypt(msg, key);
	}

	@Override
	public int[] init(int[] msg) {
		return msg;
	}

	@Override
	public int[] finalize(int[] msg) {
		/* Re-xor with the key */
		return MatrixOps.add(msg, key);
	}
}
