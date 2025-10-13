package UI.ProductUI;

import DAO.Database_Connection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
//Nguyễn Trung Nghĩa
public class Add_Frame extends JFrame{

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    JLabel MaSPLabel = new JLabel("Mã SP:");
    JTextField MaSPTextField = new JTextField();
    JLabel TenSPLabel = new JLabel("Tên SP:");
    JTextField TenSPTextField = new JTextField();
    JLabel DonViLabel = new JLabel("Đơn vị:");
    JTextField DonViTextField = new JTextField();
    JLabel GiaLabel = new JLabel("Giá:");
    JTextField GiaTextField = new JTextField();
    JLabel SoLuongLabel = new JLabel("Số lượng:");
    JTextField TextField = new JTextField();
    JLabel titleLabel = new JLabel("Thêm sản phẩm");
    JButton addButton = new JButton("Thêm SP");

    Font labelFont = new Font("Poppins", Font.PLAIN, 14);
    Font fieldFont = new Font("Poppins", Font.PLAIN, 14);

    public Add_Frame(){
        setTitle("Thêm sản phẩm");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        MaSPLabel.setPreferredSize(new Dimension(120, 30));
        MaSPLabel.setFont(labelFont);

        MaSPTextField.setPreferredSize(new Dimension(250, 30));
        MaSPTextField.setFont(fieldFont);

        TenSPLabel.setPreferredSize(new Dimension(120, 30));
        TenSPLabel.setFont(labelFont);

        TenSPTextField.setPreferredSize(new Dimension(250, 30));
        TenSPTextField.setFont(fieldFont);

        DonViLabel.setPreferredSize(new Dimension(120, 30));
        DonViLabel.setFont(labelFont);

        DonViTextField.setPreferredSize(new Dimension(250, 30));
        DonViTextField.setFont(fieldFont);

        GiaLabel.setPreferredSize(new Dimension(120, 30));
        GiaLabel.setFont(labelFont);

        GiaTextField.setPreferredSize(new Dimension(250, 30));
        GiaTextField.setFont(fieldFont);

        SoLuongLabel.setPreferredSize(new Dimension(120, 30));
        SoLuongLabel.setFont(labelFont);

        TextField.setPreferredSize(new Dimension(250, 30));
        TextField.setFont(fieldFont);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(MaSPLabel, gbc);
        gbc.gridx = 1; panel.add(MaSPTextField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(TenSPLabel, gbc);
        gbc.gridx = 1; panel.add(TenSPTextField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(DonViLabel, gbc);
        gbc.gridx = 1; panel.add(DonViTextField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(GiaLabel, gbc);
        gbc.gridx = 1; panel.add(GiaTextField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(SoLuongLabel, gbc);
        gbc.gridx = 1; panel.add(TextField, gbc);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleLabel.setPreferredSize(new Dimension(300, 40));
        titleLabel.setFont(new Font("Inter", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        addButton.setPreferredSize(new Dimension(120, 40));
        addButton.setFont(new Font("Inter", Font.BOLD, 16));
        panel.add(addButton, gbc);

        addButton.addActionListener(e -> {
            String maSP = MaSPTextField.getText().trim();
            String ten = TenSPTextField.getText().trim();
            String donvi = DonViTextField.getText().trim();
            String giaStr = GiaTextField.getText().trim();
            String soluongStr = TextField.getText().trim();

            if(maSP.isEmpty() || ten.isEmpty() || donvi.isEmpty() || giaStr.isEmpty() || soluongStr.isEmpty()){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            double gia;
            int soluong;
            try{
                gia = Double.parseDouble(giaStr);
                soluong = Integer.parseInt(soluongStr);
            }
            catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "Giá và số lượng phải là số hợp lệ!");
                return;
            }

            if(insertProduct(maSP, ten, donvi, gia, soluong)){
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                MaSPTextField.setText("");
                TenSPTextField.setText("");
                DonViTextField.setText("");
                GiaTextField.setText("");
                TextField.setText("");
            }
            else{
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!");
            }
        });

        panel.setBackground(new Color(0xE0F2F1));

        add(panel);
        setVisible(true);
    }

    private boolean insertProduct(String maSP, String ten, String donvi, double gia, int soluong){
        String checkSql = "SELECT 1 FROM sanpham WHERE ten = ?";
        String insertSql = "INSERT INTO sanpham(maSP, ten, donvi, gia, soluong) VALUES(?,?,?,?,?)";

        try(Connection con = Database_Connection.getConnection()){
            try(PreparedStatement checkStmt = con.prepareStatement(checkSql)){
                checkStmt.setString(1, ten);
                ResultSet rs = checkStmt.executeQuery();
                if(rs.next()){
                    JOptionPane.showMessageDialog(this, "Sản phẩm đã tồn tại!");
                    return false;
                }
            }

            try(PreparedStatement pstmt = con.prepareStatement(insertSql)){
                pstmt.setString(1, maSP);
                pstmt.setString(2, ten);
                pstmt.setString(3, donvi);
                pstmt.setDouble(4, gia);
                pstmt.setInt(5, soluong);
                pstmt.executeUpdate();
                return true;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws Exception{
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        new Add_Frame();
    }
}
