import com.nvankempen.knots.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int PROGRESS_REPORT_THRESHOLD = 300;

    public static void main(String[] args) {
        final long start = System.nanoTime();
        final int n = search(Integer.parseInt(args[0]), Byte.parseByte(args[1]));
        final long elapsed = System.nanoTime() - start;
        System.out.println("Generated " + n + " quandles in " + (elapsed / Math.pow(10, 9)) + " seconds.");
    }

    private static int search(int n, byte t) {
        final Group<Byte> X = Z(n);
        final Group<Byte> A = Z(2);

        final OrientedSingQuandle<Byte, Byte> initial = new OrientedSingQuandle<>(new SingQuandle<>(X), A);

        for (Byte x : X.getAllElements()) {
            for (Byte y : X.getAllElements()) {
                initial.right(x, y, (byte) mod(t * x + (1 - t) * y, n));
                initial.phi(x, y, (byte) mod((x - y) * (x - y) * y, 2));
            }
        }

        final AtomicInteger count = new AtomicInteger(0);
        OrientedSingQuandle.generate(Z(n), q -> {
            final int current = count.incrementAndGet();
            if (current % PROGRESS_REPORT_THRESHOLD == 0) {
                System.out.println("Generated " + current + " quandles...");
            }
        });
        return count.get();
    }

    private static int mod(int a, int n) {
        return (a > 0) ? (a % n) : ((a % n) + n);
    }

    private static Group<Byte> Z(int n) {
        return new Group<>() {
            @Override
            public Byte getUnknownValue() {
                return -1;
            }

            @Override
            public Byte getIdentity() {
                return 1;
            }

            @Override
            public List<Byte> getAllElements() {
                final List<Byte> elements = new ArrayList<>();
                for (byte i = 0; i < n; ++i) {
                    elements.add(i);
                }
                return elements;
            }

            @Override
            public Byte operation(Byte a, Byte b) {
                return (byte) ((a * b) % n);
            }
        };
    }
}
