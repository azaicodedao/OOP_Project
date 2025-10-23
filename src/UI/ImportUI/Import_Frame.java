package UI.ImportUI;

import Model.Import_Detail;
import Model.Product;
import DAO.Service.ImportSEV.Import_Service;
import DAO.Service.ImportSEV.Import_Detail_Service;
import DAO.Service.ProductSEV.Product_Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Trần Thanh Tùng

public class Import_Frame extends JFrame {

    private JComboBox<Product> cb_Product;
    private JTable tb_Import;
    private DefaultTableModel modelTable;
    private JLabel lb_Tongtien;
    private JTextField txt_SoLuong, txt_GiaNhap, txt_GiaBan;
    private JButton btn_Import, btn_Back;

    private Import_Service import_service;
    private Import_Detail_Service import_detail_service;
    private Product_Service product_service;

    public Import_Frame() {
        import_service = new Import_Service();
        import_detail_service = new Import_Detail_Service();
        product_service = new Product_Service();

        setTitle("Nhập hàng - Quản lý bán hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadProducts();
        addEvents();

        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- TOP: Nhập sản phẩm ---
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        cb_Product = new JComboBox<>();
        txt_SoLuong = new JTextField();
        txt_GiaNhap = new JTextField();
        txt_GiaBan = new JTextField();

        inputPanel.add(new JLabel("Sản phẩm:"));
        inputPanel.add(cb_Product);
        inputPanel.add(new JLabel("Số lượng nhập:"));
        inputPanel.add(txt_SoLuong);
        inputPanel.add(new JLabel("Giá nhập:"));
        inputPanel.add(txt_GiaNhap);
        inputPanel.add(new JLabel("Giá bán:"));
        inputPanel.add(txt_GiaBan);

        btn_Import = new JButton("Thêm vào phiếu nhập");
        btn_Back = new JButton("Quay lại");

        inputPanel.add(btn_Import);
        inputPanel.add(btn_Back);

        // --- CENTER: Bảng hiển thị chi tiết nhập ---
        modelTable = new DefaultTableModel(new Object[]{"Mã SP", "Tên sản phẩm", "SL", "Giá nhập", "Giá bán", "Thành tiền"}, 0);
        tb_Import = new JTable(modelTable);
        JScrollPane scrollPane = new JScrollPane(tb_Import);

        // --- BOTTOM: Tổng tiền ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lb_Tongtien = new JLabel("Tổng tiền: 0");
        bottomPanel.add(lb_Tongtien);

        // --- Thêm tất cả vào mainPanel ---
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadProducts() {
        ArrayList<Product> list = product_service.getAll();
        for (Product p : list) {
            cb_Product.addItem(p);
        }
    }

    private void addEvents() {
        // Thêm chi tiết nhập hàng
        btn_Import.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Import_Product();
            }
        });

        // Quay lại
        btn_Back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Back();
            }
        });
    }

    private void Back() {
        dispose();
        new UI.Home_Frame(); // mở giao diện chính
    }

    private void Import_Product() {
        try {
            Product product = (Product) cb_Product.getSelectedItem();
            int soLuong = Integer.parseInt(txt_SoLuong.getText());
            double giaNhap = Double.parseDouble(txt_GiaNhap.getText());
            double giaBan = Double.parseDouble(txt_GiaBan.getText());
            double thanhTien = soLuong * giaNhap;

            // Hiển thị lên bảng
            modelTable.addRow(new Object[]{
                    product.getId(),
                    product.getTenSP(),
                    soLuong,
                    giaNhap,
                    giaBan,
                    thanhTien
            });

            // Cập nhật tổng tiền
            double tong = 0;
            for (int i = 0; i < modelTable.getRowCount(); i++) {
                tong += (double) modelTable.getValueAt(i, 5);
            }
            lb_Tongtien.setText("Tổng tiền: " + tong);

            // Lưu xuống DB
            Import_Detail detail = new Import_Detail(0, product.getId(), soLuong, giaNhap, giaBan);
            import_detail_service.insert(detail);

            JOptionPane.showMessageDialog(this, "Đã thêm vào phiếu nhập!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu!");
        }
    }
}


