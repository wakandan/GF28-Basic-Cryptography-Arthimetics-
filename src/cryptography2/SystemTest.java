package cryptography2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemTest {
	int[] key = { 44, 55, 66, 77 };

	int[] iv = { 100, 200, 50, 40 };

	int[] msg = { 1, 2, 3, 4 };

	@Before
	public void setUp() {
		GF28.init("table.txt");
	}

	@Test
	public void testGF28() {
		assertEquals(GF28.add(100, 200), 172);
		assertEquals(GF28.inv(0), 0);
		assertEquals(GF28.mul(100, 200), 0xB5);
		assertEquals(GF28.div(100, 200), 0x8D);
	}

	@Test
	public void testRound() {
		int[] test1 = Round.encrypt(iv, key);
		int[] test2 = Round.decrypt(test1, key);
		for (int i = 0; i < test1.length; i++)
			assertEquals(iv[i], test2[i]);
	}

	@Test
	public void crossTestBlock() {
		int[] expected = { 0x5a, 0xa9, 0x1e, 0xe8 };
		int[] msg = { 203, 136, 64, 111 };
		int[] key = { 11, 22, 33, 44 };
		BlockEncryption be = new BlockEncryption();
		be.setKey(key);
		be.makeKeyStream();
		int[][] keystream = be.getKeyStream();
		int[] actual = Round.encrypt(Round.encrypt(
				Round.encrypt(MatrixOps.add(msg, key), keystream[0]),
				keystream[1]), keystream[2]);
		assertArrayEquals(expected, actual);
	}

	@Test
	public void crossTestKeygen() {
		int[] roundKey3 = { 200, 184, 11, 93 };
		int[] roundKey2 = { 142, 41, 81, 58 };
		int[] roundKey1 = { 237, 111, 49, 193 };
		int[] key = { 11, 22, 33, 44 };
		BlockEncryption be = new BlockEncryption();
		be.setKey(key);
		be.makeKeyStream();
		assertArrayEquals(be.keystream[0], roundKey1);
		assertArrayEquals(be.keystream[1], roundKey2);
		assertArrayEquals(be.keystream[2], roundKey3);
	}

	@Test
	public void testBlock() {
		int[] key = { 1, 2, 3, 4 };
		int[] msg = { 1, 0, 0, 0 };
		BlockEncryption be = new BlockEncryption();
		be.setKey(key);
		be.makeKeyStream();
		int[][] keystream = be.getKeyStream();
		int[] actuals = be.work(msg);
		int[] expected = Round.encrypt(Round.encrypt(
				Round.encrypt(MatrixOps.add(msg, key), keystream[0]),
				keystream[1]), keystream[2]);
		assertArrayEquals(expected, actuals);
	}

	@Test
	public void testKeyGen() {
		int[] key = { 0, 1, 1, 1 }; /* Previous key */
		int[] expectedRoundKey1 = { 0x8C, 0, 0, 1 }; /* Key for round 1 */
		int[] expectedRoundKey2 = { 0xCB, 0, 0xF6, 1 }; /* Key for round 1 */
		assertArrayEquals(expectedRoundKey1, Key.encrypt(key, 1));
		assertArrayEquals(expectedRoundKey2,
				Key.encrypt(Key.encrypt(key, 1), 2));
	}

	@Test
	public void testEncryptDecrypt() {
		int[] encrypted = new BlockEncryption().setKey(key).makeKeyStream()
				.work(msg);
		int[] decrypted = new BlockDecryption().setKey(key).makeKeyStream()
				.work(encrypted);
		for (int i = 0; i < encrypted.length; i++)
			assertEquals(msg[i], decrypted[i]);

	}

	@Test
	public void testCBCMode() {
		int[] key = { 1, 0, 0, 0 };
		int[] msg = { 0, 1, 2, 3, 4, 5, 6, 7 };
		int[] iv = { 1, 1, 1, 1 };
		C2 blackBox = new C2(key);
	}

	@Test
	public void testCipher() throws IOException {
		int[] input = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] output = new int[input.length];
		String inputFilename = "test_plaintext.txt";
		String outFilename = "test_ciphered.txt";
		String decipheredFilename = "test_deciphered.txt";
		FileWriter fw = new FileWriter(inputFilename);
		/* Write input to a test file */
		for (int i = 0; i < input.length; i++)
			fw.write(input[i]);
		fw.close();
		C2 blackbox = new C2(key);
		blackbox.encrypt(inputFilename, outFilename);
		blackbox.decrypt(outFilename, decipheredFilename);
		FileReader fr = new FileReader(decipheredFilename);
		for (int i = 0; i < input.length; i++) {
			output[i] = fr.read();
			assertEquals(input[i], output[i]);
		}
		fr.close();
		(new File(outFilename)).delete();
		(new File(decipheredFilename)).delete();
	}

	@Test
	public void crossTestCipher() throws IOException {
		// String oriFileName = "plaintext.bin";
		String oriFileName = "giang_Plain.txt";
		String keyFileName = "giang_key.txt";
		String cipherFileName = "giang_encrypted.bin";
		String encryptedFileName = "encrypted_" + oriFileName;
		String decryptedFileName = "decrypted_giang_encrypted.bin";
		String[] decrypt_args = { "d", cipherFileName, keyFileName };		
		C2.main(decrypt_args);
		FileReader decryptedFileReader = new FileReader(decryptedFileName);
		FileReader oriFileReader = new FileReader(oriFileName);
		for (int i = 0; i < 10; i++) 
			assertEquals(decryptedFileReader.read(), oriFileReader.read());
	}

	@Test
	public void testProgram() throws IOException {
		String[] encrypt_args = { "e", "test_plaintext.txt", "key.txt" };
		C2.main(encrypt_args);
		File plainFile = new File("test_plaintext.txt");
		File encryptedFile = new File("encrypted_test_plaintext.txt");
		assertTrue(encryptedFile.exists());
		String[] decrypt_args = { "d", "encrypted_test_plaintext.txt",
				"key.txt" };
		C2.main(decrypt_args);
		File decryptedFile = new File("decrypted_encrypted_test_plaintext.txt");
		assertTrue(decryptedFile.exists());
		assertEquals(plainFile.length(), decryptedFile.length());
		long len = plainFile.length();
		FileReader plainFileReader = new FileReader("test_plaintext.txt");
		FileReader decryptedFileReader = new FileReader(
				"decrypted_encrypted_test_plaintext.txt");
		for (int i = 0; i < len % 10; i++)
			assertEquals(plainFileReader.read(), decryptedFileReader.read());
		plainFileReader.close();
		decryptedFileReader.close();
		encryptedFile.delete();
		decryptedFile.delete();
	}

	@After
	public void tearDown() {

	}

	public void printarray(int[] a) {
		for (int i = 0; i < a.length; i++)
			System.out.print(a[i] + " ");
		System.out.println("\nOR");
		for (int i = 0; i < a.length; i++)
			System.out.printf("%X ", a[i]);
	}

}
