package DAO.Service.InvoiceSEV;

import DAO.Database_Connection;
import Model.Invoice_Detail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Invoice_Detail_Service {
    // xuat hoa don theo id_Invoice
    public ArrayList<Invoice_Detail> getAllByInvoiceId(int id_Invoice){
        ArrayList<Invoice_Detail> list = new ArrayList<>();

        String sql = """
            SELECT id, id_hoadon, id_sanpham, soluong, dongia, thanhtien
            FROM chitiethoadon
            WHERE id_hoadon = ?
        """;

        try (Connection conn = Database_Connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_Invoice);

            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int invoiceId = rs.getInt("id_hoadon");
                    int productId = rs.getInt("id_sanpham");
                    int quantity = rs.getInt("soluong");
                    double price = rs.getDouble("dongia");
                    double totalAmount = rs.getDouble("thanhtien");

                    Invoice_Detail detail = new Invoice_Detail(id, invoiceId, productId, quantity, price, totalAmount);
                    list.add(detail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // them hoa don
    public boolean insert(Invoice_Detail detail){
        String sql = "INSERT INTO chitiethoadon (id_hoadon, id_sanpham, soluong, dongia) VALUES (?, ?, ?, ?)";
        int rowsAffected = 0; // luu tru so luong hang duoc them vao

        try(Connection conn = Database_Connection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            // Lấy giá trị qua các Getters
            ps.setInt(1, detail.getId_Invoice());
            ps.setInt(2, detail.getId_Product());
            ps.setInt(3, detail.getSoluong());
            ps.setDouble(4, detail.getDongia());

            rowsAffected = ps.executeUpdate(); // Thực thi lệnh chèn

        } catch(Exception e){
            e.printStackTrace();
        }

        // Trả về true nếu có ít nhất 1 hàng được chèn thành công
        return rowsAffected > 0;
    }
}