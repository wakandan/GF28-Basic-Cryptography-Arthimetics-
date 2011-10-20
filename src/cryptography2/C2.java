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

    private final int BLOCK_SIZE = 4;

    public C2(String filename) {
        readFromFile(filename);
    }

    private void readFromFile(String filename) {
        /* Read from a file and save into a stream */
        File file = new File(filename);
        int filesize = (int) file.length();
        this.stream = new int[filesize];
        try {
            FileInputStream ip = new FileInputStream(filename);
            for (int i = 0; i < filesize; i++) {
                this.stream[i] = ip.read();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void encrypt(int[] key, String outfilename) throws IOException {
        /* Encrypt a stream of int in CBC mode */
        int i = 0;
        Random randGen = new Random();
        FileOutputStream fw = null;
        BlockCipher blockEncryption = new BlockEncryption();

        fw = new FileOutputStream(outfilename);

        /* Generate the initial value. */
        int[] iv = new int[BLOCK_SIZE];
        int[] iv_ECB = new int[BLOCK_SIZE];
        for (int j = 0; j < BLOCK_SIZE; j++)
            iv[j] = randGen.nextInt(255);
        printarray(iv);
        System.out.println();
        iv_ECB = blockEncryption.work(iv, key);
        /* Encrypt the iv in ECB mode and save it first in the file */
        for (int j = 0; j < BLOCK_SIZE; j++)
            fw.write(iv_ECB[j]);
        int[] prevMsg = iv;
        while (i < stream.length) {
            int low = i;
            int high = i + BLOCK_SIZE;
            if (high >= stream.length)
                high = stream.length;
            int[] msg = new int[BLOCK_SIZE];
            /* Get the plain text block */
            for (int j = low; j < high; j++)
                msg[j % BLOCK_SIZE] = stream[j];

            /* XOR the current block with cipher text, then encrypt */
            prevMsg = blockEncryption.work(MatrixOps.add(msg, prevMsg), key);
            for (int j = 0; j < BLOCK_SIZE; j++)
                fw.write(prevMsg[j]);
            printarray(prevMsg);
            i = high;
        }
        fw.close();
    }

    private void decrypt(int[] key, String outfilename)
            throws FileNotFoundException, IOException {
        /* Decrypt a stream of int in CBC mode */
        int[] iv = new int[BLOCK_SIZE];
        int[] msg = new int[BLOCK_SIZE];
        int[] tmpMsg = new int[BLOCK_SIZE];
        FileOutputStream fw = new FileOutputStream(outfilename);
        /*Read the first 4 bytes for initial value*/
        for (int i = 0; i < BLOCK_SIZE; i++)
            iv[i] = stream[i];
        /* Decipher the initial value, which was saved first in the file */
        BlockCipher blockDecryption = new BlockDecryption();
        iv = blockDecryption.work(iv, key);
        printarray(iv);
        System.out.println();
        int[] prevMsg = iv;
        int i = BLOCK_SIZE;
        /* While reaching the beginning of the file (as we read backward) */
        while (i < stream.length) {
            /* Get from the second cipher text block */
            for (int j = 0; j < BLOCK_SIZE; j++)
                msg[j] = stream[i + j];

            /* Decrypt and xor with the previous block */
            tmpMsg = blockDecryption.work(msg, key);
            /* This should be the plain text */
            tmpMsg = MatrixOps.add(prevMsg, tmpMsg);
            for(int j=0; j<BLOCK_SIZE; j++)
                System.out.print((char)tmpMsg[j]);
            /* Copy the plain text for the next iteration */
            copy(prevMsg, msg, 0);
            i += BLOCK_SIZE;
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
        C2 blackBox = new C2(infilename);
        try {
            if (encrypt)
                blackBox.encrypt(key, outfilename);
            else
                blackBox.decrypt(key, outfilename);
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
