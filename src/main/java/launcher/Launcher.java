package launcher;

import checker.Checker;
import org.apache.commons.cli.*;
import rewriter.Rewriter;

import java.io.IOException;

public class Launcher {

    private final static String LIB_MPFR = "mpfr";
    private final static String LIB_MF = "mf";

    public static void run(String[] args) {
        CommandLine cmd;
        Options options = new Options();

        Option optMode = Option.builder("m").longOpt("mode").hasArg().required(true).desc("Operation mode: static " +
                "checker or rewriter").build();
        // Arguments for checker
        Option optFile = Option.builder("c").longOpt("config").hasArg().optionalArg(true).desc("Config file with: (1)" +
                "Java source file names and paths, (2) whitelisted/blacklisted classes and methods").build();
        Option optFast = Option.builder("f").longOpt("fast").optionalArg(true).desc("Run checker in fast mode").build();
        // Arguments for rewriting
        Option optPath = Option.builder("p").longOpt("path").hasArg().optionalArg(true).desc(".class file path").build();
        Option optLib = Option.builder("l").longOpt("lib").hasArg().optionalArg(true).desc("Math library name").build();
        Option optCount = Option.builder("i").longOpt("instrument").optionalArg(true).desc("Instrument program for " +
                "bytecode counting").build();
        options.addOption(optMode);
        options.addOption(optFile);
        options.addOption(optFast);
        options.addOption(optPath);
        options.addOption(optLib);
        options.addOption(optCount);

        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
            String mode = cmd.getOptionValue("m");
            if (mode.equals("checker")) {
                String config = null;
                boolean fast = false;
                if(cmd.hasOption("c")) {
                    config = cmd.getOptionValue("c");
                } else {
                    System.err.println("Missing argument \"config\" when executing in checker mode");
                    System.exit(1);
                }
                if(cmd.hasOption("f")) {
                    fast = true;
                }
                Checker checker = new Checker(config, fast);
                checker.execute();
            } else if (mode.equals("rewriter")) {
                String mathLib = null;
                String path = null;
                boolean instrument = false;
                if(cmd.hasOption("p")) {
                    path = cmd.getOptionValue("p");
                } else {
                    System.err.println("Missing argument \"path\" when executing in rewriter mode");
                    System.exit(1);
                }
                if(cmd.hasOption("l")) {
                    mathLib = cmd.getOptionValue("l");
                    if (mathLib.equals(LIB_MPFR)) {
                        mathLib = "MPFR";
                    } else if (mathLib.equals(LIB_MF)) {
                        mathLib = "Micro";
                    } else {
                        System.err.println("Invalid math library");
                        System.exit(1);
                    }
                } else {
                    System.err.println("Missing argument \"lib\" when executing in rewriter mode");
                    System.exit(1);
                }
                if(cmd.hasOption("i")) {
                    instrument = true;
                }
                Rewriter rewriter = new Rewriter(mathLib);
                rewriter.rewriteClass(path);
            } else {
                System.err.println("Invalid argument value for mode");
                System.exit(1);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws ParseException {

        run(args);

//        Rewriter rw = new Rewriter();
//        try {
//            rw.testRewrite(args[0]);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        double[] xs = new double[1000];
//        double[] ys = new double[1000];
//
//        for (int i = 0; i < 1000; i++) {
//            Random r = new Random();
//            xs[i] = r.nextDouble()*10;
//            ys[i] = r.nextDouble()*25;
//        }

//
//
//        int idx, scale;
//        double sm;
//        double mpfr;
//        long lsm, lmpfr;
//        BigFloat bfx, bfy;
//        Random r = new Random();
//        BinaryMathContext mc = BinaryMathContext.BINARY64.withRoundingMode(RoundingMode.FLOOR);
//        bfx = new BigFloat(0, mc);
//        bfy = bfx.cos(mc);
//        System.out.printf("%.5f %.5f\n", bfx.doubleValue(), bfy.doubleValue());
//        for (int i = 0; i < 1000000; i++) {
//            idx = i % 1000;
//            bfx = new BigFloat(xs[idx], mc);
//            bfy = new BigFloat(ys[idx], mc);
//            scale = r.nextInt();
//            mpfr = bfx.scalb(scale, mc).doubleValue();
//            sm = StrictMath.scalb(xs[idx], scale);
//            System.out.println("[" + scale + "]" + " " + sm + " " + mpfr + " " + (mpfr-sm));
//            mpfr = BigFloat.copysign(bfx, bfy, mc).doubleValue();
//            sm = StrictMath.copySign(xs[idx], ys[idx]);
//            System.out.println(mpfr-sm);
//            lsm = StrictMath.getExponent(xs[idx]);
//            lmpfr = bfx.exponent(Double.MIN_EXPONENT, Double.MAX_EXPONENT);
//            System.out.println(lsm + " " + lmpfr + " " + (lsm-lmpfr));

//            sm = StrictMath.max(xs[idx], ys[idx]);
//            mpfr = BigFloat.max(bfx, bfy, mc).doubleValue();
//            if (sm - mpfr != 0) {
//                System.out.println(sm + " " + mpfr);
//            }
//            sm = StrictMath.floor(xs[idx]);
//            mpfr = bfx.floor(mc).doubleValue();
//            if (sm - mpfr != 0) {
//                System.out.println(sm + " " + mpfr);
//            }
//            sm = StrictMath.ceil(ys[idx]);
//            mpfr = bfy.ceil(mc).doubleValue();
//            if (sm - mpfr != 0) {
//                System.out.println(sm + " " + mpfr);
//            }
//            sm_l = StrictMath.round(xs[idx]);
////            mpfr = bfx.round(mc).doubleValue();
//            mpfr_l = bfx.roundJava(mc).longValue();
//            if (sm_l - mpfr_l != 0) {
//                System.out.println(sm_l + " " + mpfr_l);
//            }
//            sm_l = StrictMath.round(ys[idx]);
//            mpfr_l = bfy.roundJava(mc).longValue();
//            if (sm_l - mpfr_l != 0) {
//                System.out.println(sm_l + " " + mpfr_l);
//            }
//        }

//        System.out.println(StrictMath.ceil(0.0/0.0) + " " + new BigFloat(0.0/0.0, mc).ceil(mc).doubleValue());
//        System.out.println(StrictMath.ceil(Double.NEGATIVE_INFINITY) + " " + new BigFloat(Double.NEGATIVE_INFINITY, mc).ceil(mc).doubleValue());
//        System.out.println(StrictMath.ceil(Double.POSITIVE_INFINITY) + " " + new BigFloat(Double.POSITIVE_INFINITY, mc).ceil(mc).doubleValue());
//        System.out.println(StrictMath.ceil(-0.0) + " " + new BigFloat(-0.0, mc).ceil(mc).doubleValue());
//        System.out.println(StrictMath.ceil(+0.0) + " " + new BigFloat(+0.0, mc).ceil(mc).doubleValue());
//        System.out.println(StrictMath.ceil(-0.0000000000000000000000091) + " " + new BigFloat(-0.0000000000000000000000091, mc).ceil(mc).doubleValue());
//        Math.round(34);

//        System.out.println(StrictMath.round(0.0/0.0) + " " + new BigFloat(0.0/0.0, mc).round(mc).longValue());
//        System.out.println(StrictMath.round(Double.NEGATIVE_INFINITY) + " " + new BigFloat(Double.NEGATIVE_INFINITY, mc).round(mc).longValue());
//        System.out.println(StrictMath.round(Double.POSITIVE_INFINITY) + " " + new BigFloat(Double.POSITIVE_INFINITY, mc).round(mc).longValue());
//        System.out.println(StrictMath.round((double)Long.MIN_VALUE) + " " + new BigFloat((double)Long.MIN_VALUE, mc).round(mc).longValue());
//        System.out.println(StrictMath.round((double)Long.MAX_VALUE) + " " + new BigFloat((double)Long.MAX_VALUE, mc).round(mc).longValue());
//
//        System.out.println(StrictMath.round(0.0/0.0) + " " + new BigFloat(0.0/0.0, mc).roundJava(mc).longValue());
//        System.out.println(StrictMath.round(Double.NEGATIVE_INFINITY) + " " + new BigFloat(Double.NEGATIVE_INFINITY, mc).roundJava(mc).longValue());
//        System.out.println(StrictMath.round(Double.POSITIVE_INFINITY) + " " + new BigFloat(Double.POSITIVE_INFINITY, mc).roundJava(mc).longValue());
//        System.out.println(StrictMath.round((double)Long.MIN_VALUE) + " " + new BigFloat((double)Long.MIN_VALUE, mc).roundJava(mc).longValue());
//        System.out.println(StrictMath.round((double)Long.MAX_VALUE) + " " + new BigFloat((double)Long.MAX_VALUE, mc).roundJava(mc).longValue());
//        System.out.println(StrictMath.round((double)Long.MAX_VALUE) + " " + new BigFloat((double)Long.MAX_VALUE, mc).longValue());

//        System.out.println(StrictMath.round(0.0/0.0) + " " + (long)(new BigFloat(0.0/0.0, mc).round(mc).doubleValue()));
//        System.out.println(StrictMath.round(Double.NEGATIVE_INFINITY) + " " + (long)(new BigFloat(Double.NEGATIVE_INFINITY, mc).round(mc).doubleValue()));
//        System.out.println(StrictMath.round(Double.POSITIVE_INFINITY) + " " + (long)(new BigFloat(Double.POSITIVE_INFINITY, mc).round(mc).doubleValue()));
//        System.out.println(StrictMath.round((double)Long.MIN_VALUE) + " " + (long)(new BigFloat((double)Long.MIN_VALUE, mc).round(mc).doubleValue()));
//        System.out.println(StrictMath.round((double)Long.MAX_VALUE) + " " + (long)(new BigFloat((double)Long.MAX_VALUE, mc).round(mc).doubleValue()));

//        System.out.println(StrictMath.getExponent(0.0/0.0) + " " + new BigFloat(0.0/0.0, mc).exponent(Double.MIN_EXPONENT, Double.MAX_EXPONENT));
//        System.out.println(StrictMath.getExponent(0.0) + " " + new BigFloat(0.0, mc).exponent(Double.MIN_EXPONENT, Double.MAX_EXPONENT));
    }
}
