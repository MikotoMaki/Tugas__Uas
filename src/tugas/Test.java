package tugas.uas;

import java.text.NumberFormat;
import java.util.Locale;

public class Test {
    public static void main(String[] args) {
        double s = 1000000;
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        System.out.println(formatRupiah.format(s));
    }
}
