package DAO.Service.ImportSEV;

import DAO.Database_Connection;
import Model.Import_Detail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;

//Trần Thanh Tùng

public class Import_Detail_Service {

    // Lấy danh sách chi tiết nhập theo id phiếu nhập
    public ArrayList<Import_Detail> getAllByImportId(int id_Import) {
        // TODO: lấy dữ liệu thật từ DB
        // Tạm thời giả lập dữ liệu test:
        ArrayList<Import_Detail> list = new ArrayList<>();
        String sql = "SELECT * FROM chitietphieunhap WHERE id_phieunhap = ?";

        try (Connection con = Database_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_Import);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Import_Detail detail = new Import_Detail();
                detail.setId_Import(rs.getInt("id_phieunhap"));
                detail.setId_Product(rs.getInt("id_sanpham"));
                detail.setSoluong(rs.getInt("soluongnhap"));
                detail.setGiaNhap(rs.getDouble("gianhap"));
                detail.setGiaBan(rs.getDouble("giaban"));
                detail.setThanhTien(rs.getDouble("thanhtien"));
                list.add(detail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm mới chi tiết phiếu nhập
    public boolean insert(Import_Detail detail) {
        String sql = "INSERT INTO chitietphieunhap (id_phieunhap, id_sanpham, soluongnhap, gianhap, giaban) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Database_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detail.getId_Import());
            ps.setInt(2, detail.getId_Product());
            ps.setInt(3, detail.getSoluong());
            ps.setDouble(4, detail.getGiaNhap());
            ps.setDouble(5, detail.getGiaBan());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
