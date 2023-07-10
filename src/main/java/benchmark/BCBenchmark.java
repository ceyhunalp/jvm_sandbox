package benchmark;

import com.google.common.math.Stats;
import contract.Write;
import counter.BytecodeCounter;
import counter.Counter;
import org.apache.tuweni.crypto.sodium.Sodium;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BCBenchmark {

    final static String HEADER = "avg,stddev,min,max";
    final static double MICROSECS = 1E3;

    public static void runCounterBenchmark(int seed, int wc, int ec,
                                           double[] times) {
        Write w = new Write(seed);
        byte[] X = new byte[Write.ED25519_CORE_BYTES];
        Sodium.crypto_core_ed25519_random(X);
        byte[] key = new byte[Write.KEY_SIZE];
        Sodium.randombytes(key, Write.KEY_SIZE);
        byte[] seedBytes = ByteBuffer.allocate(4).putInt(23).array();
        w.newWrite(X, key, Write.KEY_SIZE, seedBytes);

        long start, end;
        for (int i = 0; i < wc + ec; i++) {
            Counter.count = 0;
            start = System.nanoTime();
            w.checkProof();
            end = System.nanoTime();
            times[i] = (end - start) / MICROSECS;
        }
        Counter.print();
    }

    public static void removeWarmupTimes(double[] times, double[] execTimes, int wc) {
        System.arraycopy(times, wc, execTimes, 0, execTimes.length);
    }

    public static void writeStats(String outdir, double[] times,
                                  String prefix) throws IOException {
        String fname = prefix + ".csv";
        Path outPath = Paths.get(outdir, fname);
        String outfile = outPath.toString();
        Stats stats = Stats.of(times);
        FileWriter fw = new FileWriter(outfile, true);
        fw.write(String.format("%s\n", HEADER));
        fw.write(String.format("%.6f,%.6f,%.6f,%.6f\n",
                stats.mean(),
                stats.sampleStandardDeviation(),
                stats.min(),
                stats.max()));
        fw.close();
    }

    public static void launchBenchmark(String[] args) throws IOException, AnalyzerException {
        if (args.length != 7) {
            System.err.println("missing argument(s)");
            System.exit(-1);
        }

        int wc = Integer.parseInt(args[0]);
        int ec = Integer.parseInt(args[1]);
        int seed = Integer.parseInt(args[2]);
        String instrType = args[3];
        String contractPath = args[4];
        String counterPath = args[5];
        String outdir = args[6];
        String prefix = "base";

        BytecodeCounter bc = new BytecodeCounter(contractPath, counterPath);

        if (instrType.equals("-s")) {
            bc.rewriteSequential();
        } else if (instrType.equals("-b")) {
            bc.rewriteBlock();
        } else {
            double[] allTimes = new double[wc + ec];
            double[] execTimes = new double[ec];
            runCounterBenchmark(seed, wc, ec, allTimes);
            removeWarmupTimes(allTimes, execTimes, wc);

            writeStats(outdir, execTimes, prefix);
        }
    }

    public static void main(String[] args) throws IOException, AnalyzerException {
        launchBenchmark(args);
    }
}
