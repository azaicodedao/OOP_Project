package UI.InvoiceUI;

import DAO.Service.InvoiceSEV.Invoice_Service;
import Model.Invoice;
import UI.Base_Frame;
import UI.Home_Frame;
import UI.MoneyFormat;
import com.toedter.calendar.JDateChooser;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class
Invoice_List_Frame extends Base_Frame {
    private JDateChooser date_from, date_to;
    private DefaultTableModel model;
    private JTable table;
    private JButton btn_Refresh, btn_Back;
    private Invoice_Service invoice_service =  new Invoice_Service();

    public Invoice_List_Frame() {
        setTitle("Quản lý hóa đơn");
        // Panel top--------------------------------------------
        JPanel pnl_top = new JPanel();
        pnl_top.setBackground(background_color);
        JLabel title_label = new JLabel("Danh sách hóa đơn");
        title_label.setFont(new Font("Poppins", Font.BOLD, 40));
        title_label.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        pnl_top.add(title_label);

        add(pnl_top, BorderLayout.NORTH);
        // Panel Center------------------------------------------
        JPanel pnl_Center = new JPanel(new BorderLayout());
        pnl_Center.setBackground(background_color);
//        Panel Center_top
        JPanel pnl_Center_top = new JPanel(new FlowLayout(FlowLayout.CENTER,30,5));
        pnl_Center_top.setBackground(background_color);
        JLabel lb_From = createLabel("Từ ngày") ;
        date_from = createDateChooser();

        JLabel lb_To = createLabel("Đến ngày:");
        date_to = createDateChooser();

        JButton btn_Search = createButton16("Lọc");

        pnl_Center_top.add(lb_From);
        pnl_Center_top.add(date_from);
        pnl_Center_top.add(lb_To);
        pnl_Center_top.add(date_to);
        pnl_Center_top.add(btn_Search);

        pnl_Center.add(pnl_Center_top, BorderLayout.NORTH);

//        JScrollPane Table
        String[] column_names = new String[]{"ID","Tổng tiền","Ngày lập"};
        model = new DefaultTableModel(column_names, 0){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = createTable(model);
//        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setCellRenderer(center_Renderer);
        table.getColumnModel().getColumn(1).setCellRenderer(right_Renderer);
        table.getColumnModel().getColumn(2).setCellRenderer(center_Renderer);
        JScrollPane scrollPane = createScrollPane(table);
        pnl_Center.add(scrollPane, BorderLayout.CENTER);
        add(pnl_Center, BorderLayout.CENTER);

//        JPanel bottom -------------------------------------------------------------------
        JPanel pnl_bottom = createPanelBottom();
        JButton btn_Sort = createButton20("Sắp xếp");
        btn_Refresh = createButton20("Làm mới");
        btn_Back = createButton20 ("Quay lại");
        pnl_bottom.add(btn_Sort);
        pnl_bottom.add(btn_Back);
        pnl_bottom.add(btn_Refresh);
        add(pnl_bottom, BorderLayout.SOUTH);
        LoadData();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2 && table.getSelectedRow()!=-1){
                    int row = table.getSelectedRow();
                    int id_Hoadon = (int) table.getValueAt(row,0);
                    ViewDetail(id_Hoadon);
                }
            }
        });
        btn_Sort.addActionListener(e->SortPrice());
        btn_Search.addActionListener(e->SearchData());
        btn_Refresh.addActionListener(e -> LoadData());
        btn_Back.addActionListener(e -> Back());
    }
    private void SearchData(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Lấy dữ liệu từ DateChooser
            Date fromDate = date_from.getDate();
            Date toDate = date_to.getDate();

            // Nếu người dùng nhập tay mà chưa chọn, vẫn đọc được từ ô nhập
            if (fromDate == null) {
                String text = ((JTextField) date_from.getDateEditor().getUiComponent()).getText();
                if (!text.isEmpty()) fromDate = sdf.parse(text);
            }

            if (toDate == null) {
                String text = ((JTextField) date_to.getDateEditor().getUiComponent()).getText();
                if (!text.isEmpty()) toDate = sdf.parse(text);
            }

            if (fromDate == null || toDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập hoặc chọn đầy đủ ngày bắt đầu và kết thúc!");
                return;
            }

            model.setRowCount(0);

//            Chuyen sang LocalDateTime
            LocalDateTime from = fromDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().atStartOfDay();
            LocalDateTime to = toDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59);

            ArrayList<Invoice> invoices = invoice_service.getByDateRange(from.format(formatter), to.format(formatter));
            for (Invoice i : invoices) {
                model.addRow(new Object[]{
                        i.getId(),
                        MoneyFormat.format(i.getTotal()),
                        i.getDate()
                });
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày nhập không hợp lệ! Vui lòng dùng định dạng dd/MM/yyyy.");
        }
    }
    private void LoadData(){
        model.setRowCount(0);
        date_from.setDate(null);
        date_to.setDate(null);
            ArrayList<Invoice> invoices = invoice_service.getAll();
            for (Invoice invoice : invoices) {
                model.addRow(new Object[]{
                        invoice.getId(),
                        MoneyFormat.format(invoice.getTotal()),
                        invoice.getDate()
                });
            }
            if(invoices.isEmpty()){
                JOptionPane.showMessageDialog(this,"Chưa có hóa đơn nào !");
                model.setRowCount(10);
            }
    }
    private void SortPrice(){
//        model.setRowCount(0);
//        date_from.setDate(null);
//        date_to.setDate(null);
//        ArrayList<Invoice> invoices = invoice_service.getAll();
//        Collections.sort(invoices,(a,b)->
//                Double.compare(a.getTotal(), b.getTotal()));
//        for (Invoice invoice : invoices) {
//            model.addRow(new Object[]{
//                    invoice.getId(),
//                    MoneyFormat.format(invoice.getTotal()),
//                    invoice.getDate()
//            });
//        }
//        if(invoices.isEmpty()){
//            JOptionPane.showMessageDialog(this,"Chưa có hóa đơn nào !");
//            model.setRowCount(10);
//        }
        ArrayList<Object[]> row = new ArrayList<>();
        for(int i = 0; i < model.getRowCount(); i++){
            Object[] row_item = new Object[model.getColumnCount()];
            for(int j = 0; j < model.getColumnCount(); j++){
                row_item[j] = model.getValueAt(i, j);
            }
            row.add(row_item);
        }
        Collections.sort(row,(a,b)->Double.compare(Double.parseDouble(a[1].toString().replace("VNĐ","").replace(",","").trim()),Double.parseDouble(b[1].toString().replace("VNĐ","").replace(",","").trim())));
        model.setRowCount(0);
        for(int i = 0; i < row.size(); i++){
            model.addRow(row.get(i));
        }
    }
    private void ViewDetail(int id){
        new Invoice_Detail_Frame(id);
    }
    private void Back(){
        new Home_Frame();
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        new Invoice_List_Frame();
    }

}
