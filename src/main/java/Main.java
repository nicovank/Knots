import com.nvankempen.quandles.*;

public class Main {

    public static void main(String[] args) {
        Quandles.generate((byte) 3).forEach(System.out::println);
    }
}
