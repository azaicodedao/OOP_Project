package UI.ImportUI;

// Trần Thanh Tùng

import Model.Import_Detail;
import DAO.Service.ImportSEV.Import_Detail_Service;
import UI.Base_Frame;
import UI.MoneyFormat;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class Import_Detail_Frame extends Base_Frame {

    private JTable tb_ImportDetail;
    private DefaultTableModel modelTable;
    private JButton btn_Back;
    private JLabel lb_TongTien;
    private JTextField txt_TongTien;
    private final Import_Detail_Service detail_service = new Import_Detail_Service();

    private int idPhieuNhap; 

    public Import_Detail_Frame(int idPhieuNhap) {
        this.idPhieuNhap = idPhieuNhap;

        setTitle("Chi tiết phiếu nhập #" + idPhieuNhap);
        setSize(900, 580);
        setLocationRelativeTo(null);

        // NORTH - Tiêu đề
        JPanel pnlNorth = new JPanel();
        pnlNorth.setBackground(background_color);
        JLabel lb_header = createLabel("CHI TIẾT PHIẾU NHẬP #" + idPhieuNhap);
        lb_header.setFont(new Font("Poppins", Font.BOLD, 26));
        pnlNorth.add(lb_header);
        add(pnlNorth, BorderLayout.NORTH);

        // CENTER - Bảng dữ liệu
        String[] columns = {"STT", "Tên sản phẩm", "Số lượng", "Giá nhập", "Thành tiền"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tb_ImportDetail = createTable(modelTable);
        tb_ImportDetail.getColumnModel().getColumn(0).setMaxWidth(40);
        tb_ImportDetail.getColumnModel().getColumn(1).setMinWidth(200);
        tb_ImportDetail.getColumnModel().getColumn(2).setMinWidth(70);
        tb_ImportDetail.getColumnModel().getColumn(4).setMinWidth(150);

        // CĂN CHỈNH CỘT
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        // Căn giữa cho STT, Tên SP, Số lượng
        tb_ImportDetail.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tb_ImportDetail.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tb_ImportDetail.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        // Căn phải cho Giá nhập, Thành tiền
        tb_ImportDetail.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tb_ImportDetail.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        // Căn giữa tiêu đề cột
        ((DefaultTableCellRenderer) tb_ImportDetail.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = createScrollPane(tb_ImportDetail);
        add(scrollPane, BorderLayout.CENTER);

        // SOUTH - Tổng tiền + Nút quay lại
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBackground(background_color);

        // Tổng tiền
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        pnlTotal.setBackground(background_color);
        lb_TongTien = createLabel("Tổng tiền:");
        txt_TongTien = createTextField();
        txt_TongTien.setEditable(false);
        txt_TongTien.setPreferredSize(new Dimension(150, 30));
        pnlTotal.add(lb_TongTien);
        pnlTotal.add(txt_TongTien);

        // Nút quay lại
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        pnlButton.setBackground(background_color);
        btn_Back = createButton16("Quay lại");
        pnlButton.add(btn_Back);

        pnlSouth.add(pnlButton, BorderLayout.WEST);
        pnlSouth.add(pnlTotal, BorderLayout.EAST);
        add(pnlSouth, BorderLayout.SOUTH);

        // Sự kiện
        btn_Back.addActionListener(e -> dispose());

        // Load dữ liệu
        LoadData();
    }

    // Load dữ liệu theo id phiếu nhập
    private void LoadData() {
        ArrayList<Import_Detail> list = detail_service.getById(idPhieuNhap);
        modelTable.setRowCount(0);
        double tongTien = 0;
        int stt = 1;

        for (Import_Detail d : list) {
            modelTable.addRow(new Object[]{
                    stt++,
                    d.getTenSanPham(),
                    d.getSoluong(),
                    MoneyFormat.format(d.getGiaNhap()),
                    MoneyFormat.format(d.getThanhTien())
            });
            tongTien += d.getThanhTien();
        }

        txt_TongTien.setText(MoneyFormat.format(tongTien));
    }

    public static void main(String[] args) {
        new Import_Detail_Frame(1).setVisible(true);
    }
}
