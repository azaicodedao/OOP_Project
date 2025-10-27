package UI.ImportUI;

// Trần Thanh Tùng

import Model.Product;
import Model.Import;
import Model.Import_Detail;
import DAO.Service.ImportSEV.Import_Service;
import DAO.Service.ImportSEV.Import_Detail_Service;
import DAO.Service.ProductSEV.Product_Service;
import UI.Base_Frame;
import UI.ProductUI.Product_Manage_Frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Import_Frame extends Base_Frame {

    private JComboBox<Product> cb_Product;
    private JTable tb_Import;
    private DefaultTableModel modelTable;
    private JLabel lb_Tongtien, lb_Product, lb_SoLuong, lb_GiaNhap, lb_GiaBan;
    private JTextField txt_SoLuong, txt_GiaNhap, txt_GiaBan, txt_Display;
    private JButton btn_Import, btn_Back, btn_Add;

    private final Import_Service import_service = new Import_Service();
    private final Import_Detail_Service import_detail_service = new Import_Detail_Service();
    private final Product_Service product_service = new Product_Service();

    public Import_Frame() {
        setTitle("Phiếu nhập hàng");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // ====== Panel North - Tiêu đề ======
        JPanel pnlNorth = new JPanel();
        pnlNorth.setBackground(background_color);
        JLabel lb_header = createLabel("BẢNG NHẬP LIỆU");
        lb_header.setFont(new Font("Poppins", Font.BOLD, 30));
        pnlNorth.add(lb_header);
        add(pnlNorth, BorderLayout.NORTH);

        // ====== Panel West - Nhập thông tin sản phẩm ======
        JPanel pnlWest = new JPanel();
        pnlWest.setLayout(new BoxLayout(pnlWest, BoxLayout.Y_AXIS));
        pnlWest.setBackground(new Color(0xDCE6F1));
        pnlWest.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lb_Product = createLabel("Sản phẩm:");
        lb_SoLuong = createLabel("Số lượng:");
        lb_GiaNhap = createLabel("Giá nhập:");
        lb_GiaBan = createLabel("Giá bán:");

        cb_Product = createComboBox();
        txt_SoLuong = createTextField();
        txt_GiaNhap = createTextField();
        txt_GiaBan = createTextField();

        pnlWest.add(lb_Product);
        pnlWest.add(cb_Product);
        pnlWest.add(Box.createVerticalStrut(10));
        pnlWest.add(lb_SoLuong);
        pnlWest.add(txt_SoLuong);
        pnlWest.add(Box.createVerticalStrut(10));
        pnlWest.add(lb_GiaNhap);
        pnlWest.add(txt_GiaNhap);
        pnlWest.add(Box.createVerticalStrut(10));
        pnlWest.add(lb_GiaBan);
        pnlWest.add(txt_GiaBan);
        pnlWest.add(Box.createVerticalStrut(20));

        btn_Add = createButton16("Thêm sản phẩm");
        btn_Add.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlWest.add(btn_Add);

        add(pnlWest, BorderLayout.WEST);

        // ====== Panel Center - Bảng hiển thị sản phẩm ======
        String[] columns = {"Mã SP", "Tên SP", "Đơn vị", "Số lượng", "Giá nhập", "Giá bán", "Thành tiền"};
        modelTable = new DefaultTableModel(columns, 0);
        tb_Import = createTable(modelTable);
        JScrollPane scrollPane = createScrollPane(tb_Import);
        add(scrollPane, BorderLayout.CENTER);

        // ====== Panel South - Nút chức năng và tổng tiền ======
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBackground(background_color);
        pnlSouth.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Nhóm nút ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pnlButtons.setBackground(background_color);

        btn_Import = createButton20("Nhập hàng");
        btn_Back = createButton20("Bảng sản phẩm");

        pnlButtons.add(btn_Import);
        pnlButtons.add(btn_Back);

        // --- Tổng tiền ---
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlTotal.setBackground(background_color);

        lb_Tongtien = createLabel("Tổng tiền:");
        txt_Display = createTextField();
        txt_Display.setEditable(false);
        txt_Display.setPreferredSize(new Dimension(150, 30));

        pnlTotal.add(lb_Tongtien);
        pnlTotal.add(txt_Display);

        pnlSouth.add(pnlButtons, BorderLayout.WEST);
        pnlSouth.add(pnlTotal, BorderLayout.EAST);
        add(pnlSouth, BorderLayout.SOUTH);

        // ====== Gọi hàm dữ liệu và sự kiện ======
        LoadData();
        btn_Import.addActionListener(e -> Import_Product());
        btn_Back.addActionListener(e -> Back());
        btn_Add.addActionListener(e -> Add_Product());

        // ====== Hiển thị giao diện ======
        setVisible(true);
    }

    // ====== Load sản phẩm lên combobox ======
    private void LoadData() {
        ArrayList<Product> list = product_service.getAll();
        cb_Product.removeAllItems();
        for (Product p : list) {
            cb_Product.addItem(p);
        }
    }

    // ====== Quay lại ======
    private void Back() {
        dispose();
        new Product_Manage_Frame();
    }

    // ====== Thêm sản phẩm vào bảng ======
    private void Add_Product() {
        Product selectedProduct = (Product) cb_Product.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
            return;
        }

        try {
            int soLuong = Integer.parseInt(txt_SoLuong.getText());
            double giaNhap = Double.parseDouble(txt_GiaNhap.getText());
            double giaBan = Double.parseDouble(txt_GiaBan.getText());
            double thanhTien = soLuong * giaNhap;

            modelTable.addRow(new Object[]{
                    selectedProduct.getId(),
                    selectedProduct.getTenSP(),
                    selectedProduct.getDonvi(),
                    soLuong,
                    giaNhap,
                    giaBan,
                    thanhTien
            });

            updateTotal();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đúng định dạng:\nVD:\nSố lượng: 20\nGiá nhập: 10000\nGiá bán: 20000");
        }
    }

    // ====== Cập nhật tổng tiền ======
    private void updateTotal() {
        double tong = 0;
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            tong += (double) modelTable.getValueAt(i, 6);
        }
        txt_Display.setText(String.valueOf(tong));
    }

    // ====== Xử lý nhập hàng ======
    private void Import_Product() {
        try {
            int rowCount = modelTable.getRowCount();
            if (rowCount == 0) {
                JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào để nhập!");
                return;
            }

            double tongTien = 0;
            for (int i = 0; i < rowCount; i++) {
                tongTien += (double) modelTable.getValueAt(i, 6);
            }

            Import imp = new Import(tongTien, LocalDateTime.now());
            boolean inserted = import_service.insert(imp);
            if (!inserted) {
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thất bại!");
                return;
            }

            for (int i = 0; i < rowCount; i++) {
                int idProduct = (int) modelTable.getValueAt(i, 0);
                int soLuong = (int) modelTable.getValueAt(i, 3);
                double giaNhap = (double) modelTable.getValueAt(i, 4);
                double giaBan = (double) modelTable.getValueAt(i, 5);

                Import_Detail detail = new Import_Detail();
                detail.setId_Import(0);
                detail.setId_Product(idProduct);
                detail.setSoluong(soLuong);
                detail.setGiaNhap(giaNhap);
                detail.setGiaBan(giaBan);

                import_detail_service.insert(detail);
            }

            JOptionPane.showMessageDialog(this, "Nhập hàng thành công!");
            modelTable.setRowCount(0);
            updateTotal();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi nhập hàng!");
        }
    }

    public static void main(String[] args) {
        new Import_Frame();
    }
}
