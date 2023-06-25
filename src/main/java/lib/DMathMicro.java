package lib;

import net.dclausen.microfloat.MicroDouble;
import net.dclausen.microfloat.MicroFloat;

public class DMathMicro {

    public static double abs(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.abs(d);
        return Double.longBitsToDouble(val);
    }

    public static float abs(float a) {
        int f = Float.floatToIntBits(a);
        int val = MicroFloat.abs(f);
        return Float.intBitsToFloat(val);
    }

    public static double acos(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.acos(d);
        return Double.longBitsToDouble(val);
    }

    public static double asin(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.asin(d);
        return Double.longBitsToDouble(val);
    }

    public static double atan(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.atan(d);
        return Double.longBitsToDouble(val);
    }

    // atan2 missing

    // cbrt missing

    public static double ceil(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.ceil(d);
        return Double.longBitsToDouble(val);
    }

    // copysign missing (D+F)

    public static double cos(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.cos(d);
        return Double.longBitsToDouble(val);
    }

    public static double cosh(double x) {
        long d = Double.doubleToLongBits(x);
        long val = MicroDouble.cosh(d);
        return Double.longBitsToDouble(val);
    }

    public static double exp(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.exp(d);
        return Double.longBitsToDouble(val);
    }

    public static double expm1(double x) {
        long d = Double.doubleToLongBits(x);
        long val = MicroDouble.expm1(d);
        return Double.longBitsToDouble(val);
    }

    public static double floor(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.floor(d);
        return Double.longBitsToDouble(val);
    }

    // getExponent missing (D+F)

    // hypot missing

    public static double IEEEremainder(double f1, double f2) {
        long d1 = Double.doubleToLongBits(f1);
        long d2 = Double.doubleToLongBits(f2);
        long val = MicroDouble.IEEEremainder(d1, d2);
        return Double.longBitsToDouble(val);
    }

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

    public static double log1p(double x) {
        long d = Double.doubleToLongBits(x);
        long val = MicroDouble.log1p(d);
        return Double.longBitsToDouble(val);
    }

    public static double max(double a, double b) {
        long d1 = Double.doubleToLongBits(a);
        long d2 = Double.doubleToLongBits(b);
        long val = MicroDouble.max(d1, d2);
        return Double.longBitsToDouble(val);
    }

    public static float max(float a, float b) {
        int f1 = Float.floatToIntBits(a);
        int f2 = Float.floatToIntBits(b);
        int val = MicroFloat.max(f1, f2);
        return Float.intBitsToFloat(val);
    }

    public static double min(double a, double b) {
        long d1 = Double.doubleToLongBits(a);
        long d2 = Double.doubleToLongBits(b);
        long val = MicroDouble.min(d1, d2);
        return Double.longBitsToDouble(val);
    }

    public static float min(float a, float b) {
        int f1 = Float.floatToIntBits(a);
        int f2 = Float.floatToIntBits(b);
        int val = MicroFloat.min(f1, f2);
        return Float.intBitsToFloat(val);
    }

    public static double pow(double a, double b) {
        long dbase = Double.doubleToLongBits(a);
        long dexp = Double.doubleToLongBits(b);
        long val = MicroDouble.pow(dbase, dexp);
        return Double.longBitsToDouble(val);
    }

    public static double rint(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.rint(d);
        return Double.longBitsToDouble(val);
    }

    public static double round(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.sin(d);
        return Double.longBitsToDouble(val);
    }

    public static float round(float a) {
        int f = Float.floatToIntBits(a);
        int val = MicroFloat.round(f);
        return Float.intBitsToFloat(val);
    }

    public static double scalb(double d, int scaleFactor) {
        long dbits = Double.doubleToLongBits(d);
        long val = MicroDouble.scalbn(dbits, scaleFactor);
        return Double.longBitsToDouble(val);
    }

    // scalb (F) missing

    // signum missing (D+F)

    public static double sin(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.sin(d);
        return Double.longBitsToDouble(val);
    }

    public static double sinh(double x) {
        long d = Double.doubleToLongBits(x);
        long val = MicroDouble.sinh(d);
        return Double.longBitsToDouble(val);
    }

    public static double sqrt(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.sqrt(d);
        return Double.longBitsToDouble(val);
    }

    public static double tan(double a) {
        long d = Double.doubleToLongBits(a);
        long val = MicroDouble.tan(d);
        return Double.longBitsToDouble(val);
    }

    public static double tanh(double x) {
        long d = Double.doubleToLongBits(x);
        long val = MicroDouble.tanh(d);
        return Double.longBitsToDouble(val);
    }
}
