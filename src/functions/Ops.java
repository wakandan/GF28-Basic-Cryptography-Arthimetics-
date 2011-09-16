package functions;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ops {
    int[][] sum_table;

    int[][] mul_table;

    int[][] div_table;

    int[] inv_table;

    final static private int NUM_ROW = 255;

    public Ops() {
        try {
            sum_table = read_file("resources/sum_table.txt");
            mul_table = read_file("resources/mul_table.txt");
            div_table = read_file("resources/div_table.txt");
            inv_table = read_file1("resources/inv_table.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }

    private int[][] read_file(String filename) throws FileNotFoundException,
            IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename)));
        int[][] result = new int[NUM_ROW][NUM_ROW];
        String strLine;
        int counter = 0;
        while ((strLine = in.readLine()) != null) {
            String[] tmp = strLine.split(" ");
            for (int i = 0; i < NUM_ROW; i++) {
                result[counter][i] = Integer.parseInt(tmp[i]);
            }
            counter++;
        }
        return result;
    }

    private int[] read_file1(String filename) throws FileNotFoundException,
            IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename)));
        int[] result = new int[NUM_ROW];
        String strLine;
        while ((strLine = in.readLine()) != null) {
            String[] tmp = strLine.split(" ");
            for (int i = 0; i < NUM_ROW; i++) {
                result[i] = Integer.parseInt(tmp[i]);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Ops ops = new Ops();
        for (int i = 0; i < NUM_ROW; i++) {
            System.out.print(ops.inv_table[i] + " ");
        }
    }

    public int add(int a, int b) {
        if (a == 0 || b == 0)
            return a + b;
        else
            return sum_table[a][b];
    }

    public int mul(int a, int b) {
        if (a == 0 || b == 0)
            return 0;
        else
            return mul_table[a][b];
    }

    public int inv(int a) {
        if (a == 0)
            return -1;
        else
            return inv_table[a];
    }

}
