/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cryptography2;

/**
 * 
 * @author Xinzhen
 */
public class Key {
    private BasicFunctions BFs = new BasicFunctions();

    // pass in key of r-1 and return key of r
    public static int[] encrypt(int[] k, int r) {
        int rc = 0;
        int[] consts  = {0, 0x8D, 0xCB, 0xE8};       
        rc = consts[r];
        int[] k1 = new int[4];
        for (int i = 0; i < k.length; i++) {
            // S int-substitution
            k1[i] = GF28.inv(k[(i + r) % 4]);
            // addition to round constant
            if (i >= 1)
                rc = 0x01;
            k1[i] = GF28.add(k1[i], rc);
        }
        return k1;
    }

    public static int[] decrypt(int[] k, int r) {
        int rc = 0;
        if (r == 1)
            rc = 0x8D;
        else if (r == 2)
            rc = 0xCB;
        else if (r == 3)
            rc = 0xE8;
        int[] k1 = new int[4];
        int a = 0;
        for (int i = 0; i < k.length; i++) {
            if (r == 1)
                a = (i + 3) % 4;
            else if (r == 2)
                a = (i + 2) % 4;
            else if (r == 3)
                a = (i + 1) % 4;

            // addition to round constant
            if (i >= 1)
                rc = 0x01;
            k[i] = GF28.add(k[i], rc);
            // S int-substitution
            k1[i] = GF28.inv(k[a]);
        }
        return k1;
    }
}
