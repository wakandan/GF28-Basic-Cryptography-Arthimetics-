package cryptography;
/**
 *
 * @author Bruce Nguyen
 */
public class BasicFunctions {
    // Condition: n > a > 0
    private int t1, t2, t3, a1, a2, a3, b1, b2, b3, q;

    public int inverse(int a, int irreducible_polynomial){
      if(a == 0) return 0;
      a1 = 1;
      a2 = 0;
      a3 = irreducible_polynomial;
      b1 = 0;
      b2 = 1;
      b3 = a;
      q  = divide (a3,b3);
      System.out.println("Calculating for "+a);
      while(b3>1){
        t1 = a1; t2 = a2; t3 = a3;
        a1 = b1; a2 = b2; a3 = b3;
        b1 = minus(t1,multiply(b1,q,irreducible_polynomial));
        //System.out.print(b1+"  ");
        b2 = minus(t2,multiply(b2,q,irreducible_polynomial));
        //System.out.print(b2+"  ");
        b3 = minus(t3,multiply(b3,q,irreducible_polynomial));
        //System.out.println(b3);
        q  = divide(a3,b3);        
      }
      return b2;
    }

    public int plus(int a, int b){
        return a^b;
    }

    public int minus(int a, int b){
        return a^b;
    }

    public int multiply(int a, int b, int irreducible_polynomial){
        int result = 0;
        int temp;

        temp = b;
        int count = 0;

        while(temp!=0){
            count++;
            temp = temp>>1;
        }

        while(b>0){
            b = b<<1;
        }

        for(;count>0;count--){
            result = result<<1;
            if(b<0) result = plus(result,a);
            b = b<<1;
        }

        result = modulus(result,irreducible_polynomial);

        return result;
    }

    public int divide (int n, int a){// return n/a in GF(2^8)
        if(n<a) return 0;
        if(n==a) return 1;
        
        int result = 0;
        int count = 0;

        while(n>0){
            n = n<<1;
            a = a<<1;
        }
        while(a>0){
            a = a<<1;
            count++;
        } 

        for(;count>=0;count--){
            result = result << 1;
            if(n<0){
                result++;
                n = n^a;
            }
            n = n<<1;
        }

        return result;
    }
    
    public int modulus(int n, int a){//n mod a
        if(n<a) return n;
        if(n==a) return 0;

        int bits_a_different_n = 0;
        int times_n_shift_left = 0;

        while(n>0){
            n = n<<1;
            times_n_shift_left++;
            a = a<<1;
        }
        while(a>0){
            a = a<<1;
            bits_a_different_n++;
        }

        for(;bits_a_different_n>=0;bits_a_different_n--){
            if(n<0) n = n^a;
            n = n<<1;
            times_n_shift_left++;
        }

        for(;times_n_shift_left>0;times_n_shift_left--)n = n>>>1;

        return n;
    }
    
/*    public int[] matrix_solve(int[][] a){
        for(int j=0; j < a[0].length;j++){
            for(int i=0; i < a.length;i++){
                
            }
        }
    }
 */
}
