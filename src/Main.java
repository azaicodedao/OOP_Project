import javax.swing.*;
// Nguyễn Trung Nghĩa
public class Main {
    public static void main(String[] args) throws Exception{
        // Giao diện đẹp hơn (Nimbus Look and Feel)
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        new UI.Home_Frame();
    }
}
