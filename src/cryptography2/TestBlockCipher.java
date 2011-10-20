package cryptography2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestBlockCipher {
    int[] key = { 44, 55, 66, 77 };

    int[] iv = { 100, 200, 50, 40 };

    int[] msg = { 1, 2, 3, 4 };

    @Before
    public void setUp() {
        GF28.init("D:/doc/workspace/CryptoAssignment1/src/cryptography2/table.txt");
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
        int[] encrypted = new BlockEncryption().work(msg, key);
        int[] decrypted = new BlockDecryption().work(encrypted, key);
        for (int i = 0; i < encrypted.length; i++)
            assertEquals(msg[i], decrypted[i]);

    }

    @Test
    public void testCipher() {
        
    }
    
    public void printarray(int[] a) {
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
        System.out.println("");
    }

}
