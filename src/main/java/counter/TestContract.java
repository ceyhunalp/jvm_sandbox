package counter;

public class TestContract {
    private double Cvar1;
    private float Cvar2;
    private char Cvar3;
    private String Cvar4;
    private int Cvar5;
    private long Cvar6;

    public int sum(int k) {
        if (k > 0) {
            return k + sum(k - 1);
        } else {
            return 0;
        }
    }
}