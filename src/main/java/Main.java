import com.nvankempen.knots.*;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        Quandle.generate((byte) 3, new Group<Byte>() {
            @Override
            public Byte getUnknownValue() {
                return -1;
            }

            @Override
            public SortedSet<Byte> getAllElements() {
                return new TreeSet<>(Set.of((byte) 0, (byte) 1, (byte) 2));
            }
        }, System.out::println);
    }
}
