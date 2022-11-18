package lib;

import net.dclausen.microfloat.MicroDouble;
import org.kframework.mpfr.BigFloat;

public class DMathMicro {

    public static double log(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.log(d);
        return Double.longBitsToDouble(val);
    }

    public static double log10(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.log10(d);
        return Double.longBitsToDouble(val);
    }

    public static double log1p(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.log1p(d);
        return Double.longBitsToDouble(val);
    }

    public static double exp(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.exp(d);
        return Double.longBitsToDouble(val);
    }

    public static double pow(double a, double b) {
        long dbase = Double.doubleToLongBits(a);
        long dexp = Double.doubleToLongBits(b);
        long val = MicroDouble.pow(dbase, dexp);
        return Double.longBitsToDouble(val);
    }

    public static double sin(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.sin(d);
        return Double.longBitsToDouble(val);
    }

    public static double cos(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.cos(d);
        return Double.longBitsToDouble(val);
    }

    public static double tan(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.tan(d);
        return Double.longBitsToDouble(val);
    }

    public static double abs(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.abs(d);
        return Double.longBitsToDouble(val);
    }
}
