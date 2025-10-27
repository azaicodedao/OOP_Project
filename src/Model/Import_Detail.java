package Model;

public class Import_Detail {
    private int id;
    private int id_Import;   // id_phieunhap
    private int id_Product;  // id_sanpham
    private String tenSanPham;
    private int soluong;
    private double giaNhap;
    private double giaBan;
    private double thanhTien;

    public Import_Detail() {}

    public Import_Detail(int id_Import, int id_Product, int soluong, double giaNhap, double giaBan) {
        this.id_Import = id_Import;
        this.id_Product = id_Product;
        this.soluong = soluong;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
    }

    // Getters & setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId_Import() {
        return id_Import;
    }
    public void setId_Import(int id_Import) {
        this.id_Import = id_Import;
    }

    public int getId_Product() {
        return id_Product;
    }
    public void setId_Product(int id_Product) {
        this.id_Product = id_Product;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }
    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoluong() {
        return soluong;
    }
    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public double getGiaNhap() {
        return giaNhap;
    }
    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public double getGiaBan() {
        return giaBan;
    }
    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public double getThanhTien() {
        return thanhTien;
    }
    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
}
