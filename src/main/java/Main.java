import com.nvankempen.knots.*;
import com.nvankempen.knots.utils.Box;

import java.util.SortedSet;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        final Box<Integer> count = Box.create(0);
        Quandle.generate(Z(6), q -> {
            count.setElement(count.getElement() + 1);
        });
        System.out.println(count);
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
