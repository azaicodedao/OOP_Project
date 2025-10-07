public class Product {
    private String maSP;
    private String ten;
    private String donvi;
    private double gia;
    private int soluong;

    public Product(String maSP, String ten, String donvi, double gia, int soluong) {
        this.maSP = maSP;
        this.ten = ten;
        this.donvi = donvi;
        this.gia = gia;
        this.soluong = soluong;
    }

    // Getter - Setter
    public String getMaSP() { return maSP; }
    public String getTen() { return ten; }
    public String getDonvi() { return donvi; }
    public double getGia() { return gia; }
    public int getSoluong() { return soluong; }

    public void setMaSP(String maSP) { this.maSP = maSP; }
    public void setTen(String ten) { this.ten = ten; }
    public void setDonvi(String donvi) { this.donvi = donvi; }
    public void setGia(double gia) { this.gia = gia; }
    public void setSoluong(int soluong) { this.soluong = soluong; }
}