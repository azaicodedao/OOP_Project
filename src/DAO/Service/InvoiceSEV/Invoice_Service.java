package DAO.Service.InvoiceSEV;

import DAO.Database_Connection;
import Model.Invoice;

import java.io.PipedInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class Invoice_Service {
    public ArrayList<Invoice> getAll(){
        ArrayList<Invoice> list = new ArrayList<>();
        String sql = "select * from hoadon ORDER BY ngaylap DESC";
        try (Connection conn = Database_Connection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                int id = rs.getInt("id");
                Double total = rs.getDouble("tongtien");
                LocalDateTime ngaylap = rs.getTimestamp("ngaylap").toLocalDateTime();
                list.add(new Invoice(id, ngaylap, total));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<Invoice> getByDateRange(String from, String to){
        ArrayList<Invoice> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql = """
        SELECT id, ngaylap, tongtien
        FROM hoadon
        WHERE ngaylap BETWEEN ? AND ?
        ORDER BY ngaylap DESC
    """;

        try (Connection conn = Database_Connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.parse(from, formatter)));
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.parse(to, formatter)));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                Double total = rs.getDouble("tongtien");
                LocalDateTime ngaylap = rs.getTimestamp("ngaylap").toLocalDateTime();
                Invoice invoice = new Invoice(id, ngaylap, total);
                list.add(invoice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public int insert(Invoice invoice){
        String sql = "insert into hoadon(tongtien,ngaylap) values(?,?)";
        try(Connection conn = Database_Connection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setDouble(1,invoice.getTotal());
            ps.setTimestamp(2, Timestamp.valueOf(invoice.getDate()));
            ps.executeUpdate();

            // Lấy id vừa insert
            try (ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public Optional<Invoice> getInvoiceById(int id){
        String sql = "SELECT id, ngaylap, tongtien FROM hoadon WHERE id = ?";

        try (Connection conn = Database_Connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int invoiceId = rs.getInt("id");
                    Double total = rs.getDouble("tongtien");
                    LocalDateTime ngaylap = rs.getTimestamp("ngaylap").toLocalDateTime();

                    // Trả về đối tượng Invoice bọc trong Optional
                    return Optional.of(new Invoice(invoiceId, ngaylap, total));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Trả về Optional rỗng nếu không tìm thấy hoặc có lỗi
        return Optional.empty();
    }
}
 
                
                    

     

      
                      