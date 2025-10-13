package UI;

import UI.ProductUI.Product_Manage_Frame;

import javax.swing.*;
import java.awt.*;
// Nguyễn Trung Nghĩa
public class Home_Frame extends JFrame{

    JButton qlspButton, tao_gio_hangButton, dangxuatButton, exitButton;
    JLabel titleLabel, footerLabel;
    JPanel panel;

    Home_Frame(){

        titleLabel = new JLabel("PHẦN MỀM QUẢN LÝ BÁN HÀNG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 10, 0));

        footerLabel = new JLabel("© 2025 - Demo", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Poppins", Font.BOLD, 15));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // --------------------- qlspButton ---------------------

        qlspButton = new JButton();
        qlspButton.setLayout(new BorderLayout());

        JLabel iconLabel1 = new JLabel(new ImageIcon("image/qlsp.png"));
        iconLabel1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JLabel textLabel1 = new JLabel("Quản lý sản phẩm", SwingConstants.CENTER);
        textLabel1.setFont(new Font("Inter", Font.BOLD, 30));

        qlspButton.add(iconLabel1, BorderLayout.WEST);
        qlspButton.add(textLabel1, BorderLayout.CENTER);

        qlspButton.setPreferredSize(new Dimension(250, 50));
        qlspButton.setFocusable(false);
        qlspButton.addActionListener(e ->{
            dispose();
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                new Product_Manage_Frame();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (UnsupportedLookAndFeelException ex) {
                throw new RuntimeException(ex);
            }
        });

        // --------------------- qlspButton ---------------------

        // --------------------- tao_gio_hangButton ---------------------

        tao_gio_hangButton = new JButton();
        tao_gio_hangButton.setLayout(new BorderLayout());

        JLabel iconLabel2 = new JLabel(new ImageIcon("image/giohang.png"));
        iconLabel2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JLabel textLabel2 = new JLabel("Tạo giỏ hàng", SwingConstants.CENTER);
        textLabel2.setFont(new Font("Inter", Font.BOLD, 30));

        tao_gio_hangButton.add(iconLabel2, BorderLayout.WEST);
        tao_gio_hangButton.add(textLabel2, BorderLayout.CENTER);

        tao_gio_hangButton.setPreferredSize(new Dimension(250, 50));
        tao_gio_hangButton.setFocusable(false);

        // --------------------- tao_gio_hangButton ---------------------

        // --------------------- dangxuatButton ---------------------

        dangxuatButton = new JButton();
        dangxuatButton.setLayout(new BorderLayout());

        JLabel iconLabel4 = new JLabel(new ImageIcon("image/logout.png"));
        iconLabel4.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JLabel textLabel4 = new JLabel("Đăng xuất", SwingConstants.CENTER);
        textLabel4.setFont(new Font("Inter", Font.BOLD, 30));

        dangxuatButton.add(iconLabel4, BorderLayout.WEST);
        dangxuatButton.add(textLabel4, BorderLayout.CENTER);

        dangxuatButton.setPreferredSize(new Dimension(250, 50));
        dangxuatButton.setFocusable(false);
        dangxuatButton.addActionListener(e ->{
            dispose();
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                new Login_Frame();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (UnsupportedLookAndFeelException ex) {
                throw new RuntimeException(ex);
            }
        });

        // --------------------- dangxuatButton ---------------------

        // --------------------- exitButton ---------------------

        exitButton = new JButton();
        exitButton.setLayout(new BorderLayout());

        JLabel iconLabel3 = new JLabel(new ImageIcon("image/exit.png"));
        iconLabel3.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        JLabel textLabel3 = new JLabel("Thoát", SwingConstants.CENTER);
        textLabel3.setFont(new Font("Inter", Font.BOLD, 30));

        exitButton.add(iconLabel3, BorderLayout.WEST);
        exitButton.add(textLabel3, BorderLayout.CENTER);

        exitButton.setPreferredSize(new Dimension(250, 50));
        exitButton.setFocusable(false);

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        // --------------------- exitButton ---------------------

        panel = new JPanel();
        panel.setBackground(new Color(0xE0F2F1));
        panel.setOpaque(true);
        panel.setLayout(new GridLayout(4, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));
        panel.add(qlspButton);
        panel.add(tao_gio_hangButton);
        panel.add(dangxuatButton);
        panel.add(exitButton);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("PHẦN MỀM QUẢN LÝ BÁN HÀNG");
        setSize(800, 600);
        getContentPane().setBackground(new Color(0xE0F2F1));
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(footerLabel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
