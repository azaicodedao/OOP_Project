package DAO.Service.ImportSEV;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import Model.Import;
import DAO.Database_Connection;

public class Import_Service{

    // Lấy toàn bộ danh sách phiếu nhập
    public ArrayList<Import> getAll(){
        ArrayList<Import> list = new ArrayList<>();
        String sql = "SELECT * FROM phieunhap ORDER BY ngaynhap DESC";

        try(Connection con = Database_Connection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                double total = rs.getDouble("tongtien");
                Timestamp ts = rs.getTimestamp("ngaynhap");
                LocalDateTime ngaynhap = ts.toLocalDateTime();

                Import imp = new Import(total, ngaynhap);
                imp.setId(rs.getInt("id"));
                list.add(imp);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    // Thêm một phiếu nhập mới
    public boolean insert(Import imp){
        String sql = "INSERT INTO phieunhap(tongtien, ngaynhap) VALUES(?, ?)";
        try(Connection con = Database_Connection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setDouble(1, imp.getTotal());
            pstmt.setTimestamp(2, Timestamp.valueOf(imp.getNgayNhap()));
            pstmt.executeUpdate();
            return true;

        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Lọc phiếu nhập theo khoảng ngày
    public ArrayList<Import> getByDateRange(String from, String to){
        ArrayList<Import> list = new ArrayList<>();
        String sql = "SELECT * FROM phieunhap WHERE ngaynhap BETWEEN ? AND ? ORDER BY ngaynhap DESC";

        try(Connection con = Database_Connection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, from);
            pstmt.setString(2, to);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                double total = rs.getDouble("tongtien");
                Timestamp ts = rs.getTimestamp("ngaynhap");
                LocalDateTime ngaynhap = ts.toLocalDateTime();

                Import imp = new Import(total, ngaynhap);
                imp.setId(rs.getInt("id"));
                list.add(imp);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
