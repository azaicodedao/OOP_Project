package UI;

import java.text.DecimalFormat;

public class MoneyFormat {
    private static final DecimalFormat moneyFormat = new DecimalFormat("#,### VNĐ");

    // Định dạng số thành chuỗi tiền tệ
    public static String format(double amount) {
        return moneyFormat.format(amount);
    }

    // Chuyển ngược chuỗi tiền tệ về số
    public static double parse(String text) {
        try {
            return Double.parseDouble(
                    text.replace("VNĐ", "")
                            .replace(",", "")
                            .trim()
            );
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
