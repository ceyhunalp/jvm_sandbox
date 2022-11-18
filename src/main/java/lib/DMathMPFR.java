package lib;

import org.kframework.mpfr.BigFloat;
import org.kframework.mpfr.BinaryMathContext;
import java.math.RoundingMode;

public class DMathMPFR {

    static final private BinaryMathContext mc = BinaryMathContext.BINARY64.withRoundingMode(RoundingMode.HALF_EVEN);

    public static double log(double a) {
        BigFloat bf = new BigFloat(a, mc);
        return bf.log(mc).doubleValue();
    }

    public static double log10(double a) {
        BigFloat bf = new BigFloat(a, mc);
        return bf.log10(mc).doubleValue();
    }

    public static double log1p(double x) {
        BigFloat bf = new BigFloat(x, mc);
        return bf.log1p(mc).doubleValue();
    }

    public static double exp(double a) {
        BigFloat bf = new BigFloat(a, mc);
        return bf.sin(mc).doubleValue();
    }

    public static double pow(double a, double b) {
        BigFloat base = new BigFloat(a, mc);
        BigFloat exp = new BigFloat(b, mc);
        return base.pow(exp, mc).doubleValue();
    }

    public static double sin(double a) {
        BigFloat bf = new BigFloat(a, mc);
        return bf.sin(mc).doubleValue();
    }

    public static double cos(double a) {
        BigFloat bf = new BigFloat(a, mc);
        return bf.cos(mc).doubleValue();
    }

    public static double tan(double a) {
        BigFloat bf = new BigFloat(a, mc);
        return bf.tan(mc).doubleValue();
    }

    public static double cbrt(double a) {
        BigFloat bf = new BigFloat(a, mc);
        return bf.cbrt(mc).doubleValue();
    }

    public static double hypot(double x, double y) {
        BigFloat bfX = new BigFloat(x, mc);
        BigFloat bfY = new BigFloat(y, mc);
        return BigFloat.hypot(bfX, bfY, mc).doubleValue();
    }

    public static double abs(double a) {
        BigFloat bf = new BigFloat(a, mc);
        return bf.abs(mc).doubleValue();
    }
}
