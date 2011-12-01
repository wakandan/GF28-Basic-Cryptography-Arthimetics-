package cryptography2;

/**
 * @author: Bruce Nguyen
 * @maintainer: Kand 
 */
public class BasicFunctions {
    // Condition: n > a > 0
    private int t1, t2, t3, a1, a2, a3, b1, b2, b3, q;

    /*Find inversion using Extended Euclid algorithm*/
    public int inverse(int a, int irreducible_polynomial) {
        if (a == 0)
            return 0;
        a1 = 1;
        a2 = 0;
        a3 = irreducible_polynomial;
        b1 = 0;
        b2 = 1;
        b3 = a;
        q = divide(a3, b3, irreducible_polynomial);

        while (b3 > 1) {
            t1 = a1;
            t2 = a2;
            t3 = a3;
            a1 = b1;
            a2 = b2;
            a3 = b3;
            b1 = minus(t1, multiply(b1, q, irreducible_polynomial),
                    irreducible_polynomial);
            b2 = minus(t2, multiply(b2, q, irreducible_polynomial),
                    irreducible_polynomial);
            b3 = minus(t3, multiply(b3, q, irreducible_polynomial),
                    irreducible_polynomial);
            q = divide(a3, b3, irreducible_polynomial);
        }
        return b2;
    }

    /*Add 2 numbers. Basically just XOR and MOD the polynomial*/
    public int add(int a, int b, int irreducible_polynomial) {
        return modulus(a ^ b, irreducible_polynomial);
    }
    
    /*Subtract 2 numbers. Same as add function*/
    public int minus(int a, int b, int irreducible_polynomial) {
        return modulus(a ^ b, irreducible_polynomial);
    }

    /*Multiply 2 numbers*/
    public int multiply(int a, int b, int irreducible_polynomial) {
        int result = 0;
        int temp;

        temp = b;
        int count = 0;

        while (temp != 0) {
            count++;
            temp = temp >>> 1;
        }

        while (b > 0) {
            b = b << 1;
        }

        for (; count > 0; count--) {
            result = result << 1;
            if (b < 0)
                result = add(result, a, irreducible_polynomial);
            b = b << 1;
        }

        result = modulus(result, irreducible_polynomial);

        return result;
    }

    /*Divide a by b, using multiplication of a and inverse of b*/
    public int divide(int n, int a, int irreducible_polynomial) {// return n/a
                                                                 // in GF(2^8)
        if (n < a)
            return 0;
        if (n == a)
            return 1;

        int result = 0;
        int count = 0;

        while (n > 0) {
            n = n << 1;
            a = a << 1;
        }
        while (a > 0) {
            a = a << 1;
            count++;
        }

        for (; count >= 0; count--) {
            result = result << 1;
            if (n < 0) {
                result++;
                n = n ^ a;
            }
            n = n << 1;
        }
        result = modulus(result, irreducible_polynomial);

        return result;
//        return multiply(n, inverse(a, irreducible_polynomial), irreducible_polynomial);
    }

    /*Self explained name */
    public int modulus(int n, int a) {// n mod a
        if (n < a)
            return n;
        if (n == a)
            return 0;

        int bits_a_different_n = 0;
        int times_n_shift_left = 0;

        while (n > 0) {
            n = n << 1;
            times_n_shift_left++;
            a = a << 1;
        }
        while (a > 0) {
            a = a << 1;
            bits_a_different_n++;
        }

        for (; bits_a_different_n >= 0; bits_a_different_n--) {
            if (n < 0)
                n = n ^ a;
            n = n << 1;
            times_n_shift_left++;
        }

        for (; times_n_shift_left > 0; times_n_shift_left--)
            n = n >>> 1;

        return n;
    }
}
