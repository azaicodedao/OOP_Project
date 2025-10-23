package Model;

//Tran Thanh Tung

public class Import_Detail {
    private int id_Import;
    private int id_Product;
    private int soluong;
    private double giaNhap;
    private double giaBan;
    private double thanhTien;

    public Import_Detail(int id_Import, int id_Product, int soluong, double giaNhap, double giaBan) {
        this.id_Import = id_Import;
        this.id_Product = id_Product;
        this.soluong = soluong;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.thanhTien = soluong * giaNhap;
    }

    public Import_Detail() {
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

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
        this.thanhTien = this.soluong * this.giaNhap; // cập nhật lại thành tiền
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
        this.thanhTien = this.soluong * this.giaNhap;
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
