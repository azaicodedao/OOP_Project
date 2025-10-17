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
import java.util.ArrayList;

public class
Invoice_List_Frame extends javax.swing.JFrame {
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
        JPanel pnl_Center_top = new JPanel();
        JLabel lb_From =  new JLabel("From:");
        JDateChooser date_from = new JDateChooser();
        JLabel lb_x = new JLabel("->");
        JLabel lb_To = new JLabel("To:");
        JDateChooser date_to = new JDateChooser();


        pnl_Center_top.add(lb_From);
        pnl_Center_top.add(date_from);
        pnl_Center_top.add(lb_x);
        pnl_Center_top.add(lb_To);
        pnl_Center_top.add(date_to);

        pnl_Center.add(pnl_Center_top, BorderLayout.NORTH);

//        JScrollPane Table
        String[] column_names = new String[]{"ID","Tổng tiền","Ngày lập"};
        model = new DefaultTableModel(column_names, 0){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);

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
        btn_Refresh.addActionListener(e -> LoadData());
        btn_Back.addActionListener(e -> Back());
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
