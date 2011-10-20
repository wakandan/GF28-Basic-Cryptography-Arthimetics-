package cryptography2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Random;

public class C2 {
	int[] stream;
	int[][] keystream;
	int[] key;
	int numRound;
	int blockSize;
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
		int i = 0;
		Random randGen = new Random();
		FileOutputStream fw = null;
		/* Create a new encryption block to encrypt the stream of byte */
		blockCipher = new BlockEncryption();
		this.setBlockCipher(blockCipher);
		this.readFromFile(inputFilename);
		fw = new FileOutputStream(outfilename);

		/* Generate the initial value. */
		int[] iv = new int[blockSize];
		int[] iv_ECB = new int[blockSize];
		for (int j = 0; j < blockSize; j++)
			iv[j] = randGen.nextInt(255);
		iv_ECB = blockCipher.work(iv);
		/* Encrypt the iv in ECB mode and save it first in the file */
		for (int j = 0; j < blockSize; j++)
			fw.write(iv_ECB[j]);
		int[] prevMsg = iv;
		while (i < stream.length) {
			int low = i;
			int high = i + blockSize;
			if (high >= stream.length)
				high = stream.length;
			int[] msg = new int[blockSize];
			/* Get the plain text block */
			for (int j = low; j < high; j++)
				msg[j % blockSize] = stream[j];
			/* XOR the current block with cipher text, then encrypt */
			prevMsg = blockCipher.work(MatrixOps.add(msg, prevMsg));
			for (int j = 0; j < blockSize; j++)
				fw.write(prevMsg[j]);
			i = high;
		}
		fw.close();
	}

	public void decrypt(String inputFilename, String outfilename)
			throws FileNotFoundException, IOException {
		/* Decrypt a stream of int in CBC mode */				
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
			for (int j = 0; j < blockSize; j++)
				fw.write(tmpMsg[j]);
			/* Copy the plain text for the next iteration */
			copy(prevMsg, msg, 0);
			i += blockSize;
		}
		fw.close();
	}

	public static void main(String[] argv) {
		boolean encrypt = false;
		GF28.init("D:/doc/workspace/CryptoAssignment1/src/cryptography2/table.txt");
		int[] key = { 44, 55, 66, 77 };
		String infilename;
		String outfilename;
		if (encrypt) {
			infilename = "plaintext.txt";
			outfilename = "ciphertext.txt";
		} else {
			infilename = "ciphertext.txt";
			outfilename = "decipheredtext.txt";
		}
		C2 blackBox = new C2(key);
		try {
			if (encrypt) {
				blackBox.encrypt(infilename, outfilename);
			} else {
				blackBox.decrypt(infilename, outfilename);
			}
		} catch (IOException e) {
			System.out.println("Can not write to file " + outfilename);
			e.printStackTrace();
		}
	}

	private void copy(int[] a, int[] b, int start) {
		/* Copy ints from b into a, index in a from start */
		for (int i = 0; i < a.length; i++)
			a[i + start] = b[i];
	}

	private void printarray(int[] a) {
		for (int i = 0; i < a.length; i++)
			System.out.print(a[i] + " ");
	}
}
