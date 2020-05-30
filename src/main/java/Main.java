import com.nvankempen.knots.*;
import com.nvankempen.knots.utils.Box;

import java.util.SortedSet;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        final long start = System.nanoTime();
        final int n = search(Integer.parseInt(args[0]));
        final long elapsed = System.nanoTime() - start;
        System.out.println("Found " + n + " quandles in " + (elapsed / Math.pow(10, 9)) + " seconds.");
    }

    private static int search(int n) {
        final Box<Integer> count = Box.create(0);
        Quandle.generate(Z(n), q -> {
            count.setElement(count.getElement() + 1);
        });
        return count.getElement();
    }

    private static Group<Byte> Z(int n) {
        return new Group<>() {
            @Override
            public Byte getUnknownValue() {
                return -1;
            }

            @Override
            public SortedSet<Byte> getAllElements() {
                final SortedSet<Byte> elements = new TreeSet<>();
                for (byte i = 0; i < n; ++i) {
                    elements.add(i);
                }
                return elements;
            }
        };
    }
}
