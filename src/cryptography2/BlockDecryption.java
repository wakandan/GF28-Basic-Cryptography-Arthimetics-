package cryptography2;

public class BlockDecryption extends BlockCipher {

	@Override
	public int[] calcRoundKey(int roundNum) {
		/* Apply key in the reverse order */
		return keystream[roundNum - 1];
	}

	@Override
	public int[] calcMsg(int[] msg, int[] key) {
		// TODO Auto-generated method stub
		return Round.decrypt(msg, key);
	}

	@Override
	public int[] init(int[] msg) {
		// TODO Auto-generated method stub
		return msg;
	}

	@Override
	public int[] finalize(int[] msg) {
		int i;
		/* Re-xor with the key */
		msg = MatrixOps.add(msg, key);

		/* Remove padding if there is any */
		for (i = key.length - 1; i >= 0 && msg[i] == 0xff; i--);
		if (i < msg.length - 1)
			msg[i + 1] = '\0';
		return msg;
	}
}
