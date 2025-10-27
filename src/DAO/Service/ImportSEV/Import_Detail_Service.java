package DAO.Service.ImportSEV;

import DAO.Database_Connection;
import Model.Import_Detail;
import java.sql.*;
import java.util.ArrayList;

public class Import_Detail_Service {

    // ======= Thêm chi tiết phiếu nhập =======
    public boolean insert(Import_Detail detail) {
        String sql = "INSERT INTO chitietphieunhap (id_phieunhap, id_sanpham, soluongnhap, gianhap, giaban) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database_Connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detail.getId_Import());
            ps.setInt(2, detail.getId_Product());
            ps.setInt(3, detail.getSoluong());
            ps.setDouble(4, detail.getGiaNhap());
            ps.setDouble(5, detail.getGiaBan());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ======= Lấy danh sách chi tiết theo ID phiếu nhập =======
    public ArrayList<Import_Detail> getById(int idPhieuNhap) {
        ArrayList<Import_Detail> list = new ArrayList<>();
        String sql = """
                SELECT c.id, c.id_phieunhap, s.ten AS ten_sanpham, c.id_sanpham,
                       c.soluongnhap, c.gianhap, c.giaban, c.thanhtien
                FROM chitietphieunhap c
                JOIN sanpham s ON c.id_sanpham = s.id
                WHERE c.id_phieunhap = ?
                ORDER BY c.id DESC
                """;

        try (Connection conn = Database_Connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPhieuNhap);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Import_Detail d = new Import_Detail();
                d.setId(rs.getInt("id"));
                d.setId_Import(rs.getInt("id_phieunhap"));
                d.setId_Product(rs.getInt("id_sanpham"));
                d.setTenSanPham(rs.getString("ten_sanpham"));
                d.setSoluong(rs.getInt("soluongnhap"));
                d.setGiaNhap(rs.getDouble("gianhap"));
                d.setGiaBan(rs.getDouble("giaban"));
                d.setThanhTien(rs.getDouble("thanhtien"));
                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
