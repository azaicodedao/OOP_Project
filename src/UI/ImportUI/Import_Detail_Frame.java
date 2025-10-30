package UI.ImportUI;

// Trần Thanh Tùng

import Model.Import_Detail;
import DAO.Service.ImportSEV.Import_Detail_Service;
import UI.Base_Frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Import_Detail_Frame extends Base_Frame {

    private JTable tb_ImportDetail;
    private DefaultTableModel modelTable;
    private JButton btn_Back;
    private JLabel lb_TongTien;
    private JTextField txt_TongTien;
    private final Import_Detail_Service detail_service = new Import_Detail_Service();

    private int idPhieuNhap; // 🟩 ID phiếu nhập được truyền vào

    public Import_Detail_Frame(int idPhieuNhap) {
        this.idPhieuNhap = idPhieuNhap;

        setTitle("Chi tiết phiếu nhập #" + idPhieuNhap);
        setSize(950, 600);
        setLocationRelativeTo(null);

        // ======= NORTH - Tiêu đề =======
        JPanel pnlNorth = new JPanel();
        pnlNorth.setBackground(background_color);
        JLabel lb_header = createLabel("CHI TIẾT PHIẾU NHẬP #" + idPhieuNhap);
        lb_header.setFont(new Font("Poppins", Font.BOLD, 26));
        pnlNorth.add(lb_header);
        add(pnlNorth, BorderLayout.NORTH);

        // ======= CENTER - Bảng dữ liệu =======
        String[] columns = {"STT", "ID", "Tên sản phẩm", "Số lượng", "Giá nhập", "Giá bán", "Thành tiền"};
        modelTable = new DefaultTableModel(columns, 0);
        tb_ImportDetail = createTable(modelTable);
        JScrollPane scrollPane = createScrollPane(tb_ImportDetail);
        add(scrollPane, BorderLayout.CENTER);

        // ======= SOUTH - Tổng tiền + Nút quay lại =======
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBackground(background_color);

        // --- Tổng tiền ---
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        pnlTotal.setBackground(background_color);
        lb_TongTien = createLabel("Tổng tiền:");
        txt_TongTien = createTextField();
        txt_TongTien.setEditable(false);
        txt_TongTien.setPreferredSize(new Dimension(150, 30));
        pnlTotal.add(lb_TongTien);
        pnlTotal.add(txt_TongTien);

        // --- Nút quay lại ---
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        pnlButton.setBackground(background_color);
        btn_Back = createButton16("Quay lại");
        pnlButton.add(btn_Back);

        pnlSouth.add(pnlButton, BorderLayout.WEST);
        pnlSouth.add(pnlTotal, BorderLayout.EAST);
        add(pnlSouth, BorderLayout.SOUTH);

        // ======= Sự kiện =======
        btn_Back.addActionListener(e -> {
            dispose();
            new Import_Frame().setVisible(true);
        });

        // ======= Load dữ liệu =======
        LoadData();
    }

    // ======= Load dữ liệu theo id phiếu nhập =======
    private void LoadData() {
        ArrayList<Import_Detail> list = detail_service.getById(idPhieuNhap);
        modelTable.setRowCount(0);
        double tongTien = 0;
        int stt = 1; // 🟩 biến đếm STT

        for (Import_Detail d : list) {
            modelTable.addRow(new Object[]{
                    stt++,                          // STT
                    d.getId(),
                    d.getTenSanPham(),
                    d.getSoluong(),
                    d.getGiaNhap(),
                    d.getGiaBan(),
                    d.getThanhTien()
            });
            tongTien += d.getThanhTien();
        }

        txt_TongTien.setText(String.format("%,.0f", tongTien)); // Định dạng có dấu phẩy
    }

    // Dùng để test riêng frame này
    public static void main(String[] args) {
        new Import_Detail_Frame(1).setVisible(true); // ví dụ mở phiếu nhập có ID = 1
    }
}
