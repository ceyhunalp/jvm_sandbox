package sample;

public class SampleClass {

//    final static double NANOSECS = 1E9;
//    // No canonicalization
//    public static void serializeResult(double[] result, double[] bufResult) {
//        for (int i = 0; i < result.length; i++) {
//            long lng = Double.doubleToRawLongBits(result[i]);
//            for (int j = 0; j < 8; j++) {
//                bufResult[i * 8 + (7 - j)] =
//                        (byte) ((lng >> ((7 - j) * 8)) & 0xff);
//            }
//        }
//    }
//
//    // Canonicalized
//    public static void serializeCanonResult(double[] result, double[] bufResult) {
//        for (int i = 0; i < result.length; i++) {
//            long lng = Double.doubleToLongBits(result[i]);
//            for (int j = 0; j < 8; j++) {
//                bufResult[i * 8 + (7 - j)] =
//                        (byte) ((lng >> ((7 - j) * 8)) & 0xff);
//            }
//        }
//    }

    public static void main(String[] args) {
        Foo f = new Foo();
        String s = f.toString();
        System.out.println(s);
        f.write();

//        Write w = new Write(12);
//        w.hashCode();
//        w.toString();
//        Long.getLong("sd");
//        "sd".describeConstable();
//        Integer x = 5;
//        Integer.valueOf(23);
//        Double.toString(12);
//        LinkedHashMap s = null;
////        Set<Object> aa = s.entrySet();
////        s.remove(null);
////        aa.iterator();
//        s.hashCode();
//        s.values();
//        Write w = new Write(12);
//        w.equals(w);
//        s.toString();
//
//        LinkedHashSet sd = null;
//        sd.toArray();
//        s.isEmpty();
//        s.toString();
//        Object o;
//        Double.hashCode();
//        o.toString();
//        Math.tanh();
//        Double.doubleToLongBits();
//        new Double().floatValue()
//        int y = 934;
//        byte x = (byte) y;
//        double x = Math.sin(0);
//        System.out.printf("%.20f\n", x);
//
//        long l = 0xfff0000000000009L;
//        System.out.printf("%x\n", l);
//        double d = Double.longBitsToDouble(l);
//        System.out.println(d);
//        System.out.printf("%x\n", Double.doubleToLongBits(d));
//        System.out.printf("%x\n", Double.doubleToRawLongBits(d));

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
