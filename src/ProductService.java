import java.sql.Connection;
import java.sql.SQLException;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    public boolean addProduct(String maSP, String ten, String donvi, double gia, int soluong) {
        try (Connection con = Database_Connection.getConnection()) {
            // Kiểm tra sản phẩm đã tồn tại chưa
            if (productDAO.existsByName(con, ten)) {
                return false; // đã tồn tại
            }
            return productDAO.insertProduct(con, maSP, ten, donvi, gia, soluong);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}