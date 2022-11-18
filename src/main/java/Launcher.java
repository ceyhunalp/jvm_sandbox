import checker.ASTData;
import checker.Checker;
import org.apache.commons.cli.*;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Launcher {

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
        Option optLib = Option.builder("l").longOpt("lib").hasArg().optionalArg(true).desc("Math library name").build();
        Option optCount = Option.builder("i").longOpt("instrument").optionalArg(true).desc("Instrument program for " +
                "bytecode counting").build();
        options.addOption(optMode);
        options.addOption(optFile);
        options.addOption(optFast);
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
                checker.printData();
                checker.execute();

            } else if (mode.equals("rewriter")) {
                String mathLib = null;
                boolean instrument = false;
                if(cmd.hasOption("l")) {
                    mathLib = cmd.getOptionValue("l");
                } else {
                    System.err.println("Missing argument \"lib\" when executing in rewriter mode");
                    System.exit(1);
                }
                if(cmd.hasOption("i")) {
                    instrument = true;
                }
            } else {
                System.err.println("Invalid argument value for mode");
                System.exit(1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws ParseException {
        run(args);
//        Checker checker = new Checker();
//        String[] srcPaths = {args[1]};
//        CompilationUnit cu = checker.createCompilationUnit(args[0], srcPaths);
//        ASTData astData = checker.extractASTData(cu);
//
//        if (!astData.classes.isEmpty()) {
//            for (String cl : astData.classes) {
//                System.out.println("Class name: " + cl);
//            }
//        }
//        System.out.println("====");
//        if (!astData.imports.isEmpty()) {
//            for (String imp : astData.imports) {
//                System.out.println("Package: " + imp);
//            }
//        }
//        System.out.println("====");
//        if (!astData.methods.isEmpty()) {
//            for (String cname : astData.methods.keySet()) {
//                System.out.println(cname + " -> " + astData.methods.get(cname).toString());
//            }
//        }

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
//        int idx;
//        double sm;
//        long sm_l, mpfr_l;
//        double mpfr;
//        BigFloat bfx, bfy;
//        BinaryMathContext mc = BinaryMathContext.BINARY64.withRoundingMode(RoundingMode.FLOOR);
//        for (int i = 0; i < 1000000; i++) {
//            idx = i % 1000;
//            bfx = new BigFloat(xs[idx],mc);
//            bfy = new BigFloat(ys[idx],mc);
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

//        System.out.println(StrictMath.round(0.0/0.0) + " " + new BigFloat(0.0/0.0, mc).round(mc).longValue());
//        System.out.println(StrictMath.round(Double.NEGATIVE_INFINITY) + " " + new BigFloat(Double.NEGATIVE_INFINITY, mc).round(mc).longValue());
//        System.out.println(StrictMath.round(Double.POSITIVE_INFINITY) + " " + new BigFloat(Double.POSITIVE_INFINITY, mc).round(mc).longValue());
//        System.out.println(StrictMath.round((double)Long.MIN_VALUE) + " " + new BigFloat((double)Long.MIN_VALUE, mc).round(mc).longValue());
//        System.out.println(StrictMath.round((double)Long.MAX_VALUE) + " " + new BigFloat((double)Long.MAX_VALUE, mc).round(mc).longValue());
//        System.out.println(StrictMath.round((double)Long.MAX_VALUE) + " " + new BigFloat((double)Long.MAX_VALUE, mc).longValue());

//        MessageDigest md;
//        Double d;
//        Boolean b;
//
//        Character.Subset s = Character.UnicodeBlock.BASIC_LATIN;
//        s.hashCode();
//        Character.UnicodeScript us = Character.UnicodeScript.ANATOLIAN_HIEROGLYPHS;
//        us.hashCode();
//        System.out.println(s.hashCode());

//        double x;
//        long a = Long.MIN_VALUE;
//        x = (double) a;
//        x -= 1;
//
//        System.out.println(StrictMath.round(x) + " " + new BigFloat(x, mc).roundJava(mc).longValue());

    }
}
