package lib;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class DMathApfloat {

    public static double sin(double a) {
        Apfloat apf = new Apfloat(a);
        return ApfloatMath.sin(apf).doubleValue();
    }

    public static double cos(double a) {
        Apfloat apf = new Apfloat(a);
        return ApfloatMath.cos(apf).doubleValue();
    }

    public static double tan(double a) {
        Apfloat apf = new Apfloat(a);
        return ApfloatMath.tan(apf).doubleValue();
    }

    public static double exp(double a) {
        Apfloat apf = new Apfloat(a);
        return ApfloatMath.exp(apf).doubleValue();
    }

    public static double log(double a) {
        Apfloat apf = new Apfloat(a);
        return ApfloatMath.log(apf).doubleValue();
    }

    public static double log10(double a) {
        Apfloat apf = new Apfloat(a);
        return ApfloatMath.log(apf, new Apfloat(10)).doubleValue();
    }

    public static double pow(double a, double b) {
        Apfloat base = new Apfloat(a);
        Apfloat exp = new Apfloat(b);
        return ApfloatMath.pow(base, exp).doubleValue();
    }

    public static double cbrt(double a) {
        Apfloat apf = new Apfloat(a);
        return ApfloatMath.cbrt(apf).doubleValue();
    }

    public static double abs(double a) {
        Apfloat apf = new Apfloat(a);
        return ApfloatMath.abs(apf).doubleValue();
    }
}
