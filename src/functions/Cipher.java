package functions;

public class Cipher {
    Ops ops;
    MatrixOps mops;
    int[][] D, D1;
    int[] C0;
    public Cipher() {
        this.ops = new Ops();
        this.mops = new MatrixOps();
        mops.setOps(ops);
    }
    
    public int[] encrypt(int[] msg){
        return mops.add(mops.mul(D, msg), C0);
    }
    public int[] decrypt(int[] msg){
        return mops.funcS(mops.mul(D1, mops.add(msg, C0)));
    }

    public void setOps(Ops ops) {
        this.ops = ops;
    }

    public void setMops(MatrixOps mops) {
        this.mops = mops;
    }

    public void setD(int[][] d) {
        D = d;
    }

    public void setD1(int[][] d1) {
        D1 = d1;
    }

    public void setC0(int[] c0) {
        C0 = c0;
    }
    
    
}
