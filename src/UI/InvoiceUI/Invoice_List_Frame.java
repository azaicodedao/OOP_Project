package UI.InvoiceUI;

import DAO.Service.InvoiceSEV.Invoice_Service;
import Model.Invoice;
import UI.Home_Frame;
import UI.MoneyFormat;
import com.toedter.calendar.JDateChooser;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class
Invoice_List_Frame extends javax.swing.JFrame {
    private JDateChooser date_from, date_to;
    private DefaultTableModel model;
    private JTable table;
    private JButton btn_Refresh, btn_Back;
    private Invoice_Service invoice_service =  new Invoice_Service();

    public Invoice_List_Frame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(600, 500);
        setResizable(false);
        setVisible(true);
        // Panel top--------------------------------------------
        JPanel pnl_top = new JPanel();
        JLabel title_label = new JLabel("📋 Danh sách hóa đơn");
        pnl_top.add(title_label);

        add(pnl_top, BorderLayout.NORTH);
        // Panel Center------------------------------------------
        JPanel pnl_Center = new JPanel(new BorderLayout());
//        Panel Center_top
        JPanel pnl_Center_top = new JPanel(new FlowLayout(2));
        JLabel lb_From =  new JLabel("From:");
        date_from = new JDateChooser();
        date_from.setDateFormatString("dd/MM/yyyy");

        JLabel lb_To = new JLabel("To:");
        date_to = new JDateChooser();
        date_to.setDateFormatString("dd/MM/yyyy");

        // Cho phép nhập tay
        ((JTextField) date_from.getDateEditor().getUiComponent()).setEditable(true);
        ((JTextField) date_to.getDateEditor().getUiComponent()).setEditable(true);

        JButton btn_Search = new JButton("Search");

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
        table = new JTable(model);
        //  Không cho di chuyển cột
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        pnl_Center.add(scrollPane, BorderLayout.CENTER);
        add(pnl_Center, BorderLayout.CENTER);

//        JPanel bottom -------------------------------------------------------------------
        JPanel pnl_bottom = new JPanel();
        btn_Refresh = new JButton("Refresh");
        btn_Back = new JButton("Home");
        pnl_bottom.add(btn_Refresh);
        pnl_bottom.add(btn_Back);
        add(pnl_bottom, BorderLayout.SOUTH);
        LoadData();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2 && table.getSelectedRow()!=-1){
                    int row = table.getSelectedRow();
                    int id_Hoadon = (int) table.getValueAt(row,0);
//                    ViewDetail(id_Hoadon);
                }
            }
        });
        btn_Search.addActionListener(e->SearchData());
        btn_Refresh.addActionListener(e -> LoadData());
        btn_Back.addActionListener(e -> Back());
    }
    private void SearchData(){
        model.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
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

//            Chuyen sang LocalDate
            LocalDate from = fromDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate to = toDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            ArrayList<Invoice> invoices = invoice_service.getByDateRange(from.toString(), to.toString());
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
//    private void ViewDetail(int id){
//        new Invoice_Detai_Frame(id);
//    }
    private void Back(){
        new Home_Frame();
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        new Invoice_List_Frame();
    }

}
