package UI.ImportUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.toedter.calendar.JDateChooser;
import java.util.List;
import DAO.Service.ImportSEV.Import_Service;
import Model.Import;
import UI.Home_Frame;

// Nguyễn Trung Nghĩa

public class Import_List_Frame extends JFrame{
    private final JTable importTable;
    private final DefaultTableModel tableModel;
    private final JButton backButton, refreshButton, filterButton;
    private final JDateChooser fromDateChooser, toDateChooser;
    private final Import_Service importService;
    private List<Import> importList;

    public Import_List_Frame(){

        importService = new Import_Service();
        // Tiêu đề
        JLabel titleLabel = new JLabel("DANH SÁCH PHIẾU NHẬP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        // Panel lọc ngày
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        filterPanel.setBackground(new Color(0xE0F2F1));

        JLabel fromLabel = new JLabel("Từ ngày:");
        fromLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        fromDateChooser = createStyledDateChooser();

        JLabel toLabel = new JLabel("Đến ngày:");
        toLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        toDateChooser = createStyledDateChooser();

        filterButton = createStyledButton("Lọc", new Dimension(100, 35));
        filterButton.setFont(new Font("Inter", Font.BOLD, 16));
        filterButton.addActionListener(e -> filterByDateRange());

        JButton clearFilterButton = createStyledButton("Xóa lọc", new Dimension(100, 35));
        clearFilterButton.setFont(new Font("Inter", Font.BOLD, 16));
        clearFilterButton.addActionListener(e -> {
            fromDateChooser.setDate(null); // Xóa ngày đã chọn
            toDateChooser.setDate(null);   // Xóa ngày đã chọn
            loadImportData();
        });

        filterPanel.add(fromLabel);
        filterPanel.add(fromDateChooser);
        filterPanel.add(toLabel);
        filterPanel.add(toDateChooser);
        filterPanel.add(filterButton);
        filterPanel.add(clearFilterButton);

        // Tạo model cho bảng với 3 cột
        String[] columns ={"ID", "Tổng tiền", "Ngày nhập"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };

        // JTable để hiển thị dữ liệu dạng bảng
        importTable = new JTable(tableModel);
        importTable.setFont(new Font("Inter", Font.PLAIN, 16));
        importTable.setForeground(new Color(0x333333));
        importTable.setRowHeight(40);
        importTable.setSelectionBackground(new Color(0xBEE3F8));
        importTable.setSelectionForeground(Color.BLACK);
        importTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 16));
        importTable.getTableHeader().setBackground(new Color(0xDCE6F1));
        importTable.getTableHeader().setForeground(new Color(0x333333));
        ((DefaultTableCellRenderer) importTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Căn chỉnh độ rộng cột
        importTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        importTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        importTable.getColumnModel().getColumn(2).setPreferredWidth(200);

        // Căn giữa hoặc phải cho các cột
        importTable.getColumnModel().getColumn(0).setCellRenderer(new CenterRenderer());
        importTable.getColumnModel().getColumn(1).setCellRenderer(new RightRenderer());
        importTable.getColumnModel().getColumn(2).setCellRenderer(new CenterRenderer());

        // Sự kiện click vào hàng
        importTable.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    int selectedRow = importTable.getSelectedRow();
                    if(selectedRow != -1){
                        showImportDetail(selectedRow);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(importTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 40, 10, 40),
                BorderFactory.createLineBorder(new Color(0xA7B1B7), 2, true)
        ));

        // Tạo panel cho nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(0xE0F2F1));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        backButton = createStyledButton("Quay lại", new Dimension(140, 50));
        backButton.addActionListener(e -> goBack());

        refreshButton = createStyledButton("Làm mới", new Dimension(140, 50));
        refreshButton.addActionListener(e -> loadImportData());

        buttonPanel.add(backButton);
        buttonPanel.add(refreshButton);

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
        centerPanel.setBackground(new Color(0xE0F2F1));

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadImportData();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Quản lý phiếu nhập");
        setSize(900, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0xE0F2F1));
        setVisible(true);
    }

    private JButton createStyledButton(String text, Dimension size){
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, size.height == 50 ? 20 : 14));
        button.setPreferredSize(size);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private JDateChooser createStyledDateChooser() {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Inter", Font.PLAIN, 16));
        dateChooser.setPreferredSize(new Dimension(140, 30));
        dateChooser.setDateFormatString("dd/MM/yyyy");

        dateChooser.setBackground(new Color(0xE0F2F1));
        dateChooser.getCalendarButton().setBackground(new Color(0xE0F2F1));
        dateChooser.getDateEditor().getUiComponent().setBackground(Color.WHITE);

        return dateChooser;
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

        tableModel.setRowCount(0);

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

                tableModel.addRow(new Object[]{importItem.getId(), formattedTotal, formattedDate});
            }

            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Đã tìm thấy " + importList.size() + " phiếu nhập", "Kết quả lọc", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.out.println("Lỗi khi lọc dữ liệu: " + e.getMessage());
        }
    }

    private void loadImportData(){
        tableModel.setRowCount(0);

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

                tableModel.addRow(new Object[]{importItem.getId(), formattedTotal, formattedDate});
            }

        } catch(Exception e){
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.out.println("Lỗi khi tải dữ liệu");
        }
    }

    private void showImportDetail(int selectedRow){
        if(selectedRow >= 0 && selectedRow < importList.size()){
            Import selectedImport = importList.get(selectedRow);
            dispose();
//            new Import_Detail_Frame(selectedImport);
        }
    }

    private void goBack(){
        dispose();
        try{
            new Home_Frame();
        } catch(Exception e){
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Lỗi khi quay lại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.out.println("Lỗi khi quay về Home_Frame");
        }
    }

    // Custom renderer để căn giữa
    static class CenterRenderer extends DefaultTableCellRenderer{
        public CenterRenderer(){
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(!isSelected){
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255)); // Zebra striping
            }
            return c;
        }
    }

    // Custom renderer để căn phải(cho cột tiền)
    static class RightRenderer extends DefaultTableCellRenderer{
        public RightRenderer(){
            setHorizontalAlignment(JLabel.RIGHT);
            setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(!isSelected){
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255));
            }
            return c;
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