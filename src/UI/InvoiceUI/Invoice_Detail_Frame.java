package UI.InvoiceUI;

import DAO.Service.InvoiceSEV.Invoice_Detail_Service;
import DAO.Service.InvoiceSEV.Invoice_Service;
import DAO.Service.ProductSEV.Product_Service;
import Model.Invoice;
import Model.Invoice_Detail;
import Model.Product;
import UI.Base_Frame;
import UI.Home_Frame;
import UI.MoneyFormat;
import UI.InvoiceUI.Invoice_List_Frame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class Invoice_Detail_Frame extends Base_Frame {
    private JTable tb_invoice_detail;
    private DefaultTableModel model_detail;
    private JLabel lb_Thanhtien;
    private JButton btn_Back;
    private JLabel lb_Time;

    private final int invoiceId;
    private Invoice_Detail_Service invoice_detail_service = new Invoice_Detail_Service();
    private Invoice_Service invoice_service = new Invoice_Service();
    private Product_Service product_service = new Product_Service();


    public Invoice_Detail_Frame(int id_Invoice) {
        this.invoiceId = id_Invoice;
        setTitle("Chi tiết hóa đơn ID: " + id_Invoice);
        setLayout(new BorderLayout());

        // --- 1. Tiêu đề (NORTH) ---
        JLabel titleLabel = new JLabel("CHI TIẾT HÓA ĐƠN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        titleLabel.setBackground(background_color);
        titleLabel.setOpaque(true);
        add(titleLabel, BorderLayout.NORTH);

        // --- 2. Panel Center_Top (Thông tin Thành tiền & Thời gian) ---
        JPanel pnl_info_top = createPanelBottom();
        pnl_info_top.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));

        // Thành tiền
        JLabel lblThanhtienTitle = createLabel("Thành tiền:"); // SỬ DỤNG createLabel
        lblThanhtienTitle.setFont(new Font("Inter", Font.BOLD, 20));

        lb_Thanhtien = createLabel("0 VNĐ"); // SỬ DỤNG createLabel
        lb_Thanhtien.setFont(new Font("Inter", Font.BOLD, 18));
        lb_Thanhtien.setPreferredSize(new Dimension(150, 30));
        lb_Thanhtien.setHorizontalAlignment(SwingConstants.CENTER);
        lb_Thanhtien.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Thời gian xuất
        JLabel lblThoiGianTitle = createLabel("Thời gian xuất:");
        lblThoiGianTitle.setFont(new Font("Inter", Font.BOLD, 20));

        lb_Time = createLabel("Chưa tải...");
        lb_Time.setFont(new Font("Inter", Font.BOLD, 18));
        lb_Time.setPreferredSize(new Dimension(180, 30));
        lb_Time.setHorizontalAlignment(SwingConstants.CENTER);
        lb_Time.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        pnl_info_top.add(lblThanhtienTitle);
        pnl_info_top.add(lb_Thanhtien);
        pnl_info_top.add(lblThoiGianTitle);
        pnl_info_top.add(lb_Time);

        // --- 3. Bảng (Center) ---
        String[] cols = new String[] { "STT", "Tên sản phẩm", "Đơn vị", "Giá", "Số lượng" };
        model_detail = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tb_invoice_detail = createTable(model_detail); // SỬ DỤNG createTable

        for (int i = 0; i < tb_invoice_detail.getColumnCount(); i++) {
            if (i == 3) {
                tb_invoice_detail.getColumnModel().getColumn(i).setCellRenderer(right_Renderer);
            } else {
                tb_invoice_detail.getColumnModel().getColumn(i).setCellRenderer(center_Renderer);
            }
        }
        tb_invoice_detail.getColumnModel().getColumn(0).setMaxWidth(50);
        tb_invoice_detail.getColumnModel().getColumn(1).setMinWidth(200);
        tb_invoice_detail.getColumnModel().getColumn(2).setMinWidth(70);
        tb_invoice_detail.getColumnModel().getColumn(4).setMinWidth(50);

        // Căn giữa và in đậm tiêu đề
        JTableHeader header = tb_invoice_detail.getTableHeader();
        header.setDefaultRenderer(new SimpleHeaderRenderer(header.getDefaultRenderer()));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = createScrollPane(tb_invoice_detail);

        JPanel main_center_panel = createPanelBottom();
        main_center_panel.setLayout(new BorderLayout());
        main_center_panel.add(pnl_info_top, BorderLayout.NORTH);
        main_center_panel.add(scrollPane, BorderLayout.CENTER);

        add(main_center_panel, BorderLayout.CENTER);

        // --- 4. Panel Bottom (SOUTH) ---
        JPanel pnl_bottom = createPanelBottom(); // SỬ DỤNG createPanel
        pnl_bottom.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 15));
        btn_Back = createButton20("QUAY LẠI"); // SỬ DỤNG createButton20

        // Sự kiện: Quay lại
        btn_Back.addActionListener(e -> BackToList());
        pnl_bottom.add(btn_Back);
        add(pnl_bottom, BorderLayout.SOUTH);

        // Tải dữ liệu khi khung được tạo
        LoadData();
        setVisible(true);
    }

    private void LoadData() {
        model_detail.setRowCount(0);
        try {
            // 1. Lấy thông tin Hóa đơn cha
            Optional<Invoice> invoiceOpt = invoice_service.getInvoiceById(invoiceId);
            if (!invoiceOpt.isPresent()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn ID: " + invoiceId, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Invoice invoice = invoiceOpt.get();

            // 2. Lấy chi tiết hóa đơn
            ArrayList<Invoice_Detail> detailList = invoice_detail_service.getAllByInvoiceId(invoiceId);

            // 3. Hiển thị thông tin tổng quát
            lb_Thanhtien.setText(MoneyFormat.format(invoice.getTotal()));
            lb_Time.setText(invoice.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            // 4. Điền dữ liệu vào bảng
            int stt = 1;
            for (Invoice_Detail detail : detailList) {
                Optional<Product> productOpt = product_service.getProductById(detail.getId_Product());
                String tenSP = productOpt.map(Product::getTenSP).orElse("Không tìm thấy");
                String donvi = productOpt.map(Product::getDonvi).orElse("N/A");
                model_detail.addRow(new Object[]{
                        stt++,
                        tenSP,
                        donvi,
                        MoneyFormat.format(detail.getDongia()),
                        detail.getSoluong()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu chi tiết: " + e.getMessage(), "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void BackToList() {
        dispose();
    }

    static class SimpleHeaderRenderer implements TableCellRenderer {
        DefaultTableCellRenderer renderer;
        private final Color TABLE_HEADER_BACKGROUND = new Color(0xDCE6F1);

        public SimpleHeaderRenderer(TableCellRenderer defaultRenderer) {
            renderer = (DefaultTableCellRenderer) defaultRenderer;
            renderer.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            c.setFont(c.getFont().deriveFont(Font.BOLD));
            c.setBackground(TABLE_HEADER_BACKGROUND);
            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Invoice_Detail_Frame(1);
        });
    }
}