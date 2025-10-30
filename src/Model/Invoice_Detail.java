package Model;

public class Invoice_Detail {
    private int id;
    private int id_Invoice;
    private int id_Product;
    private int Soluong;
    private double Dongia;
    private double ThanhTien;

    public Invoice_Detail(int id, int id_Invoice, int id_Product, int soluong, double dongia, double thanhTien) {
        this.id = id;
        this.id_Invoice = id_Invoice;
        this.id_Product = id_Product;
        Soluong = soluong;
        Dongia = dongia;
        ThanhTien = thanhTien;
    }

    public int getId() {
        return id;
    }

    public int getId_Invoice() {
        return id_Invoice;
    }

    public int getId_Product() {
        return id_Product;
    }

    public int getSoluong() {
        return Soluong;
    }

    public double getDongia() {
        return Dongia;
    }

    public double getThanhTien() {
        return ThanhTien;
    }
}

