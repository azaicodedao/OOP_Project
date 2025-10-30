package UI.InvoiceUI;

import DAO.Service.InvoiceSEV.Invoice_Service;
import DAO.Service.InvoiceSEV.Invoice_Detail_Service;
import DAO.Service.ProductSEV.Product_Service;
import Model.*;
import UI.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
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

        // --- 1. Tiêu đề (NORTH) ---
        headerLabel = new JLabel("GIỎ HÀNG SẢN PHẨM", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Poppins", Font.BOLD, 32));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        headerLabel.setBackground(background_color);
        headerLabel.setOpaque(true);
        add(headerLabel, BorderLayout.NORTH);

        // --- 2. Panel Center_Top chứa combobox, buttons, ... ---
        JPanel pnl_center_top = createPanelBottom();
        pnl_center_top.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // Thiết lập JComboBox
        cb_Product = createComboBox();
        cb_Product.setPreferredSize(new Dimension(150, 30));
        cb_Product.setFont(new Font("Inter", Font.PLAIN, 20));

        // Trường nhập số lượng đơn giản
        JTextField txt_input_soluong = createTextField();
        txt_input_soluong.setPreferredSize(new Dimension(80, 35));
        txt_input_soluong.setText("1");
        txt_input_soluong.setFont(new Font("Inter", Font.PLAIN, 20));

        btn_Add = createButton16("Thêm");
        btn_Delete = createButton16("Xóa");

        // LABEL CỠ CHỮ 20 IN ĐẬM
        JLabel productLabel = createLabel("Sản phẩm:");
        productLabel.setFont(new Font("Inter", Font.BOLD, 20));
        JLabel quantityLabel = createLabel("Số lượng:");
        quantityLabel.setFont(new Font("Inter", Font.BOLD, 20));

        pnl_center_top.add(productLabel);
        pnl_center_top.add(cb_Product);
        pnl_center_top.add(quantityLabel);
        pnl_center_top.add(txt_input_soluong);
        pnl_center_top.add(btn_Add);
        pnl_center_top.add(btn_Delete);

        // --- 3. Bảng (Center) ---
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

        // Cấu hình Header (chỉ đặt chiều cao)
        JTableHeader header = tb_Invoice.getTableHeader();
        header.setDefaultRenderer(new SimpleHeaderRenderer(header.getDefaultRenderer()));
        header.setReorderingAllowed(false);
        Dimension headerSize = header.getPreferredSize();
        header.setPreferredSize(new Dimension(headerSize.width, 40));

        // Ẩn cột ID sản phẩm
        tb_Invoice.getColumnModel().getColumn(1).setMinWidth(0);
        tb_Invoice.getColumnModel().getColumn(1).setMaxWidth(0);
        tb_Invoice.getColumnModel().getColumn(1).setWidth(0);

        // ÁP DỤNG TRÌNH SỬA CĂN TRÁI CHO CỘT SỐ LƯỢNG (Index 5)
        JTextField leftAlignedTextField = new JTextField();
        tb_Invoice.getColumnModel().getColumn(5).setCellEditor(new LeftAlignEditor(leftAlignedTextField, this, this.product_service));

        JScrollPane scrollPane = createScrollPane(tb_Invoice);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JPanel centerPanel = createPanelBottom();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(pnl_center_top, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);


        // --- 4. Panel Bottom (SOUTH) ---
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

        // --- Xử lý sự kiện và Load Dữ liệu ---
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
                        SwingUtilities.invokeLater(() -> Tinhtong());
                    }
                }
            }
        });

        Tinhtong();
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

        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

        // 1. Lấy thông tin tồn kho thực tế
        int productId = selectedProduct.getId();
        Product productInStock = product_service.getProductById(productId).orElse(null);

        if (productInStock == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin tồn kho cho sản phẩm này.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stockQty = productInStock.getSoluong();
        int existingRow = findProductInTable(productId);
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
        stopTableEditing();

        int[] selectedRowsView = tb_Invoice.getSelectedRows();

        if (modelTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng rỗng, không có gì để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (selectedRowsView.length > 0) {
            int[] selectedRowsModel = new int[selectedRowsView.length];
            for (int i = 0; i < selectedRowsView.length; i++) {
                selectedRowsModel[i] = tb_Invoice.convertRowIndexToModel(selectedRowsView[i]);
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc xóa " + selectedRowsModel.length + " sản phẩm đã chọn không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                for (int i = selectedRowsModel.length - 1; i >= 0; i--) {
                    modelTable.removeRow(selectedRowsModel[i]);
                }
                reNumberTableRows();
                Tinhtong();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa trên bảng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

//    private void Save_Invoice() {
//        stopTableEditing();
//
//        if (modelTable.getRowCount() == 0) {
//            JOptionPane.showMessageDialog(this, "Giỏ hàng rỗng. Vui lòng thêm sản phẩm trước khi lưu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        double tongTien = getTongTienValue();
//
//        // Yêu cầu xác nhận từ người dùng
//        int confirm = JOptionPane.showConfirmDialog(this,
//                "Xác nhận lưu đơn hàng với tổng tiền: " + MoneyFormat.format(tongTien) + "?\n(Thao tác này sẽ trừ số lượng tồn kho)",
//                "Xác nhận Lưu & Trừ Tồn Kho", JOptionPane.YES_NO_OPTION);
//
//        if (confirm != JOptionPane.YES_OPTION) {
//            return;
//        }
//
//        // 1. LƯU HÓA ĐƠN CHA (HEADER) VÀ LẤY ID
//        Invoice newInvoice = new Invoice(tongTien, LocalDateTime.now());
//        int newInvoiceId = invoice_service.insert(newInvoice);
//
//        if (newInvoiceId > 0) {
//            boolean detailsOk = true;
//            boolean stockUpdated = true;
//
//            // 2. LƯU CHI TIẾT VÀ TRỪ TỒN KHO TRONG CÙNG VÒNG LẶP
//            for (int i = 0; i < modelTable.getRowCount(); i++) {
//                try {
//                    int productId = (int) modelTable.getValueAt(i, 1);
//
//                    String donGiaFormatted = (String) modelTable.getValueAt(i, 4);
//                    double donGia = MoneyFormat.parse(donGiaFormatted);
//
//                    Object soluongObj = modelTable.getValueAt(i, 5);
//                    int soluong = -1;
//                    try {
//                        soluong = Integer.parseInt(soluongObj.toString());
//                    } catch (NumberFormatException nfe) {
//                        System.err.println("Lỗi chuyển đổi số lượng tại hàng " + i + ": " + soluongObj);
//                        stockUpdated = false; detailsOk = false;
//                        break;
//                    }
//                    if (soluong <= 0) {
//                        JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ (phải > 0) tại hàng " + (i+1), "Lỗi Dữ liệu", JOptionPane.ERROR_MESSAGE);
//                        stockUpdated = false; detailsOk = false;
//                        break;
//                    }
//
//                    // a) LƯU CHI TIẾT HÓA ĐƠN
//                    Invoice_Detail detail = new Invoice_Detail(
//                            0, newInvoiceId, productId, soluong, donGia, 0.0
//                    );
//
//                    if (!invoice_detail_service.insert(detail)) {
//                        detailsOk = false;
//                        System.err.println("Lỗi khi chèn chi tiết hóa đơn ID=" + newInvoiceId + ", sản phẩm ID=" + productId);
//                        break;
//                    }
//
//                    // b) TRỪ SỐ LƯỢNG TỒN KHO TRONG BẢNG sanpham
//                    product_service.updateInvoice(productId, soluong);
//
//                } catch (Exception e) {
//                    stockUpdated = false; // Báo cáo lỗi trừ tồn kho
//                    detailsOk = false; // Báo cáo lỗi chi tiết
//                    System.err.println("LỖI GIAO DỊCH: Xảy ra lỗi khi xử lý hàng " + (i+1) + " trong giỏ hàng. " + e.getMessage());
//                    e.printStackTrace();
//                    break;
//                }
//            }
//
//            // 3. THÔNG BÁO VÀ HOÀN TẤT
//            if (detailsOk && stockUpdated) {
//                JOptionPane.showMessageDialog(this, "Lưu hóa đơn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//
//                // Dọn dẹp giao diện
//                modelTable.setRowCount(0);
//                Tinhtong();
//            } else {
//                // LƯU Ý: Nếu lỗi xảy ra, chỉ cần báo lỗi. Việc Rollback phải được xử lý ở tầng CSDL.
//                JOptionPane.showMessageDialog(this, "LỖI: Xảy ra sự cố khi lưu chi tiết hoặc cập nhật tồn kho. Vui lòng kiểm tra log để biết chi tiết.", "Lỗi Nghiệp Vụ", JOptionPane.ERROR_MESSAGE);
//            }
//
//        } else {
//            // Lỗi này xảy ra khi Invoice_Service.insert() trả về -1
//            JOptionPane.showMessageDialog(this, "Lưu hóa đơn cha thất bại. Vui lòng kiểm tra lại kết nối CSDL và cài đặt ID tự tăng.", "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    // File: Invoice_Create_Frame.java

    private void Save_Invoice() {
        // 1. CHUẨN BỊ VÀ XÁC THỰC
        stopTableEditing(); // Buộc Commit giá trị trước khi Lưu

        if (modelTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng rỗng. Vui lòng thêm sản phẩm trước khi lưu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double tongTien = getTongTienValue();

        // Yêu cầu xác nhận từ người dùng
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận lưu đơn hàng với tổng tiền: " + MoneyFormat.format(tongTien) + "?\n(Thao tác này sẽ trừ số lượng tồn kho)",
                "Xác nhận Lưu & Trừ Tồn Kho", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // 2. LƯU HÓA ĐƠN VÀ CHI TIẾT (Logic CSDL)
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

                    // a) LƯU CHI TIẾT HÓA ĐƠN
                    Invoice_Detail detail = new Invoice_Detail(
                            0, newInvoiceId, productId, soluong, donGia, 0.0
                    );

                    if (!invoice_detail_service.insert(detail)) {
                        detailsOk = false;
                        System.err.println("Lỗi khi chèn chi tiết: " + productId);
                        break;
                    }

                    // b) TRỪ SỐ LƯỢNG TỒN KHO
                    product_service.updateInvoice(productId, soluong);

                } catch (Exception e) {
                    stockUpdated = false;
                    System.err.println("LỖI GIAO DỊCH: Không trừ được tồn kho hoặc lỗi khác. " + e.getMessage());
                    e.printStackTrace();
                    break;
                }
            }

            // 3. THÔNG BÁO VÀ HOÀN TẤT
            if (detailsOk && stockUpdated) {
                JOptionPane.showMessageDialog(this, "Lưu hóa đơn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                // Dọn dẹp giao diện
                modelTable.setRowCount(0);
                Tinhtong();
            } else {
                // Báo cáo lỗi và yêu cầu kiểm tra CSDL
                JOptionPane.showMessageDialog(this, "LỖI: Xảy ra sự cố khi lưu chi tiết hoặc cập nhật tồn kho.", "Lỗi Nghiệp Vụ", JOptionPane.ERROR_MESSAGE);
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


    static class SimpleHeaderRenderer implements TableCellRenderer {
        DefaultTableCellRenderer renderer;

        public SimpleHeaderRenderer(TableCellRenderer defaultRenderer) {
            renderer = (DefaultTableCellRenderer) defaultRenderer;
            renderer.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            c.setFont(c.getFont().deriveFont(Font.BOLD));
            return c;
        }
    }

    static class LeftAlignEditor extends DefaultCellEditor {
        Product_Service productService;

        public LeftAlignEditor(JTextField textField, Invoice_Create_Frame frame, Product_Service service) {
            super(textField);
            this.productService = service;

            textField.setHorizontalAlignment(JTextField.LEFT);
            textField.setFont(new Font("Inter", Font.PLAIN, 16));

            textField.addActionListener(e -> {
                stopCellEditing();
            });
        }

        @Override
        public boolean stopCellEditing() {
            String text = ((JTextField) getComponent()).getText();
            int newQty;

            try {
                newQty = Integer.parseInt(text);
                if (newQty <= 0) {
                    JOptionPane.showMessageDialog(null, "Số lượng phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Số lượng không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // 2. Lấy thông tin tồn kho
            JTable table = (JTable) getComponent().getParent();
            int selectedRow = table.getEditingRow();

            if (selectedRow == -1) {
                return super.stopCellEditing();
            }

            int productId = (int) table.getModel().getValueAt(selectedRow, 1);
            Product productInStock = productService.getProductById(productId).orElse(null);

            if (productInStock == null) {
                JOptionPane.showMessageDialog(null, "Không thể kiểm tra tồn kho.", "Lỗi Tồn Kho", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int stockQty = productInStock.getSoluong();

            // 3. Kiểm tra Tồn Kho
            if (newQty > stockQty) {
                JOptionPane.showMessageDialog(null,
                        "Không đủ tồn kho! Tối đa có thể đặt là: " + stockQty,
                        "Lỗi Tồn Kho", JOptionPane.ERROR_MESSAGE);

                // Đặt lại giá trị tối đa vào ô chỉnh sửa
                ((JTextField) getComponent()).setText(String.valueOf(stockQty));
                return false; // NGĂN KHÔNG CHO THOÁT EDITOR
            }

            return super.stopCellEditing();
        }
    }


    public static void main(String[] args) {
        new Invoice_Create_Frame();
    }
}