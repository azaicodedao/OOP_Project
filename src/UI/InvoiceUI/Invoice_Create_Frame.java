package UI.InvoiceUI;

import DAO.Service.InvoiceSEV.Invoice_Service;
import DAO.Service.InvoiceSEV.Invoice_Detail_Service;
import DAO.Service.ProductSEV.Product_Service;
import Model.*;
import UI.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Invoice_Create_Frame extends Base_Frame {

    // Thuộc tính UI và Dịch vụ
    private JComboBox<Product> cb_Product;
    private JTable tb_Invoice;
    private DefaultTableModel modelTable;
    private JButton btn_Add, btn_Home, btn_Delete, btn_Save;
    private JTextField txt_TongTien;
    private JLabel headerLabel;

    private final Invoice_Service invoice_service = new Invoice_Service();
    private final Invoice_Detail_Service invoice_detail_service = new Invoice_Detail_Service();
    private final Product_Service product_service = new Product_Service();


    private final Color TABLE_HEADER_BACKGROUND = new Color(0xDCE6F1);

    public Invoice_Create_Frame() {
        setTitle("Giỏ hàng sản phẩm");
        setLayout(new BorderLayout());

        //Tiêu đề (NORTH)
        headerLabel = new JLabel("GIỎ HÀNG SẢN PHẨM", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Poppins", Font.BOLD, 40));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        headerLabel.setBackground(background_color);
        headerLabel.setOpaque(true);
        add(headerLabel, BorderLayout.NORTH);

        //Panel Center_Top chứa combobox, buttons, ...
        JPanel pnl_center_top = createPanelBottom();
        pnl_center_top.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // Thiết lập JComboBox
        cb_Product = createComboBox();

        // Trường nhập số lượng đơn giản
        JTextField txt_input_soluong = createTextField();
        txt_input_soluong.setPreferredSize(new Dimension(80, 35));
        txt_input_soluong.setText("1");
        txt_input_soluong.setFont(new Font("Inter", Font.PLAIN, 20));

        btn_Add = createButton16("Thêm");
        btn_Delete = createButton16("Xóa");

        // LABEL
        JLabel productLabel = createLabel("Sản phẩm:");
        JLabel quantityLabel = createLabel("Số lượng:");

        pnl_center_top.add(productLabel);
        pnl_center_top.add(cb_Product);
        pnl_center_top.add(quantityLabel);
        pnl_center_top.add(txt_input_soluong);
        pnl_center_top.add(btn_Add);
        pnl_center_top.add(btn_Delete);

        //Bảng (Center)
        String[] cols = new String[] { "STT", "ID SP (Ẩn)", "Tên Sản Phẩm", "Đơn vị", "Đơn giá", "Số lượng" };
        modelTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 || columnIndex == 1 || columnIndex == 5 ? Integer.class : super.getColumnClass(columnIndex);
            }
        };
        tb_Invoice = createTable(modelTable);

        // Cấu hình Renderer (Sử dụng Renderer đã có trong Base_Frame)
        tb_Invoice.getColumnModel().getColumn(0).setCellRenderer(center_Renderer);
        tb_Invoice.getColumnModel().getColumn(2).setCellRenderer(center_Renderer);
        tb_Invoice.getColumnModel().getColumn(3).setCellRenderer(center_Renderer);
        tb_Invoice.getColumnModel().getColumn(4).setCellRenderer(right_Renderer);
        tb_Invoice.getColumnModel().getColumn(5).setCellRenderer(center_Renderer);

        tb_Invoice.getColumnModel().getColumn(0).setMaxWidth(40);
        tb_Invoice.getColumnModel().getColumn(1).setMinWidth(200);
        tb_Invoice.getColumnModel().getColumn(2).setMinWidth(70);
        tb_Invoice.getColumnModel().getColumn(4).setMinWidth(150);

        JTableHeader header = tb_Invoice.getTableHeader();
        header.setReorderingAllowed(false); // Không cho kéo cột
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));

        // Ẩn cột ID sản phẩm
        tb_Invoice.getColumnModel().getColumn(1).setMinWidth(0);
        tb_Invoice.getColumnModel().getColumn(1).setMaxWidth(0);
        tb_Invoice.getColumnModel().getColumn(1).setWidth(0);

        JScrollPane scrollPane = createScrollPane(tb_Invoice);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JPanel centerPanel = createPanelBottom();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(pnl_center_top, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);


        //Panel Bottom (SOUTH)
        JPanel pnl_bottom = createPanelBottom();
        pnl_bottom.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 15));

        btn_Home = createButton20("Quay lại");
        btn_Save = createButton20("Lưu");

        txt_TongTien = createTextField();
        txt_TongTien.setPreferredSize(new Dimension(150, 35));
        txt_TongTien.setEditable(false);
        txt_TongTien.setHorizontalAlignment(JTextField.RIGHT);
        txt_TongTien.setFont(new Font("Inter", Font.BOLD, 18));

        JLabel totalLabel = createLabel("Tổng tiền:");
        totalLabel.setFont(new Font("Inter", Font.BOLD, 20));

        pnl_bottom.add(btn_Home);
        pnl_bottom.add(btn_Save);
        pnl_bottom.add(totalLabel);
        pnl_bottom.add(txt_TongTien);

        add(pnl_bottom, BorderLayout.SOUTH);

        // Xử lý sự kiện và Load Dữ liệu
        LoadProductsToComboBox();

        // ÁP DỤNG SỬA LỖI: BUỘC DỪNG CHỈNH SỬA TRƯỚC KHI THAO TÁC NÚT
        btn_Add.addActionListener(e -> { stopTableEditing(); Add_Item_From_Input(txt_input_soluong); });
        btn_Delete.addActionListener(e -> { stopTableEditing(); Delete_Item(); });
        btn_Save.addActionListener(e -> { stopTableEditing(); Save_Invoice(); });
        btn_Home.addActionListener(e -> Back_Home());

        // TableModelListener chỉ gọi Tinhtong() sau khi commit thành công
        modelTable.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    if (row >= 0 && col == 5) {
                        updateRow(row);
                    }
                }
            }
        });
        setVisible(true);
    }

    private void stopTableEditing() {
        if (tb_Invoice.isEditing()) {
            tb_Invoice.getCellEditor().stopCellEditing();
        }
    }

    private void Add_Item_From_Input(JTextField inputField) {
        Product selectedProduct = (Product) cb_Product.getSelectedItem();
        String soluongText = inputField.getText().trim();
        int soluongThem;

        try {
            soluongThem = Integer.parseInt(soluongText);
            if (soluongThem <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Lấy thông tin tồn kho thực tế
        int productId = selectedProduct.getId();
        Product productInStock = product_service.getProductById(productId).orElse(null);
        int stockQty = productInStock.getSoluong();
        int existingRow = findProductInTable(productId); // xác định cần cộng thêm vào số lượng hay thêm dòng mới
        int totalQtyInCart;
        int currentQtyInCart = 0;

        if (existingRow != -1) {
            currentQtyInCart = (int) modelTable.getValueAt(existingRow, 5);
            totalQtyInCart = currentQtyInCart + soluongThem;

            if (totalQtyInCart > stockQty) {
                JOptionPane.showMessageDialog(this,
                        "Không đủ tồn kho! Tồn kho: " + stockQty + ", Đã có trong giỏ: " + currentQtyInCart,
                        "Lỗi Tồn Kho", JOptionPane.ERROR_MESSAGE);
                return;
            }

            modelTable.setValueAt(totalQtyInCart, existingRow, 5);
        } else {
            totalQtyInCart = soluongThem;

            if (totalQtyInCart > stockQty) {
                JOptionPane.showMessageDialog(this,
                        "Không đủ tồn kho! Tồn kho hiện tại: " + stockQty,
                        "Lỗi Tồn Kho", JOptionPane.ERROR_MESSAGE);
                return;
            }

            modelTable.addRow(new Object[]{
                    modelTable.getRowCount() + 1,
                    selectedProduct.getId(),
                    selectedProduct.getTenSP(),
                    selectedProduct.getDonvi(),
                    MoneyFormat.format(selectedProduct.getGia()),
                    soluongThem
            });
        }
        Tinhtong();
        inputField.setText("1");
    }

    private void Delete_Item() {
        int[] selectedRows = tb_Invoice.getSelectedRows();
        if (modelTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng rỗng, không có gì để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (selectedRows.length > 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc xóa " + selectedRows.length + " sản phẩm đã chọn không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    modelTable.removeRow(selectedRows[i]);
                }
                reNumberTableRows();
                Tinhtong();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa trên bảng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void Save_Invoice() {
        if (modelTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng rỗng. Vui lòng thêm sản phẩm trước khi lưu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double tongTien = getTongTienValue();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận lưu đơn hàng với tổng tiền: " + MoneyFormat.format(tongTien) + "?\n(Thao tác này sẽ trừ số lượng tồn kho)",
                "Xác nhận Lưu & Trừ Tồn Kho", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        // LƯU HÓA ĐƠN VÀ CHI TIẾT (Logic CSDL)
        Invoice newInvoice = new Invoice(tongTien, LocalDateTime.now());
        int newInvoiceId = invoice_service.insert(newInvoice);
        if (newInvoiceId > 0) {
            boolean detailsOk = true;
            boolean stockUpdated = true;

            // LƯU CHI TIẾT VÀ TRỪ TỒN KHO TRONG CÙNG VÒNG LẶP
            for (int i = 0; i < modelTable.getRowCount(); i++) {
                try {
                    int productId = (int) modelTable.getValueAt(i, 1);
                    double donGia = MoneyFormat.parse((String) modelTable.getValueAt(i, 4));
                    // Lấy số lượng an toàn
                    Object soluongObj = modelTable.getValueAt(i, 5);
                    int soluong = Integer.parseInt(soluongObj.toString());
                    // LƯU CHI TIẾT HÓA ĐƠN
                    Invoice_Detail detail = new Invoice_Detail(
                            0, newInvoiceId, productId, soluong, donGia, 0.0
                    );
                    if (!invoice_detail_service.insert(detail)) {
                        detailsOk = false;
                        JOptionPane.showMessageDialog(this,
                                "Lỗi khi chèn chi tiết sản phẩm có ID: " + productId,
                                "Lỗi chi tiết hóa đơn", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    //TRỪ SỐ LƯỢNG TỒN KHO
                    product_service.updateInvoice(productId, soluong);
                } catch (Exception e) {
                    stockUpdated = false;
                    JOptionPane.showMessageDialog(this,
                            "LỖI GIAO DỊCH: Không trừ được tồn kho hoặc lỗi khác.\nChi tiết: " + e.getMessage(),
                            "Lỗi xử lý", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            // THÔNG BÁO VÀ HOÀN TẤT
            if (detailsOk && stockUpdated) {
                JOptionPane.showMessageDialog(this, "Lưu hóa đơn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                // Dọn dẹp giao diện
                modelTable.setRowCount(0);
                Tinhtong();
            }
        } else {
            // Lỗi này xảy ra khi Invoice_Service.insert() trả về -1
            JOptionPane.showMessageDialog(this, "Lưu hóa đơn cha thất bại.", "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int findProductInTable(int productId) {
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            int id = (int) modelTable.getValueAt(i, 1);
            if (id == productId) {
                return i;
            }
        }
        return -1;
    }

    private void reNumberTableRows() {
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            modelTable.setValueAt(i + 1, i, 0);
        }
    }

    private void Back_Home() {
        new Home_Frame().setVisible(true);
        dispose();
    }

    private double getTongTienValue() {
        double tongTien = 0.0;
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            try {
                String giaText = (String) modelTable.getValueAt(i, 4);
                Object soluongObj = modelTable.getValueAt(i, 5);
                int soluong = Integer.parseInt(soluongObj.toString());

                double gia = MoneyFormat.parse(giaText);
                tongTien += gia * soluong;
            } catch (Exception e) {
                System.err.println("Lỗi khi tính tổng tiền tại hàng " + i + ": " + e.getMessage());
            }
        }
        return tongTien;
    }

    private void Tinhtong() {
        double tongTien = getTongTienValue();
        txt_TongTien.setText(MoneyFormat.format(tongTien));
    }

    private void LoadProductsToComboBox() {
        cb_Product.removeAllItems();
        ArrayList<Product> productList = product_service.getAll();
        for (Product p : productList) {
            cb_Product.addItem(p);
        }
    }

    private void updateRow(int row) {
        try {
            int productId = (int) modelTable.getValueAt(row, 1);
            Object soluongObj = modelTable.getValueAt(row, 5);
            // Kiểm tra nhập số
            int newQty = Integer.parseInt(soluongObj.toString());
            if (newQty <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                modelTable.setValueAt(1, row, 5); // tránh lỗi khi tính tổng tiền
                return;
            }
            // Kiểm tra tồn kho
            Product productInStock = product_service.getProductById(productId).orElse(null);
//            if (productInStock == null) {
//                JOptionPane.showMessageDialog(this, "Không thể kiểm tra tồn kho.", "Lỗi tồn kho", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
            int stockQty = productInStock.getSoluong();
            if (newQty > stockQty) {
                JOptionPane.showMessageDialog(this,
                        "Không đủ tồn kho! Tối đa có thể đặt là: " + stockQty,
                        "Lỗi tồn kho", JOptionPane.ERROR_MESSAGE);
                modelTable.setValueAt(stockQty, row, 5);
                return;
            }
            Tinhtong();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            modelTable.setValueAt(1, row, 5);
        }
    }

    public static void main(String[] args) {
        new Invoice_Create_Frame();
    }
}