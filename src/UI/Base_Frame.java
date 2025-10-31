package UI;

import Model.Product;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class Base_Frame extends JFrame {
    protected Color background_color= new Color(0xE0F2F1);
    protected Color foreground_color=new Color(0x333333);
    protected Color selection_color=new Color(0xBEE3F8);
    protected Font text_font = new Font("Inter", Font.PLAIN, 16);
    protected DefaultTableCellRenderer center_Renderer = new DefaultTableCellRenderer();
    protected DefaultTableCellRenderer right_Renderer = new DefaultTableCellRenderer();
    protected DefaultTableCellRenderer left_Renderer = new DefaultTableCellRenderer();

    public Base_Frame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage((new ImageIcon("image/logo.png")).getImage());
        setSize(900, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(background_color);
        setVisible(true);

    }
    // Giao dien nut 1
    protected JButton createButton16(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Inter", Font.BOLD,16));
        btn.setPreferredSize(new Dimension(100,35));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return btn;
    }
    //  Giao dien nut 2
    protected JButton createButton20(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Inter", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(140,50));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return btn;
    }
    // Giao dien JTextField
    protected JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(180, 30));
        txt.setFont(text_font);
        return txt;
    }
    // Giao dien JDateChooser
    protected JDateChooser createDateChooser() {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setFont(text_font);
        dateChooser.setPreferredSize(new Dimension(140, 30));
        dateChooser.setDateFormatString("dd/MM/yyyy");

        dateChooser.setBackground(background_color);
        dateChooser.getCalendarButton().setBackground(background_color);
        dateChooser.getDateEditor().getUiComponent().setBackground(Color.WHITE);

        // Cho phép nhập tay
        ((JTextField) dateChooser.getDateEditor().getUiComponent()).setEditable(true);

        return dateChooser;
    }
    // Giao dien JLabel
    protected JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(text_font);
        return label;
    }
    // Giao dien Combobox
    protected <T>JComboBox<T> createComboBox() {
        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setFont(text_font);
        comboBox.setPreferredSize(new Dimension(180, 30));
        comboBox.setMaximumRowCount(6);
        return comboBox;
    }
    // Giao dien JTable
    protected JTable createTable(TableModel model) {
        JTable table = new JTable(model);
        center_Renderer.setHorizontalAlignment(JLabel.CENTER);
        right_Renderer.setHorizontalAlignment(JLabel.RIGHT);
        left_Renderer.setHorizontalAlignment(JLabel.LEFT);

        table.setFont(text_font);
        table.setForeground(foreground_color);
        table.setRowHeight(40);
        table.setSelectionBackground(selection_color);
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setFont(text_font);
        table.getTableHeader().setBackground(new Color(0xDCE6F1));
        table.getTableHeader().setForeground(new Color(0x333333));

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER);

        //  Không cho di chuyển cột
        table.getTableHeader().setReorderingAllowed(false);

        return table;
    }
    // Giao dien JScrollPane
    protected JScrollPane createScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(background_color);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 40, 10, 40),
                BorderFactory.createLineBorder(new Color(0xA7B1B7), 2, true)
        ));
        return scrollPane;
    }
    // Giao dien JPanelBottom
    protected JPanel createPanelBottom() {
        JPanel pnl_bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnl_bottom.setBackground(background_color);
        pnl_bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return pnl_bottom;
    }

}
