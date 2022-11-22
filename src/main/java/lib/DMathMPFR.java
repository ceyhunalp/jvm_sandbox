package lib;

import org.kframework.mpfr.BigFloat;
import org.kframework.mpfr.BinaryMathContext;
import java.math.RoundingMode;
import java.sql.SQLSyntaxErrorException;

public class DMathMPFR {

    static final private BinaryMathContext mcFloat = BinaryMathContext.BINARY32.withRoundingMode(RoundingMode.HALF_EVEN);
    static final private BinaryMathContext mcDouble = BinaryMathContext.BINARY64.withRoundingMode(RoundingMode.HALF_EVEN);

    public static double abs(double a) {
        //System.out.println("In abs(double)");
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.abs(mcDouble).doubleValue();
    }

    public static float abs(float a) {
        //System.out.println("In abs(float)");
        BigFloat bf = new BigFloat(a, mcFloat);
        return bf.abs(mcFloat).floatValue();
    }

    public static double acos(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.acos(mcDouble).doubleValue();
    }

    public static double asin(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.asin(mcDouble).doubleValue();
    }

    public static double atan(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.atan(mcDouble).doubleValue();
    }

    public static double atan2(double y, double x) {
        BigFloat bfX = new BigFloat(x, mcDouble);
        BigFloat bfY = new BigFloat(y, mcDouble);
        return BigFloat.atan2(bfY, bfX, mcDouble).doubleValue();
    }

    public static double cbrt(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.cbrt(mcDouble).doubleValue();
    }

    public static double ceil(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.ceil(mcDouble).doubleValue();
    }

    public static double copysign(double magnitude, double sign) {
        BigFloat bfMagnitude = new BigFloat(magnitude, mcDouble);
        BigFloat bfSign = new BigFloat(sign, mcDouble);
        return BigFloat.copysign(bfMagnitude, bfSign, mcDouble).doubleValue();
    }

    public static float copysign(float magnitude, float sign) {
        BigFloat bfMagnitude = new BigFloat(magnitude, mcFloat);
        BigFloat bfSign = new BigFloat(sign, mcFloat);
        return BigFloat.copysign(bfMagnitude, bfSign, mcFloat).floatValue();
    }

    public static double cos(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.cos(mcDouble).doubleValue();
    }

    public static double cosh(double x) {
        BigFloat bf = new BigFloat(x, mcDouble);
        return bf.cosh(mcDouble).doubleValue();
    }

    public static double exp(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.exp(mcDouble).doubleValue();
    }

    public static double expm1(double x) {
        BigFloat bf = new BigFloat(x, mcDouble);
        return bf.expm1(mcDouble).doubleValue();
    }

    public static double floor(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.floor(mcDouble).doubleValue();
    }

    public static int getExponent(double d) {
        BigFloat bf = new BigFloat(d, mcDouble);
        return (int) bf.exponent(mcDouble.minExponent, mcDouble.maxExponent);
    }

    public static int getExponent(float f) {
        BigFloat bf = new BigFloat(f, mcFloat);
        return (int) bf.exponent(mcFloat.minExponent, mcFloat.maxExponent);
    }

    public static double hypot(double x, double y) {
        BigFloat bfX = new BigFloat(x, mcDouble);
        BigFloat bfY = new BigFloat(y, mcDouble);
        return BigFloat.hypot(bfX, bfY, mcDouble).doubleValue();
    }

    public static double IEEEremainder(double f1, double f2) {
        BigFloat bf1 = new BigFloat(f1, mcDouble);
        BigFloat bf2 = new BigFloat(f2, mcDouble);
        return bf1.remainder(bf2, mcDouble).doubleValue();
    }

    public static double log(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.log(mcDouble).doubleValue();
    }

    public static double log10(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.log10(mcDouble).doubleValue();
    }

    public static double log1p(double x) {
        BigFloat bf = new BigFloat(x, mcDouble);
        return bf.log1p(mcDouble).doubleValue();
    }

    public static double max(double a, double b) {
        BigFloat bfA = new BigFloat(a, mcDouble);
        BigFloat bfB = new BigFloat(b, mcDouble);
        return BigFloat.max(bfA, bfB, mcDouble).doubleValue();
    }

    public static float max(float a, float b) {
        BigFloat bfA = new BigFloat(a, mcFloat);
        BigFloat bfB = new BigFloat(b, mcFloat);
        return BigFloat.max(bfA, bfB, mcFloat).floatValue();
    }

    public static double min(double a, double b) {
        BigFloat bfA = new BigFloat(a, mcDouble);
        BigFloat bfB = new BigFloat(b, mcDouble);
        return BigFloat.min(bfA, bfB, mcDouble).doubleValue();
    }

    public static float min(float a, float b) {
        BigFloat bfA = new BigFloat(a, mcFloat);
        BigFloat bfB = new BigFloat(b, mcFloat);
        return BigFloat.min(bfA, bfB, mcFloat).floatValue();
    }

    public static double nextAfter(double start, double direction) {
        BigFloat bfStart = new BigFloat(start, mcDouble);
        BigFloat bfDirection = new BigFloat(direction, mcDouble);
        return bfStart.nextAfter(bfDirection, mcDouble.minExponent, mcDouble.maxExponent).doubleValue();
    }

    public static float nextAfter(float start, float direction) {
        BigFloat bfStart = new BigFloat(start, mcFloat);
        BigFloat bfDirection = new BigFloat(direction, mcFloat);
        return bfStart.nextAfter(bfDirection, mcFloat.minExponent, mcFloat.maxExponent).floatValue();
    }

    public static double nextDown(double d) {
        BigFloat bf = new BigFloat(d, mcDouble);
        return bf.nextDown(mcDouble.minExponent, mcDouble.maxExponent).doubleValue();
    }

    public static float nextDown(float f) {
        BigFloat bf = new BigFloat(f, mcFloat);
        return bf.nextDown(mcFloat.minExponent, mcFloat.maxExponent).floatValue();
    }

    public static double nextUp(double d){
        BigFloat bf = new BigFloat(d, mcDouble);
        return bf.nextUp(mcDouble.minExponent, mcDouble.maxExponent).doubleValue();
    }

    public static double nextUp(float f){
        BigFloat bf = new BigFloat(f, mcFloat);
        return bf.nextUp(mcFloat.minExponent, mcFloat.maxExponent).floatValue();
    }

    public static double pow(double a, double b) {
        BigFloat base = new BigFloat(a, mcDouble);
        BigFloat exp = new BigFloat(b, mcDouble);
        return base.pow(exp, mcDouble).doubleValue();
    }

    public static double rint(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.rint(mcDouble).doubleValue();
    }

    public static long round(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return (long)(bf.round(mcDouble).doubleValue());
    }

    public static long round(float a) {
        BigFloat bf = new BigFloat(a, mcFloat);
        return (long)(bf.round(mcFloat).floatValue());
    }

//    public static double scalb(double d, int scaleFactor) {
//        BigFloat bf = new BigFloat(d, mcDouble);
//        BigFloat result = bf.scalb(scaleFactor, mcDouble);
//        long exponent = result.exponent(mcDouble.minExponent, mcDouble.maxExponent);
//        if (exponent > mcDouble.maxExponent) {
//            return Double.POSITIVE_INFINITY;
//        }
//        return result.doubleValue();
//    }

    public static double signum(double d) {
        BigFloat bf = new BigFloat(d, mcDouble);
        return bf.signum();
    }

    public static double signum(float f) {
        BigFloat bf = new BigFloat(f, mcFloat);
        return bf.signum();
    }

    public static double sin(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.sin(mcDouble).doubleValue();
    }

    public static double sinh(double x) {
        BigFloat bf = new BigFloat(x, mcDouble);
        return bf.sinh(mcDouble).doubleValue();
    }

    public static double tan(double a) {
        BigFloat bf = new BigFloat(a, mcDouble);
        return bf.tan(mcDouble).doubleValue();
    }

    public static double tanh(double x) {
        BigFloat bf = new BigFloat(x, mcDouble);
        return bf.tanh(mcDouble).doubleValue();
    }
}
