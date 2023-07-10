package counter;

public class Executor {
    public static void main(String[] args) {
        TestContract contract = new TestContract();
        Counter.count = 0;
        int sum = 0;
        // warm up
        for (int i = 0; i < 1000000; i++) {
            contract.sum(2000);
        }
        // experiment
        Counter.count = 0;
        long startTime = System.nanoTime();
        sum = contract.sum(2000);
        long stopTime = System.nanoTime();
        System.out.println("Answer: " + sum);
        System.out.println("Time micro seconds :" + (stopTime - startTime) / 1000);
        Counter.print();
    }
}