import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login_Frame extends JFrame {

    JButton loginButton = new JButton("Đăng Nhập");
    JButton registerButton = new JButton("Đăng Ký");
    JLabel IDLabel = new JLabel("Tên Đăng Nhập:");
    JLabel passwordLabel = new JLabel("Mật Khẩu:");
    JTextField IDTextField = new JTextField();
    JPasswordField passwordTextField = new JPasswordField();
    JLabel messageLabel = new JLabel("");
    JLabel titleLabel = new JLabel("ĐĂNG NHẬP");

    Login_Frame(){

        IDLabel.setBounds(78, 130, 120, 35);
        IDLabel.setFont(new Font("Poppins", Font.PLAIN, 15));

        passwordLabel.setBounds(78, 180, 120, 35);
        passwordLabel.setFont(new Font("Poppins", Font.PLAIN, 15));

        titleLabel.setBounds(0, 40, 500, 50);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(JTextField.CENTER);

        messageLabel.setBounds(0, 325, 500, 80);
        messageLabel.setFont(new Font("Poppins", Font.ITALIC, 25));
        messageLabel.setHorizontalAlignment(JTextField.CENTER);

        IDTextField.setBounds(192, 130, 230, 35);
        passwordTextField.setBounds(192, 180, 230, 35);

        loginButton.setBounds(140, 250, 100, 40);
        loginButton.setFont(new Font("Inter", Font.BOLD, 13));
        loginButton.setFocusable(false);
        loginButton.addActionListener(e -> {
            String id = IDTextField.getText().trim();
            String pass = String.valueOf(passwordTextField.getPassword()).trim();

            String query = "SELECT * FROM taikhoan WHERE tendangnhap = ? AND matkhau = ?";
            try(Connection con = Database_Connection.getConnection();
                PreparedStatement stmt = con.prepareStatement(query)){
                stmt.setString(1, id);
                stmt.setString(2, pass);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
//                    messageLabel.setForeground(Color.GREEN);
//                    messageLabel.setText("Đăng nhập thành công");
                    dispose();
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    new Home_Frame();
                }
                else{
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Tên đăng nhập hoặc mật khẩu sai");
                    passwordTextField.setText("");
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Lỗi kết nối database");
            }
        });

        registerButton.setBounds(260, 250, 100, 40);
        registerButton.setFont(new Font("Inter", Font.BOLD, 13));
        registerButton.setFocusable(false);
        registerButton.addActionListener(e -> {
            String id = IDTextField.getText().trim();
            String pass = String.valueOf(passwordTextField.getPassword()).trim();

            if(id.isEmpty() || pass.isEmpty()){
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Vui lòng nhập đủ thông tin");
                return;
            }

            String checkQuery = "SELECT * FROM taikhoan WHERE tendangnhap = ?";
            String insertQuery = "INSERT INTO taikhoan (tendangnhap, matkhau) VALUES (?, ?)";
            try(Connection con = Database_Connection.getConnection()){
                try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, id);
                    ResultSet rs = checkStmt.executeQuery();
                    if(rs.next()){
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText("Tên đăng nhập đã tồn tại");
                        return;
                    }
                }
                try(PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, id);
                    insertStmt.setString(2, pass);
                    insertStmt.executeUpdate();

                    messageLabel.setForeground(Color.GREEN);
                    messageLabel.setText("Đăng ký thành công");
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Đăng ký thất bại");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Đăng nhập");
        setSize(500, 500);
        getContentPane().setBackground(new Color(0xE0F2F1));
        setLayout(null);
        add(titleLabel);
        add(IDLabel);
        add(IDTextField);
        add(passwordLabel);
        add(passwordTextField);
        add(loginButton);
        add(registerButton);
        add(messageLabel);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
