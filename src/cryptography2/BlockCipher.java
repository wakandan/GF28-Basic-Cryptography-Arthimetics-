package cryptography2;

public abstract class BlockCipher {
	private static final int NUM_ROUND = 3;
	private static final int BLOCK_SIZE = 4;

	public int getBlockSize() {
		return BLOCK_SIZE;
	}

	public int[] key;

	public int[][] keystream;

	public void setKeystream(int[][] keystream) {
		this.keystream = keystream;
	}

	public int getNumRound() {
		return NUM_ROUND;
	}

	public abstract int[] calcRoundKey(int roundNum);

	public abstract int[] calcMsg(int[] msg, int[] key);

	public abstract int[] init(int[] msg);

	public abstract int[] finalize(int[] msg);

	public int[] work(int[] msg) {
		int[] roundKey;
		int[] tmpMsg = init(msg);
		for (int i = 1; i <= NUM_ROUND; i++) {
			roundKey = calcRoundKey(i);
			/* First round, need to use an iv value */
			tmpMsg = calcMsg(tmpMsg, roundKey);
		}
		return finalize(tmpMsg);
	}

	public BlockCipher setKey(int[] key) {
		this.key = key;
		return this;
	}

	public BlockCipher makeKeyStream() {
		keystream = new int[NUM_ROUND][key.length];
		int[] tmpKey = key;
		for (int i = 0; i < NUM_ROUND; i++) {
			keystream[i] = Key.encrypt(tmpKey, i + 1);
			tmpKey = keystream[i];
		}
		return this;
	}
	
	public int[][] getKeyStream(){
		return this.keystream;
	}
}
