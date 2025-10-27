package DAO.Service.ProductSEV;

import DAO.Database_Connection;
import Model.Product;

import java.sql.*;
import java.util.ArrayList;

public class Product_Service {
   public ArrayList<Product> getAll(){
       ArrayList<Product> list = new ArrayList<>();
       String sql = "select * from sanpham";
       try {
           Connection conn =  Database_Connection.getConnection();

           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           while (rs.next()) {
               list.add(new Product(rs.getInt("id"),rs.getString("ten"),rs.getString("donvi"),rs.getDouble("gia"),rs.getInt("soluong")));
           }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
       return list;
   }
   public boolean addProduct(Product pro){
       String checksql = "SELECT COUNT(*) FROM sanpham WHERE LOWER(ten) = LOWER(?)";
       String insertsql = "INSERT INTO sanpham(ten, donvi, gia, soluong) VALUES(?,?,?,?)";
       try (
           Connection conn = Database_Connection.getConnection();

           PreparedStatement check_ps = conn.prepareStatement(checksql);
           PreparedStatement insert_ps = conn.prepareStatement(insertsql)) {

//           Kiểm tra sản phẩm tồn tại chưa
           check_ps.setString(1,pro.getTenSP().trim());
           ResultSet rs = check_ps.executeQuery();
           if (rs.next() && rs.getInt(1) > 0) {
               // Nếu sản phẩm đã tồn tại -> thông báo
               return false;
           }

               insert_ps.setString(1, pro.getTenSP());
               insert_ps.setString(2,pro.getDonvi());
               insert_ps.setDouble(3,pro.getGia());
               insert_ps.setInt(4,pro.getSoluong());

               return insert_ps.executeUpdate()>0;
           }
       catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
   }
   public boolean updateProduct(Product pro) {
       String sql = "UPDATE sanpham SET donvi=?, gia=? WHERE id=?";
       try (Connection conn = Database_Connection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

           ps.setString(1,pro.getDonvi());
           ps.setDouble(2,pro.getGia());
           ps.setInt(3,pro.getId());

           return ps.executeUpdate()>0;
       }
       catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
   }
//   public boolean deleteProduct(int id) {
//       String sql = "DELETE FROM sanpham WHERE id=?";
//       try(Connection conn = Database_Connection.getConnection();
//            PreparedStatement ps = conn.prepareStatement(sql)){
//           ps.setInt(1,id);
//           return ps.executeUpdate()>0;
//       }
//       catch (SQLException e) {
//           e.printStackTrace();
//       }
//       return false;
//   }
   public void updateInvoice(int id, int soLuongMua) {
       String sql = "UPDATE sanpham SET soluong = soluong - ? WHERE id=?";
       try( Connection conn = Database_Connection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
           ps.setInt(1,soLuongMua);
           ps.setInt(2,id);

           ps.executeUpdate();
       }
       catch (SQLException e) {
           e.printStackTrace();
       }
   }
   public void updateImport(int id, int soLuongNhap, double giaBan) {
       String sql = "UPDATE sanpham SET gia = ?, soluong = soluong + ? WHERE id=?";
       try( Connection conn = Database_Connection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
           ps.setDouble(1,giaBan);
           ps.setInt(2,soLuongNhap);
           ps.setInt(3,id);

           ps.executeUpdate();
       }
       catch (SQLException e) {
           e.printStackTrace();
       }
   }
}
