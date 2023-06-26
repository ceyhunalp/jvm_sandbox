package sample;

import java.util.Random;

public class SampleClass {

    final static double NANOSECS = 1E9;
    // No canonicalization
    public static void serializeResult(double[] result, double[] bufResult) {
        for (int i = 0; i < result.length; i++) {
            long lng = Double.doubleToRawLongBits(result[i]);
            for (int j = 0; j < 8; j++) {
                bufResult[i * 8 + (7 - j)] =
                        (byte) ((lng >> ((7 - j) * 8)) & 0xff);
            }
        }
    }

    // Canonicalized
    public static void serializeCanonResult(double[] result, double[] bufResult) {
        for (int i = 0; i < result.length; i++) {
            long lng = Double.doubleToLongBits(result[i]);
            for (int j = 0; j < 8; j++) {
                bufResult[i * 8 + (7 - j)] =
                        (byte) ((lng >> ((7 - j) * 8)) & 0xff);
            }
        }
    }

    public static void main(String[] args) {
        double x = Math.sin(0);
        System.out.printf("%.9f\n", x);

        //long l = 0xfff0000000000009L;
        //System.out.println(l);
        //double d = Double.longBitsToDouble(l);
        //System.out.println(Double.doubleToLongBits(d));
        //System.out.println(Double.doubleToRawLongBits(d));

        //double[] result = new double[4000000];
        //Random rand = new Random();
        //for (int i = 0; i < 4000000; i++) {
            //result[i] = rand.nextDouble();
        //}
        //double[] bufResult = new double[4000000 * 8];
        //long start, end;
        //double[] times = new double[110];
        //for (int i = 0; i < 110; i++) {
            //start = System.nanoTime();
            //serializeResult(result, bufResult);
            //end = System.nanoTime();
            //times[i] = (end - start) / NANOSECS;
        //}

        //for (int i = 0; i < 10; i++) {
            //System.out.printf("%.6f\n", times[100+i]);
        //}
    }
}
