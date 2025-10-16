package UI.ProductUI;


import javax.swing.*;
import java.awt.*;
import DAO.Service.ProductSEV.Product_Service;
import Model.Product;

// Nguyễn Trung Nghĩa

public class Add_Frame extends JFrame {

    private final Product_Manage_Frame parent;
    private final Product_Service productService;

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    JLabel titleLabel = new JLabel("Thêm sản phẩm");
    JLabel TenSPLabel = new JLabel("Tên SP:");
    JTextField TenSPTextField = new JTextField();
    JLabel DonViLabel = new JLabel("Đơn vị:");
    JTextField DonViTextField = new JTextField();
    JButton addButton = new JButton("Thêm SP");

    Font labelFont = new Font("Poppins", Font.PLAIN, 16);
    Font fieldFont = new Font("Poppins", Font.PLAIN, 16);

    public Add_Frame(Product_Manage_Frame parent){
        this.parent = parent;
        this.productService = new Product_Service();

        setTitle("Thêm sản phẩm");
        setSize(450, 330);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        TenSPLabel.setPreferredSize(new Dimension(120, 35));
        TenSPLabel.setFont(labelFont);

        TenSPTextField.setPreferredSize(new Dimension(250, 35));
        TenSPTextField.setFont(fieldFont);

        DonViLabel.setPreferredSize(new Dimension(120, 35));
        DonViLabel.setFont(labelFont);

        DonViTextField.setPreferredSize(new Dimension(250, 35));
        DonViTextField.setFont(fieldFont);

        // Layout components
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 35, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleLabel.setPreferredSize(new Dimension(300, 40));
        titleLabel.setFont(new Font("Inter", Font.BOLD, 35));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(TenSPLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(TenSPTextField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(DonViLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(DonViTextField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        addButton.setPreferredSize(new Dimension(120, 40));
        addButton.setFont(new Font("Inter", Font.BOLD, 16));
        panel.add(addButton, gbc);

        addButton.addActionListener(e -> Add_item());

        panel.setBackground(new Color(0xE0F2F1));
        add(panel);
        setVisible(true);
    }

    public void Add_item() {
        String ten = TenSPTextField.getText().trim();
        String donvi = DonViTextField.getText().trim();

        if(ten.isEmpty() || donvi.isEmpty()){
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16)); // Căn chỉnh font chữ OptionPane
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product newProduct = new Product(ten, donvi);

        if(productService.addProduct(newProduct)){
            UIManager.put("OptionPane.messageFont", new Font("Inter", Font.PLAIN, 16));
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
            TenSPTextField.setText("");
            DonViTextField.setText("");

            if(parent != null){
                parent.refreshTable();
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Sản phẩm đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            new Add_Frame(null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}