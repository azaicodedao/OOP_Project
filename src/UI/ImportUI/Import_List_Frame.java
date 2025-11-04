package UI.ImportUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import UI.Base_Frame;
import com.toedter.calendar.JDateChooser;

import java.util.ArrayList;
import DAO.Service.ImportSEV.Import_Service;
import Model.Import;
import UI.Home_Frame;

// Nguyễn Trung Nghĩa

public class Import_List_Frame extends Base_Frame {
    private  JTable table;
    private  DefaultTableModel model;
    private  JButton btn_Back, btn_Refresh, btn_Filter;
    private  JDateChooser fromDateChooser, toDateChooser;
    private  Import_Service importService;
    private  ArrayList<Import> importList;


    public Import_List_Frame(){
        setTitle("Quản lý phiếu nhập");

        importService = new Import_Service();
        // Tiêu đề
        JLabel titleLabel = new JLabel("DANH SÁCH PHIẾU NHẬP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        // Panel lọc ngày
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        filterPanel.setBackground(background_color);

        JLabel fromLabel = createLabel("Từ ngày:");
        fromDateChooser = createDateChooser();

        JLabel toLabel = createLabel("Đến ngày:");
        toDateChooser = createDateChooser();

        btn_Filter = createButton16("Lọc");
        btn_Filter.addActionListener(e -> filterByDateRange());

        filterPanel.add(fromLabel);
        filterPanel.add(fromDateChooser);
        filterPanel.add(toLabel);
        filterPanel.add(toDateChooser);
        filterPanel.add(btn_Filter);

        // Tạo model cho bảng với 3 cột
        String[] columns ={"ID", "Tổng tiền", "Ngày nhập"};
        model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };

        // JTable để hiển thị dữ liệu dạng bảng
        table = createTable(model);
        // Căn chỉnh độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);

        // Căn giữa hoặc phải cho các cột
        table.getColumnModel().getColumn(0).setCellRenderer(center_Renderer);
        table.getColumnModel().getColumn(1).setCellRenderer(center_Renderer);
        table.getColumnModel().getColumn(2).setCellRenderer(center_Renderer);

        // Sự kiện click vào hàng
        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    int selectedRow = table.getSelectedRow();
                    if(selectedRow != -1){
                        Object idValue = table.getValueAt(selectedRow, 0);
                        int selectedId = Integer.parseInt(idValue.toString());
                        showImportDetail(selectedId);
                    }
                }
            }
        });

        JScrollPane scrollPane = createScrollPane(table);
        // Tạo panel cho nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(0xE0F2F1));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        btn_Back = createButton20("Quay lại");
        btn_Back.addActionListener(e -> Back());

        btn_Refresh = createButton20("Làm mới");
        btn_Refresh.addActionListener(e -> LoadData());

        buttonPanel.add(btn_Back);
        buttonPanel.add(btn_Refresh);

        // Layout chính
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.CENTER); // Đặt filterPanel ở giữa
        add(scrollPane, BorderLayout.CENTER); // ScrollPane sẽ chiếm phần còn lại
        add(buttonPanel, BorderLayout.SOUTH);

        // Sửa lại layout để filterPanel nằm trên scrollPane
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBackground(background_color);

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        LoadData();

    }

    // Lọc theo khoảng ngày
    private void filterByDateRange() {
        // Kiểm tra xem đã chọn ngày chưa
        if (fromDateChooser.getDate() == null || toDateChooser.getDate() == null) {
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy ngày từ JDateChooser
        java.util.Date fromUtilDate = fromDateChooser.getDate();
        java.util.Date toUtilDate = toDateChooser.getDate();

        // Chuyển từ java.util.Date sang java.sql.Date
        java.sql.Date fromSqlDate = new java.sql.Date(fromUtilDate.getTime());
        java.sql.Date toSqlDate = new java.sql.Date(toUtilDate.getTime());

        // Chuyển sang LocalDate để format
        LocalDate fromDate = fromSqlDate.toLocalDate();
        LocalDate toDate = toSqlDate.toLocalDate();

        // Kiểm tra ngày bắt đầu không lớn hơn ngày kết thúc
        if (fromDate.isAfter(toDate)) {
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không thể lớn hơn ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Format sang định dạng database (yyyy-MM-dd)
        DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDB = fromDate.format(dbFormatter);
        String toDB = toDate.format(dbFormatter);

        model.setRowCount(0);

        try {
            importList = importService.getByDateRange(fromDB, toDB);

            if (importList.isEmpty()) {
                UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
                JOptionPane.showMessageDialog(this, "Không có phiếu nhập nào trong khoảng thời gian này!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Import importItem : importList) {
                String formattedTotal = String.format("%,.0f VNĐ", importItem.getTotal());
                String formattedDate = importItem.getNgayNhap().toString();

                model.addRow(new Object[]{importItem.getId(), formattedTotal, formattedDate});
            }

            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Đã tìm thấy " + importList.size() + " phiếu nhập", "Kết quả lọc", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.out.println("Lỗi khi lọc dữ liệu: " + e.getMessage());
        }
    }

    private void LoadData(){
        model.setRowCount(0);
        fromDateChooser.setDate(null); // Xóa ngày đã chọn
        toDateChooser.setDate(null);   // Xóa ngày đã chọn

        try{
            importList = importService.getAll();

            if(importList.isEmpty()){
                UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16)); // Căn chỉnh font chữ OptionPane
                JOptionPane.showMessageDialog(this, "Không có phiếu nhập nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for(Import importItem : importList){
                String formattedTotal = String.format("%,.0f VNĐ", importItem.getTotal());
                String formattedDate = importItem.getNgayNhap().toString();

                model.addRow(new Object[]{importItem.getId(), formattedTotal, formattedDate});
            }

        } catch(Exception e){
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.out.println("Lỗi khi tải dữ liệu");
        }
    }

    private void showImportDetail(int selectedId){
        new Import_Detail_Frame(selectedId);
    }

    private void Back(){
        dispose();
        try{
            new Home_Frame();
        } catch(Exception e){
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Lỗi khi quay lại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.out.println("Lỗi khi quay về Home_Frame");
        }
    }


    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            try{
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch(Exception e){
                e.printStackTrace();
            }
            new Import_List_Frame();
        });
    }
}