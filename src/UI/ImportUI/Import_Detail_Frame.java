package UI.ImportUI;

// Tran Thanh Tung

import DAO.Service.ImportSEV.Import_Detail_Service;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import Model.Import_Detail;

public class Import_Detail_Frame extends JFrame {

    private JTable tb_import_detail;
    private DefaultTableModel model;
    private JButton btn_Print;
    private JButton btn_Back;
    private Import_Detail_Service importDetailService;
    private int id_Import;

    // --- Constructor ---
    public Import_Detail_Frame(int id_Import) {
        this.id_Import = id_Import;
        importDetailService = new Import_Detail_Service();

        setTitle("Chi tiết phiếu nhập #" + id_Import);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
                "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Giá Nhập", "Thành Tiền"
        });

        tb_import_detail = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tb_import_detail);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel btnPanel = new JPanel();
        btn_Print = new JButton("In / Xuất PDF");
        btn_Back = new JButton("Quay lại");
        btnPanel.add(btn_Print);
        btnPanel.add(btn_Back);
        add(btnPanel, BorderLayout.SOUTH);

        // Load data
        LoadData();

        // Button actions
        btn_Back.addActionListener(e -> Back());
        btn_Print.addActionListener(e -> exportPDF(id_Import));

        setVisible(true);
    }

    // --- Quay lại ---
    private void Back() {
        dispose();
        // Có thể mở lại frame trước đó nếu cần
    }

    // --- Load dữ liệu từ database/service ---
    private void LoadData() {
        model.setRowCount(0); // clear
        ArrayList<Import_Detail> list = importDetailService.getAllByImportId(id_Import);

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu chi tiết phiếu nhập!");
            return;
        }

        for (Import_Detail d : list) {
            model.addRow(new Object[]{
                    d.getId_Product(),
                    "Tên SP #" + d.getId_Product(),
                    d.getSoluong(),
                    d.getGiaNhap(),
                    d.getThanhTien(),
            });
        }
    }

    // --- Xuất PDF ---
    private void exportPDF(int id_Invoice) {
        try {
            JOptionPane.showMessageDialog(this, "Xuất PDF cho phiếu nhập #" + id_Invoice + " thành công!");
            // TODO: gọi class hỗ trợ xuất PDF nếu có (ví dụ: PDFExporter.exportImportDetail(...))
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + e.getMessage());
        }
    }

    // --- Chạy thử ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Import_Detail_Frame(1));
    }
}
