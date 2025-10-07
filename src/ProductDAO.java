import java.sql.*;

public class ProductDAO {

    public boolean existsByName(Connection con, String ten) throws SQLException {
        String sql = "SELECT 1 FROM sanpham WHERE ten = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ten);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public boolean insertProduct(Connection con, String maSP, String ten, String donvi, double gia, int soluong) throws SQLException {
        String sql = "INSERT INTO sanpham(maSP, ten, donvi, gia, soluong) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSP);
            ps.setString(2, ten);
            ps.setString(3, donvi);
            ps.setDouble(4, gia);
            ps.setInt(5, soluong);
            return ps.executeUpdate() > 0;
        }
    }
}