package UI;

import UI.ImportUI.Import_List_Frame;
import UI.InvoiceUI.Invoice_Create_Frame;
import UI.InvoiceUI.Invoice_List_Frame;
import UI.ProductUI.Product_Manage_Frame;

import javax.swing.*;
import java.awt.*;

// Nguyễn Trung Nghĩa

public class Home_Frame extends Base_Frame{

    JButton qlspButton, tao_gio_hangButton, btn_Import_List, btn_Invoice_List, exitButton;
    JLabel title, footerLabel;
    JPanel panel;

    public Home_Frame(){

        title = new JLabel("PHẦN MỀM QUẢN LÝ BÁN HÀNG", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 40));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

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

        qlspButton.setPreferredSize(new Dimension(250, 60));
        qlspButton.setFocusable(false);
        qlspButton.addActionListener(e -> qlsp());

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

        tao_gio_hangButton.setPreferredSize(new Dimension(250, 60));
        tao_gio_hangButton.setFocusable(false);
        tao_gio_hangButton.addActionListener(e -> tao_gio_hang());

        // --------------------- tao_gio_hangButton ---------------------

        // --------------------- btn_Import_List ---------------------

        btn_Import_List = new JButton();
        btn_Import_List.setLayout(new BorderLayout());

        JLabel iconLabel3 = new JLabel(new ImageIcon("image/import.png"));
        iconLabel3.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JLabel textLabel3 = new JLabel("Danh sách phiếu nhập", SwingConstants.CENTER); // Sửa text
        textLabel3.setFont(new Font("Inter", Font.BOLD, 30));

        btn_Import_List.add(iconLabel3, BorderLayout.WEST);
        btn_Import_List.add(textLabel3, BorderLayout.CENTER);

        btn_Import_List.setPreferredSize(new Dimension(250, 60));
        btn_Import_List.setFocusable(false);
        btn_Import_List.addActionListener(e -> Import_List());

        // --------------------- btn_Import_List ---------------------

        // --------------------- btn_Invoice_List ---------------------

        btn_Invoice_List = new JButton();
        btn_Invoice_List.setLayout(new BorderLayout());

        JLabel iconLabel4 = new JLabel(new ImageIcon("image/invoice.png"));
        iconLabel4.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JLabel textLabel4 = new JLabel("Danh sách hóa đơn", SwingConstants.CENTER); // Sửa text
        textLabel4.setFont(new Font("Inter", Font.BOLD, 30));

        btn_Invoice_List.add(iconLabel4, BorderLayout.WEST);
        btn_Invoice_List.add(textLabel4, BorderLayout.CENTER);

        btn_Invoice_List.setPreferredSize(new Dimension(250, 60));
        btn_Invoice_List.setFocusable(false);
        btn_Invoice_List.addActionListener(e -> Invoice_List());

        // --------------------- btn_Invoice_List ---------------------

        // --------------------- exitButton ---------------------

        exitButton = new JButton();
        exitButton.setLayout(new BorderLayout());

        JLabel iconLabel5 = new JLabel(new ImageIcon("image/exit.png"));
        iconLabel5.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        JLabel textLabel5 = new JLabel("Thoát", SwingConstants.CENTER);
        textLabel5.setFont(new Font("Inter", Font.BOLD, 30));

        exitButton.add(iconLabel5, BorderLayout.WEST);
        exitButton.add(textLabel5, BorderLayout.CENTER);

        exitButton.setPreferredSize(new Dimension(250, 60));
        exitButton.setFocusable(false);
        exitButton.addActionListener(e -> exit());

        // --------------------- exitButton ---------------------

        panel = new JPanel();
        panel.setBackground(new Color(0xE0F2F1));
        panel.setOpaque(true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));

        panel.add(qlspButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(tao_gio_hangButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(btn_Import_List);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(btn_Invoice_List);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(exitButton);

        setTitle("PHẦN MỀM QUẢN LÝ BÁN HÀNG");
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(footerLabel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void qlsp(){
        dispose();
        try{
            new Product_Manage_Frame();
        } catch (Exception e){
            System.out.println("Chuyển frame sang qlsp bị lỗi");
        }
    }

    private void tao_gio_hang(){
        dispose();
        try{
            new Invoice_Create_Frame();
        } catch (Exception e){
            System.out.println("Chuyển frame sang tạo giỏ hàng bị lỗi");
        }
    }

    private void Import_List(){
        dispose();
        try{
            new Import_List_Frame();
        } catch (Exception e){
            System.out.println("Chuyển frame sang xem danh sách phiếu nhập bị lỗi");
        }
    }

    private void Invoice_List(){
        dispose();
        try{
            new Invoice_List_Frame();
        } catch (Exception e){
            System.out.println("Chuyển frame sang xem danh sách hóa đơn bị lỗi");
        }
    }

    private void exit(){
        UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
        int result = JOptionPane.showConfirmDialog(this,
                "Bạn có muốn thoát không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}