import com.nvankempen.knots.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int PROGRESS_REPORT_THRESHOLD = 300;

    public static void main(String[] args) {
        final long start = System.nanoTime();
        final int n = search(Integer.parseInt(args[0]));
        final long elapsed = System.nanoTime() - start;
        System.out.println("Generated " + n + " quandles in " + (elapsed / Math.pow(10, 9)) + " seconds.");
    }

    private static int search(int n) {
        final AtomicInteger count = new AtomicInteger(0);
        Quandle.generate(Z(n), q -> {
            final int current = count.incrementAndGet();
            if (current % PROGRESS_REPORT_THRESHOLD == 0) {
                System.out.println("Generated " + current + " quandles...");
            }
        });
        return count.get();
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
