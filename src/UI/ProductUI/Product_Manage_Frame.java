package UI.ProductUI;

import DAO.Service.ProductSEV.Product_Service;
import Model.Product;
import UI.Home_Frame;
import UI.ImportUI.Import_Frame;
import UI.InvoiceUI.Invoice_Create_Frame;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

//Nguyễn Văn Tiến
public class Product_Manage_Frame extends JFrame {
    private JTable tbProduct;
    private DefaultTableModel model;
    private JTextField txt_Search;
    private JButton btn_Add, btn_Delete, btn_Import, btn_Back, btn_CreateInvoice, btn_Search;
    private Product_Service product_service = new Product_Service();


    public Product_Manage_Frame() {
        setTitle(" Quản lý sản phẩm ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        // Panel top ------------------------------------------------------
        JPanel pnltop = new JPanel();
        JLabel header = new JLabel("Quản lý sản phẩm");
        pnltop.add(header);
        pnltop.setBackground(new Color(189, 220, 239));
        add(pnltop, BorderLayout.NORTH);

        // Panel Center -------------------------------------------------------------
        JPanel pnlcenter = new JPanel(new BorderLayout());
        // Panel Center_top-------------
        JPanel pnl_center_top = new JPanel(null);
        pnl_center_top.setPreferredSize(new Dimension(600, 50));
        txt_Search = new JTextField();
        btn_Search = new JButton("Search");
        txt_Search.setBounds(300, 10, 150, 30);
        btn_Search.setBounds(460, 15, 80, 20);
        pnl_center_top.add(txt_Search);
        pnl_center_top.add(btn_Search);
        pnlcenter.add(pnl_center_top, BorderLayout.NORTH);

        // Panel Center_table-----------
        String[] cols = new String[] { "ID", "Mã SP", "Tên SP", "Đơn vị", "Đơn giá", "Số lượng" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            // Cột id không được sửa trên bảng
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        };
        tbProduct = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tbProduct);
        pnlcenter.add(scrollPane, BorderLayout.CENTER);

        add(pnlcenter, BorderLayout.CENTER);

        // Cập nhật dữ liệu của bảng
        LoadTable();

        // Sửa dữ liệu trên bảng
        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow(); // hàng đầu tiên bị thay đổi
                    updateProduct(row);
                }
            }
        });

        // Panel bottom -----------------------------------------------
        JPanel pnlbottom = new JPanel();
        btn_Add = new JButton("Thêm sản phẩm");
        btn_Delete = new JButton("Xóa");
        btn_Import = new JButton("Nhập hàng");
        btn_CreateInvoice = new JButton("Tạo đơn");
        btn_Back = new JButton("Quay lại");
        pnlbottom.add(btn_Add);
        pnlbottom.add(btn_Delete);
        pnlbottom.add(btn_Import);
        pnlbottom.add(btn_Back);
        pnlbottom.add(btn_CreateInvoice);
        add(pnlbottom, BorderLayout.SOUTH);

        // Xử lý sự kiện -------------------------------------------------------
        btn_Add.addActionListener(e -> Add_Product());
        btn_Delete.addActionListener(e -> Delete_Product());
        btn_Import.addActionListener(e -> Import_Product());
        btn_Back.addActionListener(e -> Back());
        btn_CreateInvoice.addActionListener(e -> InvoiceCreate());
        btn_Search.addActionListener(e -> Search_Product());
    }

    private void LoadTable() {
        Product_Service product_service = new Product_Service();
        model.setRowCount(0);
        ArrayList<Product> list = product_service.getAll();
        for (Product p : list) {
            if (p.getSoluong() <= 10) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Sản phẩm \"" + p.getTenSP() + "\" sắp hết hàng!\n(Số lượng còn: " + p.getSoluong() + ")",
                        "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            }
            model.addRow(
                    new Object[] { p.getId(), p.getMaSP(), p.getTenSP(), p.getDonvi(), p.getGia(), p.getSoluong() });
        }
    }

    // Load lại bảng khi thêm sản phẩm xong
    public void refreshTable() {
        LoadTable();
    }

    private void Search_Product() {
        String key = txt_Search.getText().toLowerCase();
        model.setRowCount(0);
        ArrayList<Product> list = product_service.getAll();
        for (Product p : list) {
            if (p.getMaSP().toLowerCase().contains(key) || p.getTenSP().toLowerCase().contains(key)) {
                model.addRow(new Object[] { p.getId(), p.getMaSP(), p.getTenSP(), p.getDonvi(), p.getGia(),
                        p.getSoluong() });
            }
        }
    }

    private void updateProduct(int row) {
        try {
            int id = (int) model.getValueAt(row, 0);
            String maSP = (String) model.getValueAt(row, 1);
            String tenSP = (String) model.getValueAt(row, 2);
            String donvi = (String) model.getValueAt(row, 3);
            double gia = Double.parseDouble(model.getValueAt(row, 4).toString());
            int soluong = Integer.parseInt(model.getValueAt(row, 5).toString());

            Product sp = new Product(id,tenSP, donvi, gia, soluong);

            // Update DB
            boolean ok = product_service.updateProduct(sp);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm đang gặp lỗi!");
        }
    }

    // Thêm parent vào Add_Frame để dữ liệu được cập nhật real-time
    private void Add_Product() {
        Add_Frame add_frame = new Add_Frame(this);
        add_frame.setVisible(true);
    }

    private void Delete_Product() {
        int row = tbProduct.getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc xóa sản phẩm này không?", "Cảnh báo!",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) model.getValueAt(row, 0);
                if (product_service.deleteProduct(id)) {
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn sản phẩm để xóa!");
        }
    }

    private void Import_Product() {
        new Import_Frame();
    }

    private void Back() {
        Home_Frame home_frame = new Home_Frame();
        home_frame.setVisible(true);
        dispose();
    }

    private void InvoiceCreate() {
        Invoice_Create_Frame invoice_create_frame = new Invoice_Create_Frame();

    }

    public static void main(String[] args) {
        new Product_Manage_Frame().setVisible(true);

    }
}