package cryptography2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class C2 {
	int[] stream;
	int[][] keystream;
	int[] key;
	int numRound;
	int blockSize;
	int[] iv = null;
	BlockCipher blockCipher;

	public C2(int[] key) {
		this.key = key;
	}

	public void setBlockCipher(BlockCipher blockCipher) {
		this.blockCipher = blockCipher;
		this.numRound = blockCipher.getNumRound();
		this.blockSize = blockCipher.getBlockSize();
		this.makeKeyStream();
		this.blockCipher.setKeystream(keystream);
		this.blockCipher.setKey(key);
	}

	public void setIV(int[] iv) {
		this.iv = iv;
	}

	public void genRandomIV() {
		this.iv = new int[blockSize];
		Random randGen = new Random();
		for (int j = 0; j < blockSize; j++)
			iv[j] = randGen.nextInt(255);
	}

	private void makeKeyStream() {
		keystream = new int[numRound][key.length];
		int[] tmpKey = key;
		for (int i = 0; i < numRound; i++) {
			keystream[i] = Key.encrypt(tmpKey, i + 1);
			tmpKey = keystream[i];
		}
	}

	protected void readFromFile(String filename) throws IOException {
		/* Read from a file and save into a stream */
		File file = new File(filename);
		int filesize = (int) file.length();
		this.stream = new int[filesize];
		FileInputStream ip = new FileInputStream(filename);
		for (int i = 0; i < filesize; i++) {
			this.stream[i] = ip.read();
		}
	}

	public void encrypt(String inputFilename, String outfilename)
			throws IOException {
		/* Encrypt a stream of int in CBC mode */

		Random randGen = new Random();
		FileOutputStream fw = null;

		/* Create a new encryption block to encrypt the stream of byte */
		blockCipher = new BlockEncryption();
		this.setBlockCipher(blockCipher);
		this.readFromFile(inputFilename);
		fw = new FileOutputStream(outfilename);

		/* Generate the initial value. */
		int[] iv_ECB = new int[blockSize];
		if (this.iv == null)
			this.genRandomIV();

		iv_ECB = blockCipher.work(iv);

		/* Encrypt the iv in ECB mode and save it first in the file */
		for (int j = 0; j < blockSize; j++)
			fw.write(iv_ECB[j]);
		int[] prevMsg = iv;
		int i = 0;
		while (i < stream.length) {
			int low = i;
			int high = i + blockSize;
			if (high >= stream.length)
				high = stream.length;
			int[] msg = new int[blockSize];

			/* Get the plain text block */
			for (int j = low; j < high; j++)
				msg[j % blockSize] = stream[j];

			/* Do padding */
			if ((high - low) % blockSize != 0)
				for (int j = high % blockSize; j < blockSize; j++)
					msg[j] = 0xff;

			/* XOR the current block with plain text, then encrypt */
			prevMsg = blockCipher.work(MatrixOps.add(msg, prevMsg));
			for (int j = 0; j < blockSize; j++)
				fw.write(prevMsg[j]);
			i = high;
		}
		fw.close();
	}

	/* Decrypt a stream of int in CBC mode */
	public void decrypt(String inputFilename, String outfilename)
			throws FileNotFoundException, IOException {

		this.readFromFile(inputFilename);
		blockCipher = new BlockDecryption();
		this.setBlockCipher(blockCipher);
		FileOutputStream fw = new FileOutputStream(outfilename);
		int[] msg = new int[blockSize];
		int[] tmpMsg = new int[blockSize];
		int[] iv = new int[blockSize];

		/* Read the first 4 bytes for initial value */
		for (int i = 0; i < blockSize; i++)
			iv[i] = stream[i];

		/* Decipher the initial value, which was saved first in the file */
		iv = blockCipher.work(iv);
		int[] prevMsg = iv;
		int i = blockSize;

		/* While reaching the beginning of the file (as we read backward) */
		while (i < stream.length) {
			/* Get from the second cipher text block */
			for (int j = 0; j < blockSize; j++)
				msg[j] = stream[i + j];

			/* Decrypt and xor with the previous block */
			tmpMsg = blockCipher.work(msg);

			/* This should be the plain text */
			tmpMsg = MatrixOps.add(prevMsg, tmpMsg);
			for (int j = 0; j < blockSize && tmpMsg[j] != 0xff; j++)
				fw.write(tmpMsg[j]);

			/* Copy the plain text for the next iteration */
			copy(prevMsg, msg, 0);
			i += blockSize;
		}
		fw.close();
	}

	public static void main(String[] argv) throws IOException {
		if (argv.length != 3)
			bailOut();
		/* Init GF28 arithmetic data */
		GF28.init("table.txt");
		int[] key = readKeyFile(argv[2]);
		/* Create a new blackbox */
		C2 blackBox = new C2(key);

		String infilename = argv[1];
		String outfilename;

		/* Check for the action */
		if (argv[0].toUpperCase().compareTo("E") == 0) {
			outfilename = "encrypted_" + infilename;
			blackBox.encrypt(infilename, outfilename);
		} else if (argv[0].toUpperCase().compareTo("D") == 0) {
			outfilename = "decrypted_" + infilename;
			blackBox.decrypt(infilename, outfilename);
		} else
			bailOut();
	}

	private static int[] readKeyFile(String filename) throws IOException {
		/* Read the key */
		int[] inputkey;
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String[] inputKeyStr = br.readLine().split(" ");
		inputkey = new int[inputKeyStr.length];
		for (int i = 0; i < inputKeyStr.length; i++)
			inputkey[i] = Integer.parseInt(inputKeyStr[i]);
		return inputkey;
	}

	private void copy(int[] a, int[] b, int start) {
		/* Copy ints from b into a, index in a from start */
		for (int i = 0; i < a.length; i++)
			a[i + start] = b[i];
	}

	public static void bailOut() {
		System.out
				.println("Usage: C2 <action(D|E)> <data_file_name> <key_file>");
		System.out
				.println("Key file should contains 4 DECIMAL number in (0-255), space delimited");
		System.exit(1);
	}
}
