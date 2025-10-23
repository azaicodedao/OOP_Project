package Model;

public class Product {
    private int id;
    private String tenSP;
    private String donvi;
    private double gia;
    private int soluong;

    // Đạng hiển thị trong Table
    public Product(int id, String tenSP, String donvi, double gia, int soluong) {
        this.id = id;
        this.tenSP = tenSP;
        this.donvi = donvi;
        this.gia = gia;
        this.soluong = soluong;
    }

    // Dạng khi tạo trong UI.Product.Add_Frame
    public Product(String tenSP, String donvi) {
        this.tenSP = tenSP;
        this.donvi = donvi;
        this.gia = 0.0;
        this.soluong = 0;

    }


    public int getId() {
        return this.id;
    }


    public String getTenSP() {
        return this.tenSP;
    }

    public String getDonvi() {
        return this.donvi;
    }

    public double getGia() {
        return this.gia;
    }

    public int getSoluong() {
        return this.soluong;
    }

    public String toString() {
        return this.tenSP;
    }

}
