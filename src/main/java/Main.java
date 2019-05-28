import com.nvankempen.quandles.*;

import java.util.Set;

public class Main {
    public static void main(String[] args) {

        byte n = (byte) 3;

        Quandle q = new Quandle(n, new byte[][] {
                {0, 0, 1}, {1, 1, 0}, {2, 2, 2}
        }, new byte[][] {
                {0, 0, 0}, {1, 0, 1}, {2, 2, 0}
        });

        System.out.println(q.isTriangleValid());
        System.out.println(q.isValid());

        Set<Quandle> quandles = Quandles.generate((byte) 4);
        for (Quandle quandle : quandles) {
            System.out.println(quandle);
        }
    }
}
