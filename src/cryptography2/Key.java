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

    // pass in key of r-1 and return key of r
    public static int[] encrypt(int[] k, int r) {
        int rc = 0;
        switch(r) {
        case 1: rc = 0x8D; break;
        case 2: rc = 0xCB; break;
        case 3: rc = 0xE8; break;
        }
        int[] consts  = {rc, 1, 1, 1};       
        int[] k1 = new int[4];
        for (int i = 0; i < k.length; i++) 
            k1[i] = GF28.inv(k[(i + r) % 4]);        
        return MatrixOps.add(k1, consts);
    }
}
