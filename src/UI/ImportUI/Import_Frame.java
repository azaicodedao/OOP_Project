package UI.ImportUI;

import Model.Product;
import Model.Import;
import Model.Import_Detail;
import DAO.Service.ImportSEV.Import_Service;
import DAO.Service.ImportSEV.Import_Detail_Service;
import DAO.Service.ProductSEV.Product_Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

// Trần Thanh Tùng

public class Import_Frame extends JFrame {

    private JComboBox<Product> cb_Product;
    private JTable tb_Import;
    private DefaultTableModel modelTable;
    private JLabel lb_Tongtien;
    private JTextField txt_SoLuong, txt_GiaNhap, txt_GiaBan;
    private JButton btn_Import, btn_Back;

    private final Import_Service import_service = new Import_Service();
    private final Import_Detail_Service import_detail_service = new Import_Detail_Service();
    private final Product_Service product_service = new Product_Service();

    private double tongTien = 0;

    public Import_Frame() {
        setTitle("Phiếu nhập hàng");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        LoadData();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        // ====== TOP: chọn sản phẩm ======
        JPanel topPanel = new JPanel(new GridLayout(2, 5, 10, 10));

        cb_Product = new JComboBox<>();
        txt_SoLuong = new JTextField();
        txt_GiaNhap = new JTextField();
        txt_GiaBan = new JTextField();
        lb_Tongtien = new JLabel("0", SwingConstants.RIGHT);
        btn_Import = new JButton("Nhập hàng");
        btn_Back = new JButton("Quay lại");

        topPanel.add(new JLabel("Sản phẩm:"));
        topPanel.add(cb_Product);
        topPanel.add(new JLabel("Số lượng:"));
        topPanel.add(txt_SoLuong);
        topPanel.add(new JLabel("Giá nhập:"));
        topPanel.add(txt_GiaNhap);
        topPanel.add(new JLabel("Giá bán:"));
        topPanel.add(txt_GiaBan);
        topPanel.add(btn_Import);
        topPanel.add(btn_Back);

        // ====== TABLE ======
        String[] columns = {"Mã SP", "Tên sản phẩm", "Đơn vị", "Số lượng", "Giá nhập", "Giá bán", "Thành tiền"};
        modelTable = new DefaultTableModel(columns, 0);
        tb_Import = new JTable(modelTable);
        JScrollPane scrollPane = new JScrollPane(tb_Import);

        // ====== BOTTOM: tổng tiền ======
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(new JLabel("Tổng tiền: "));
        bottomPanel.add(lb_Tongtien);

        // ====== Thêm vào frame ======
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        // ====== Sự kiện ======
        btn_Import.addActionListener(e -> Import_Product());
        btn_Back.addActionListener(e -> Back());
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
        dispose(); // Đóng frame này
        // new Main_Frame().setVisible(true); // nếu có trang chính
    }

    // ====== Xử lý nhập hàng ======
    private void Import_Product() {
        try {
            Product selectedProduct = (Product) cb_Product.getSelectedItem();
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(this, "Chưa chọn sản phẩm!");
                return;
            }

            int soLuong = Integer.parseInt(txt_SoLuong.getText());
            double giaNhap = Double.parseDouble(txt_GiaNhap.getText());
            double giaBan = Double.parseDouble(txt_GiaBan.getText());

            double thanhTien = soLuong * giaNhap;
            tongTien += thanhTien;
            lb_Tongtien.setText(String.valueOf(tongTien));

            // Thêm vào bảng hiển thị
            modelTable.addRow(new Object[]{
                    selectedProduct.getMaSP(),
                    selectedProduct.getTenSP(),
                    selectedProduct.getDonvi(),
                    soLuong,
                    giaNhap,
                    giaBan,
                    thanhTien
            });

            // Tạo đối tượng phiếu nhập bằng constructor hiện có (không sửa Import.java)
            Import imp = new Import(tongTien, LocalDateTime.now());

            // Gọi service để insert phiếu nhập (Import_Service.insert returns boolean in your code)
            boolean inserted = import_service.insert(imp);
            if (!inserted) {
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thất bại!");
                return;
            }

            // Tạo chi tiết phiếu nhập (lưu ý: id_Import chưa được gán vì insert() không trả về id)
            Import_Detail detail = new Import_Detail();
            // nếu Import_Detail có trường id_Import, bạn có thể set 0 hoặc bỏ qua:
            detail.setId_Import(0); // tạm; DB có thể từ chối nếu FK NOT NULL
            detail.setId_Product(selectedProduct.getId());
            detail.setSoluong(soLuong);
            detail.setGiaNhap(giaNhap);
            detail.setGiaBan(giaBan);
            import_detail_service.insert(detail);

            JOptionPane.showMessageDialog(this, "Nhập hàng thành công!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi nhập hàng!");
        }
    }
}
