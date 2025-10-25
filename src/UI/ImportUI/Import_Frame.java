package UI.ImportUI;

import Model.Product;
import Model.Import;
import Model.Import_Detail;
import DAO.Service.ImportSEV.Import_Service;
import DAO.Service.ImportSEV.Import_Detail_Service;
import DAO.Service.ProductSEV.Product_Service;
import UI.Base_Frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

// Trần Thanh Tùng

public class Import_Frame extends Base_Frame {

    private JComboBox<Product> cb_Product;
    private JTable tb_Import;
    private DefaultTableModel modelTable;
    private JLabel lb_Tongtien,lb_Product,lb_SoLuong,lb_GiaNhap,lb_GiaBan;
    private JTextField txt_SoLuong, txt_GiaNhap, txt_GiaBan;
    private JButton btn_Import, btn_Back, btn_Add;

    private final Import_Service import_service = new Import_Service();
    private final Import_Detail_Service import_detail_service = new Import_Detail_Service();
    private final Product_Service product_service = new Product_Service();

    private double tongTien = 0;

    public Import_Frame() {
        setTitle("Phiếu nhập hàng");
        setSize(1100, 650);

//        Panel top - Tiêu đề
        JPanel pnlNorth = new JPanel();
        pnlNorth.setBackground(background_color);
        JLabel lb_header = createLabel("BẢNG NHẬP LIỆU");
        lb_header.setFont(new Font("Poppins", Font.BOLD, 30));
        pnlNorth.add(lb_header);
        add(pnlNorth, BorderLayout.NORTH);


//        Panel West - nhập liệu
        JPanel pnlWest = new JPanel();
        pnlWest.setLayout(new BoxLayout(pnlWest, BoxLayout.Y_AXIS));
        lb_Product = createLabel("Sản phẩm");
        lb_SoLuong = createLabel("Số lượng");
        lb_GiaNhap = createLabel("Giá nhập");
        lb_GiaBan = createLabel("Giá bán");

        cb_Product = new JComboBox<>();
        cb_Product.setPreferredSize(new Dimension(150,30));
        txt_SoLuong = createTextField();
        txt_SoLuong.setPreferredSize(new Dimension(150,30));
        txt_GiaNhap = createTextField();
        txt_GiaNhap.setPreferredSize(new Dimension(150,30));
        txt_GiaBan = createTextField();
        txt_GiaBan.setPreferredSize(new Dimension(150,30));

        JPanel pnlProducts = new JPanel();
        pnlProducts.setBackground(selection_color);
        pnlProducts.add(lb_Product);
        pnlProducts.add(cb_Product);

        JPanel pnlSoLuong = new JPanel();
        pnlSoLuong.setBackground(selection_color);
        pnlSoLuong.add(lb_SoLuong);
        pnlSoLuong.add(txt_SoLuong);

        JPanel pnlGiaNhap = new JPanel();
        pnlGiaNhap.setBackground(selection_color);
        pnlGiaNhap.add(lb_GiaNhap);
        pnlGiaNhap.add(txt_GiaNhap);

        JPanel pnlGiaBan = new JPanel();
        pnlGiaBan.setBackground(selection_color);
        pnlGiaBan.add(lb_GiaBan);
        pnlGiaBan.add(txt_GiaBan);

        JPanel pnlAdd = new JPanel();
        pnlAdd.setBackground(selection_color);
        btn_Add = createButton16("Thêm");
        pnlAdd.add(btn_Add);

        pnlWest.add(pnlProducts);
        pnlWest.add(pnlSoLuong);
        pnlWest.add(pnlGiaNhap);
        pnlWest.add(pnlGiaBan);
        pnlWest.add(pnlAdd);
        add(pnlWest, BorderLayout.WEST);

//        Panle Center - Bảng dữ liệu ------------------------
        String[] columns = {"Mã SP", "Tên SP", "Đơn vị", "Số lượng", "Giá nhập", "Giá bán", "Thành tiền"};
        modelTable = new DefaultTableModel(columns, 0);
        tb_Import = createTable(modelTable);
        JScrollPane scrollPane = createScrollPane(tb_Import);
        add(scrollPane, BorderLayout.CENTER);

//        Panel South - Xử lý sự kiện
        JPanel pnlSouth = createPanel();
        pnlSouth.setLayout(new BoxLayout(pnlSouth, BoxLayout.X_AXIS));

//        Phần button
        JPanel pnl_button = new JPanel(new FlowLayout(FlowLayout.RIGHT,50,5));
        pnl_button.setBackground(background_color);
        btn_Import = createButton16("Nhập hàng");
        btn_Back = createButton16("Bảng sản phẩm");
        btn_Import.setPreferredSize(new Dimension(120,30));
        btn_Back.setPreferredSize(new Dimension(150,30));
        add(pnlSouth, BorderLayout.SOUTH);
        pnl_button.add(btn_Import);
        pnl_button.add(btn_Back);

//        Phần hiển thị
        JPanel pnl_Display = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,5));
        pnl_Display.setBackground(background_color);
        lb_Tongtien = createLabel("Tổng tiền");
        JLabel lb_Display = createLabel("");
        pnl_Display.add(lb_Tongtien);
        pnl_Display.add(lb_Display);

        pnlSouth.add(pnl_button);
        pnlSouth.add(pnl_Display);






    }

    private void initComponents() {

        // ====== TOP: chọn sản phẩm ======
        JPanel topPanel = new JPanel();








        // ====== TABLE ======
        String[] columns = {"Mã SP", "Tên sản phẩm", "Đơn vị", "Số lượng", "Giá nhập", "Giá bán", "Thành tiền"};
        modelTable = new DefaultTableModel(columns, 0);
        tb_Import = createTable(modelTable);
        JScrollPane scrollPane = createScrollPane(tb_Import);

        // ====== BOTTOM: tổng tiền ======
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(new JLabel("Tổng tiền: "));
        bottomPanel.add(lb_Tongtien);

        // ====== Thêm vào frame ======
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


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
                    selectedProduct.getId(),
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

    public static void main(String[] args) {
        new Import_Frame().setVisible(true);
    }
}
