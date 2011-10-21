package cryptography2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBlockCipher {
    int[] key = { 44, 55, 66, 77 };

    int[] iv = { 100, 200, 50, 40 };

    int[] msg = { 1, 2, 3, 4 };

    @Before
    public void setUp() {
        GF28.init("table.txt");
    }

    @Test
    public void testRound() {
        int[] test1 = Round.encrypt(iv, key);
        int[] test2 = Round.decrypt(test1, key);
        for (int i = 0; i < test1.length; i++)
            assertEquals(iv[i], test2[i]);
    }

    @Test
    public void testEncryptDecrypt() {
        int[] encrypted = new BlockEncryption().setKey(key).makeKeyStream().work(msg);
        int[] decrypted = new BlockDecryption().setKey(key).makeKeyStream().work(encrypted);
        for (int i = 0; i < encrypted.length; i++)
            assertEquals(msg[i], decrypted[i]);

    }

    @Test
    public void testCipher() throws IOException {
        int[] input = {1,2,3,4,5,6,7,8,9};
        int[] output = new int[input.length];
        String inputFilename = "test_plaintext.txt";
        String outFilename = "test_ciphered.txt";
        String decipheredFilename = "test_deciphered.txt";
        FileWriter fw = new FileWriter(inputFilename);
        /*Write input to a test file*/
        for(int i=0; i<input.length; i++)
            fw.write(input[i]);
        fw.close();
        C2 blackbox = new C2(key);
        blackbox.encrypt(inputFilename, outFilename);
        blackbox.decrypt(outFilename, decipheredFilename);
        FileReader fr = new FileReader(decipheredFilename);
        for(int i=0; i<input.length; i++) {
            output[i] = fr.read();
            assertEquals(input[i], output[i]);
        }
        fr.close();
        (new File(outFilename)).delete();
        (new File(decipheredFilename)).delete();
    }
    
    @Test
    public void testProgram() throws IOException {
    	String[] encrypt_args = {"e", "test_plaintext.txt", "key.txt"}; 
    	C2.main(encrypt_args);
    	File plainFile = new File("test_plaintext.txt");
    	File encryptedFile = new File("encrypted_test_plaintext.txt");
    	assertTrue(encryptedFile.exists());
    	String[] decrypt_args = {"d", "encrypted_test_plaintext.txt", "key.txt"}; 
    	C2.main(decrypt_args);  
    	File decryptedFile = new File("decrypted_encrypted_test_plaintext.txt");
    	assertTrue(decryptedFile.exists());
    	assertEquals(plainFile.length(), decryptedFile.length());
    	long len = plainFile.length();
    	FileReader plainFileReader = new FileReader("test_plaintext.txt");
    	FileReader decryptedFileReader = new FileReader("decrypted_encrypted_test_plaintext.txt");
    	for(int i=0; i<len%10; i++)
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
        System.out.println("");
    }

}
