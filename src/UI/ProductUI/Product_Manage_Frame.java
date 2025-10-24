package UI.ProductUI;

import DAO.Service.ProductSEV.Product_Service;
import Model.Product;
import UI.Base_Frame;
import UI.Home_Frame;
import UI.ImportUI.Import_Frame;
import UI.InvoiceUI.Invoice_Create_Frame;
import UI.MoneyFormat;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

//Nguyễn Văn Tiến
public class Product_Manage_Frame extends Base_Frame {
    private JTable tbProduct;
    private DefaultTableModel model;
    private JTextField txt_Search;
    private JButton btn_Add, btn_Refresh, btn_Import, btn_Back, btn_CreateInvoice, btn_Search;
    private Product_Service product_service = new Product_Service();


    public Product_Manage_Frame() {
        setTitle(" Quản lý sản phẩm ");


        // Panel top ------------------------------------------------------
        JPanel pnltop = new JPanel(new FlowLayout(FlowLayout.CENTER,10,5));
        pnltop.setBackground(background_color);
        JLabel header = new JLabel("Quản lý sản phẩm");
        header.setFont(new Font("Poppins", Font.BOLD, 20));
        pnltop.add(header);
        pnltop.setBackground(background_color);
        add(pnltop, BorderLayout.NORTH);

        // Panel bottom -----------------------------------------------
        JPanel pnlbottom = createPanel();
        btn_Add = createButton16("Thêm SP");
        btn_Refresh = createButton16("Làm mới");
        btn_Import = createButton16("Nhập hàng");
        btn_Import.setPreferredSize(new Dimension(150,35));
        btn_CreateInvoice = createButton16("Tạo đơn");
        btn_Back =createButton16("Quay lại");
        pnlbottom.add(btn_Add);
        pnlbottom.add(btn_Refresh);
        pnlbottom.add(btn_Import);
        pnlbottom.add(btn_Back);
        pnlbottom.add(btn_CreateInvoice);
        add(pnlbottom, BorderLayout.SOUTH);

        // Panel Center -------------------------------------------------------------
        JPanel pnlcenter = new JPanel(new BorderLayout());
        pnlcenter.setBackground(background_color);
        // Panel Center_top-------------
        JPanel pnl_center_top = new JPanel(new FlowLayout(FlowLayout.RIGHT,40,5));
        pnl_center_top.setBackground(background_color);
        txt_Search = createTextField();
        btn_Search = createButton16("Tìm kiếm");
        pnl_center_top.add(txt_Search);
        pnl_center_top.add(btn_Search);
        pnlcenter.add(pnl_center_top, BorderLayout.NORTH);

        // Panel Center_table-----------
        String[] cols = new String[] { "ID","Tên SP", "Đơn vị", "Đơn giá", "Số lượng" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            // Cột id không được sửa trên bảng
            public boolean isCellEditable(int row, int column) {
                return column != 1 && column != 0 && column != 4;
            }
        };
        tbProduct = createTable(model);
        tbProduct.getColumnModel().getColumn(0).setCellRenderer(center_Renderer);
        tbProduct.getColumnModel().getColumn(1).setCellRenderer(center_Renderer);
        tbProduct.getColumnModel().getColumn(2).setCellRenderer(center_Renderer);
        tbProduct.getColumnModel().getColumn(3).setCellRenderer(right_Renderer);
        tbProduct.getColumnModel().getColumn(4).setCellRenderer(center_Renderer);
        JScrollPane scrollPane = createScrollPane(tbProduct);
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

        // Xử lý sự kiện -------------------------------------------------------
        btn_Add.addActionListener(e -> Add_Product());
        btn_Refresh.addActionListener(e -> refreshTable());
        btn_Import.addActionListener(e -> Import_Product());
        btn_Back.addActionListener(e -> Back());
        btn_CreateInvoice.addActionListener(e -> InvoiceCreate());
        btn_Search.addActionListener(e -> Search_Product());
    }

    private void LoadTable() {
        Product_Service product_service = new Product_Service();
        model.setRowCount(0);
        ArrayList<Product> list = product_service.getAll();
        StringBuilder res = new StringBuilder();
        for (Product p : list) {
            if (p.getSoluong() <= 5) {
                res.append(p.getTenSP()).append(": ").append(p.getSoluong()).append("\n");
            }
            model.addRow(
                    new Object[] { p.getId(), p.getTenSP(), p.getDonvi(), MoneyFormat.format(p.getGia()), p.getSoluong() });
        }
        String result = res.toString().trim();
        if(result.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Sản phẩm sắp hết :\n"+ result,
                    "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
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
            if (p.getTenSP().toLowerCase().contains(key)) {
                model.addRow(new Object[] { p.getId(), p.getTenSP(), p.getDonvi(), MoneyFormat.format(p.getGia()),
                        p.getSoluong() });
            }
        }
    }

    private void updateProduct(int row) {
        try {
            int id = (int) model.getValueAt(row, 0);
            String tenSP = (String) model.getValueAt(row, 1);
            String donvi = (String) model.getValueAt(row, 2);
            double gia = MoneyFormat.parse(model.getValueAt(row, 3).toString());
            int soluong = Integer.parseInt(model.getValueAt(row, 4).toString());

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
//    private void Delete_Product() {}
//
//    private void Delete_Product() {
//        int row = tbProduct.getSelectedRow();
//        if (row != -1) {
//            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc xóa sản phẩm này không?", "Cảnh báo!",
//                    JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                int id = (int) model.getValueAt(row, 0);
//                if (product_service.deleteProduct(id)) {
//                    model.removeRow(row);
//                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Xóa thất bại");
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "Bạn chưa chọn sản phẩm để xóa!");
//        }
//    }

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